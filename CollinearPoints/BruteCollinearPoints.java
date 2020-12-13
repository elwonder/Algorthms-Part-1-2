import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private int amount;
	private LineSegment[] segmentsArr;

	public BruteCollinearPoints(Point[] points) {
		if (points == null) throw new IllegalArgumentException();
		int N = points.length;
		for (int i = 0; i < N; i++) {
			if (points[i] == null) throw new IllegalArgumentException();
			for (int j = i + 1; j < N; j++) 
				if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException(); 
		}
		this.amount = 0;
		this.segmentsArr = new LineSegment[8];
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				for (int k = j + 1; k < N; k++) {
					for (int l = k + 1; l < N; l++) {
						if ((points[i].slopeTo(points[j]) ==
						    points[j].slopeTo(points[k])) &&
						    (points[j].slopeTo(points[k]) ==
						    points[k].slopeTo(points[l])) && 
						    (i != j && j != k && k != l)) {
							Point[] arr = new Point[4];
							arr[0] = points[i];
							arr[1] = points[j];
							arr[2] = points[k];
							arr[3] = points[l];
							Arrays.sort(arr);
							LineSegment newSegment = new LineSegment(arr[0], arr[3]);
							this.segmentsArr[amount] = newSegment;
							amount++;
							if (amount > this.segmentsArr.length * 0.75) resize();
						    }
					}
				}
			}
		}
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
	 }
}
