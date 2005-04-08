/* $Id$ */
package org.firebirdsql.isql.isqlBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;

import junit.framework.TestCase;
public class ISQLTestBase extends TestCase {

	public ISQLTestBase(String arg0) {
		super(arg0);
	}

	protected void setUp() {
	}

	protected void tearDown() {
		File f = new File("fbtest.gdb");
		f.delete();
		f = new File("TEST.G00");
		f.delete();
	}

// skip over blank lines
	private String getNextLine(BufferedReader is) throws IOException {
		String s;
		
		do {
			s = is.readLine();
		} while ((s != null) && s.length()==0);
		return s; 
	}
	private boolean CompareTextFiles(String fileName1, String fileName2) {
		// note 4 back slashes to get 1 real backslash - regexp requirement
		String[] stringsToEat = {"C:\\\\WINNT\\\\SYSTEM32\\\\TEST.G00",
				"C:\\\\CYGWIN\\\\HOME\\\\VULCANJ\\\\TEST.G00",
				"Number of DB pages allocated = [0-9]*",
				"Transaction - oldest active = [0-9]*",
				"Transaction - oldest snapshot = [0-9]*",
				"Transaction - Next = [0-9]*", "TEST\\s*[0-9]:[0-9[a-f]]{3}.*",
				"Forced Writes are OFF",
				"Forced Writes are ON",
				"SQL> "};

		String String1, String2;
		boolean match = true;
		try {
			BufferedReader is1 = new BufferedReader(new FileReader(fileName1));
			BufferedReader is2 = new BufferedReader(new FileReader(fileName2));
			while ((String1 = getNextLine(is1)) != null) {
				String2 = getNextLine(is2);
				if (String1.equals("Dynamic SQL Error")) {

					// jim removed the "Dynamic SQL Error" string, along with
					// the newline in his Vulcan development. This means we have
					// to eat a line, plus the 1st character (a hyphen) from the
					// next line. It also means that a readLine() here
					// should always work.

					// If String2 == "DYNAMIC SQL ERROR", we must be running
					// Firebird1.5 to Firebird 1.5. String2 should never be DSE
					// when running against Vulcan
					if (!String2.equals("Dynamic SQL Error")) {
						String1 = getNextLine(is1); 
						String1 = String1.substring(1, String1.length());
					}
				}
				for (int i = 0; i < stringsToEat.length; i++) {
					String1 = String1.replaceAll(stringsToEat[i], ".");
					if (String2 != null)
						String2 = String2.replaceAll(stringsToEat[i], ".");
				}

				if (!String1.equals(String2)) {
					match = false;
				}
			}
			is1.close();
			is2.close();
		} catch (FileNotFoundException fnfe) {
			match = false;
			System.err
					.println("Error - could not find either the output file or the benchmark log file.");
			System.err.println("looking for files: " + fileName1 + ", "
					+ fileName2);
		} catch (IOException ioe) {
			match = false;
			System.err.println("Error in reading output/benchmark file(s).");
		}

		return match;
	}

	// If ISQL hangs, this routine will kill it.
	private class DestroyHungProcess extends Thread {
		Process proc;
		int delay;

		private DestroyHungProcess(Process proc, int delay) {
			this.proc = proc;
			this.delay = delay;
		}

		public void run() {
			try {
				sleep(delay);
				try {
					int status = proc.exitValue();
				} catch (IllegalThreadStateException itse) {
					System.out.println("Destroying hung process");
					proc.destroy();
				}
			} catch (InterruptedException ie) {
				// user killed the process?
			}
		}
	}

	private class StreamGobbler extends Thread {
		InputStream is;
		String type;
		String outputFileName;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
			this.outputFileName = null;
		}

		StreamGobbler(InputStream is, String type, String outputFileName) {
			this.is = is;
			this.type = type;
			this.outputFileName = outputFileName;
		}

		public void run() {
			try {
				PrintWriter pw = null;
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				if (outputFileName != null) {
					pw = new PrintWriter(new FileWriter(outputFileName, false));
				}
				while ((line = br.readLine()) != null) {
					System.out.println(type + ">" + line);
					if (pw != null) {
						pw.println(line);
					}
				}
				if (pw != null)
					pw.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void processISQLInput(String inputFile, String benchFile,
			String outputFile) throws InterruptedException, IOException {
		String diffFile = new String(outputFile.substring(0, outputFile
				.length() - 7)
				+ ".diff");

		File f = new File(outputFile);
		f.delete();

		// may not exist, but try to delete to be on safe side
		f = new File(diffFile);
		f.delete();

		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("isql -u sysdba -p masterkey -m -e -i "
				+ inputFile + " -o " + outputFile);

		// any error message?
		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(),
				"ERROR");

		// any output? In this case, save the output for later comparison.
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(),
				"OUTPUT");

		// kick them off
		errorGobbler.start();
		outputGobbler.start();

		errorGobbler.join(5000); // 5 seconds
		outputGobbler.join(5000);
		DestroyHungProcess dp = new DestroyHungProcess(proc, 5000);
		dp.start();
		int exitVal = proc.waitFor();
		assertTrue ("ISQL exited abnormally. It was either hung, or you passed it bad command line parameters.", ( (exitVal==0) || (exitVal==1))); 
//		assertEquals(
//				"ISQL exited abnormally. It was either hung, or you passed it bad command line parameters.",
//				0, exitVal);
		if (CompareTextFiles(benchFile, outputFile) == false) {
			// CompareFiles cf = new CompareFiles();
			//int compareResult = cf.compare(benchFile, outputFile);
			//URL diffFileURL = null;

			// if (compareResult != 0) {
			// put the diff files in the output subdirectory, but with .diff
			// suffix
			// diffFileURL = (new File (diffFile)).toURL();
			System.setOut(new PrintStream(new FileOutputStream(diffFile)));
			Diff d = new Diff();
			d.doDiff(benchFile, outputFile);
			assertEquals("ISQL output does not match the benchmark log file. "
					+ "See difference file: " + diffFile, 0, 1);
		}

	}
}

