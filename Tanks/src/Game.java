import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {
    private static final Point[] squarePoints = new Point[] {
            new Point(0, 0),
            new Point(0, Square.HEIGHT),
            new Point(Square.WIDTH, Square.HEIGHT),
            new Point(Square.WIDTH, 0)
    };
    private static final Point[] bulletPoints = new Point[] {
            new Point(0, 0),
            new Point(0, 2),
            new Point(10, 2),
            new Point(10, 0)
    };
    private static final Random r = new Random();

    private Canvas canvas;

    private Runnable update;

    private List<Polygon> gameObjects;

    private Tank player;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String args[]) throws IOException {
        Game g = new Game();

        g.start();
    }

    public void start() {
        canvas.paint(canvas.getGraphics());

        update = () -> update();

        scheduler.scheduleAtFixedRate(update, 1, 1000/60, TimeUnit.MILLISECONDS);

    }

    public void makeBullet() {
        gameObjects.add(new Bullet(bulletPoints, new Point(player.getPoints()[3].x + Canvas.MAXWIDTH / 2, player.getPoints()[3].y + Canvas.MAXHEIGHT / 2), player.rotation, 50, 50, Math.abs(player.accel.x + player.accel.y)));
    }

    public void update() {
        ArrayList<Polygon> rem = new ArrayList<>();
        for(Polygon p : gameObjects) {
            p.update();
            if (p instanceof Bullet) {
                if (((Bullet) p).counter > ((Bullet) p).lifetime) {
                    rem.add(p);
                }
            }
        }
        for (Polygon p: rem) gameObjects.remove(p);
        if (canvas.getKeys().get(' ')) {
            makeBullet();
        }
        player.update(canvas.getKeys());
        canvas.update(gameObjects, player);
        canvas.paint(canvas.getGraphics());
    }

    public Game() throws IOException {
        gameObjects = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Point position = new Point(r.nextInt(Canvas.MAXWIDTH), r.nextInt(Canvas.MAXHEIGHT));
            gameObjects.add(new Square(squarePoints, position, 0));
        }

        player = new Tank(new Point[] {new Point(0,0), new Point(0, 40), new Point(40, 40), new Point(40, 20), new Point(40, 0)},new Point(0, 0),0,
		        10,10, ImageIO.read(new File("images/tank_blue.png")),10);

	    canvas = new Canvas(player);

        canvas.update(gameObjects, player);

        canvas.setVisible(true);

    }

}