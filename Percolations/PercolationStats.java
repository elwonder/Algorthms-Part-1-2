import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
	private double[] samples;
	private double mean;
	private double stddev;
	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n < 1 || trials < 1) throw new IllegalArgumentException("Illegal arguments");
		this.samples = new double[trials];
		for (int i = 0; i < trials; i++) {
			Percolation trial = new Percolation(n);
			double amount = n*n;
			while (!trial.percolates()) {
				trial.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
			}
			samples[i] = ((double) trial.numberOfOpenSites())/(amount);
		}
		this.mean = StdStats.mean(this.samples);
		this.stddev = StdStats.stddev(this.samples);

	}

	// sample mean of percolation threshold
	public double mean() {
		return this.mean;
	}

	//sample standard deviation of percolation threshold
	public double stddev() {
		if (this.samples.length == 1) return Double.NaN;
		return this.stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return this.mean() - (1.96*this.stddev())/Math.sqrt(this.samples.length);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return this.mean() + (1.96*this.stddev())/Math.sqrt(this.samples.length);
	}

	// test client 
	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats test = new PercolationStats(n, T);
		System.out.println("mean = " + test.mean());
		System.out.println("stddev = " + test.stddev());
		System.out.println("95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi() + "]");
	}

}
