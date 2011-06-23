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

package cx.ath.jbzdak.linux.serial.jna.bindings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * This class implements input stream that is returned by {@link cx.ath.jbzdak.linux.serial.jna.bindings.TermiosPort#getIn()}.
 *
 *
 *
 * <h1>When {@link #read()} function returns</h1>
 *
 * <p>
 * It depends on values passed to {@link TermiosPort#setVmin(int)},  {@link TermiosPort#setVtime(int)} and {@link cx.ath.jbzdak.ioCommons.ConfigurablePort#setBlockingStrategy(cx.ath.jbzdak.ioCommons.BlockingStrategy)}.
 * </p>
 * <p>
 *    Howewer -- the internal {@link Termios#read(int, byte[], int)} method may block itself, if
 *    {@link TermiosPort#setVmin(int)} and  {@link TermiosPort#setVtime(int)} are set improperly.
 *
 *    Basic workings od these swithes is like that:
 *    <ul>
 *       <li>VMIN = 0, VTIME = 0 this is pure nonblocking behaviour. This will never block</li>
 *       <li>VMIN = 0, VTIME > 0 read method will return when after last byte there will be VTIME tenths of seconds of silence. This will block but will not block indefinetly.</li>
 *       <li>VMIN > 0, VTIME = 0 read method will return when after VMIN bytes are read. This may block indefinetly. </li>
 *       <li>VMIN > 0, VTIME > 0 read method will return when after VMIN bytes are read or VTIME has passsed. This will block indefinetly if there are no characters
 *       in the buffer (if there is at least one it will block for at most VTIME).</li>*
 *    </ul>
 *
 *    Reading one character at a time (VMIN = 0, VTIME = 0) makes this inefficient (read needs contenxt switch in Linux Kernel).
 * </p>                                                                                                                                *
 *
 * <p>
 *    Blocking strategies are enforced by setting default values for VMIN and VMAX.
 * </p>
 * Created by: Jacek Bzdak
 */
public class TermiosInputStream extends InputStream{
   private static final Logger LOGGER = LoggerFactory.getLogger(TermiosInputStream.class);

   protected final TermiosPort termiosPort;

   protected final ByteBuffer buffer;


   protected TermiosInputStream(TermiosPort termiosPort) {
      this.termiosPort = termiosPort;
      buffer = ByteBuffer.allocate(Math.max(36, termiosPort.configWrapper.vmin));
      buffer.limit(0);
   }

   @Override
   public int read() throws IOException {
      if(buffer.hasRemaining()){
         return buffer.get();
      }
      try {
         termiosPort.termios.read(termiosPort.fileDescriptor, buffer);
      } catch (NativeException e) {
         LOGGER.error("Error while reading from port", e);
         buffer.clear();
         return -1;
      }
      if(buffer.hasRemaining()){
         return buffer.get();
      }
      return -1;
   }
}
