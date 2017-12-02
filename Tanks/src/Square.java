import java.awt.image.BufferedImage;
import java.util.Random;

public class Square extends Damagable {
    public Square(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image){
        super(inShape, inPosition, inRotation, width, height, image, new Random().nextInt(50) + 50);
    }

    @Override
    public void move() {
        this.position.x += Math.random() * 5;
        this.position.y += Math.random() * 5;
    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }
}
