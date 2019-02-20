
import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;

public class SudokuBoard extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a text field to display only one character.
	 */
	public SudokuBoard() {
		super("");
		((AbstractDocument) this.getDocument()).setDocumentFilter(new OneDigitFIlter());
	}

	private class OneDigitFIlter extends DocumentFilter {	   	   
		OneDigitFIlter() {	    
			super();	    
		} 	   

		public void insertString(FilterBypass fb, int offset, String  str, AttributeSet attr) throws BadLocationException {	    
			if ((fb.getDocument().getLength() + str.length()) > 1) {
				return;
			}
			if (! str.isEmpty() && ! Character.isDigit(str.charAt(0))) {
				return;
			}
			fb.insertString(offset, str, attr);	         
		}
		
		public void replace(FilterBypass fb, int offset, int length, String  str, AttributeSet attr) throws BadLocationException {	    
			if ((fb.getDocument().getLength() + str.length() - length) > 1) {
				return;
			}
			if (! str.isEmpty() && ! Character.isDigit(str.charAt(0))) {
				return;
			}
			fb.replace(offset, length, str, attr);	         
		}
	}

	public void color(Color c) {
		setBackground(c);
	}
}
