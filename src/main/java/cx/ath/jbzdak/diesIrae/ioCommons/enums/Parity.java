package cx.ath.jbzdak.diesIrae.ioCommons.enums;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public enum Parity {
   NONE(0), ODD(1), EVEN(2), MARK(3), SPACE(4);

   final int contents;

   Parity(int contents) {
      this.contents = contents;
   }

   public int getContents() {
      return contents;
   }
}
