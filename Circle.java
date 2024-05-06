import java.lang.Math;

public class Circle extends AbstractShape implements CollisionDetector {
    private Point center;
    private float radius;
    private static int numOfInstances;

    public Circle() {
        numOfInstances++;
    }

    public Circle(Point c, float r) {
        this.center = c;
        this.radius = r;
        numOfInstances++;
    }

    public Point getCenter() {
        return this.center;
    }

    public float getRadius() {
        return this.radius;
    }

    public static int getNumOfInstances() {
        return numOfInstances;
    }

    public boolean intersect(Point s) {
        return s.intersect(this);
    }


    public boolean intersect(LineSeg s) {
        return s.intersect(this);
    }



    public boolean intersect(Rectangle s) {
        return s.intersect(this);
    }


     public boolean intersect(Circle s) {
         float dx = this.center.getX() - s.getCenter().getX();
         float dy = this.center.getY() - s.getCenter().getY();
         float distance = (float)Math.sqrt(dx * dx + dy * dy);

         return distance < this.radius + s.getRadius();
     }
}
