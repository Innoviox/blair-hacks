import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

	private Canvas canvas;

	private List<Square> squares;

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
			}
		};

		scheduler.scheduleAtFixedRate(update, 1, 1000/60, TimeUnit.MILLISECONDS);

	}

	public void update() {
		//TODO update!
	}

	public Game() {
		canvas = new Canvas();
		squares = new ArrayList<>();
		canvas.setVisible(true);

	}

}