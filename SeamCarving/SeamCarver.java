import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver 
{
	private Picture picture;
	private int height;
	private int width;
	private double[][] energy;

	public SeamCarver(Picture picture)
	{
		if (picture == null) throw new IllegalArgumentException();
		this.picture = new Picture(picture);
		this.height = this.picture.height();
		this.width = this.picture.width();
		this.energy = new double[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				this.energy[i][j] = computeEnergy(i, j);

	}

	private void updateEnergy() 
	{
		this.energy = new double[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				this.energy[i][j] = computeEnergy(i, j);
	}

	public Picture picture()
	{
		return new Picture(this.picture);
	}

	public int width()
	{
		return this.width;
	}

	public int height()
	{
		return this.height;
	}

	private double computeEnergy(int x, int y)
	{
		if (checkHorizontalBorder(x) || checkVerticalBorder(y)) return 1000;
		return Math.sqrt(horizontalGradient(x, y) + verticalGradient(x, y)); 
	}

	public double energy(int x, int y)
	{
		if ((x < 0 || x > this.width-1) || (y < 0 || y > this.height-1)) throw new IllegalArgumentException();
		return this.energy[x][y];
	}

	private int horizontalGradient(int x, int y)
	{
		Color leftNeighbor = this.picture.get(x - 1, y);
		Color rightNeighbor = this.picture.get(x + 1, y);
		int r_x = rightNeighbor.getRed() - leftNeighbor.getRed();
		int g_x = rightNeighbor.getGreen() - leftNeighbor.getGreen();
		int b_x = rightNeighbor.getBlue() - leftNeighbor.getBlue();
		return r_x*r_x + g_x*g_x + b_x*b_x;
	}

	private int verticalGradient(int x, int y)
	{
		Color upperNeighbor = this.picture.get(x, y - 1);
		Color lowerNeighbor = this.picture.get(x, y + 1);
		int r_y = lowerNeighbor.getRed() - upperNeighbor.getRed();
		int g_y = lowerNeighbor.getGreen() - upperNeighbor.getGreen();
		int b_y = lowerNeighbor.getBlue() - upperNeighbor.getBlue();
		return r_y*r_y + g_y*g_y + b_y*b_y;
	}


	private boolean checkHorizontalBorder(int x)
	{
		return (x == 0) || (x == (this.width - 1));
	}

	private boolean checkVerticalBorder(int y)
	{
		return (y == 0) || (y == (this.height - 1));
	}

	public int[] findHorizontalSeam()
	{
		double[][]distTo = new double[this.width][this.height];
		int[][] edgeTo = new int[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				distTo[i][j] = Double.POSITIVE_INFINITY;
		for (int y = 0; y < this.height; y++)
			distTo[0][y] = energy[0][y];
		for (int x = 0; x < this.width; x++)
			for (int y = 0; y < this.height; y++)
			{
				if (x < this.width - 1) 
				{
					if (y > 0) relaxVert(edgeTo, distTo, x + 1, y - 1, x, y);
					relaxVert(edgeTo, distTo, x + 1, y, x, y);
					if (y < this.height - 1) relaxVert(edgeTo, distTo, x + 1, y + 1, x, y);
				}
			}
		int min_y = 0;
		for (int y = 0; y < this.height; y++)
			min_y = distTo[this.width-1][y] < distTo[this.width-1][min_y] ? y : min_y;
		int[] result = new int[this.width];
		for (int x = this.width - 1; x >= 0; x--)
		{
			result[x] = min_y;
			min_y = edgeTo[x][min_y];
		}
		return result;
	}

	public int[] findVerticalSeam()
	{
		double[][] distTo = new double[this.width][this.height];
		int[][] edgeTo = new int[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				distTo[i][j] = Double.POSITIVE_INFINITY;
		for (int x = 0; x < this.width; x++) 
			distTo[x][0] = energy[x][0];
		for (int y = 0; y < this.height; y++)
			for (int x = 0; x < this.width; x++)
			{
				if (y < this.height - 1)
				{	
					if (x > 0) relaxHor(edgeTo, distTo, x - 1, y + 1, x, y);
					relaxHor(edgeTo, distTo, x, y + 1, x, y);
					if (x < this.width - 1) relaxHor(edgeTo, distTo, x + 1, y + 1, x, y);
				}
			}
		int min_x = 0;
		for (int x = 0; x < this.width; x++)
			min_x = distTo[x][this.height-1] < distTo[min_x][this.height-1] ? x : min_x;
		int[] result = new int[this.height];
		for (int y = this.height - 1; y >= 0; y--) 
		{
			result[y] = min_x;
			min_x = edgeTo[min_x][y];
		}
		return result;
	}

	private void relaxHor(int[][] edgeTo, double[][] distTo, int x, int y, int prev_x, int prev_y)
	{
		if (distTo[prev_x][prev_y] + energy[x][y] < distTo[x][y]) {
			distTo[x][y] = distTo[prev_x][prev_y] + energy[x][y];
			edgeTo[x][y] = prev_x;
		}
	}

	private void relaxVert(int[][] edgeTo, double[][] distTo, int x, int y, int prev_x, int prev_y)
	{
		if (distTo[prev_x][prev_y] + energy[x][y] < distTo[x][y])
		{
			distTo[x][y] = distTo[prev_x][prev_y] + energy[x][y];
			edgeTo[x][y] = prev_y;
		}
	}

	public void removeVerticalSeam(int[] seam)
	{
		if (seam == null) throw new IllegalArgumentException();
		if (!isCorrectVerticalSeam(seam)) throw new IllegalArgumentException();
		Picture newPicture = new Picture(this.width - 1, this.height);
		for (int i = 0; i < this.height; i++)
			for (int j = 0, k = 0; j < this.width; j++, k++) 
			{
				if (seam[i] == j) 
				{
					k--;
					continue;
				}
				newPicture.set(k, i, this.picture.get(j, i));
			}
		this.picture = newPicture;
		this.width -= 1;
		updateEnergy();
	}

	private boolean isCorrectVerticalSeam(int[] seam)
	{
		if (seam.length > this.height || seam.length < this.height) return false;
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] < 0 || seam[i] > this.width - 1) return false;
			if (i < seam.length - 1)
				if (Math.abs(seam[i] - seam[i + 1]) > 1) return false;
		}
		return true;
	}

	public void removeHorizontalSeam(int[] seam) 
	{
		if (seam == null) throw new IllegalArgumentException();
		if (!isCorrectHorizontalSeam(seam)) throw new IllegalArgumentException();
		Picture newPicture = new Picture(this.width, this.height - 1);
		for (int i = 0; i < this.width; i++)
			for (int j = 0, k = 0; j < this.height; j++, k++)
			{
				if (seam[i] == j)
				{
					k--;
					continue;
				}
				newPicture.set(i, k, this.picture.get(i, j));
			}
		this.picture = newPicture;
		this.height -= 1;
		updateEnergy();
	}

	private boolean isCorrectHorizontalSeam(int[] seam)
	{
		if (seam.length > this.width || seam.length < this.width) return false;
		for (int i = 0; i < seam.length; i++)
		{
			if (seam[i] < 0 || seam[i] > this.height-1) return false;
			if (i < seam.length - 1)
				if (Math.abs(seam[i] - seam[i + 1]) > 1) return false;
		}
		return true;
	}

	public static void main(String[] args)
	{
		SeamCarver test = new SeamCarver(new Picture(args[0]));
	}
}
