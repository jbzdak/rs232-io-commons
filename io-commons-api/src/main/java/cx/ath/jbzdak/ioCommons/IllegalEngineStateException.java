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

public class IllegalEngineStateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalEngineStateException() {	}

	public IllegalEngineStateException(String message) {
		super(message);
	}

	public IllegalEngineStateException(Throwable cause) {
		super(cause);
	}

	public IllegalEngineStateException(String message, Throwable cause) {
		super(message, cause);
	}

}
