package cx.ath.jbzdak.ioCommons.command;

import cx.ath.jbzdak.ioCommons.Command;

/**
 * Created by: Jacek Bzdak
 */
public abstract class IntegerCommand extends Command<Integer>{

   protected IntegerCommand(int timeout) {
      super(true, timeout);
   }

   @Override
   public Integer pareseResult(String s) {
      return Integer.valueOf(s);
   }
}
