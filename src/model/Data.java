package model;

/**
 * Class contains data of messages between a server and clients
 */
public class Data {
	/**
	 * List of all logged players
	 */
	private PlayersList playersList;
	
	/**
	 * Game's board
	 */
	private Board board;
	
	/**
	 * One cell
	 */
	private Cell cell;
	
	/**
	 * Player object
	 */
	private Player player;
	
	/**
	 * Info about a message
	 */
	private String info;
	
	/**
	 * Set of cells which creates a winner composition
	 */
	private Cell[] winners;
	
	/**
	 * Return a players list
	 * @return
	 */
	public PlayersList getPlayersList() {
		return playersList;
	}
	
	/**
	 * Set the given players list
	 * @param playersList Players list object
	 * @return
	 */
	public Data setPlayersList(PlayersList playersList) {
		this.playersList = playersList;
		return this;
	}
	
	/**
	 * Return a board object
	 * @return
	 */
	public Board getBoard() {
		return board;
	}
	
	/**
	 * Set a board
	 * @param board Board object
	 * @return
	 */
	public Data setBoard(Board board) {
		this.board = board;
		return this;
	}
	
	/**
	 * Return a single cell object
	 * @return
	 */
	public Cell getCell() {
		return cell;
	}
	
	/**
	 * Set a single cell
	 * @param cell Cell object
	 * @return
	 */
	public Data setCell(Cell cell) {
		this.cell = cell;
		return this;
	}
	
	/**
	 * Return a player
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Set a player
	 * @param player Player object
	 * @return
	 */
	public Data setPlayer(Player player) {
		this.player = player;
		return this;
	}
	
	/**
	 * Return a message with information
	 * @return
	 */
	public String getInfo() {
		return info;
	}
	
	/**
	 * Set a message with information
	 * @param info Text message
	 * @return
	 */
	public Data setInfo(String info) {
		this.info = info;
		return this;
	}
	
	/**
	 * Return a set of cells which represent a winning composition
	 * @return
	 */
	public Cell[] getWinners() {
		return winners;
	}
	
	/**
	 * Set a set of winning cells
	 * @param winners Set of winning cells
	 * @return
	 */
	public Data setWinners(Cell[] winners) {
		this.winners = winners;
		return this;
	}
}
