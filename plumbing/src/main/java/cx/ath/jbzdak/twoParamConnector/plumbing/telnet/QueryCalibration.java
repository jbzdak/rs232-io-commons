package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;

import java.nio.charset.Charset;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
class QueryCalibration extends Command{
     final byte[] command;

   public QueryCalibration() {
      super(true, 10);
      this.command = Charset.forName("ASCII").encode("?\r").array();
   }

   public byte[] getCommand() {
      return command;
   }
}
