package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 2, 2010
 */
public abstract class TwoParametricDriver {
   public static final Command QUERY_CALIBRATION = new QueryCalibration();
   public static final Command QUERY_FIRST = new QueryFirst();
   public static final Command QUERY_SECOND = new QuerySecond();
   public static final Command SET_DETECTION_MODE = new SetModeCommand(5);
   public static final Command SET_CALIBRATE_FIRST_MODE = new SetModeCommand(1);
   public static final Command SET_CALIBRATE_SECOND_MODE = new SetModeCommand(2);
//
//   public static TwoParametricDriver createDriver(){
//      return new TwoParamDriverImpl();
//   }

   public abstract Integer executeCommand(Command command);

   public abstract boolean isPortResponsive();

   public abstract void dispose();
}
