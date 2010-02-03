package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;
import cx.ath.jbzdak.twoParamConnector.api.Driver;
import cx.ath.jbzdak.twoParamConnector.api.DriverStateListener;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;
import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;

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
         listener.driverStateWillChange(oldState, newState);
      }
      TwoParametricDriver driver = createDriver();
      internalDetectorFrontend  = initializeState(newState, driver);
      internalDetectorFrontend.setDriver(driver);
      state =  newState;
      for(DriverStateListener listener : listeners){
         listener.driverStateChanged(oldState, newState);
      }
      internalDetectorFrontend.start();
   }

   protected TwoParametricDriver createDriver(){
      return  new TwoParamDriverImpl();
   }

   private InternalDetectorFrontend initializeState(TwoParametricState state, TwoParametricDriver driver){
      switch (state){
         case COINCIDENCE:
            driver.executeCommand(TwoParametricDriver.SET_DETECTION_MODE);
            return new TwoParametricFrontendImpl<String>();
         case CALIBRATE_BETA_CHANNEL:
            driver.executeCommand(getSetModeFor(Detector.BETA));
            return  new DetectorCalibration(Detector.BETA);
         case CALIBRATE_GAMMA_CHANNEL:
            driver.executeCommand(getSetModeFor(Detector.GAMMA));
            return new DetectorCalibration(Detector.GAMMA);
         default:
            throw new IllegalStateException();
      }
   }

   private Command getSetModeFor(Detector detector){
      Integer detectorNo = ConfigurationImpl.get().typeToNumberMap.get(detector);
      switch (detectorNo){
         case 1:
            return TwoParametricDriver.SET_CALIBRATE_FIRST_MODE;
         case 2:
            return TwoParametricDriver.SET_CALIBRATE_SECOND_MODE;
         default:
            throw new UnsupportedOperationException();
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
