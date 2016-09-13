package se.bitsplz.jpa.exception;

public final class ECommerceException extends Exception{
   
   private static final long serialVersionUID = -2207545457962428740L;
   
   
   public ECommerceException(String message, Throwable cause){
      super(message, cause);
   }
   
   
   public ECommerceException(String message){
      super(message);
   }
   
   
   public ECommerceException(Throwable cause){
      super(cause);
   }
}
