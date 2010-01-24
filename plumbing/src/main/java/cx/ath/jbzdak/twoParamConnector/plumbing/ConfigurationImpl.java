package cx.ath.jbzdak.twoParamConnector.plumbing;

import cx.ath.jbzdak.twoParamConnector.api.Configuration;
import cx.ath.jbzdak.twoParamConnector.api.ImplementationFrontend;
import cx.ath.jbzdak.twoParamConnector.api.LinearFun;
import cx.ath.jbzdak.twoParamConnector.api.enums.Detector;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class ConfigurationImpl implements Configuration{

   public static ConfigurationImpl get(){
      return (ConfigurationImpl) ImplementationFrontend.createFrontend().getConfiguration();
   }

   String portName;

   public final Map<Detector, LinearFun> currentCalibration = new TreeMap<Detector, LinearFun>();

   public final  Map<Detector, LinearFun> defaultCalibration = new TreeMap<Detector, LinearFun>();

   public final int firstChannel, lastChannel;

   public final Map<Detector, Integer> typeToNumberMap = new TreeMap<Detector, Integer>();

   public final int resultsRefreshTime;

   public ConfigurationImpl() {
      ConfigFilesConfiguration files = new ConfigFilesConfiguration();
      portName = files.portProperties.getProperty("portName");
      defaultCalibration.put(Detector.BETA, LinearFun.parseFromHumanFormat(files.detectorsProperties.getProperty("defaultCalibration.channelBeta")));
      defaultCalibration.put(Detector.GAMMA, LinearFun.parseFromHumanFormat(files.detectorsProperties.getProperty("defaultCalibration.channelGamma")));
      currentCalibration.putAll(defaultCalibration);
      firstChannel = Integer.parseInt(files.detectorsProperties.getProperty("firstChannelNo"));
      lastChannel = Integer.parseInt(files.detectorsProperties.getProperty("lastChannelNo"));
      typeToNumberMap.put(Detector.BETA, Integer.parseInt(files.detectorsProperties.getProperty(Detector.BETA.name())));
      typeToNumberMap.put(Detector.GAMMA, Integer.parseInt(files.detectorsProperties.getProperty(Detector.GAMMA.name())));
      resultsRefreshTime = Integer.parseInt(files.detectorsProperties.getProperty("resultsRefreshTime"));
   }

   @Override
   public LinearFun getCurrentCalibration(Detector detector) {
      return currentCalibration.get(detector);
   }

   @Override
   public LinearFun getDefaultCalibration(Detector detector) {
      return defaultCalibration.get(detector);
   }
 
   @Override
   public void setCurrentCalibration(LinearFun newCalibration, Detector detector) {
      currentCalibration.put(detector, newCalibration);
   }

   public String getPortName() {
      return portName;
   }

   public void setPortName(String portName) {
      this.portName = portName;
   }
}
