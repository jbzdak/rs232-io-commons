package cx.ath.jbzdak.twoParamConnector.api;

import cx.ath.jbzdak.spectrometric.api.SpectrometricResult;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public interface CalibrationFrontend extends DetectorFrontend<SpectrometricResult>{

   int getDetectorNumber();

}
