public class Point implements Cloneable {
    public double x,y;
    public Point(double inX, double inY) { x = inX; y = inY; }

    public double getX(){ return x;}
    public double getY(){ return y;}
    public void setX(double x){ this.x = x;}
    public void setY(double y){ this.y = y;}

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }

    public Point clone() {
        return new Point(x, y);
    }
    public String toString() { return x + "," + y;}
}