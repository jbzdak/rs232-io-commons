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

   Future<?> currentCommand;

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
   public <T> T executeCommand(Command<T> command) throws IOException, InterruptedException {
      try {
         lock.lock();
         currentCommand = service.schedule(new CommandCallable<T>(command), 0, TimeUnit.MILLISECONDS);
         timeoutTask = new TimeoutTask();
         timeoutTimer.schedule(timeoutTask, timeout);
         condition.await();
         if(currentCommand.isCancelled()){
            return null;
         }
         try {
            return (T) currentCommand.get();
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

   class CommandCallable<T> implements Callable<T>{

      final Command<T> command;

      public CommandCallable(Command<T> command) {
         this.command = command;
      }

      @Override
      public T call() throws Exception {
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
