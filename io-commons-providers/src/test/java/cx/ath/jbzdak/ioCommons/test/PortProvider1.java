package cx.ath.jbzdak.ioCommons.test;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import cx.ath.jbzdak.common.properties.AbstractExtendedProperties;
import cx.ath.jbzdak.ioCommons.Port;
import cx.ath.jbzdak.ioCommons.PortException;
import cx.ath.jbzdak.ioCommons.PortFactory;
import edu.umd.cs.findbugs.annotations.ExpectWarning;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Jacek Bzdak
 */
public class PortProvider1 extends PortFactory<Port> {

   public static boolean throwInConstructor = false;

   public PortProvider1() {
      if(throwInConstructor){
         throw new RuntimeException();
      }
   }

   @Override
   public String getProviderName() {
      return "pp1";
   }

   @Override
   public Port createPort(AbstractExtendedProperties providerProperties) throws PortException {
      return new Port1();
   }
}
