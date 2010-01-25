package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cx.ath.jbzdak.twoParamConnector.api.Cumulative;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public abstract class MultiList<E extends Cumulative<? super E, E>, Tag extends Comparable>{

   private final Map<Tag, ObservableList<E>> values = new HashMap<Tag, ObservableList<E>>();

   private final List<E> results;

   protected final int listSize;

   /**
    * Creates list that contains {@link cx.ath.jbzdak.twoParamConnector.plumbing.util.MultiList#listSize} zeros.
    * @return created list
    */
   protected final Factory<ObservableList> listFactory;

   protected MultiList(Factory<ObservableList> listFactory, int listSize) {
      this.listFactory = listFactory;
      this.listSize = listSize;
      results = listFactory.make();
   }

   public List<E> getForTag(Tag tag){
      ObservableList<E> list = values.get(tag);
      if(list == null){
         list = listFactory.make();
         list.addListListener(new ListListener(tag));
         values.put(tag, list);
      }
      return list;
   }

   public boolean removeTag(Tag tag){
      ObservableList<E> list = values.remove(tag);
      if(list==null) return false;
      for (int ii = 0; ii < list.size(); ii++) {
         E result = results.get(ii).copy();
         result.substract(list.get(ii));
         results.set(ii, result);
      }
      return true;
   }

   public List<E> getResults() {
      return results;
   }

   private class ListListener implements cx.ath.jbzdak.twoParamConnector.plumbing.util.ListListener<E>{

      final Tag tag;

      private ListListener(Tag tag) {
         this.tag = tag;
      }

      @Override
      public void elementAdded(List<E> es, E addedElement, int i) {
         throw new UnsupportedOperationException("Cant add elements to this list");
      }

      @Override
      public void elementRemoved(List<E> es, E removedElement, int i) {
         throw new UnsupportedOperationException("Cant remove elements from this list");
      }

      @Override
      public void elementReplaced(List<E> es, E newElement, E oldElement, int i) {
         E difference = newElement.copy();
         difference.substract(oldElement);
         results.get(i).add(difference);
      }
   }

}
