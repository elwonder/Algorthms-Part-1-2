import edu.princeton.cs.algs4.BinaryStdIn;
import java.util.Arrays;
import edu.princeton.cs.algs4.BinaryStdOut;


public class MoveToFront
{

	private static final int R = 256;


	public static void encode() 
	{
		int[] sequence = new int[R];
		for (int i = 0; i < R; i++)
			sequence[i] = i;
		while (!BinaryStdIn.isEmpty()) 
		{
			char c = BinaryStdIn.readChar();
			int position = 0; 
			while ((char) sequence[position] != c) 
				position++;
			BinaryStdOut.write((char) position);
			for (int i = (position - 1); i >= 0; i--)
				sequence[i+1] = sequence[i];
			sequence[0] = c;
		}
		BinaryStdOut.close();
	}

	public static void decode()
	{
		int[] sequence = new int[R];
		for (int i = 0; i < R; i++)
			sequence[i] = i;
		while (!BinaryStdIn.isEmpty())
		{
			int position = (int) BinaryStdIn.readChar();
			BinaryStdOut.write((char) sequence[position]);
			int buffer = sequence[position];
			for (int i = (position - 1); i >= 0; i--)
				sequence[i+1] = sequence[i];
			sequence[0] = buffer; 
		}
		BinaryStdOut.close();
	}

	public static void main(String[] args)
	{
		String sign = args[0];
		if (sign.equals("-"))
			encode();
		else if (sign.equals("+"))
			decode();
	}

}
