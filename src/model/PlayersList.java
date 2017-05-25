package model;

import java.util.*;

/**
 * Class contains a list of all players logged in a game
 */
public class PlayersList {
	/**
	 * List of all logged players
	 */
	Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
	
	/**
	 * Add a new player to a game
	 * @param player Object of a new player
	 * @return
	 */
	public boolean add(Player player) {
		return players.add(player);
	}
	
	/**
	 * Remove the player from a game
	 * @param player Object of a removed player
	 * @return
	 */
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
	
	/**
	 * Return a number of logged players
	 * @return
	 */
	public int size() {
		return players.size();
	}
	
	/**
	 * Return an object of a player whose identifier is given as a parameter
	 * @param id Id of desired player
	 * @return
	 */
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
	
	/**
	 * Return a player who will be an enemy to the given player
	 * @param enemy The player who is fighting and who needs to find a next enemy
	 * @return
	 */
	public Player getPlayerForFight(Player enemy) {
		Player foundPlayer = null;
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player player = it.next();
			if (!player.getId().equals(enemy.getId())) {
				if(foundPlayer == null) {
					foundPlayer = player;
				} else {
					if (foundPlayer.getScore() > player.getScore()) {
						foundPlayer = player;
					}
				}
			}
	    }
		return foundPlayer;
	}
	
	/**
	 * Return an enemy who is first in a current fighting
	 * @return
	 */
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
