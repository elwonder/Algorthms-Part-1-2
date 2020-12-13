import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;


public class BoggleSolver
{
	private final simpleTrie trie;
	private final int[] scores = {0, 0, 0, 1, 1, 2, 3, 5};
	private static final int MAX_SCORE = 11;
	private SET<String> result;
	private BoggleBoard board;
	private boolean[][] visited;


	public BoggleSolver(String[] dictionary)
	{
		this.trie = new simpleTrie();
		for (String s: dictionary)
			this.trie.add(s);
	}

	private class simpleTrie 
	{
		private static final int R = 26;

		private Node root;
		private int n;

		private class Node 
		{
			private Node[] next = new Node[R];
			private boolean isString;
		}

		public simpleTrie() {}

		public boolean contains(String key)
		{
			Node x = get(root, key, 0);
			if (x == null) return false;
			return x.isString;
		}

		private Node get(Node x, String key, int d)
		{
			if (x == null) return null;
			if (d == key.length()) return x;
			char c = (char) ((int) key.charAt(d) - 65);
			return get(x.next[c], key, d+1);
		}

		public void add(String key)
		{
			root = add(root, key, 0);
		}

		private Node add(Node x, String key, int d)
		{
			if (x == null) x = new Node();
			if (d == key.length())
			{
				if (!x.isString) n++;
				x.isString = true;
			}
			else 
			{
				char c = (char) ((int) key.charAt(d) -  65);
				x.next[c] = add(x.next[c], key, d+1);
			}
			return x;
		}

		public boolean hasPrefix(String prefix)
		{
			Node x = get(root, prefix, 0);
			return x == null;
		}
	}

	public Iterable<String> getAllValidWords(BoggleBoard board)
	{
		this.result = new SET<String>();
		this.board = board;
		int rows = board.rows();
		int cols = board.cols();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
			{
				String word = "";
				this.visited = new boolean[rows][cols];
				this.goDeep(word, i, j);
			}
		return result;
	}
		
	private void goDeep(String word, int i, int j)
	{
		visited[i][j] = true;
		char character = this.board.getLetter(i, j);
		if (character == 'Q') word = word + "QU";
		else word = word + this.board.getLetter(i, j); 
		if (this.trie.contains(word) && word.length() >= 3
				&& !this.result.contains(word)) result.add(word);
		int rows = this.board.rows();
		int cols = this.board.cols();
		if (this.trie.hasPrefix(word)) return;
		if (i > 0 && !visited[i-1][j]) 
		{
			goDeep(word, i - 1, j);
			visited[i-1][j] = false;
		}
		if (j > 0 && !visited[i][j-1])
		{
			goDeep(word, i, j - 1);
			visited[i][j-1] = false;
		}
		if (j > 0 && i > 0 && !visited[i-1][j-1]) 
		{
			goDeep(word, i - 1, j - 1);
			visited[i-1][j-1] = false;
		}
		if (j > 0 && i < rows-1 && !visited[i+1][j-1])
		{
			goDeep(word,i+1, j-1); 
			visited[i+1][j-1] = false;
		}
		if (i < rows - 1 && !visited[i+1][j])
		{
			goDeep(word, i+1, j);
			visited[i+1][j] = false;
		}
		if (i < rows-1 && j < cols-1 && !visited[i+1][j+1]) {
			goDeep(word,i+1,j+1);
			visited[i+1][j+1] = false;
		}
		if (j < cols-1 && !visited[i][j+1]) 
		{
			goDeep(word, i, j+1);
			visited[i][j+1] = false;
		}
		if (j < cols-1 && i > 0 && !visited[i-1][j+1]) {
			goDeep(word, i-1, j+1);
			visited[i-1][j+1] = false;
		}
	}





	public int scoreOf(String word)
	{
		if (!this.trie.contains(word)) return 0;
		if (word.length() >= 8) return MAX_SCORE;
		return this.scores[word.length()];
	}

	public static void main(String[] args)
	{
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
	}
}
