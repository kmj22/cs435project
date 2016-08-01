public class ArrayEntry extends Position {

	// This entry's index within the array.
    private int index;
	
    public ArrayEntry(int c, int i, int j, int index) {
        super(c, i, j);
		
		this.index = index;
    }
	
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
}
