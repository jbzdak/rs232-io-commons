package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import cx.ath.jbzdak.twoParamConnector.api.Cumulative;

import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class DefaultMultiList<E extends Cumulative<? super E, E>, Tag extends Comparable> extends MultiList<E, Tag>{

   final Class<? extends List> listClazz;

   final Factory<E> factory;

   public DefaultMultiList(int listSize, Factory<E> factory, Class<? extends List> listClazz) {
      super(listSize);
      this.factory = factory;
      this.listClazz = listClazz;
   }

   @Override
   protected ObservableList<E> createList() {
      try {
         List<E> result = listClazz.newInstance();
         ObservableList<E> observable;
         if (result instanceof ObservableList) {
            observable = (ObservableList<E>) result;
         }else{
            observable = new WrappedObservableList<E>(result);
         }
         for(int ii=0; ii < listSize; ii++){
            result.add(factory.make());
         }
         return observable;
      } catch (Exception e){
         throw new RuntimeException(e);
      }
   }
}
