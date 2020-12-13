import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private int size;
    private SET<Point2D> pointSet;

    public PointSET() 
    {
        this.size = 0;
        this.pointSet = new SET<Point2D>();
    }

    public boolean isEmpty() 
    {
        return this.size == 0;
    }

    public int size() 
    {
        return this.size;
    }

    public void insert(Point2D p) 
    {
        if (p == null) throw new IllegalArgumentException();
        if (!this.pointSet.contains(p)) 
        {
            this.pointSet.add(p);
            this.size++;
        }
    }

    public boolean contains(Point2D p) 
    {
        if (p == null) throw new IllegalArgumentException();
        return this.pointSet.contains(p);
    }

    public void draw() 
    {
        for (Point2D p: this.pointSet)
            p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) 
    {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> result = new SET<Point2D>();
        for (Point2D p: this.pointSet) 
        {
            if (rect.contains(p))
                result.add(p);
        }
        return result;
    }

    public Point2D nearest(Point2D p) 
    {
        if (p == null) throw new IllegalArgumentException();
        double nearestDistance = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D p2: this.pointSet) 
        {
            if (p.distanceTo(p2) < nearestDistance)
            {
                nearestDistance = p.distanceTo(p2);
                nearestPoint = p2;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args)
    {

    }
}
