package martinutils.swing;


/**
 * An output stream linked to a Swing {@link ConsoleFrame} error text area
 * @author martin
 */
public class TextAreaErrorStream extends TextAreaOutputStream {

	public TextAreaErrorStream(ConsoleFrame console) {
		super(console);
	}
	
	@Override
	protected void appendString(String str) {
		console.appendError(str);
	}
	
}