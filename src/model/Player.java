package model;

/**
 * Class contains information about a player
 */
public class Player {
	/**
	 * Player id
	 */
	private transient String id;
	
	/**
	 * Player name
	 */
	private String name;
	
	/**
	 * Number of player's points
	 */
	private int score = 0;
	
	/**
	 * Player sign
	 */
	private String sign = "";
	
	/**
	 * A flag which informs if the player is playing the game for now
	 */
	private boolean inGame;

	/**
	 * Return player id
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set player id
	 * @param id Player id
	 * @return
	 */
	public Player setId(String id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Return player name
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set player name
	 * @param name Player name
	 * @return
	 */
	public Player setName(String name) {
		this.name = name;
		return this;
	}
	
	/**
	 * Return a number of points
	 * @return
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Set number of points
	 * @param score Number of points
	 * @return
	 */
	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	
	/**
	 * Increment a number of player's points
	 */
	public void incrementScore() {
		++score;
	}
	
	/**
	 * Return a sign of the player when he is in a game
	 * @return
	 */
	public String getSign() {
		return sign;
	}
	
	/**
	 * Set the given sign
	 * @param sign The sign
	 * @return
	 */
	public Player setSign(String sign) {
		this.sign = sign;
		return this;
	}
	
	/**
	 * Check if the player is fighting for now
	 * @return
	 */
	public boolean getInGame() {
		return inGame;
	}
	
	/**
	 * Set the flag if the player is fighting for now or not
	 * @param inGame A flag
	 * @return
	 */
	public Player setInGame(boolean inGame) {
		this.inGame = inGame;
		return this;
	}
}
