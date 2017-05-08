package model;

public class Cell {
	private int x;
	private int y;
	
	public Cell(){}
	public Cell(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public int getX() {
		return x;
	}
	public Cell setX(int x) {
		this.x = x;
		return this;
	}
	public int getY() {
		return y;
	}
	public Cell setY(int y) {
		this.y = y;
		return this;
	}
}
