import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Bot extends Tank {
    private Tank t;
    private boolean left, front, right;
    private boolean leftswitch, downswitch, rightswitch;
    private int lefttick = 0, downtick = 10, righttick = 20;
    private int counter = 0;
    Bot(Tank t) throws IOException{
        super(Game.tankPoints, new Point(Game.r.nextInt(Canvas.MAXWIDTH), 0), 0, 40, 40, ImageIO.read(new File("images/tank_dark.png")), 500);
        this.t = t;
        left = false;
        front = true;
        right = false;
    }

    @Override
    public void paint(Graphics brush, Point cameraTranslation) {
        translate(cameraTranslation);
        AffineTransform at = new AffineTransform();
        at.translate(position.x - img.getWidth() / 4
                , position.y - img.getHeight() / 4);
        at.rotate(Math.toRadians(rotation), findCenter().x, findCenter().y);
        Graphics2D g2d = (Graphics2D) brush;
        g2d.drawImage(img, at, null);
        this.hb.paint(brush, cameraTranslation);
    }

    @Override
    public void move() {
        super.move();
        position.setX((position.getX() + accel.x)); //move
        position.setY((position.getY() + accel.y));
    }

    public void update(){
        counter++;
        if (counter > lefttick) {
            left = !left;
            lefttick += Game.r.nextInt(100);
        }
        else if (counter > righttick) {
            right = !right;
            righttick += Game.r.nextInt(100);
        }
        else if (counter > righttick) {
            front = !front;
            righttick += Game.r.nextInt(100);
        }
        move();
    }
}
