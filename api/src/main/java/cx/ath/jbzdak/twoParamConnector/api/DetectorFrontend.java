package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public interface DetectorFrontend<DATA> {

   DATA getData();

   void addFrontendListener(FrontendListener<DATA> frontendListener);
   
}
