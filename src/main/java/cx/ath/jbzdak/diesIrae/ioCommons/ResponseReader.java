package cx.ath.jbzdak.diesIrae.ioCommons;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public interface ResponseReader {

   public void startWatchingForInoput();

   public ByteBuffer readInput();

   /**
    * Assume that you may call this method only once in livetime of this ResponseReaderS
    * @param input
    */
   public void setInput(InputStream input);

   public void dispose();
}
