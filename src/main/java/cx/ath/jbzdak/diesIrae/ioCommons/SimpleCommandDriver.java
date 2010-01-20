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
public class SimpleCommandDriver implements CommandDriver{

   private static final Charset ASCII = Charset.forName("ASCII");

   private final Port port;

   private final ResponseReader responseReader;

   private final ReentrantLock enginesLock = new ReentrantLock(false);

   private final Condition waitCondition = enginesLock.newCondition();

   public SimpleCommandDriver(Port port, ResponseReader reader) {
      this.port = port;
      responseReader = reader;
      reader.setInput(port.getIn());
   }


   public String executeCommand(Command command) throws IOException, InterruptedException{
      enginesLock.lock();
      try{
         if(command.isRecievesInput()){
            responseReader.startWatchingForInoput();
         }
         port.writeToOutput(command.getCommand());
         waitCondition.await(command.getTimeout(), TimeUnit.MILLISECONDS);
         ByteBuffer contents= responseReader.readInput();
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
      responseReader.dispose();
      port.dispose();
   }



}
