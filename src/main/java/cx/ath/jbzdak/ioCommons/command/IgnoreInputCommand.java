package cx.ath.jbzdak.ioCommons.command;

import cx.ath.jbzdak.ioCommons.Command;

/**
 * Created by: Jacek Bzdak
 */
public abstract class IgnoreInputCommand extends Command<Void> {

   protected IgnoreInputCommand() {
      super(true, 0);
   }

   protected IgnoreInputCommand(int timeout) {
      super(true, timeout);
   }

   @Override
   public Void pareseResult(String s) {
      return null;
   }
}
