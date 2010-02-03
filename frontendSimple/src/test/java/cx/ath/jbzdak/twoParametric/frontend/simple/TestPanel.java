package cx.ath.jbzdak.twoParametric.frontend.simple;

import javax.swing.*;

import java.awt.BorderLayout;

import cx.ath.jbzdak.twoParamConnector.api.Driver;
import cx.ath.jbzdak.twoParamConnector.api.ImplementationFrontend;
import cx.ath.jbzdak.twoParamConnector.api.TwoParamFrontend;
import cx.ath.jbzdak.twoParamConnector.api.enums.TwoParametricState;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Feb 3, 2010
 */
public class TestPanel {


   public static void main(String[] args){
      ImplementationFrontend frontend = ImplementationFrontend.getFrontend();
      Driver driver = frontend.createDriver();
      driver.changeState(TwoParametricState.COINCIDENCE);
      TwoParamFrontend<String> ctrl = (TwoParamFrontend<String>) driver.getControlObject();
      TwoParametricPanel twoParametricPanel = new TwoParametricPanel(ctrl);
      JFrame frame = new JFrame();
      frame.setSize(640, 480);
      frame.setLayout(new BorderLayout());
      frame.add(twoParametricPanel, BorderLayout.CENTER);
      ctrl.start();
      frame.setVisible(true);
   }

   
}
