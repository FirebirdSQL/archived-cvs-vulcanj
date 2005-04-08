/* $Id$ */
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.firebirdsql.gds.GDSType;
import org.firebirdsql.management.FBManager;

/*
 * 
 * @author bioliv, Mar 25, 2005
 * 
 * If we are running in an embedded setup, we can verify that the required
 * pieces are present. This saves the tester from having to read the XML ouptut
 * files just to see that Jaybird isn't in the libpath, for example.
 *  
 */

public class CheckEmbedded extends Task {
	private FBManager fbManager;
	public void execute() {
		try {
			System.loadLibrary("jaybird");
			System.out.println("loaded jaybird library successfully");
			fbManager = new FBManager(GDSType.getType("EMBEDDED"));
			// fbManager.setServer(host);
			//fbManager.setPort(Integer.parseInt(port));
			fbManager.start();
			fbManager.stop();
			fbManager = null;
			System.out.println("firebird library ok");
		} catch (UnsatisfiedLinkError ule) {
			System.out.println(ule.getMessage());
			throw new BuildException("Jaybird not found. Build fails.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			throw new BuildException("Firebird library not found. Build fails.");
		}
	}
}

