package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.spectrometric.api.SpectrometricResult;
import cx.ath.jbzdak.spectrometric.util.MutableIntBufferResult;
import cx.ath.jbzdak.twoParamConnector.api.CalibrationFrontend;
import cx.ath.jbzdak.twoParamConnector.api.FrontendListener;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class DetectorCalibration implements CalibrationFrontend, InternalDetectorFrontend<SpectrometricResult> {

   final Detector detector;

   TwoParametricDriver driver;

   final SpectrometricResult result;

   public DetectorCalibration(Detector detector) {
      this.detector = detector;
      result = new MutableIntBufferResult(ConfigurationImpl.get().firstChannel, ConfigurationImpl.get().lastChannel);
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
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void dispose() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public SpectrometricResult getData() {
      return null;
   }

   @Override
   public void addFrontendListener(FrontendListener<SpectrometricResult> spectrometricResultFrontendListener) {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
