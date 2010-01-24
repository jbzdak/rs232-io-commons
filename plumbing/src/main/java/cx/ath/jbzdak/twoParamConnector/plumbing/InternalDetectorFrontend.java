package cx.ath.jbzdak.twoParamConnector.plumbing;

import cx.ath.jbzdak.twoParamConnector.api.DetectorFrontend;
import cx.ath.jbzdak.twoParamConnector.plumbing.telnet.TwoParametricDriver;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public interface InternalDetectorFrontend<DATA> extends DetectorFrontend<DATA>{

   void setDriver(TwoParametricDriver driver);

   void dispose();

}
