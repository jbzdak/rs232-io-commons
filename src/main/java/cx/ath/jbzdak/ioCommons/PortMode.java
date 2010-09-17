package cx.ath.jbzdak.ioCommons;

import gnu.io.SerialPort;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public enum PortMode {
   /**
    * Sets dtr bit {@link gnu.io.SerialPort}. I guess it sets logical 1 to DTR line
    * when port is connected.
    */
   DTR_ENABLED(){
      @Override
      void setMode(SerialPort port) {
         port.setDTR(true);
      }},
   /**
    * Sets rts bit on {@link gnu.io.SerialPort}. I gues it sets logical 1 before sending. 
    */
   RTS_ENABLED(){
      @Override
      void setMode(SerialPort port) {
         port.setRTS(true);
      }};

   abstract void setMode(SerialPort port);
}
