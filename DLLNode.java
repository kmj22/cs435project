public class DLLNode extends Position {
    private DLLNode next;
	private DLLNode prev;

    public DLLNode(int c, int i, int j) {
        super(c, i, j);
		
		this.next = null;
		this.prev = null;
    }
	
	
	public DLLNode getNext() {
		return next;
	}
	
	public DLLNode getPrev() {
		return prev;
	}
	
	public void setNext(DLLNode next) {
		this.next = next;
	}	
	
	public void setPrev(DLLNode prev) {
		this.prev = prev;
	}
}
