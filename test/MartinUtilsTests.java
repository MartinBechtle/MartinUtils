import martinutils.sql.SqlBooleanClauseTest;
import martinutils.text.MartinDateTimeFormatterTest;
import martinutils.text.StringSplitterTest;
import martinutils.text.StringUtilTest;
import martinutils.xml.XmlAsStrUtilityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	SqlBooleanClauseTest.class,
	MartinDateTimeFormatterTest.class,
	StringUtilTest.class, 
	XmlAsStrUtilityTest.class,
	StringSplitterTest.class})
public class MartinUtilsTests {

}
