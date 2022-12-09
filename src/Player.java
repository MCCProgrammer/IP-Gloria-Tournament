/**
 * @author Miguel Cacais do Carmo && Tiago Pinto Pereira
 * 		   The purpose of this class is to store data
 *         from each player i.e. each object(player) of this class will store
 *         different information (like: color, currentSquare, etc)
 */

public class Player {
	private static final int FIRST_SQUARE = 1,POSITIVE=1,NEGATIVE=-1;
	private String color;
	private boolean isEliminated;

	private int currentSquare, nrFines, gamesWon, playerOrder, eliminationOrder;

	/**
	 * 
	 * @param color
	 * @param playerOrder
	 * @pre: color!=null && 1 <= playerOrder <= nPlayers
	 */
	public Player(String color, int playerOrder) {
		this.color = color;
		this.playerOrder = playerOrder;
		
		currentSquare = FIRST_SQUARE;
		isEliminated = false;
		nrFines = 0;
		gamesWon = 0;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getGamesWon() {
		return gamesWon;
	}
	
	public int getNumberOfFines() {
		return nrFines;
	}
	
	public int getCurrentSquare() {
		return currentSquare;
	}
	
	public int getEliminationOrder() {
		return eliminationOrder;
	}
	
	public int getPlayerOrder() {
		return playerOrder;
	}
	public boolean isEliminated() {
		return isEliminated;
	}
	
	public boolean hasFine() {
		return nrFines > 0;
	}

	/**
	 * @param result
	 * @pre: 1 <= result <= totalSquares
	 */
	public void setCurrentSquare(int result) {
		currentSquare = result;
	}

	/**
	 * 
	 * @param n
	 * @pre: 1 <= n <= nPlayer - 1
	 */
	public void setEliminationOrder(int n) {
		eliminationOrder = n;
	}
	
	public void setEliminated() {
		isEliminated = true;
	}

	/**
	 * 
	 * @param n
	 */
	public void increaseFine(int n) {
		nrFines += n;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void decreaseFine(int n) {
		nrFines -= n;
	}
	
	/**
	 * 
	 * @param n
	 */
	public void increaseGamesWon(int n) {
		gamesWon += n;
	}
	
	/**
	 * @pre: the current game must be over
	 */
	public void resetCurrentSquare() {
		currentSquare = FIRST_SQUARE;
	}
	
	/**
	 * @pre: the current game must be over
	 */
	public void resetFines() {
		nrFines = 0;
	}

	/**
	 * 
	 * @param other
	 * @pre: other != null
	 * @return a positive or a negative number depending on the criteria we are using (ex: players
	 *  are sorted in descending order according to the current square)
	 */
	public int compareTo(Player other) {
		int result;
		boolean condition = isEliminated && other.isEliminated();
		if (condition) {
			result = eliminationOrder - other.getEliminationOrder();
			if (result > 0)
				return NEGATIVE;
			return POSITIVE;
		}
		condition = isEliminated || other.isEliminated();
		if (condition) {
			if (isEliminated)
				return POSITIVE;
			return NEGATIVE;
		}
		result = other.getGamesWon() - gamesWon;
		if (result != 0)
			return result;
		result = other.getCurrentSquare() - currentSquare;
		if (result != 0)
			return result;
		return playerOrder - other.getPlayerOrder();
	}
	
}