package cx.ath.jbzdak.diesIrae.ioCommons;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: 2009-10-27
 */
public class TimeoutReader implements ResponseReader{

   private final int bufferLen;

   private InputStream in;

   private final ReentrantLock bufferLock = new ReentrantLock();

   private final ReentrantLock readLock = new ReentrantLock();

   private final BufferFillingThread bufferFillingThread = new BufferFillingThread();

   private ByteBuffer buffer;

   private volatile boolean watchForInput;

   public TimeoutReader(int bufferLen) {
      this.bufferLen = bufferLen;
   }

   @Override
   public void setInput(InputStream input) {
      try {
         bufferLock.lock();
         if(in!=null){
            throw new UnsupportedOperationException("Can't set inputStream twice, and this object already has input set");
         }
         in = input;
         buffer = ByteBuffer.allocate(bufferLen);
         bufferFillingThread.start();
      } finally {
         bufferLock.unlock();
      }
   }

   void setWatchForInput(boolean watchForInput) {
      this.watchForInput = watchForInput;
   }

   public void startWatchingForInoput() {
      setWatchForInput(true);
   }

   public ByteBuffer readInput(){
      readLock.lock();
      setWatchForInput(false);
      try{
         ByteBuffer newBuffer = ByteBuffer.allocate(bufferLen);
         ByteBuffer result;
         bufferLock.lock();
         try{
            result = buffer;
            buffer = newBuffer;
            return result;
         }finally {
            bufferLock.unlock();
         }
      }finally {
         readLock.unlock();
      }
   }

   public void dispose(){
      bufferFillingThread.stop = true;
      bufferFillingThread.interrupt();
      ReentrantLock reentrantLock = new ReentrantLock();
      Condition condition = reentrantLock.newCondition();
      reentrantLock.lock();
      try{
         while(true){
            if(!bufferFillingThread.runing){
               return;
            }
            try {
               condition.await(5, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
               //NOOP
            }
         }
      }finally {
         reentrantLock.unlock();
      }
   }

   class BufferFillingThread extends Thread{
      public volatile boolean stop = false;
      public volatile boolean runing = true;

      BufferFillingThread() {
         super("BufferFillingThread");
         setDaemon(true);
      }

      @Override
      public void run() {
         while(!stop){
            try{
               int read = in.read();
               if(!watchForInput){
                  continue;
               }
               if(read==-1){
                  continue;
               }
               if(!bufferLock.tryLock(10, TimeUnit.MICROSECONDS)){
                  continue;
               }
               try{
                  buffer.put((byte)read);
               }finally {
                  bufferLock.unlock();
               }
            }catch (InterruptedException e){
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            } catch (Exception e){
               e.printStackTrace();
            }
         }
         runing = false;
      }
   }
   
}
