package cx.ath.jbzdak.twoParamConnector.plumbing;

import java.util.List;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeInteger;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.DefaultMultiList;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.Factory;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 1, 2010
 */
public class TwoParamMultiList<Tag extends Comparable>
        extends DefaultMultiList<CumulativeInteger, Tag>{

   public TwoParamMultiList(final int rows,final  int cols) {
      super(rows * cols, new Factory<CumulativeInteger>() {
         @Override
         public CumulativeInteger make() {
            return new CumulativeInteger(0);
         }
      }, new Factory<List>() {
         @Override
         public List make() {
            return new TwoParamMatrix(cols, rows, CumulativeInteger.class);
         }
      });
   }
}
