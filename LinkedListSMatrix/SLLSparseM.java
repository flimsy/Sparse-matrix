package LinkedListSMatrix;

public class SLLSparseM implements SparseM {
	private final int nRows;// total # of rows
	private final int nCols;// total # of columns
	private int nElements;// total # of elements;
	private RHNode first;// The first non-zero row

	public SLLSparseM(int nr, int nc) {
		if (nr <= 0)
			nr = 1; // if zero or negative nr, set nr = 1;
		if (nc <= 0)
			nc = 1; // if zero or negative nc, set nc = 1;
		nRows = nr;
		nCols = nc;
	}

	@Override
	public void addition(SparseM otherM) {

		if ((otherM.nrows() != nRows) || (otherM.ncols() != nCols)) {
			System.out
			.println("Rows or column mismatch, only matrix with same # of rows and columns can be added to each other.");
			return;
		}

		RHNode current = first;
		RHNode other = otherM.getFirst();

		RHNode lastActive = null; // keeps track of the node before we hit null
		while (other != null) {

			if (current != null)
				lastActive = current;

			if (current == null) {

				final RHNode replacement = new RHNode(other);

				replacement.setNext(null);
				replacement.setPrevious(lastActive);

				lastActive.setNext(other);

				nElements += replacement.getNumElements();

				other = other.getNext();
				continue;
			}

			if (current.getrIdx() > other.getrIdx()) {
				final RHNode replacement = new RHNode(other);

				/* IF HEADER */
				if (current.getPrevious() == null) {

					replacement.setPrevious(null);
					replacement.setNext(current);

					current.setPrevious(replacement);

					first = replacement;

					nElements += replacement.getNumElements();
					other = other.getNext();
					continue;
				}

				replacement.setNext(current);
				replacement.setPrevious(current.getPrevious());
				current.getPrevious().setNext(replacement);
				current.setPrevious(replacement);

				nElements += replacement.getNumElements();
				other = other.getNext();

				continue;
			}

			if (current.getrIdx() < other.getrIdx()) {
				current = current.getNext();
				continue;
			}

			if (current.getrIdx() == other.getrIdx()) {

				current = EAddition(current, other);
				other = other.getNext();
				current = current.getNext();

				continue;
			}
			other = other.getNext();
		}

	}

	@Override
	public void clearElement(int ridx, int cidx) {

		if (outOfBounds(ridx, cidx))
			return;

		if (getElement(ridx, cidx) == 0)
			// is already 0, no point doing
			// anything.
			return;

		RHNode currentRow = first;

		while (currentRow != null) {
			if (currentRow.getrIdx() == ridx) {

				ENode currentCol = currentRow.getFirst();

				while (currentCol != null) {
					if (currentCol.getcIdx() == cidx) {

						/*
						 * if the row we're dealing with only has 1 element, and
						 * we need to clear it
						 */
						if (currentRow.getNumElements() == 1) {
							currentRow.setFirst(null);
							currentRow.DecrementElements();
							if (currentRow == first) {
								first = null;
								nElements--;
								return;
							}
							currentRow.getPrevious().setNext(
									currentRow.getNext());
							currentRow.getNext().setPrevious(
									currentRow.getPrevious());
							nElements--;
							return;
						}

						/* if the row we're dealing with has more than 1 element */
						if (currentCol.getPrevious() == null) {// if the node we
							// need to clear
							// is the header
							currentRow.setFirst(currentCol.getNext());
							currentRow.getFirst().setPrevious(null);
							currentRow.DecrementElements();
							nElements--;
							return;
						}
						currentCol.getPrevious().setNext(currentCol.getNext());
						currentCol.getNext().setPrevious(
								currentCol.getPrevious());
						currentRow.DecrementElements();
						nElements--;
					}
					currentCol = currentCol.getNext();
				}
			}
			currentRow = currentRow.getNext();
		}
	}

	/* Checks to see if the col cIdx exists in the given row */
	private boolean colExists(int rIdx, int cidx) {
		RHNode current = first;

		while (current != null) {
			if (current.getrIdx() > rIdx)
				return false;
			if (current.getrIdx() == rIdx) {

				ENode currentCol = current.getFirst();

				while (currentCol != null) {
					if (currentCol.getcIdx() == cidx)
						return true;

					currentCol = currentCol.getNext();
				}
			}
			current = current.getNext();
		}

		return false;
	}

