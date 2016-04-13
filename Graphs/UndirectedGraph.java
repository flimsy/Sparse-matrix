package Graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import LinkedListSMatrix.SLLSparseM;

public class UndirectedGraph {

	// generate all power sets of a set
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
		final Set<Set<T>> sets = new HashSet<Set<T>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<T>());
			return sets;
		}
		final List<T> list = new ArrayList<T>(originalSet);
		final T head = list.get(0);
		final Set<T> rest = new HashSet<T>(list.subList(1, list.size()));
		for (final Set<T> set : powerSet(rest)) {
			final Set<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	private final SLLSparseM matrix;
	private final int numNodes;

	private final List<ArrayList<Integer>> possibleCliques;

	// constructor initialize an undirected graph, n is the number of nodes
	public UndirectedGraph(int n) {
		matrix = new SLLSparseM(n, n);
		numNodes = n;
		possibleCliques = new ArrayList<ArrayList<Integer>>();
		generateAllSets();

	}

	private List<Set<Integer>> filterList(List<ArrayList<Integer>> filteringList) {

		final PriorityQueue<Set<Integer>> queue = new PriorityQueue<Set<Integer>>(
				16, (a, b) -> b.size() - a.size());
		for (final ArrayList<Integer> clique : filteringList)
			queue.add(new HashSet<Integer>(clique));
		final List<Set<Integer>> result = new ArrayList<>();
		while (!queue.isEmpty()) {
			final Set<Integer> largest = queue.poll();
			result.add(largest);
			final Iterator<Set<Integer>> rest = queue.iterator();
			while (rest.hasNext())
				if (largest.containsAll(rest.next()))
					rest.remove();
		}

		return result;

	}

	// compute maximal soft clique
	// cliquesize_lower_bd: k
	// num_missing_edges: l
	public void findMaxSoftClique(int cliquesize_lower_bd, int num_missing_edges) {

		final List<ArrayList<Integer>> filteringList = new ArrayList<ArrayList<Integer>>();

		for (final ArrayList<Integer> clique : possibleCliques)
			if (testClique(clique, num_missing_edges))
				filteringList.add(clique);

		for (final Set<Integer> finalClique : filterList(filteringList))
			printClique(new ArrayList<Integer>(finalClique));

	}
	
	ArrayList<ArrayList<Integer>> AdvancedPowerSet(int startIdx,
			ArrayList<ArrayList<Integer>> coll, int edgeCount) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		if (edgeCount < 0)
			return coll;
		if (startIdx > numNodes - 1)
			return coll;

		if (coll.isEmpty()) {
			for (int i = startIdx; i < numNodes; i++) {
				if (startIdx == i)
					continue;
				if (matrix.getElement(startIdx, i) == 1) {
					// including SI

					list.add(startIdx);
					list.add(i);

					coll.add(new ArrayList<Integer>(list));

					list.clear();

					//excluding SI
					list.add(i);

					coll.add(new ArrayList<Integer>(list));

					list.clear();
				}

			}
			return AdvancedPowerSet(startIdx + 1, coll, edgeCount);
		}

		ArrayList<ArrayList<Integer>> copyList = new ArrayList<ArrayList<Integer>>(
				coll);
		// including SI
		for (ArrayList<Integer> temp : copyList) {
			for (int i = startIdx + 1; i < numNodes; i++) {
				if (startIdx == i)
					continue;
				list = new ArrayList<Integer>(temp);
				list.add(i);
				if (testClique(list, edgeCount)) {
					coll.add(new ArrayList<Integer>(list));
				}
			}
		}

		return AdvancedPowerSet(startIdx + 1, coll, edgeCount);

	}
	

	// compute maximal soft clique by using recursion
	// to compute all (k,l) soft cliques using recursion
	// you should check the partial subset during generation
	// rather than checking the whole subset
	// cliquesize_lower_bd: k
	// num_missing_edges: l
	public void findMaxSoftCliqueAdvanced(int cliquesize_lower_bd,
			int num_missing_edges) {
		
		for (final Set<Integer> finalClique : filterList(AdvancedPowerSet(0, new ArrayList<ArrayList<Integer>>(), num_missing_edges)))
			printClique(new ArrayList<Integer>(finalClique));		
	}

	private void generateAllSets() {
		final Set<Integer> mySet = new HashSet<Integer>();

		for (int i = 0; i < numNodes; i++)
			mySet.add(i);
		for (final Set<Integer> s : powerSet(mySet))
			if (s.size() > 1) {
				final ArrayList<Integer> list = new ArrayList<Integer>();

				for (final int i : s)
					list.add(i);

				possibleCliques.add(list);
			}

	}

	// check if the given node id is out of bounds
	private boolean outOfBounds(int nidx) {
		return ((nidx < 0) || (nidx > numNodes)) ? true : false;
	}

	

	// print an output soft clique in one line
	public void printClique(ArrayList<Integer> nlist) {
		for (int i = 0; i < nlist.size(); ++i)
			System.out.print(nlist.get(i) + " ");
		System.out.println("");
	}

	// set an edge (n1,n2).
	// Since this is an undirected graph, (n2,n1) is also set to one
	public void setEdge(int n1, int n2) {

		// out of bounds
		if (outOfBounds(n1) || outOfBounds(n2))
			return;

		// can't set a node to itself.
		if (n1 == n2)
			return;

		matrix.setElement(n1, n2, 1);
		matrix.setElement(n2, n1, 1);

	}

	/* Returns true if this list contains duplicates */
	private boolean duplicate(List<Integer> l){
		Set<Integer> set = new HashSet<Integer>(l);
		
		if(set.size() < l.size())
			return true;
		
		return false;
	}
	
	// test if this clique is valid or not
	private boolean testClique(List<Integer> list, int num_missing_edges) {
		
		if(duplicate(list)) return false;
		
		int counter0 = 0;
		for (final Set<Integer> s : powerSet(new HashSet<Integer>(list)))
			if (s.size() == 2)
				if (matrix.getElement((int) s.toArray()[0],
						(int) s.toArray()[1]) == 1) {

					// we found a connection, we do nothing in this case
				} else
					counter0++;
		// we found a break, lets take a note. This is how we will
		// determine if nodes are cliques.

		// check to see if the amount of break in our nodes is greater than the
		// edges we are missing
		if (counter0 > num_missing_edges)
			return false;

		return true;

	}

}
