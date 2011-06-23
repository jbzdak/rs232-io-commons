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
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public enum Parity {
   @Default
   NONE(0),
   ODD(1),
   EVEN(2),
   MARK(3),
   SPACE(4);

   final int contents;

   Parity(int contents) {
      this.contents = contents;
   }

   public int getContents() {
      return contents;
   }
}
