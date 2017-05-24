package model;

public class Board {
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
	private String[][] cells = new String[3][3];
	private int placed = 0;
	
	public Board() {
		reset();
	}
	
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
	
	public int getPlaced() {
		return placed;
	}
	
	public void setMove(Cell cell, String sign) throws Exception {
		if (cells[cell.getY()][cell.getX()].equals("")) {
			cells[cell.getY()][cell.getX()] = sign;
			++placed;
		} else {
			throw new Exception();
		}
	}
	
	public Cell[] getWinners() throws Exception {
		for(Cell[] set : winners) {
			if(checkCells(set[0], set[1], set[2])) {
				return set;
			}
		}
		throw new Exception();
	}
	
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