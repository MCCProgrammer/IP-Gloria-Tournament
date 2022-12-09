/**
 * @author Miguel Cacais do Carmo && Tiago Pinto Pereira 
 * 		   The purpose of this class is to be the system class,therefore, 
 * 		   it is the class that coordinates all the other classes. In this case 
 * 		   it will coordinate all the moves and the players times to play that 
 * 		   are happening during the match.It also stores all the information about the board.
 */

public class GloriaGame {
	
	/**
	 * Integer variables that are used to identify the square types
	 */
	private static final int CRAB_TYPE = 1, DEATH_TYPE = 2, HELL_TYPE = 3;
	private static final int BIRD_ADVANCE = 9, BY_ONE = 1;
	private static final int FINE = 1, CLIFF = 2;

	private Player[] players;

	/**
	 * 
	 * matrix that will have the length equals to totalSquares and it will tell
	 * witch squares are cliffs or fines and the corresponding types
	 */
	private int[][] cliffsAndFines;

	/* The variable firstRound will tell if it is the first time the player has
	   played in a game*/
	private int nextPlayer, nPlayers, totalSquares, firstRound;
	private boolean isCupOver;
	private int playersEliminated, gamesPlayed;
	private String winnerPlayer;
	private boolean hasDeathOccured;

	/**
	 * 
	 * @param totalSquares
	 * @param nPlayers
	 * @param squaresWithFine
	 * @param squaresWithCliff
	 * @pre: 10 <= totalSquares <= 150 && squaresWithFine != null && 
	 * 		 squaresWithCliff != null && 3 <= nPlayers <= 10
	 */
	public GloriaGame(int totalSquares, int nPlayers, int[][] squaresWithFine, 
					  int[][] squaresWithCliff) {

		this.totalSquares = totalSquares;
		this.nPlayers = nPlayers;
		firstRound = nPlayers;

		players = new Player[nPlayers];
		createCliffsAndFines(squaresWithFine, squaresWithCliff);
		gamesPlayed = 0;
		hasDeathOccured = false;
		playersEliminated = 0;
		isCupOver = false;
		nextPlayer = 1;
	}
	
	/**
	 * 
	 * @param color
	 * @param indexPlayer
	 * @pre: color != null && 0 <= indexPlayer <= nPlayers - 1
	 */
	public void addNewPlayer(String color, int indexPlayer) {
		players[indexPlayer] = new Player(color, indexPlayer + 1);
	}

	/**
	 * 
	 * @param playerColor
	 * @pre: playerColor!=null
	 * @return either the variable playerColor pass on the argument is a color of an
	 *         existent player or not
	 */
	public boolean isCorValid(String playerColor) {
		int i = 0;
		while (i < nPlayers && !(players[i].getColor().equals(playerColor))) {
			i++;
		}
		return i != nPlayers;
	}
	
	/**
	 * 
	 * @return either the cup is or not over
	 */
	public boolean isCupOver() {
		return isCupOver;
	}
	
	/**
	 * 
	 * @param playerColor
	 * @pre: playerColor != null && isCorValid(playerColor) returns true
	 * @return either the player is eliminated or not
	 */
	public boolean isEliminated(String playerColor) {
		return getPlayerByColor(playerColor).isEliminated();
	}

	/**
	 * 
	 * @param playerColor
	 * @pre: playerColor!=null && isCorValid(playerColor) returns true
	 * @return either the player(witch have the same color) has fines or not
	 */
	public boolean canRollDice(String playerColor) {
		return getPlayerByColor(playerColor).getNumberOfFines() == 0;
	}

	/**
	 * 
	 * @return the color of the player that won the game
	 * @pre: isCupOver() returns true
	 */
	public String getWinnerColor() {
		return winnerPlayer;
	}

	/**
	 * 
	 * @param playerColor
	 * @pre: playerColor!=null && isCorValid(playerColor) returns true
	 * @return the player(with the corresponding color) current home
	 */
	public int getCurrentSquare(String playerColor) {
		return getPlayerByColor(playerColor).getCurrentSquare();
	}

	/**
	 * @pre: the current game must be finished && the cup must not be over
	 */
	private void resetGame() {
		for (int i = 0; i < nPlayers; i++) {
			if (!players[i].isEliminated()) {
				players[i].resetCurrentSquare();
				players[i].resetFines();
			}
		}
		hasDeathOccured = false;
		nextPlayer = 1;
		firstRound = nPlayers - playersEliminated;
	}

	/**
	 * 
	 * @param index
	 * @pre: 0 <= index < nPlayers
	 * @return the player color that corresponds to the specific index
	 */
	private String getPlayerColorByIndex(int index) {
		return players[index].getColor();
	}

