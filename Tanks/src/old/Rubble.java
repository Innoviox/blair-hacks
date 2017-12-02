package old; /**
 * Generated when things are destroyed
 * The rubble class is an extension of old.Bullet
 * because it only lasts a specific amount of time
 * old.Rubble is ejected from things when they die.
 * <p>
 * ADSB PS11: old.Asteroids
 * 10/3/17
 * @author Simon Chervenak
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Rubble extends Bullet {
	private static final Random gen = new Random();
	public Color color;
	
	/**
	 * Constructs a old.Rubble object using the old.Bullet constructor
	 * @param s origin point
	 * @param dust is the origin type old.Asteroid?
	 * @param color color of origin type
	 * @param image image (null)
	 */
	public Rubble(Polygon s, boolean dust, Color color, BufferedImage image) {
		super(generateShape(dust), s.position.clone(), gen.nextInt(360), s.width, s.height, image, 0);
		step = 3; 
		lifetime = 150;
		this.color = color;
	}
	
	/**
	 * Generate random shape
	 * If dust, make it small, otherwise, random size.
	 * @param dust
	 * @return
	 */
	private static Point[] generateShape(boolean dust) {
		int h;
		if (dust) h = 1;
		else h = randint(10, 25);
		return new Point[] {
				new Point(0, 0),
				new Point(h, 0),
				new Point(h, 1),
				new Point(0, 1)
		};
	}
	
	/**
	 * Make sure that the bullet
	 * is painted the right color
	 * @param brush the brush to draw the bullet with
	 * @return void nothing
	 */
	@Override
	public void paint(Graphics brush) {
		brush.setColor(color);
		super.paint(brush);
	}
}
