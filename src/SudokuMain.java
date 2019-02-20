
public class SudokuMain {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Sudoku s = new Sudoku();
//		s.randomFill();
//		s.removeRandom();
//		s.printBoard();
//		if (s.isGeneric()) {
//			System.out.println("Såja" +"\n");
//		}
		s.printBoard();
		SudokuGUI gui = new SudokuGUI(s);
	}
}
