package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.DriverImpl;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class ImplementationFrontendImpl extends ImplementationFrontend{

   ConfigurationImpl configuration = new ConfigurationImpl();

   @Override
   public Driver createDriver() {
      return new DriverImpl();
   }

   @Override
   public ConfigurationImpl getConfiguration() {
      return configuration;
   }
}