	/* MERGES ELEMENT NODES AND ADDS THOSE THAT SHARE SAME COLUMN INDEX */
	public RHNode EAddition(RHNode first, RHNode second) {

		ENode current = first.getFirst();
		ENode other = second.getFirst();

		ENode lastActive = null; // keeps track of the node before we hit null
		while (other != null) {

			if (current != null)
				lastActive = current;

			if (current == null) {

				final ENode replacement = new ENode(other);

				replacement.setNext(null);
				replacement.setPrevious(lastActive);

				lastActive.setNext(other);
				other = other.getNext();

				continue;
			}

			if (current.getcIdx() > other.getcIdx()) {
				final ENode replacement = new ENode(other);

				/* IF HEADER */
				if (current.getPrevious() == null) {

					replacement.setPrevious(null);
					replacement.setNext(current);

					current.setPrevious(replacement);

					first.setFirst(replacement);

					nElements += 1;
					other = other.getNext();
					continue;
				}

				replacement.setNext(current);
				replacement.setPrevious(current.getPrevious());
				current.getPrevious().setNext(replacement);
				current.setPrevious(replacement);

				nElements += 1;
				other = other.getNext();

				continue;
			}

			if (current.getcIdx() < other.getcIdx()) {
				current = current.getNext();
				continue;
			}

			if (current.getcIdx() == other.getcIdx()) {
				current.setValue(current.getValue() + other.getValue());
				other = other.getNext();
				current = current.getNext();
				continue;
			}
			other = other.getNext();
		}
		return first;

	}

	@Override
	public void getAllElements(int[] ridx, int[] cidx, int[] val) {

		if (nElements == 0)
			return;

		RHNode currentRow = first;

		int counter = 0;
		while (currentRow != null) {

			ENode currentCol = currentRow.getFirst();
			while (currentCol != null) {

				ridx[counter] = currentRow.getrIdx();
				cidx[counter] = currentCol.getcIdx();
				val[counter] = currentCol.getValue();
				counter++;

				currentCol = currentCol.getNext();
			}

			currentRow = currentRow.getNext();

		}

	}

	@Override
	public int getElement(int ridx, int cidx) {

		if (outOfBounds(ridx, cidx))
			return -1;

		RHNode currentRow = first; // declaring our initial search point to go
		// through all rows
		while (currentRow != null) {

			if (currentRow.getrIdx() > ridx)
				// higher then the one we're
				// looking for, it means the one
				// we're looking for doesn't
				// exist;
				return 0;

			if (currentRow.getrIdx() == ridx) {// checking if the row we're
				// looking for exists

				ENode currentCol = currentRow.getFirst(); // our starting point
				// for the element
				// search in the row

				while (currentCol != null) {// searching through all the
					// elements in the row

					if (currentCol.getcIdx() > cidx)
						// thats higher than
						// what we're looking
						// for, then our column
						// doesn't exist;
						return 0;

					if (currentCol.getcIdx() == cidx)
						// we were looking for.
						return currentCol.getValue();// return the said
					// element's value

					currentCol = currentCol.getNext(); // we didn't find the
					// value we were looking
					// for, lets keep
					// searching
				}

			}

			currentRow = currentRow.getNext();
		}
		return 0;// the element we were looking for doesn't exist
	}

	@Override
	public RHNode getFirst() {
		return first;
	}

	@Override
	public int ncols() {
		return nCols;
	}

	@Override
	public int nrows() {
		return nRows;
	}

	@Override
	public int numElements() {
		return nElements;
	}

	// check if the given (ridx, cidx) is out of bounds
	private boolean outOfBounds(int ridx, int cidx) {
		return ((ridx < 0) || (ridx >= nRows) || (cidx < 0) || (cidx >= nCols));
	}

	public void print(SparseM M) {
		// output the final matrix
		final int nelem = M.numElements();
		final int[] ridx = new int[nelem];
		final int[] cidx = new int[nelem];
		final int[] val = new int[nelem];
		M.getAllElements(ridx, cidx, val);
		int nr, nc, ne;
		nr = M.nrows(); // number of rows
		nc = M.ncols(); // number of columns
		ne = M.numElements(); // number of elements
		System.out.println(nr + " " + nc + " " + ne);
		for (int i = 0; i < ne; i++)
			System.out.println(ridx[i] + " " + cidx[i] + " " + val[i]);
		System.out.println("END");
	}

