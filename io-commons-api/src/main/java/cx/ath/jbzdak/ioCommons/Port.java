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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by: Jacek Bzdak
 */
public abstract class Port {

   /**
    * Default value for dataBits setting ({@value}).
    */
   public static final int DEFAULT_DATA_BITS = 8;


   private static final Logger LOGGER = LoggerFactory.getLogger(Port.class);

   protected PortState portState = PortState.INITIAL;

   public abstract void open() throws PortException;

   public abstract void close() throws PortException;

   public abstract void writeToOutput(byte[] data) throws IOException;

   public abstract InputStream getIn();

   public PortState getState() {
      return portState;
   }

   protected void checkState(){
      if(!getState().equals(PortState.OPEN)){
         throw new IllegalStateException("Illegal port state. Port should be OPEN for following operation. It is: " + getState());
      }
   }

   protected void checkState(PortState desiredState){
      if(!getState().equals(desiredState)){
         throw new IllegalStateException("Illegal port state. Port should be " + desiredState.name() + " for following operation. It is: " + getState());
      }
   }

   public void dispose() {
      try {
         close();
      } catch (PortException e) {
         LOGGER.error("Error while closing serial port ", e);
      }
   }
}
