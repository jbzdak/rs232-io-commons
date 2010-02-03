package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import java.util.concurrent.TimeUnit;

import cx.ath.jbzdak.common.PropertyChangeSupport;
import cx.ath.jbzdak.diesIrae.ioCommons.*;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class TwoParamDriverImpl extends TwoParametricDriver {

   private final PropertyChangeSupport support = new PropertyChangeSupport(this);

   final Port port;

   final CommandDriver simpleCommandDriver;

   private boolean portResponsive;

   TwoParamDriverImpl() {
      ConfigurationImpl configuration = ConfigurationImpl.get();
      port = Port.makeDriver(configuration.driverPropertes);
      simpleCommandDriver = new TimeoutCommandDriver(port, new NewLineReader((byte)'\r'), 1, TimeUnit.SECONDS);
   }

   @Override
   public Integer executeCommand(Command command){
      try {
         String result = simpleCommandDriver.executeCommand(command);
         setPortResponsive(true);
         if(result==null){
            return null;
         }else{
            return Integer.parseInt(result);
         }
      } catch (Exception e){
         setPortResponsive(false);
         return null;
      }
   }

   @Override
   public boolean isPortResponsive() {
      return portResponsive;
   }

   void setPortResponsive(boolean portResponsive) {
      boolean oldPortResponsive = this.portResponsive;
      this.portResponsive = portResponsive;
      support.firePropertyChange("portResponsive", oldPortResponsive, this.portResponsive);
   }

   @Override
   public void dispose() {
      port.dispose();
   }
}
