package cx.ath.jbzdak.twoParametric.frontend.simple;

import org.jfree.data.xy.AbstractXYZDataset;

import cx.ath.jbzdak.twoParamConnector.api.Matrix;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 24, 2010
 */
public class MatrixDataset extends AbstractXYZDataset{

   Matrix<? extends Number> matrix;

   public void setMatrix(Matrix<? extends Number> matrix) {
      this.matrix = matrix;
      fireDatasetChanged();
   }

   @Override
   public Number getZ(int series, int item) {
      return matrix.get(getX(series, item).intValue(), getY(series, item).intValue());
   }

   @Override
   public int getItemCount(int series) {
      return matrix.getCols()*matrix.getRows();
   }

   @Override
   public Number getX(int series, int item) {
      return item/matrix.getCols();
   }

   @Override
   public Number getY(int series, int item) {
      return item%matrix.getCols();
   }

   @Override
   public int getSeriesCount() {
      return 1;
   }

   @Override
   public Comparable getSeriesKey(int series) {
      return Integer.valueOf(1);
   }
}
