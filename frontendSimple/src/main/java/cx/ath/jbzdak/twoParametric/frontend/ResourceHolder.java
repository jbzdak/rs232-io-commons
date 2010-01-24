package cx.ath.jbzdak.twoParametric.frontend;

import java.util.ResourceBundle;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 24, 2010
 */
public class ResourceHolder {

   private static final ResourceHolder RESOURCE_HOLDER = new ResourceHolder();

   public static String getString(String key){
      return RESOURCE_HOLDER.get(key);
   }

   private final ResourceBundle resources;

   public ResourceHolder() {
      resources = ResourceBundle.getBundle("simpleUI");
   }

   public String get(String key){
      if(resources.containsKey(key))
         return resources.getString(key);
      return  resources.getString(key);      
   }
}
