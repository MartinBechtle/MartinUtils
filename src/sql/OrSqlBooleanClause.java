package sql;

/**
 * Eredita una SqlBooleanClause di tipo OR. Ovvero tutte le condizioni inserite in questa "clause" saranno concatenate tramite OR.
 * @author martin
 */
public class OrSqlBooleanClause extends SqlBooleanClause
{
	public OrSqlBooleanClause()
	{
		super(SqlBooleanClauseType.OR, null);
	}
	
	public OrSqlBooleanClause(String clause)
	{
		super(SqlBooleanClauseType.OR, clause);
	}
}