package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.plumbing.TwoParamMatrix;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class TagResults {


   long time;

   TwoParamMatrix<CumulativeInteger> results;

   public void addPoint(int x, int y){
      results.get(x, y).increment();
      //results.notifyElementChanged(x,y);
   }


   
}
