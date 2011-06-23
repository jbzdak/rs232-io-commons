package cx.ath.jbzdak.linux.serial.jna.bindings;

import cx.ath.jbzdak.ioCommons.AbstractConfigurablePort;
import cx.ath.jbzdak.ioCommons.BlockingStrategy;
import cx.ath.jbzdak.ioCommons.Port;
import junit.framework.AssertionFailedError;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This test needs a loopback device connected to appropriate port
 * Created by: Jacek Bzdak
 */
public class TermiosPortTests {

   static TermiosPortFactory termiosPortFactory;

   AbstractConfigurablePort port;

   @BeforeClass
   public static void setUpClass(){
      termiosPortFactory = new TermiosPortFactory();
   }

   @org.junit.Before
   public void setUp() throws Exception {
      Properties properties = new Properties();
      properties.load(getClass().getResourceAsStream("/termios-test.properties"));
      port = termiosPortFactory.createPort(properties);
   }


   @After
   public void tearDown() throws Exception {
      port.dispose();
   }


   @Test
   public void testOpen() throws Exception {
      port.open();
   }

   @Test
   public void testHelloLoop() throws Exception {
      port.open();
      String s = RandomStringUtils.randomAlphabetic(15);
      port.writeToOutput(s.getBytes());
      synchronized (""){
         "".wait(50);
      }
      String str = IOUtils.toString(port.getIn());
      Assert.assertEquals(s, str);
   }

//   @Test()
//   public void testBlocking() throws Exception {
//      BlockTester bt = new BlockTester();
//      bt.test();
//      port.close();
//   }

   @Test
   public void testBlocking2() throws Exception{
      port.setBlockingStrategy(BlockingStrategy.BLOCK);
      port.open();
      String message  = RandomStringUtils.randomAlphanumeric(15);
      port.writeToOutput(message.getBytes());
      byte [] bytes = new byte[message.length()];
      int read = port.getIn().read(bytes);
      Assert.assertEquals("We should read whole message in one batch if. This exceptption doesn't signify error in the tested port class, " +
              "rather it is an error in the test itself",read, message.length());
      Assert.assertEquals(message, new String(bytes));
   }

   @Test
   public void testBlocking3() throws Exception{
      testBlocking2();
      BlockTester tester = new BlockTester();
      tester.test();
   }



   class BlockTester{
      volatile boolean finishedBlock = false;

      volatile Exception exception;

      public void block() {
         try {
            port.getIn().read();
         } catch (Exception e) {
            exception = e;
         }
      }

      public void blockOnOtherThread(){
         Runnable r = new Runnable() {
            @Override
            public void run() {
               block();
            }
         };

         Thread t = new Thread(r);
         t.start();
      }

      public void test() throws Exception{
         blockOnOtherThread();
         synchronized (""){
            "".wait(6000);
            if(exception != null){
               throw new Exception(exception);
            }
            if(finishedBlock){
               throw new RuntimeException();
            }
         }
      }



   }
}
