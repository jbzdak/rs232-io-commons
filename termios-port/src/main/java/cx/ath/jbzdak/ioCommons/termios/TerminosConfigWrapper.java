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

package cx.ath.jbzdak.ioCommons.termios;

import cx.ath.jbzdak.ioCommons.*;
import cx.ath.jbzdak.linux.serial.jna.bindings.NativeException;
import cx.ath.jbzdak.linux.serial.jna.bindings.TermiosEntryPoint;
import cx.ath.jbzdak.linux.serial.jna.bindings.TermiosWrapper;
import cx.ath.jbzdak.linux.serial.jna.bindings.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by: Jacek Bzdak
 */
class TerminosConfigWrapper {

   private static final Logger LOGGER = LoggerFactory.getLogger(TerminosConfigWrapper.class);

   final TermiosWrapper termiosWrapper;

   final List<ConfigUpdater> configUpdaters = new ArrayList<ConfigUpdater>();

   TermiosConfig termiosConfig = new TermiosConfig();

   Parity parity = Parity.NONE;

   OnParityError onParityError = OnParityError.IGNORE;

   StopBits stopBits = StopBits.ONE_BIT;

   FlowControl flowControl = FlowControl.DISABLED;

   ConfigFlag inputSpeed, outputSpeed;

   BlockingStrategy strategy;

   BlockingStrategy blockingStrategy = BlockingStrategy.TIMEOUT_BLOCK;

   int dataBits = Port.DEFAULT_DATA_BITS;

   byte vmin = -1;

   byte vtime = -2;

   boolean performDefaultUpdates = true;


   public TerminosConfigWrapper(TermiosWrapper termiosWrapper) {
      this.termiosWrapper = termiosWrapper;
   }

   public TerminosConfigWrapper() {
      this(TermiosEntryPoint.TERMIOS_WRAPPER);
   }

   public void loadFromFD(int portFD) throws NativeException {
      termiosConfig = termiosWrapper.getConfig(portFD);
      if(LOGGER.isInfoEnabled()){
         LOGGER.info("Loaded config from port {}. Config is: {}", portFD, termiosConfig);
      }

   }

   /**
    * Registers objects that will update port config after all default updates
    * are performed.
    * @param configUpdater
    */
   public void registerConfigUpdater(ConfigUpdater configUpdater){
      configUpdaters.add(configUpdater);
   }

   public void setParity(Parity parity) {
      checkParity(parity);
      this.parity = parity;
   }

   public void setStopBits(StopBits stopBits) {
      checkStopBits(stopBits);
      this.stopBits = stopBits;
   }


   public void setDataBits(int dataBits) {
      checkDataBits(dataBits);
      this.dataBits = dataBits;
   }

   public void setFlowControl(FlowControl flowControl) {
      this.flowControl = flowControl;
   }

   @ModifiesNativeResources
   public void applySettings(int portFD) throws NativeException {
      applyDataBits();
      applyStopBits();
      applyTimeouts();
      applyOutputParity();
      applyFlowControl();
      applySpeed();
      applyDefaultUpdates();
      applyUpdaters();
      if(LOGGER.isInfoEnabled()){
          LOGGER.info("Saved config from port {}. Config is: {}", portFD, termiosConfig);
      }
      termiosWrapper.applyConfig(portFD, termiosConfig);
   }

   /**
    * Performs default updates of port config, such as enabling reading from port and so on :)
    * @param performDefaultUpdates
    */
   public void setPerformDefaultUpdates(boolean performDefaultUpdates) {
      this.performDefaultUpdates = performDefaultUpdates;
   }


   public void setBlockingStrategy(BlockingStrategy blockingStrategy) {
      this.blockingStrategy = blockingStrategy;
   }

   public void setSpeed(ConfigFlag speed) {
      setInputSpeed(speed);
      setOutputSpeed(speed);
   }

   public void setSpeed(int speed) {
      setInputSpeed(speed);
      setOutputSpeed(speed);
   }

