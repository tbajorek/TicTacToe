package model;

/**
 * Class represents a game's board
 */
public class Board {
	/**
	 * List of all possible set of winning cells
	 */
	private static Cell[][] winners = {
			{new Cell(0,0), new Cell(0,1), new Cell(0,2)},
			{new Cell(1,0), new Cell(1,1), new Cell(1,2)},
			{new Cell(2,0), new Cell(2,1), new Cell(2,2)},
			{new Cell(0,0), new Cell(1,0), new Cell(2,0)},
			{new Cell(0,1), new Cell(1,1), new Cell(2,1)},
			{new Cell(0,2), new Cell(1,2), new Cell(2,2)},
			{new Cell(0,0), new Cell(1,1), new Cell(2,2)},
			{new Cell(2,0), new Cell(1,1), new Cell(0,2)},
	};
	
	/**
	 * 2-dimensional array of cells that is empty or contains characters
	 */
	private String[][] cells = new String[3][3];
	
	/**
	 * Counter of placed cells
	 */
	private int placed = 0;
	
	/**
	 * Create an empty board
	 */
	public Board() {
		reset();
	}
	
	/**
	 * Reset the board. It fulfills the board by empty signs.
	 */
	public void reset() {
		for( int y = 0; y <= cells.length - 1; y++)
		{
			for( int x = 0; x <= cells[y].length - 1; x++)
			{
				cells[y][x] = "";
			}
		}
		placed = 0;
	}
	
	/**
	 * Return a number of placed cells
	 * @return
	 */
	public int getPlaced() {
		return placed;
	}
	
	/**
	 * Make a move. The function sets the given sign in the cell.
	 * @param cell Cell object where is made a move
	 * @param sign Set sign character
	 * @throws Exception Thrown when target cell is not empty
	 */
	public void setMove(Cell cell, String sign) throws Exception {
		if (cells[cell.getY()][cell.getX()].equals("")) {
			cells[cell.getY()][cell.getX()] = sign;
			++placed;
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Return a winning set of cells if it exists
	 * @return
	 * @throws Exception Thrown if any winning set isn't exist on the board
	 */
	public Cell[] getWinners() throws Exception {
		for(Cell[] set : winners) {
			if(checkCells(set[0], set[1], set[2])) {
				return set;
			}
		}
		throw new Exception();
	}
	
	/**
	 * Check if the given set of cells contains winning configuration on the board
	 * @param c1 First cell
	 * @param c2 Second cell
	 * @param c3 Third cell
	 * @return
	 */
	private boolean checkCells(Cell c1, Cell c2, Cell c3) {
		if(cells[c1.getY()][c1.getX()].equals(cells[c2.getY()][c2.getX()]) &&
		   cells[c2.getY()][c2.getX()].equals(cells[c3.getY()][c3.getX()]) &&
		   cells[c3.getY()][c3.getX()].equals(cells[c1.getY()][c1.getX()]) &&
		   !cells[c1.getY()][c1.getX()].equals("")) {
			return true;
		} else {
			return false;
		}
	}
}