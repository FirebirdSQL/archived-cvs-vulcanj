package org.firebirdsql.isql;
import java.sql.SQLException;
import java.io.IOException;
import org.firebirdsql.isql.isqlBase.ISQLTestBase;
public class TestVulcan extends ISQLTestBase{
public TestVulcan (String name) {
super(name);
}
public static String blgDir = System.getProperty("test.blg", "blg-vulcan");
public void testAlter_numeric() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/alter_numeric.sql", blgDir+"/vulcan/alter_numeric.blg", "output/vulcan/alter_numeric.output");
}
public void testAutoCommit() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/autoCommit.sql", blgDir+"/vulcan/autoCommit.blg", "output/vulcan/autoCommit.output");
}
public void testAvg_tests() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/avg_tests.sql", blgDir+"/vulcan/avg_tests.blg", "output/vulcan/avg_tests.output");
}
public void testBig_index() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/big_index.sql", blgDir+"/vulcan/big_index.blg", "output/vulcan/big_index.output");
}
public void testCase() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/case.sql", blgDir+"/vulcan/case.blg", "output/vulcan/case.output");
}
public void testCheck_constraint_s0302147() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/check_constraint_s0302147.sql", blgDir+"/vulcan/check_constraint_s0302147.blg", "output/vulcan/check_constraint_s0302147.output");
}
public void testCoalesce_ascii() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/coalesce_ascii.sql", blgDir+"/vulcan/coalesce_ascii.blg", "output/vulcan/coalesce_ascii.output");
}
public void testColon() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/colon.sql", blgDir+"/vulcan/colon.blg", "output/vulcan/colon.output");
}
public void testDerived_table() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/derived_table.sql", blgDir+"/vulcan/derived_table.blg", "output/vulcan/derived_table.output");
}
public void testExecuteBlock() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/executeBlock.sql", blgDir+"/vulcan/executeBlock.blg", "output/vulcan/executeBlock.output");
}
public void testExecuteInto() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/executeInto.sql", blgDir+"/vulcan/executeInto.blg", "output/vulcan/executeInto.output");
}
public void testFactorial() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/factorial.sql", blgDir+"/vulcan/factorial.blg", "output/vulcan/factorial.output");
}
public void testFactorial_recursive() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/factorial_recursive.sql", blgDir+"/vulcan/factorial_recursive.blg", "output/vulcan/factorial_recursive.output");
}
public void testFblimits() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/fblimits.sql", blgDir+"/vulcan/fblimits.blg", "output/vulcan/fblimits.output");
}
public void testIndex_assert() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/index_assert.sql", blgDir+"/vulcan/index_assert.blg", "output/vulcan/index_assert.output");
}
public void testKeyword() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/keyword.sql", blgDir+"/vulcan/keyword.blg", "output/vulcan/keyword.output");
}
public void testLeftjoin() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/leftjoin.sql", blgDir+"/vulcan/leftjoin.blg", "output/vulcan/leftjoin.output");
}
public void testLong_in_s0324697() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/long_in_s0324697.sql", blgDir+"/vulcan/long_in_s0324697.blg", "output/vulcan/long_in_s0324697.output");
}
public void testLong_literal() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/long_literal.sql", blgDir+"/vulcan/long_literal.blg", "output/vulcan/long_literal.output");
}
public void testPortal_sp() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/portal_sp.sql", blgDir+"/vulcan/portal_sp.blg", "output/vulcan/portal_sp.output");
}
public void testPrimary_key() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/primary_key.sql", blgDir+"/vulcan/primary_key.blg", "output/vulcan/primary_key.output");
}
public void testQuote_keywords() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/quote_keywords.sql", blgDir+"/vulcan/quote_keywords.blg", "output/vulcan/quote_keywords.output");
}
public void testSequence_limit() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/sequence_limit.sql", blgDir+"/vulcan/sequence_limit.blg", "output/vulcan/sequence_limit.output");
}
public void testSet_tran() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/set_tran.sql", blgDir+"/vulcan/set_tran.blg", "output/vulcan/set_tran.output");
}
public void testSp_null() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/sp_null.sql", blgDir+"/vulcan/sp_null.blg", "output/vulcan/sp_null.output");
}
public void testSubstring_bug() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/substring_bug.sql", blgDir+"/vulcan/substring_bug.blg", "output/vulcan/substring_bug.output");
}
public void testUnassigned_code() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/unassigned_code.sql", blgDir+"/vulcan/unassigned_code.blg", "output/vulcan/unassigned_code.output");
}
public void testUnicode_hang() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/unicode_hang.sql", blgDir+"/vulcan/unicode_hang.blg", "output/vulcan/unicode_hang.output");
}
public void testUtf16_003() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/utf16_003.sql", blgDir+"/vulcan/utf16_003.blg", "output/vulcan/utf16_003.output");
}
public void testUtf32_003() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/utf32_003.sql", blgDir+"/vulcan/utf32_003.blg", "output/vulcan/utf32_003.output");
}
public void testXythos_sp() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/xythos_sp.sql", blgDir+"/vulcan/xythos_sp.blg", "output/vulcan/xythos_sp.output");
}
}
