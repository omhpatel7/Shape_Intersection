import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class BlackboxWithoutJUnit
{
    private static void assertEquals(float a, float b, float eps) {
        if ((Math.abs(a - b) < eps)) return;
        throw new AssertionError();
    }

    private static void assertTrue(boolean value) {
        if (value) return;
        throw new AssertionError();
    }

    private static void assertFalse(boolean value) {
        if (!value) return;
        throw new AssertionError();
    }

    private BlackboxWithoutJUnit() {}

    public static void runTests() {
        BlackboxWithoutJUnit runner = new BlackboxWithoutJUnit();
        int testsCompleted = 0;
        int testsFailed = 0;

        for (Method m : runner.getClass().getMethods()) {
            if (!m.isAnnotationPresent(Test.class)) continue;

            try {
                m.invoke(runner);
                testsCompleted++;
            } catch (Exception e) {
                System.out.println("Test failed: " + m.getName());
                testsFailed++;
            }
        }

        System.out.println("Testing finished with " + testsCompleted + " successful and " + testsFailed + " failed.");
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Test {}

    public static float eps = 0.0001f;

    // Blackbox tests
    @Test
    public void pointConstructor() {
        Point p = new Point(1, 2);

        assertEquals(p.getX(), 1, eps);
        assertEquals(p.getY(), 2, eps);
    }

    @Test
    public void lineSegConstructor() {
        Point b = new Point(1, 2);
        Point e = new Point(3, 4);

        LineSeg s = new LineSeg(b, e);

        assertEquals(s.getBegin().getX(), b.getX(), eps);
        assertEquals(s.getBegin().getY(), b.getY(), eps);
        assertEquals(s.getEnd().getX(), e.getX(), eps);
        assertEquals(s.getEnd().getY(), e.getY(), eps);
    }

    @Test
    public void rectangleConstructor() {
        Rectangle r = new Rectangle(1, 2, 3, 4);

        assertEquals(r.getLeft(), 1, eps);
        assertEquals(r.getRight(), 2, eps);
        assertEquals(r.getTop(), 3, eps);
        assertEquals(r.getBottom(), 4, eps);
    }

    @Test
    public void circleConstructor() {
        Point center = new Point(1, 2);
        Circle c = new Circle(center, 3f);

        assertEquals(c.getCenter().getX(), center.getX(), eps);
        assertEquals(c.getCenter().getY(), center.getY(), eps);
        assertEquals(c.getRadius(), 3f, eps);
    }

    // Point intersections

    @Test
    public void pointIntersectPoint() {
        assertTrue(new Point(1, 2).intersect(new Point(1, 2)));
    }

    @Test
    public void pointIntersectLineStraight() {
        assertTrue(new Point(2, 0).intersect(new LineSeg(new Point(1, 0), new Point(3, 0))));
        assertFalse(new Point(4, 0).intersect(new LineSeg(new Point(1, 0), new Point(3, 0))));
    }

    @Test
    public void pointIntersectLineAngled() {
        assertTrue(new Point(1, 2).intersect(new LineSeg(new Point(0, 1), new Point(3, 4))));
        assertFalse(new Point(2, 2).intersect(new LineSeg(new Point(0, 1), new Point(3, 4))));
    }

    @Test
    public void pointIntersectLineExtremes() {
        LineSeg l = new LineSeg(new Point(1, 0), new Point(3, 0));
        assertTrue(new Point(1, 0).intersect(l));
        assertTrue(new Point(3, 0).intersect(l));
    }

    @Test
    public void pointIntersectCircle() {
        assertTrue(new Point(1, 1).intersect(new Circle(new Point(0, 0), 5f)));
        assertFalse(new Point(10, 1).intersect(new Circle(new Point(0, 0), 5f)));
    }

    @Test
    public void pointIntersectCircleTangent() {
        assertTrue(new Point(0, 5).intersect(new Circle(new Point(0, 0), 5f)));
        assertTrue(new Point(0, 3).intersect(new Circle(new Point(0, 1), 2f)));
        assertFalse(new Point(0, 5.01f).intersect(new Circle(new Point(0, 0), 5f)));
        assertFalse(new Point(0, 3.01f).intersect(new Circle(new Point(0, 1), 2f)));
    }

    @Test
    public void pointIntersectRect() {
        assertTrue(new Point(2, 2).intersect(new Rectangle(1, 3, 3, 1)));
        assertFalse(new Point(2, 6).intersect(new Rectangle(1, 3, 3, 1)));
    }

    @Test
    public void pointIntersectRectTangent() {
        assertTrue(new Point(1, 2).intersect(new Rectangle(1, 3, 3, 1)));
        assertTrue(new Point(1, 3).intersect(new Rectangle(1, 3, 3, 1)));
        assertFalse(new Point(0.99f, 2).intersect(new Rectangle(1, 3, 3, 1)));
    }

    // LineSeg intersections

    @Test
    public void lineIntersectPoint() {
        assertTrue(new LineSeg(new Point(0, 0), new Point(3, 3)).intersect(new Point(1, 1)));
        assertTrue(new LineSeg(new Point(-1, 2), new Point(-3, 4)).intersect(new Point(-2, 3)));
        assertFalse(new LineSeg(new Point(1, 2), new Point(-3, 4)).intersect(new Point(-2, 3)));
    }

    @Test
    public void lineIntersectLine() {
        // two diagonals
        assertTrue(new LineSeg(new Point(0, 0), new Point(3, 3)).intersect(new LineSeg(new Point(0, 3), new Point(3, 0))));

        // two lines on top of each other
        assertTrue(new LineSeg(new Point(0, 3), new Point(3, 3)).intersect(new LineSeg(new Point(0, 3), new Point(3, 3))));

        // two lines aligned with the axis
        assertTrue(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new LineSeg(new Point(0, -3), new Point(0, 3))));

        // one diagonal, one aligned to the axis
        assertTrue(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new LineSeg(new Point(-3, -3), new Point(3, 3))));

        // non-overlapping intersection
        assertFalse(new LineSeg(new Point(0, 0), new Point(3, 3)).intersect(new LineSeg(new Point(0, 3), new Point(-3, 0))));

        // overlapping bounds intersection
        assertFalse(new LineSeg(new Point(0, 0), new Point(3, 3)).intersect(new LineSeg(new Point(1, 0), new Point(2, 1))));
    }

    @Test
    public void lineIntersectCircle() {
        assertTrue(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new Circle(new Point(0, 0), 2f)));
        assertTrue(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new Circle(new Point(0, 0), 8f)));
        assertFalse(new LineSeg(new Point(-3, 10), new Point(3, 10)).intersect(new Circle(new Point(0, 0), 8f)));
    }

    @Test
    public void lineIntersectCircleTangent() {
        assertTrue(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new Circle(new Point(0, -2), 2f)));
        assertFalse(new LineSeg(new Point(-3, 0), new Point(3, 0)).intersect(new Circle(new Point(0, -2.01f), 2f)));
    }

    @Test
    public void lineIntersectRect() {
        assertTrue(new LineSeg(new Point(-3, 1), new Point(3, 1)).intersect(new Rectangle(0, 2, 2, -1)));
        assertTrue(new LineSeg(new Point(-3, 1), new Point(3, 1)).intersect(new Rectangle(-4, 4, 2, -1)));
        assertFalse(new LineSeg(new Point(-3, 3), new Point(3, 3)).intersect(new Rectangle(-4, 4, 2, -1)));
    }

    @Test
    public void lineIntersectRectTangent() {
        assertTrue(new LineSeg(new Point(-3, 1), new Point(0, 0)).intersect(new Rectangle(0, 2, 2, -1)));
        assertTrue(new LineSeg(new Point(-3, 1), new Point(0, -1)).intersect(new Rectangle(0, 2, 2, -1)));
        assertFalse(new LineSeg(new Point(-3, 1), new Point(0, -1.01f)).intersect(new Rectangle(0, 2, 2, -1)));
    }

    // Circle intersections

    @Test
    public void circleIntersectPoint() {
        assertTrue(new Circle(new Point(1, 2), 3f).intersect(new Point(1, 2)));
        assertTrue(new Circle(new Point(-1, 2), 3f).intersect(new Point(-1, 3)));
        assertTrue(new Circle(new Point(-1, 2), 3f).intersect(new Point(-2, 2)));
        assertFalse(new Circle(new Point(1, 2), 3f).intersect(new Point(2, 8)));
        assertFalse(new Circle(new Point(1, 2), 3f).intersect(new Point(2, -8)));
    }

    public void circleIntersectPointRadius() {
        // make sure circle is implemented using radius, not diameter
        assertTrue(new Circle(new Point(1, 2), 2f).intersect(new Point(1, 3)));
        assertFalse(new Circle(new Point(1, 2), 2f).intersect(new Point(1, 5)));
    }

    @Test
    public void circleIntersectTangent() {
        assertTrue(new Circle(new Point(1, 2), 3f).intersect(new Point(1, 5)));
        assertTrue(new Circle(new Point(-1, 2), 3f).intersect(new Point(-4, 2)));
    }

    @Test
    public void circleIntersectLine() {
        assertTrue(new Circle(new Point(0, 0), 2f).intersect(new LineSeg(new Point(-3, 0), new Point(3, 0))));
        assertTrue(new Circle(new Point(0, 0), 8f).intersect(new LineSeg(new Point(-3, 0), new Point(3, 0))));
        assertFalse(new Circle(new Point(0, 0), 8f).intersect(new LineSeg(new Point(-3, 10), new Point(3, 10))));
    }

    @Test
    public void circleIntersectLineTangent() {
        assertTrue(new Circle(new Point(0, -2), 2f).intersect(new LineSeg(new Point(-3, 0), new Point(3, 0))));
        assertFalse(new Circle(new Point(0, -2.01f), 2f).intersect(new LineSeg(new Point(-3, 0), new Point(3, 0))));
    }

    @Test
    public void circleIntersectCircle() {
        assertTrue(new Circle(new Point(-1, 0), 3f).intersect(new Circle(new Point(0, 0), 1f)));
        assertFalse(new Circle(new Point(-1, 0), 3f).intersect(new Circle(new Point(4, 0), 1f)));
    }

    @Test
    public void circleIntersectCircleTangent() {
        assertFalse(new Circle(new Point(-2, 0), 2f).intersect(new Circle(new Point(2, 0), 2f)));
    }

    @Test
    public void circleIntersectRectEnclosed() {
        // fully surrounded
        assertTrue(new Circle(new Point(0, 0), 1f).intersect(new Rectangle(-3, 3, 3, -3)));
        assertTrue(new Circle(new Point(0, 0), 20f).intersect(new Rectangle(-3, 3, 3, -3)));
    }

    @Test
    public void circleIntersectRect() {
        // touching on one edge
        assertTrue(new Circle(new Point(-4, 0), 3f).intersect(new Rectangle(-3, 3, 3, -3)));
        assertFalse(new Circle(new Point(-7, 0), 3f).intersect(new Rectangle(-3, 3, 3, -3)));
    }

    @Test
    public void circleIntersectRectTangent() {
        assertFalse(new Circle(new Point(-3, 0), 3f).intersect(new Rectangle(0, 3, 3, -3)));
    }

    // Rectangle intersections
    @Test
    public void rectIntersectPoint() {
        assertTrue(new Rectangle(-1, 1, 1, -1).intersect(new Point(0, 0)));
        assertFalse(new Rectangle(-1, 1, 1, -1).intersect(new Point(2, 0)));
    }

    @Test
    public void rectIntersectLineEnclosed() {
        // enclosed
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(0, 0), new Point(1, 0))));
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(4, 0), new Point(6, 0))));
    }

    @Test
    public void rectIntersectLine() {
        // touching on one edge
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(-4, 0), new Point(-2, 0))));

        // touching on two edges
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(-4, 0), new Point(4, 0))));

        // touching on diagonal
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(0, 0), new Point(4, 4))));

        // not touching on diagonal
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(2, -7), new Point(5, -3))));
    }

    @Test
    public void rectIntersectLineTangent() {
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(-4, 0), new Point(-3, 0))));
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(-4, -4), new Point(-2, -6))));
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new LineSeg(new Point(-4, -4), new Point(-2, -7))));
    }

    @Test
    public void rectIntersectCircleEnclosed() {
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(0, 0), 1f)));
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(0, 0), 20f)));
    }

    @Test
    public void rectIntersectCircle() {
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(-3, 0), 1f)));
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(-4, 0), 3f)));

        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(-9, 0), 3f)));
    }

    @Test
    public void rectIntersectCircleTangent() {
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new Circle(new Point(4, 0), 1f)));
    }

    @Test
    public void rectIntersectRectEnclosed() {
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-1, 1, 1, -1)));
    }

    @Test
    public void rectIntersectRect() {
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-8, 0, 3, 1)));
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-3, 3, 5, -5)));

        // by single regions
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-2, 2, 6, 4))); // top
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-2, 2, -4, -6))); // bottom
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-4, -2, 3, -2))); // left
        assertTrue(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(2, 4, 2, -3))); // right

        assertFalse(new Rectangle(-5, -4, 5, 4).intersect(new Rectangle(4, 5, 3, 2)));
        assertFalse(new Rectangle(-3, 3, 1, 0).intersect(new Rectangle(-3, 3, 3, 2)));
        assertFalse(new Rectangle(-3, 0, 1, 0).intersect(new Rectangle(1, 3, 3, 2)));
    }

    @Test
    public void rectIntersectRectTangent() {
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(3, 4, 5, -5)));
        assertFalse(new Rectangle(-3, 3, 5, -5).intersect(new Rectangle(-4, -3, 5, -5)));
    }
}
