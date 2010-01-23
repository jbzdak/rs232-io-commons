package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
public interface Matrix<T extends  Number> {

   int getRows();

   int getCols();

   T get(int row, int col);

   /**
    *
     * @return old value
     */
   T set(int row, int col, T newValue);
   
}
