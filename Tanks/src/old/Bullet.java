package old; /**
 * A bullet class.
 * This class represents a bullet shot
 * by ships. It also is a superclass for
 * objects that disappear after a set time 
 * limit like old.Rubble.
 * <p>
 * ADSB PS11: old.Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet extends Polygon {
	public double step = 15; //speed
	public int lifetime = 40; //how long it stays on the screen
	public int counter = 0; //how long it's been alive
	/**
	 * Construct a bullet object
	 * This method constructs a new
	 * bullet instance using old.Polygon's
	 * constructor.
	 * @param inShape shape of bullet
	 * @param inPosition position
	 * @param inRotation rotation
	 * @param width screen width
	 * @param height screen height
	 * @param image image (usually null)
	 * @param stepinc how fast the ship is going so the bullets go faster
	 * @return void nothing
	 */
	public Bullet(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, double stepinc) {
		super(inShape, inPosition, inRotation, width, height, image);	 //call super constructor
		step += stepinc; //increase speed
		color = Color.green;
	}
	
	/**
	 * Moves the bullet
	 * This method moves the bullet using trig
	 * based on the angle, then increases
	 * how long the bullet has lived for.
	 * @return void nothing
	 */
	public void move() {
		position.setX(((position.getX() + Math.cos(Math.toRadians(rotation)) * (step))));
		position.setY(((position.getY() + Math.sin(Math.toRadians(rotation)) * (step))));
		counter++;
	}
	
	/**
	 * Destroy a bullet
	 * Bullets don't decompose into anything,
	 * they just die.
	 * @return old.Polygon[] objects to add to to the main objects list.
	 */
	public Polygon[] destroy() {
		return new Polygon[] {}; 
	}
	
	/**
	 * Paint a bullet.
	 * This method is overriden because
	 * bullets don't have images
	 * so we don't want any NullPointerExceptions.
	 * @return void nothing
	 */
	@Override
	public void paint(Graphics brush) {
		Point[] points = this.getPoints();
		int pl = points.length;
		int[] x = new int[pl];
		int[] y = new int[pl];
		for (int i = 0; i < pl; i++) {
			x[i] = (int)points[i].getX();
			y[i] = (int)points[i].getY();
		}
		brush.fillPolygon(x, y, pl);
	}
}
