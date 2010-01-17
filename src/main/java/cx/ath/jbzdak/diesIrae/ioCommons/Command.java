package cx.ath.jbzdak.diesIrae.ioCommons;

public abstract class Command {

	private final boolean recievesInput;

	private final int timeout;

   protected Command(boolean recievesInput, int timeout) {
      this.recievesInput = recievesInput;
      this.timeout = timeout;
   }

   abstract byte[] getCommand();

   public boolean isRecievesInput() {
      return recievesInput;
   }

   public int getTimeout() {
      return timeout;
   }
}
