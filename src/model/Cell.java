package model;

/**
 * Class represents a single cell in a board
 */
public class Cell {
	/**
	 * Horizontal coordinate
	 */
	private int x;
	
	/**
	 * Vertical coordinate
	 */
	private int y;
	
	/**
	 * Default constructor makes the created cell empty
	 */
	public Cell(){}
	
	/**
	 * Initialization the created cell
	 * @param x Horizontal coordinate
	 * @param y Vertical coordinate
	 */
	public Cell(int x, int y) {
		setX(x);
		setY(y);
	}
	
	/**
	 * Return horizontal coordinate of the cell
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Set the horizontal coordinate of the cell
	 * @param x Horizontal coordinate
	 * @return
	 */
	public Cell setX(int x) {
		this.x = x;
		return this;
	}
	
	/**
	 * Return vertical coordinate of the cell
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Set the vertical coordinate of the cell
	 * @param y Vertical coordinate
	 * @return
	 */
	public Cell setY(int y) {
		this.y = y;
		return this;
	}
}
