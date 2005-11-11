package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestTable extends ISQLTestBase{
public TestTable (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_table_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_01.sql", blgDir+"/table/alter_table_01.blg", "output/table/alter_table_01.output");
}
public void testAlter_table_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_02.sql", blgDir+"/table/alter_table_02.blg", "output/table/alter_table_02.output");
}
public void testAlter_table_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_03.sql", blgDir+"/table/alter_table_03.blg", "output/table/alter_table_03.output");
}
public void testAlter_table_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_04.sql", blgDir+"/table/alter_table_04.blg", "output/table/alter_table_04.output");
}
public void testAlter_table_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_05.sql", blgDir+"/table/alter_table_05.blg", "output/table/alter_table_05.output");
}
public void testAlter_table_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_06.sql", blgDir+"/table/alter_table_06.blg", "output/table/alter_table_06.output");
}
public void testAlter_table_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_07.sql", blgDir+"/table/alter_table_07.blg", "output/table/alter_table_07.output");
}
public void testAlter_table_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_08.sql", blgDir+"/table/alter_table_08.blg", "output/table/alter_table_08.output");
}
public void testAlter_table_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_09.sql", blgDir+"/table/alter_table_09.blg", "output/table/alter_table_09.output");
}
public void testAlter_table_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_10.sql", blgDir+"/table/alter_table_10.blg", "output/table/alter_table_10.output");
}
public void testAlter_table_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/alter_table_11.sql", blgDir+"/table/alter_table_11.blg", "output/table/alter_table_11.output");
}
public void testCreate_table_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_01.sql", blgDir+"/table/create_table_01.blg", "output/table/create_table_01.output");
}
public void testCreate_table_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_02.sql", blgDir+"/table/create_table_02.blg", "output/table/create_table_02.output");
}
public void testCreate_table_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_03.sql", blgDir+"/table/create_table_03.blg", "output/table/create_table_03.output");
}
public void testCreate_table_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_04.sql", blgDir+"/table/create_table_04.blg", "output/table/create_table_04.output");
}
public void testCreate_table_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_05.sql", blgDir+"/table/create_table_05.blg", "output/table/create_table_05.output");
}
public void testCreate_table_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_06.sql", blgDir+"/table/create_table_06.blg", "output/table/create_table_06.output");
}
public void testCreate_table_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/table/create_table_07.sql", blgDir+"/table/create_table_07.blg", "output/table/create_table_07.output");
}
}
