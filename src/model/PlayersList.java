package model;

import java.util.*;

import javax.websocket.Session;

public class PlayersList {
	Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
	
	public boolean add(Player player) {
		return players.add(player);
	}
	
	public boolean remove(Player player) {
		Iterator<Player> it = players.iterator();
	    while (it.hasNext()) {
	        if (it.next().getId().equals(player.getId())) {
	            it.remove();
	            return true;
	        }
	    }
	    return false;
	}
	
	public int size() {
		return players.size();
	}
	
	public Player getById(String id) {
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player player = it.next();
	        if (player.getId().equals(id)) {
	            return player;
	        }
	    }
		return new Player();
	}
	
	public Player getPlayerForFight(Player enemy) {
		Player foundPlayer = null;
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player player = it.next();
			if(!player.getId().equals(enemy.getId())) {
				foundPlayer = player;
			}
	    }
		it = players.iterator();
		while (it.hasNext()) {
			Player player = it.next();
			if(foundPlayer == null) {
				foundPlayer = player;
			} else {
				if (foundPlayer.getScore() > player.getScore() && !player.getId().equals(enemy.getId())) {
					foundPlayer = player;
				}
			}
	    }
		return foundPlayer;
	}
	
	public Player getPlayerForFight() {
		Player foundPlayer = null;
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player player = it.next();
			if(foundPlayer == null) {
				foundPlayer = player;
			} else {
				if (foundPlayer.getScore() > player.getScore()) {
					foundPlayer = player;
				}
			}
	    }
		return foundPlayer;
	}
}
