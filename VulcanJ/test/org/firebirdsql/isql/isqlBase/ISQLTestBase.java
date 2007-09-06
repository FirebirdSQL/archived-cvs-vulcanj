/* $Id$ */
package org.firebirdsql.isql.isqlBase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
		f = new File ("test.fdb");
		f.delete();
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

		File f = new File(outputFile);
		// check to see if output file's directory is present
		if (!(f.getParentFile().exists())) {
			f.getParentFile().mkdir();
		}

		// delete output file if it exists
		f.delete();

		// may not exist, but try to delete to be on safe side
		String diffFile = new String("diff/"
				+ f.getName().substring(0, f.getName().indexOf('.')) + ".diff");
		f = new File(diffFile);
		f.delete();

		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec("isql -u sysdba -p masterkey -m -m2 -e -i "
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
		assertTrue(
				"ISQL exited abnormally. It was either hung, or you passed it bad command line parameters.",
				((exitVal == 0) || (exitVal == 1)));
		System.setOut(new PrintStream(new FileOutputStream(diffFile)));
		Diff d = new Diff();
		int match = d.doDiff(benchFile, outputFile);
		System.out.close();
		
		assertTrue("ISQL output does not match the benchmark log file. "
				+ "See difference file: " + diffFile, match == 0);
		if (match==0) {
			// remove the diff file if files identical
			f = new File (diffFile);
			f.delete();
		}
	}
}

