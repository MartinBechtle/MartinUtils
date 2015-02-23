package martinutils.text;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Utility di formattazione date a partire da vari input (java.util.Date, java.sql.Timestamp, java.sql.Date, long). 
 * E' possibile scegliere il formato sia per l'ora che per la data, e anche la timezone.
 * @author martin
 */
public class MartinDateTimeFormatter
{
	protected boolean showDate = false;
	protected boolean showTime = false;
	protected boolean displaySec = false;
	
	protected String timeFormat = "HH:mm:ss";
	protected String dateFormat = "yyyy/MM/dd";
	
	protected TimeZone timeZone = TimeZone.getDefault();
	
	/**
	 * Costruttore di default (non formatta niente se non si chiama almeno uno dei metodi showDate o showTime.
	 * Il formato per tempo e data di default è quello americano. La timezone è quella della JVM.
	 */
	public MartinDateTimeFormatter()
	{
	}
	
	/**
	 * Costruttore impostato sulla timeZone della JVM e formato americano. Consente di specificare cosa visualizzare
	 */
	public MartinDateTimeFormatter(boolean showDate, boolean showTime, boolean displaySec)
	{
		this.showDate = showDate;
		this.showTime = showTime;
		this.displaySec = displaySec;
	}

	/**
	 * Costruttore full options
	 */
	public MartinDateTimeFormatter(boolean showDate, boolean showTime, boolean displaySec, String dateFormat, String timeFormat, String timeZoneId)
	{
		this.showDate = showDate;
		this.showTime = showTime;
		this.displaySec = displaySec;
		
		this.dateFormat = dateFormat;
		this.timeFormat = timeFormat;
		this.timeZone = TimeZone.getTimeZone(timeZoneId);
	}
	
	/**
	 * Chiamando questo metodo si imposterà il formatter in modo da stampare la data
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter showDate()
	{
		showDate = true;
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposterà il formatter in modo da stampare l'ora
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter showTime()
	{
		showTime = true;
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposterà il formatter in modo da stampare anche i secondi nell'ora
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter displaySec()
	{
		displaySec = true;
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposta il timeformat (il default è quello americano)
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter setTimeFormat(String format)
	{
		if (format != null)
			timeFormat = format;
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposta il dateformat (il default è quello americano)
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter setDateFormat(String format)
	{
		if (format != null)
			dateFormat = format;
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposta il fuso orario (il default è quello della jvm)
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter setTimeZone(String timezoneId)
	{
		if (timezoneId != null)
			timeZone = TimeZone.getTimeZone(timezoneId);
		return this;
	}
	
	/**
	 * Chiamando questo metodo si imposta il fuso orario (il default è quello della jvm)
	 * @return un riferimento a questa stessa istanza per method chaining
	 */
	public MartinDateTimeFormatter setTimeZone(TimeZone timeZone)
	{
		if (timeZone != null)
			this.timeZone = timeZone;
		return this;
	}
	
	/**
	 * Formatta un timestamp sql
	 * @param ts
	 * @return
	 */
	public String format(Timestamp ts)
	{
		return format(ts.getTime());
	}
	
	/**
	 * Formatta una long rappresentante una data
	 * @param time
	 * @return
	 */
	public String format(long time)
	{
		return format( new Date(time) );
	}
	
	/**
	 * Formatta l'ora corrente
	 * @return
	 */
	public String format()
	{
		return format( new Date() );
	}
	
	/**
	 * Formatta una Date sql
	 * @param date
	 * @return
	 */
	public String format( java.sql.Date date )
	{
		return format(new Date(date.getTime()));
	}
	
	/**
	 * Formatta una date
	 * @param date
	 * @return
	 */
	public String format(Date date)
	{
		String format = "";
		
		if (showDate && showTime)
			format = dateFormat + " " + timeFormat;
		else if (showDate)
			format = dateFormat;
		else if (showTime)
			format = timeFormat;
		
		if (!displaySec)
			format = format.replaceAll("[ :,\\.;]ss", "");
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);
		
		return sdf.format(date);
	}
}
