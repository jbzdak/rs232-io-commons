package cx.ath.jbzdak.twoParamConnector.plumbing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class ConfigFilesConfiguration {

   private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFilesConfiguration.class);

   Properties portProperties;

   Properties detectorsProperties;

   public ConfigFilesConfiguration() {
      loadPortProperties();
   }

   void loadDetectorsProperties(){
       Properties def = new Properties();
      try {
         def.load(getClass().getResourceAsStream("detectors.properties"));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      Properties user = new Properties(def);
      try {
         user.load(new FileInputStream(new File(".\\.config", "twoParametricDetectors.properties")));
      } catch (IOException e) {
         LOGGER.info("Couldnt load user port propertiesFile, creating defaults",e);
         createDefaultDetectorProperties(def);
      }
      detectorsProperties = user; 
   }

   void loadPortProperties(){
      Properties def = new Properties();
      try {
         def.load(getClass().getResourceAsStream("driver.properties"));
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      Properties user = new Properties(def);
      try {
         user.load(new FileInputStream(new File(".\\.config", "twoParametricPort.properties")));
      } catch (IOException e) {
         LOGGER.info("Couldnt load user port propertiesFile, creating defaults",e);
         createDefaultPortProperties(def);
      }
      portProperties = user;
   }

   void createDefaultPortProperties(Properties def){
      try {
         File configDir = new File(".", ".config");
         configDir.mkdirs();
         File defProps = new File(configDir, "twoParametricPort.properties");
         defProps.createNewFile();
         def.store(new FileOutputStream(defProps), "Auto created");
      } catch (IOException e) {
         LOGGER.warn("Couldnt create user port propertiesFile", e);
      }
   }

    void createDefaultDetectorProperties(Properties def){
      try {
         File configDir = new File(".", ".config");
         configDir.mkdirs();
         File defProps = new File(configDir, "twoParametriDetectors.properties");
         defProps.createNewFile();
         def.store(new FileOutputStream(defProps), "Auto created");
      } catch (IOException e) {
         LOGGER.warn("Couldnt create user port propertiesFile", e);
      }
   }
}
