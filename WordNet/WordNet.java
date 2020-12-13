import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import java.util.HashMap;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.Bag;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class WordNet
{
	private HashMap<String, Bag<Integer>> nouns;
	private HashMap<Integer, String> synsets;
	private SAP sap;

	public WordNet(String synsets, String hypernyms)
	{
		if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
		this.nouns = new HashMap<String, Bag<Integer>>();
		this.synsets = new HashMap<Integer, String>();
		In synsetsInput = new In(synsets);
		while (synsetsInput.hasNextLine())
		{
			String currentLine = synsetsInput.readLine();
			String[] items = currentLine.split(",");
			int id = Integer.parseInt(items[0]);
			String[] synonyms = items[1].split(" ");
			for (String currentItem: synonyms)
			{
				if (!nouns.containsKey(currentItem)) 
				{
					Bag<Integer> newEntry = new Bag<Integer>();
					newEntry.add(id);
					nouns.put(currentItem, newEntry);
				} else 
				{
					nouns.get(currentItem).add(id);
				} 
			}
			this.synsets.put(id, items[1]);
		}
		In hypernymsInput = new In(hypernyms);
		Digraph connections = new Digraph(this.nouns.size());
		while (hypernymsInput.hasNextLine())
		{
			String currentLine = hypernymsInput.readLine();
			StringTokenizer hypernymsTokens	= new StringTokenizer(currentLine, ",");
			int child = Integer.parseInt(hypernymsTokens.nextToken());
			while (hypernymsTokens.hasMoreTokens())
			{
				int ancestor = Integer.parseInt(hypernymsTokens.nextToken());
				connections.addEdge(child, ancestor);
			}
		}
		Topological test = new Topological(connections);
		if (!test.hasOrder()) throw new IllegalArgumentException();
		this.sap = new SAP(connections);
	}

	public Iterable<String> nouns()
	{
		return this.nouns.keySet();
	}

	public boolean isNoun(String word)
	{
		if (word == null) throw new IllegalArgumentException();
		return this.nouns.containsKey(word);
	}

	public int distance(String nounA, String nounB)
	{
		if (nounA == null || nounB == null) throw new IllegalArgumentException();
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		Bag<Integer> firstID = nouns.get(nounA);
		Bag<Integer> secondID = nouns.get(nounB);
		return sap.length(firstID, secondID);
	}

	public String sap(String nounA, String nounB)
	{
		if (nounA == null || nounB == null) throw new IllegalArgumentException();
		if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
		int sapID = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
		return synsets.get(sapID);
	}
		


	public static void main(String[] args)
	{
		String synsets = args[0];
		String hypernyms = args[1];
		WordNet test = new WordNet(synsets, hypernyms);
	}
		
	
}
