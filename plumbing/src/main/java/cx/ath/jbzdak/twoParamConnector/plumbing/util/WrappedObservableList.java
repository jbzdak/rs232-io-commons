package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.*;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class WrappedObservableList<E> extends AbstractObservableList<E>{

   final List<E> wrapped;

   public WrappedObservableList(List<E> wrapped) {
      this.wrapped = wrapped;
   }

   public boolean add(E e) {
      wrapped.add(e);
      fireElementAdded(wrapped.size()-1);
      return true;
   }

   public void add(int index, E element) {
      wrapped.add(index, element);
   }

   public boolean addAll(Collection<? extends E> c) {
      return wrapped.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends E> c) {
      return wrapped.addAll(index, c);
   }

   public void clear() {
      List<E> copy = new ArrayList<E>(wrapped);
      wrapped.clear();
      for (int ii = 0; ii < copy.size(); ii++) {
         E e = copy.get(ii);
         fireElementRemoved(ii,e);
      }
   }

   public boolean contains(Object o) {
      return wrapped.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return wrapped.containsAll(c);
   }

   public E get(int index) {
      return wrapped.get(index);
   }

   public int hashCode() {
      return wrapped.hashCode();
   }

   public int indexOf(Object o) {
      return wrapped.indexOf(o);
   }

   public boolean isEmpty() {
      return wrapped.isEmpty();
   }

   public Iterator<E> iterator() {
      return new Iterator<E>(){

         final Iterator<E> ii = wrapped.iterator();

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
      return wrapped.lastIndexOf(o);
   }

   public ListIterator<E> listIterator() {
      return wrapped.listIterator();
   }

   public ListIterator<E> listIterator(int index) {
      return wrapped.listIterator(index);
   }

   public E remove(int index) {
      E removed =  wrapped.remove(index);
      if(removed==null) return null;
      fireElementRemoved(index, removed);
      return removed;
   }

   public boolean remove(Object o) {
      int indexOf = indexOf(o);
      if(wrapped.remove(o)){
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
         wrapped.remove(entry.getKey().intValue());
         fireElementRemoved(entry.getKey(), entry.getValue());
      }
      return true;
   }

   public boolean retainAll(Collection<?> c) {
      ArrayList<E> copy = new ArrayList<E>(wrapped);
      if(!wrapped.retainAll(c)){
         return false;
      }
      for (int ii = 0; ii < copy.size(); ii++) {
         E e = copy.get(ii);
         fireElementRemoved(ii,e);
      }
      for (int ii = 0; ii < wrapped.size(); ii++) {
         E e =  wrapped.get(ii);
         fireElementAdded(ii);
      }
      return true;
   }

   public E set(int index, E element) {
      E old = wrapped.set(index, element);
      fireElementChanged(index, old);
      return old; 
   }

   public int size() {
      return wrapped.size();
   }

   public List<E> subList(int fromIndex, int toIndex) {
      return new WrappedObservableList<E>(wrapped.subList(fromIndex, toIndex));
   }

   public Object[] toArray() {
      return wrapped.toArray();
   }

   public <T> T[] toArray(T[] a) {
      return wrapped.toArray(a);
   }
}
