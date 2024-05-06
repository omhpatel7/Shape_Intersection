import java.lang.Math;

public class Rectangle extends AbstractShape implements CollisionDetector {
	private float left;
    private float right;
    private float top;
    private float bottom;
    private static int numOfInstances;

    public Rectangle() {
        numOfInstances++;
    }

    public Rectangle(float l, float r, float t, float b) {
        this.left = l;
        this.right = r;
        this.top = t;
        this.bottom = b;
        numOfInstances++;
    }

    public float getLeft() {
        return this.left;
    }

    public float getRight() {
        return this.right;
    }

    public float getTop() {
        return this.top;
    }

    public float getBottom() {
        return this.bottom;
    }

    public static int getNumOfInstances() {
        return numOfInstances;
    }


    public boolean intersect(Point s) {
        return s.getX() >= this.getLeft() && s.getX() <= this.getRight() &&
               s.getY() >= this.getBottom() && s.getY() <= this.getTop();
    }


    public boolean intersect(LineSeg s) {
    
        float x1 = s.getBegin().getX();
        float y1 = s.getBegin().getY();
        float x2 = s.getEnd().getX();
        float y2 = s.getEnd().getY();

        boolean check1 = checkIntersection(x1, y1, x2, y2, left, top, right, top);
        boolean check2 = checkIntersection(x1, y1, x2, y2, right, top, right, bottom);
        boolean check3 = checkIntersection(x1, y1, x2, y2, right, bottom, left, bottom);
        boolean check4 = checkIntersection(x1, y1, x2, y2, left, bottom, left, top);

        return check1 || check2 || check3 || check4;
    }

    private boolean checkIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        float temp = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        float q, w;

        if (temp == 0) {
            return false;
        } else{
            q = Math.abs(((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / temp);
            w = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / temp;
        }

        return q >= 0 && q <= 1 && w >= 0 && w <= 1;
    }

    public boolean intersect(Rectangle s) {
        return left < s.getRight() && right > s.getLeft() && top > s.getBottom() && bottom < s.getTop();
    }

    public boolean intersect(Circle s) {
        float x = Math.max(this.left, Math.min(s.getCenter().getX(), this.right));
        float y = Math.max(this.bottom, Math.min(s.getCenter().getY(), this.top));

        float dx = s.getCenter().getX() - x;
        float dy = s.getCenter().getY() - y;
        float squared = dx * dx + dy * dy;

        boolean intersecting = squared < (s.getRadius() * s.getRadius());

        return intersecting;
    }
    

}
