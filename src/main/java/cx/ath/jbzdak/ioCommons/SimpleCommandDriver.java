package cx.ath.jbzdak.ioCommons;

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

   private final ResponseReader responseReaderForNoInput;

   private final ReentrantLock enginesLock = new ReentrantLock(false);

   private final Condition waitCondition = enginesLock.newCondition();

   public SimpleCommandDriver(Port port, ResponseReader reader) {
      this(port, reader, null);
   }

   public SimpleCommandDriver(Port port, ResponseReader responseReader, ResponseReader responseReaderForNoInput) {
      this.port = port;
      this.responseReader = responseReader;
      this.responseReaderForNoInput = responseReaderForNoInput;
   }

   private ResponseReader getApplicableReader(Command<?> command){
      if (command.hasCustomResponseReader()){
         return  command.getCustomResponseReader(port.getIn());
      }
      if (!command.isRecievesInput()){
         return responseReaderForNoInput;
      }
      return responseReader;
   }

   public <T> T executeCommand(Command<T> command) throws IOException, InterruptedException{
      enginesLock.lock();
      try{
         ResponseReader reader = getApplicableReader(command);
         if(reader != null){
            reader.startWatchingForInput();
         }
         port.writeToOutput(command.getCommand());
         waitCondition.await(command.getTimeout(), TimeUnit.MILLISECONDS);
         ByteBuffer contents= responseReader.readInput();
         contents.rewind();
         int ii;
         for(ii=0; contents.get()!=0; ii++);
         if(reader != null){
            String res = ASCII.decode(ByteBuffer.wrap(contents.array(),0,ii)).toString().trim();
            return command.pareseResult(res);
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
