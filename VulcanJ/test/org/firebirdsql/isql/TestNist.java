package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestNist extends ISQLTestBase{
public TestNist (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testCdr001() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr001.sql", blgDir+"/nist/cdr001.blg", "output/nist/cdr001.output");
}
public void testCdr002() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr002.sql", blgDir+"/nist/cdr002.blg", "output/nist/cdr002.output");
}
public void testCdr003() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr003.sql", blgDir+"/nist/cdr003.blg", "output/nist/cdr003.output");
}
public void testCdr004() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr004.sql", blgDir+"/nist/cdr004.blg", "output/nist/cdr004.output");
}
public void testCdr005() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr005.sql", blgDir+"/nist/cdr005.blg", "output/nist/cdr005.output");
}
public void testCdr006() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr006.sql", blgDir+"/nist/cdr006.blg", "output/nist/cdr006.output");
}
public void testCdr007() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr007.sql", blgDir+"/nist/cdr007.blg", "output/nist/cdr007.output");
}
public void testCdr008() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr008.sql", blgDir+"/nist/cdr008.blg", "output/nist/cdr008.output");
}
public void testCdr009() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr009.sql", blgDir+"/nist/cdr009.blg", "output/nist/cdr009.output");
}
public void testCdr010() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr010.sql", blgDir+"/nist/cdr010.blg", "output/nist/cdr010.output");
}
public void testCdr011() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr011.sql", blgDir+"/nist/cdr011.blg", "output/nist/cdr011.output");
}
public void testCdr012() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr012.sql", blgDir+"/nist/cdr012.blg", "output/nist/cdr012.output");
}
public void testCdr013() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr013.sql", blgDir+"/nist/cdr013.blg", "output/nist/cdr013.output");
}
public void testCdr014() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr014.sql", blgDir+"/nist/cdr014.blg", "output/nist/cdr014.output");
}
public void testCdr015() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr015.sql", blgDir+"/nist/cdr015.blg", "output/nist/cdr015.output");
}
public void testCdr016() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr016.sql", blgDir+"/nist/cdr016.blg", "output/nist/cdr016.output");
}
public void testCdr017() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr017.sql", blgDir+"/nist/cdr017.blg", "output/nist/cdr017.output");
}
public void testCdr018() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr018.sql", blgDir+"/nist/cdr018.blg", "output/nist/cdr018.output");
}
public void testCdr019() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr019.sql", blgDir+"/nist/cdr019.blg", "output/nist/cdr019.output");
}
public void testCdr021() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr021.sql", blgDir+"/nist/cdr021.blg", "output/nist/cdr021.output");
}
public void testCdr022() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr022.sql", blgDir+"/nist/cdr022.blg", "output/nist/cdr022.output");
}
public void testCdr023() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr023.sql", blgDir+"/nist/cdr023.blg", "output/nist/cdr023.output");
}
public void testCdr024() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr024.sql", blgDir+"/nist/cdr024.blg", "output/nist/cdr024.output");
}
public void testCdr025() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr025.sql", blgDir+"/nist/cdr025.blg", "output/nist/cdr025.output");
}
public void testCdr026() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr026.sql", blgDir+"/nist/cdr026.blg", "output/nist/cdr026.output");
}
public void testCdr028() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr028.sql", blgDir+"/nist/cdr028.blg", "output/nist/cdr028.output");
}
public void testCdr031() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/cdr031.sql", blgDir+"/nist/cdr031.blg", "output/nist/cdr031.output");
}
public void testDml001() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml001.sql", blgDir+"/nist/dml001.blg", "output/nist/dml001.output");
}
public void testDml004() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml004.sql", blgDir+"/nist/dml004.blg", "output/nist/dml004.output");
}
public void testDml005() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml005.sql", blgDir+"/nist/dml005.blg", "output/nist/dml005.output");
}
public void testDml008() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml008.sql", blgDir+"/nist/dml008.blg", "output/nist/dml008.output");
}
public void testDml009() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml009.sql", blgDir+"/nist/dml009.blg", "output/nist/dml009.output");
}
public void testDml010() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml010.sql", blgDir+"/nist/dml010.blg", "output/nist/dml010.output");
}
public void testDml011() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml011.sql", blgDir+"/nist/dml011.blg", "output/nist/dml011.output");
}
public void testDml012() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml012.sql", blgDir+"/nist/dml012.blg", "output/nist/dml012.output");
}
public void testDml013() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml013.sql", blgDir+"/nist/dml013.blg", "output/nist/dml013.output");
}
public void testDml014() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml014.sql", blgDir+"/nist/dml014.blg", "output/nist/dml014.output");
}
public void testDml015() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml015.sql", blgDir+"/nist/dml015.blg", "output/nist/dml015.output");
}
public void testDml016() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml016.sql", blgDir+"/nist/dml016.blg", "output/nist/dml016.output");
}
public void testDml018() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml018.sql", blgDir+"/nist/dml018.blg", "output/nist/dml018.output");
}
public void testDml019() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml019.sql", blgDir+"/nist/dml019.blg", "output/nist/dml019.output");
}
public void testDml020() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml020.sql", blgDir+"/nist/dml020.blg", "output/nist/dml020.output");
}
public void testDml021() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml021.sql", blgDir+"/nist/dml021.blg", "output/nist/dml021.output");
}
public void testDml022() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml022.sql", blgDir+"/nist/dml022.blg", "output/nist/dml022.output");
}
public void testDml023() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml023.sql", blgDir+"/nist/dml023.blg", "output/nist/dml023.output");
}
public void testDml023NullsAreEqualForDistinct() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml023NullsAreEqualForDistinct.sql", blgDir+"/nist/dml023NullsAreEqualForDistinct.blg", "output/nist/dml023NullsAreEqualForDistinct.output");
}
public void testDml023NullsSortTogetherInOrderBy() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml023NullsSortTogetherInOrderBy.sql", blgDir+"/nist/dml023NullsSortTogetherInOrderBy.blg", "output/nist/dml023NullsSortTogetherInOrderBy.output");
}
public void testDml024() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml024.sql", blgDir+"/nist/dml024.blg", "output/nist/dml024.output");
}
public void testDml025() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml025.sql", blgDir+"/nist/dml025.blg", "output/nist/dml025.output");
}
public void testDml026() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml026.sql", blgDir+"/nist/dml026.blg", "output/nist/dml026.output");
}
public void testDml027() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml027.sql", blgDir+"/nist/dml027.blg", "output/nist/dml027.output");
}
public void testDml029() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml029.sql", blgDir+"/nist/dml029.blg", "output/nist/dml029.output");
}
public void testDml03x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml03x.sql", blgDir+"/nist/dml03x.blg", "output/nist/dml03x.output");
}
public void testDml04x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml04x.sql", blgDir+"/nist/dml04x.blg", "output/nist/dml04x.output");
}
public void testDml05x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml05x.sql", blgDir+"/nist/dml05x.blg", "output/nist/dml05x.output");
}
public void testDml060() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml060.sql", blgDir+"/nist/dml060.blg", "output/nist/dml060.output");
}
public void testDml061() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml061.sql", blgDir+"/nist/dml061.blg", "output/nist/dml061.output");
}
public void testDml062() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml062.sql", blgDir+"/nist/dml062.blg", "output/nist/dml062.output");
}
public void testDml064() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml064.sql", blgDir+"/nist/dml064.blg", "output/nist/dml064.output");
}
public void testDml065() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml065.sql", blgDir+"/nist/dml065.blg", "output/nist/dml065.output");
}
public void testDml068() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml068.sql", blgDir+"/nist/dml068.blg", "output/nist/dml068.output");
}
public void testDml069() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml069.sql", blgDir+"/nist/dml069.blg", "output/nist/dml069.output");
}
public void testDml070() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml070.sql", blgDir+"/nist/dml070.blg", "output/nist/dml070.output");
}
public void testDml073() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml073.sql", blgDir+"/nist/dml073.blg", "output/nist/dml073.output");
}
public void testDml075() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml075.sql", blgDir+"/nist/dml075.blg", "output/nist/dml075.output");
}
public void testDml076() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml076.sql", blgDir+"/nist/dml076.blg", "output/nist/dml076.output");
}
public void testDml077() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml077.sql", blgDir+"/nist/dml077.blg", "output/nist/dml077.output");
}
public void testDml079() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml079.sql", blgDir+"/nist/dml079.blg", "output/nist/dml079.output");
}
public void testDml080() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml080.sql", blgDir+"/nist/dml080.blg", "output/nist/dml080.output");
}
public void testDml081() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml081.sql", blgDir+"/nist/dml081.blg", "output/nist/dml081.output");
}
public void testDml082() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml082.sql", blgDir+"/nist/dml082.blg", "output/nist/dml082.output");
}
public void testDml083() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml083.sql", blgDir+"/nist/dml083.blg", "output/nist/dml083.output");
}
public void testDml084() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml084.sql", blgDir+"/nist/dml084.blg", "output/nist/dml084.output");
}
public void testDml086() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml086.sql", blgDir+"/nist/dml086.blg", "output/nist/dml086.output");
}
public void testDml087() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml087.sql", blgDir+"/nist/dml087.blg", "output/nist/dml087.output");
}
public void testDml090() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml090.sql", blgDir+"/nist/dml090.blg", "output/nist/dml090.output");
}
public void testDml091() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml091.sql", blgDir+"/nist/dml091.blg", "output/nist/dml091.output");
}
public void testDml099() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml099.sql", blgDir+"/nist/dml099.blg", "output/nist/dml099.output");
}
public void testDml104() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml104.sql", blgDir+"/nist/dml104.blg", "output/nist/dml104.output");
}
public void testDml106() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml106.sql", blgDir+"/nist/dml106.blg", "output/nist/dml106.output");
}
public void testDml112() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml112.sql", blgDir+"/nist/dml112.blg", "output/nist/dml112.output");
}
public void testDml114() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml114.sql", blgDir+"/nist/dml114.blg", "output/nist/dml114.output");
}
public void testDml117() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml117.sql", blgDir+"/nist/dml117.blg", "output/nist/dml117.output");
}
public void testDml119() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml119.sql", blgDir+"/nist/dml119.blg", "output/nist/dml119.output");
}
public void testDml121() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml121.sql", blgDir+"/nist/dml121.blg", "output/nist/dml121.output");
}
public void testDml130() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml130.sql", blgDir+"/nist/dml130.blg", "output/nist/dml130.output");
}
public void testDml132() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml132.sql", blgDir+"/nist/dml132.blg", "output/nist/dml132.output");
}
public void testDml134() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml134.sql", blgDir+"/nist/dml134.blg", "output/nist/dml134.output");
}
public void testDml135() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml135.sql", blgDir+"/nist/dml135.blg", "output/nist/dml135.output");
}
public void testDml137() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml137.sql", blgDir+"/nist/dml137.blg", "output/nist/dml137.output");
}
public void testDml141() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml141.sql", blgDir+"/nist/dml141.blg", "output/nist/dml141.output");
}
public void testDml144() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml144.sql", blgDir+"/nist/dml144.blg", "output/nist/dml144.output");
}
public void testDml147() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml147.sql", blgDir+"/nist/dml147.blg", "output/nist/dml147.output");
}
public void testDml148() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml148.sql", blgDir+"/nist/dml148.blg", "output/nist/dml148.output");
}
public void testDml149() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml149.sql", blgDir+"/nist/dml149.blg", "output/nist/dml149.output");
}
public void testDml153() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml153.sql", blgDir+"/nist/dml153.blg", "output/nist/dml153.output");
}
public void testDml154() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml154.sql", blgDir+"/nist/dml154.blg", "output/nist/dml154.output");
}
public void testDml155() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml155.sql", blgDir+"/nist/dml155.blg", "output/nist/dml155.output");
}
public void testDml158() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml158.sql", blgDir+"/nist/dml158.blg", "output/nist/dml158.output");
}
public void testDml160() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml160.sql", blgDir+"/nist/dml160.blg", "output/nist/dml160.output");
}
public void testDml163() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml163.sql", blgDir+"/nist/dml163.blg", "output/nist/dml163.output");
}
public void testDml168() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml168.sql", blgDir+"/nist/dml168.blg", "output/nist/dml168.output");
}
public void testDml170() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml170.sql", blgDir+"/nist/dml170.blg", "output/nist/dml170.output");
}
public void testDml173() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml173.sql", blgDir+"/nist/dml173.blg", "output/nist/dml173.output");
}
public void testDml174() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml174.sql", blgDir+"/nist/dml174.blg", "output/nist/dml174.output");
}
public void testDml175() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml175.sql", blgDir+"/nist/dml175.blg", "output/nist/dml175.output");
}
public void testDml176() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml176.sql", blgDir+"/nist/dml176.blg", "output/nist/dml176.output");
}
public void testDml177() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml177.sql", blgDir+"/nist/dml177.blg", "output/nist/dml177.output");
}
public void testDml178() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml178.sql", blgDir+"/nist/dml178.blg", "output/nist/dml178.output");
}
public void testDml179() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml179.sql", blgDir+"/nist/dml179.blg", "output/nist/dml179.output");
}
public void testDml181() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml181.sql", blgDir+"/nist/dml181.blg", "output/nist/dml181.output");
}
public void testDml182() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml182.sql", blgDir+"/nist/dml182.blg", "output/nist/dml182.output");
}
public void testDml183() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/dml183.sql", blgDir+"/nist/dml183.blg", "output/nist/dml183.output");
}
public void testFlg006() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/flg006.sql", blgDir+"/nist/flg006.blg", "output/nist/flg006.output");
}
public void testFlg008() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/flg008.sql", blgDir+"/nist/flg008.blg", "output/nist/flg008.output");
}
public void testFlg009() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/flg009.sql", blgDir+"/nist/flg009.blg", "output/nist/flg009.output");
}
public void testFlg011() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/flg011.sql", blgDir+"/nist/flg011.blg", "output/nist/flg011.output");
}
public void testFlg012() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/flg012.sql", blgDir+"/nist/flg012.blg", "output/nist/flg012.output");
}
public void testSdl001() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl001.sql", blgDir+"/nist/sdl001.blg", "output/nist/sdl001.output");
}
public void testSdl008() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl008.sql", blgDir+"/nist/sdl008.blg", "output/nist/sdl008.output");
}
public void testSdl009() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl009.sql", blgDir+"/nist/sdl009.blg", "output/nist/sdl009.output");
}
public void testSdl010() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl010.sql", blgDir+"/nist/sdl010.blg", "output/nist/sdl010.output");
}
public void testSdl012() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl012.sql", blgDir+"/nist/sdl012.blg", "output/nist/sdl012.output");
}
public void testSdl013() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl013.sql", blgDir+"/nist/sdl013.blg", "output/nist/sdl013.output");
}
public void testSdl014() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl014.sql", blgDir+"/nist/sdl014.blg", "output/nist/sdl014.output");
}
public void testSdl015() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl015.sql", blgDir+"/nist/sdl015.blg", "output/nist/sdl015.output");
}
public void testSdl016() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl016.sql", blgDir+"/nist/sdl016.blg", "output/nist/sdl016.output");
}
public void testSdl017() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl017.sql", blgDir+"/nist/sdl017.blg", "output/nist/sdl017.output");
}
public void testSdl019() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl019.sql", blgDir+"/nist/sdl019.blg", "output/nist/sdl019.output");
}
public void testSdl020() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl020.sql", blgDir+"/nist/sdl020.blg", "output/nist/sdl020.output");
}
public void testSdl024() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl024.sql", blgDir+"/nist/sdl024.blg", "output/nist/sdl024.output");
}
public void testSdl025() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl025.sql", blgDir+"/nist/sdl025.blg", "output/nist/sdl025.output");
}
public void testSdl026() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl026.sql", blgDir+"/nist/sdl026.blg", "output/nist/sdl026.output");
}
public void testSdl027() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl027.sql", blgDir+"/nist/sdl027.blg", "output/nist/sdl027.output");
}
public void testSdl028() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/sdl028.sql", blgDir+"/nist/sdl028.blg", "output/nist/sdl028.output");
}
public void testXts70x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts70x.sql", blgDir+"/nist/xts70x.blg", "output/nist/xts70x.output");
}
public void testXts72x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts72x.sql", blgDir+"/nist/xts72x.blg", "output/nist/xts72x.output");
}
public void testXts73x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts73x.sql", blgDir+"/nist/xts73x.blg", "output/nist/xts73x.output");
}
public void testXts74x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts74x.sql", blgDir+"/nist/xts74x.blg", "output/nist/xts74x.output");
}
public void testXts75x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts75x.sql", blgDir+"/nist/xts75x.blg", "output/nist/xts75x.output");
}
public void testXts76x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts76x.sql", blgDir+"/nist/xts76x.blg", "output/nist/xts76x.output");
}
public void testXts77x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts77x.sql", blgDir+"/nist/xts77x.blg", "output/nist/xts77x.output");
}
public void testXts79x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/xts79x.sql", blgDir+"/nist/xts79x.blg", "output/nist/xts79x.output");
}
public void testYts75x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/yts75x.sql", blgDir+"/nist/yts75x.blg", "output/nist/yts75x.output");
}
public void testYts76x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/yts76x.sql", blgDir+"/nist/yts76x.blg", "output/nist/yts76x.output");
}
public void testYts77x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/yts77x.sql", blgDir+"/nist/yts77x.blg", "output/nist/yts77x.output");
}
public void testYts79x() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/yts79x.sql", blgDir+"/nist/yts79x.blg", "output/nist/yts79x.output");
}
public void testYts8xx() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/nist/yts8xx.sql", blgDir+"/nist/yts8xx.blg", "output/nist/yts8xx.output");
}
}
