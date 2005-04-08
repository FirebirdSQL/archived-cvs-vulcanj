/* $Id$ */
package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestCast extends ISQLTestBase{
public TestCast (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCast_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_01.sql", blgDir+"/cast/cast_01.blg", "output/cast/cast_01.output");
}
public void testCast_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_02.sql", blgDir+"/cast/cast_02.blg", "output/cast/cast_02.output");
}
public void testCast_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_03.sql", blgDir+"/cast/cast_03.blg", "output/cast/cast_03.output");
}
public void testCast_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_04.sql", blgDir+"/cast/cast_04.blg", "output/cast/cast_04.output");
}
public void testCast_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_05.sql", blgDir+"/cast/cast_05.blg", "output/cast/cast_05.output");
}
public void testCast_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_06.sql", blgDir+"/cast/cast_06.blg", "output/cast/cast_06.output");
}
public void testCast_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_07.sql", blgDir+"/cast/cast_07.blg", "output/cast/cast_07.output");
}
public void testCast_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_08.sql", blgDir+"/cast/cast_08.blg", "output/cast/cast_08.output");
}
public void testCast_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_09.sql", blgDir+"/cast/cast_09.blg", "output/cast/cast_09.output");
}
public void testCast_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_10.sql", blgDir+"/cast/cast_10.blg", "output/cast/cast_10.output");
}
public void testCast_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_11.sql", blgDir+"/cast/cast_11.blg", "output/cast/cast_11.output");
}
public void testCast_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_12.sql", blgDir+"/cast/cast_12.blg", "output/cast/cast_12.output");
}
public void testCast_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_13.sql", blgDir+"/cast/cast_13.blg", "output/cast/cast_13.output");
}
public void testCast_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_14.sql", blgDir+"/cast/cast_14.blg", "output/cast/cast_14.output");
}
public void testCast_15() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_15.sql", blgDir+"/cast/cast_15.blg", "output/cast/cast_15.output");
}
public void testCast_16() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_16.sql", blgDir+"/cast/cast_16.blg", "output/cast/cast_16.output");
}
public void testCast_17() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_17.sql", blgDir+"/cast/cast_17.blg", "output/cast/cast_17.output");
}
public void testCast_18() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_18.sql", blgDir+"/cast/cast_18.blg", "output/cast/cast_18.output");
}
public void testCast_19() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_19.sql", blgDir+"/cast/cast_19.blg", "output/cast/cast_19.output");
}
public void testCast_21() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_21.sql", blgDir+"/cast/cast_21.blg", "output/cast/cast_21.output");
}
public void testCast_22() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_22.sql", blgDir+"/cast/cast_22.blg", "output/cast/cast_22.output");
}
public void testCast_23() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_23.sql", blgDir+"/cast/cast_23.blg", "output/cast/cast_23.output");
}
public void testCast_24() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/cast/cast_24.sql", blgDir+"/cast/cast_24.blg", "output/cast/cast_24.output");
}
}
