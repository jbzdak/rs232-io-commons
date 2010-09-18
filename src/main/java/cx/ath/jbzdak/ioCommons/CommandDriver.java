package cx.ath.jbzdak.ioCommons;

import java.io.IOException;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public interface CommandDriver {

   public <T> T executeCommand(Command<T> command) throws IOException, InterruptedException;

   public void dispose();
}
