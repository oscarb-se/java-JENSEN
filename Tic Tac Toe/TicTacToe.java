import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

public class TicTacToe { // *1
	public final int rows = 5, cols = rows;
	public final int cellSize = 3;
	public final String[][] board = new String[rows][cols];
	public final String empty = "   ";

	public boolean quit = false;

	public Player currentPlayer;
	public Player nextPlayer;
	public Player p1;
	public Player p2;

	public char opponent = '\0'; // (char) "0" (string)

	public Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) {
		new TicTacToe(); // create a new instance of *1, will call *1 constructor
	}

	public TicTacToe() { // constructor of *1
		String player1;
		String player2;
		String gameType = "";
		

		switch (chooseOpponent()) {

			case 'p' : player1 = askName(1);
					   player2 = askName(2);
					   break;

			case 'c' : player1 = "You";
					   player2 = "Computer";
					   break;

			default  : System.out.println("Invalid input");	
					   return;
		}
		
		this.p1 = new Player(player1, " X ", 1, 0);
		this.p2 = new Player(player2, " O ", 2, 0); 

		while (!quit) {
			initGame();
			showBoard();
			placeMark();			
			showResults();
			checkQuit();
		}
	}

	public char chooseOpponent() {
		
		boolean  flag = false;

		while (!flag) {
			System.out.println("Play against Computer -> Enter c");
			System.out.println("Play against Player -> Enter p");
			opponent = keyboard.next().charAt(0);			
			opponent = Character.toLowerCase(opponent);
			
			if (opponent == 'c') {
				flag = true;
				
			} else if (opponent == 'p') {
				flag = true;	

			} else {
				System.out.println("Invalid input");
			}
		}
		return opponent;
	}

	public String askName(int playerNumber) {
		if (opponent == 'p') {
			System.out.print("Player" + playerNumber + "'s name: ");			
		}

		return keyboard.next();
	}

	public void initGame() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				board[row][col] = empty;
			}
		}
	}

	public void showBoard() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				System.out.print(board[row][col]);

				if (col != cols - 1) {
					System.out.print("|");
				}
			}

			System.out.println();

			if (row != rows - 1) {
				for (int col = 0; col < cols * (cellSize + 1) - 1; col++) {
					System.out.print("-");
				}
			}

			System.out.println();
		}
	}

	public void placeMark() {
		boolean input = false;
		boolean gameEnd = false;
		currentPlayer = p1;
		int moveCounter = 0;

		int row = 0;
		int col = 0;

		while (!gameEnd) {
			do {
				if (opponent == 'p') {
					System.out.print(currentPlayer.name + "'s turn. Enter your indices. (Row: 1-" + rows + " / Colum: 1-" + rows + "):\n");

					row = keyboard.nextInt() - 1;
					col = keyboard.nextInt() - 1;
				
				} else if (currentPlayer == p1) {
					System.out.print("Your turn. Enter your indices. (Row: 1-" + rows + " / Colum: 1-" + rows + "):\n");

					row = keyboard.nextInt() - 1;
					col = keyboard.nextInt() - 1;

				} else {
					boolean isValid = false;

					while (!isValid) {						
						Random r = new Random();
						int i1 = r.nextInt(rows);
						int i2 = r.nextInt(cols);
						
						if (board[i1][i2] == empty) {					
							row = i1;
							col = i2;
							isValid = true;
							System.out.print(currentPlayer.name + "'s turn.\n");
						}
					}
				}

				if (row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] == empty) {
					board[row][col] = currentPlayer.piece;

					if (currentPlayer == p1) {
						nextPlayer = p2;
					} else {
						nextPlayer = p1;
					}

					showBoard();

					input = true;
					++moveCounter;

					if (checkWinner()) {
						System.out.println(currentPlayer.name + " won!");
						gameEnd = true;
						currentPlayer.wins++;
					}

					if (moveCounter == (rows * cols) && !checkWinner()) {
						System.out.println("Draw!");
						gameEnd = true;
					}

					currentPlayer = nextPlayer;
				} else {
					System.out.println("Invalid input. Enter your indices. (1-3 / 1-3): ");
				}
			} while (!input);
		}
	}

	public boolean checkWinner() {
		return checkHorizontal() || checkVertical() || checkDiagonal();
	}

	public boolean checkHorizontal() {
		for (int row = 0; row < rows; row++) {
			String piece = board[row][0];

			if (piece == empty) {
				continue;
			}

			for (int col = 0; col < cols; col++) {
				if (board[row][col] != piece) {
					break;
				}

				if (col == cols - 1)
					return true;
			}
		}

		return false;
	}

	public boolean checkVertical() {
		for (int col = 0; col < cols; col++) {
			String piece = board[0][col];

			if (piece == empty) {
				continue;
			}

			for (int row = 0; row < rows; row++) {

				if (board[row][col] != piece) {
					break;
				}

				if (row == rows - 1) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean checkDiagonal() {
		// from upleft to downright
		for (int row = 0; row < rows; row++) {
			String piece = board[0][0];

			if (piece == empty) {
				continue;
			}

			if (board[row][row] != piece) {
				break;
			}

			if (row == rows - 1) {
				return true;
			}
		}

		// from upright to downleft
		for (int row = 0, col = cols-1; row < rows; row++, col--) {
			String piece = board[0][cols - 1];

			if (piece == empty) {
				break;
			}

			if (board[row][col] != piece) {
				break;
			}

			if (row + col != cols - 1) {
				break;
			}

			if (row == rows - 1) { // row = currentRow etc
				return true; 
			}
		}

		return false;
	}

	public void showResults() {
		HashMap<Integer, String> wins = new HashMap<Integer, String>();
		wins.put(0, "wins");
		wins.put(1, "win");
		wins.put(2, "wins");

		System.out.println("**** Results ****");
		System.out.println(p1.name + " : " + p1.wins + " " + wins.get(Math.min(p1.wins, 2)));
		System.out.println(p2.name + " : " + p2.wins + " " + wins.get(Math.min(p2.wins, 2)));
	}

	public void checkQuit() {
		boolean  flag = false;

		do {
			String urge = "Do you want to quit? (y/n): ";
			System.out.println(urge);
			char userInput = keyboard.next().charAt(0);
			userInput = Character.toLowerCase(userInput);

			if (userInput == 'y') {
			System.out.println("Bye bye!");
			quit = true;
			System.exit(0);

			} else if (userInput == 'n') {
				System.out.println("New game!");
				flag = true;
				quit = false;

			} else {
				System.out.println("Invalid input!");
			}
		} while (!flag);
	}
}