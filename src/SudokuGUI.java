import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SudokuGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private SudokuBoard[][] GUIMatrix;
	@SuppressWarnings("unused")
	private Sudoku sudoku;
	private int hints;

	public SudokuGUI(Sudoku sudoku) {
		GUIMatrix = new SudokuBoard[9][9];
		this.sudoku = sudoku;
		hints = 0;

		JFrame frame = new JFrame("Sudoku Solver");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel actions = new JPanel();
		frame.add(actions, BorderLayout.NORTH);
		actions.add(new LoadButton());
		actions.add(new RandomButton());
		actions.add(new SaveButton());

		JPanel grid = new JPanel();
		frame.add(grid, BorderLayout.CENTER);
		grid.setLayout(new GridLayout(9, 9));
		JPanel buttons = new JPanel();
		frame.add(buttons, BorderLayout.SOUTH);
		buttons.add(new SolveButton());
		buttons.add(new ClearButton());
		buttons.add(new CheckSoulutionButton());
		buttons.add(new HintButton());

		for (int x = 0; x < 9; x++) {
			Color c = new Color(100, 200, 250);
			for (int y = 0; y < 9; y++) {
				SudokuBoard sq = new SudokuBoard();
				if (x < 3 || x > 5) {
					if (y < 3 || y > 5)
						sq.color(c);
				} else if (y > 2 && y < 6) {
					sq.color(c);
				}
				GUIMatrix[x][y] = sq;
				grid.add(sq);
			}
		}

		frame.setSize(245, 330);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

	}

	class SolveButton extends JButton implements ActionListener {

		/**
		 * Creates a new Solve Button
		 */
		public SolveButton() {
			super("Solve");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent e) {
			Sudoku s = new Sudoku();
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (GUIMatrix[y][x].getText().equals("")) {
						s.setNumber(y, x, 0);
					} else {
						int val = Integer.parseInt(GUIMatrix[y][x].getText());
						s.setNumber(y, x, val);
					}
				}
			}

			if (s.solveSudoku()) {
				for (int y = 0; y < 9; y++) {
					for (int x = 0; x < 9; x++) {
						GUIMatrix[y][x].setText(Integer.toString(s.getNumber(y,
								x)));
					}
				}
				JOptionPane.showMessageDialog(null,
						"Here you go, wasn't so hard, was it?",
						"Help to the rescue", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fault at" + s.findError(),
						"No wonder you didn't solve it",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ClearButton extends JButton implements ActionListener {

		/**
		 * Creates a new Solve Button
		 */
		public ClearButton() {
			super("Clear");
			addActionListener(this);

		}

		public void actionPerformed(ActionEvent e) {
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					GUIMatrix[y][x].setText("");
				}
			}
		}
	}

	class CheckSoulutionButton extends JButton implements ActionListener {

		public CheckSoulutionButton() {
			super("Check");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			Sudoku s = new Sudoku();
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (GUIMatrix[y][x].getText().equals("")) {
						s.setNumber(y, x, 0);
					} else {
						int val = Integer.parseInt(GUIMatrix[y][x].getText());
						s.setNumber(y, x, val);
					}
				}
			}
			if (s.isItDone()) {
				if (hints > 2) {
					JOptionPane.showMessageDialog(null, "You made it" + ".... "
							+ "\n" + "But you used " + hints + " hints",
							"Congratulations", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "You made it!",
							"Congratulations", JOptionPane.INFORMATION_MESSAGE);
				}
			} else if (s.legalSudoku()) {
				JOptionPane.showMessageDialog(null, "So far so good",
						"Checking Sudoku", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fault at" + s.findError(),
						"You messed up", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	class HintButton extends JButton implements ActionListener {

		public HintButton() {
			super("Hint");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			hints++;
			Random rand = new Random();
			Sudoku s = new Sudoku();
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (GUIMatrix[y][x].getText().equals("")) {
						s.setNumber(y, x, 0);
					} else {
						int val = Integer.parseInt(GUIMatrix[y][x].getText());
						s.setNumber(y, x, val);
					}
				}
			}
			if (!s.isItDone()) {
				if (s.solveSudoku()) {
					boolean found = false;
					while (!found) {
						int ypos = rand.nextInt(9);
						int xpos = rand.nextInt(9);
						if (GUIMatrix[ypos][xpos].getText().equals("")) {
							GUIMatrix[ypos][xpos].setText(Integer.toString(s
									.getNumber(ypos, xpos)));
							found = true;
						}
					}
				} else {
					JOptionPane.showMessageDialog(null,
							"Fault at" + s.findError(), "Unsolvable",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Take a seat, the sudoku is already solved",
						"Easy now", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	class LoadButton extends JButton implements ActionListener {

		public LoadButton() {
			super("Load");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg) {
			Sudoku s = new Sudoku();
			String fileName = JOptionPane.showInputDialog("Name of sudoku");
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				String[] n = { "Empty board", "Last saved", "Default sudoku" };
				int res = JOptionPane.showOptionDialog(null,
						"Couldn't find sudoku, pick one of theese?",
						"Sudoku not found", 2, 3, null, n, 0);
				try {
					String string = null;
					if (res == 2) {
						string = "Default";
					} else if (res == 1) {
						string = "Progress";
					} else {
						string = "Zero";
					}
					scanner = new Scanner(new File(string));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
			for (int j = 0; j < 9; j++) {
				String line = scanner.nextLine();
				Scanner lineScanner = new Scanner(line);
				for (int i = 0; i < 9; i++) {
					int nbr = lineScanner.nextInt();
					s.setNumber(j, i, nbr);
				}
			}
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (s.getNumber(y, x) != 0) {
						GUIMatrix[y][x].setText(Integer.toString(s.getNumber(y,
								x)));
					} else {
						GUIMatrix[y][x].setText("");
					}
				}
			}
		}
	}

	class SaveButton extends JButton implements ActionListener {

		public SaveButton() {
			super("Save and quit");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg) {
			Sudoku s = new Sudoku();
			for (int y = 0; y < 9; y++) {
				for (int x = 0; x < 9; x++) {
					if (GUIMatrix[y][x].getText().equals("")) {
						s.setNumber(y, x, 0);
					} else {
						int val = Integer.parseInt(GUIMatrix[y][x].getText());
						s.setNumber(y, x, val);
					}
				}
			}
			try {
				File temp = new File("Progress");
				temp.createNewFile();
				FileWriter writer = new FileWriter(temp);
				for (int y = 0; y < 9; y++) {
					for (int x = 0; x < 9; x++) {
						if (y > 0 && x == 0) {
							writer.write("\n");
						}
						writer.write(Integer.toString(s.getNumber(y, x)) + " ");
					}
				}
				writer.flush();
				writer.close();
				System.exit(1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class RandomButton extends JButton implements ActionListener {

		public RandomButton() {
			super("Randomize");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg) {
			Sudoku s = new Sudoku();
			s.randomFill();
			s.removeRandom();
			if (s.isGeneric()) {
				for (int y = 0; y < 9; y++) {
					for (int x = 0; x < 9; x++) {
						if (s.getNumber(y, x) != 0) {
							GUIMatrix[y][x].setText(Integer.toString(s
									.getNumber(y, x)));
						} else {
							GUIMatrix[y][x].setText("");
						}
					}
				}
			} else {
				actionPerformed(arg);
			}

		}
	}
}