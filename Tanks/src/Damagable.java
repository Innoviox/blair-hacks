import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Damagable extends Polygon {
    public int health, max_health;
    protected HealthBar hb;
    public Damagable(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image, int health) {
        super(inShape, inPosition, inRotation, width, height, image);
        this.health = health;
        this.max_health = health;
        this.hb = new HealthBar(this);
    }

    public void updateHealth(int newHealth) {
        this.health = newHealth;
        this.max_health = newHealth;
    }


    @Override
    public void paint(Graphics brush) {
        super.paint(brush);
        this.hb.paint(brush);
    }
}
