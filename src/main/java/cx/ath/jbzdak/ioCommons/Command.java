package cx.ath.jbzdak.ioCommons;

import java.io.InputStream;


/**
 *  Responsible for sending command to device and readint it's response.
 * @param <T>
 */
public abstract class Command<T> {

	private final boolean recievesInput;

	private final int timeout;


   /**
    *
    * @param recievesInput does this command reclieve input. If this is true one needs also to override {@link Command#pareseResult(String)}
    * @param timeout how much driver should wait initially before sending command and waiting for input.
    */
   protected Command(boolean recievesInput, int timeout) {
      this.timeout = timeout;
      this.recievesInput = recievesInput;
   }

   public boolean hasCustomResponseReader(){
      return false;
   }

   public ResponseReader getCustomResponseReader(InputStream inputStream) {
      return null;
   }

   public abstract byte[] getCommand();

   public T pareseResult(String s) {
      if (recievesInput){
         throw new AbstractMethodError();
      }else{
         return null;
      }
   }

   public boolean isRecievesInput() {
      return recievesInput;
   }

   public int getTimeout() {
      return timeout;
   }
}
