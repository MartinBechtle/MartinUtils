package martinutils.io;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;




import org.apache.commons.lang3.StringUtils;

/**
 * Serve a stampare file a righe e colonne
 * @author Martin
 */
public class LineWriter
{
	private Writer out = null;
	private StringBuilder sb = null;
	private String colSeparator = "\t";
	private String lineSeparator = "\n";
	private String colWrapper = "";
	private List<EscapeSequence> escapeSequences = new ArrayList<>();
	
	/**
	 * Costruttore vuoto: tutto il contenuto verrà appeso ad uno StringBuilder. Poi si userà la funzione saveToFile per l'output
	 */
	public LineWriter()
	{
		sb = new StringBuilder();
	}
	
	/**
	 * Creando l'istanza con questo costruttore, le varie righe non saranno tenute in memoria ma saranno mandate direttamente ad un Writer.
	 * @param out il Writer a cui mandare l'output. Si consiglia caldamente l'uso di un BufferedWriter
	 */
	public LineWriter(Writer out)
	{
		if (out == null)
			throw new IllegalArgumentException("parameter cannot be null");
		this.out = out;
	}
	
	/**
	 * Imposta la sequenza di separazione colonne (può essere anche solo un carattere). Default: tab.
	 * @param separator
	 * @return reference a questa istanza per method chaining
	 */
	public LineWriter setColSeparator(String separator)
	{
		if (StringUtils.isEmpty(separator))
			throw new IllegalArgumentException("separator cannot be empty");
		
		this.colSeparator = separator;
		return this;
	}
	
	/**
	 * Imposta la sequenza di separazione righe (può essere anche un solo carattere). Default: newline.
	 * @param separator
	 * @return reference a questa istanza per method chaining
	 */
	public LineWriter setLineSeparator(String separator)
	{
		if (StringUtils.isEmpty(separator))
			throw new IllegalArgumentException("separator cannot be empty");
		
		this.lineSeparator = separator;
		return this;
	}

	/**
	 * Imposta la sequenza che wrappa i contenuti delle colonne (può essere anche un solo carattere). Default: nessuna.
	 * @param wrapper la sequenza che wrappa i contenuti
	 * @param replacement la sequenza con cui rimpizzare il wrapper all'interno dei contenuti, per l'escaping
	 * @return reference a questa istanza per method chaining
	 */
	public LineWriter setColWrapper(String wrapper, String replacement)
	{
		if (wrapper == null)
			wrapper = "";
		
		this.colWrapper = wrapper;
		this.wrap = colWrapper.length() > 0;
		
		if (this.wrap)
			addEscapeSequence(wrapper, replacement);
		
		return this;
	}
	
	/**
	 * Aggiunge una sequenza di escape all'interno delle colonne. Esempio: si potrebbe voler escapare i tab, le newline, le virgolette.
	 * NB: è possibile procedere senza sequenze di escape a patto che non venga impostato alcun wrapper e che si sia sicuri che all'interno delle stringhe non ci siano separatori di righe e colonne
	 * @param match la stringa da matchare
	 * @param replacement il suo rimpiazzo per escaping
	 * @return
	 */
	public LineWriter addEscapeSequence(String match, String replacement)
	{
		if (StringUtils.isEmpty(match))
			throw new IllegalArgumentException();
		
		if (replacement == null)
			replacement = "";
		
		this.escapeSequences.add( new EscapeSequence(match, replacement) );
		this.escape = true;
		
		return this;
	}
	
	/**
	 * Imposta il formato di output a Csv. Ovvero: separatore di righe newline e separatore colonne virgola. 
	 * Le colonne saranno wrappate in virgolette "" e queste saranno escapate a loro volta da una virgoletta.
	 * @return reference a questa istanza per method chaining
	 */
	public LineWriter setOutputAsCsv()
	{
		this.lineSeparator = "\n";
		this.colSeparator = ",";
		this.colWrapper = "\"";
		
		this.wrap = true;
		
		return this;
	}
	
	/**
	 * Stampa una riga
	 * @param cols
	 * @throws IOException
	 * @throws LineFormatException 
	 */
	public void printLine(String ... cols) throws IOException, LineFormatException
	{
		int len = cols.length;
		
		if (len > 0) // se non è stata passata alcuna stringa, si stampa una riga vuota
		{
			int lastIndex = len - 1;
			
			// Si stampano tutte le colonne tranne l'ultima, seguite dal colSeparator
			for (int i = 0; i < lastIndex; i++)
			{
				appendContent(cols[i]);
				append(colSeparator);
			}
			
			// Si stampa l'ultima colonna (che non deve essere seguita dal colSeparator)
			appendContent(cols[lastIndex]);
		}
		
		// Si stampa il separatore dei riga
		append(lineSeparator);
	}
	
	/**
	 * Stampa su file UTF-8 il contenuto di questa istanza. Se l'output era stato mandato su un writer, stamperà un file vuoto.
	 * @param file
	 * @throws IOException 
	 */
	public void saveToFile(File file) throws IOException
	{
		if (sb == null)
			sb = new StringBuilder();
		
		FileUtil.saveUtf8File(file, sb.toString());
	}
	
	/**
	 * Stampa su file il contenuto di questa istanza. Se l'output era stato mandato su un writer, stamperà un file vuoto.
	 * @param file
	 * @throws IOException 
	 */
	public void saveToFile(File file, String charset) throws IOException
	{
		if (sb == null)
			sb = new StringBuilder();
		
		FileUtil.saveTextFile(file, sb.toString(), charset);
	}
	
	private void append(String str) throws IOException
	{
		if (sb != null)
			sb.append(str);
		else
			out.append(str);
	}
	
	private void appendContent(String col) throws LineFormatException, IOException
	{
		col = col != null ? wrapAndEscape(col) : "";
		
		if (col.contains(colSeparator))
		{
			String output = col.replace(colSeparator, "###" + colSeparator + "###");
			throw new LineFormatException("Content contains unescaped column separator:\n" + output);
		}
		if (col.contains(lineSeparator))
		{
			String output = col.replace(lineSeparator, "###" + lineSeparator + "###");
			throw new LineFormatException("Content contains unescaped line separator:\n" + output);
		}
		
		append(col);
	}
	
	private boolean wrap = false;
	private boolean escape = false;
	
	private String wrapAndEscape(String col)
	{
		if (wrap)
			col = String.format("%s%s%s", colWrapper, col, colWrapper);
		
		if (escape)
			for (EscapeSequence seq : escapeSequences)
				col = col.replace(seq.match, seq.replacement);
		
		return col;
	}
}

class EscapeSequence
{
	String match;
	String replacement;
	
	public EscapeSequence(String match, String replacement) {
		this.match = match;
		this.replacement = replacement;
	}
}
