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

import com.sun.xml.internal.ws.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by: Jacek Bzdak
 */
public class IoCommonsEntryPoint {

   private static final Logger LOGGER = LoggerFactory.getLogger(IoCommonsEntryPoint.class);

   public static final String ENTRY_POINT_RESOURCE_PATH = "/META-INF/service/cx.ath.jbzdak.ioCommons.IoCommonsEntryPoint";

   private static volatile IoCommonsEntryPoint ENTRY_POINT;

   public static Port createPort(Properties properties) throws PortException {
      if(ENTRY_POINT == null){
         synchronized (IoCommonsEntryPoint.class){
            if(ENTRY_POINT == null){
               ENTRY_POINT = new IoCommonsEntryPoint();
            }
         }
      }
      return ENTRY_POINT.createPortInternal(properties);
   }

   private final Map<String, PortFactory> portFactories;

   private final PortFactory defaultFactory;

   public IoCommonsEntryPoint() {
      Map<String, PortFactory> portFactory = new HashMap<String, PortFactory>();
      PortFactory defaultFactory = null;
      InputStream inputStream = getClass().getResourceAsStream(ENTRY_POINT_RESOURCE_PATH);
      if(inputStream == null){
         throw new ExceptionInInitializerError("Couldn't open file defining registered port factories.\n\nIf you dont know what this error is please include " +
                 "io-commons-providers module and it's dependencies in your project.");
      }
      String contents;
      try {
         contents = IOUtils.toString(inputStream);
      } catch (IOException e) {
         throw new ExceptionInInitializerError("IOException while reading registered port factories");
      }
      StringTokenizer tokenizer = new StringTokenizer(contents, "\n");
      while (tokenizer.hasMoreTokens()){
         PortFactory factory = registerFactory(portFactory, tokenizer.nextToken());
         if(defaultFactory == null){
            defaultFactory = factory;
         }
      }
      this.portFactories = Collections.unmodifiableMap(portFactory);
      this.defaultFactory = defaultFactory;
   }

   public Port createPortInternal(Properties properties) throws PortException {
      String providerName = properties.getProperty(PortFactory.PROVIDER_PROPERTY_NAME);
      PortFactory portFactory;
      if(providerName != null && !providerName.isEmpty()){
         portFactory = portFactories.get(providerName);
         if(portFactory == null){
            throw new PortCreationException("Couldn't find port provider '" + providerName + "'.");
         }
      }else {
         portFactory = defaultFactory;
      }
      return portFactory.createPort(properties);
   }

   private static PortFactory registerFactory( Map<String, PortFactory> registerTo, String tokenName){
      try {
         String className = tokenName.trim();
         Class clazz = Class.forName(className);
         Constructor constructor = clazz.getConstructor();
         boolean isAccessible = constructor.isAccessible();
         constructor.setAccessible(true);
         Object newInstance = constructor.newInstance();
         PortFactory newFactory = PortFactory.class.cast(newInstance);
         registerTo.put(newFactory.getProviderName(), newFactory);
         constructor.setAccessible(isAccessible);
         return newFactory;
      } catch (ClassNotFoundException e) {
         handlePortFactoryException(e, tokenName);
      } catch (InstantiationException e) {
         handlePortFactoryException(e, tokenName);
      } catch (IllegalAccessException e) {
         handlePortFactoryException(e, tokenName);
      } catch (NoSuchMethodException e) {
         handlePortFactoryException(e, tokenName);
      } catch (InvocationTargetException e) {
         handlePortFactoryException(e, tokenName);
      }
      return null;
   }

   private static void handlePortFactoryException(Exception e, String tokenName){
      LOGGER.error("Couldnt initialize portFactories from class '{}', exception reclieved", tokenName);
      LOGGER.error("Exception.", e);
   }

   @Deprecated
   /**
    * Used for testing purposes only. Most probably will not work in the way intended in multithread enviorment!
    */
   static void reset(){
      ENTRY_POINT = null;
   }


}
