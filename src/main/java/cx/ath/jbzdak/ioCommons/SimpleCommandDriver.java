package cx.ath.jbzdak.ioCommons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-27
 */
@ThreadSafe
public class SimpleCommandDriver implements CommandDriver{

   private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCommandDriver.class);

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
            reader.setInput(port.getIn());
         }
         byte[] command_bytes = command.getCommand();
         if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Writing command xxx'{}'", ASCII.decode(ByteBuffer.wrap(command_bytes)));
         }
         port.writeToOutput(command_bytes);
//         LOGGER.debug("AFTER WRITE");
         waitCondition.await(command.getTimeout(), TimeUnit.MILLISECONDS);
         if(reader == null){
//            LOGGER.debug("Command does not take response");
            return null;
         }
//         LOGGER.debug("Response reader {}", reader);
         long wait = 0;
         if(LOGGER.isDebugEnabled()){
            wait = System.currentTimeMillis();
         }
         ByteBuffer contents= reader.readInput();
         contents.rewind();
         if(LOGGER.isDebugEnabled()){
            wait = wait - System.currentTimeMillis();
//            LOGGER.debug("Waited {}ms for resultsFromPort", wait);
         }
         int ii;
         for(ii=0; contents.get()!=0; ii++);
         String res = ASCII.decode(ByteBuffer.wrap(contents.array(),0,ii)).toString().trim();
         LOGGER.debug("Read response {} ", res);
         return command.pareseResult(res);
      }finally {
         enginesLock.unlock();
      }
   }

   public void dispose(){
      responseReader.dispose();
      port.dispose();
   }



}
