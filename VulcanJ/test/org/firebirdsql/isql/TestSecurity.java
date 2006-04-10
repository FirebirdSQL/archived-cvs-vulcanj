package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestSecurity extends ISQLTestBase{
public TestSecurity (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCreate_database_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/security/create_database_03.sql", blgDir+"/security/create_database_03.blg", "output/security/create_database_03.output");
}
public void testCreate_database_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/security/create_database_14.sql", blgDir+"/security/create_database_14.blg", "output/security/create_database_14.output");
}
public void testCreate_page_sizes() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/security/create_page_sizes.sql", blgDir+"/security/create_page_sizes.blg", "output/security/create_page_sizes.output");
}
public void testCreate_schema_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/security/create_schema_01.sql", blgDir+"/security/create_schema_01.blg", "output/security/create_schema_01.output");
}
}
