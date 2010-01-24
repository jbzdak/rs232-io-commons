package cx.ath.jbzdak.twoParamConnector.plumbing.telnet;

import cx.ath.jbzdak.common.PropertyChangeSupport;
import cx.ath.jbzdak.twoParamConnector.api.DetectorFrontend;
import cx.ath.jbzdak.twoParamConnector.api.FrontendListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 23, 2010
 */
public abstract class AbstractFrontend<DATA> implements DetectorFrontend<DATA>{

   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

   protected final List<FrontendListener<DATA>> listeners = new ArrayList<FrontendListener<DATA>>();

   protected boolean acquiring;

   @Override
   public boolean isAcquiring() {
      return acquiring;
   }

   void setAcquiring(boolean acquiring) {
      boolean oldAcquiring = this.acquiring;
      this.acquiring = acquiring;
      support.firePropertyChange("acquiring", oldAcquiring, this.acquiring);
   }

   @Override
   public void addFrontendListener(FrontendListener<DATA> dataFrontendListener) {
      listeners.add(dataFrontendListener);
   }

   public void fireDataChanged(DATA data){
      for (FrontendListener<DATA> listener : listeners) {
         listener.onNewData(data);
      }
   }
}
