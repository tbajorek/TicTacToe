package model;

public class Data {
	private PlayersList playersList;
	private Board board;
	private Cell cell;
	private Player player;
	private String info;
	private Cell[] winners;
	
	public PlayersList getPlayersList() {
		return playersList;
	}
	public Data setPlayersList(PlayersList playersList) {
		this.playersList = playersList;
		return this;
	}
	public Board getBoard() {
		return board;
	}
	public Data setBoard(Board board) {
		this.board = board;
		return this;
	}
	public Cell getCell() {
		return cell;
	}
	public Data setCell(Cell cell) {
		this.cell = cell;
		return this;
	}
	public Player getPlayer() {
		return player;
	}
	public Data setPlayer(Player player) {
		this.player = player;
		return this;
	}
	public String getInfo() {
		return info;
	}
	public Data setInfo(String info) {
		this.info = info;
		return this;
	}
	public Cell[] getWinners() {
		return winners;
	}
	public Data setWinners(Cell[] winners) {
		this.winners = winners;
		return this;
	}
}
