import java.lang.Math;

public class Point extends AbstractShape implements CollisionDetector {
    private float x = 0;
    private float y = 0;
    private static int numOfInstances;

    public Point() {
    	numOfInstances++;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        numOfInstances++;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public static int getNumOfInstances() {
        return numOfInstances;
    }

    public boolean intersect(Point s) {
        return Math.abs(this.x - s.getX()) < 1e-10 && Math.abs(this.y - s.getY()) < 1e-10;
    }

    public boolean intersect(LineSeg s) {
        return s.intersect(this);
    }



    public boolean intersect(Rectangle s) {
        return s.intersect(this);
    }


    public boolean intersect(Circle s) {
        float dx = this.getX() - s.getCenter().getX();
        float dy = this.getY() - s.getCenter().getY();
        float dis = dx * dx + dy * dy;

        return dis <= (s.getRadius() * s.getRadius());
    }

}
