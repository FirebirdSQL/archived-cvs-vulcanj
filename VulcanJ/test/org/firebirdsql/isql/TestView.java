/* $Id$ */
package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestView extends ISQLTestBase{
public TestView (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCreate_view_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_01.sql", blgDir+"/view/create_view_01.blg", "output/view/create_view_01.output");
}
public void testCreate_view_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_02.sql", blgDir+"/view/create_view_02.blg", "output/view/create_view_02.output");
}
public void testCreate_view_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_03.sql", blgDir+"/view/create_view_03.blg", "output/view/create_view_03.output");
}
public void testCreate_view_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_04.sql", blgDir+"/view/create_view_04.blg", "output/view/create_view_04.output");
}
public void testCreate_view_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_05.sql", blgDir+"/view/create_view_05.blg", "output/view/create_view_05.output");
}
public void testCreate_view_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_06.sql", blgDir+"/view/create_view_06.blg", "output/view/create_view_06.output");
}
public void testCreate_view_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_07.sql", blgDir+"/view/create_view_07.blg", "output/view/create_view_07.output");
}
public void testCreate_view_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/view/create_view_08.sql", blgDir+"/view/create_view_08.blg", "output/view/create_view_08.output");
}
}
