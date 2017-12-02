import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bullet extends Polygon {
    public double step = 15; //speed
    public int lifetime = 40; //how long it stays on the screen
    public int counter = 0; //how long it's been alive

    public Bullet(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, double stepinc) {
        super(inShape, inPosition, inRotation, width, height, image);	 //call super constructor
        step += stepinc; //increase speed
        color = Color.green;
    }

    public void move() {
        position.setX(((position.getX() + Math.cos(Math.toRadians(rotation)) * (step))));
        position.setY(((position.getY() + Math.sin(Math.toRadians(rotation)) * (step))));
        counter++;
    }

    public Polygon[] destroy() {
        return new Polygon[] {};
    }

    @Override
    public void paint(Graphics brush) {
        Point[] points = this.getPoints();
        int pl = points.length;
        int[] x = new int[pl];
        int[] y = new int[pl];
        for (int i = 0; i < pl; i++) {
            x[i] = (int)points[i].getX();
            y[i] = (int)points[i].getY();
        }
        brush.fillPolygon(x, y, pl);
    }
}
