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

package cx.ath.jbzdak.ioCommons.rxtx;

import cx.ath.jbzdak.ioCommons.*;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.Properties;

public class RxtxPort extends Port {

   protected static final Logger ENGINE_LOGGER = LoggerFactory.getLogger(RxtxPort.class);

   private final CommPortIdentifier identifier;
   private final SerialPort port;
   private final OutputStream out;
   private final InputStream in;
   private volatile PortState state = PortState.CLOSED;

   private static CommPortIdentifier makeIdentifier(String portName){
      try{
         return CommPortIdentifier.getPortIdentifier(portName);
      }catch(NoSuchPortException e){
         throw new RuntimeException(e);
      }
   }


   public RxtxPort(String identifier, int baudRate, int dataBits, StopBits stopBits, Parity parity, EnumSet<PortMode> modes) throws IllegalPortStateException {
      this(makeIdentifier(identifier), baudRate, dataBits, stopBits, parity, modes);
   }

   public RxtxPort(CommPortIdentifier identifier, int baudRate, int dataBits, StopBits stopBits, Parity parity, EnumSet<PortMode> modes) throws IllegalPortStateException {
      this(identifier, baudRate, dataBits, stopBits.getContents(), parity.getContents(), modes);
   }

   private RxtxPort(CommPortIdentifier identifier, int baudRate, int dataBits, int stopBits, int parity, EnumSet<PortMode> modes) throws IllegalPortStateException {
      super();
      this.identifier = identifier;
      if (identifier == null) {
         throw new IllegalStateException();
      }
      if (identifier.isCurrentlyOwned()) {
         throw new IllegalPortStateException(
                 "Port jest w danej chwili obsługiwany przez: '"
                         + identifier.getCurrentOwner() + "'");
      }
      state = PortState.OPENING;
      try {
         CommPort commPort = identifier.open(
                 RxtxPort.class.getSimpleName(), 2000);
         if (!(commPort instanceof SerialPort)) {
            throw new IllegalPortStateException(
                    "Nieobsługiwany typ portu! - nie jest to port szeregowy");
         }

         port = (SerialPort) commPort;
         for (PortMode mode : modes) {
            setMode(mode);
         }
         port.setSerialPortParams(baudRate, dataBits, stopBits, parity);
         in = port.getInputStream();
         out = port.getOutputStream();
      } catch (RuntimeException e) {
         state = PortState.CLOSED;
         throw e;
      } catch (Exception e){
         state = PortState.CLOSED;
         throw new RuntimeException(e);
      }
      state = PortState.OPEN;
   }

   @Override
   public void open() throws PortException {

   }

   private void setMode(PortMode mode) throws IllegalPortParameter {
      switch (mode){
         case DTR_ENABLED:
            port.setDTR(true);
            break;
         case RTS_ENABLED:
            port.setRTS(true);
            break;
         default:
            throw new IllegalPortParameter("RxtxPort can't handle mode " + mode);
      }
   }

   @Override
   public void close() throws PortException {
      state = PortState.CLOSING;
      try {
         port.close();
      } catch (RuntimeException e) {
         if (identifier.isCurrentlyOwned()) {
            state = PortState.OPEN;
         } else {
            state = PortState.CLOSED;
         }
         throw e;
      }
   }

   @Override
   public void writeToOutput(byte[] data) throws IOException {
      checkState();
      if(ENGINE_LOGGER.isTraceEnabled()){
         ENGINE_LOGGER.trace("OUT: " +  new String(data, Charset.forName("ASCII")));
      }
      out.write(data);
      out.flush();
   }


   @Override
   public InputStream getIn() {
      checkState();
      return in;
   }

   @Override
   public PortState getState() {
      return state;
   }


   @Override
   public void dispose() {
      try {
         in.close();
      } catch (IOException e) {
         ENGINE_LOGGER.warn("Exception while closing input stream for port {}", identifier);
         ENGINE_LOGGER.warn("Exception is", e);
      }
      try {
         out.close();
      } catch (IOException e) {
         ENGINE_LOGGER.warn("Exception while closing output stream for port {}", identifier);
         ENGINE_LOGGER.warn("Exception is", e);
      }
      port.close();
   }

}
