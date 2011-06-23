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

import cx.ath.jbzdak.common.properties.AbstractExtendedProperties;
import cx.ath.jbzdak.common.properties.ExtendedProperties;
import cx.ath.jbzdak.common.properties.MapProperties;
import org.omg.CORBA.StringHolder;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by: Jacek Bzdak
 */
public abstract class PortFactory<P extends Port> {

   public static final String PROVIDER_PROPERTY_NAME = "provider";

   public abstract String getProviderName();

   public abstract P createPort(AbstractExtendedProperties providerProperties) throws PortException;

   /**
    * Utility methods that configures configPort using default properties from properties
    * @param configPort
    * @param properties
    */
   protected final void configurePort(ConfigurablePort configPort, AbstractExtendedProperties properties){
      configPort.setPortName(properties.getString("portName"));
      configPort.setDataBits(properties.getInt("dataBits"));
      configPort.setSpeed(properties.getInt("baudRate"));
      configPort.setFlowControl(properties.getEnum("flowControl", FlowControl.class));
      configPort.setParity(properties.getEnum("parity", Parity.class));
      configPort.setStopBits(properties.getEnum("stopBits", StopBits.class));
   }

   public P createPort(Properties properties) throws PortException{
      Map<String, String> map = new HashMap<String, String>();

      for (Map.Entry<Object, Object> entry : properties.entrySet()) {
         String key = entry.getKey().toString();
         if(!key.contains(".")){
            map.put(key, entry.getValue().toString());
         }
         String providerPrefix = getProviderName() + ".";
         int idx = providerPrefix.length() + 1;
         if(key.startsWith(providerPrefix)){
            map.put(key.substring(idx), entry.getValue().toString());
         }
      }

      return createPort(new MapProperties(map));

   }

}
