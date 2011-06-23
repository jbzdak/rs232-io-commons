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

import cx.ath.jbzdak.common.annotation.Default;

/**
 * These constants will necesarili be provider specific ;)
 *
 * Created by: Jacek Bzdak
 */
public enum BlockingStrategy {
   /**
    * Input stream should block if no characters are aviable.
    */
   BLOCK,
   /**
    * Input stream can wait some (short) time if no chars are in the input, it can also wait if there are arriving bytes,
    * but there is a chance some more data will arrive.
    *
    * If there are no chars and timeout passed input stream will return EOF.
    */
   @Default
   TIMEOUT_BLOCK,
   /**
    * There will be no blocking.
    *
    * If there are no chars input stream will return EOF.
    */
   NO_BLOCK
}
