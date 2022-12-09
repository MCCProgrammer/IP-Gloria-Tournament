/**
*@author Miguel Cacais do Carmo && Tiago Pinto Pereira
         *The main purpose of this class is to interact with the user, therefore,
 		 it reads and prints all the data.Also, this class just only interacts with the 
 		 GloriaGame class.
*/

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
	
	/**
	 * String variables that are associated with the commands
	 */
	private static final String OPTION_PLAYER = "player", OPTION_SQUARE = "square", 
								OPTION_STATUS = "status",OPTION_DICE = "dice", 
								OPTION_EXIT = "exit", OPTION_RANKING = "ranking";
	
	/**
	 *  String variables that are used for printing out messages
	 */
	private static final String INVALID_COMMAND = "Invalid command";
	private static final String CUP_OVER = "The cup is over";
	private static final String NOT_OVER = "The cup was not over yet...";
	private static final String NOEXISTENT_PLAYER = "Nonexistent player";
	private static final String INVALID_DICE = "Invalid dice";
	private static final String ROLL_DICE = " can roll the dice";
	private static final String NOT_ROLL_DICE = " cannot roll the dice";
	private static final String ELIMINATED = "Eliminated player";
	private static final String INPUT_TXT = "boards.txt";
	private static final String CRAB = "crab", DEATH = "death";
	
	/**
	 * Integer variables to associate with the cliff type
	 */
	private static final int CRAB_TYPE = 1, DEATH_TYPE = 2, HELL_TYPE = 3;

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner in = new Scanner(System.in);
		
		String colors = inputString(in);
		
		int nPlayers, nrTabuleiro;
		nPlayers = colors.length();
		nrTabuleiro = inputInt(in);
		
		Scanner input_file = new Scanner(new FileReader(INPUT_TXT));
		
		int[][] squaresWithFine, squaresWithCliff;
		int totalSquares;
		
		do {
			totalSquares = input_file.nextInt();
			input_file.nextLine();
			squaresWithFine = createFineMatrix(input_file);
			squaresWithCliff = createCliffMatrix(input_file);
			nrTabuleiro--;
		} while (nrTabuleiro > 0);
		
		input_file.close();

		GloriaGame game = new GloriaGame(totalSquares, nPlayers,
										 squaresWithFine, squaresWithCliff);

		for (int i = 0; i < nPlayers; i++) {
			game.addNewPlayer(getPlayersColors(colors, i), i);
		}

		String option;
		do {
			option = in.next();
			executeOption(in, option, game);

		} while (!option.equals(OPTION_EXIT));

		in.close();

	}
	
	/**
	 * 
	 * @param in
	 * @return
	 */
	private static int inputInt(Scanner in) {
		int n = in.nextInt();
		in.nextLine();
		return n;
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws FileNotFoundException
	 */
	private static String inputString(Scanner input) throws FileNotFoundException {
		String n = input.next();
		input.nextLine();
		return n;
	}

	/**
	 * divides the string that contains all the colors of the players in the cup
	 * 
	 * @param colors
	 * @param i: represents the index of the player
	 * @pre: colors!=null
	 * @return color
	 */
	private static String getPlayersColors(String colors, int i) {
		String color = new String(new char[] { colors.charAt(i) });
		return color;
	}

	/**
	 * create a matrix by the following input
	 * 
	 * @param input_file
	 * @return matrix with 2 rows and n columns
	 * @throws FileNotFoundException
	 */
	private static int[][] createFineMatrix(Scanner input_file) throws FileNotFoundException {
		int[][] matrix = new int[inputInt(input_file)][2];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < 2; j++) {
				matrix[i][j] = input_file.nextInt();
			}
			input_file.nextLine();
		}
		return matrix;
	}
	
	/**
	 * create a special matrix that in the second row transform the strings input with 
	 * the corresponding int
	 * 
	 * @param input_file
	 * @return matrix with 2 rows and n columns
	 * @throws FileNotFoundException
	 */
	private static int[][] createCliffMatrix(Scanner input_file) throws FileNotFoundException {
		int[][] matrix = new int[inputInt(input_file)][2];
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = input_file.nextInt();
			String type = inputString(input_file);
			if (type.equals(CRAB)) {
				matrix[i][1] = CRAB_TYPE;
			} else if (type.equals(DEATH)) {
				matrix[i][1] = DEATH_TYPE;
			} else {
				matrix[i][1] = HELL_TYPE;
			}
		}
		return matrix;
	}

	/**
	 * execute the variable option pass in the argument
	 * 
	 * @param in
	 * @param option
	 * @param game
	 * @pre: option!=null
	 */
	private static void executeOption(Scanner in, String option, GloriaGame game) {

		switch (option) {

		case OPTION_PLAYER: {
			executeOptionPlayer(game);
			break;
		}

		case OPTION_SQUARE: {
			executeOptionSquare(in, game);
			break;
		}

		case OPTION_STATUS: {
			executeOptionStatus(in, game);
			break;
		}
		case OPTION_RANKING: {
			executeOptionRanking(in, game);
			break;
		}

		case OPTION_DICE: {
			executeOptionDice(in, game);
			break;
		}

		case OPTION_EXIT: {
			executeOptionExit(game);
			break;
		}

		default:
			System.out.println(INVALID_COMMAND);
			in.nextLine();
		}
	}

	/**
	 * is called when the variable option in executeOption() is equals to "player"
	 * 
	 * @param game
	 */
	private static void executeOptionPlayer(GloriaGame game) {
		if (game.isCupOver()) {
			System.out.println(CUP_OVER);
		} else {
			System.out.println("Next to play: " + game.getNexttoPlay());
		}
	}

	/**
	 * is called when the variable option in executeOption() is equals to "square"
	 * 
	 * @param in
	 * @param game
	 */
	private static void executeOptionSquare(Scanner in, GloriaGame game) {
		String playerColor = in.nextLine().substring(1);

		if (!game.isCorValid(playerColor)) {
			System.out.println(NOEXISTENT_PLAYER);
		} else if (game.isEliminated(playerColor)) {
			System.out.println(ELIMINATED);
		} else {
			System.out.println(playerColor + " is on square " + 
			game.getCurrentSquare(playerColor));
		}
	}

	/**
	 * is called when the variable option in executeOption() is equals to "status"
	 * 
	 * @param in
	 * @param game
	 */
	private static void executeOptionStatus(Scanner in, GloriaGame game) {
		String playerColor = in.nextLine().substring(1);

		if (!game.isCorValid(playerColor)) {
			System.out.println(NOEXISTENT_PLAYER);
		} else if (game.isCupOver()) {
			System.out.println(CUP_OVER);
		} else if (game.isEliminated(playerColor)) {
			System.out.println(ELIMINATED);
		} else if (game.canRollDice(playerColor)) {
			System.out.println(playerColor + ROLL_DICE);
		} else {
			System.out.println(playerColor + NOT_ROLL_DICE);
		}
	}
	
	/**
	 * is called when the variable option in executeOption() is equals to "ranking"
	 * 
	 * @param in
	 * @param game
	 */
	private static void executeOptionRanking(Scanner in, GloriaGame game) {
		PlayerIterator it = game.sortedIterator();
		while (it.hasNext()) {
			Player P = it.next();
			if (!P.isEliminated()) {
				System.out.println(P.getColor() + ": " + P.getGamesWon() + 
				" games won; on square " + P.getCurrentSquare() + ".");
			} else {
				System.out.println(P.getColor() + ": " + P.getGamesWon() 
				+ " games won; eliminated.");
			}
		}

	}

	/**
	 * is called when the variable option in executeOption() is equals to "dice"
	 * 
	 * @param in
	 * @param game
	 */
	private static void executeOptionDice(Scanner in, GloriaGame game) {
		int d1 = in.nextInt();
		int d2 = inputInt(in);

		if ((d1 < 1 || d1 > 6) || (d2 < 1 || d2 > 6)) {
			System.out.println(INVALID_DICE);
		} else if (game.isCupOver()) {
			System.out.println(CUP_OVER);
		} else {
			game.makeMove(d1, d2);
		}
	}

	/**
	 * is called when the variable option in executeOption() is equals to "exit"
	 * 
	 * @param game
	 */
	private static void executeOptionExit(GloriaGame game) {
		if (game.isCupOver()) {
			System.out.println(game.getWinnerColor() + " won the cup!");
		} else {
			System.out.println(NOT_OVER);
		}
	}
	
}