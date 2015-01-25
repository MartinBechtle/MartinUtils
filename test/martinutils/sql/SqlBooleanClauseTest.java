package martinutils.sql;

import static org.junit.Assert.*;

import org.junit.Test;

public class SqlBooleanClauseTest
{
	@Test
	public void testPrint()
	{
		 SqlBooleanClause where = new AndSqlBooleanClause("A");
		 SqlBooleanClause secondClause = new OrSqlBooleanClause("B");
		 secondClause.add("C");
		 SqlBooleanClause thirdClause = new OrSqlBooleanClause("D");
		 SqlBooleanClause innerClause = new AndSqlBooleanClause();
		 innerClause.add("E").add("F");
		 thirdClause.add(innerClause);
		 where.add(secondClause).add(thirdClause);
		 
		 String result = where.print();
		 assertEquals("A AND (B OR C) AND (D OR (E AND F))", result);
	}

}
