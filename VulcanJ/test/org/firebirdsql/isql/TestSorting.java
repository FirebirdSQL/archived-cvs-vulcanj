package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestSorting extends ISQLTestBase{
public TestSorting (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testFirebirdNullsSortFirst() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/sorting/FirebirdNullsSortFirst.sql", blgDir+"/sorting/FirebirdNullsSortFirst.blg", "output/sorting/FirebirdNullsSortFirst.output");
}
public void testFirebirdNullsSortLast() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/sorting/FirebirdNullsSortLast.sql", blgDir+"/sorting/FirebirdNullsSortLast.blg", "output/sorting/FirebirdNullsSortLast.output");
}
}
