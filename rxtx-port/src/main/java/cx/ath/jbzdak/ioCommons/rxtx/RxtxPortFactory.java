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

import cx.ath.jbzdak.common.properties.AbstractExtendedProperties;
import cx.ath.jbzdak.ioCommons.*;

import java.util.EnumSet;

/**
 * Created by: Jacek Bzdak
 */
public class RxtxPortFactory extends PortFactory{

   @Override
   public String getProviderName() {
      return "rxtx";
   }

   @Override
   public Port createPort(AbstractExtendedProperties providerProperties) throws PortException{
      String modesStr = providerProperties.getString("modes");
      EnumSet<PortMode> modes = EnumSet.noneOf(PortMode.class);
      if(modesStr!=null){
         for(String mode : modesStr.split(",")){
            modes.add(PortMode.valueOf(mode.trim()));
         }
      }
      RxtxPort port = new RxtxPort(
              providerProperties.getString("portName"),
              providerProperties.getInt("baudRate"),
              providerProperties.getInt("dataBits"),
              providerProperties.getEnum("stopBits", StopBits.class),
              providerProperties.getEnum("parity", Parity.class),
              modes);
      return port;
   }
}
