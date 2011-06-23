/*
 * Copyright for Jacek Bzdak 2011.
 *
 * This file is part of Serial ioCommons, utility library to do serial
 * port communication using native APIs and JNA to bind them to java.
 *
 * Serial ioCommons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Serial ioCommons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Serial ioCommons.  If not, see <http://www.gnu.org/licenses/>.
 */

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
            responseReader.startWatchingForInput();
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
