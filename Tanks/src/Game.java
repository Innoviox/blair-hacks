import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

	private Canvas canvas;

	private List<Square> squares;

	private List<Bullet> bullets;

	private Tank player;

	private Runnable update;

	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void main(String args[]) {
		Game g = new Game();

		g.start();
	}

	public void start() {
		canvas.paint(canvas.getGraphics());

		update = new Runnable() {
			@Override
			public void run() {
				update();
				canvas.paint(canvas.getGraphics());
			}
		};

		scheduler.scheduleAtFixedRate(update, 1, 1000/60, TimeUnit.MILLISECONDS);

	}

	public void update() {
		//TODO update!
		for(Square s : squares)
			s.update();

		for(Bullet b : bullets)
			b.update();

		player.update();
	}

	public Game() {
		canvas = new Canvas();
		squares = new ArrayList<>();
		bullets = new ArrayList<>();

//		player = new Tank();

		canvas.setVisible(true);

	}

}