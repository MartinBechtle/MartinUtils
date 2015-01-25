package sql;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Questa classe consente di creare facilmente di creare condizioni booleane per query SQL.</p>
 * <div>Va utilizzata per costruire dinamicamente casi molto complessi: esempio A and (B or C) and (D or (E and F))</div>
 * <div>Non può essere istanziata direttamente: bisogna utilizzare una delle due classi che la ereditano: AndSqlBooleanClause oppure OrSqlBooleanClause.</div>
 * <div>Ad ogni oggetto di questa classe è possibile aggiungere più condizioni, il che consente di creare un albero. Aggiungendo condizioni ad una
 * clause di tipo AND, tutte queste saranno concatenate tramite AND. Stessa cosa per quella di tipo OR</div>
 * <p>Esempio:</p>
 * <pre>
 * <code>
 * SqlBooleanClause where = new AndSqlBooleanClause(A);
 * SqlBooleanClause secondClause = new OrSqlBooleanClause(B);
 * secondClause.add(C);
 * SqlBooleanClause thirdClause = new OrSqlBooleanClausesecondClause.add(C);
 * SqlBooleanClause innerClause = new AndSqlBooleanClause()
 * innerClause.add(E).add(F);
 * thirdClause.add(innerClause);
 * where.add(secondClause).add(thirdClause);
 * where.print();
 * </code>
 * </pre>
 * @author martin
 */
public class SqlBooleanClause
{
	public static void main(String[] args)
	{
		SqlBooleanClause where = new OrSqlBooleanClause();
		SqlBooleanClause height = new AndSqlBooleanClause();
		SqlBooleanClause name = new AndSqlBooleanClause();
		
		where.add(height).add(name);
		
		name.add("name == 'martin'");
		height.add("height > 180");
		height.add("height < 190");
		
		String whereStr = where.print();
		System.out.println(whereStr);
	}
	
	protected enum SqlBooleanClauseType {AND, OR};
	private SqlBooleanClauseType type = SqlBooleanClauseType.AND;
	
	private List<SqlBooleanClause> clauses = new ArrayList<>();
	private String clause = null;
	
	/**
	 * Costruttore interno
	 */
	protected SqlBooleanClause(SqlBooleanClauseType type, String clause)
	{
		this.type = type;
		this.clause = clause;
	}
	
	/**
	 * Questo metodo serve ad aggiungere una condizione singola sotto forma testuale
	 * @param clause la stringa che esprime la condizione con operatori SQL (esempio: age = 20)
	 * @return un riferimento a questa istanza per consentire il concatenamento di metodi
	 */
	public SqlBooleanClause add(String clause)
	{
		// E' la prima condizione che viene aggiunta?
		if (this.clauses.isEmpty())
		{
			if (this.clause == null)
			{
				// Sì, settiamola
				this.clause = clause;
			}
			else
			{
				// No, è la seconda, allora bisogna configurarla come concatenazione di condizioni.
				SqlBooleanClause firstClause = new SqlBooleanClause(this.type, this.clause); // la prima chiaramente è quella che già era impostata
				SqlBooleanClause secondClause = new SqlBooleanClause(type, clause); // la seconda è l'argomento passato
				clauses.add(firstClause);
				clauses.add(secondClause);
			}
		}
		else
		{
			// No, è l'n-esima
			SqlBooleanClause nextClause = new SqlBooleanClause(type, clause); // la prossima è l'argomento passato
			clauses.add(nextClause);
		}
		
		return this;
	}
	
	/**
	 * Questo metodo serve ad aggiungere una condizione sotto forma di oggetto
	 * @param clause
	 * @return un riferimento a questa istanza per consentire il concatenamento di metodi
	 */
	public SqlBooleanClause add(SqlBooleanClause clause)
	{
		// E' la prima condizione che viene aggiunta?
		if (this.clauses.isEmpty())
		{
			if (this.clause == null)
			{
				// Sì
				this.clauses.add(clause);
			}
			else
			{
				// No, c'era già una clausola singola prima
				SqlBooleanClause firstClause = new SqlBooleanClause(this.type, this.clause); // la prima chiaramente è quella che già era impostata
				clauses.add(firstClause);
				clauses.add(clause);
			}
		}
		else
		{
			// No, è l'n-esima
			clauses.add(clause);
		}
		
		return this;
	}
	
	/**
	 * Stampa le condizioni concatenando nel giusto modo e mettendo le parentesi dove necessario
	 * @return
	 */
	public String print()
	{
		return print(true);
	}
	
	/**
	 * Stampa le condizioni ricorsivamente.
	 * @param firstLevel Se ci sono più condizioni e questa opzione è true, allora non vengono messe le parentesi
	 * @return
	 */
	private String print(boolean firstLevel)
	{
		int size = clauses.size();
		
		if (size == 0) {
			return clause != null ? clause : "";
		}
		else if (size == 1) {
			return clauses.get(0).print(false);
		}
		else
		{
			// Se ci sono più condizioni, bisogna concatenarle
			StringBuilder sb = new StringBuilder();
			boolean isFirst = true;
			int count = 0;
			
			// Stampare tutte le condizioni
			for (SqlBooleanClause clause : clauses)
			{
				String printedClause = clause.print(false);
				if (printedClause.isEmpty())
					continue; // condizione vuota
				
				count++;
				
				if (isFirst) // è la prima, non c'è bisogno di concatenarla
				{
					sb.append(printedClause); 
					isFirst = false;
					continue;
				}
				
				// E' l'n-esima, bisogna concatenarla
				sb.append(this.type == SqlBooleanClauseType.AND ? " AND " : " OR ");
				sb.append(printedClause);
			}
			
			String printedClauses = sb.toString();
			if (printedClauses.isEmpty() || count == 0)
				return ""; // erano tutte condizioni vuote
			
			if (count == 1)
				return printedClauses; // in realtà era una condizione singola
			
			// Sono più condizioni, vanno messe tra parentesi solo se questa non è la condizione di primo livello
			if (firstLevel)
				return printedClauses;
			
			return "(" + printedClauses + ")";
		}
	}
}