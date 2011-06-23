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

   public void startWatchingForInput() {

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

