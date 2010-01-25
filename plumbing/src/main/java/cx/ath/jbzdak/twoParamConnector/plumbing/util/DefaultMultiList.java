package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.List;

import cx.ath.jbzdak.twoParamConnector.api.Cumulative;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class DefaultMultiList<E extends Cumulative<? super E, E>, Tag extends Comparable> extends MultiList<E, Tag>{



   public DefaultMultiList(int listSize, Factory<E> factory, Class<? extends List> listClazz) {
      super(new Fact<E>(factory, listClazz, listSize), listSize);
   }

   private static class Fact<E> implements Factory<ObservableList>{
      final Class<? extends List> listClazz;

      final Factory<E> factory;

      final int listSize;

      private Fact(Factory<E> factory, Class<? extends List> listClazz, int listSize) {
         this.factory = factory;
         this.listClazz = listClazz;
         this.listSize = listSize;
      }

      @Override
      public ObservableList make() {
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
}
