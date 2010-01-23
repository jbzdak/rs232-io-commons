package cx.ath.jbzdak.twoParamConnector.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public abstract class ImplementationFrontend {

   private static final Logger LOGGER = LoggerFactory.getLogger(ImplementationFrontend.class);

   static ImplementationFrontend frontend;
   static{
      ImplementationFrontend tmp;
      try {
         tmp = (ImplementationFrontend)
                    Class.forName("cx.ath.jbzdak.twoParamConnector.api.ImplementationFrontendImpl").newInstance();
      } catch (Exception e){
         LOGGER.error("Cant create ImplementationFrontend instance. Two parametric driwer will not work.");
         tmp=null;
      }
      frontend = tmp;
   }

   public abstract Driver createDriver();

   public abstract Configuration getConfiguration();

   public static ImplementationFrontend createFrontend(){
      try {
         return frontend;
      } catch (Exception e){
         throw new RuntimeException(e);
      }
   }

}
