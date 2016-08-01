public abstract class Position {
    private int c;
	private int i;
	private int j;
    
    public Position(int c, int i, int j) {
        this.c = c;
		this.i = i;
		this.j = j;
    }
    
    public int getC() {
        return c;
    }
	
	public int getI() {
		return i;
	}
	
	public int getJ() {
		return j;
	}
}