   public void setInputSpeed(ConfigFlag inputSpeed) {
      if(!inputSpeed.isBaudRate()){
         throw new InvalidParameterException("Flag passed to setSpeed is not a Flg that represents speed. This flag is: " + inputSpeed);
      }
      this.inputSpeed = inputSpeed;
   }

   public void setOutputSpeed(ConfigFlag outputSpeed) {
      if(!inputSpeed.isBaudRate()){
         throw new InvalidParameterException("Flag passed to setSpeed is not a Flg that represents speed. This flag is: " + outputSpeed);
      }
      this.outputSpeed = outputSpeed;
   }

   public void setInputSpeed(int inputSpeed) {
      this.inputSpeed = ConfigFlag.getBaudRateConstant(inputSpeed);
   }

   public void setOutputSpeed(int outputSpeed) {
      this.outputSpeed = ConfigFlag.getBaudRateConstant(outputSpeed);
   }

   /**
    * Controlls when internal read function {@link cx.ath.jbzdak.linux.serial.jna.bindings.Termios#read(int, byte[], int) will return}. It will return after
    * reading <code>vmin</code> chars. Use this if you can asses length of incomming mesages (eg. they are all 20 bytes),
    * so read will wait until 20 characters are read (or timeout defined in {@link #setVtime(int) has passed}.
    *
    * Corresponds to VMIN setting in {@link TermiosConfig#c_cc} array.
    *
    * @see TermiosInputStream for more in-depth description
    *
    * If you are unsure what this changes leave it alone :) (setting it to 1 byte is save vut not too fast).
    *
    */
   public void setVmin(int vmin) {
      this.vmin = (byte) vmin;
   }

   /**
    *
    * Controlls when internal read function {@link cx.ath.jbzdak.linux.serial.jna.bindings.Termios#read(int, byte[], int) will return}. It will return after
    * reading <code>vmin</code> chars. It will return either when more than vtime tenths of seconds passes after last
    * reclieved byte.
    *
    * This setting interacts with {@link #setVmin(int)}. See {@link }
    *
    * @see TermiosInputStream for more in-depth description *
    *
    * @param vtime
    */
   public void setVtime(int vtime) {
      this.vtime =(byte) vtime;
   }

   //////////////////////////////// CHECKS

   void checkParity(Parity parity){
      if(EnumSet.of(Parity.MARK, Parity.SPACE).contains(parity)){
         throw new UnsupportedOperationException("Mark and space parity are unsupported for now. " +
                 "You can implement it fairly easily.");
      }
   }

   void checkStopBits(StopBits stopBits){
      if(stopBits == StopBits.ONE_AND_HALF){
         throw new UnsupportedOperationException("ONE_AND_A_HALF stop bits is unsupported");
      }
   }

   public void checkDataBits(int dataBits){
      if(dataBits > 8 || dataBits < 5){
         throw new UnsupportedOperationException("Frame sizes between  6 and 8 are supported");
      }

   }

   void checkOnParity(OnParityError val){
      if(val != OnParityError.IGNORE){
         throw  new InvalidParameterException("For now only OnParityError.INGORE is supported");
      }
   }

   //////////////////////////////////// APPLIES

   @ModifiesNativeResources
   void applyUpdaters(){
      for (ConfigUpdater updater : configUpdaters) {
         updater.updateConfig(termiosConfig);
      }
   }

   @ModifiesNativeResources
   void applySpeed() throws NativeException {
      termiosWrapper.setInputBaudRate(termiosConfig, inputSpeed);
      termiosWrapper.setOutputBaudRate(termiosConfig, outputSpeed);
   }

   @ModifiesNativeResources
   void applyStopBits(){
      checkStopBits(stopBits);
      int flag = termiosConfig.c_cflag;
      switch (stopBits){
         case ONE_BIT:
            flag = ConfigFlag.switchOff(flag, ConfigFlag.CSTOPB);
            break;
         case TWO_BITS:
            flag = ConfigFlag.switchOn(flag, ConfigFlag.CSTOPB);
            break;
         default:
            throw new UnsupportedOperationException();
      }
      termiosConfig.c_cflag = flag;
   }

