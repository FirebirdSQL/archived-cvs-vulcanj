package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestException extends ISQLTestBase{
public TestException (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_exception_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/alter_exception_01.sql", blgDir+"/exception/alter_exception_01.blg", "output/exception/alter_exception_01.output");
}
public void testCreate_exception_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/create_exception_01.sql", blgDir+"/exception/create_exception_01.blg", "output/exception/create_exception_01.output");
}
public void testCreate_exception_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/create_exception_03.sql", blgDir+"/exception/create_exception_03.blg", "output/exception/create_exception_03.output");
}
public void testCreate_exception_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/create_exception_04.sql", blgDir+"/exception/create_exception_04.blg", "output/exception/create_exception_04.output");
}
public void testDrop_exception_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/drop_exception_01.sql", blgDir+"/exception/drop_exception_01.blg", "output/exception/drop_exception_01.output");
}
public void testDrop_exception_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/drop_exception_02.sql", blgDir+"/exception/drop_exception_02.blg", "output/exception/drop_exception_02.output");
}
public void testDrop_exception_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/exception/drop_exception_03.sql", blgDir+"/exception/drop_exception_03.blg", "output/exception/drop_exception_03.output");
}
}
