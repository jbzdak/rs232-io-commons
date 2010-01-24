package cx.ath.jbzdak.twoParamConnector.plumbing;

import cx.ath.jbzdak.twoParamConnector.api.CumulativeNumber;
import cx.ath.jbzdak.twoParamConnector.api.Matrix;
import cx.ath.jbzdak.twoParamConnector.plumbing.util.SuperObservableList;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 21, 2010
 */
public class TwoParamMatrix<T extends CumulativeNumber> extends SuperObservableList<T> implements Matrix<T>, List<T>{

   T[][] contents;

   final int rows, cols;

   public TwoParamMatrix(int cols, int rows, T[][] contents) {
      this.cols = cols;
      this.contents = contents;
      this.rows = rows;
   }

   public TwoParamMatrix(int cols, int rows, Class<T> clazz) {
      this.cols = cols;
      this.rows = rows;
      contents = (T[][]) Array.newInstance(clazz, rows, cols);
   }

   @Override
   public T get(int row, int col) {
      return contents[row][col];
   }

   @Override
   public int getCols() {
      return rows;
   }

   @Override
   public int getRows() {
      return cols;
   }

   @Override
   public T set(int row, int col, T newValue) {
      return contents[row][col]=newValue;
   }

   @Override
   public T get(int index) {
      return get(index/getCols(), index%getCols());
   }

   @Override
   public int size() {
      return getRows()*getCols();
   }

   public void notifyElementChanged(int row, int col){
      notifyElementChanged(row*getCols()+col);
   }
}
