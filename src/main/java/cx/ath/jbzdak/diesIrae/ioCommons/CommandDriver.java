package cx.ath.jbzdak.diesIrae.ioCommons;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-27
 */
@ThreadSafe
public class CommandDriver {

   private static final Charset ASCII = Charset.forName("ASCII");

   private final Port port;

   private final ResponseReader inputBuffer;

   private final ReentrantLock enginesLock = new ReentrantLock(false);

   private final Condition waitCondition = enginesLock.newCondition();

   public CommandDriver(Port port, ResponseReader reader) {
      this.port = port;
      inputBuffer =reader;
   }


   public String executeCommand(Command command) throws IOException, InterruptedException{
      enginesLock.lock();
      try{
         if(command.isRecievesInput()){
            inputBuffer.startWatchingForInoput();
         }
         port.writeToOutput(command.getCommand());
         waitCondition.await(command.getTimeout(), TimeUnit.MILLISECONDS);
         ByteBuffer contents= inputBuffer.readInput();
         contents.rewind();
         int ii;
         for(ii=0; contents.get()!=0; ii++);

         if(command.isRecievesInput()){
           return ASCII.decode(ByteBuffer.wrap(contents.array(),0,ii)).toString().trim();
         }else{
            return null;
         }
      }finally {
         enginesLock.unlock();
      }
   }

   public void dispose(){
      inputBuffer.dispose();
      port.dispose();
   }



}
