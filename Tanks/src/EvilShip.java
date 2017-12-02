/**
 * EvilShip class
 * This class makes EvilShips, which 
 * chase after the player and shoot 
 * bullets at set intervals.
 * <p>
 * ADSB PS11: Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EvilShip extends Ship {
	private Random gen = new Random(); //random number generator
	private int frame = 0; //when to shoot
	private final int SHOOT = 50; //how often to shoot
	private BufferedImage[] images; 
	public Ship ship; //ship to follow
	/**
	 * Construct an EvilShip object
	 * This method constructs an EvilShip
	 * object using the Ship constructor.
	 * @param inShape shape to draw
	 * @param inPosition position
	 * @param width screen width
	 * @param height screen height
	 * @param images images for ship
	 * @param ship ship to follow
	 */
	public EvilShip(Point[] inShape, Point inPosition, int width, int height, BufferedImage[] images, Ship ship) {
		super(inShape, inPosition, 0.0, width, height, images[0]);
		this.images = images;
		setShip(ship);
		speed = 0.05;
		color = Color.blue;
	}
	/**
	 * Set ship that this instance is following to newShip
	 * @param newShip new Ship to follow
	 * @return void nothing
	 */
	public void setShip (Ship newShip) { ship = newShip; }
	/**
	 * Move the EvilShip in relation to the Ship it's following
	 * @return void nothing
	 */
	public void move() {
		rotation = calcRot(); //calculate rotation

		accelerate(2); //set speed
		position.setX((position.getX() + 0.5 * accel.x) % width); //move
		position.setY((position.getY() + 0.5 * accel.y) % height);
		if (position.getX() <= -25) position.setX(width + position.getX()); //warp
		if (position.getY() <= -25) position.setY(height + position.getY());
		
		
		accel.x = Math.min(Math.max(accel.x, -MAX_ACCEL), MAX_ACCEL);
		accel.y = Math.min(Math.max(accel.y, -MAX_ACCEL), MAX_ACCEL);
		
		shoot = ++frame % SHOOT == 0; //shoot?
	}
	/**
	 * Calculate rotation based on Ship that the instance is following
	 * using arctan
	 * @return double angle to rotate
	 */
	public double calcRot() {
		return Math.toDegrees(Math.atan((double)(ship.position.y - position.y) / (ship.position.x - position.x))) + 180 * (ship.position.x > position.x ? 0 : 1);
	}
	/**
	 * Set speed to acceleration based on rotation
	 * @param acceleration speed
	 * @return void nothing
	 */
	public void accelerate (double acceleration) { 
	    accel.x = (acceleration * Math.cos(Math.toRadians(rotation)));
	    accel.y = (acceleration * Math.sin(Math.toRadians(rotation)));
	}
	
	/**
	 * Get hitbox for the EvilShip, it's just a square.
	 * @return Point[] shape to draw
	 */
	public static Point[] generateShape() {
		return new Point[] {
				new Point(15, 0),
				new Point(30, 15),
				new Point(15, 30),
				new Point(0, 15),
		};
	}
	
	/**
	 * EvilShips are worth 10000 points when shot
	 * @return int points when shot
	 */
	public int score() {return 10000;}
	
	/**
	 * Choose which image to render.
	 * This method is for if at a later level EvilShips
	 * can take damage (which might be implemented later).
	 * @return void nothing
	 */
	@Override
	public void paint(Graphics brush) {
		this.img = images[0];
		super.paint(brush);
	}	
}
