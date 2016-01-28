package martinutils.swing;

import java.io.IOException;

/**
 * An output stream linked to a Swing {@link ConsoleFrame} error text area
 * @author martin
 */
public class TextAreaErrorStream extends TextAreaOutputStream {

	public TextAreaErrorStream(ConsoleFrame console) {
		super(console);
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char)b;
		console.appendError( Character.toString(c) );
	}
	
}