package model;

public class Player {
	private transient String id;
	private String name;
	private int score = 0;
	private String sign = "";
	private boolean inGame;

	public String getId() {
		return id;
	}
	public Player setId(String id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public Player setName(String name) {
		this.name = name;
		return this;
	}
	public int getScore() {
		return score;
	}
	public Player setScore(int score) {
		this.score = score;
		return this;
	}
	public void incrementScore() {
		++score;
	}
	public String getSign() {
		return sign;
	}
	public Player setSign(String sign) {
		this.sign = sign;
		return this;
	}
	public boolean getInGame() {
		return inGame;
	}
	public Player setInGame(boolean inGame) {
		this.inGame = inGame;
		return this;
	}
}
