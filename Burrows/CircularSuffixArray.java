import java.util.Arrays;
import edu.princeton.cs.algs4.Quick;
import java.util.Comparator;

public class CircularSuffixArray
{

	private Integer[] suffixArray;	
	private int length;
	private String string;

	private class compareSuffixes implements Comparator<Integer>
	{
		public int compare(Integer a, Integer b) {
			int l = length;
			for (int i = 0; i < l; i++)
			{
				char aChar = string.charAt((i+a) % l);
				char bChar = string.charAt((i+b) % l);
				if (aChar > bChar) return 1;
				if (aChar < bChar) return -1;
			}
			return 0;
		}

	}

	public CircularSuffixArray(String s)
	{
		if (s == null) throw new IllegalArgumentException();
		this.string = s;
		this.length = s.length();
		this.suffixArray = new Integer[this.length];
		for (int i = 0; i < this.length; i++)
			this.suffixArray[i] = i;
		Arrays.sort(suffixArray, new compareSuffixes());

	}

	public int length()
	{
		return this.length;
	}

	public int index(int i)
	{
		if (i < 0 || i > this.length - 1) throw new IllegalArgumentException();
		return this.suffixArray[i];
	}

	public static void main(String[] args)
	{
		CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
	}
}
