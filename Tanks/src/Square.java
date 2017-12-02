import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Square extends Damagable {
    private static final Random r = new Random();
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public Square(Point[] inShape, Point inPosition, double inRotation) throws IOException{
        super(inShape, inPosition, inRotation, WIDTH, HEIGHT, ImageIO.read(new File("images/square.png")), r.nextInt(50) + 50);
        AffineTransform at = new AffineTransform();
        at.scale(WIDTH / 50.0, HEIGHT / 50.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        this.img = op.filter(this.img, null);
    }

    @Override
    public void move() {
        this.hb.move();
        //this.rotate(r.nextInt(2) - 1);
        //this.health = Math.max(0, this.health - 1);
    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }
}
