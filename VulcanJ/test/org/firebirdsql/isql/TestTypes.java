package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestTypes extends ISQLTestBase{
public TestTypes (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testBigint() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/types/bigint.sql", blgDir+"/types/bigint.blg", "output/types/bigint.output");
}
public void testDouble() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/types/double.sql", blgDir+"/types/double.blg", "output/types/double.output");
}
public void testFloat() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/types/float.sql", blgDir+"/types/float.blg", "output/types/float.output");
}
}
