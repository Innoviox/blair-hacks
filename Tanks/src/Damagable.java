import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Damagable extends Polygon {
    public int health, max_health;
    public Damagable(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, int health) {
        super(inShape, inPosition, inRotation, width, height, image);
        this.health = health;
        this.max_health = health;
    }

    public void updateHealth(int newHealth) {
        this.health = newHealth;
        this.max_health = newHealth;
    }

    public void paintHealthBar(Graphics brush) {
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        int back = (int)position.x;// - (width / 2);
        xPoints[0] = back;
        xPoints[1] = back;
        xPoints[2] = back + max_health;
        xPoints[3] = back + max_health;

        int bottom = (int)position.y;// - height;
        yPoints[0] = bottom;
        yPoints[1] = bottom + 10;
        yPoints[2] = bottom + 10;
        yPoints[3] = bottom;

        brush.setColor(Color.black);
        brush.drawPolygon(xPoints, yPoints, 4);

        xPoints[2] = back + health;
        xPoints[3] = back + health;

        brush.setColor(color.green);
        brush.fillPolygon(xPoints, yPoints, 4);
    }

    @Override
    public void paint(Graphics brush) {
        super.paint(brush);
        paintHealthBar(brush);
    }
}
