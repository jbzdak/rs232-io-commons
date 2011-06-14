package cx.ath.jbzdak.ioCommons.command;

/**
 * Created by: Jacek Bzdak
 */
public class ClearInputCommand extends IgnoreInputCommand{

   public ClearInputCommand() {
   }

   public ClearInputCommand(int timeout) {
      super(timeout);
   }

   private static final byte[] bytes = "\n\n\n".getBytes();

   @Override
   public byte[] getCommand() {
      return bytes;
   }
}
