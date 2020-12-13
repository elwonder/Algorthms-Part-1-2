import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

public class Solver {
    private int solutionMoves;
    private Stack<Board> solutions;
    private boolean isSolvable;

    private class SearchNode 
    {
		public Board board;
		public SearchNode previous;
        public short moves;
        public short manhattan;
        public short priority;

		public SearchNode(Board current, SearchNode previous, int moves, int manhattan) {
			this.board = current;
			this.previous = previous;
            this.moves = (short) moves;
            this.manhattan = (short) manhattan;
            this.priority = (short) (manhattan + moves);
		}
	}


    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        MinPQ<SearchNode> gameQueue = new MinPQ<SearchNode>(new HammingPriority());
        MinPQ<SearchNode> alternativeGameQueue = new MinPQ<SearchNode>(new HammingPriority());
        SearchNode current = new SearchNode(initial, null, 0, initial.manhattan());
        SearchNode alternativeCurrent = new SearchNode(initial.twin(), null, 0, initial.manhattan());
        gameQueue.insert(current);
        alternativeGameQueue.insert(alternativeCurrent);
        while (!current.board.isGoal() && !alternativeCurrent.board.isGoal()) 
        {
            for (Board s: alternativeCurrent.board.neighbors()) 
            {

                if (alternativeCurrent.previous == null || !s.equals(alternativeCurrent.previous.board))
                {
                    alternativeGameQueue.insert(
                        new SearchNode(s, alternativeCurrent, alternativeCurrent.moves + 1, s.manhattan()));
                }
            }
            for (Board s: current.board.neighbors())
            {
                if  (current.previous == null || !s.equals(current.previous.board))
                {
                    gameQueue.insert(
                        new SearchNode(s, current, current.moves + 1, s.manhattan()));
                }
            }
            alternativeCurrent = alternativeGameQueue.delMin();
            current = gameQueue.delMin();
        }
        if (alternativeCurrent.board.isGoal()) {
            this.solutionMoves = -1;
            this.isSolvable = false;
            return;
        }
        this.solutionMoves = current.moves;
        this.isSolvable = true;
        this.solutions = new Stack<Board>();
        while (current.previous != null) {
            this.solutions.push(current.board);
            current = current.previous;
        }
        this.solutions.push(current.board);
    }

    public boolean isSolvable() {
        return this.isSolvable;
    }

    private static class HammingPriority implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return a.priority - b.priority;
        }
    }

    public int moves() {
        return this.solutionMoves;
    }

    public Iterable<Board> solution() {
        return this.solutions;
    }

    public static void main(String[] args) {
        int[][] testTiles = new int[][] {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board testBoard = new Board(testTiles);
        Solver test = new Solver(testBoard);
        for (Board s: test.solution())
            System.out.println(s);
    }
}