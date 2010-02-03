package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.DriverImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.TestDriver;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.TwoParametricDriver;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class ImplementationFrontendImpl extends ImplementationFrontend{

   ConfigurationImpl configuration = new ConfigurationImpl();

   @Override
   public Configuration getConfiguration() {
      return configuration;
   }

   @Override
   public Driver createDriver() {
      return new DriverImpl(){
         @Override
         protected TwoParametricDriver createDriver() {
            return new TestDriver();
         }
      };
   }

}