   @ModifiesNativeResources
   void applyOutputParity(){
      checkParity(parity);
      int flag = termiosConfig.c_cflag;
      int iflag = termiosConfig.c_iflag;
      switch (parity){
         case NONE:
            flag = ConfigFlag.switchOff(flag, ConfigFlag.PARENB);
            iflag = InputFlag.switchOff(iflag, InputFlag.INPCK);
            break;
         case EVEN:
            flag = ConfigFlag.switchOn(flag, ConfigFlag.PARENB);
            flag = ConfigFlag.switchOff(flag, ConfigFlag.PARODD);
            iflag = InputFlag.switchOn(iflag, InputFlag.INPCK, InputFlag.IGNPAR);
            break;
         case ODD:
            flag = ConfigFlag.switchOn(flag, ConfigFlag.PARENB);
            flag = ConfigFlag.switchOn(flag, ConfigFlag.PARODD);
            iflag = InputFlag.switchOn(iflag, InputFlag.INPCK, InputFlag.IGNPAR);
            break;
         default:
            throw new UnsupportedOperationException();
      }

      termiosConfig.c_cflag = flag;
      termiosConfig.c_iflag = iflag;
   }

   @ModifiesNativeResources
   void applyDataBits(){
      checkDataBits(dataBits);
      int flag = termiosConfig.c_cflag;
      flag = ConfigFlag.switchOff(flag, ConfigFlag.CSIZE);
      ConfigFlag bitFlag;
      switch (dataBits){
         case 5:
            bitFlag = ConfigFlag.CS5;
            break;
         case 6:
            bitFlag = ConfigFlag.CS6;
            break;
         case 7:
            bitFlag = ConfigFlag.CS7;
            break;
         case 8:
            bitFlag = ConfigFlag.CS8;
            break;
         default:
            throw  new UnsupportedOperationException();
      }

      flag = ConfigFlag.switchOn(flag, bitFlag);
      termiosConfig.c_cflag = flag;

   }

   @ModifiesNativeResources
   void applyDefaultUpdates(){
      if(!performDefaultUpdates) return;
      termiosConfig.c_oflag = 0;
//      termiosConfig.c_iflag = InputFlag.switchOff(termiosConfig.c_iflag);
      termiosConfig.c_iflag = InputFlag.switchOn(termiosConfig.c_iflag, InputFlag.IXANY, InputFlag.IXOFF);
      termiosConfig.c_lflag = LocalFlag.switchOff(termiosConfig.c_lflag, LocalFlag.ICANON,
              LocalFlag.ECHO, LocalFlag.ECHOE, LocalFlag.ISIG);
   }

   @ModifiesNativeResources
   void applyTimeouts(){
      int vmin = this.vmin;
      int vtime = this.vtime;
      if(this.vmin < 0){
         switch (blockingStrategy){
            case NO_BLOCK:
               vmin = 0;
               break;
            case TIMEOUT_BLOCK:
               vmin = 0;
               break;
            case BLOCK:
               vmin = 1;
               break;
            default:
               throw new UnsupportedOperationException();
         }
      }
      if(this.vtime < 0){
         switch (blockingStrategy){
            case NO_BLOCK:
               vtime = 0;
               break;
            case TIMEOUT_BLOCK:
               vtime = 2;
               break;
            case BLOCK:
               vtime = 2;
               break;
            default:
               throw new UnsupportedOperationException();
         }
      }
      termiosConfig.c_cc[CCIndices.VMIN.value] = (byte) vmin;
      termiosConfig.c_cc[CCIndices.VTIME.value] = (byte) vtime;
   }

   @ModifiesNativeResources
   void applyFlowControl(){
      int flag = termiosConfig.c_cflag;
      switch (flowControl){
         case DISABLED:
            flag = ConfigFlag.switchOff(flag, ConfigFlag.CRTSCTS);
            break;
         case RTS_CTS:
            flag = ConfigFlag.switchOn(flag, ConfigFlag.CRTSCTS);
            break;
         default:
            throw new UnsupportedOperationException();
      }
      termiosConfig.c_cflag = flag;
   }


}
