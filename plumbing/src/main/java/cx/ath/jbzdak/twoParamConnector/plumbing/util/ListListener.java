package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public interface ListListener<T> {

   /**
    *
    * @param list .
    * @param addedElement .
    * @param i element is at <code>get(i)</code>
    */
   void elementAdded(List<T> list, T addedElement, int i);

   /**
    *
    * @param list .
    * @param removedElement .
    * @param i element was at <code>i</code>
    */
   void elementRemoved(List<T> list, T removedElement, int i);

   /**
    *
    * @param list .
    * @param newElement .
    * @param oldElement .
    * @param i <code>newElement</code> is at <code>i</code>.
    */
   void elementReplaced(List<T> list, T newElement, T oldElement, int i);
}
