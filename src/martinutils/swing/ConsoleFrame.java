package martinutils.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * A very useful info/error logging console for Swing applications.
 * @author martin, vash
 */
public class ConsoleFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private GridLayout layout;
	private JTextArea consoleTextArea;	
	private JTextArea errorConsoleTextArea;
	private JScrollPane consoleScrollPane;
	private JScrollPane errorScrollPane;
	
	/**
	 * Frame with two text areas. Default size frame (800px width, 600px height)
	 */
	public ConsoleFrame(JFrame parent) {
		this(parent, 800, 600);
	}
	
	/**
	 * Frame with two text areas: one for output and one for error
	 * @param parent
	 * @param width
	 * @param height
	 */
	public ConsoleFrame(final JFrame parent, int width, int height) {
		
		// Build console for output
		this.setTitle("Console");
		this.setSize(width, height);
		this.setVisible(false);
		
		// When this window is closed, the parent window will be brought on front and focused
		addWindowListener(new WindowAdapter() {
	        public void windowClosed(WindowEvent e){
	        	parent.toFront();
	        }
	    });
		
		layout = new GridLayout(2,1);
		this.setLayout(layout);
		
		int textAreaWidth = width - 10;
		int textAreaHeight = height - 10;
		
		consoleTextArea = new JTextArea();
		((DefaultCaret)consoleTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		consoleTextArea.setSize(textAreaWidth, textAreaHeight);
		consoleTextArea.setEditable(false);
		consoleTextArea.setText("");
		
		consoleScrollPane = new JScrollPane(consoleTextArea);
		this.add(consoleScrollPane);
		
		// Build console for errors
		errorConsoleTextArea = new JTextArea();
		((DefaultCaret)errorConsoleTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		errorConsoleTextArea.setSize(textAreaWidth, textAreaHeight);
		errorConsoleTextArea.setForeground(Color.RED);
		errorConsoleTextArea.setText("");
		
		errorScrollPane = new JScrollPane(errorConsoleTextArea);
		errorScrollPane.setLocation(0, height / 2);		
		this.add(errorScrollPane);
	}
	
	public void append(String text) {
		consoleTextArea.append(text);
	}
	
	public void appendError(String errorText) {
		
		errorConsoleTextArea.append(errorText);
	}
	
	public void printError(String error) {
		
		this.append("Error, an exception has been launched, see the error console below for further details.");
		errorConsoleTextArea.setText(error);
	}
	
	/**
	 * Show the console and reset the text
	 */
	public void activate() {
		consoleTextArea.setText("");
		errorConsoleTextArea.setText("");
		
		this.setVisible(true);
	}
	
	/**
	 * Show the console and execute some code and catch any exception, printing it to the console
	 */
	public void executeCode(ConsoleMethod method) {
		
		activate();
		try {
			method.execute();
		}
		catch (Exception exc) {
			String error = ExceptionUtils.getStackTrace(exc);
			appendError(error);
		}
	}
	
	/**
	 * Executes code in an async way, so that the console can be updated in realtime.
	 * Any exception caught will halt the thread and append the error to the console.
	 * @param method
	 */
	public void executeAsyncCode(ConsoleMethod method) {
		
		activate();
		new AsyncWorker(this, method).start();
	}
}

class AsyncWorker extends Thread {
	
	private ConsoleMethod method;
	private ConsoleFrame console;
	
	public AsyncWorker(ConsoleFrame console, ConsoleMethod method) {
		this.method = method;
		this.console = console;
	}
	
	@Override
	public void run() {
		try {
			method.execute();
		}
		catch (Exception exc) {
			String error = ExceptionUtils.getStackTrace(exc);
			console.appendError(error);
		}
	}
}
