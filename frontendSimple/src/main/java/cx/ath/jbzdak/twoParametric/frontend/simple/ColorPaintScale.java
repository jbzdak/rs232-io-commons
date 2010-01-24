package cx.ath.jbzdak.twoParametric.frontend.simple;

import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
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

   double lastUpperBound;

   public void installInRenderer(final XYBlockRenderer renderer){
      addOverflowListener(new ActionListener() {

         final Method method;
         {
            try {
               method = XYBlockRenderer.class.getMethod("fireChangeEvent");
               method.setAccessible(true);
            } catch (NoSuchMethodException e) {
               LOGGER.error("Exception while creating ColorPaintScaleListener");
               throw new RuntimeException(e);
            }
         }

         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               method.invoke(renderer);
            } catch (Exception e1){
               LOGGER.warn("Exception while invoking fireChangeEvent on XYBlockRenderer via reflection api", e1);
            }
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
         lastUpperBound = value;
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
