package cx.ath.jbzdak.ioCommons;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public class PortException extends RuntimeException{
   public PortException() {
   }

   public PortException(Throwable cause) {
      super(cause);
   }

   public PortException(String message) {
      super(message);
   }

   public PortException(String message, Throwable cause) {
      super(message, cause);
   }
}
