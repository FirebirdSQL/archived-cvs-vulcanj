package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestFbtcs extends ISQLTestBase{
public TestFbtcs (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCf_isql_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_01.sql", blgDir+"/fbtcs/cf_isql_01.blg", "output/fbtcs/cf_isql_01.output");
}
public void testCf_isql_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_02.sql", blgDir+"/fbtcs/cf_isql_02.blg", "output/fbtcs/cf_isql_02.output");
}
public void testCf_isql_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_03.sql", blgDir+"/fbtcs/cf_isql_03.blg", "output/fbtcs/cf_isql_03.output");
}
public void testCf_isql_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_04.sql", blgDir+"/fbtcs/cf_isql_04.blg", "output/fbtcs/cf_isql_04.output");
}
public void testCf_isql_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_05.sql", blgDir+"/fbtcs/cf_isql_05.blg", "output/fbtcs/cf_isql_05.output");
}
public void testCf_isql_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_06.sql", blgDir+"/fbtcs/cf_isql_06.blg", "output/fbtcs/cf_isql_06.output");
}
public void testCf_isql_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_07.sql", blgDir+"/fbtcs/cf_isql_07.blg", "output/fbtcs/cf_isql_07.output");
}
public void testCf_isql_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_08.sql", blgDir+"/fbtcs/cf_isql_08.blg", "output/fbtcs/cf_isql_08.output");
}
public void testCf_isql_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_09.sql", blgDir+"/fbtcs/cf_isql_09.blg", "output/fbtcs/cf_isql_09.output");
}
public void testCf_isql_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_10.sql", blgDir+"/fbtcs/cf_isql_10.blg", "output/fbtcs/cf_isql_10.output");
}
public void testCf_isql_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_11.sql", blgDir+"/fbtcs/cf_isql_11.blg", "output/fbtcs/cf_isql_11.output");
}
public void testCf_isql_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_12.sql", blgDir+"/fbtcs/cf_isql_12.blg", "output/fbtcs/cf_isql_12.output");
}
public void testCf_isql_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_13.sql", blgDir+"/fbtcs/cf_isql_13.blg", "output/fbtcs/cf_isql_13.output");
}
public void testCf_isql_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_14.sql", blgDir+"/fbtcs/cf_isql_14.blg", "output/fbtcs/cf_isql_14.output");
}
public void testCf_isql_15() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_15.sql", blgDir+"/fbtcs/cf_isql_15.blg", "output/fbtcs/cf_isql_15.output");
}
public void testCf_isql_16() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_16.sql", blgDir+"/fbtcs/cf_isql_16.blg", "output/fbtcs/cf_isql_16.output");
}
public void testCf_isql_17() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_17.sql", blgDir+"/fbtcs/cf_isql_17.blg", "output/fbtcs/cf_isql_17.output");
}
public void testCf_isql_18() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_18.sql", blgDir+"/fbtcs/cf_isql_18.blg", "output/fbtcs/cf_isql_18.output");
}
public void testCf_isql_19() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_19.sql", blgDir+"/fbtcs/cf_isql_19.blg", "output/fbtcs/cf_isql_19.output");
}
public void testCf_isql_20() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_20.sql", blgDir+"/fbtcs/cf_isql_20.blg", "output/fbtcs/cf_isql_20.output");
}
public void testCf_isql_22() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_22.sql", blgDir+"/fbtcs/cf_isql_22.blg", "output/fbtcs/cf_isql_22.output");
}
public void testCf_isql_23() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_23.sql", blgDir+"/fbtcs/cf_isql_23.blg", "output/fbtcs/cf_isql_23.output");
}
public void testCf_isql_26() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_26.sql", blgDir+"/fbtcs/cf_isql_26.blg", "output/fbtcs/cf_isql_26.output");
}
public void testCf_isql_27() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_27.sql", blgDir+"/fbtcs/cf_isql_27.blg", "output/fbtcs/cf_isql_27.output");
}
public void testCf_isql_28() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/fbtcs/cf_isql_28.sql", blgDir+"/fbtcs/cf_isql_28.blg", "output/fbtcs/cf_isql_28.output");
}
}
