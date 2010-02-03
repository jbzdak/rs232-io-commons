package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.plumbing.TwoParamMatrix;
import cx.ath.jbzdak.twoParamConnector.plumbing.TwoParamMultiList;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 1, 2010
 */
public class MultiMatrix {
   MultiList<CumulativeInteger, String> list;

   @Before
   public void foo(){
    list =
           new TwoParamMultiList<String>(2,2);
   }

   @Test
   public void test(){
      TwoParamMatrix<CumulativeInteger> a = (TwoParamMatrix<CumulativeInteger>) list.getForTag("A");
      a.set(0,0,new CumulativeInteger(0));
      a.set(0,1,new CumulativeInteger(1));
      a.set(1,0,new CumulativeInteger(2));
      a.set(1,1,new CumulativeInteger(3));

      TwoParamMatrix<CumulativeInteger> b = (TwoParamMatrix<CumulativeInteger>) list.getForTag("B");
      b.set(0,0,new CumulativeInteger(0));
      b.set(0,1,new CumulativeInteger(1));
      b.set(1,0,new CumulativeInteger(2));
      b.set(1,1,new CumulativeInteger(3));

      TwoParamMatrix<CumulativeInteger> twoParamMatrix =
              (TwoParamMatrix<CumulativeInteger>) list.getResults();

      Assert.assertEquals(twoParamMatrix.get(0).intValue(), 0);
      Assert.assertEquals(twoParamMatrix.get(1).intValue(), 2);
      Assert.assertEquals(twoParamMatrix.get(2).intValue(), 4);
      Assert.assertEquals(twoParamMatrix.get(3).intValue(), 6);


   }

}