	/**
	 * @param playerColor
	 * @pre: playerColor!=null
	 * @return the player that corresponds to the specific player color
	 */
	private Player getPlayerByColor(String playerColor) {
		int i = 0;
		while (i < nPlayers && !players[i].getColor().equals(playerColor)) {
			i++;
		}
		return players[i];
	}

	/**
	 * apply the d1 + d2 pass on the argument to the player current home and 
	 * also checks different conditions
	 * 
	 * @param dice
	 * @pre:  1 <= d1 <= 6 && 1 <= d2<= 12 && isCupOver == false 
	 */
	public void makeMove(int d1, int d2) {
		int index = indexNextToPlay();
		int curent_home = players[index].getCurrentSquare();
		int result = d1 + d2 + curent_home;
		if (isSpecialDice(d1, d2)) {
			result = totalSquares;
		} else if (isOnBird(result)) {
			result += BIRD_ADVANCE;
		} else if (isOnCliffOrFine(result) == FINE) {
			players[index].increaseFine(getFineQuant(result));
		} else if (isOnCliffOrFine(result) == CLIFF) {
			result = resolveCliffType(getCliffType(result), players[index], d1 + d2, result);
		}
		result=getBoundaries(result);
		players[index].setCurrentSquare(result);
		firstRound--;
		setNextToPlay();
		if (isOnLastSquare(result)) {
			gameOver(players[index]);
		}
	}
	
	/**
	 * 
	 * @param d1
	 * @param d2
	 * @return either is a special dice or not and if it is the first time the player 
	 * has played the current game
	 */
	private boolean isSpecialDice(int d1, int d2) {
		return (((d1 == 6 && d2 == 3) || (d1 == 3 && d2 == 6)) && (firstRound > 0));
	}
	
	/**
	 * 
	 * @param square
	 * @pre: 1 <= square <= totalSquares
	 * @return
	 */
	private boolean isOnLastSquare(int square) {
		return (square == totalSquares);
	}
	
	/**
	 * 
	 * @param P
	 * @pre: P != null && the current game must be over
	 */
	private void gameOver(Player P) {
		gamesPlayed++;
		if (playersEliminated - gamesPlayed < 0) {
			EliminatePlayer();
			if (isOnlyOnePLayer()) {
				setCupIsOver();
			} else {
				resetGame();
			}
		} else {
			resetGame();
		}
		P.increaseGamesWon(BY_ONE);
	}
	
	/**
	 * 
	 * @param square
	 * @return square with the correct boundaries
	 */
	private int getBoundaries(int square) {
		if (square < 1) {
			return 1;
		} else if (square > totalSquares) {
			return totalSquares;
		} else {
			return square;
		}
	}
	
	/**
	 * 
	 * @return the next player to roll the dices
	 * @pre: iscupOver() == false
	 */
	public String getNexttoPlay() {
		return getPlayerColorByIndex(indexNextToPlay());
	}
	
	/**
	 * 
	 * @param square
	 * @pre: 1 <= square <= totalSquares && cliffsAndFines[square-1][0] == CLIFF 
	 * @return the corresponding cliff type to the specific square
	 */
	private int getCliffType(int square) {
		return cliffsAndFines[square - 1][1];
	}
	
	/**
	 * 
	 * @param type
	 * @param P
	 * @param dice
	 * @param result
	 * @pre: 1 <= type <= 3 && P != null && 2<= dice <= 12 && isOnCliffOrFine(result) == CLIFF
	 * @return the square that the player will be according with the cliff type
	 */
	private int resolveCliffType(int type, Player P, int dice, int result) {
		if (type == CRAB_TYPE) {
			result = result - (2 * dice);
		} else if (type == DEATH_TYPE) {
			if (!hasDeathOccured) {
				P.setEliminated();
				hasDeathOccured = true;
				playersEliminated++;
				P.setEliminationOrder(playersEliminated);
				if (isOnlyOnePLayer()) {
					setCupIsOver();
					getPlayerNotEliminated().increaseGamesWon(BY_ONE);
				}
			}
		} else if (type == HELL_TYPE) {
			result = 1;
		}
		return result;
	}
	
	/**
	 * 
	 * @pre: isOnlyOnePLayer() == true
	 * @return the player who is not eliminated from the cup
	 */
	private Player getPlayerNotEliminated() {
		int i = 0;
		while (i < nPlayers && players[i].isEliminated()) {
			i++;
		}
		return players[i];
	}
	
	/**
	 * 
	 * @return either is just one player left in the cup or not
	 */
	private boolean isOnlyOnePLayer() {
		return (playersEliminated == nPlayers - 1);
	}

