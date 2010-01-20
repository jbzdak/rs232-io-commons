package cx.ath.jbzdak.twoParamConnector.api;

/**
 *
 * @param <T> Type that can be added/substracted from this type.
 * @param <THIS_TYPE> This type used as a result of {@link cx.ath.jbzdak.twoParamConnector.api.Cumulative#copy()} operation. 
 */
public interface Cumulative<T, THIS_TYPE extends Cumulative<T, THIS_TYPE>> {

   void add(T number);

   void substract(T number);

   THIS_TYPE copy();
   
}
