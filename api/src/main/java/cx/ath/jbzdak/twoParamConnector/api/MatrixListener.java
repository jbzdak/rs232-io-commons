package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
public interface MatrixListener<T extends Number> {

   void valueChanged(int i, int j, T oldVal, T newVal);
}
