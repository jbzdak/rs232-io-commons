package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;

import java.nio.charset.Charset;

class SetModeCommand extends Command{

   final byte[] command;

   public SetModeCommand(int commandNo) {
      super(false, 60);
      command = Charset.forName("ASCII").encode("!" + commandNo + "\r").array();
   }

   @Override
   public byte[] getCommand() {
      return command;
   }
}
