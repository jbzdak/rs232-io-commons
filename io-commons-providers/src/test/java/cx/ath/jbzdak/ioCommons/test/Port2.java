package cx.ath.jbzdak.ioCommons.test;

import cx.ath.jbzdak.ioCommons.Port;
import cx.ath.jbzdak.ioCommons.PortException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Jacek Bzdak
 */
public class Port2 extends Port{
   @Override
   public void open() throws PortException {

   }

   @Override
   public void close() throws PortException {

   }

   @Override
   public void writeToOutput(byte[] data) throws IOException {

   }

   @Override
   public InputStream getIn() {
      return null;
   }
}
