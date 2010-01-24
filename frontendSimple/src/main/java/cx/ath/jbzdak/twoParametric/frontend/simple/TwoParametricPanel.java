package cx.ath.jbzdak.twoParametric.frontend.simple;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;

import cx.ath.jbzdak.twoParamConnector.api.*;

import static cx.ath.jbzdak.twoParametric.frontend.ResourceHolder.getString;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 24, 2010
 */
public class TwoParametricPanel extends JPanel{

   private final TwoParamFrontend frontend;

   private final MatrixDataset dataset = new MatrixDataset();

   private final ChartPanel chartPanel;

   public TwoParametricPanel(TwoParamFrontend frontend) {
      setLayout(new BorderLayout());
      this.frontend = frontend;
      frontend.addFrontendListener(new FrontendListener<Matrix<CumulativeInteger>>() {
         @Override
         public void onNewData(Matrix<CumulativeInteger> data) {
            dataset.setMatrix(data);
         }
      });
      ChartMaker maker = new ChartMaker();
      chartPanel = maker.panel;
      add(chartPanel, BorderLayout.CENTER);

   }



   private class ChartMaker{

      RedScale scale;

      XYPlot plot;

      JFreeChart chart;

      ChartPanel panel;

      private ChartMaker() {
         createPlot();
         initializeChart();
         updateLegend();
         panel = new ChartPanel(chart);
      }

      private void createPlot(){
            Configuration configuration = ImplementationFrontend.getFrontend().getConfiguration();
            XYBlockRenderer xyBlockRenderer = new XYBlockRenderer();
            scale = new RedScale();
            scale.installInRenderer(xyBlockRenderer);
            xyBlockRenderer.setPaintScale(scale);
            ValueAxis xAxis = new NumberAxis(getString("channelXLabel"));
            xAxis.setRange(new Range(configuration.getFirstChannel(), configuration.getLastChannel()));
            ValueAxis yAxis = new NumberAxis(getString("channelYLabel"));
            yAxis.setRange(new Range(configuration.getFirstChannel(), configuration.getLastChannel()));
            plot = new XYPlot(dataset, xAxis, yAxis, xyBlockRenderer);
         }

         private void updateLegend(){
            chart.removeLegend();

            NumberAxis legendAxis = new NumberAxis(getString("scale"));
            legendAxis.setAxisLinePaint(Color.WHITE);
            legendAxis.setTickMarkPaint(Color.WHITE);

            PaintScaleLegend legend = new PaintScaleLegend(scale, legendAxis);
            legend.setStripOutlineVisible(false);
            legend.setSubdivisionCount(20);
            legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
            legend.setAxisOffset(5);
            legend.setMargin(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
            legend.setPadding(new RectangleInsets(10.0, 10.0, 10.0, 10.0));
            legend.setStripWidth(10.0);
            legend.setPosition(RectangleEdge.LEFT);

            chart.addSubtitle(legend);

         }

         private void initializeChart(){

            chart = new JFreeChart(getString("plotLabel"), plot);

         }

   }
}
