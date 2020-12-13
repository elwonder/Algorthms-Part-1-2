import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;


public class Outcast 
{

	private WordNet wordnet;

	public Outcast(WordNet wordnet) 
	{
		this.wordnet = wordnet;
	}

	public String outcast(String[] nouns)
	{
		String maxString = nouns[0];
		int maxDistanceSum = 0;
		for (String s: nouns) 
		{
			int distanceSum = 0;
			for (String s_x: nouns)
				distanceSum += this.wordnet.distance(s, s_x);
			if (distanceSum > maxDistanceSum)
			{
				maxString = s;
				maxDistanceSum = distanceSum;
			}
		}
		return maxString;
	}

	public static void main(String[] args)
	{
		WordNet wordnet = new WordNet(args[0], args[1]);
    		Outcast outcast = new Outcast(wordnet);
    		for (int t = 2; t < args.length; t++) {
        		In in = new In(args[t]);
        		String[] nouns = in.readAllStrings();
        		StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    		}

	}
}
