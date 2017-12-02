public class Camera {
    private double x;

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    private double y;

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private final Tank tank;

    public Camera(Tank tank) {
        x = 0;
        y = 0;
        this.tank = tank;
    }

    public void update() {
        this.x = tank.position.getX();
        this.y = tank.position.getY();
    }
}
