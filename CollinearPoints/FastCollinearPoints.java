import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private int amount;
	private LineSegment[] segmentsArr;

	private class Pair implements Comparable<Pair>{
		private Point point;
		private double slope;

		public Pair(Point point, double slope) {
			this.point = point;
			this.slope = slope;
		}

		public int compareTo(Pair that) {
			if (this.slope < that.slope) return -1;
			if (this.slope == that.slope) return 0;
			return 1;
		}

		public double getSlope() { return this.slope; }

		public Point getPoint() { return this.point; }

		public String toString() {
			return point + " " + slope;
		}
	}

	public FastCollinearPoints(Point[] points) {
		if (points == null) throw new IllegalArgumentException();
		int N = points.length;
		for (int i = 0; i < N; i++) {
			if (points[i] == null) throw new IllegalArgumentException();
			for (int j = i + 1; j < N; j++) {
				if (points[j] == null) throw new IllegalArgumentException();
				if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException(); 
			}
		}
		Arrays.sort(points);
		this.amount = 0;
		this.segmentsArr = new LineSegment[8];
		for (int i = 0; i < N; i++) {
			Pair[] slopes = new Pair[N];
			for (int j = 0; j < N; j++) {
				Pair newEntry = new Pair(points[j], points[i].slopeTo(points[j]));
				slopes[j] = newEntry;
			}
			process(slopes, points[i]);
		}
	}


	private void process(Pair[] slopes, Point point) {
		Arrays.sort(slopes);
		for (int i = 0; i < slopes.length;) {
			double current = slopes[i].getSlope();
			int size = 1;
			while (i+size < slopes.length && slopes[i + size].getSlope() == current) {
				size++;
			}
			if (size >= 3) {
				Point[] arr = new Point[size + 1];
				arr[0] = point;
				for (int j = 1; j < size + 1; j++) {
					arr[j] = slopes[i + j - 1].getPoint();
				}
				Arrays.sort(arr);
				LineSegment newEntry = new LineSegment(arr[0], arr[size]);
				pushEntry(newEntry);
			}
			i += size;
		}

	}

	private void pushEntry(LineSegment entry) {
		if (amount > this.segmentsArr.length*0.75) resize();
		for (int i = 0; i < this.amount; i++)
			if (this.segmentsArr[i].toString().equals(entry.toString())) return;
		this.segmentsArr[this.amount] = entry;
		this.amount++;
	}

	private void resize() {
		LineSegment[] renew = new LineSegment[this.segmentsArr.length * 2];
		for (int i = 0; i < this.segmentsArr.length; i++)
			renew[i] = this.segmentsArr[i];
		this.segmentsArr = renew;
	}

	public int numberOfSegments() {
		return amount;
	}

	public LineSegment[] segments() {
		LineSegment[] result = new LineSegment[this.amount];
		for (int i = 0; i < this.amount; i++) 
			result[i] = this.segmentsArr[i];
		return result;
	}
	public static void main(String[] args) {

    	/*	// read the n points from a file
    		In in = new In(args[0]);
    		int n = in.readInt();
    		Point[] points = new Point[n];
    		for (int i = 0; i < n; i++) {
        		int x = in.readInt();
        		int y = in.readInt();
        		points[i] = new Point(x, y);
    		}

    		// draw the points
    		StdDraw.enableDoubleBuffering();
    		StdDraw.setXscale(0, 32768);
   	 	StdDraw.setYscale(0, 32768);
    		for (Point p : points) {
        		p.draw();
    		}
    		StdDraw.show();

    		// print and draw the line segments
    		FastCollinearPoints collinear = new FastCollinearPoints(points);
    		for (LineSegment segment : collinear.segments()) {
        		StdOut.println(segment);
        		segment.draw();
    		}
    		StdDraw.show(); 
		*/
	}
}
		


