package cx.ath.jbzdak.ioCommons;

import cx.ath.jbzdak.ioCommons.IoCommonsEntryPoint;
import cx.ath.jbzdak.ioCommons.Port;
import cx.ath.jbzdak.ioCommons.test.Port1;
import cx.ath.jbzdak.ioCommons.test.Port2;
import cx.ath.jbzdak.ioCommons.test.PortProvider1;
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
      IoCommonsEntryPoint.reset();
      PortProvider1.throwInConstructor  = false;
   }

   @Test
   public void testInitializes() throws Exception {
      IoCommonsEntryPoint.createPort(getByName("default"));
   }

   @Test
   public void testDefaultProvider() throws Exception {
      Port port = IoCommonsEntryPoint.createPort(getByName("default"));
      Assert.assertTrue(port instanceof Port1);
   }

   @Test
   public void testDefaultProviderError() throws Exception {
      PortProvider1.throwInConstructor = true;
      Port port = IoCommonsEntryPoint.createPort(getByName("default"));
      Assert.assertTrue(port instanceof Port2);
   }


   @Test
   public void testSpecificProvider() throws Exception {
      Port port = IoCommonsEntryPoint.createPort(getByName("pp2"));
      Assert.assertTrue(port instanceof Port2);
   }
}


