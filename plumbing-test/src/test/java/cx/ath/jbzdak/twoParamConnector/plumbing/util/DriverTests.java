package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import cx.ath.jbzdak.twoParamConnector.api.Configuration;
import cx.ath.jbzdak.twoParamConnector.api.Driver;
import cx.ath.jbzdak.twoParamConnector.api.DriverStateListener;
import cx.ath.jbzdak.twoParamConnector.api.ImplementationFrontend;
import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 2, 2010
 */
public class DriverTests {


   Driver driver;

   Configuration configuration;

   @Before
   public void init(){
      configuration = ImplementationFrontend.getFrontend().getConfiguration();
      driver = ImplementationFrontend.getFrontend().createDriver();
   }

   @After
   public void destroy(){
      driver.dispose();
   }

   @Test
   public void testListenerOnStateChange(){
      DriverStateListener listener = Mockito.mock(DriverStateListener.class);
      driver.addDriverStateListener(listener);
      driver.changeState(TwoParametricState.COINCIDENCE);
      Mockito.verify(listener).driverStateWillChange(TwoParametricState.NO_STATE, TwoParametricState.COINCIDENCE);
      InOrder inOrder = Mockito.inOrder(listener);
      inOrder.verify(listener).driverStateWillChange(TwoParametricState.NO_STATE, TwoParametricState.COINCIDENCE);
      inOrder.verify(listener).driverStateChanged(TwoParametricState.NO_STATE, TwoParametricState.COINCIDENCE);
      Mockito.verifyNoMoreInteractions(listener);
   }
}
