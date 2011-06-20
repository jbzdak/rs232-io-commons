package cx.ath.jbzdak.ioCommons;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 17, 2010
 */
public enum StopBits {
   ONE_BIT(1), TWO_BITS(2), ONE_AND_HALF(3);

   final int contents;

   StopBits(int contents) {
      this.contents = contents;
   }

   public int getContents() {
      return contents;
   }
}
