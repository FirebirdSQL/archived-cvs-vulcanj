package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestShadow extends ISQLTestBase{
public TestShadow (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCreate_shadow_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/shadow/create_shadow_01.sql", blgDir+"/shadow/create_shadow_01.blg", "output/shadow/create_shadow_01.output");
}
public void testCreate_shadow_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/shadow/create_shadow_02.sql", blgDir+"/shadow/create_shadow_02.blg", "output/shadow/create_shadow_02.output");
}
}
