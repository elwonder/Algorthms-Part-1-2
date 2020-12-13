import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;


public class SAP 
{

	private Digraph digraph;

	public SAP(Digraph G)
	{
		if (G == null) throw new IllegalArgumentException();
		this.digraph = new Digraph(G);
	}

	private boolean checkBounds(int n) 
	{
		return (n >= 0) && (n < this.digraph.V());
	}

	public int length(int v, int w)
	{
		if (!checkBounds(v) || !checkBounds(w)) throw new IllegalArgumentException();
		int sap = ancestor(v, w);
		if (sap == -1) return -1;
		BreadthFirstDirectedPaths first = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths second = new BreadthFirstDirectedPaths(digraph, w);
		return first.distTo(sap) + second.distTo(sap);
	}

	public int ancestor(int v, int w)
	{
		if (!checkBounds(v) || !checkBounds(w)) throw new IllegalArgumentException();
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(v);
		int currentNode;
		BreadthFirstDirectedPaths first = new BreadthFirstDirectedPaths(this.digraph, w);
		BreadthFirstDirectedPaths second = new BreadthFirstDirectedPaths(this.digraph, v);
		SET<Integer> ancestorSet = new SET<Integer>();
		boolean[] marks = new boolean[this.digraph.V()];
		while (!queue.isEmpty())
		{
			currentNode = queue.dequeue();
			marks[currentNode] = true;
			for (int n: this.digraph.adj(currentNode))
				if (!marks[n]) queue.enqueue(n);
			if (first.hasPathTo(currentNode))
				ancestorSet.add(currentNode);
		}
		if (ancestorSet.size() == 0) return -1;
		int result = ancestorSet.min();
		for (int n: ancestorSet) {
			int d1 = first.distTo(n) + second.distTo(n);
			int d2 = first.distTo(result) + second.distTo(result);
			result = d1 < d2 ? n : result;
		}
		return result;
	}

	public int length(Iterable<Integer> v, Iterable<Integer>w)
	{
		if (!checkIterable(v) || !checkIterable(w)) throw new IllegalArgumentException();
		int sap = ancestor(v, w);
		if (sap == -1) return -1;
		BreadthFirstDirectedPaths first = new BreadthFirstDirectedPaths(this.digraph, v);
		BreadthFirstDirectedPaths second = new BreadthFirstDirectedPaths(this.digraph, w);
		return first.distTo(sap) + second.distTo(sap);
	}

	private boolean checkIterable(Iterable<Integer> v) 
	{
		if (v == null) return false;
		for (Integer n: v)
		{
			if (n == null) return false;
			if (!checkBounds(n)) return false;
		}
		return true;
	}


	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		if (!checkIterable(v) || !checkIterable(w)) throw new IllegalArgumentException();
		BreadthFirstDirectedPaths first = new BreadthFirstDirectedPaths(this.digraph, w);
		BreadthFirstDirectedPaths second = new BreadthFirstDirectedPaths(this.digraph, v);
		SET<Integer> ancestorSet = new SET<Integer>();
		for (int n: v) 
		{
			int currentNode;
			boolean[] marks = new boolean[this.digraph.V()];
			Queue<Integer> queue = new Queue<Integer>();
			queue.enqueue(n);
			while (!queue.isEmpty())
			{
				currentNode = queue.dequeue();
				marks[currentNode] = true;
				for (int r: this.digraph.adj(currentNode))
					if (!marks[r]) queue.enqueue(r);
				if (first.hasPathTo(currentNode))
					ancestorSet.add(currentNode);
			}
		}
		if (ancestorSet.size() == 0) return -1;
		int result = ancestorSet.min();
		for (int n: ancestorSet)
		{
			int d1 = first.distTo(n) + second.distTo(n);
			int d2 = first.distTo(result) + second.distTo(result);
			result = d1 < d2 ? n : result;
		}
		return result;
	}

	public static void main(String[] args) {
    		In in = new In(args[0]);
    		Digraph G = new Digraph(in);
    		SAP sap = new SAP(G);
    		while (!StdIn.isEmpty()) {
        		int v = StdIn.readInt();
        		int w = StdIn.readInt();
        		int length   = sap.length(v, w);
       			int ancestor = sap.ancestor(v, w);
	        	StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    		}
	}
}


