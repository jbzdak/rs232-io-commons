package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.plumbing.TwoParamMatrix;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 25, 2010
 */
public class TwoParamMatrixTest {

   TwoParamMatrix<CumulativeInteger> twoParamMatrix;

   @Before
   public void setUp(){
      twoParamMatrix = new TwoParamMatrix<CumulativeInteger>(2,2, CumulativeInteger.class);
   }

   @Test
   public void testFromArray(){
      twoParamMatrix.set(0, new CumulativeInteger(1));
      twoParamMatrix.set(1, new CumulativeInteger(2));
      twoParamMatrix.set(2, new CumulativeInteger(3));
      twoParamMatrix.set(3, new CumulativeInteger(4));

      Assert.assertEquals(twoParamMatrix.get(0,0).intValue(), 1);
      Assert.assertEquals(twoParamMatrix.get(0,1).intValue(), 2);
      Assert.assertEquals(twoParamMatrix.get(1,0).intValue(), 3);
      Assert.assertEquals(twoParamMatrix.get(1,1).intValue(), 4);
   }

   @Test
   public void testToArray(){
      twoParamMatrix.set(0,0, new CumulativeInteger(1));
      twoParamMatrix.set(0,1, new CumulativeInteger(2));
      twoParamMatrix.set(1,0, new CumulativeInteger(3));
      twoParamMatrix.set(1,1, new CumulativeInteger(4));

      Assert.assertEquals(twoParamMatrix.get(0).intValue(), 1);
      Assert.assertEquals(twoParamMatrix.get(1).intValue(), 2);
      Assert.assertEquals(twoParamMatrix.get(2).intValue(), 3);
      Assert.assertEquals(twoParamMatrix.get(3).intValue(), 4);
   }
}
