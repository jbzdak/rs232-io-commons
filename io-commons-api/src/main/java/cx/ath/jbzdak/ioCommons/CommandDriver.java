package cx.ath.jbzdak.ioCommons;

import java.io.IOException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public interface CommandDriver {

   public String executeCommand(Command command) throws IOException, InterruptedException;

   public void dispose();
}
