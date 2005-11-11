package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestAvg extends ISQLTestBase{
public TestAvg (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAvg_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_01.sql", blgDir+"/avg/avg_01.blg", "output/avg/avg_01.output");
}
public void testAvg_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_02.sql", blgDir+"/avg/avg_02.blg", "output/avg/avg_02.output");
}
public void testAvg_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_03.sql", blgDir+"/avg/avg_03.blg", "output/avg/avg_03.output");
}
public void testAvg_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_04.sql", blgDir+"/avg/avg_04.blg", "output/avg/avg_04.output");
}
public void testAvg_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_06.sql", blgDir+"/avg/avg_06.blg", "output/avg/avg_06.output");
}
public void testAvg_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_07.sql", blgDir+"/avg/avg_07.blg", "output/avg/avg_07.output");
}
public void testAvg_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_08.sql", blgDir+"/avg/avg_08.blg", "output/avg/avg_08.output");
}
public void testAvg_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/avg/avg_09.sql", blgDir+"/avg/avg_09.blg", "output/avg/avg_09.output");
}
}
