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
            new Point(0, 50),
            new Point(50, 50),
            new Point(50, 0)
    };
    private static final Random r = new Random();
    private Canvas canvas;

    private List<Square> squares;


    private Runnable update;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void main(String args[]) throws IOException {
        Game g = new Game();

        g.start();
    }

    public void start() {
        canvas.paint(canvas.getGraphics());

        update = new Runnable() {
            @Override
            public void run() {
                update();
            }
        };

        scheduler.scheduleAtFixedRate(update, 1, 1000/60, TimeUnit.MILLISECONDS);

    }

    public void update() {
        //TODO update!
    }

    public Game() throws IOException {
        canvas = new Canvas();
        squares = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Point position = new Point(r.nextInt(Canvas.MAXWIDTH), r.nextInt(Canvas.MAXHEIGHT));
            squares.add(new Square(squarePoints, position, 0));
        }
        canvas.update(squares);
        canvas.setVisible(true);

    }

}