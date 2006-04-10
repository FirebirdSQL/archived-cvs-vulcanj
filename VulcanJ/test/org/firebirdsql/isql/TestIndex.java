package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestIndex extends ISQLTestBase{
public TestIndex (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_index_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/alter_index_01.sql", blgDir+"/index/alter_index_01.blg", "output/index/alter_index_01.output");
}
public void testAlter_index_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/alter_index_02.sql", blgDir+"/index/alter_index_02.blg", "output/index/alter_index_02.output");
}
public void testAlter_index_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/alter_index_03.sql", blgDir+"/index/alter_index_03.blg", "output/index/alter_index_03.output");
}
public void testAlter_index_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/alter_index_04.sql", blgDir+"/index/alter_index_04.blg", "output/index/alter_index_04.output");
}
public void testAlter_index_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/alter_index_05.sql", blgDir+"/index/alter_index_05.blg", "output/index/alter_index_05.output");
}
public void testCreate_index_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_01.sql", blgDir+"/index/create_index_01.blg", "output/index/create_index_01.output");
}
public void testCreate_index_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_02.sql", blgDir+"/index/create_index_02.blg", "output/index/create_index_02.output");
}
public void testCreate_index_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_03.sql", blgDir+"/index/create_index_03.blg", "output/index/create_index_03.output");
}
public void testCreate_index_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_04.sql", blgDir+"/index/create_index_04.blg", "output/index/create_index_04.output");
}
public void testCreate_index_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_05.sql", blgDir+"/index/create_index_05.blg", "output/index/create_index_05.output");
}
public void testCreate_index_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_06.sql", blgDir+"/index/create_index_06.blg", "output/index/create_index_06.output");
}
public void testCreate_index_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_07.sql", blgDir+"/index/create_index_07.blg", "output/index/create_index_07.output");
}
public void testCreate_index_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_08.sql", blgDir+"/index/create_index_08.blg", "output/index/create_index_08.output");
}
public void testCreate_index_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_09.sql", blgDir+"/index/create_index_09.blg", "output/index/create_index_09.output");
}
public void testCreate_index_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_10.sql", blgDir+"/index/create_index_10.blg", "output/index/create_index_10.output");
}
public void testCreate_index_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_12.sql", blgDir+"/index/create_index_12.blg", "output/index/create_index_12.output");
}
public void testCreate_index_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/create_index_13.sql", blgDir+"/index/create_index_13.blg", "output/index/create_index_13.output");
}
public void testIndex_bad_results() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/index/index_bad_results.sql", blgDir+"/index/index_bad_results.blg", "output/index/index_bad_results.output");
}
}
