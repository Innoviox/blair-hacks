/**
 * The Ship class
 * This class is the player. It 
 * moves around and responds to
 * keys. It is the only thing the
 * player controls in this game.
 * <p>
 * ADSB PS11: Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

import java.awt.Color;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Ship extends Polygon implements KeyListener {
	protected boolean left = false, right = false, front = false, shoot = false; //direction vectors
	public double speed = 0.1;
	public int shots = 0, MAX_SHOTS = 10;
	public double traction = 0.05; //slipperiness
	public Point accel = new Point(0.0, 0.0); //acceleration vector represented by a Point
	public BufferedImage image;
	public final int MAX_ACCEL = 15;
	
	/**
	 * Construct a ship object using the Polygon constructor
	 * @param inShape points to draw shape with
	 * @param inPosition starting position
	 * @param inRotation starting rotation
	 * @param width screen width
	 * @param height screen height
	 * @param image image file
	 * @return void nothing
	 */
	public Ship(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image){
		super(inShape, inPosition, inRotation, width, height, image);
		color = Color.red;
	}
	
	/**
	 * Move the ship object based on
	 * keyboard input, then calculating
	 * acceleration (or deceleration) based
	 * on rotation.
	 * @return void nothing
	 */
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
	
	/**
	 * Accelerate the ship
	 * Make the ship go faster, but in
	 * the right direction using trig 
	 * @param acceleration how much faster to go
	 * @return void nothing
	 */
	public void accelerate (double acceleration) { 
	    accel.x += (acceleration * Math.cos(Math.toRadians(rotation)));
	    accel.y += (acceleration * Math.sin(Math.toRadians(rotation)));
	}
	
	/**
	 * Check if the ship is shooting
	 * @return shooting
	 */
	public boolean shooting() {
		return shoot;
	}
	
	/**
	 * Stop the ship from shooting
	 * @return void nothing
	 */
	public void shootOff() {
		shoot = false;
	}
	/**
	 * The following methods (that start with "key")
	 * are required from KeyListener and are used
	 * to let the user control the ship with the keys.
	 * @param e the event/keypress to handle
	 * @return void nothing
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	    		case KeyEvent.VK_UP: front = true; break;
		    case KeyEvent.VK_RIGHT: right = true; break;
		    case KeyEvent.VK_LEFT: left = true; break;
		    case KeyEvent.VK_SPACE: shoot = true; break;
	    }
	}
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	    	case KeyEvent.VK_UP: front = false; break;
		    case KeyEvent.VK_RIGHT: right = false; break;
		    case KeyEvent.VK_LEFT: left = false; break;
		    case KeyEvent.VK_SPACE: shots=0;shoot = false; break;
	    }
	}
	public void keyTyped(KeyEvent e) {}
	
	/**
	 * Destroy a ship, and return the shrapnel
	 * generated.
	 * @return void nothing
	 */
	public Polygon[] destroy() {
		return new Rubble[] {
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null),
				new Rubble(this, false, Color.red, null)
		};
	}
	
	/**
	 * Generate the hitbox of the ship
	 * based on the level.
	 * @param level current level
	 * @return Point[] shape to draw with
	 */
	public static Point[] generateShape(int level) {
		Point[] oldShipPoints = new Point[]{
				new Point(400.0, 300.0),
				new Point(387.5, 287.5),
				new Point(425.0, 300.0),
			    new Point(387.5, 312.5),
			   };

		Point[] shipPoints1 = new Point[] {
				new Point(0.0, 0.0),
				new Point(6.0, 11.25),
				new Point(10.5, 12.75),
				new Point(10.5, 13.5),
				new Point(10.5, 14.25),
				new Point(6.25, 16.0),
				new Point(0.0, 27.0),
				new Point(-7.0, 22.5),
				new Point(-6.0, 15.25),
				new Point(-8.0, 16.5),
				new Point(-8.0, 10.5),
				new Point(-6.0, 9.5),
				new Point(-7.5, 4.5),
				new Point(0.0, 0.0),
		};
		/*
		Point[] shipPoints2 = new Point[] {
				
		};
		*/
		Point[] shipPoints2 = new Point[] {
				new Point(3.5, -9.0),
				new Point(5.25, -4.75), //gun
				new Point(7.25, -4.75),
				new Point(7.25, 0.5),
				new Point(5.25, 0.5),
				new Point(3.5, 5.0), //gun
				new Point(9.5, 9.5),
				new Point(23.25, 4.25),
				new Point(22.5, 1.5),
				new Point(16.0, 2.75),
				new Point(15.5, 2.5),
				new Point(17.5, 1.0),
				new Point(17.5, -5.0),
				new Point(15.5, -5.75),
				new Point(16.0, -6.75),
				new Point(23.0, -5.0),
				new Point(23.25, -8.0),
				new Point(9.5, -13.5),
		};
		
		Point[] shipPoints3 = new Point[]{
				new Point(10.0, 0.0),
				new Point(20.5, 3.5),
				new Point(20.0, 5.5), //gun
				new Point(17.25, 4.75),
				new Point(15.75, 9.0),
				new Point(18.5, 10.0),
				new Point(18.5, 14.25),
				new Point(15.75, 15.25),
				new Point(17.25, 20.0), //gun
				new Point(20.0, 19.5),
				new Point(20.5, 21.5),
				new Point(10.0, 24.5),
				new Point(2.25, 18.5),
				new Point(1.5, 16.5),
				new Point(0.5, 14.0),
				new Point(0.5, 10.25),
				new Point(1.5, 8.25),
				new Point(1.5, 6.0),
				new Point(10.0, 0.0),
		};		
		return (new Point[][] {shipPoints1, shipPoints2, shipPoints3})[level];
	}
}
