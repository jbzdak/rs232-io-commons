package cx.ath.jbzdak.diesIrae.ioCommons;

import cx.ath.jbzdak.diesIrae.ioCommons.enums.Parity;
import cx.ath.jbzdak.diesIrae.ioCommons.enums.PortState;
import cx.ath.jbzdak.diesIrae.ioCommons.enums.StopBits;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Properties;

public class Port {

   private static final Logger ENGINE_LOGGER = LoggerFactory.getLogger(Port.class);

   private final CommPortIdentifier identifier;
   private final SerialPort port;
   private final OutputStream out;
   private final InputStream in;
   private volatile PortState state = PortState.CLOSED;

   private static CommPortIdentifier makeIdentifier(String portName){
      try{
         return CommPortIdentifier.getPortIdentifier(portName);
      }catch(NoSuchPortException e){
         throw new RuntimeException(e);
      }
   }

   public static Port makeDriver(String portName, Properties properties){
      try {
         int baudRate = Integer.parseInt(properties.getProperty("baudRate"));
         int dataBits = Integer.parseInt(properties.getProperty("dataBits"));
         StopBits stopBits = StopBits.valueOf(properties.getProperty("stopBits"));
         Parity parity = Parity.valueOf(properties.getProperty("parity"));
         return new Port(portName, baudRate, dataBits, stopBits, parity);
      } catch (IllegalArgumentException e) {
         throw new PortException(e);
      }
   }

   public static Port makeDriver(Properties properties){
      return makeDriver(properties.getProperty("portName"), properties);
   }

   public Port(String identifier, int baudRate, int dataBits, StopBits stopBits, Parity parity) {
      this(makeIdentifier(identifier), baudRate, dataBits, stopBits, parity);
   }

   public Port(CommPortIdentifier identifier, int baudRate, int dataBits, StopBits stopBits, Parity parity) {
      this(identifier, baudRate, dataBits, stopBits.getContents(), parity.getContents());
   }

   private Port(CommPortIdentifier identifier, int baudRate, int dataBits, int stopBits, int parity) {
      super();
      this.identifier = identifier;
      if (identifier == null) {
         throw new IllegalStateException();
      }
      if (identifier.isCurrentlyOwned()) {
         throw new IllegalPortStateException(
                 "Port jest w danej chwili obsługiwany przez: '"
                         + identifier.getCurrentOwner() + "'");
      }
      state = PortState.OPENING;
      try {
         CommPort commPort = identifier.open(
                 Port.class.getSimpleName(), 2000);
         if (!(commPort instanceof SerialPort)) {
            throw new IllegalPortStateException(
                    "Nieobsługiwany typ portu! - nie jest to port szeregowy");
         }
         port = (SerialPort) commPort;
         port.setSerialPortParams(baudRate, dataBits, stopBits, parity);
         in = port.getInputStream();
         out = port.getOutputStream();
      } catch (RuntimeException e) {
         state = PortState.CLOSED;
         throw e;
      } catch (Exception e){
         state = PortState.CLOSED;
         throw new RuntimeException(e);
      }
      state = PortState.OPEN;
   }

   public void close() throws Exception {
      state = PortState.CLOSING;
      try {
         port.close();
      } catch (Exception e) {
         if (identifier.isCurrentlyOwned()) {
            state = PortState.OPEN;
         } else {
            state = PortState.CLOSED;
         }
         throw e;
      }
   }

   public void writeToOutput(byte[] data) throws IOException {
      checkState();
      if(ENGINE_LOGGER.isTraceEnabled()){
         ENGINE_LOGGER.trace("OUT: " +  new String(data, Charset.forName("ASCII")));
      }
      out.write(data);
      out.flush();
   }


   public InputStream getIn() {
      checkState();
      return in;
   }

   public PortState getState() {
      return state;
   }

   private void checkState(){
      if(!state.equals(PortState.OPEN)){
         throw new IllegalEngineStateException("Nieprawidłowy stan drivera silników :'" + state + "' oczekiwano że będzie otwarty");
      }
   }

   public void dispose() {
      try {
         in.close();
      } catch (IOException e) {
         ENGINE_LOGGER.warn("Exception while closing input stream for port {}", identifier);
         ENGINE_LOGGER.warn("Exception is", e);
      }
      try {
         out.close();
      } catch (IOException e) {
         ENGINE_LOGGER.warn("Exception while closing output stream for port {}", identifier);
         ENGINE_LOGGER.warn("Exception is", e);
      }
      port.close();
   }

}
