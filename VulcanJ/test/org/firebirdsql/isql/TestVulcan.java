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
public void testBig_index() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/big_index.sql", blgDir+"/vulcan/big_index.blg", "output/vulcan/big_index.output");
}
public void testCase() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/case.sql", blgDir+"/vulcan/case.blg", "output/vulcan/case.output");
}
public void testDerived_table() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/derived_table.sql", blgDir+"/vulcan/derived_table.blg", "output/vulcan/derived_table.output");
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
public void testIndex_assert() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/index_assert.sql", blgDir+"/vulcan/index_assert.blg", "output/vulcan/index_assert.output");
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
public void testUnassigned_code() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/unassigned_code.sql", blgDir+"/vulcan/unassigned_code.blg", "output/vulcan/unassigned_code.output");
}
public void testUtf16_003() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/utf16_003.sql", blgDir+"/vulcan/utf16_003.blg", "output/vulcan/utf16_003.output");
}
public void testUtf32_003() throws SQLException, InterruptedException, IOException {
processISQLInput ("ddl/vulcan/utf32_003.sql", blgDir+"/vulcan/utf32_003.blg", "output/vulcan/utf32_003.output");
}
}
