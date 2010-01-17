package cx.ath.jbzdak.diesIrae.ioCommons;

import java.nio.ByteBuffer;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public interface ResponseReader {

   public void startWatchingForInoput();

   public ByteBuffer readInput();

   public void dispose();
}
