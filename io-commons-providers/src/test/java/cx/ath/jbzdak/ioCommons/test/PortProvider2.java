package cx.ath.jbzdak.ioCommons.test;

import cx.ath.jbzdak.common.properties.AbstractExtendedProperties;
import cx.ath.jbzdak.ioCommons.Port;
import cx.ath.jbzdak.ioCommons.PortException;
import cx.ath.jbzdak.ioCommons.PortFactory;

/**
 * Created by: Jacek Bzdak
 */
public class PortProvider2 extends PortFactory<Port>{

   @Override
   public String getProviderName() {
      return "pp2";
   }

   @Override
   public Port createPort(AbstractExtendedProperties providerProperties) throws PortException {
      return new Port2();
   }
}
