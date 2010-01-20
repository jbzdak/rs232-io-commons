package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.diesIrae.ioCommons.Command;

import java.nio.charset.Charset;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
class QueryFirst extends Command{

   final byte[] command;

   public QueryFirst() {
      super(true, 10);
      this.command = Charset.forName("ASCII").encode("1\r").array();
   }

   public byte[] getCommand() {
      return command;
   }
}
