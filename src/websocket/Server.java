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

import com.google.gson.Gson;

@ServerEndpoint(
		value = "/game", 
		encoders = { MessageEncoder.class }, 
		decoders = { MessageDecoder.class }
)
public class Server {
	private static Random random = new Random();
	private static Set<Session> clients = 
		    Collections.synchronizedSet(new HashSet<Session>());
	private static PlayersList playerslist = new PlayersList();
	private static List<Player> fighters = new ArrayList<Player>(2);
	private static Board board = new Board();
	
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
	
	@OnOpen
	public void onOpen(Session session) {
		clients.add(session);
		Player player = new Player();
		player.setId(session.getId());
		playerslist.add(player);
		System.out.println("Client connected");
	}
	
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
	
	private void onWalkover(Player player, Session session) throws IOException, EncodeException {
		Player enemy = findEnemy(player);
		enemy.incrementScore();
		enemy.setInGame(false);
		Message response = new Message();
		response.setType("walkover");
		callEnemyMessage(enemy, response);
		fighters.remove(player);
		playerslist.remove(player);
		clients.remove(session);
		setNewFight(player);
	}
	
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
	
	private void callEnemyMessage(Player enemy, Message message) throws IOException, EncodeException {
		synchronized(clients) {
			for(Session client : clients){
				if (client.getId().equals(enemy.getId())) {
					client.getBasicRemote().sendObject(message);
				}
			}
		}
	}
	
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