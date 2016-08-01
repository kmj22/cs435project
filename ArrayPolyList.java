public class ArrayPolyList implements PolyList<ArrayEntry> {
		
	// # of positions in the list
	private int n = 0;
	
	// Current size of array
	private int arraySize = 10;
	
	//array to store ArrayEntries
	ArrayEntry[] array = new ArrayEntry[arraySize];
    
    public ArrayPolyList() {
        
    }
    
    public void insertTerm(int c, int i, int j) {
		//don't bother if the constant is 0
        if (c == 0) return;
		
		//if array is full, grow the array
		if (n >= arraySize){
			grow();
		}
		
		//start a at index after highest current index
		int a = n;
		boolean insertFlag = true;
		
		//find where to insert newEntry
		while (a > 0 && (i < array[a-1].getI() || (i == array[a-1].getI() && j < array[a-1].getJ()))){
			a--;
		}
		
		//if the x and y degrees are the same- no insert required
		if (a != 0 && i == array[a-1].getI() && j == array[a-1].getJ()){
			insertFlag = false;
			int newC = c + array[a-1].getC();
			
			//if the sum of constants is 0, remove the entry
			if (newC == 0){
				shiftL(a);
				n -= 1;
			}
			else{
				ArrayEntry newEntry = new ArrayEntry(newC,i,j,a-1);
				array[a-1] = newEntry;	
			}
		}
		//otherwise, a new entry must be inserted at a
		else{
			//new node to be inserted
			ArrayEntry newEntry = new ArrayEntry(c,i,j,a);
			
			//if newEntry is the new highest term, no need to shift
			if (a == n){
				array[a] = newEntry;
			}
			//otherwise, shift to the right starting at a and then insert
			else{
				shiftR(a);
				array[a] = newEntry;
			}
		}		
		
		if (insertFlag){
			n += 1;
		}
    }
    
    private void shiftR(int startIndex){
		for (int a = n; a > startIndex; a--){
			array[a] = array[a - 1];
			array[a].setIndex(a);
		}
	}
	
    private void shiftL(int removeIndex){
		for (int a = removeIndex; a < n; a++){
			if (array[a + 1] != null){
				array[a] = array[a + 1];
				array[a].setIndex(a);
			}
		}
	}
	
	private void grow(){
		ArrayEntry[] newArray = new ArrayEntry[arraySize*2];
		for (int a = 0; a < arraySize; a++){
			newArray[a] = array[a];
		}
		arraySize *= 2;
		array = newArray;
	}
	
	public ArrayEntry first() {
		return array[n-1];
    }

    public ArrayEntry last() {
        return array[0];
    }

    public boolean isFirst(ArrayEntry p) {
		return (p.getIndex() == n-1);
    }

    public boolean isLast(ArrayEntry p) {
		return (p.getIndex() == 0);        
    }

    public ArrayEntry before(ArrayEntry p) throws IndexOutOfBoundsException{
        if (!isFirst(p))
			return array[p.getIndex() + 1];
		else
			throw new IndexOutOfBoundsException();
    }

    public ArrayEntry after(ArrayEntry p) throws IndexOutOfBoundsException{
        if (!isLast(p))
			return array[p.getIndex() - 1];
		else
			throw new IndexOutOfBoundsException();
    }

    public boolean isEmpty() {
		return (n == 0);
    }

    public int size() {
        return n;
    }
   
}
