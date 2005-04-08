/* $Id$ */
/*
 * CopyOutputToBlg will copy all of the ISQL files in the VulcanJ/output
 * directory to a benchmark log directory specified by the user.
 *  
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CopyOutputToBlg {
	private static String blgDirectory = null;
	/*
	 * Old copy routine, discarded in favor of method below that uses java nio
	 */

	//	static void copy(File src, File dst) throws IOException {
	//		InputStream in = new FileInputStream(src);
	//		OutputStream out = new FileOutputStream(dst);
	//
	//		// Transfer bytes from in to out
	//		byte[] buf = new byte[1024];
	//		int len;
	//		while ((len = in.read(buf)) > 0) {
	//			out.write(buf, 0, len);
	//		}
	//		in.close();
	//		out.close();
	//	}
	/** Fast & simple file copy. */
	public static void copy(File source, File dest) throws IOException {
		FileChannel in = null, out = null;
		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();

			long size = in.size();
			MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0,
					size);

			out.write(buf);

		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	public static boolean deleteDir(File dir) {
		// to see if this directory is actually a symbolic link to a directory,
		// we want to get its canonical path - that is, we follow the link to
		// the file it's actually linked to
		File candir;
		try {
			candir = dir.getCanonicalFile();
		} catch (IOException e) {
			return false;
		}

		// a symbolic link has a different canonical path than its actual path,
		// unless it's a link to itself
		if (!candir.equals(dir.getAbsoluteFile())) {
			// this file is a symbolic link, and there's no reason for us to
			// follow it, because then we might be deleting something outside of
			// the directory we were told to delete
			return false;
		}

		// now we go through all of the files and subdirectories in the
		// directory and delete them one by one
		File[] files = candir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];

				// in case this directory is actually a symbolic link, or it's
				// empty, we want to try to delete the link before we try
				// anything
				boolean deleted = file.delete();
				if (!deleted) {
					// deleting the file failed, so maybe it's a non-empty
					// directory
					if (file.isDirectory())
						deleteDir(file);

					// otherwise, there's nothing else we can do
				}
			}
		}

		// return dir.delete();
		return true;
	}

	private static void doName(String s) {
		String[] dir = new java.io.File(s).list();
		for (int i = 0; i < dir.length; i++) {
			File f = new File(s + java.io.File.separator + dir[i]);
			if (f.isFile()
					&& f.getName().substring(f.getName().lastIndexOf("."))
							.equalsIgnoreCase(".output")) {
				String s2 = (f.getAbsolutePath().replaceFirst("output",
						blgDirectory)).replaceFirst("output", "blg");
				File dest = new File(s2);
				System.out.println(dest.getAbsoluteFile());

				try {
					copy(f, dest);
				} catch (IOException ioe) {
					// do nothing
				}
			} else if (f.isDirectory()) {
				if (!f.getName().equals("CVS")) {
					File nd = new File(s.replaceAll("output", blgDirectory)
							+ "/" + dir[i]);
					if (!nd.exists()) {
						System.out.println(s.replaceAll("output", blgDirectory)
								+ "/" + dir[i]);
						nd.mkdir();
					}
					doName(s + "/" + dir[i]);
				}
			} else {
				// System.out.println("huh? : " + f.getName());
			}
		}

		String fn;

	}

	public static void printHelp() {
		System.out
				.println("CopyOuputToBlg will copy all of the output files in your");
		System.out
				.println("VulcanJ/output directory to a benchmark log directory. ");
		System.out.println();
		System.out.println("Usage: ");
		System.out.println("\tjava -cp classes CopyOutputToBlg blgArea");
		System.out
				.println("\tblgArea is the directory to place the benchmark log files.");
	}

	public static boolean getYes() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line = br.readLine();
			if (line.startsWith("y")) {
				return true;
			}
		} catch (IOException ioe) {
			System.out.println("IO error trying to read from the console!");
			System.exit(1);
		}
		return false;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			printHelp();
			System.exit(0);
		}

		System.out
				.println("This will re-bench the Java Junit ISQL tests.\n"
						+ "Files with the suffix \".output\" will be copied from ./output\n"
						+ "to "
						+ args[0]
						+ " with a new suffix of \".blg\".\n"
						+ "Existing files in target directory will be deleted!!! \n"
						+ "\tAre you sure (y/n)?");
		if (getYes()==false){
			System.out.println("Exiting per user request.");
			System.exit(0);
		}

		File f = new File("./output");
		if (!f.exists()) {
			System.out.println("could not find ./output directory. Exiting.");
			System.exit(1);
		}

		File blgDir = new File(args[0]);
		System.out.println(blgDir.getAbsoluteFile());
		if (!blgDir.exists()) {
			System.out
					.println("Benchmark log directory doesn't exist. Create (y/n)?");
			if (getYes()) {
				if (!blgDir.mkdir()) {
					System.out
							.println("Could not create benchmark log directory. Exiting.");
					System.exit(1);
				}
			}
		} else {
			// would be nice to delete directory
			if (deleteDir(blgDir)) {
				System.out.println("directory deleted successfully");
			} else {
				System.out
						.println("could not delete benchmark log directory. Exiting.");
			}
		}
		blgDirectory = args[0];
		doName("output");
	}
}