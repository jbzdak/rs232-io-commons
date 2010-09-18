package cx.ath.jbzdak.ioCommons;

import java.io.InputStream;

public abstract class Command<T> {

	private final boolean recievesInput;

	private final int timeout;


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
         throw new UnsupportedOperationException(); 
      }
   }

   public boolean isRecievesInput() {
      return recievesInput;
   }

   public int getTimeout() {
      return timeout;
   }
}
