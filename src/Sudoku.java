import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Sudoku implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[][] sudokuMatrix;

	/**
	 * Constructor for the Sudoku matrix. Creates a matrix that is 9x9 with the
	 * staring position in the upper left corner
	 */

	public Sudoku() {
		sudokuMatrix = new int[9][9];
	}

	/**
	 * Returns the number at the specified spot in the matrix.
	 *
	 * @param y
	 *            the vertical row of the current number
	 * @param x
	 *            the horizontal row of the current number
	 * @return the number at the specified index
	 */

	public int getNumber(int y, int x) {
		return sudokuMatrix[y][x];
	}

	/**
	 * Sets the number at the specified spot in the matrix
	 *
	 * @param y
	 *            the row number
	 * @param x
	 *            the column coordinate
	 * @param val
	 *            the number you want to insert.
	 **/
	public void setNumber(int y, int x, int val) {
		sudokuMatrix[y][x] = val;
	}

	public void clearMatrix() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				sudokuMatrix[i][j] = 0;
			}
		}
	}

	public boolean legalSudoku() {
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if (sudokuMatrix[y][x] != 0
						&& !conditions(y, x, sudokuMatrix[y][x])) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isItDone() {
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if (sudokuMatrix[y][x] == 0
						|| !conditions(y, x, sudokuMatrix[y][x])) {
					return false;
				}
			}
		}
		return true;
	}

	public String findError() {
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				if (!conditions(y, x, sudokuMatrix[y][x])
						&& sudokuMatrix[y][x] != 0) {
					String s = "[" + Integer.toString(y) + "]" + "["
							+ Integer.toString(x) + "]";
					return s;
				}
			}
		}
		return "No fault found";

	}

	public int[][] toMatrix() {

		return sudokuMatrix;

	}

	public ArrayList<Integer> toRandomArray() {
		ArrayList<Integer> sorted = new ArrayList<Integer>();
		ArrayList<Integer> random = new ArrayList<Integer>();
		Random rand = new Random();
		for (int y = 0; y < 9; y++) {
			for (int x = 1; x < 10; x++) {
				sorted.add(x);
			}
		}
		for (int i = 0; i < 81; i++) {
			int index = rand.nextInt(sorted.size());
			random.add(sorted.get(index));
			sorted.remove(index);
		}
		return random;

	}

	public void randomFill() {
		Random rand = new Random();
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				list.add(new int[] { y, x });
			}
		}
		for (int i = 0; i < 30; i++) {
			int index = rand.nextInt(list.size());
			int y = list.get(index)[0];
			int x = list.get(index)[1];
			int val = rand.nextInt(9) + 1;
			if (conditions(y, x, val)) {
				setNumber(y, x, val);
				list.remove(index);
			}
		}
		solveSudoku();
	}

	public void removeRandom() {
		Random rand = new Random();
		ArrayList<int[]> list = new ArrayList<int[]>();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				list.add(new int[] { y, x });
			}
		}
		for (int i = 0; i < 30; i++) {
			int index = rand.nextInt(list.size());
			int y = list.get(index)[0];
			int x = list.get(index)[1];
			if (sudokuMatrix[y][x] != 0) {
				sudokuMatrix[y][x] = 0;
				list.remove(index);
			}
		}
	}

	public boolean isGeneric() {
		Sudoku temp = new Sudoku();
		Sudoku original = new Sudoku();
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				temp.setNumber(y, x, getNumber(y, x));
				original.setNumber(y, x, getNumber(y, x));
			}
		}
		original.solveSudoku();
		temp.solveSudokuBackwards();
		int[][] normal = original.toMatrix();
		int[][] back = temp.toMatrix();

		if (Arrays.deepEquals(back, normal)) {
			return true;
		}

		return false;
	}

	public boolean solveSudoku() {
		if (solve(0, 0)) {
			System.out.println("Snyggt");
			return true;
		} else {
			System.out.println("Nepp");
			return false;
		}
	}

	private boolean solve(int y, int x) {

		/** Makes sure we don't leave the matrix */
		if (x >= 9) {
			x = 0;
			y++;

		}

		if (y >= 9) {
			return true;
		}
		/** Searches for empty places in the matrix */
		if (sudokuMatrix[y][x] != 0) {
			if (conditions(y, x, sudokuMatrix[y][x])) {
				return solve(y, x + 1);
			}
			return false;

		} else {

			/** Tries to insert first possible value between 1 to 9 */
			for (int i = 1; i <= 9; i++) {

				/**
				 * Checks all criteria for location [y][x] and inserts i if all
				 * conditions returns true
				 */
				if (conditions(y, x, i)) {
					sudokuMatrix[y][x] = i;
					/** Recursive call for next element */
					if (solve(y, x + 1)) {
						return true;

					}
				}
			}
			sudokuMatrix[y][x] = 0;
		}
		return false;

	}

	/**
	 * Tries to insert the value for the location [y][x] in the matrix Checks
	 * for duplicates vertically, horizontally and for each 3x3 mini matrix
	 *
	 * @param y
	 *            Vertical position
	 * @param x
	 *            Horizontal position
	 * @param value
	 *            The number between 1 and 9 we would like to insert
	 * @return True if all conditions are met for the value
	 */
	private boolean conditions(int y, int x, int value) {

		/**
		 * Checks vertically for duplicates to "value" (y param) Returns false
		 * if duplicate is found
		 */
		for (int yTemp = 0; yTemp < 9; yTemp++) {
			if (yTemp != y) {
				if (sudokuMatrix[yTemp][x] == value) {
					return false;
				}
			}
		}

		/**
		 * Checks horizontally for duplicates to "value" (x param) Returns false
		 * if duplicate is found
		 */
		for (int xTemp = 0; xTemp < 9; xTemp++) {
			if (xTemp != x) {
				if (sudokuMatrix[y][xTemp] == value) {
					return false;
				}
			}
		}

		/**
		 * Checks the 3x3 box for duplicates to "value". Returns false if
		 * duplicate is found
		 *
		 * yBox and xBox reperesents the top left corner for the 3x3 box of the
		 * y and x param
		 */
		int yBox = (y / 3) * 3;
		int xBox = (x / 3) * 3;

		/** Loops through the 3x3 matrix */

		for (int i = yBox; i < (yBox + 3); i++) {
			for (int j = xBox; j < (xBox + 3); j++) {

				/**
				 * Looks for a dublicate that isn't the same spot, that of the y
				 * and x param
				 */
				if (sudokuMatrix[i][j] == value && !(i == y && j == x)) {
					return false;
				}
			}
		}
		/** If no duplicates were found */
		return true;
	}

	/**
	 * Prints the matrix. 
	 * Only used for testing.
	 **/

	public void printBoard() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.print(sudokuMatrix[i][j] + " ");
				if ((j + 1) % 3 == 0 && j != 8) {
					System.out.print("| ");
				}
			}
			if ((i + 1) % 3 == 0 && i != 8) {
				System.out.println();
				System.out.print("- - -" + "   " + "- - -" + "   " + "- - -");
			}
			System.out.println();
		}
	}

	public boolean solveSudokuBackwards() {
		if (reverseSolve(0, 0)) {
			// System.out.println("Fan vad du äger!" + "\n");
			// printBoard();
			return true;
		} else {
			// System.out.println("Feck" + "\n");
			// printBoard();
			return false;
		}
	}

	private boolean reverseSolve(int y, int x) {

		/** Makes sure we don't leave the matrix */
		if (x >= 9) {
			x = 0;
			y++;

		}

		if (y >= 9) {
			return true;
		}
		/** Searches for empty places in the matrix */
		if (sudokuMatrix[y][x] != 0) {
			if (conditions(y, x, sudokuMatrix[y][x])) {
				return reverseSolve(y, x + 1);
			}
			return false;

		} else {

			/** Tries to insert first possible value between 1 to 9 */
			for (int i = 9; i >= 0; i--) {

				/**
				 * Checks all criteria for location [y][x] and inserts i if all
				 * conditions returns true
				 */
				if (conditions(y, x, i)) {
					sudokuMatrix[y][x] = i;
					// printBoard();
					// System.out.println("\n");
					/** Recursive call for next element */
					if (reverseSolve(y, x + 1)) {
						return true;

					}
				}
			}
			sudokuMatrix[y][x] = 0;
			// printBoard();
			// System.out.println("\n");
		}
		return false;

	}
}