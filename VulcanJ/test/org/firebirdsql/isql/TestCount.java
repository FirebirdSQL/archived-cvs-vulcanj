package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestCount extends ISQLTestBase{
public TestCount (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCount_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/count/count_01.sql", blgDir+"/count/count_01.blg", "output/count/count_01.output");
}
public void testCount_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/count/count_02.sql", blgDir+"/count/count_02.blg", "output/count/count_02.output");
}
}
