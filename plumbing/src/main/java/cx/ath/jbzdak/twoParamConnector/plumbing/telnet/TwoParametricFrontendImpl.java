package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.api.FrontendListener;
import cx.ath.jbzdak.twoParamConnector.api.Matrix;
import cx.ath.jbzdak.twoParamConnector.api.TwoParamFrontend;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;

import java.util.concurrent.TimeUnit;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class TwoParametricFrontendImpl<T extends Comparable> implements TwoParamFrontend<T>, InternalDetectorFrontend<Matrix<CumulativeInteger>> {


   @Override
   public void dispose() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void setDriver(TwoParametricDriver driver) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void start() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public long getAcquisitionTime(TimeUnit timeUnit) {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public boolean isAcquiring() {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void startAcquisition(T t) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void stopAcquisition() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public boolean removeTag(T t) {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public Matrix<CumulativeInteger> getData() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   @Override
   public void addFrontendListener(FrontendListener<Matrix<CumulativeInteger>> matrixFrontendListener) {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
