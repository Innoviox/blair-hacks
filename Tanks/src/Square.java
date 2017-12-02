import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Square extends Damagable {
    private static final Random r = new Random();
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    private double vX = 0, vY = 0;
    private double aX = 0, aY = 0;;


    public Square(Point[] inShape, Point inPosition, double inRotation) throws IOException{
        super(inShape, inPosition, inRotation, WIDTH, HEIGHT, ImageIO.read(new File("images/square.png")), r.nextInt(50) + 50);
        AffineTransform at = new AffineTransform();
        at.scale(WIDTH / 50.0, HEIGHT / 50.0);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        this.img = op.filter(this.img, null);
    }

    public void update(List<Polygon> objects){
        double x = 0, y = 0;
        for(Polygon p : objects){
            if(p instanceof Square){
                x = (x + distance(p).x) / 2;
                y = (y + distance(p).y) / 2;
            }
        }

        x = 1/x;
        y = 1/y;

        aX += x / 300;
        aY += y / 300;
        move();
    }



    @Override
    public void move() {
        this.hb.move();

        position = new Point(position.x + vX, position.y + vY);

        vX += aX;
        vY += aY;



    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }
}
