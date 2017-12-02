package old; /**
 * Evil old.Bullet
 * This class is a bullet that comes out of an evil ship.
 */

import java.awt.Color;
import java.awt.image.BufferedImage;

public class EvilBullet extends Bullet {
	/**
	 * Construct a old.EvilBullet object
	 * This method constructs a new
	 * old.EvilBullet instance using old.Bullet's
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
	public EvilBullet(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, double stepinc) {
		super(inShape, inPosition, inRotation, width, height, image, stepinc);
		color = Color.red; //different color to distinguish
	}
}
