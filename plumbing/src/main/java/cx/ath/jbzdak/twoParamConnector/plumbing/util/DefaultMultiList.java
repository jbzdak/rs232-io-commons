package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.List;

import cx.ath.jbzdak.twoParamConnector.api.Cumulative;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public class DefaultMultiList<E extends Cumulative<E>, Tag extends Comparable> extends MultiList<E, Tag>{
//
//   public DefaultMultiList(int listSize, Factory<E> factory, Class<? extends List> listClazz) {
//      super(new Fact<E>(factory, listClazz, listSize), listSize);
//   }


   public DefaultMultiList(int listSize, Factory<E> factory, Factory<? extends List> listFactory, UpdateStrategy updateStrategy) {
      super(new Fact<E>(factory, listFactory, listSize), listSize, updateStrategy);
   }

   public DefaultMultiList(int listSize, Factory<E> factory, Factory<? extends List> listFactory) {
      super(new Fact<E>(factory, listFactory, listSize), listSize);
   }





   private static class Fact<E> implements Factory<ObservableList>{
      final Factory<? extends List> listFactory;

      final Factory<E> factory;

      final int listSize;

      private Fact(Factory<E> factory, Factory<? extends List> listFactory, int listSize) {
         this.factory = factory;
         this.listFactory = listFactory;
         this.listSize = listSize;
      }

//      private Fact(Factory<E> factory, final Class<? extends List> listClazz, int listSize) {
//         this.factory = factory;
//         this.listFactory = new Factory<List>() {
//            @Override
//            public List make() {
//               try {
//                  List<E> result = listClazz.newInstance();
//                  ObservableList<E> observable;
//                  if (result instanceof ObservableList) {
//                     observable = (ObservableList<E>) result;
//                  }else{
//                     observable = new WrappedObservableList<E>(result);
//                  }
//                  return observable;
//               } catch (Exception e){
//                  throw new RuntimeException();
//               }
//            }
//         };
//         this.listSize = listSize;
//      }

      @Override
      public ObservableList make() {
         try {
            List<E> result = listFactory.make();
            for(int ii=0; ii < listSize; ii++){
               result.set(ii, factory.make());
            }
            ObservableList<E> observable;
            if (result instanceof ObservableList) {
               observable = (ObservableList<E>) result;
            }else{
               observable = new WrappedObservableList<E>(result);
            }
            return observable;
         } catch (Exception e){
            throw new RuntimeException(e);
         }
      }
   }
}
