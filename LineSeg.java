import java.lang.Math;

public class LineSeg extends AbstractShape implements CollisionDetector {
    private Point begin;
    private Point end;
    private static int numOfInstances;

    public LineSeg() {
        numOfInstances++;
    }

    public LineSeg(Point b, Point e) {
        this.begin = b;
        this.end = e;
        numOfInstances++;
    }

    public Point getBegin() {
        return this.begin;
    }

    public Point getEnd() {
        return this.end;
    }

    public static int getNumOfInstances() {
        return numOfInstances;
    }

    public boolean intersect(Point s) {
        float x = Math.min(this.getBegin().getX(), this.getEnd().getX());
        float X = Math.max(this.getBegin().getX(), this.getEnd().getX());
        float y = Math.min(this.getBegin().getY(), this.getEnd().getY());
        float Y = Math.max(this.getBegin().getY(), this.getEnd().getY());

        if (s.getX() < x || s.getX() > X || s.getY() < y || s.getY() > Y) {
            return false;  
        }

        float dx1 = this.getEnd().getX() - this.getBegin().getX();
        float dy1 = this.getEnd().getY() - this.getBegin().getY();
        float dx2 = s.getX() - this.getBegin().getX();
        float dy2 = s.getY() - this.getBegin().getY();

        return Math.abs(dx1 * dy2 - dx2 * dy1) < 1e-10;
    }

    private static boolean checkSeg(Point x, Point y, Point z) {
        return x.getX() >= Math.min(x.getX(), z.getX()) && y.getX() <= Math.max(x.getX(), z.getX()) &&
               x.getY() >= Math.min(x.getY(), z.getY()) && y.getY() <= Math.max(x.getY(), z.getY());
    }
    
    private static int checkForOrientation(Point x, Point y, Point z) {
        int val = Float.compare((y.getY() - x.getY()) * (z.getX() - y.getX()) - (y.getX() - x.getX()) * (z.getY() - y.getY()), 0);
        return val;
    }
    
    private static boolean intersectionCheck(Point firstStart, Point firstEnd, Point secondStart, Point secondEnd) {
        int orientation1 = checkForOrientation(firstStart, firstEnd, secondStart);
        int orientation2 = checkForOrientation(firstStart, firstEnd, secondEnd);
        int orientation3 = checkForOrientation(secondStart, secondEnd, firstStart);
        int orientation4 = checkForOrientation(secondStart, secondEnd, firstEnd);
    
        if (orientation1 != orientation2 && orientation3 != orientation4) {
            return true;
        }
    
        return (orientation1 == 0 && checkSeg(firstStart, secondStart, firstEnd)) ||
               (orientation2 == 0 && checkSeg(firstStart, secondEnd, firstEnd)) ||
               (orientation3 == 0 && checkSeg(secondStart, firstStart, secondEnd)) ||
               (orientation4 == 0 && checkSeg(secondStart, firstEnd, secondEnd));
    }
    
    public boolean intersect(LineSeg s){
        return intersectionCheck(s.getBegin(), s.getEnd(), this.getBegin(), this.getEnd());
    }
    
    

    public boolean intersect(Rectangle s) {
        return s.intersect(this);
    }


    public boolean intersect(Circle s) {
        float dx = this.getEnd().getX() - this.getBegin().getX();
        float dy = this.getEnd().getY() - this.getBegin().getY();
        float temp = ((s.getCenter().getX() - this.getBegin().getX()) * dx + (s.getCenter().getY() - this.getBegin().getY()) * dy) / (dx * dx + dy * dy);
        Point closestPoint = new Point(this.getBegin().getX() + temp * dx, this.getBegin().getY() + temp * dy);
        float dTC = (float) Math.sqrt(Math.pow(s.getCenter().getX() - closestPoint.getX(), 2) + Math.pow(s.getCenter().getY() - closestPoint.getY(), 2));
        return dTC <= s.getRadius();
    }
}
