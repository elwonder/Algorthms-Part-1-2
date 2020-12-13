import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;

public class Board {

	private int[][] gameBoard;
	private int dimension;
	private int manhattan;
	private int hamming;
	private int blankX;
	private int blankY;

	// creat a board from an n-by-n array of tiles
	// where tiles[row][col] = tile at (row, col)
	public Board(int[][] tiles) 
	{
		this.dimension = tiles.length;
		this.gameBoard = new int[this.dimension][this.dimension];
		for (int i = 0; i < this.dimension; i++)
			for (int j = 0; j < this.dimension; j++) 
			{
				if (tiles[i][j] == 0) 
				{
					this.blankX = j;
					this.blankY = i;
				}
				this.gameBoard[i][j] = tiles[i][j];
			}
		this.manhattan = -1;
		this.hamming = -1;
		evaluateManhattan();
		evaluateHamming();
	}

	private void evaluateManhattan() 
	{
		this.manhattan = 0;
		for (int i = 0; i < this.dimension; i++)
			for (int j = 0; j < this.dimension; j++) 
				this.manhattan += manhattanDistance(i, j, this.gameBoard[i][j]);
	}

	private int manhattanDistance(int i, int j, int value)
	{
		if (value == 0) return 0;
		int trueI = value / this.dimension;
		if (value % this.dimension() == 0)
			trueI -= 1;
		int trueJ = this.dimension - 1;
		if (value % this.dimension != 0) 
			trueJ = (value % this.dimension) - 1;
		return Math.abs(i - trueI) + Math.abs(j - trueJ);
	}

	private void evaluateHamming()
	{
		this.hamming = 0;
		for (int i = 0; i < this.dimension; i++)
			for (int j = 0; j < this.dimension; j++) 
				if (!isCorrectPosition(i, j, this.gameBoard[i][j])) this.hamming++;
	}

	private boolean isCorrectPosition(int i, int j, int value)
	{
		if (value == 0) return true;
		return i * this.dimension + (j + 1) == value;
	}

	// string perpesentation of this board
	public String toString() 
	{
		StringBuilder s = new StringBuilder();
		s.append(this.dimension + "\n");
    	for (int i = 0; i < this.dimension; i++) 
		{
        		for (int j = 0; j < this.dimension; j++) 
           			 s.append(String.format("%2d ", this.gameBoard[i][j]));
       			s.append("\n");
   		}
    	return s.toString();
	}

	public int dimension() 
	{
		return this.dimension;
	}

	public int hamming() 
	{
		if (this.hamming == -1) evaluateHamming();
		return this.hamming;
	}

	public int manhattan()
	{
		if (this.manhattan == -1) evaluateManhattan();
		return this.manhattan;
	}

	public boolean isGoal()
	{
		return this.hamming == 0;
	}

	public boolean equals(Object y)
	{
		if (y == null) return false;
		if (this == y) return true;
		if (this.getClass() != y.getClass()) return false;
		Board that = (Board) y;
		if (this.hamming() != that.hamming()) return false;
		if (this.manhattan() != that.manhattan()) return false;
		for (int i = 0; i < this.dimension; i++) 
			for (int j = 0; j < this.dimension; j++)
				if (this.gameBoard[i][j] != that.gameBoard[i][j])
					return false;
		return true;
	}

	public Iterable<Board> neighbors() 
	{
		Stack<Board> neighborsStack = new Stack<Board>();
		if (blankY > 0) 
		{
			neighborsStack.push(getNeighbor(blankY - 1, blankX));
			swapBlank(this.gameBoard, blankY, blankX, blankY - 1, blankX);
		}
		if (blankY < this.dimension - 1) 
		{
			neighborsStack.push(getNeighbor(blankY + 1, blankX));
			swapBlank(this.gameBoard, blankY, blankX, blankY + 1, blankX);
		}
		if (blankX > 0)
		{
			neighborsStack.push(getNeighbor(blankY, blankX - 1));
			swapBlank(this.gameBoard, blankY, blankX, blankY, blankX - 1);
		}
		if (blankX < this.dimension - 1)
		{
			neighborsStack.push(getNeighbor(blankY, blankX + 1));
			swapBlank(this.gameBoard, blankY, blankX, blankY, blankX + 1);
		}
		return neighborsStack;
	}

	private Board getNeighbor(int y, int x) 
	{
		swapBlank(this.gameBoard, y, x, this.blankY, this.blankX);
		return new Board(this.gameBoard);
	}

	private void swapBlank(int[][] tiles, int x, int y, int x1, int x2)
	{
		tiles[x1][x2] = tiles[x][y];
		tiles[x][y] = 0;
	}

	private int[][] getCopy() {
		int[][] result = new int[this.dimension][this.dimension];
		for (int i = 0; i < this.dimension; i++)
			for (int j = 0; j < this.dimension; j++)
				result[i][j] = this.gameBoard[i][j];
		return result;
	}

	public Board twin()
	{
		int[][] twinTiles = getCopy();
		int firstX = 0;
		int secondX = 0;
		while (twinTiles[0][firstX] == 0)
			firstX++;
		while (twinTiles[1][secondX] == 0)
			secondX++;
		int tmp = twinTiles[1][secondX];
		twinTiles[1][secondX] = twinTiles[0][firstX];
		twinTiles[0][firstX] = tmp;
		return new Board(twinTiles);
	}

	public static void main(String[] args)
	{
		int[][] testTiles = new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
		Board testBoard = new Board(testTiles);
		for (Board s: testBoard.neighbors()) 
			System.out.println(s);
	}

}


