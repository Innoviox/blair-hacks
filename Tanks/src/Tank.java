import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Tank extends Damagable {
    protected boolean left = false, right = false, front = false, shoot = false; //direction vectors
    public double speed = 0.1;
    public int shots = 0, MAX_SHOTS = 10;
    public double traction = 0.5; //slipperiness
    public Point accel = new Point(0.0, 0.0); //acceleration vector represented by a old.Point
    public BufferedImage image;
    public final int MAX_ACCEL = 15;

    public Tank(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, int health) throws IOException {
        super(inShape, inPosition, inRotation, width, height, ImageIO.read(new File("images/tank_blue.png")), health);
    }


	public void paint(Graphics brush, Point cameraTranslation) {

		Point[] points = this.getPoints();
		int pl = points.length;
		int[] x = new int[pl];
		int[] y = new int[pl];
		for (int i = 0; i < pl; i++) {
			x[i] = (int)points[i].getX();
			y[i] = (int)points[i].getY();
		}

//        brush.fillPolygon(x, y, pl);

		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(rotation), findCenter().x, findCenter().y);
		Graphics2D g2d = (Graphics2D) brush;
		g2d.drawImage(img, at, null);

	}

	public void move() {
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
        position.setX((position.getX() + accel.x) % width); //move
        position.setY((position.getY() + accel.y) % height);
        if (position.getX() <= -25) position.setX(width + position.getX()); //warp
        if (position.getY() <= -25) position.setY(height + position.getY());


        accel.x = Math.min(Math.max(accel.x, -MAX_ACCEL), MAX_ACCEL); //maximum speed
        accel.y = Math.min(Math.max(accel.y, -MAX_ACCEL), MAX_ACCEL);
    }

    public void update(Map<Character,Boolean> keys){
    	left = keys.get('a');
    	right = keys.get('d');
    	front = keys.get('w');



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
}
