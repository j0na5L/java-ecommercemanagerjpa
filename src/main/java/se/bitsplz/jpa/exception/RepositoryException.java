package se.bitsplz.jpa.exception;

public final class RepositoryException extends Exception{
   
   private static final long serialVersionUID = -5915903939711775111L;
   
   
   public RepositoryException(String message, Throwable cause){
      super(message, cause);
   }
   
   
   public RepositoryException(String message){
      super(message);
   }
   
   
   public RepositoryException(Throwable cause){
      super(cause);
   }
}
