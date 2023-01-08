package uk.co.diyaccounting.web.service;

/**
 * An exception to be thrown when content cannot be read. This typically occurs when populating a content item because
 * either the underlying source cannot be read at all or the content path does not exist within that document
 */
public final class CatalogueServiceException extends Exception {

	private static final long serialVersionUID = 1L;

   /**
    * Constructs a new exception with null as its detail message.
    * 
    * @param message
    *           the detail message
    * @param cause
    *           the underlying cause to be wrapped by this exception
    * 
    * @see Exception#Exception(Throwable)
    */
   public CatalogueServiceException(final String message, final Throwable cause) {
      super(message, cause);
   }

}
