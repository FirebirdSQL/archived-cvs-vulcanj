/* $Id$ */
package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestBasic extends ISQLTestBase{
public TestBasic (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testDb_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_01.sql", blgDir+"/basic/db_01.blg", "output/basic/db_01.output");
}
public void testDb_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_02.sql", blgDir+"/basic/db_02.blg", "output/basic/db_02.output");
}
public void testDb_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_03.sql", blgDir+"/basic/db_03.blg", "output/basic/db_03.output");
}
public void testDb_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_04.sql", blgDir+"/basic/db_04.blg", "output/basic/db_04.output");
}
public void testDb_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_05.sql", blgDir+"/basic/db_05.blg", "output/basic/db_05.output");
}
public void testDb_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_06.sql", blgDir+"/basic/db_06.blg", "output/basic/db_06.output");
}
public void testDb_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_07.sql", blgDir+"/basic/db_07.blg", "output/basic/db_07.output");
}
public void testDb_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_08.sql", blgDir+"/basic/db_08.blg", "output/basic/db_08.output");
}
public void testDb_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_09.sql", blgDir+"/basic/db_09.blg", "output/basic/db_09.output");
}
public void testDb_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_10.sql", blgDir+"/basic/db_10.blg", "output/basic/db_10.output");
}
public void testDb_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_11.sql", blgDir+"/basic/db_11.blg", "output/basic/db_11.output");
}
public void testDb_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_12.sql", blgDir+"/basic/db_12.blg", "output/basic/db_12.output");
}
public void testDb_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_13.sql", blgDir+"/basic/db_13.blg", "output/basic/db_13.output");
}
public void testDb_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_14.sql", blgDir+"/basic/db_14.blg", "output/basic/db_14.output");
}
public void testDb_15() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_15.sql", blgDir+"/basic/db_15.blg", "output/basic/db_15.output");
}
public void testDb_16() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_16.sql", blgDir+"/basic/db_16.blg", "output/basic/db_16.output");
}
public void testDb_17() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_17.sql", blgDir+"/basic/db_17.blg", "output/basic/db_17.output");
}
public void testDb_18() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_18.sql", blgDir+"/basic/db_18.blg", "output/basic/db_18.output");
}
public void testDb_19() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_19.sql", blgDir+"/basic/db_19.blg", "output/basic/db_19.output");
}
public void testDb_20() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_20.sql", blgDir+"/basic/db_20.blg", "output/basic/db_20.output");
}
public void testDb_21() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_21.sql", blgDir+"/basic/db_21.blg", "output/basic/db_21.output");
}
public void testDb_22() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_22.sql", blgDir+"/basic/db_22.blg", "output/basic/db_22.output");
}
public void testDb_23() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_23.sql", blgDir+"/basic/db_23.blg", "output/basic/db_23.output");
}
public void testDb_24() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_24.sql", blgDir+"/basic/db_24.blg", "output/basic/db_24.output");
}
public void testDb_25() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_25.sql", blgDir+"/basic/db_25.blg", "output/basic/db_25.output");
}
public void testDb_26() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_26.sql", blgDir+"/basic/db_26.blg", "output/basic/db_26.output");
}
public void testDb_27() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_27.sql", blgDir+"/basic/db_27.blg", "output/basic/db_27.output");
}
public void testDb_28() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_28.sql", blgDir+"/basic/db_28.blg", "output/basic/db_28.output");
}
public void testDb_29() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_29.sql", blgDir+"/basic/db_29.blg", "output/basic/db_29.output");
}
public void testDb_30() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_30.sql", blgDir+"/basic/db_30.blg", "output/basic/db_30.output");
}
public void testDb_31() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_31.sql", blgDir+"/basic/db_31.blg", "output/basic/db_31.output");
}
public void testDb_32() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/db_32.sql", blgDir+"/basic/db_32.blg", "output/basic/db_32.output");
}
public void testIsql_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/isql_01.sql", blgDir+"/basic/isql_01.blg", "output/basic/isql_01.output");
}
public void testIsql_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/basic/isql_02.sql", blgDir+"/basic/isql_02.blg", "output/basic/isql_02.output");
}
}
