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

/**
 * Created by: Jacek Bzdak
 */
public interface ConfigurablePort {

   void setPortName(String portName);

   void setDataBits(int dataBits);

   void setParity(Parity parity);

   void setStopBits(StopBits stopBits);

   void setFlowControl(FlowControl flowControl);

   void setPerformDefaultUpdates(boolean performDefaultUpdates);

//   void setSpeed(ConfigFlag speed);

   void setSpeed(int speed);

//   void setInputSpeed(ConfigFlag inputSpeed);

   void setOutputSpeed(int outputSpeed);

   void setInputSpeed(int inputSpeed);

   void setBlockingStrategy(BlockingStrategy block);

//   void setOutputSpeed(ConfigFlag outputSpeed);
}
