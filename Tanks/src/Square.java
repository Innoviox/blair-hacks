import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Square extends Damagable {
    private static final Random r = new Random();
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;
    public Square(Point[] inShape, Point inPosition, double inRotation) throws IOException{
        super(inShape, inPosition, inRotation, WIDTH, HEIGHT, ImageIO.read(new File("images/square.png")), r.nextInt(50) + 50);
    }

    @Override
    public void move() {
        this.position.x += Math.random() * 5;
        this.position.y += Math.random() * 5;
        this.rotate(r.nextInt(2));
    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }
}
