package martinutils.swing;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream linked to a Swing {@link ConsoleFrame}
 * @author martin
 */
public class TextAreaOutputStream extends OutputStream {

	ConsoleFrame console;
	
	public TextAreaOutputStream(ConsoleFrame console) {
		this.console = console;
	}
	
	@Override
	public void write(int b) throws IOException {
		char c = (char)b;
		console.append( Character.toString(c) );
	}
	
}