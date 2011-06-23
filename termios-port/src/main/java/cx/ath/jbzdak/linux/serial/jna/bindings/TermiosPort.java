/*
 * Copyright for Jacek Bzdak 2011.
 *
 *     This file is part of Serial ioCommons, utility library to do serial
 *     port communication using native APIs and JNA to bind them to java.
 *
 *     Serial ioCommons is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Serial ioCommons is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package cx.ath.jbzdak.linux.serial.jna.bindings;

import cx.ath.jbzdak.ioCommons.*;
import cx.ath.jbzdak.linux.serial.jna.bindings.config.ConfigFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by: Jacek Bzdak
 */
public class TermiosPort extends AbstractConfigurablePort{

   private static final Logger LOGGER = LoggerFactory.getLogger(TermiosPort.class);

   final TermiosWrapper termios;

   TerminosConfigWrapper configWrapper = new TerminosConfigWrapper();

   int fileDescriptor = -1;

//   /**
//    * Default value corresponds to flags O_RDWR | O_NOCTTY | O_NDELAY
//    */
//   int openMode = 2306;

   OpenMode[] openModes = null;

   String portName;

   TermiosInputStream inputStream;

   public TermiosPort() {
      termios = TermiosEntryPoint.TERMIOS_WRAPPER;
   }

   public TermiosPort(TermiosWrapper termios) {
      this.termios = termios;
   }

   /**
    * Registers a listener that will update termios config when opening port.
    */
   public void registerConfigUpdater(ConfigUpdater configUpdater) {
      checkState(PortState.INITIAL);
      configWrapper.registerConfigUpdater(configUpdater);
   }

   /**
    * Sets open mode for port, ie. flags that are passed to {@link Termios#open(String, int)}.
    * Normally you should not change it, default value is sensible ;)
    * @param openMode open mode,
    */
   public void setOpenModes(OpenMode... openMode) {
      this.openModes = openMode;
   }

   /**
    * Port descriptor name (for example: "/dev/ttyUSB0")_
    */
   @Override
   public void setPortName(String portName) {
      this.portName = portName;
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void open() throws PortException {
      LOGGER.info("Opening port '{}'", portName);
      OpenMode[] openModes = this.openModes;
      if(openModes == null){
         switch (configWrapper.blockingStrategy){
            case TIMEOUT_BLOCK:
            case BLOCK:
               openModes = new OpenMode[]{OpenMode.RDWR, OpenMode.NOCTTY};
               break;
           case  NO_BLOCK:
              openModes = new OpenMode[]{OpenMode.RDWR, OpenMode.NOCTTY, OpenMode.NONBLOCK};
              break;
           default:
              throw new UnsupportedOperationException();
         }
      }
      try {
         portState = PortState.OPENING;
         try {
            fileDescriptor = termios.open(portName, openModes);
         } catch (NativeException e) {
            LOGGER.info("Error while opening port");
            throw e;
         }
         try {
            configWrapper.loadFromFD(fileDescriptor);
         } catch (NativeException e) {
            LOGGER.info("Error while loading config from port");
            throw e;
         }
         try {
            configWrapper.applySettings(fileDescriptor);
         } catch (NativeException e) {
            LOGGER.error("Error when applying configuration");
            throw e;
         }
         portState = PortState.OPEN;
         LOGGER.info("Port file_descriptor for port {} is {}", portName, fileDescriptor);
      } catch (NativeException e) {
         if(fileDescriptor != -1){
            LOGGER.info("Caught exception while opening port, will now try to dispose opened file descriptor");
            dispose();
            portState = PortState.CLOSED;
         }
         fileDescriptor = -1;
         throw new PortException(e);
      }
   }



   @Override
   /**
    * {@inheritDoc}
    */
   public void close() throws PortException {
      LOGGER.info("Closing {}", portName);
      if(portState == PortState.CLOSED){
         return;
      }
      try {
         portState = PortState.CLOSING;
         termios.close(fileDescriptor);
         portState = PortState.CLOSED;
      } catch (NativeException e) {
         throw  new PortException(e);
      }
   }

   @Override
   /**
    * {@inheritDoct}
    */
   public void setBlockingStrategy(BlockingStrategy block) {
      configWrapper.setBlockingStrategy(block);
   }

   @Override
   public void writeToOutput(byte[] data) throws IOException {
      checkState();
      try {
         termios.write(fileDescriptor, data);
      } catch (NativeException e) {
         throw  new PortException(e);
      }
   }

   @Override
   public InputStream getIn() {
      checkState();
      if(inputStream == null){
         inputStream = new TermiosInputStream(this);
      }
      return inputStream;
   }

   /**
    * @see TerminosConfigWrapper#setVmin(int)
    */
   public void setVmin(int vmin) {
      configWrapper.setVmin(vmin);
   }

   /**
    * @see TerminosConfigWrapper#setVtime(int)
    */
   public void setVtime(int vtime) {
      configWrapper.setVtime(vtime);
   }


   /**
    * {@inheritDoc}
    */
   @Override
   public void setDataBits(int dataBits) {
      configWrapper.setDataBits(dataBits);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setParity(Parity parity) {
      configWrapper.setParity(parity);
   }

   @Override
   public void setStopBits(StopBits stopBits) {
      configWrapper.setStopBits(stopBits);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setFlowControl(FlowControl flowControl) {
      configWrapper.setFlowControl(flowControl);
   }

   @Override
   public void setPerformDefaultUpdates(boolean performDefaultUpdates) {
      configWrapper.setPerformDefaultUpdates(performDefaultUpdates);
   }


   /**
     * Allows to set speed directly specifing appropriate ConfigFlag
     */
   public void setSpeed(ConfigFlag speed) {
      configWrapper.setSpeed(speed);
   }

   /**
     * {@inheritDoc}
     */
   @Override
   public void setSpeed(int speed) {
      configWrapper.setSpeed(speed);
   }


    /**
     * Allows to set speed directly specifing appropriate ConfigFlag
     */
   public void setInputSpeed(ConfigFlag inputSpeed) {
      configWrapper.setInputSpeed(inputSpeed);
   }

   /**
     * {@inheritDoc}
     */
   @Override
   public void setOutputSpeed(int outputSpeed) {
      configWrapper.setOutputSpeed(outputSpeed);
   }

   /**
     * {@inheritDoc}
     */
   @Override
   public void setInputSpeed(int inputSpeed) {
      configWrapper.setInputSpeed(inputSpeed);
   }


    /**
     * Allows to set speed directly specifing appropriate ConfigFlag
     */
   public void setOutputSpeed(ConfigFlag outputSpeed) {
      configWrapper.setOutputSpeed(outputSpeed);
   }

}
