package LinkedListSMatrix;

//doubly linked row head nodes
//consists of many element nodes
public class RHNode {

	private int rIdx;// row index of current node
	private int numElements;// number of elements in the row

	private ENode first;// the first element node connected to this node

	private RHNode next;
	private RHNode previous;

	public RHNode() {
		// empty constructor
	}

	public RHNode(int rIdx, ENode firstElement, RHNode next, RHNode previous) {
		this.rIdx = rIdx;
		first = firstElement;
		this.next = next;
		this.previous = previous;
	}

	public RHNode(RHNode other) { // copy constructor
		rIdx = other.rIdx;
		numElements = other.numElements;

		// copying all the elements

		ENode current = other.getFirst();
		ENode lastNode = null;
		while (current != null) {
			if (first == null) {
				first = new ENode(other.getFirst().getrIdx(), other.getFirst()
						.getcIdx(), other.getFirst().getValue());
				lastNode = first;
			} else {
				final ENode node = new ENode(current.getrIdx(),
						current.getcIdx(), current.getValue(), null, lastNode);

				lastNode.setNext(node);
				lastNode = node;
			}

			current = current.getNext();
		}
	}

	public void DecrementElements() {
		numElements--;
	}

	public ENode getFirst() {
		return first;
	}

	public RHNode getNext() {
		return next;
	}

	public int getNumElements() {
		return numElements;
	}

	public RHNode getPrevious() {
		return previous;
	}

	public int getrIdx() {
		return rIdx;
	}

	public void IncrementElements() {
		numElements++;
	}

	public void setFirst(ENode first) {
		this.first = first;
	}

	public void setNext(RHNode next) {
		this.next = next;
	}

	public void setPrevious(RHNode previous) {
		this.previous = previous;
	}

	public void setrIdx(int rIdx) {
		this.rIdx = rIdx;
	}

}
