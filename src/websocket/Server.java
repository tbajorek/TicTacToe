package websocket;

import model.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Main class of Websocket server
 */
@ServerEndpoint(
		value = "/game", 
		encoders = { MessageEncoder.class }, 
		decoders = { MessageDecoder.class }
)
public class Server {
	/**
	 * Random generator
	 */
	private static Random random = new Random();
	
	/**
	 * List of all clients connections
	 */
	private static Set<Session> clients = 
		    Collections.synchronizedSet(new HashSet<Session>());
	
	/**
	 * List of all players
	 */
	private static PlayersList playerslist = new PlayersList();
	
	/**
	 * List of two players who are fighting for the current time
	 */
	private static List<Player> fighters = new ArrayList<Player>(2);
	
	/**
	 * Board object
	 */
	private static Board board = new Board();
	
	/**
	 * Callback for coming messages
	 * @param input Message Object
	 * @param session Session Object
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	@OnMessage
	public void onMessage(Message input, Session session) 
			throws IOException, EncodeException {
		Message response = new Message();
		boolean isResponse = false;
		switch(input.getType()) {
			case "newplayer":
				isResponse = onNewPlayer(input, response, session);
				break;
			case "move":
				isResponse = onMove(input, response, session);
				break;
		}
		if (isResponse) {
			session.getBasicRemote().sendObject(response);
		}
	}
	
	/**
	 * Callback for opening a new connection
	 * @param session Session object
	 */
	@OnOpen
	public void onOpen(Session session) {
		clients.add(session);
		Player player = new Player();
		player.setId(session.getId());
		playerslist.add(player);
		System.out.println("Client connected");
	}
	
	/**
	 * Callback for closing a connection
	 * @param session Session object
	 */
	@OnClose
	public void onClose(Session session) {
		Player player = playerslist.getById(session.getId());
		if(player.getInGame()) {
			try {
				onWalkover(player, session);
			} catch(Exception e) {
				System.out.println("Walkover has been unsuccessfull");
			}
		} else {
			playerslist.remove(player);
			clients.remove(session);
		}
		System.out.println("Connection closed");
	}
	
