package cx.ath.jbzdak.twoParametric.frontend.simple;

import java.awt.Color;
import java.awt.Paint;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 24, 2010
 */
public class RedScale extends ColorPaintScale{

   Color[] colors = new Color[256];

   @Override
   public Paint getPaintInternal(double value) {
      int idx = (int) (value/getUpperBound()*255);
      Color col = colors[idx];
      if(col == null){
         col = new Color(255, 255 - idx, 255 - idx);
         colors[idx] = col;
      }
      return col;
   }
}
