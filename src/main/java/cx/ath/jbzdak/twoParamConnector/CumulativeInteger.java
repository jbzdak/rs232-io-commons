package cx.ath.jbzdak.twoParamConnector;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
@net.jcip.annotations.NotThreadSafe
public class CumulativeInteger extends CumulativeNumber{

   volatile int contents;

   public CumulativeInteger(int contents) {
      this.contents = contents;
   }

   @Override
   public double doubleValue() {
      return contents;
   }

   @Override
   public int intValue() {
      return contents;
   }

   @Override
   public long longValue() {
      return contents;
   }

   @Override
   public float floatValue() {
      return contents;
   }

   @Override
   void add(Number number) {
      contents += number.intValue();
   }
}
