package LinkedListSMatrix;

//element node doubly linked list
public class ENode {

	private int rIdx;// row index of the element node
	private int cIdx;// column index of the element node
	private int value; // the value stored in the element node

	private ENode next;// the node next to this one
	private ENode previous;// the node before this one

	public ENode() {
		// empty we don't want to set anything
	}

	public ENode(ENode other) {
		// copy constructor

		rIdx = other.rIdx;
		cIdx = other.cIdx;
		value = other.value;
	}

	public ENode(int rIdx, int cIdx, int value) {
		this.rIdx = rIdx;
		this.cIdx = cIdx;
		this.value = value;
	}

	public ENode(int rIdx, int cIdx, int value, ENode next, ENode previous) {
		this.rIdx = rIdx;
		this.cIdx = cIdx;
		this.value = value;
		this.next = next;
		this.previous = previous;
	}

	public int getcIdx() {
		return cIdx;
	}

	public ENode getNext() {
		return next;
	}

	public ENode getPrevious() {
		return previous;
	}

	public int getrIdx() {
		return rIdx;
	}

	public int getValue() {
		return value;
	}

	public void setcIdx(int cIdx) {
		this.cIdx = cIdx;
	}

	public void setNext(ENode next) {
		this.next = next;
	}

	public void setPrevious(ENode previous) {
		this.previous = previous;
	}

	public void setrIdx(int rIdx) {
		this.rIdx = rIdx;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
