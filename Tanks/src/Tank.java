import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class Tank extends Damagable {
    protected boolean left = false, right = false, front = false, shoot = false; //direction vectors
    public double speed = 0.1;
    public int shots = 0, MAX_SHOTS = 10;
    public double traction = 0.5; //slipperiness
    public Point accel = new Point(0.0, 0.0); //acceleration vector represented by a old.Point
    public BufferedImage image;
    public final int MAX_ACCEL = 15;

	private int level;

	private int rateOfFire = 10;
	private int xp;

	private Canvas canvas;

    public Tank(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, int health) throws IOException {
        super(inShape, inPosition, inRotation, width, height, ImageIO.read(new File("images/tank_blue.png")), health);
        /*
        for (Point p: shape) {
            p.setX(p.getX() + Canvas.MAXWIDTH / 2);
            p.setY(p.getY() + Canvas.MAXHEIGHT / 2);
        }
        */
    }


	public void paint(Graphics brush, Point cameraTranslation) {
        /*
		Point[] points = this.getPoints();
		int pl = points.length;
		int[] x = new int[pl];
		int[] y = new int[pl];
		for (int i = 0; i < pl; i++) {
			x[i] = (int)points[i].getX();// + Canvas.MAXWIDTH / 2;
			y[i] = (int)points[i].getY();// + Canvas.MAXHEIGHT / 2;
		}
        brush.setColor(Color.blue);
        brush.fillPolygon(x, y, pl);
        */
		AffineTransform at = new AffineTransform();
		at.translate(Canvas.MAXWIDTH/2 - img.getWidth() / 4
				, Canvas.MAXHEIGHT/2 - img.getHeight() / 4);
		at.rotate(Math.toRadians(rotation), findCenter().x, findCenter().y);
		Graphics2D g2d = (Graphics2D) brush;
		g2d.drawImage(img, at, null);
        this.hb.paint(brush, cameraTranslation);

	}

	@Override
	public boolean collides(Polygon other) {
        Point pN;
        for (Point p: other.getPoints()) {
            pN = new Point(p.x + Canvas.MAXWIDTH / 2, p.y + Canvas.MAXHEIGHT / 2);
            if (contains(pN)) return true;
        }
        for (Point p: getPoints()) {
            pN = new Point(p.x + Canvas.MAXWIDTH / 2, p.y + Canvas.MAXHEIGHT / 2);
            if (other.contains(pN)) return true;
        }
        if (contains(other.position)) return true;
        if (other.contains(position)) return true;
        return false;
    }

	public void move() {
        this.hb.move();
        if (right) rotate(6);
        if (left)  rotate(-6);
        if (front) {
            accelerate(speed);
        } else {
            //decelerate
            if (accel.x >= traction || accel.x <= -traction) {
                double ax = Math.abs(traction * Math.cos(Math.toRadians(rotation)));
                if (ax > 0) { //make sure the sign is rgith
                    if (accel.x > 0) ax = -ax;
                    else {}
                } else {
                    if (accel.x > 0) ax = -ax;
                    else {}
                }
                accel.x += ax;
            } else {
                accel.x = 0;
            }

            if (accel.y >= traction || accel.y <= -traction) {
                double ay = Math.abs(traction * Math.sin(Math.toRadians(rotation)));
                if (ay > 0) {
                    if (accel.y > 0) ay = -ay;
                    else {}
                } else {
                    if (accel.y > 0) ay = -ay;
                    else {}
                }
                accel.y += ay;
            } else {
                accel.y = 0;
            }
        }
        //position.setX((position.getX() + accel.x) % width); //move
        //position.setY((position.getY() + accel.y) % height);


        accel.x = Math.min(Math.max(accel.x, -MAX_ACCEL), MAX_ACCEL); //maximum speed
        accel.y = Math.min(Math.max(accel.y, -MAX_ACCEL), MAX_ACCEL);

        hb.move();
        hb.position.y += Canvas.MAXHEIGHT / 2;
        hb.position.x += Canvas.MAXWIDTH / 2;

    }

    public void update(Map<Character,Boolean> keys){
    	left = keys.get('a');
    	right = keys.get('d');
    	front = keys.get('w');
		move();
		if(xp > level * level * 50){
			levelup();
		}
    }

    public void levelup(){
    	level++;
    	xp = 0;

	    JOptionPane.showConfirmDialog(canvas, "Do you love pie?", "Important questions",2);


    }

    public void assignCanvas(Canvas c){
    	canvas = c;
    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }

    public void accelerate (double acceleration) {
        accel.x += (acceleration * Math.cos(Math.toRadians(rotation)));
        accel.y += (acceleration * Math.sin(Math.toRadians(rotation)));
    }

    public boolean shooting() {
        return shoot;
    }

    public void shootOff() {
        shoot = false;
    }

    public static Point[] generateShape(int level) {
        Point[] oldShipPoints = new Point[]{
                new Point(400.0, 300.0),
                new Point(387.5, 287.5),
                new Point(425.0, 300.0),
                new Point(387.5, 312.5),
        };

        return oldShipPoints;
    }
	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {

		this.xp = xp;
	}

	public int getRateOfFire() {
		return rateOfFire;
	}

	public void setRateOfFire(int rateOfFire) {
		this.rateOfFire = rateOfFire;
	}
}
