package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
public interface Matrix<T extends  Number> {

   T get(int i, int j);

   /**
    *
     * @return old value
     */
   T set(int i, int j, T newValue);
   
}
