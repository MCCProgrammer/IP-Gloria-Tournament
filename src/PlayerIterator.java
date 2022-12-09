/**
 * @author Miguel Cacais do Carmo && Tiago Pinto Pereira
 * 		   The purpose of this class is to give information from other class (ex: Player) to 
 * 		   the main without people from the outside getting the data and modified them
 */

public class PlayerIterator {
	
	private Player[] players;
	private int size;
	private int nextIndex;
	
	public PlayerIterator(Player[] players,int size) {
		this.players=players;
		this.size=size;
		nextIndex=0;
	}
	
	public boolean hasNext() {
		return nextIndex<size;
	}
	
	public Player next() {
		return players[nextIndex++];
	}
	
}
