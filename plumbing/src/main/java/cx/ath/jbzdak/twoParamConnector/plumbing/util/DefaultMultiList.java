package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import cx.ath.jbzdak.twoParamConnector.api.Cumulative;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class DefaultMultiList<E extends Cumulative<? super E, E>, Tag extends Comparable> extends MultiList<E, Tag>{

   final Class<? extends List<E>> listClazz;

   final Method createZeroElementMethod;

   public DefaultMultiList(int listSize, Method createZeroElementMethod, Class<? extends List<E>> listClazz) {
      super(listSize);
      this.createZeroElementMethod = createZeroElementMethod;
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
            result.add((E)createZeroElementMethod.invoke(null));
         }
         return observable;
      } catch (Exception e){
         throw new RuntimeException(e);
      }
   }
}
