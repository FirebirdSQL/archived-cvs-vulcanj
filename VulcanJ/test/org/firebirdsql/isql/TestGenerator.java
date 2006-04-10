package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestGenerator extends ISQLTestBase{
public TestGenerator (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCreate_generator_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/generator/create_generator_01.sql", blgDir+"/generator/create_generator_01.blg", "output/generator/create_generator_01.output");
}
public void testDrop_generator_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/generator/drop_generator_01.sql", blgDir+"/generator/drop_generator_01.blg", "output/generator/drop_generator_01.output");
}
public void testDrop_generator_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/generator/drop_generator_02.sql", blgDir+"/generator/drop_generator_02.blg", "output/generator/drop_generator_02.output");
}
public void testDrop_generator_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/generator/drop_generator_03.sql", blgDir+"/generator/drop_generator_03.blg", "output/generator/drop_generator_03.output");
}
}
