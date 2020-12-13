import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import java.util.Arrays;

public class BaseballElimination
{
	private int[] wins;
	private int[] loss;
	private int[] left;
	private int[][] grid;
	private ST<String, Integer> teams;
	private String[] teamsArray;
	private static final int ARTIFICIAL_SINK = 1;
	private static final int ARTIFICIAL_SOURCE = 1;


	public BaseballElimination(String filename)
	{
		In dataInput = new In(filename);
		int amount = dataInput.readInt();
		this.teamsArray = new String[amount];
		this.wins = new int[amount];
		this.loss = new int[amount];
		this.left = new int[amount];
		this.grid = new int[amount][amount];
		this.teams = new ST<String, Integer>();
		for (int i = 0; i < amount; i++) 
		{
			String teamName = dataInput.readString();
			this.teams.put(teamName, i);
			this.teamsArray[i] = teamName;
			this.wins[i] = dataInput.readInt();
			this.loss[i] = dataInput.readInt();
			this.left[i] = dataInput.readInt();
			for (int j = 0; j < amount; j++)
				this.grid[i][j] = dataInput.readInt();
		}

	}


	public Iterable<String> teams()
	{
		return this.teams.keys();
	}

	public int numberOfTeams()
	{
		return this.teams.size();
	}

	private boolean checkTeam(String team)
	{
		for (String s: this.teamsArray)
			if (s.equals(team)) return true;
		return false;
	}
		

	public int wins(String team)
	{
		if (!checkTeam(team)) throw new IllegalArgumentException();
		return wins[this.teams.get(team)];
	}

	public int losses(String team)
	{
		if (!checkTeam(team)) throw new IllegalArgumentException();
		return loss[this.teams.get(team)];
	}

	public int remaining(String team)
	{
		if (!checkTeam(team)) throw new IllegalArgumentException();
		return left[this.teams.get(team)];

	}

	public int against(String team1, String team2)
	{
		if (!checkTeam(team1) || !checkTeam(team2)) throw new IllegalArgumentException();
		return this.grid[this.teams.get(team1)][this.teams.get(team2)];

	}

	public boolean isEliminated(String team)
	{
		if (!checkTeam(team)) throw new IllegalArgumentException();
		Bag<String> eliminators = makeFlow(team);
		return eliminators.size() > 0; 
	}

	private Bag<String> makeFlow(String team)
	{
		if (!checkTeam(team)) throw new IllegalArgumentException();
		Bag<String> eliminators = new Bag<String>();
		if (isTrivialEliminated(team)) {
			eliminators.add(getTrivialEliminator(team));
			return eliminators;
		}
		int gamesAmount = evaluateGamesAmount();
		int networkSize = ARTIFICIAL_SOURCE +  gamesAmount + this.numberOfTeams() - 1 + ARTIFICIAL_SINK;
		int source = 0;
		int sink = networkSize - 1;
		FlowNetwork flowNetwork = new FlowNetwork(networkSize);
		int teamID = this.teams.get(team);
		int verticeID = 1;
		final int teamVerticesStart = 1 + gamesAmount;
		int jumperForI = 0;
		for (int i = 0; i < this.numberOfTeams() - 1; i++)
		{
			if (i == teamID)
			{
				jumperForI++;
				continue;
			}
			int jumperForJ = 0;
			for (int j = i + 1; j < this.numberOfTeams(); j++)
			{
				if (j == teamID)
				{
					jumperForJ++;
					continue;
				}
				if (teamID < i + 1) jumperForJ++;
				flowNetwork.addEdge(new FlowEdge(source, verticeID, grid[i][j]));
				flowNetwork.addEdge(new FlowEdge(verticeID, teamVerticesStart + i - jumperForI, Double.POSITIVE_INFINITY));
				flowNetwork.addEdge(new FlowEdge(verticeID, teamVerticesStart + j  - jumperForJ, Double.POSITIVE_INFINITY));
				verticeID++;
			}
		}
		int jumper = 0;
		for (int i = 0; i < this.numberOfTeams(); i++)
		{
			if (i == teamID)
			{
				jumper++;
				continue;
			}
			flowNetwork.addEdge(new FlowEdge(teamVerticesStart + i - jumper, sink, this.wins(team) + this.remaining(team) - this.wins[i]));
		}
		jumper = 0;
		FordFulkerson maxFlow = new FordFulkerson(flowNetwork, source, sink);
		for (int i = 0; i < this.numberOfTeams(); i++)
		{
			if (i == teamID)
			{
				jumper++;
				continue;
			}
			if (maxFlow.inCut(teamVerticesStart + i - jumper)) eliminators.add(teamsArray[i]);
		}
		System.out.println(flowNetwork);
		return eliminators;
	}	

	private int evaluateGamesAmount()
	{
		int x = this.numberOfTeams();
		return ((x - 1) * (x - 2))/2;
	}

	private boolean isTrivialEliminated(String team)
	{
		int maxPossible = this.wins(team) + this.remaining(team);
		for (String s: this.teams())
			if (this.wins(s) > maxPossible) return true;
		return false;
	}

	private String getTrivialEliminator(String team)
	{
		int maxPossible = this.wins(team) + this.remaining(team);
		for (String s: this.teams())
			if (this.wins(s) > maxPossible) return s;
		return new String();
	}


	public Iterable<String> certificateOfElimination(String team)
	{
		Bag<String> result = makeFlow(team);
		if (result.size() == 0) return null;
		return result; 
	}

	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	     }
	}
	
	
}
