package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public interface FrontendListener<T> {

   void onNewData(T data);
   
}
