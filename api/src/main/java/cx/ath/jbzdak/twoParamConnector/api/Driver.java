package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public interface Driver {

   public void addDriverStateListener(DriverStateListener driverStateListener);

   public boolean removeDriverStateListener(DriverStateListener driverStateListener);

   void changeState(TwoParametricState twoParametricState);

   Object getControlObject();

   void dispose();
}
