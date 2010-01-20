package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.twoParamConnector.api.Driver;
import cx.ath.jbzdak.twoParamConnector.api.DriverStateListener;
import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class DriverImpl implements Driver{

   final List<DriverStateListener> listeners = new CopyOnWriteArrayList<DriverStateListener>();

   TwoParametricState state = TwoParametricState.NO_STATE;
   

   @Override
   public void addDriverStateListener(DriverStateListener driverStateListener) {
      listeners.add(driverStateListener);
   }

   @Override
   public boolean removeDriverStateListener(DriverStateListener driverStateListener) {
      return listeners.remove(driverStateListener);
   }

   @Override
   public void changeState(TwoParametricState newState) {
      for(DriverStateListener listener : listeners){
         listener.driverStateWillChange(state, newState);
      }

   }

   private void sendNewStateMessage(){

   }

   @Override
   public Object getControlObject() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void dispose() {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
