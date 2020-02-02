import java.util.Random;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);

		while(true) {
			System.out.println("Welcome to connect4 \nSelect game type");
			System.out.println("Enter 1 for Player vs. Player");
			System.out.println("Enter 2 to Exit");

			char [][]connect4 = new char[7][14];
			connect4 = initializeBoard();

			int choice = input.nextInt();

			switch(choice) {

			case 1:
				//Player vs. Player
				System.out.println("Starting game");
				twoPlayers(connect4, input);
				break;

			case 2:
				return;

			default:
				System.out.println("Please enter a valid value either 1, 2 or 3!");
				break;
			}

			printCurrentGameState(connect4);
		}


	}

	public static void twoPlayers(char[][] connect4, Scanner input) {
		
		printCurrentGameState(connect4);
		int move;

		//is the move valid
		boolean isValid = false;

		//has the game ended
		boolean isEnded = false;

		//check if there is no available moves and this after reaching 42 moves
		//then there is no winner and it is a withdraw
		int countMoves = 0;

		//as long as the game hasn't ended
		while(!isEnded || countMoves!=42) {

			isValid = false;
			//as long as the player 1 move is valid
			while(!isValid) {
				System.out.print("Player 1: ");
				move = input.nextInt();
				System.out.println();

				//if it is a valid move
				if(validateMove(connect4, move)) {

					isValid = true;
					connect4 = placeMove(connect4, move, 'x', 2);
					//					isEnded = checkIsWinner(connect4, move);

					//end game because we got a winner
					//					if(isEnded) {
					//						return;
					//					}

					countMoves++;
					printCurrentGameState(connect4);
				}
				else {
					System.out.println("Invalid place, play again");
				}

			}

			isValid = false;
			//as long as the player 2 move is valid
			while(!isValid) {
				System.out.print("Player 2: ");
				move = input.nextInt();
				System.out.println();

				//if it is a valid move
				if(validateMove(connect4, move)) {

					isValid = true;
					connect4 = placeMove(connect4, move, 'o', 2);
					//					isEnded = checkIsWinner(connect4, move);

					//end game because we got a winner
					//					if(isEnded) {
					//						return;
					//					}

					countMoves++;
					printCurrentGameState(connect4);
				}
				else {
					System.out.println("Invalid place, play again");
				}
			}
		}

		if(countMoves==42) {
			System.out.println("Withdraw");
			return;
		}


	}

	public static char[][] placeMove(char[][] connect4, int move, char playerMark, int pcOrPlayers) {

		//get where the mark is added
		int rowIndex = -1;
		int colIndex = 2*(move-1);

		for(int r=1; r<connect4.length; ++r) {

			//if this is the first move to be played in this 
			//column then place it at the bottom
			if(connect4[r][colIndex]==' ' && r==6) {
				connect4[r][colIndex] = playerMark;
				rowIndex = r;
				break;

				//if it is not an empty block then place the mark 
				//on the empty block above it
			}else if(connect4[r][colIndex]!=' ') {
				connect4[r-1][colIndex] = playerMark;
				rowIndex = r-1;
				break;	
			} 
		}

		boolean isEnded = checkIsWinner(connect4, playerMark, rowIndex, colIndex);
		if(isEnded) {
			// #2 denotes to game version 2 where player vs player
			if(pcOrPlayers==2) {
				if(playerMark=='x') {
					printCurrentGameState(connect4);
					System.out.println("Player 1 is the Winner");
					//stop code instead of doing multiple returns
					System.exit(1);
				}
				else {
					printCurrentGameState(connect4);
					System.out.println("Player 2 is the Winner");
					 System.exit(1);
				}
				//denotes to game version 1 where player vs computer
			}else {
				if(playerMark=='x') {
					printCurrentGameState(connect4);
					System.out.println("Computer is the Winner");
					//stop code instead of doing multiple returns
					System.exit(1);
				}
				else {
					printCurrentGameState(connect4);
					System.out.println("Player is the Winner");
					 System.exit(1);
				}	
			}
		}

		return connect4;
	}

	public static boolean checkIsWinner(char[][] connect4, char playerMark, int rowIndex, int colIndex) {

		boolean isVertical = checkVertical(connect4, playerMark, rowIndex, colIndex);
		boolean isHorizontal = checkHorizontal(connect4, playerMark, rowIndex, colIndex);
		boolean isRightDiagonal = checkRightDiagonal(connect4, playerMark, rowIndex, colIndex);
		boolean isLeftDiagonal = checkLeftDiagonal(connect4, playerMark, rowIndex, colIndex);

		if(isVertical || isHorizontal || isRightDiagonal || isLeftDiagonal)
			return true;
		return false;
	}

	public static boolean checkLeftDiagonal(char[][] connect4, char playerMark, int rowIndex, int colIndex) {

		//find 4 moves that are next to each other
		int count = 0;

		//calculate number of steps to move across to reach the starting point
		int stepsDown = (6-rowIndex)<((12-colIndex)/2)?6-rowIndex:(12-colIndex)/2;

		//where to start because it is not a squared matrix
		int startingColPoint = colIndex + (stepsDown*2);
		int startingRowPoint = rowIndex + stepsDown;

		//calculate number of steps to move across to reach the starting point
		int stepsUp = (rowIndex-1)<(colIndex/2)?rowIndex-1:colIndex/2;

		//where to stop because it is not a squared matrix
		int endingColPoint = colIndex - (stepsUp*2);
		int endingRowPoint = rowIndex - stepsUp;
		
		//System.out.printf("starting Point:  (%d, %d),  ending Point: (%d, %d)\n", startingRowPoint, startingColPoint, endingRowPoint, endingColPoint);

		for(int r=startingRowPoint, c=startingColPoint; r>=endingRowPoint && c>=endingColPoint; --r, c-=2) {

			if(count==4) {
				return true;
			}else if(connect4[r][c]==playerMark) {
				count++;
				//System.out.printf("count: %d %s at (%d, %d)\n", count, playerMark, r, c);
				if(count==4) {
					return true;
				}
			}else if(connect4[r][c]!=playerMark) {
				count=0;
			}
		}

		return false;
	}

	public static boolean checkRightDiagonal(char[][] connect4, char playerMark, int rowIndex, int colIndex) {

		//find 4 moves that are next to each other
		int count = 0;

		//calculate number of steps to move across to reach the starting point
		int stepsDown = (6-rowIndex)<(colIndex/2)?6-rowIndex:colIndex/2;

		//where to start because it is not a squared matrix
		int startingColPoint = colIndex - (stepsDown*2);
		int startingRowPoint = rowIndex + stepsDown;

		//calculate number of steps to move across to reach the starting point
		int stepsUp = (rowIndex-1)<((12-colIndex)/2)?rowIndex-1:(12-colIndex)/2;

		//where to stop because it is not a squared matrix
		int endingColPoint = colIndex + (stepsUp*2);
		int endingRowPoint = rowIndex - stepsUp;

		for(int r=startingRowPoint, c=startingColPoint; r>=endingRowPoint && c<=endingColPoint; --r, c+=2) {

			if(count==4) {
				return true;
			}else if(connect4[r][c]==playerMark) {
				count++;
			}else if(connect4[r][c]!=playerMark) {
				count=0;
			}
		}

		return false;
	}

	public static boolean checkHorizontal(char[][] connect4, char playerMark, int rowIndex, int colIndex) {

		int count = 0;

		for(int c=0; c<13; c+=2) {

			if(count==4) {
				return true;
			}else if(connect4[rowIndex][c]==playerMark) {
				count++;
			}else if(connect4[rowIndex][c]!=playerMark) {
				count=0;
			}
		}

		return false;
	}

	public static boolean checkVertical(char[][] connect4, char playerMark, int rowIndex, int colIndex) {
		//I accessed this function because he had already made a move 
		int count = 1;

		for(int r=rowIndex; r<connect4.length; ++r) {
			if(count==4) {
				return true;
			} else if(connect4[r][colIndex]==playerMark) {
				count++;
			}else if(connect4[r][colIndex]!=playerMark) {
				return false;
			}
		}

		return false;
	}

	public static boolean validateMove(char[][] connect4, int move) {

		switch(move) {

		//check if top most element in column is not occupied
		//if not occupied then it is a valid move and return true else false
		case 1:
			if(connect4[1][0]==' ')
				return true;
			return false;

		case 2:
			if(connect4[1][2]==' ')
				return true;
			return false;

		case 3:
			if(connect4[1][4]==' ')
				return true;
			return false;

		case 4:
			if(connect4[1][6]==' ')
				return true;
			return false;

		case 5:
			if(connect4[1][8]==' ')
				return true;
			return false;

		case 6:
			if(connect4[1][10]==' ')
				return true;
			return false;

		case 7:
			if(connect4[1][12]==' ')
				return true;
			return false;

		default:
			return false;
		}
	}

	public static void printCurrentGameState(char[][] connect4) {
		for(int r=0; r<connect4.length; ++r) {
			for(int c=0; c<connect4[r].length; ++c) 
				System.out.printf(" %s ", connect4[r][c]);
			System.out.println();
		}

	}

	public static char[][] initializeBoard() {
		char [][]connect4 = new char[7][14];
		int columnCounter = 1;

		for(int r=0; r<connect4.length; ++r) {
			for(int c=0; c<connect4[r].length; ++c) {

				if(r==0 && c%2==0) {
					connect4[r][c] = Integer.toString(columnCounter).charAt(0);;
					columnCounter++;
				}else if(c%2!=0) {
					connect4[r][c] = '|';
				}else {
					connect4[r][c] = ' ';
				}

			}
		}
		return connect4;
	}

}
