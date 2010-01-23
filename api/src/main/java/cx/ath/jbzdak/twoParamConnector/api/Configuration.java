package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public interface Configuration {

   void setPortName(String port);

   String getPortName();

   LinearFun getDefaultCalibration(Detector detector);

   LinearFun getCurrentCalibration(Detector detector);

   void setCurrentCalibration(LinearFun newCalibration, Detector detector);

}
