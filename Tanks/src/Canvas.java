import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Canvas extends JFrame {

	public static final int MAXWIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int MAXHEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();


	private List<? extends Polygon> objects;

	BufferedImage bi = new BufferedImage(MAXWIDTH, MAXHEIGHT, BufferedImage.TYPE_INT_RGB);

	public Canvas() {
		this.setSize(MAXWIDTH, MAXHEIGHT);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public void paint(Graphics g) {
			Graphics b = bi.getGraphics();

			b.setColor(Color.white);

			b.fillRect(0,0,MAXWIDTH,MAXHEIGHT);

			for (Polygon p : objects)
				p.paint(b);
			g = this.getGraphics();

			g.drawImage(bi, 0, 0, null);
	}

	public void update(List<? extends Polygon> p) {
		objects = p;
	}


}
