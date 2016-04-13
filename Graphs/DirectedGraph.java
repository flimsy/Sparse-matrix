package Graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import LinkedListSMatrix.ENode;
import LinkedListSMatrix.RHNode;
import LinkedListSMatrix.SLLSparseM;

public class DirectedGraph {

	public class ArrayIndexComparator implements Comparator<Integer> {
		private final Double[] array;

		public ArrayIndexComparator(Double[] array) {
			this.array = array;
		}

		@Override
		public int compare(Integer index1, Integer index2) {
			// Autounbox from Integer to int to use as array indexes
			return array[index1].compareTo(array[index2]);
		}

		public Integer[] createIndexArray() {
			final Integer[] indexes = new Integer[array.length];
			for (int i = 0; i < array.length; i++)
				indexes[i] = i; // Autoboxing
			return indexes;
		}
	}

	private final SLLSparseM matrix;
	private final int numNodes;
	private final int[] outDegree;

	private final double[] PageRank;

	// constructor initialize an undirected graph, n is the number of nodes
	public DirectedGraph(int n) {
		matrix = new SLLSparseM(n, n);
		numNodes = n;
		outDegree = new int[n - 1];
		PageRank = new double[n - 1];

		for (int i = 0; i < PageRank.length; i++)
			PageRank[i] = 1;
	}

	// compute page rank after num_iters iterations
	// then print them in a monotonically decreasing order
	void computePageRank(int num_iters) {

		// this is to keep cycles in sync
		final double[] tempArr = new double[numNodes - 1];

		for (int i = 0; i < tempArr.length; i++)
			tempArr[i] = 1;

		for (int iterations = 0; iterations < num_iters; iterations++) {

			for (int i = 0; i < tempArr.length; i++)
				tempArr[i] = PageRank[i];

			for (int i = 0; i < (numNodes - 1); i++) {
				double sum = 0;

				for (final int p : getAllInners(i))
					sum += (tempArr[p] / getDegreeOut(p));

				PageRank[i] = sum;

			}

		}

		// this will hold all the ranks in a sorted order
		final Double[] sortedItems = new Double[PageRank.length];

		// feeding every rank to the new array
		for (int i = 0; i < PageRank.length; i++)
			sortedItems[i] = new Double(PageRank[i]);

		// using a custom comparator that ordered indexes rather than the values
		final ArrayIndexComparator comparator = new ArrayIndexComparator(
				sortedItems);
		final Integer[] indexes = comparator.createIndexArray();

		Arrays.sort(indexes, comparator);

		// reverse the array
		Collections.reverse(Arrays.asList(indexes));

		// printing it all out.
		for (final int index : indexes)
			System.out.println(index + " " + PageRank[index]);

	}

	public List<Integer> getAllInners(int n) {
		final List<Integer> list = new ArrayList<Integer>();

		RHNode first = matrix.getFirst();

		while (first != null) {

			ENode firstCol = first.getFirst();

			while (firstCol != null) {

				if (firstCol.getcIdx() == n)
					list.add(firstCol.getrIdx());

				firstCol = firstCol.getNext();
			}

			first = first.getNext();
		}

		return list;

	}

	// searches through the matrix to find all (n, *) incidents.
	public int getDegreeOut(int node) {
		int total = 0;
		for (int i = 0; i <= numNodes; i++) {
			final int elementVal = matrix.getElement(node, i);
			if ((elementVal != -1) && (elementVal != 0))
				total++;
		}
		return total;
	}

	// check if the given node id is out of bounds
	private boolean outOfBounds(int nidx) {
		return ((nidx < 0) || (nidx > numNodes)) ? true : false;
	}

	// set an edge (n1,n2)
	// beware of repeatingly setting a same edge and out-of-bound node ids
	public void setEdge(int n1, int n2) {
		if (outOfBounds(n1) || outOfBounds(n2))
			return; // can't set this node

		if (n1 == n2)
			return; // can't set a node pointing to itself.

		// pr has initial value of 1
		matrix.setElement(n1, n2, 1);

		outDegree[n1] = getDegreeOut(n1);
	}
}
