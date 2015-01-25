package martinutils.sql;

/**
 * Eredita una SqlBooleanClause di tipo AND. Ovvero tutte le condizioni inserite in questa "clause" saranno concatenate tramite AND.
 * @author martin
 */
public class AndSqlBooleanClause extends SqlBooleanClause
{
	public AndSqlBooleanClause()
	{
		super(SqlBooleanClauseType.AND, null);
	}
	
	public AndSqlBooleanClause(String clause)
	{
		super(SqlBooleanClauseType.AND, clause);
	}
}