package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import cx.ath.jbzdak.common.CloseableThread;
import cx.ath.jbzdak.diesIrae.ioCommons.Command;
import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.api.Matrix;
import cx.ath.jbzdak.twoParamConnector.api.TwoParamFrontend;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;
import cx.ath.jbzdak.twoParamConnector.plumbing.ConfigurationImpl;
import cx.ath.jbzdak.twoParamConnector.plumbing.InternalDetectorFrontend;
import cx.ath.jbzdak.twoParamConnector.plumbing.TwoParamMatrix;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.DefaultMultiList;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.Factory;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.MultiList;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class TwoParametricFrontendImpl<T extends Comparable>
        extends AbstractFrontend<Matrix<CumulativeInteger>>
        implements TwoParamFrontend<T>, InternalDetectorFrontend<Matrix<CumulativeInteger>> {

   TwoParametricDriver driver = new TwoParametricDriver();

   final MultiList<CumulativeInteger, T> contents;

   volatile TwoParamMatrix<CumulativeInteger> currentResults;

   boolean acquiring;

   final Command queryGamma, queryBeta;

   final int resultsRefreshTime;

   private ReentrantLock startStopLock = new ReentrantLock();

   private DataRequestThread dataRequestThread;

   private volatile T tag;

   public TwoParametricFrontendImpl() {
      ConfigurationImpl configuration = ConfigurationImpl.get();
      int channelNum = configuration.lastChannel - configuration.firstChannel;
      contents = new DefaultMultiList<CumulativeInteger,T>(channelNum, new Factory<CumulativeInteger>() {
         @Override
         public CumulativeInteger make() {
            return new CumulativeInteger(0);
         }
      }, TwoParamMatrix.class);
      queryGamma = getCommandForDetectorId(configuration.typeToNumberMap.get(Detector.GAMMA));
      queryBeta = getCommandForDetectorId(configuration.typeToNumberMap.get(Detector.BETA));
      resultsRefreshTime = configuration.resultsRefreshTime;
   }

   private Command getCommandForDetectorId(int id){
      switch (id){
         case 1:
            return TwoParametricDriver.QUERY_FIRST;
         case 2:
            return TwoParametricDriver.QUERY_SECOND;
         default:
            throw new IllegalArgumentException();
      }
   }

   @Override
   public void dispose() {
      if(driver!=null){
         driver.dispose();
      }
   }

   @Override
   public void setDriver(TwoParametricDriver driver) {
      this.driver = driver;
   }

   @Override
   public void start() {
      startAcquisition(tag);
   }

   @Override
   public long getAcquisitionTime(TimeUnit timeUnit) {
      return 0; //TODO implement ;)
   }

   @Override
   public void startAcquisition(T t) {
      tag = t; 
      try {
         startStopLock.lock();
         currentResults = (TwoParamMatrix<CumulativeInteger>) contents.getForTag(t);
         dataRequestThread = new DataRequestThread();
         dataRequestThread.run();
         setAcquiring(true);
      } finally {
         startStopLock.unlock();
      }
   }

   @Override
   public void stop() {
      try {
         startStopLock.lock();
         dataRequestThread.shutdown();
      currentResults = null; 
      setAcquiring(false);
      } finally {
         startStopLock.unlock();
      }
   }

   @Override
   public boolean removeTag(T t) {
      return contents.removeTag(t);
   }

   @Override
   public Matrix<CumulativeInteger> getData() {
      return (Matrix<CumulativeInteger>) contents.getResults();
   }

   class DataRequestThread extends CloseableThread{

      long lastUpdate = -1;

      @Override
      protected void executeOneIteration() {
         Integer row = driver.executeCommand(queryGamma);
         Integer col = driver.executeCommand(queryBeta);
         if(row != null && col != null){
            CumulativeInteger integer = currentResults.get(row, col);
            CumulativeInteger copy = integer.copy();
            integer.increment();
            currentResults.notifyElementChanged(row, col, copy);
         }
         long currTime = System.currentTimeMillis();
         if((currTime - lastUpdate) >= resultsRefreshTime){
            lastUpdate = currTime;
            fireDataChanged((Matrix<CumulativeInteger>) contents.getResults());
         }
      }
   }
}
