import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {
    private static final Point[] squarePoints = new Point[]{
            new Point(0, 0),
            new Point(0, Square.HEIGHT),
            new Point(Square.WIDTH, Square.HEIGHT),
            new Point(Square.WIDTH, 0)
    };
    private static final Point[] bulletPoints = new Point[]{
            new Point(0, 0),
            new Point(0, 2),
            new Point(10, 2),
            new Point(10, 0)
    };
    public static final Point[] tankPoints = new Point[]{new Point(0, 0), new Point(0, 40), new Point(40, 40), new Point(40, 20), new Point(40, 0)};
    public static final Random r = new Random();

    private Canvas canvas;

    private Runnable update;

    private List<Polygon> gameObjects;

    private Tank player;

    private int updates = 0;

    private int lastShot = 0;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String args[]) throws IOException {
        Game g = new Game();

        g.start();
    }

    public void start() {
        canvas.paint(canvas.getGraphics());

        update = () -> update();

        scheduler.scheduleAtFixedRate(update,1,1000/60, TimeUnit.MILLISECONDS);

    }

    public void update() {
        int count = 0;
        updates++;
        ArrayList<Polygon> rem = new ArrayList<>();
        for (Polygon p : gameObjects) {
            if (!(p instanceof Square)) {
                p.update();
            } else {
                count++;
                ((Square) p).update(gameObjects);

                if (player.distance(p).magnitude() > Math.sqrt(Canvas.MAXWIDTH * Canvas.MAXWIDTH + Canvas.MAXHEIGHT * Canvas.MAXHEIGHT  )) {
                    rem.add(p);
                }
            }
            if (p instanceof Bullet) {
                if (((Bullet) p).counter > ((Bullet) p).lifetime) {
                    rem.add(p);
                }
            }
            for (Polygon p2: gameObjects) {
                if (p != p2 && p.collides(p2)) {
                    damage(p, p2, rem);
                    damage(p2, p, rem);
                }
            }
            if (player.collides(p)) {
                damage(p, player, rem);
                damage(player, p, rem);
            }
        }

        if (new Random().nextInt(count) == 1) {
            Point position = new Point(r.nextInt(Canvas.MAXWIDTH), r.nextInt(Canvas.MAXHEIGHT));
            try {
                gameObjects.add(new Square(squarePoints, position, 0));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        gameObjects.removeAll(rem);
        if (canvas.getKeys().get(' ')) {
            makeBullet();
        }
        player.update(canvas.getKeys());
        canvas.update(gameObjects, player);
        canvas.paint(canvas.getGraphics());
    }

    public Game() throws IOException {
        gameObjects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Point position = new Point(r.nextInt(Canvas.MAXWIDTH), r.nextInt(Canvas.MAXHEIGHT));
            gameObjects.add(new Square(squarePoints, position, 0));
        }
        player = new Tank(tankPoints, new Point(0, 0), 0,
                10, 10, ImageIO.read(new File("images/tank_blue.png")), 100);

        //gameObjects.add(new Bot(player));
        canvas = new Canvas(player);
		player.assignCanvas(canvas);
		canvas.update(gameObjects, player);

		canvas.setVisible(true);

	}

	public void damage(Polygon p, ArrayList<Polygon> rem, Polygon p2) {
		Damagable d;
		try {
			d = (Damagable) p;
			if (p2 instanceof Bullet)
				d.health -= player.getDamage();
			else
				d.health--;
			if (d.health <= 0) {
				rem.add(p);
				if(p2 instanceof Bullet)
					player.setXp(player.getXp() + d.max_health);
			}
		} catch (ClassCastException e) {
		}
	}

	public void damage(Polygon p, Polygon p2, ArrayList<Polygon> rem) {
		Damagable d;
		try {
			d = (Damagable) p;
			d.health--;
			if (d.health <= 0) {
				rem.add(p);
				if (p2 instanceof Bullet || p2 instanceof Tank) player.setXp(player.getXp() + d.max_health);
			}
			//p.recoil();
		} catch (ClassCastException e) {
		}
	}


	public void makeBullet() {

		if (updates - lastShot > player.getRateOfFire()) {
			lastShot = updates;
			gameObjects.add(new Bullet(bulletPoints, new Point(player.getPoints()[3].x + Canvas.MAXWIDTH / 2, player.getPoints()[3].y + Canvas.MAXHEIGHT / 2), player.rotation, 50, 50, Math.abs(player.accel.x + player.accel.y)));
		}
	}
}