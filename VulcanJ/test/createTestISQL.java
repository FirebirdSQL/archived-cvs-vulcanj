/* $Id$ */
/*
 * Author: bioliv Created on: Jul 26, 2004
 * 
 * This program will search the ./ddl directory and create a corresponding java
 * wrapper around all of the .SQL files found in the ./ddl directory. Then, the
 * .java files are automatically compiled by the build process. This allows the
 * whole process to be automated by junit.
 *  
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class createTestISQL {

	// code to append to the generated .java file
	static final String[] testISQLEndsWith = {"}"};

	// package for the generated .java file
	static final String testPackage = "package org.firebirdsql.isql;";

	static class OnlyDDL implements FilenameFilter {
		public boolean accept(File dir, String s) {
			if (s.endsWith(".sql"))
				return true;
			return false;
		}
	}

	public static String toTitleCase(String s) {
		String retString = "";

		boolean capNext = true; // capitalize 1st character
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '/' || s.charAt(i) == '.' || s.charAt(i) == '\\') {
				// skip over funny character, and set a flag to
				// capitalize the next char
				capNext = true;
			} else if (capNext) {
				capNext = false;
				String tempString = "";
				tempString += s.charAt(i);
				retString += tempString.toUpperCase();
			} else
				retString += s.charAt(i);
		}
		return retString;
	}
	private static void writeHeader(PrintWriter pw, String f) {
		pw.println(testPackage);
		pw.println("import java.sql.SQLException;");
		pw.println("import java.io.IOException;");
		pw.println("import org.firebirdsql.isql.isqlBase.ISQLTestBase;");
		pw.println("public class Test" + toTitleCase(f)
				+ " extends ISQLTestBase{");
		pw.println("public " + "Test" + toTitleCase(f) + " (String name) {");
		pw.println("super(name);");
		pw.println("}");
		pw.println("public static String blgDir = System.getProperty(\"test.blg\", \"blg-vulcan\");");
	}

	private static void doFile(PrintWriter pw, String f, String p,
			String testClass) {
		String fn = f.substring(0, f.indexOf(".sql"));
		pw
				.println("public void "
						+ testClass
						+ "() throws SQLException, InterruptedException, IOException {");
//		pw.println("processISQLInput (\"" + p + fn + ".sql\", \""
//				+ p.replaceFirst(".*ddl", "blg") + fn + ".blg\", \""
//				+ p.replaceFirst(".*ddl", "output") + fn + ".output\");");
		pw.println("processISQLInput (\"" + p + fn + ".sql\", "
				+ p.replaceFirst(".*ddl", "blgDir+\"") +  fn + ".blg\", \""
				+ p.replaceFirst(".*ddl", "output") + fn + ".output\");");
		pw.println("}");

	}

	private static void doName(String myDir) throws IOException {
		String[] dir = new java.io.File(myDir).list();
		boolean firstTime = true;
		PrintWriter myPw = null;
		for (int i = 0; i < dir.length; i++) {
			File f = new File(myDir + java.io.File.separator + dir[i]);
			String fileName = f.getName();
			System.out.println("fileName: " + fileName);
			if (!fileName.equals("CVS")) {
				if (f.isFile()
						&& fileName.substring(fileName.lastIndexOf("."))
								.equalsIgnoreCase(".sql")) {
					// there is a file with a .sql suffix, as in
					// ddl/avg/avg_01.sql.
					// we want to create a file test/isql/avg/Testavg.java
					String fileNoSuffix = fileName.substring(0, fileName
							.indexOf('.'));
					String parent = "";
					if (firstTime) {
						String outputDir = "test/org/firebirdsql/isql/";
						parent = myDir.substring((myDir.indexOf("ddl") + 3));
						parent = toTitleCase(parent);
						//					parent = parent.substring(parent.indexOf('/') + 1,
						//							parent.length());
						String javaFile = outputDir + "Test"
								+ toTitleCase(parent) + ".java";
						System.out.println("Creating javaFile: " + javaFile);
						myPw = new PrintWriter(new FileWriter(javaFile));
						writeHeader(myPw, parent);
						firstTime = false;
					}
					doFile(myPw, f.getName(), myDir + "/", "test"
							+ toTitleCase(fileNoSuffix));
				} else if (f.isDirectory()) {
					File nd = new File(myDir.replaceFirst("ddl", "output")
							+ "/" + dir[i]);
					if (!nd.exists()) {
						System.out.println(myDir.replaceFirst("ddl", "output")
								+ "/" + dir[i]);
						nd.mkdir();
					}
					nd = new File(myDir.replaceFirst("ddl", "test") + "/"
							+ dir[i]);
					//					if (!nd.exists()) {
					//						nd.mkdir();
					//					}
					doName(myDir + "/" + dir[i]);
				} else
					System.out.println("huh? : " + f.getName());
			}
			// if myPW != null, then we have an open file to close
		}
		if (myPw != null) {
			for (int j = 0; j < testISQLEndsWith.length; j++) {
				myPw.println(testISQLEndsWith[j]);
			}
			myPw.close();
		}
	}

	public static void main(String[] args) {
		// test for ./ddl directory
		File d1 = new File("./ddl");
		if (d1.exists() == false) {
			System.out.println("./ddl directory does not exist. Exiting.");
			System.exit(0);
		}

		try {
			System.out
					.println("This will re-create the Java Junit ISQL tests. Are you sure (y/n)?");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));

			try {
				String line = br.readLine();
				if (!line.startsWith("y")) {
					System.exit(0);
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read from the console!");
				System.exit(1);
			}

			doName("ddl");
			System.out.println("run completed. ");

		} catch (IOException ioe) {
			System.out.println("Could not create output file(s) testXXX.java");
			System.out.println(ioe.getMessage());
		}

	}
}