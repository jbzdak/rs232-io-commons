package cx.ath.jbzdak.ioCommons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public class NewLineReader implements ResponseReader{

   private static final Logger LOGGER = LoggerFactory.getLogger(NewLineReader.class);

   private final byte eolChar;

   private InputStream inputStream;

   public NewLineReader(byte eolChar) {
      this.eolChar = eolChar;
   }

   @Override
   public void setInput(InputStream input) {
      inputStream = input;
   }

   public void dispose() {
      try{
         inputStream.close();
      }catch (IOException e){
         LOGGER.warn("Error while closing input stream",e);
      }
   }

   public void startWatchingForInoput() {

   }

   public ByteBuffer readInput() {
      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      int read;
      while(true){
         try {
            read = inputStream.read();
            if(read==-1 || read==eolChar){
               break;
            }
            byteBuffer.put((byte)read);
         } catch (IOException e) {
            if(LOGGER.isDebugEnabled()){
               LOGGER.debug("Exception while reading from input", e);
            }else{
               LOGGER.warn("Exception while reading from input");
            }

         }
      }
      return byteBuffer;
   }
}

