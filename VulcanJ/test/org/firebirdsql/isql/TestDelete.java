package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestDelete extends ISQLTestBase{
public TestDelete (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testDelete_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/delete/delete_01.sql", blgDir+"/delete/delete_01.blg", "output/delete/delete_01.output");
}
public void testDelete_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/delete/delete_02.sql", blgDir+"/delete/delete_02.blg", "output/delete/delete_02.output");
}
public void testDelete_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/delete/delete_03.sql", blgDir+"/delete/delete_03.blg", "output/delete/delete_03.output");
}
}
