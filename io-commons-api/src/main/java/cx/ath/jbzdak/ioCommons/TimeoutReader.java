/*
 * Copyright for Jacek Bzdak 2011.
 *
 *     This file is part of Serial ioCommons, utility library to do serial
 *     port communication using native APIs and JNA to bind them to java.
 *
 *     Serial ioCommons is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Serial ioCommons is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package cx.ath.jbzdak.ioCommons;

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

   public void startWatchingForInput() {
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
