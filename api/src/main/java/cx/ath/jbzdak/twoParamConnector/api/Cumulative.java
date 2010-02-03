package cx.ath.jbzdak.twoParamConnector.api;

/**
 *
 * @param <T> Type that can be added/substracted from this type.
 * @param <THIS_TYPE> This type used as a result of {@link cx.ath.jbzdak.twoParamConnector.api.Cumulative#copy()} operation. 
 */
public abstract class Cumulative<THIS_TYPE extends Cumulative<THIS_TYPE>> extends Number{

   public abstract void setValue(Number number);

   public abstract  void add(Number number);

   public abstract void substract(Number number);

   public abstract THIS_TYPE copy();

   public abstract void increment();

}
