package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestDatabase extends ISQLTestBase{
public TestDatabase (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_database_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/alter_database_01.sql", blgDir+"/database/alter_database_01.blg", "output/database/alter_database_01.output");
}
public void testAlter_database_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/alter_database_02.sql", blgDir+"/database/alter_database_02.blg", "output/database/alter_database_02.output");
}
public void testAlter_database_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/alter_database_03.sql", blgDir+"/database/alter_database_03.blg", "output/database/alter_database_03.output");
}
public void testCreate_database_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_01.sql", blgDir+"/database/create_database_01.blg", "output/database/create_database_01.output");
}
public void testCreate_database_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_02.sql", blgDir+"/database/create_database_02.blg", "output/database/create_database_02.output");
}
public void testCreate_database_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_03.sql", blgDir+"/database/create_database_03.blg", "output/database/create_database_03.output");
}
public void testCreate_database_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_04.sql", blgDir+"/database/create_database_04.blg", "output/database/create_database_04.output");
}
public void testCreate_database_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_05.sql", blgDir+"/database/create_database_05.blg", "output/database/create_database_05.output");
}
public void testCreate_database_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_06.sql", blgDir+"/database/create_database_06.blg", "output/database/create_database_06.output");
}
public void testCreate_database_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_07.sql", blgDir+"/database/create_database_07.blg", "output/database/create_database_07.output");
}
public void testCreate_database_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_08.sql", blgDir+"/database/create_database_08.blg", "output/database/create_database_08.output");
}
public void testCreate_database_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_09.sql", blgDir+"/database/create_database_09.blg", "output/database/create_database_09.output");
}
public void testCreate_database_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_10.sql", blgDir+"/database/create_database_10.blg", "output/database/create_database_10.output");
}
public void testCreate_database_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_11.sql", blgDir+"/database/create_database_11.blg", "output/database/create_database_11.output");
}
public void testCreate_database_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_12.sql", blgDir+"/database/create_database_12.blg", "output/database/create_database_12.output");
}
public void testCreate_database_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_13.sql", blgDir+"/database/create_database_13.blg", "output/database/create_database_13.output");
}
public void testCreate_database_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/database/create_database_14.sql", blgDir+"/database/create_database_14.blg", "output/database/create_database_14.output");
}
}
