package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.twoParamConnector.api.Driver;
import cx.ath.jbzdak.twoParamConnector.api.DriverStateListener;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;
import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class DriverImpl implements Driver{

   final List<DriverStateListener> listeners = new CopyOnWriteArrayList<DriverStateListener>();

   TwoParametricState state = TwoParametricState.NO_STATE;

   InternalDetectorFrontend internalDetectorFrontend;

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
      TwoParametricState oldState = state;
      if(internalDetectorFrontend!=null){
         internalDetectorFrontend.dispose();
      }
      state = null;
      for(DriverStateListener listener : listeners){
         listener.driverStateWillChange(state, newState);
      }
      internalDetectorFrontend  = initializeState(newState);
      internalDetectorFrontend.setDriver(new TwoParametricDriver());
      state =  newState;
      for(DriverStateListener listener : listeners){
         listener.driverStateChanged(oldState, newState);
      }
      internalDetectorFrontend.start();
   }

   private InternalDetectorFrontend initializeState(TwoParametricState state){
      switch (state){
         case COINCIDENCE:
            return new TwoParametricFrontendImpl<String>();
         case CALIBRATE_BETA_CHANNEL:
            return  new DetectorCalibration(Detector.BETA);
         case CALIBRATE_GAMMA_CHANNEL:
            return new DetectorCalibration(Detector.GAMMA);
         default:
            throw new IllegalStateException();
      }
   }

   @Override
   public Object getControlObject() {
      return internalDetectorFrontend;
   }

   @Override
   public void dispose() {
      if(internalDetectorFrontend!=null){
         internalDetectorFrontend.dispose();
      }
   }
}
