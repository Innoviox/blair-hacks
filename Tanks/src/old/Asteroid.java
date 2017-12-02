package old; /**
 * Represents an old.Asteroid
 * This class contains, moves, and draws an
 * asteroid that the player has to shoot down.
 * <p>
 * ADSB PS11: old.Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Asteroid extends Polygon {
	private int step = 3; //speed
	private int w, h; //width and height of the asteroid
	private int type; //randomly assigned type
	/**
	 * Constructor
	 * This method sets up an asteroid
	 * using mainly the inherited old.Polygon
	 * constructor.
	 * @param inShape points to draw shape with
	 * @param inPosition starting position
	 * @param inRotation starting rotation
	 * @param sWidth screen width
	 * @param sHeight screen height
	 * @param w asteroid width
	 * @param h asteroid height
	 * @param image image file
	 * @param type assigned type
	 * @return void nothing
	 */
	public Asteroid(Point[] inShape, Point inPosition, double inRotation, int sWidth, int sHeight, int w, int h, BufferedImage image, int type) {
		super(inShape, inPosition, inRotation, sWidth, sHeight, image); //call super constructor
		//set up fields
		this.img = resize(img, w, h); //make image right size
		this.w=w;
		this.h=h;
		this.type = type;
		color = Color.gray;
	}
	
	/**
	 * Resize an image
	 * Used to resize an image to the
	 * correct height so it fits the 
	 * asteroid.
	 * @param img image to resize
	 * @param newW new width 
	 * @param newH new height
	 * @return BufferedImage new resized image
	 */
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	/**
	 * Move the asteroid
	 * This method moves the asteroid based on
	 * its rotation, then checks to see if it
	 * warps to the other side.
	 * @return void nothing
	 */
	public void move() {
		position.setX(((position.getX() + Math.cos(Math.toRadians(rotation)) * (step))) % width);
		position.setY(((position.getY() + Math.sin(Math.toRadians(rotation)) * (step))) % height);
		if (position.getX() <= -25) position.setX(width + position.getX());
		if (position.getY() <= -25) position.setY(height + position.getY());
	}
	
	/**
	 * Destroy the asteroid
	 * This method destroys the asteroid.
	 * It generates two new asteroids and
	 * a bunch of dust.
	 * @return old.Polygon[] objects that are generated
	 */
	public Polygon[] destroy() {
		if (w > 20 && h > 20) { //only ggenertae new asteroids if big enough
			Point[] shape = generateShape(w/2, h/2, type); //generate shape of new asteroids
			//generate new asteroids
			Asteroid a1 = new Asteroid(shape, new Point(position.getX() + w/2 + 10, position.getY() + h/2 + 10), rotation + 45, width, height, w/2, h/2, img, type);
			Asteroid a2 = new Asteroid(shape, new Point(position.getX() - w/2 - 10, position.getY() - h/2 - 10), rotation - 45, width, height, w/2, h/2, img, type);
			//generate dust
			Rubble r1 = new Rubble(this, true, Color.gray, null);
			Rubble r2 = new Rubble(this, true, Color.gray, null);
			Rubble r3 = new Rubble(this, true, Color.gray, null);
			return new Polygon[] {a1, a2, r1, r2, r3};
		} else {
			//generate just dust
			Rubble r1 = new Rubble(this, true, Color.gray, null);
			Rubble r2 = new Rubble(this, true, Color.gray, null);
			Rubble r3 = new Rubble(this, true, Color.gray, null);
			return new Polygon[] {r1, r2, r3}; 
		}
	}
	/**
	 * Generate different shapes
	 * The four following methods generate shapes
	 * based on type, width, and/or height.
	 * @param type type of asteroid
	 * @param w width of asteroid
	 * @param h height of asteroid
	 * @return old.Point[] shape
	 */
	public static Point[] generateShape(int type) {
		int w = randint(30, 50), h = randint(30, 50);
		if (type == 1) return generateShape1(w, h);
		else return generateShape2(w, h);
	}

	public static Point[] generateShape(int w, int h, int type) {
		if (type == 1) return generateShape1(w, h);
		else return generateShape2(w, h);
	}
	
	public static Point[] generateShape1(int w, int h) {
		double ow = 50, oh = 42;
		double[] x = new double[] {8, 37, 50, 42, 30, 15, 0, 8};
		double[] y = new double[] {0, 0, 21, 37, 35, 41, 26, 0};
		Point[] ret = new Point[x.length + 1];
		for (int i = 0; i < x.length; i++) {
			ret[i] = new Point((x[i]/ow) * w, (y[i]/oh) * h);
		}
		ret[x.length] = new Point(w, h);
		return ret;
	}
	
	public static Point[] generateShape2(int w, int h) {
		double ow = 49, oh = 47;
		double[] x = new double[] {7, 33, 48.5, 40, 15, 0, 7};
		double[] y = new double[] {7, 0, 18.5, 45, 42.5, 29.5, 7};
		Point[] ret = new Point[x.length + 1];
		for (int i = 0; i < x.length; i++) {
			ret[i] = new Point((x[i]/ow) * w, (y[i]/oh) * h);
		}
		ret[x.length] = new Point(w, h); 
		return ret;
	}

	/**
	 * Score an assteroid
	 * Score an asteroid based on its width and height.
	 * To make smaller asteroids be weighted more, 
	 * the values are subtracted from 100, multiplied,
	 * then rounded to produce an even number.
	 * @return int the score
	 */
	public int score() {
		return ((100 - w) * (100 - h)) / 100 * 100;
	}
}
