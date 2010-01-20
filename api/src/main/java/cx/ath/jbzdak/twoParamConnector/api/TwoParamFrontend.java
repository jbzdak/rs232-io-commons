package cx.ath.jbzdak.twoParamConnector.api;

import java.util.concurrent.TimeUnit;

/**
 * Frontend do dwuparametrycznego systemu made by P. Wacławik.
 *
 * Tag ma pozwalać na odróżnienie/dodawanie/usuwanie poszczególnych akwizycji
 * z wykresu dwuparametrycznego. 
 *
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 16, 2010
 */
public interface TwoParamFrontend<Tag extends Comparable> extends DetectorFrontend<Matrix<CumulativeInteger>>{

   /**
    * Czy pobiera dane.
    * @return
    */
   boolean isAcquiring();

   /**
    * Startuje akwizycję a pomiary taguje jako tag.
    * @param tag tag pomiarów
    *  */
   void startAcquisition(Tag tag);

   /**
    * Wysyła żądanie zatrzymania akwizycji. Akwizycja będzie zatrzymana po jakimś czasie.
    * Zatrzymanei akwizycji spowoduje wysłanie odpowieeniego {@link java.beans.PropertyChangeEvent}.  
    */
   void stopAcquisition();

   /**
    * Kasuje pomiary otagowane tagiem.
    */
   boolean removeTag(Tag tag);

   /**
    * Pobiera czas akwizycji
    * @param timeUnit jednostka
    * @return czas akwizycji
    */
   long getAcquisitionTime(TimeUnit timeUnit);
   
}
