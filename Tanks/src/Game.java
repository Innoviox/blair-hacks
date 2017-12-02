import java.util.ArrayList;
import java.util.List;

public class Game {

	private Canvas canvas;

	private List<Square> squares;

	public static void main(String args[]) {
		Game g = new Game();

		g.start();
	}

	public void start() {
		//TODO implement startup
		canvas.paint(canvas.getGraphics());





	}


	public Game() {
		canvas = new Canvas();
		squares = new ArrayList<>();
		canvas.setVisible(true);

	}

}