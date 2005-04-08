/* $Id$ */
package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestDomain extends ISQLTestBase{
public TestDomain (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_domain_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/alter_domain_01.sql", blgDir+"/domain/alter_domain_01.blg", "output/domain/alter_domain_01.output");
}
public void testAlter_domain_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/alter_domain_02.sql", blgDir+"/domain/alter_domain_02.blg", "output/domain/alter_domain_02.output");
}
public void testAlter_domain_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/alter_domain_03.sql", blgDir+"/domain/alter_domain_03.blg", "output/domain/alter_domain_03.output");
}
public void testAlter_domain_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/alter_domain_04.sql", blgDir+"/domain/alter_domain_04.blg", "output/domain/alter_domain_04.output");
}
public void testAlter_domain_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/alter_domain_05.sql", blgDir+"/domain/alter_domain_05.blg", "output/domain/alter_domain_05.output");
}
public void testCreate_domain_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_01.sql", blgDir+"/domain/create_domain_01.blg", "output/domain/create_domain_01.output");
}
public void testCreate_domain_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_02.sql", blgDir+"/domain/create_domain_02.blg", "output/domain/create_domain_02.output");
}
public void testCreate_domain_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_03.sql", blgDir+"/domain/create_domain_03.blg", "output/domain/create_domain_03.output");
}
public void testCreate_domain_04() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_04.sql", blgDir+"/domain/create_domain_04.blg", "output/domain/create_domain_04.output");
}
public void testCreate_domain_05() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_05.sql", blgDir+"/domain/create_domain_05.blg", "output/domain/create_domain_05.output");
}
public void testCreate_domain_06() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_06.sql", blgDir+"/domain/create_domain_06.blg", "output/domain/create_domain_06.output");
}
public void testCreate_domain_07() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_07.sql", blgDir+"/domain/create_domain_07.blg", "output/domain/create_domain_07.output");
}
public void testCreate_domain_08() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_08.sql", blgDir+"/domain/create_domain_08.blg", "output/domain/create_domain_08.output");
}
public void testCreate_domain_09() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_09.sql", blgDir+"/domain/create_domain_09.blg", "output/domain/create_domain_09.output");
}
public void testCreate_domain_10() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_10.sql", blgDir+"/domain/create_domain_10.blg", "output/domain/create_domain_10.output");
}
public void testCreate_domain_11() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_11.sql", blgDir+"/domain/create_domain_11.blg", "output/domain/create_domain_11.output");
}
public void testCreate_domain_12() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_12.sql", blgDir+"/domain/create_domain_12.blg", "output/domain/create_domain_12.output");
}
public void testCreate_domain_13() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_13.sql", blgDir+"/domain/create_domain_13.blg", "output/domain/create_domain_13.output");
}
public void testCreate_domain_14() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_14.sql", blgDir+"/domain/create_domain_14.blg", "output/domain/create_domain_14.output");
}
public void testCreate_domain_15() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_15.sql", blgDir+"/domain/create_domain_15.blg", "output/domain/create_domain_15.output");
}
public void testCreate_domain_16() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_16.sql", blgDir+"/domain/create_domain_16.blg", "output/domain/create_domain_16.output");
}
public void testCreate_domain_17() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_17.sql", blgDir+"/domain/create_domain_17.blg", "output/domain/create_domain_17.output");
}
public void testCreate_domain_18() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_18.sql", blgDir+"/domain/create_domain_18.blg", "output/domain/create_domain_18.output");
}
public void testCreate_domain_19() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_19.sql", blgDir+"/domain/create_domain_19.blg", "output/domain/create_domain_19.output");
}
public void testCreate_domain_20() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_20.sql", blgDir+"/domain/create_domain_20.blg", "output/domain/create_domain_20.output");
}
public void testCreate_domain_21() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_21.sql", blgDir+"/domain/create_domain_21.blg", "output/domain/create_domain_21.output");
}
public void testCreate_domain_22() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_22.sql", blgDir+"/domain/create_domain_22.blg", "output/domain/create_domain_22.output");
}
public void testCreate_domain_23() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_23.sql", blgDir+"/domain/create_domain_23.blg", "output/domain/create_domain_23.output");
}
public void testCreate_domain_24() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_24.sql", blgDir+"/domain/create_domain_24.blg", "output/domain/create_domain_24.output");
}
public void testCreate_domain_25() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_25.sql", blgDir+"/domain/create_domain_25.blg", "output/domain/create_domain_25.output");
}
public void testCreate_domain_26() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_26.sql", blgDir+"/domain/create_domain_26.blg", "output/domain/create_domain_26.output");
}
public void testCreate_domain_27() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_27.sql", blgDir+"/domain/create_domain_27.blg", "output/domain/create_domain_27.output");
}
public void testCreate_domain_28() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_28.sql", blgDir+"/domain/create_domain_28.blg", "output/domain/create_domain_28.output");
}
public void testCreate_domain_29() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_29.sql", blgDir+"/domain/create_domain_29.blg", "output/domain/create_domain_29.output");
}
public void testCreate_domain_30() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_30.sql", blgDir+"/domain/create_domain_30.blg", "output/domain/create_domain_30.output");
}
public void testCreate_domain_31() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_31.sql", blgDir+"/domain/create_domain_31.blg", "output/domain/create_domain_31.output");
}
public void testCreate_domain_32() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_32.sql", blgDir+"/domain/create_domain_32.blg", "output/domain/create_domain_32.output");
}
public void testCreate_domain_33() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_33.sql", blgDir+"/domain/create_domain_33.blg", "output/domain/create_domain_33.output");
}
public void testCreate_domain_34() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_34.sql", blgDir+"/domain/create_domain_34.blg", "output/domain/create_domain_34.output");
}
public void testCreate_domain_35() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_35.sql", blgDir+"/domain/create_domain_35.blg", "output/domain/create_domain_35.output");
}
public void testCreate_domain_36() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_36.sql", blgDir+"/domain/create_domain_36.blg", "output/domain/create_domain_36.output");
}
public void testCreate_domain_37() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_37.sql", blgDir+"/domain/create_domain_37.blg", "output/domain/create_domain_37.output");
}
public void testCreate_domain_38() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_38.sql", blgDir+"/domain/create_domain_38.blg", "output/domain/create_domain_38.output");
}
public void testCreate_domain_39() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_39.sql", blgDir+"/domain/create_domain_39.blg", "output/domain/create_domain_39.output");
}
public void testCreate_domain_40() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_40.sql", blgDir+"/domain/create_domain_40.blg", "output/domain/create_domain_40.output");
}
public void testCreate_domain_41() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_41.sql", blgDir+"/domain/create_domain_41.blg", "output/domain/create_domain_41.output");
}
public void testCreate_domain_42() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/create_domain_42.sql", blgDir+"/domain/create_domain_42.blg", "output/domain/create_domain_42.output");
}
public void testDrop_domain_01() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/drop_domain_01.sql", blgDir+"/domain/drop_domain_01.blg", "output/domain/drop_domain_01.output");
}
public void testDrop_domain_02() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/drop_domain_02.sql", blgDir+"/domain/drop_domain_02.blg", "output/domain/drop_domain_02.output");
}
public void testDrop_domain_03() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/domain/drop_domain_03.sql", blgDir+"/domain/drop_domain_03.blg", "output/domain/drop_domain_03.output");
}
}
