package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.common.PropertyChangeSupport;
import cx.ath.jbzdak.diesIrae.ioCommons.*;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class TwoParametricDriver {



   public static final Command QUERY_CALIBRATION = new QueryCalibration();

   public static final Command QUERY_FIRST = new QueryFirst();

   public static final Command QUERY_SECOND = new QuerySecond();

   public static final Command SET_DETECTION_MODE = new SetModeCommand(5);

   public static final Command SET_CALIBRATE_FIRST_MODE = new SetModeCommand(1);

   public static final Command SET_CALIBRATE_SECOND_MODE = new SetModeCommand(2);

   private final PropertyChangeSupport support = new PropertyChangeSupport(this);

   final Port port;

   final CommandDriver simpleCommandDriver;

   private boolean portResponsive;


   public TwoParametricDriver() {
      try {
         Properties properties = new Properties();
         properties.load(getClass().getResourceAsStream("driver.properties"));
         port = Port.makeDriver(properties);
         simpleCommandDriver = new TimeoutCommandDriver(port, new NewLineReader((byte)'\r'), 1, TimeUnit.SECONDS);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

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

   public boolean isPortResponsive() {
      return portResponsive;
   }

   void setPortResponsive(boolean portResponsive) {
      boolean oldPortResponsive = this.portResponsive;
      this.portResponsive = portResponsive;
      support.firePropertyChange("portResponsive", oldPortResponsive, this.portResponsive);
   }

   public void dispose() {
      port.dispose();
   }
}
