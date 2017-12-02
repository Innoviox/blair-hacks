import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Canvas extends JFrame implements KeyListener{

	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	static public Point cameraTranslation = new Point(0, 0);

	private Map<Character, Boolean> keys;

	private List<? extends Polygon> objects;

	private Tank player;

	BufferedImage bi = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_RGB);

	public Canvas(Tank player) {
		this.setSize(MAXWIDTH, MAXHEIGHT);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		cameraTranslation = player.position;
		System.out.println(cameraTranslation);
		keys = new HashMap<>();
		keys.put('w',false);
		keys.put('s',false);
		keys.put('d',false);
		keys.put('a',false);
		this.addKeyListener(this);
	}

	public void paint(Graphics g) {
			Graphics b = bi.getGraphics();

			b.setColor(Color.white);

			b.fillRect(0,0,MAXWIDTH,MAXHEIGHT);

			player.paint(b, cameraTranslation);

			for (Polygon p : objects)
				p.paint(b, cameraTranslation);

			g = this.getGraphics();

			g.drawImage(bi, 0, 0, null);
	}

	public void update(List<? extends Polygon> p, Tank player) {
		objects = p;
		this.player = player;
	}

	public Map<Character,Boolean> getKeys(){
		return keys;
	}

	@Override
	public void keyReleased(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_W){
			keys.put('w',false);
		}
		if(event.getKeyCode() == KeyEvent.VK_S){
			keys.put('s',false);
		}
		if(event.getKeyCode() == KeyEvent.VK_A){
			keys.put('a',false);
		}
		if(event.getKeyCode() == KeyEvent.VK_D){
			keys.put('d',false);
		}
	}

	@Override
	public void keyTyped(KeyEvent event){

	}
	@Override
	public void keyPressed(KeyEvent event){
		if(event.getKeyCode() == KeyEvent.VK_W){
			keys.put('w',true);
		}
		if(event.getKeyCode() == KeyEvent.VK_S){
			keys.put('s',true);
		}
		if(event.getKeyCode() == KeyEvent.VK_A){
			keys.put('a',true);
		}
		if(event.getKeyCode() == KeyEvent.VK_D){
			keys.put('d',true);
		}
	}


}
