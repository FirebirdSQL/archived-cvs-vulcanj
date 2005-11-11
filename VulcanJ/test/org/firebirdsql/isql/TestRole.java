package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestRole extends ISQLTestBase{
public TestRole (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCreate_role_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/role/create_role_01.sql", blgDir+"/role/create_role_01.blg", "output/role/create_role_01.output");
}
public void testCreate_role_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/role/create_role_02.sql", blgDir+"/role/create_role_02.blg", "output/role/create_role_02.output");
}
}
