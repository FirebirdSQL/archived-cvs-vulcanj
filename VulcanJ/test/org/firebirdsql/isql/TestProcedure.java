package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestProcedure extends ISQLTestBase{
public TestProcedure (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_procedure_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/alter_procedure_01.sql", blgDir+"/procedure/alter_procedure_01.blg", "output/procedure/alter_procedure_01.output");
}
public void testAlter_procedure_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/alter_procedure_03.sql", blgDir+"/procedure/alter_procedure_03.blg", "output/procedure/alter_procedure_03.output");
}
public void testCreate_procedure_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_01.sql", blgDir+"/procedure/create_procedure_01.blg", "output/procedure/create_procedure_01.output");
}
public void testCreate_procedure_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_02.sql", blgDir+"/procedure/create_procedure_02.blg", "output/procedure/create_procedure_02.output");
}
public void testCreate_procedure_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_03.sql", blgDir+"/procedure/create_procedure_03.blg", "output/procedure/create_procedure_03.output");
}
public void testCreate_procedure_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_04.sql", blgDir+"/procedure/create_procedure_04.blg", "output/procedure/create_procedure_04.output");
}
public void testCreate_procedure_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_05.sql", blgDir+"/procedure/create_procedure_05.blg", "output/procedure/create_procedure_05.output");
}
public void testCreate_procedure_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_06.sql", blgDir+"/procedure/create_procedure_06.blg", "output/procedure/create_procedure_06.output");
}
public void testCreate_procedure_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_08.sql", blgDir+"/procedure/create_procedure_08.blg", "output/procedure/create_procedure_08.output");
}
public void testCreate_procedure_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/procedure/create_procedure_09.sql", blgDir+"/procedure/create_procedure_09.blg", "output/procedure/create_procedure_09.output");
}
}
