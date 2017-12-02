import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Polygon {
    public Point[] shape;   // An array of points.
    public Point position;   // The offset mentioned above.

    public double rotation; // Zero degrees is due east.
    public int width, height;
    public BufferedImage img;
    public Color color;

    public Polygon(Point[] inShape, Point inPosition, double inRotation, int width, int height, BufferedImage image) {
        shape = inShape;
        position = inPosition;
        rotation = inRotation;

        // First, we find the shape's top-most left-most boundary, its origin.
        Point origin = shape[0].clone();
        for (Point p : shape) {
            if (p.x < origin.x) origin.x = p.x;
            if (p.y < origin.y) origin.y = p.y;
        }

        // Then, we orient all of its points relative to the real origin.
        for (Point p : shape) {
            p.x -= origin.x;
            p.y -= origin.y;
        }
        this.width = width;
        this.height = height;
        this.img = image;
    }
    // "getPoints" applies the rotation and offset to the shape of the polygon.
    public Point[] getPoints() {
        Point center = findCenter();
        Point[] points = new Point[shape.length];
        for (int i = 0; i < shape.length; i++) {
//	    for (old.Point p : shape) {
            Point p = shape[i];
            double x = ((p.x-center.x) * Math.cos(Math.toRadians(rotation)))
                    - ((p.y-center.y) * Math.sin(Math.toRadians(rotation)))
                    + center.x/2 + position.x;
            double y = ((p.x-center.x) * Math.sin(Math.toRadians(rotation)))
                    + ((p.y-center.y) * Math.cos(Math.toRadians(rotation)))
                    + center.y/2 + position.y;
            points[i] = new Point(x,y);
        }
        return points;
    }

    // "contains" implements some magical math (i.e. the ray-casting algorithm).
    public boolean contains(Point point) {
        Point[] points = getPoints();
        double crossingNumber = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            if ((((points[i].x < point.x) && (point.x <= points[j].x)) ||
                    ((points[j].x < point.x) && (point.x <= points[i].x))) &&
                    (point.y > points[i].y + (points[j].y-points[i].y)/
                            (points[j].x - points[i].x) * (point.x - points[i].x))) {
                crossingNumber++;
            }
        }
        return crossingNumber%2 == 1;
    }

    public void rotate(int degrees) {rotation = (rotation+degrees)%360;}

    public void translate(Point cameraTranslation) {
        position.x -= cameraTranslation.x;
        position.y -= cameraTranslation.y;
    }
    public void paint(Graphics brush, Point cameraTranslation) {
        translate(cameraTranslation);
        Point[] points = this.getPoints();
        int pl = points.length;
        int[] x = new int[pl];
        int[] y = new int[pl];
        for (int i = 0; i < pl; i++) {
            x[i] = (int)points[i].getX();
            y[i] = (int)points[i].getY();
        }
        brush.setColor(Color.blue);
        brush.fillPolygon(x, y, pl);
        /*
        AffineTransform at = new AffineTransform();

        at.translate(position.x - img.getWidth() / 4, position.y - img.getHeight() / 4);
        at.rotate(Math.toRadians(rotation), findCenter().x, findCenter().y);

        Graphics2D g2d = (Graphics2D) brush;
        g2d.drawImage(img, at, null);
        */
    }

    /**
     * Check if this polygon is colliding with another
     * This method checks by comparing to see if
     * the corners are inside the other and if
     * the center is inside the other. It is
     * inexact but close enough that the user
     * won't notice a small discrepancy.
     * @param other old.Polygon to check
     * @return collision?
     */
    public boolean collides(Polygon other) {
        for (Point p: other.getPoints()) {
            if (contains(p)) return true;
        }
        for (Point p: getPoints()) {
            if (other.contains(p)) return true;
        }
        if (contains(other.position)) return true;
        if (other.contains(position)) return true;
        return false;
    }

    public abstract void move(); //move the polygon
    public abstract Polygon[] destroy(); //destroy the polygon

    public void update() {move();}
	  /*
	  The following methods are private access restricted because, as this access
	  level always implies, they are intended for use only as helpers of the
	  methods in this class that are not private. They can't be used anywhere else.
	  */

    // "findArea" implements some more magic math.
    private double findArea() {
        double sum = 0;
        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            sum += shape[i].x*shape[j].y-shape[j].x*shape[i].y;
        }
        return Math.abs(sum/2);
    }

    // "findCenter" implements another bit of math.
    protected Point findCenter() {
        Point sum = new Point(0,0);
        for (int i = 0, j = 1; i < shape.length; i++, j=(j+1)%shape.length) {
            sum.x += (shape[i].x + shape[j].x)
                    * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
            sum.y += (shape[i].y + shape[j].y)
                    * (shape[i].x * shape[j].y - shape[j].x * shape[i].y);
        }
        double area = findArea();
        return new Point(Math.abs(sum.x/(6*area)),Math.abs(sum.y/(6*area)));
    }

    /**
     * Generate a random number from min to max, used in calculating shape
     * @param min lower bound
     * @param max upper bound
     * @return int randomly generated number
     */
    public static int randint(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
