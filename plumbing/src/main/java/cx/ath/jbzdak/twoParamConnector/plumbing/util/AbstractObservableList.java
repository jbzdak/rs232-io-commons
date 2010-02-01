package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
@NotThreadSafe
public abstract class AbstractObservableList<E> implements ObservableList<E>{

   final List<ListListener<E>> listeners = new ArrayList<ListListener<E>>();

   @Override
   public void addListListener(ListListener<E> listListener) {
      listeners.add(listListener);
   }

   @Override
   public boolean removeListListener(ListListener<E> listListener) {
      return listeners.remove(listListener);
   }

   @Override
   public void notifyElementChanged(int index, E oldVal) {
      fireElementChanged(index, oldVal);
   }

   protected void fireElementAdded(int i){
      E e = get(i);
      for(ListListener<E> l: listeners){
         l.elementAdded(this, e, i);
      }
   }

   protected void fireElementRemoved(int i, E e){
      for(ListListener<E> l: listeners){
         l.elementAdded(this, e, i);
      }
   }

   protected void fireElementChanged(int i, E oldElement){
      E newElement = get(i);
      for(ListListener<E> l : listeners){
         l.elementReplaced(this, newElement, oldElement, i);
      }
   }
}
