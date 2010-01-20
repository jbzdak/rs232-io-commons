package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public interface DriverStateListener {

   void driverStateWillChange(TwoParametricState fromState, TwoParametricState newState);

   void driverStateChanged(TwoParametricState oldState, TwoParametricState newState);
}
