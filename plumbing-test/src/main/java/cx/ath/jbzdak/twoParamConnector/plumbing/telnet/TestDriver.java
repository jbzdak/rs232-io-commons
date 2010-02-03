package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;
import cx.ath.jbzdak.twoParamConnector.api.LinearFun;
import cx.ath.jbzdak.twoParamConnector.api.SilgleDimFunAcceptor;
import cx.ath.jbzdak.twoParamConnector.api.VonNeumannRandom;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.test.TPDState;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 2, 2010
 */
public class TestDriver extends TwoParametricDriver {

   private static final Logger LOGGER = LoggerFactory.getLogger(TestDriver.class);

   private TPDState state = TPDState.NO_STATE;

   final int channelNo;

   final int firstChannel;

   final Random rnd = new Random();

   final VonNeumannRandom calibrate1Rand;

   final VonNeumannRandom calibrate2Rand;

   public TestDriver() {
      super();
      channelNo = ConfigurationImpl.get().getLastChannel() -  ConfigurationImpl.get().getFirstChannel();
      firstChannel = ConfigurationImpl.get().getFirstChannel();
      calibrate1Rand = new VonNeumannRandom(new SilgleDimFunAcceptor(new LinearFun(1,0)), rnd);
      calibrate2Rand = new VonNeumannRandom(new SilgleDimFunAcceptor(new LinearFun(1, firstChannel + channelNo)), rnd);
   }

   @Override
   public void dispose() {
      synchronized (this){
         try {
            wait(3000);
         } catch (InterruptedException e) {
            LOGGER.warn("", e);
         }
      }
   }

   @Override
   public Integer executeCommand(Command command) {
      String commandStr = Charset.forName("ASCII").decode(ByteBuffer.wrap(command.getCommand())).toString().trim();
      if(commandStr.startsWith("!")){
         if(state != TPDState.NO_STATE){
            throw new UnsupportedOperationException();
         }
         int state = Integer.parseInt(commandStr.substring(1));
         switch (state){
            case 1:
               this.state = TPDState.CALIBRATE_FIRST;
               break;
            case 2:
               this.state = TPDState.CALIBRATE_SECOND;
               break;
            case 5:
               this.state = TPDState.QUERY;
               break;
            default:
               throw new UnsupportedOperationException();
         }
         return null;
      }
      switch (state){
         case CALIBRATE_FIRST:
            return (int) calibrate1Rand.nextDouble()*channelNo + firstChannel;
         case CALIBRATE_SECOND:
            return (int) calibrate2Rand.nextDouble()*channelNo + firstChannel;
         case QUERY:
            return rnd.nextInt(channelNo/2)*2 + firstChannel;
         default:
            throw new UnsupportedOperationException();
      }
   }

   @Override
   public boolean isPortResponsive() {
      return true;
   }
}