	/* Checks to see if the row rIdx exists */
	private boolean rowExists(int rIdx) {
		RHNode current = first;

		while (current != null) {
			if (current.getrIdx() > rIdx)
				return false;
			if (current.getrIdx() == rIdx)
				return true;
			current = current.getNext();
		}

		return false;
	}

	@Override
	public void setElement(int ridx, int cidx, int val) {

		if (outOfBounds(ridx, cidx))
			return;
		/*
		 * currently the sparse matrix is empty, gotta start with setting up
		 * first
		 */
		if (nElements == 0) {
			final ENode newElement = new ENode(ridx, cidx, val, null, null);
			first = new RHNode(ridx, newElement, null, null);
			nElements++;
			first.IncrementElements();
			return;
		}

		/*
		 * if the value we are trying to enter is 0, just clear the node that
		 * currently sits in that spot
		 */
		if (val == 0)
			clearElement(ridx, cidx);

		/* if the row exists we just need to add a new element to it */
		if (rowExists(ridx)) {
			/* if the column already exists, we just need to change the value */
			if (colExists(ridx, cidx)) {
				RHNode currentRow = first;

				while (currentRow != null) {

					if (currentRow.getrIdx() == ridx) {

						ENode currentCol = currentRow.getFirst();

						while (currentCol != null) {

							if (currentCol.getcIdx() == cidx) {
								currentCol.setValue(val);
								return;
							}
							currentCol = currentCol.getNext();
						}
					}
					currentRow = currentRow.getNext();
				}
			} else { // Col doesn't exist so we have to add it
				RHNode currentRow = first;

				while (currentRow != null) {
					/* We found the row we need to add the element to */
					if (currentRow.getrIdx() == ridx) {

						ENode currentCol = currentRow.getFirst();

						while (currentCol != null) {
							/*
							 * we passed the cidx of our column so we add to the
							 * one before it
							 */
							if (currentCol.getcIdx() > cidx) {
								ENode newNode = new ENode(ridx, cidx, val,
										currentCol, currentCol.getPrevious());
								/* We're dealing with a header */

								if (currentCol.getPrevious() == null) {
									newNode = new ENode(ridx, cidx, val,
											currentCol, null);
									currentCol.setPrevious(newNode);
									currentRow.setFirst(newNode);
									nElements++;
									currentRow.IncrementElements();
									return;
								}

								currentCol.getPrevious().setNext(newNode);
								currentCol.setPrevious(newNode);
								currentRow.IncrementElements();
								nElements++;
								return;
							}
							currentCol = currentCol.getNext();
						}

						/*
						 * If we hit this point, it means we couldn't find a
						 * column bigger then the one we're trying to add, so we
						 * add this column to the end of list
						 */

						ENode CCol = currentRow.getFirst();

						while (CCol.getNext() != null)
							CCol = CCol.getNext();

						CCol.setNext(new ENode(ridx, cidx, val, null, CCol));
						currentRow.IncrementElements();
						nElements++;
						return;
					}

					currentRow = currentRow.getNext();
				}
			}

		} else { // row doesn't exist, we need to make it and then add the
			// element to it.

			RHNode currentRow = first;

			while (currentRow != null) {
				/*
				 * We found a row that comes after the row we're trying to
				 * create
				 */
				if (currentRow.getrIdx() > ridx) {

					/* Means this node is the header */

					if (currentRow.getPrevious() == null) {
						final RHNode newRNode = new RHNode(ridx, new ENode(
								ridx, cidx, val, null, null), currentRow, null);
						currentRow.setPrevious(newRNode);
						first = newRNode;
						newRNode.IncrementElements();
						nElements++;
						return;
					}

					final RHNode newRNode = new RHNode(ridx, new ENode(ridx,
							cidx, val, null, null), currentRow,
							currentRow.getPrevious());

					currentRow.getPrevious().setNext(newRNode);
					currentRow.setPrevious(newRNode);
					newRNode.IncrementElements();
					nElements++;
					return;
				}
				currentRow = currentRow.getNext();
			}

			/*
			 * if we hit this point, it means we couldn't find a row that would
			 * come after the row we're trying to make
			 */
			/* so we now add our row to the end of the list */

			RHNode RRow = first;

			while (RRow.getNext() != null)
				RRow = RRow.getNext();

			final RHNode node = new RHNode();

			node.setNext(null);
			node.setPrevious(RRow);
			node.setFirst(new ENode(ridx, cidx, val));
			node.setrIdx(ridx);
			node.IncrementElements();
			nElements++;

			RRow.setNext(node);

		}

	}

}
