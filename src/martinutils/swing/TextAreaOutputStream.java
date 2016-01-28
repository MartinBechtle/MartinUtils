package martinutils.swing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream linked to a Swing {@link ConsoleFrame}
 * @author martin
 */
public class TextAreaOutputStream extends OutputStream {

	protected ByteArrayOutputStream baos;
	protected ConsoleFrame console;
	
	public TextAreaOutputStream(ConsoleFrame console) {
		this.console = console;
		this.baos = new ByteArrayOutputStream();
	}
	
	// we cannot simply write the integers to the console as chars, as they could be part of a multi-byte unicode character
	// so we need to use the ByteArrayOutputStream as buffer and regularly flush it to the console
	@Override
	public void write(int b) throws IOException {

		char c = (char)b;
		baos.write(b);
		if (c == '\n') { // a good idea would be to flush each time we have a new line
			_flush();
		}
		// then we also override the flush method so that we get the last bytes in the console
	}
	
	@Override
	public void flush() throws IOException {
		 _flush();
	}
	
	protected void _flush() throws IOException {
		
		if (baos.size() > 0) {
			appendString(baos.toString());
		}
		baos.close();
		baos = new ByteArrayOutputStream();
	}
	
	protected void appendString(String str) {
		console.append(str);
	}
	
	public void close() throws IOException {
		flush();
	}
	
}