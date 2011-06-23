package cx.ath.jbzdak.ioCommons.test;

import cx.ath.jbzdak.ioCommons.IoCommonsEntryPoint;
import cx.ath.jbzdak.ioCommons.Port;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by: Jacek Bzdak
 */
public class TestProviders {

   protected Properties getByName(String name) throws IOException {
      Properties properties = new Properties();
      properties.load(getClass().getResourceAsStream("/" + name + ".properties"));
      return properties;
   }

   @After
   public void tearDown() throws Exception {
      PortProvider1.throwInConstructor  = false;
   }

   @Test
   public void testInitializes() throws Exception {
      IoCommonsEntryPoint.createPort(getByName("default"));
   }

   @Test
   public void testInitializesErrorProvider() throws Exception {
      PortProvider1.throwInConstructor = true;
      Port port = IoCommonsEntryPoint.createPort(getByName("default"));
      Assert.assertTrue(port instanceof Port1);

   }
}


