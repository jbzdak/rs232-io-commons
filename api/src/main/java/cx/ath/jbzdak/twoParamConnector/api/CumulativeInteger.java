package cx.ath.jbzdak.twoParamConnector.api;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
@net.jcip.annotations.NotThreadSafe
public class CumulativeInteger extends CumulativeNumber<CumulativeInteger>{

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
   public void add(Number number) {
      contents += number.intValue();
   }

   @Override
   public CumulativeInteger copy() {
      return new CumulativeInteger(contents);
   }

   @Override
   public void substract(Number number) {
      contents -= number.intValue();
   }
}
