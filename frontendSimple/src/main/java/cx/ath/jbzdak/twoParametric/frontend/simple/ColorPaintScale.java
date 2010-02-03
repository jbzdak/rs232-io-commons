package cx.ath.jbzdak.twoParametric.frontend.simple;

import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 24, 2010
 */
public abstract class ColorPaintScale implements PaintScale{

   private static final Logger LOGGER = LoggerFactory.getLogger(ColorPaintScale.class);

   private final ActionEvent overflowEvent = new ActionEvent(this, 0, "OVERFLOW");

   List<ActionListener> overflowListeners = new ArrayList<ActionListener>();

   double lastUpperBound = 0;

   public void installInRenderer(final XYBlockRenderer renderer){
      addOverflowListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
           renderer.notifyListeners(new RendererChangeEvent(this));
         }
      });
   }

   @Override
   public double getLowerBound() {
      return 0;
   }

   @Override
   public double getUpperBound() {
      return lastUpperBound;
   }

   @Override
   public Paint getPaint(double value) {
      if(value> lastUpperBound){
         lastUpperBound = Math.round(value*1.1)+1;
         fireOverflow();
      }
      return getPaintInternal(value);
   }

   public void addOverflowListener(ActionListener actionListener){
      overflowListeners.add(actionListener);
   }

   protected void fireOverflow(){
      for (ActionListener listener : overflowListeners) {
         listener.actionPerformed(overflowEvent);
      }
   }

   public abstract Paint getPaintInternal(double value);
}
