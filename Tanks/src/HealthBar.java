import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class HealthBar extends Polygon {
    private Damagable obj;
    private static final int BASE = -20;
    private static final Point[] BAR_SHAPE = new Point[] {
            new Point(0, 0),
            new Point(0, 10),
            new Point(10, 10),
            new Point(10, 0)
    };
    public HealthBar(Damagable obj) {
        super(BAR_SHAPE, obj.position, obj.rotation, obj.max_health, obj.height, null);
        this.shape[2].x = obj.max_health;
        this.shape[3].x = obj.max_health;
        this.obj = obj;
    }

    @Override
    public void move() {
        this.rotation = obj.rotation;
        this.position = obj.position.clone();
    }

    @Override
    public Polygon[] destroy() {
        return new Polygon[0];
    }

    @Override
    public void paint(Graphics brush, Point cameraTranslation) {
        shape = new Point[] {
                new Point(0, BASE),
                new Point(0, BASE + 10),
                new Point(obj.max_health / 2, BASE + 10),
                new Point(obj.max_health / 2, BASE)
        };
        Point[] points;
        int pl;
        int[] x;
        int[] y;


        shape[2] = new Point(obj.health / 2, BASE + 10);
        shape[3] = new Point(obj.health / 2, BASE);
        points = this.getPoints();
        pl = points.length;
        x = new int[pl];
        y = new int[pl];
        for (int i = 0; i < pl; i++) {
            x[i] = (int)points[i].getX();
            y[i] = (int)points[i].getY();
        }
        brush.setColor(color.green);
        brush.fillPolygon(x, y, pl);
    }
}