	/**
	 * @pre: isOnlyOnePlayer() == true
	 */
	private void setCupIsOver() {
		isCupOver = true;
		Player P = getPlayerNotEliminated();
		winnerPlayer = P.getColor();
	}

	/**
	 * this method serves for eliminate the player who is on the lowest square if it is 
	 * more than one is based on their time to play
	 */
	private void EliminatePlayer() {
		int lowestSquare = lowestSquare();
		int index = 0;
		for (int i = 0; i < nPlayers; i++) {
			if (players[i].getCurrentSquare() == lowestSquare && !players[i].isEliminated()){
				index = i;
			}
		}
		playersEliminated++;
		players[index].setEliminationOrder(playersEliminated);
		players[index].setEliminated();

	}

	/**
	 * 
	 * @return the lowest square a player is on
	 */
	private int lowestSquare() {
		int result = totalSquares;
		for (int i = 0; i < nPlayers; i++) {
			if (players[i].getCurrentSquare() < result && !players[i].isEliminated()) {
				result = players[i].getCurrentSquare();
			}
		}
		return result;
	}

	/**
	 * checks if the next to play player doesn't have fines and isn't eliminated 
	 * (if so call the method setNextToPlay())
	 * 
	 * @return the index of the next player that will roll the dices
	 */
	private int indexNextToPlay() {
		int i = nextPlayer - 1;
		if (!players[i].hasFine() && !players[i].isEliminated()) {
			return i;
		} else {
			setNextToPlay();
			return this.indexNextToPlay();
		}
	}

	/**
	 * set the next player to play and also checks if the player isn't eliminated 
	 * if so call setNextToPlay() and also checks if the player has fines or not(if so 
	 * decrease the amount of fines by one and call  setNextToPlay())
	 */
	public void setNextToPlay() {
		nextPlayer++;
		if (nextPlayer == nPlayers + 1) {
			nextPlayer = 1;
		}
		if (players[nextPlayer - 1].isEliminated()) {
			setNextToPlay();
		} else if (players[nextPlayer - 1].hasFine()) {
			players[nextPlayer - 1].decreaseFine(BY_ONE);
			setNextToPlay();
		}
	}

	/**
	 * @param square
	 * @pre: square!=null
	 * @return if the variable square pass in the argument is a bird
	 */
	private boolean isOnBird(int square) {
		return square >= 2 && square <= totalSquares - 1 && square % 9 == 0;
	}

	/**
	 * @param square
	 * @pre: square!=null
	 * @return a character depending if the square is a Cliff or a Fine (if not any
	 *         of them return 0)
	 */
	private int isOnCliffOrFine(int square) {
		if (square > 1 && square < totalSquares) {
			return cliffsAndFines[square - 1][0];
		} else {
			return 0;
		}

	}

	/**
	 * 
	 * @param square
	 * @pre: 1 <= square <= totalSquares && cliffsAndFines[square-1][0] == FINE 
	 * @return the corresponding fine amount to the specific square
	 */
	private int getFineQuant(int square) {
		return cliffsAndFines[square - 1][1];
	}

	public PlayerIterator sortedIterator() {
		Player[] aux = new Player[nPlayers];
		for (int i = 0; i < nPlayers; i++) {
			aux[i] = players[i];
		}
		sort(aux, nPlayers);
		return new PlayerIterator(aux, nPlayers);
	}

	public void sort(Player[] aux, int size) {
		for (int i = 0; i < size - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < size; j++) {
				if (aux[j].compareTo(aux[minIndex]) < 0) {
					minIndex = j;
				}
			}
			Player tmp = aux[i];
			aux[i] = aux[minIndex];
			aux[minIndex] = tmp;
		}
	}

	/**
	 * 
	 * @param squaresWithFine
	 * @param squaresWithCliff
	 * @pre:  squaresWithFine!=null && squaresWithCliff!=null
	 */
	private void createCliffsAndFines(int[][] squaresWithFine, int[][] squaresWithCliff) {
		int square;
		cliffsAndFines = new int[totalSquares][2];
		for (int i = 0; i < squaresWithFine.length; i++) {
			for (int j = 0; j < 2; j++) {
				square = squaresWithFine[i][0];
				cliffsAndFines[square - 1][0] = FINE;
				cliffsAndFines[square - 1][1] = squaresWithFine[i][1];
			}
		}
		for (int i = 0; i < squaresWithCliff.length; i++) {
			for (int j = 0; j < 2; j++) {
				square = squaresWithCliff[i][0];
				cliffsAndFines[square - 1][0] = CLIFF;
				cliffsAndFines[square - 1][1] = squaresWithCliff[i][1];
			}
		}
	}
	
}