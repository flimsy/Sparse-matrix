package LinkedListSMatrix;

public interface SparseM {
	void addition(SparseM otherM); // adding the matrix otherM into the current
	// matrix

	void clearElement(int ridx, int cidx); // set the element at (ridx,cidx) to
	// zero

	void getAllElements(int[] ridx, int[] cidx, int[] val); // get all nonzero
	// elements

	int getElement(int ridx, int cidx); // return the element at a given entry
	// (ridx, cidx),

	RHNode getFirst();

	int ncols(); // return number of columns

	int nrows(); // return number of rows

	int numElements(); // return total number of nonzero elements in the matrix

	void setElement(int ridx, int cidx, int val); // set the element at
	// (ridx,cidx) to val

}
