public class DLLPolyList implements PolyList<DLLNode> {

    private DLLNode head;
	private DLLNode tail;
	
	private int size;

    public DLLPolyList() {
		this.head = null;
		this.tail = null;
		this.size = 0;
    }
    
   
    public void insertTerm(int c, int i, int j) {
        //don't bother if constant is 0
		if (c == 0) return;
		
		//create new node to be inserted
		DLLNode node = new DLLNode(c,i,j);
		boolean insertFlag = true;
		
		//list is empty
		if (isEmpty()){
			head = tail = node;
		}
		//new head
		else if (i > head.getI() || (i == head.getI() && j > head.getJ())){
			node.setNext(head);
			head.setPrev(node);
			head = node;
		}
		//new tail
		else if (i < tail.getI() || (i == tail.getI() && j < tail.getJ())){
			node.setPrev(tail);
			tail.setNext(node);
			tail = node;
		}
		else{
			//temporary pointer to node
			DLLNode curr = head;
			
			//sort descending by degree of x, then y
			while (curr != null && i < curr.getI() || (i == curr.getI() && j < curr.getJ())){
				curr = curr.getNext();
			}
			
			//degree of x and y are equal
			if (i == curr.getI() && j == curr.getJ()){
				//nothing new will be inserted
				insertFlag = false;
				
				//add the constants
				int newC = c + curr.getC();
				
				//if the sum of constants is not zero
				if (newC != 0){
					//replace curr with new sumNode
					DLLNode sumNode = new DLLNode(newC,i,j);	
					sumNode.setNext(curr.getNext());
					sumNode.setPrev(curr.getPrev());
					if (curr != head)
						curr.getPrev().setNext(sumNode);
					else
						head = sumNode;
					if (curr != tail)	
						curr.getNext().setPrev(sumNode);
					else
						tail = sumNode;
				}
				else{
					//if newC is zero, remove curr
					removeNode(curr);
				}
				
			}
			else{
				//link node to next and previous
				node.setNext(curr);
				node.setPrev(curr.getPrev());
				curr.getPrev().setNext(node);
				curr.setPrev(node);
			}
		}
		
		if (insertFlag){
			size++;
		}
		
    }

	private void removeNode(DLLNode p){	
		if (size == 1){
			head = tail = null;
		}
		else if (isFirst(p)){
			head = p.getNext();
			head.setPrev(null);
		}
		else if (isLast(p)){
			tail = p.getPrev();
			tail.setNext(null);
		}
		else if(size > 2){
			p.getNext().setPrev(p.getPrev());
			p.getPrev().setNext(p.getNext());
		}
		size--;
	}
	
    public DLLNode first() {
        return head;
    }

    public DLLNode last() {
        return tail;
    }

    public boolean isFirst(DLLNode p) {
		return (p == head);
    }

    public boolean isLast(DLLNode p) {
        return (p == tail);
    }

    public DLLNode before(DLLNode p) throws IndexOutOfBoundsException{//handle error case
		if (p != head)
			return p.getPrev();
		else
			throw new IndexOutOfBoundsException();
    }

    public DLLNode after(DLLNode p) throws IndexOutOfBoundsException{//handle error case
		if (p != tail)
			return p.getNext();
		else
			throw new IndexOutOfBoundsException();
    }

    public boolean isEmpty() {
        return (head == null && tail == null);
    }

    public int size() {
		return size;
    }

}
