package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 25, 2010
 */
public class MultiListTest {

   MultiList<CumulativeInteger, String> list;

   @Before
   public void init(){
      list = new DefaultMultiList<CumulativeInteger, String>(5, new Factory<CumulativeInteger>() {
         @Override
         public CumulativeInteger make() {
            return new CumulativeInteger(0);
         }
      }, new Factory<List>() {
         @Override
         public List make() {
            ArrayList result = new ArrayList();
            for(int ii=0; ii < 5; ii++){
               result.add(null);
            }
            return result;
         }
      });
   }
   
   void fillList(int num, CumulativeInteger number, List<CumulativeInteger> list){
      for(int ii = 0; ii < num; ii++){
           list.set(ii, number.copy());
      }
   }
   
   @Test
   public void test1(){
      List<CumulativeInteger> l1 = list.getForTag("1");
      fillList(5, new CumulativeInteger(5), l1);
      List<CumulativeInteger> l2 = list.getForTag("2");
      fillList(5, new CumulativeInteger(2), l2);
      for (CumulativeInteger integer : list.getResults()) {
         Assert.assertEquals(integer.intValue(), 7);
      }
   }
}