	/**
	 * Action called when a new player joins to the game
	 * @param input Input message
	 * @param output Output message
	 * @param session Session of a new player
	 * @return
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private boolean onNewPlayer(Message input, Message output, Session session) throws IOException, EncodeException {
		Player player = playerslist.getById(session.getId());
		player.setName(input.getData().getPlayer().getName());
		if (fighters.size() < 2) {
			fighters.add(player);
			if (fighters.size() == 2) {
				fighters.get(0).setSign(((random.nextInt()%2 == 1)?"X":"O"));
				if (fighters.get(0).getSign().equals("X")) {
					fighters.get(1).setSign("O");
				} else {
					fighters.get(1).setSign("X");
				}
				onStartTheGame();
			}
		}
		broadWithList();
		return false;
	}
	
	/**
	 * Action called when a new game starts
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void onStartTheGame() throws IOException, EncodeException {
		Message startMsg = new Message();
		startMsg.setType("startgame");
		fighters.get(0).setInGame(true);
		fighters.get(1).setInGame(true);
		broadWithList();
		board.reset();
		synchronized(clients) {
			for(Session client : clients){
				if(client.getId().equals(fighters.get(0).getId())) {
					startMsg.getData().setPlayer(fighters.get(0));
					client.getBasicRemote().sendObject(startMsg);
				} else if(client.getId().equals(fighters.get(1).getId())) {
					startMsg.getData().setPlayer(fighters.get(1));
					client.getBasicRemote().sendObject(startMsg);
				}
			}
		}
	}
	
	/**
	 * Action called when a player, who is fighting, makes a move
	 * @param input Input message
	 * @param output Output message
	 * @param session Session of the player who has made a move
	 * @return
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private boolean onMove(Message input, Message output, Session session) throws IOException, EncodeException {
		Message moveMsg = new Message();
		moveMsg.setType("move");
		moveMsg.getData().setCell(input.getData().getCell());
		Player player = playerslist.getById(session.getId());
		try {
			board.setMove(input.getData().getCell(), player.getSign());
		} catch(Exception e) {
			output.setType("errormove");
			return true;
		}
		callEnemyMessage(findEnemy(playerslist.getById(session.getId())), moveMsg);
		Player enemy = findEnemy(player);
		try {
			Cell[] winners = board.getWinners();
			player.incrementScore();
			Message resultMsg = new Message();
			resultMsg.getData().setWinners(winners);
			resultMsg.setType("win");
			session.getBasicRemote().sendObject(resultMsg);
			resultMsg.setType("lost");
			callEnemyMessage(enemy, resultMsg);
			setNewFight(player);
			return false;
		} catch(Exception e) {}
		if(board.getPlaced() == 9) {//tie
			onTie(player, enemy, session);
		}
		return false;
	}
	
	/**
	 * Action called after one player left the fighting. The second player wins.
	 * @param player The player who is still staying in the game
	 * @param session Session object of the player
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void onWalkover(Player player, Session session) throws IOException, EncodeException {
		Player enemy = findEnemy(player);
		enemy.incrementScore();
		enemy.setInGame(false);
		Message response = new Message();
		response.setType("walkover");
		callEnemyMessage(enemy, response);
		playerslist.remove(player);
		clients.remove(session);
		setNewFight(enemy);
	}
	
	/**
	 * Action called when nobody of fighting players won
	 * @param player First player
	 * @param enemy Second player
	 * @param session Session of player connection
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void onTie(Player player, Player enemy, Session session) throws IOException, EncodeException {
		Message tieMsg = new Message();
		tieMsg.setType("tie");
		session.getBasicRemote().sendObject(tieMsg);
		callEnemyMessage(enemy, tieMsg);
		fighters.remove(1).setInGame(false);
		fighters.remove(0).setInGame(false);
		fighters.add(playerslist.getPlayerForFight().setInGame(true));
		setNewFight(fighters.get(0));
	}
	
	/**
	 * Send a broadcast message with players list to all players
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void broadWithList() throws IOException, EncodeException {
		Message broadMsg = new Message();
		broadMsg.setType("list")
				.getData().setPlayersList(playerslist);
		synchronized(clients) {
			for(Session client : clients){
				client.getBasicRemote().sendObject(broadMsg);
			}
		}
	}
	
	/**
	 * Send the given message to the enemy
	 * @param enemy Enemy player
	 * @param message Message object
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void callEnemyMessage(Player enemy, Message message) throws IOException, EncodeException {
		synchronized(clients) {
			for(Session client : clients){
				if (client.getId().equals(enemy.getId())) {
					client.getBasicRemote().sendObject(message);
				}
			}
		}
	}
	
	/**
	 * Set a new fight between the given player and other player chosen from players list.
	 * @param player Player who stays in game. The second player will be his enemy.
	 * @throws IOException Thrown when an error with I/O operations occurred
	 * @throws EncodeException Thrown when encoder can't encode outgoing message
	 */
	private void setNewFight(Player player) throws IOException, EncodeException {
		if(playerslist.size() > 1) {
			Player enemy = findEnemy(player);
			if(fighters.contains(enemy)) {
				enemy.setInGame(false);
				fighters.remove(enemy);
			}
			fighters.get(0).setSign(((random.nextInt()%2 == 1)?"X":"O"));
			Player newEnemy = playerslist.getPlayerForFight(player);
			fighters.add(newEnemy.setInGame(true));
			if (fighters.get(0).getSign().equals("X")) {
				fighters.get(1).setSign("O");
			} else {
				fighters.get(1).setSign("X");
			}
			try {
				Thread.sleep(4000);
			} catch(InterruptedException e) {}
			onStartTheGame();
		} else {
			broadWithList();
		}
	}
	
	/**
	 * Find a second player who is fighting now against the given player
	 * @param player Player object
	 * @return
	 */
	private Player findEnemy(Player player) {
		if (fighters.size() < 2) {
			return new Player();
		}
		if (fighters.get(0).getId().equals(player.getId())) {
			return fighters.get(1);
		} else if (fighters.get(1).getId().equals(player.getId())) {
			return fighters.get(0);
		} else {
			return new Player();
		}
	}
}