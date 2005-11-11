package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestTrigger extends ISQLTestBase{
public TestTrigger (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_trigger_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_01.sql", blgDir+"/trigger/alter_trigger_01.blg", "output/trigger/alter_trigger_01.output");
}
public void testAlter_trigger_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_02.sql", blgDir+"/trigger/alter_trigger_02.blg", "output/trigger/alter_trigger_02.output");
}
public void testAlter_trigger_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_03.sql", blgDir+"/trigger/alter_trigger_03.blg", "output/trigger/alter_trigger_03.output");
}
public void testAlter_trigger_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_05.sql", blgDir+"/trigger/alter_trigger_05.blg", "output/trigger/alter_trigger_05.output");
}
public void testAlter_trigger_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_07.sql", blgDir+"/trigger/alter_trigger_07.blg", "output/trigger/alter_trigger_07.output");
}
public void testAlter_trigger_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_08.sql", blgDir+"/trigger/alter_trigger_08.blg", "output/trigger/alter_trigger_08.output");
}
public void testAlter_trigger_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_10.sql", blgDir+"/trigger/alter_trigger_10.blg", "output/trigger/alter_trigger_10.output");
}
public void testAlter_trigger_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_12.sql", blgDir+"/trigger/alter_trigger_12.blg", "output/trigger/alter_trigger_12.output");
}
public void testAlter_trigger_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_13.sql", blgDir+"/trigger/alter_trigger_13.blg", "output/trigger/alter_trigger_13.output");
}
public void testAlter_trigger_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_14.sql", blgDir+"/trigger/alter_trigger_14.blg", "output/trigger/alter_trigger_14.output");
}
public void testAlter_trigger_15() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_15.sql", blgDir+"/trigger/alter_trigger_15.blg", "output/trigger/alter_trigger_15.output");
}
public void testAlter_trigger_16() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_16.sql", blgDir+"/trigger/alter_trigger_16.blg", "output/trigger/alter_trigger_16.output");
}
public void testAlter_trigger_17() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/alter_trigger_17.sql", blgDir+"/trigger/alter_trigger_17.blg", "output/trigger/alter_trigger_17.output");
}
public void testCreate_trigger_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_01.sql", blgDir+"/trigger/create_trigger_01.blg", "output/trigger/create_trigger_01.output");
}
public void testCreate_trigger_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_02.sql", blgDir+"/trigger/create_trigger_02.blg", "output/trigger/create_trigger_02.output");
}
public void testCreate_trigger_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_03.sql", blgDir+"/trigger/create_trigger_03.blg", "output/trigger/create_trigger_03.output");
}
public void testCreate_trigger_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_04.sql", blgDir+"/trigger/create_trigger_04.blg", "output/trigger/create_trigger_04.output");
}
public void testCreate_trigger_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_05.sql", blgDir+"/trigger/create_trigger_05.blg", "output/trigger/create_trigger_05.output");
}
public void testCreate_trigger_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_06.sql", blgDir+"/trigger/create_trigger_06.blg", "output/trigger/create_trigger_06.output");
}
public void testCreate_trigger_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_07.sql", blgDir+"/trigger/create_trigger_07.blg", "output/trigger/create_trigger_07.output");
}
public void testCreate_trigger_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_08.sql", blgDir+"/trigger/create_trigger_08.blg", "output/trigger/create_trigger_08.output");
}
public void testCreate_trigger_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_09.sql", blgDir+"/trigger/create_trigger_09.blg", "output/trigger/create_trigger_09.output");
}
public void testCreate_trigger_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/trigger/create_trigger_10.sql", blgDir+"/trigger/create_trigger_10.blg", "output/trigger/create_trigger_10.output");
}
}
