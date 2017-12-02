import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Bot extends Tank {
    private Tank t;
    private boolean left, down, right;
    private boolean leftswitch, downswitch, rightswitch;
    private int lefttick = 0, downtick = 10, righttick = 20;
    private int counter = 0;
    Bot(Tank t) throws IOException{
        super(Game.tankPoints, new Point(Game.r.nextInt(Canvas.MAXWIDTH), 0), 0, 40, 40, ImageIO.read(new File("images/tank_dark.png")), 500);
        this.t = t;
        left = false;
        down = false;
        right = false;
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
            down = !down;
            righttick += Game.r.nextInt(100);
        }
        move();
    }
}
