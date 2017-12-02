/**
 * Main class of the program
 *
 * <p>
 * This class is the center class of the program. It instantiates all the objects,
 * keeps track of them, paints them, and controls the game.
 * <p>
 * ADSB PS11: old.Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

/*
CLASS: old.Asteroids
DESCRIPTION: Extending old.Game, old.Asteroids is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

class Tanks extends Game implements KeyListener {
    public Tank tank; //ship instance

    private static int width = 800, height = 600;
    private final Point[] defaultBullet = new Point[] {
            new Point(0, 0),
            new Point(0, 5),
            new Point(5, 1),
            new Point(1, 0)
    }; //bullet shape

    private ArrayList <Polygon> objects; //holds all the objects to be painted
    private final Random gen = new Random(); //generates random numbers

    private int frame = 0, FLICKER_FRAME_LIMIT = 10, frametick = 0, FRAME_TICK_LIMIT = 7; //frames for ship flickering at the beginning

    private BufferedImage tankImg1, tankImg2, tankImg3, sqImg1, botImg1; //images for graphics
    private BufferedImage[] tankImgs, botImgs, sqImgs; //lists of images
    public int level = 0; //to keep track of how far the player is

    private int health; //lives
    private int xp = 0; //score
    private ArrayList<int[]> scoreLabels = new ArrayList<>(); //keeps track of the score labels when you hit an asteroid

    /**
     * Main game constructor
     * This instantiates all the levels, and images needed to run the game, then
     * starts it off.
     * @throws IOException
     */
    public Tank() throws IOException {
        //set up window
        super("Tanks!", width, height);
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(this);

        //set up images
        tankImg1 = ImageIO.read(new File("images/ship1.png"));
        tankImg2 = ImageIO.read(new File("images/ship3.png"));
        tankImg3 = ImageIO.read(new File("images/ship4.png"));


        sqImg1 = ImageIO.read(new File("images/asteroid1.png"));

        botImg1 = ImageIO.read(new File("images/ufo_damage0.png"));

        //make images smaller
        BufferedImage[] imgs = new BufferedImage[] {tankImg1, tankImg2, tankImg3, sqImg1, botImg1};
        for (int i = 0; i < imgs.length; i++) {
            AffineTransform at = new AffineTransform();
            at.scale(0.5, 0.5);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            imgs[i] = op.filter(imgs[i],  null);
        }

        tankImgs = new BufferedImage[] {
                imgs[0],
                imgs[1],
                imgs[2]
        };

        for (int i = 0; i < tankImgs.length; i++) { //make ship images half the size
            AffineTransform at = new AffineTransform();
            at.scale(0.5, 0.5);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            tankImgs[i] = op.filter(tankImgs[i],  null);
        }

        sqImgs = new BufferedImage[] {
                imgs[3],
        };
        botImgs = new BufferedImage[] {
                imgs[4],
        };

        for (int i = 0; i < botImgs.length; i++) { //make ufo images half the size
            AffineTransform at = new AffineTransform();
            at.scale(0.5, 0.5);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            botImgs[i] = op.filter(botImgs[i],  null);
        }
        level = 1;
        health = 50 * level;

        initWave(); //press go
    }


    /**
     * Make the ship
     * This method makes a new ship, centered at 300, 400.
     * @return void nothing
     */
    private void initShip() {
        frame = 0; //flickering setup
        frametick = 0;
        tank = new Tank(Tank.generateShape(level), new Point(300, 400), 0.0, width, height, tankImgs[level]); //make ship
        this.addKeyListener(tank); //listen for arrow keys
        //for (Polygon p: objects) if (p instanceof EvilShip) ((EvilShip)p).setShip(ship); //put target of UFOs as ship
        objects.add(tank); //add it to the objects list
    }

    /**
     * Make the asteroids
     * This method sets up the asteroids at random locations
     * and adds them to the objects list.
     * @return void nothing
     */
    private void initAsteroids() {
        for (int i = 0; i < astN[wave]; i++) { //up to the number of asteroids in the wave
            int asteroidType = gen.nextInt(2); //type
            Point[] pointArr = Asteroid.generateShape(asteroidType); //shape based on type
            Point pos = new Point(gen.nextInt(width), gen.nextInt(height)); //random location
            double r = gen.nextDouble() * 360; //random rotation
            int w = (int) pointArr[pointArr.length - 1].getX(); //width and height
            int h = (int) pointArr[pointArr.length - 1].getY();
            Point[] pA2 = new Point[pointArr.length - 1]; //delete width and height locator
            for (int j = 0; j < pointArr.length - 1; j++) pA2[j] = pointArr[j].clone();
            asteroidImg = asteroidImgs[asteroidType];
            Asteroid a = new Asteroid(pA2, pos, r, width, height, w, h, asteroidImg, asteroidType); //instantiate asteroid
            objects.add(a); //add asteroid to objects
        }
    }

    /**
     * Make the UFOs
     * This method makes the UFOs at random locations
     * and adds them to the objects list.
     * @return void nothing
     */
    private void initEvilShips() {
        for (int i = 0; i < shipN[wave]; i++) { //for each UFO in the wave
            Point[] pointArr = EvilShip.generateShape(); //random shape
            Point pos = new Point(gen.nextInt(width), gen.nextInt(height)); //random location
            EvilShip es = new EvilShip(pointArr, pos, width, height, evilShipImgs, ship); //instantiate ship
            objects.add(es); //add to objecst
        }
    }

    /**
     * Make everything in the wave
     * This method sets up the wave, making
     * the ship, UFOs, and asteroids.
     * @return void nothing
     */
    private void initWave() {
        astN = levels.get(level)[0];
        shipN = levels.get(level)[1];
        guns = levels.get(level)[2];
        objects = new ArrayList <Polygon>();
        initEvilShips();
        initShip();
        initAsteroids();
        waveFrame = 0;
        levelFrame = 0;
    }

    /**
     * Destroys everything on the screen
     * This destroys everything on the screen by
     * setting the lists of objects to null, deleting
     * all the references to objects on the screen.
     * Notably this does not affect the ship as the
     * ship is also hosted in a private field.
     * @return void nothing
     */
    private void deInitWave() {
        objects = null;
    }

    /**
     * Control center paint method
     * This method flickers the ship and decides
     * what to display on the screen based on the
     * gameState field.
     * @param brush the brush to draw the objects with
     * @return void nothing
     */
    public void paint(Graphics brush) {
        brush.setColor(Color.black); //draw background
        brush.fillRect(0, 0, width, height);

        lives = Math.max(lives,  0); //no negative lives
        if (gameState == PLAY) { //ship only flickers if playing
            boolean flicker, flickering;
            if (frame > FLICKER_FRAME_LIMIT) {
                flicker = false;
                flickering = false;
            } else if (frame % 2 == 0) {
                flicker = false;
                flickering = true;

            } else {
                flicker = true;
                flickering = true;
            }
            if (FRAME_TICK_LIMIT > 0 && frametick % FRAME_TICK_LIMIT == 0) frame++;
            frametick++;

            mainPaint(brush, flicker, false, flickering); //paint the objects
        } else {
            switch (gameState) { //deploy methods based on game state
                case START:
                    startScreen(brush);
                    break;
                case LEVEL:
                    levelScreen(brush);
                    break;
                case WAVE:
                    waveScreen(brush);
                    break;
                case DEAD:
                    mainPaint(brush, true, true, true);
                    break;
            }
        }
    }

    /**
     * Draws the start screen
     * Draws the start text on the screen.
     * Waits for keyboard response, then
     * continues.
     * @param brush the brush to draw the text with
     * @return void nothing
     */
    public void startScreen(Graphics brush) {
        brush.setColor(Color.white);
        brush.drawString("ASTEROIDS", 300, 300); //draw text
        brush.drawString("Press any key to begin", 300, 325);
    }

    /**
     * Draws the level screen
     * Draws the level text on the screen.
     * Waits for a set time limit (50 frames), then
     * continues.
     * @param brush the brush to draw the text with
     * @return void nothing
     */
    public void levelScreen(Graphics brush) {
        brush.setColor(Color.white);
        brush.drawString("Level " + (level + 1), 300, 300);

        levelFrame++;
        if (levelFrame > 50) gameState = WAVE;
    }

    /**
     * Draws the wave screen
     * Draws the wave text on the screen.
     * Waits for a set time limit (50 frames), then
     * continues.
     * @param brush the brush to draw the text with
     * @return void nothing
     */
    public void waveScreen(Graphics brush) {
        brush.setColor(Color.white);
        brush.drawString("Level " + (level + 1), 300, 300);
        brush.drawString("Wave " + (wave + 1), 300, 325);
        waveFrame++;
        if (waveFrame > 50) gameState = PLAY;
    }

    /**
     * Respawns the ship
     * Destroys the current ship, then makes
     * a new one, decreasing score by 1000.
     * @return void nothing
     */
    public void respawn() {
        ship = null;
        initShip();
        score -= 1000;
    }

    /**
     * Main paint method
     * This method paints all the objects and score labels.
     * It also checks for collisions between objects and processes
     * bullet firing, object motion, and game procedure.
     * @param brush brush to paint the objects with
     * @param flicker whether to paint the ship or not
     * @param dead if the ship is dead
     * @param flickering whether or not the ship can die
     * @return void nothing
     */
    public void mainPaint(Graphics brush, boolean flicker, boolean dead, boolean flickering) {
        brush.setColor(Color.white); //statistic text
        brush.drawString("Level: " + (level + 1), 10, 20);
        brush.drawString("Wave: " + (wave + 1), 10, 45);
        brush.drawString("Lives: " + lives, 10, 70);
        brush.drawString("Score: " + score, 10, 95);

        boolean remove1, remove2, eb1, b1, es1, s1, eb2, b2, es2, s2, a1, a2, r1, r2, collides, safe1, safe2, respawn = false; //type booleans
        int rubble = 0, evilShips = 0, asteroids = 0; //numbers of things
        for (int i = 0; i < objects.size(); i++) { //iterate through objects
            remove1 = false; //whether to remove the object

            Polygon o1 = objects.get(i); //the object

            //get the type of the object
            eb1 = o1 instanceof EvilBullet;
            es1 = o1 instanceof EvilShip;
            s1 = o1 instanceof Ship && !es1;
            a1 = o1 instanceof Asteroid;
            r1 = o1 instanceof Rubble;
            b1 = o1 instanceof Bullet && !eb1 && !r1;
            safe1 = s1 && flickering; //if the ship can be collided with

            if ((!s1 || (s1 && !flicker))) { //paint and move the object
                if (!es1 || (es1 && !flickering)) o1.move();
                brush.setColor(o1.color);
                o1.paint(brush);
            }

            for (int j = 0; j < objects.size(); j++) { //iterate through again
                remove2 = false; //whether to remove the second object

                Polygon o2 = objects.get(j);

                //get type of second object
                eb2 = o2 instanceof EvilBullet;
                es2 = o2 instanceof EvilShip;
                s2 = o2 instanceof Ship && !es2;
                a2 = o2 instanceof Asteroid;
                r2 = o2 instanceof Rubble;
                b2 = o2 instanceof Bullet && !eb2 && !r2;
                safe2 = s2 && flickering;

                if (!o1.equals(o2)) { //don't process same object
                    if (!r1 && !r2 && o1.collides(o2)) { //rubble can't collide
                        if ((eb1 || eb2) && (es1 && es2)) collides = false; //evilBullets can't collide with evilShips
                        else if ((b1 || b2) && (s1 || s2)) collides = false; //bullets can't collide with ships
                        else if (safe1 || safe2) collides = false; //don't process collision if ship is safe
                        else collides = true;
                    } else {
                        collides = false;
                    }

                    if (collides) { //process collision
                        remove1 = true; //destroy both objects
                        remove2 = true;

                        for (Polygon d: o1.destroy()) objects.add(d);
                        for (Polygon d: o2.destroy()) objects.add(d);
                        if ((s1 || s2) && !flicker && !respawn) { //process life loss
                            if (--lives > 0) {
                                respawn = true;
                            } else {
                                gameState = DEAD;
                                dead = true;
                            }
                        }

                        //only score objects if scorable and hit by a bullet
                        if (a1 && b2) score((Asteroid) o1);
                        else if (a2 && b1) score((Asteroid) o2);
                        else if (es1 && b2) score((EvilShip) o1);
                        else if (es2 && b1) score((EvilShip) o2);
                    }
                }

                if (remove2) { //remove the second object
                    objects.remove(o2);
                    j--;
                }
            }

            if (o1 instanceof Bullet) { //process bullet motion
                Bullet bo1 = (Bullet) o1;
                if (bo1.counter > bo1.lifetime) { //if bullet is dead
                    bo1.destroy();
                    objects.remove(bo1);
                    i--;
                }
            }

            if (s1 && !respawn) { //process ship
                Ship so1 = (Ship) o1;
                if (!flickering && !dead && so1.shooting()) {
                    if (so1.shots < ship.MAX_SHOTS) {
                        Bullet b;
                        for (int gun: guns) { //for each gun
                            b = new Bullet(defaultBullet, o1.getPoints()[gun].clone(), so1.rotation, width, height, null, Math.abs(so1.accel.x + so1.accel.y)); //shoot bullet
                            score -= 50;
                            objects.add(b);
                        }
                        so1.shootOff();
                        so1.shots++;
                    }
                }
            }

            if (es1) { //process UFOs
                EvilShip eo1 = (EvilShip) o1;

                if (eo1.shoot) {
                    EvilBullet eb = new EvilBullet(defaultBullet, eo1.getPoints()[1].clone(), eo1.rotation, width, height, null, Math.abs(eo1.accel.x + eo1.accel.y));
                    objects.add(eb);
                }
            }

            if (remove1) { //remove first object
                objects.remove(o1);
                i--;
            }
            if (!remove1) { //count object types
                if (a1) asteroids++;
                else if (es1) evilShips++;
                else if (r1) rubble++;
            }
        }

        if (respawn) respawn(); //respawn ship

        if (dead && rubble == 0) { //process death
            brush.setColor(Color.white);
            brush.drawString("GAME OVER", 400, 300);
        } else if (asteroids == 0 && evilShips == 0 && !dead) {
            if (!won && ++wave < astN.length) { //increase wave
                if (lives > 0) {
                    gameState = LEVEL;
                    deInitWave();
                    initWave();
                    levelFrame = 0;
                } else {
                    gameState = DEAD;
                    dead = true;
                }
            } else {
                if (!won && ++level < levels.size()) { //increase level
                    if (lives > 0) {
                        gameState = LEVEL;
                        wave = 0;
                        deInitWave();
                        initWave();
                        levelFrame = 0;
                    } else {
                        gameState = DEAD;
                        dead = true;
                    }
                } else { //you won
                    brush.setColor(Color.white);
                    brush.drawString("YOU WIN", 400, 300);
                    won = true;
                }
            }
        }

        brush.setColor(Color.white); //score label text
        for (int i = 0; i < scoreLabels.size(); i++) {
            int[] sl = scoreLabels.get(i);
            brush.drawString(Integer.toString(sl[2]), sl[0], sl[1]);
            sl[3]++;
            scoreLabels.remove(i);
            if (sl[3] > 50) { //delete score label
                i--;
            } else {
                scoreLabels.add(i, sl); //add back score label but with new frame
            }
        }
    }

    /**
     * Score an asteroid
     * This method processes the scoring of
     * an old.Asteroid death.
     * @param a the asteroid to be scored
     * @return void nothing
     */
    private void score (Asteroid a) {
        score((int)a.position.x, (int)a.position.y, a.score());
    }

    /**
     * Score a UFO
     * This method processes the scoring of
     * an UFO death.
     * @param a the UFO to be scored
     * @return void nothing
     */
    private void score (EvilShip e) {
        score((int)e.position.x, (int)e.position.y, e.score());
    }

    /**
     * Creates a score label based on a score
     * This method graphically displays the score of
     * an asteroid collision, then updates the score
     * field.
     * @param x x position of the label
     * @param y y position of the label
     * @param score score to add
     */
    private void score (int x, int y, int score) {
        this.score += score; //update score
        scoreLabels.add(new int[] {x, y, score, 0}); //add to score labels
    }

    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /*
     * Checks for button press
     * This method checks for a button press.
     * If it recieves one and is on the start screen,
     * it continues on to the level screen.
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameState == START) gameState = LEVEL;
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Main method
     * This method instantiates and runs a new old.Asteroids class
     * @param args N/A
     * @throws IOException if files are not found
     * @return void nothing
     */
    public static void main(String[] args) throws IOException {
        Asteroids a = new Asteroids();
        a.repaint();
    }

}