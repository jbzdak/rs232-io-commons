package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public abstract class SuperObservableList<E> extends AbstractList<E> implements ObservableList<E>{

   public boolean add(E e) {
      add(size(), e);
      return  true;
   }

   public void add(int index, E element) {
      super.add(index, element);
      fireElementAdded(index);
   }

   public boolean addAll(Collection<? extends E> c) {
      return super.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends E> c) {
      return super.addAll(index, c);
   }

   public void clear() {
      List<E> copy = new ArrayList<E>(this);
      super.clear();
      for (int ii = 0; ii < copy.size(); ii++) {
         E e = copy.get(ii);
         fireElementRemoved(ii,e);
      }
   }

   public boolean contains(Object o) {
      return super.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return super.containsAll(c);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public int indexOf(Object o) {
      return super.indexOf(o);
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public Iterator<E> iterator() {
      return new Iterator<E>(){

         final Iterator<E> ii = iterator();

         @Override
         public boolean hasNext() {
            return ii.hasNext();
         }

         @Override
         public E next() {
            return ii.next();
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int lastIndexOf(Object o) {
      return super.lastIndexOf(o);
   }

   public ListIterator<E> listIterator() {
      return super.listIterator();
   }

   public ListIterator<E> listIterator(int index) {
      return super.listIterator(index);
   }

   public E remove(int index) {
      E removed =  super.remove(index);
      if(removed==null) return null;
      fireElementRemoved(index, removed);
      return removed;
   }

   public boolean remove(Object o) {
      int indexOf = indexOf(o);
      if(super.remove(o)){
         return false;
      }
      fireElementRemoved(indexOf, (E) o);
      return true;
   }

   public boolean removeAll(Collection<?> c) {
      Map<Integer,  E> indexes = new HashMap<Integer,E>(c.size());
      for(Object o : c){
         int index = indexOf(o);
         if(index!=-1){
            indexes.put(index, (E) o);
         }
      }
      if(indexes.isEmpty()){
         return false;
      }
      for (Map.Entry<Integer, E> entry : indexes.entrySet()) {
         super.remove(entry.getKey().intValue());
         fireElementRemoved(entry.getKey(), entry.getValue());
      }
      return true;
   }

   public boolean retainAll(Collection<?> c) {
      ArrayList<E> copy = new ArrayList<E>(this);
      if(!super.retainAll(c)){
         return false;
      }
      for (int ii = 0; ii < copy.size(); ii++) {
         E e = copy.get(ii);
         fireElementRemoved(ii,e);
      }
      for (int ii = 0; ii < size(); ii++) {
         E e =  get(ii);
         fireElementAdded(ii);
      }
      return true;
   }

   public E set(int index, E element) {
      E old = super.set(index, element);
      fireElementChanged(index, old);
      return old; 
   }
   
   public List<E> subList(int fromIndex, int toIndex) {
      return new WrappedObservableList<E>(super.subList(fromIndex, toIndex));
   }

   public Object[] toArray() {
      return super.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return super.toArray(a);
   }
   
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
   public void notifyElementChanged(int index) {
      fireElementChanged(index, null);
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
