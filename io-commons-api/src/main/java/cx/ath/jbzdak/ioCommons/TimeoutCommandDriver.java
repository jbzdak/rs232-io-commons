/*
 * Copyright for Jacek Bzdak 2011.
 *
 *     This file is part of Serial ioCommons, utility library to do serial
 *     port communication using native APIs and JNA to bind them to java.
 *
 *     Serial ioCommons is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Serial ioCommons is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package cx.ath.jbzdak.ioCommons;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jacek Bzdak jbzdak@gmail.com
 *         Date: Jan 20, 2010
 */
public class TimeoutCommandDriver implements CommandDriver{

   private final long timeout;

   private final SimpleCommandDriver driver;

   private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

   final ReentrantLock lock = new ReentrantLock();

   final Condition condition = lock.newCondition();

   final Timer timeoutTimer = new Timer("Command driver timeout timer");

   TimerTask timeoutTask;

   Future<String> currentCommand;

   public TimeoutCommandDriver(Port port, ResponseReader reader, int timeout, TimeUnit timeUnit) {
      this.timeout = timeUnit.toMillis(timeout);
      if(this.timeout <= 0){
         throw new IllegalArgumentException("Timeout passed to TimeoutCommandDriver must be positive");
      }
      driver = new SimpleCommandDriver(port, reader);
   }

   @Override
   public void dispose() {
      driver.dispose();
      service.shutdownNow();
      timeoutTimer.cancel();
   }

   @Override
   public String executeCommand(Command command) throws IOException, InterruptedException {
      try {
         lock.lock();
         currentCommand = service.schedule(new CommandCallable(command), 0, TimeUnit.MILLISECONDS);
         timeoutTask = new TimeoutTask();
         timeoutTimer.schedule(timeoutTask, timeout);
         condition.await();
         if(currentCommand.isCancelled()){
            return null;
         }
         try {
            return currentCommand.get();
         } catch (ExecutionException e) {
            if (e.getCause() instanceof RuntimeException) {
               RuntimeException runtimeException = (RuntimeException) e.getCause();
               throw runtimeException;
            }else{
               throw new RuntimeException(e.getCause());
            }
         }
      } finally {
         lock.unlock();
      }
   }

   class CommandCallable implements Callable<String>{

      final Command command;

      public CommandCallable(Command command) {
         this.command = command;
      }

      @Override
      public String call() throws Exception {
         try{
            return driver.executeCommand(command);
         }finally {
            try {
               lock.lock();
               timeoutTask.cancel();
               condition.signalAll();
            } finally {
               lock.unlock();
            }

         }
      }
   }

   class TimeoutTask extends TimerTask{
      @Override
      public void run() {
         try {
            lock.lock();
            if(currentCommand.isDone()){
               return;
            }
            currentCommand.cancel(true);
            condition.signalAll();
         } finally {
            lock.unlock();
         }
      }
   }

}
