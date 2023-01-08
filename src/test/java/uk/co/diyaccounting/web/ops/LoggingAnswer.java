package uk.co.diyaccounting.web.ops;

import org.easymock.IAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingAnswer<T> implements IAnswer<T>{

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(LoggingAnswer.class);

   /**
    * The answer to be given
    */
   private T t;

   /**
    * Set the answer to be given om the constructor
    * @param t
    */
   public LoggingAnswer(final T t){
      this.t = t;
   }

   /**
    * Answer with the set t
    * 
    * @return the t from the member
    */
   @Override
   public T answer() throws Throwable {
      LoggingAnswer.logger.debug("Answering with: {} {}", this.t.getClass().getSimpleName(), this.t.toString());
      return this.t;
   }

}
