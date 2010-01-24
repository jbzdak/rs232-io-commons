package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.common.CloseableThread;
import cx.ath.jbzdak.spectrometric.api.SpectrometricResult;
import cx.ath.jbzdak.spectrometric.util.MutableIntBufferResult;
import cx.ath.jbzdak.twoParamConnector.api.CalibrationFrontend;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class DetectorCalibration extends AbstractFrontend<SpectrometricResult> implements CalibrationFrontend, InternalDetectorFrontend<SpectrometricResult> {

   final Detector detector;

   final int resultsRefreshTime;

   volatile TwoParametricDriver driver;

   volatile DataAcquisitionThread thread;

   final ReentrantLock startStopLock = new ReentrantLock();

   final MutableIntBufferResult result;


   public DetectorCalibration(Detector detector) {
      ConfigurationImpl configuration = ConfigurationImpl.get();
      this.detector = detector;
      result = new MutableIntBufferResult(ConfigurationImpl.get().firstChannel, ConfigurationImpl.get().lastChannel);
      resultsRefreshTime = configuration.resultsRefreshTime;
   }

   @Override
   public Detector getDetector() {
      return detector;
   }

   @Override
   public void setDriver(TwoParametricDriver driver) {
      this.driver = driver; 
   }

   @Override
   public void start() {
      try {
         startStopLock.lock();
         thread = new DataAcquisitionThread();
         thread.start();
         setAcquiring(true);
      } finally {
         startStopLock.unlock();
      }

   }

   @Override
   public void stop() {
      try {
         startStopLock.lock();
         thread.shutdown();
         thread = null;
         setAcquiring(false);
      } finally {
         startStopLock.unlock();
      }

   }

   @Override
   public void clearData() {
      result.createImmutableView();
   }

   @Override
   public void dispose() {
      if(thread!=null){
         thread.shutdown();
      }
      if(driver!=null){
         driver.dispose();
      }
   }

   @Override
   public SpectrometricResult getData() {
      return result.createImmutableView();
   }

   class DataAcquisitionThread extends CloseableThread{

      long lastUpdate = -1;

      @Override
      protected void executeOneIteration() {
         Integer channel = driver.executeCommand(TwoParametricDriver.QUERY_CALIBRATION);
         if(channel!=null){
           result.incrementChannel(channel);
         }
         long currentTime = System.currentTimeMillis();
         if(currentTime - lastUpdate >= resultsRefreshTime){
            fireDataChanged(getData());
            lastUpdate = currentTime;
         }
      }
   }


}
