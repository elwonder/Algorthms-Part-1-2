import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int[][] Grid;
	private WeightedQuickUnionUF Union;
	private boolean[] State;
	private int n;
	private int openSites;
	private int VirtualBottom;
	private int VirtualTop;

	//creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		this.Grid = new int[n+1][n+1];
		this.State = new boolean[n*n+1];
		this.n = n;
		this.openSites = 0;
		this.Union = new WeightedQuickUnionUF(n*n + 2);
		this.VirtualTop = 0;
		this.VirtualBottom = n*n + 1;
		int num = 1;
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= n; j++) { 
				this.Grid[i][j] = num;
				this.State[num] = false;
				if (i == 1) this.Union.union(num, VirtualTop);
				if (i == n) this.Union.union(num, VirtualBottom);
				num++;
			}
		}

	}

	private void checkBounds(int x) {
		if (x < 1 || x > n) throw new IllegalArgumentException("Illegal arguments");
	}

	private void unite(int a, int b) {
		this.Union.union(a, b);
	}


	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		checkBounds(row);
		checkBounds(col);
		if (isOpen(row, col)) return;
		State[Grid[row][col]] = true;
		this.openSites += 1;
		if ((row - 1 > 0) && isOpen(row - 1, col))
			unite(this.Grid[row][col], this.Grid[row-1][col]);
		if ((col - 1 > 0) && isOpen(row, col - 1))
			unite(this.Grid[row][col], this.Grid[row][col-1]);
		if ((row + 1 <= n) && isOpen(row + 1, col))
			unite(this.Grid[row][col], this.Grid[row+1][col]);
		if ((col + 1 <= n) && isOpen(row, col + 1))
			unite(this.Grid[row][col], this.Grid[row][col+1]);
	}

	
	// is the site (row, col) open?
	public boolean isOpen(int row, int col) { 
		checkBounds(row);
		checkBounds(col);
		return State[Grid[row][col]];
	}


	// is the site (row col) full?
	public boolean isFull(int row, int col) { 
		checkBounds(row);
		checkBounds(col); 
		if (this.Union.connected(this.Grid[row][col], this.VirtualTop) && isOpen(row, col)) return true;
		return false;
	}


	// returns the number of open sites
	public int numberOfOpenSites() { 
		return this.openSites; 
	}


	// does the system percolate?
	public boolean percolates() { 
		if (this.n == 1 && !this.isOpen(1, 1)) return false;
		return this.Union.connected(this.VirtualTop, this.VirtualBottom);
	}


	// test client (optional)
	public static void main(String[] args) {
	}

}
