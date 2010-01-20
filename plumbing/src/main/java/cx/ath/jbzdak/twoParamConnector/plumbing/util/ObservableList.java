package cx.ath.jbzdak.twoParamConnector.plumbing.util;

import java.util.List;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 18, 2010
 */
public interface ObservableList<E> extends List<E> {

   public void addListListener(ListListener<E> listListener);

   public boolean removeListListener(ListListener<E> listListener);

}
