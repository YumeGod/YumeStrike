package net.jsign.commons.cli;

public class UnrecognizedOptionException extends ParseException {
   private static final long serialVersionUID = -252504690284625623L;
   private String option;

   public UnrecognizedOptionException(String message) {
      super(message);
   }

   public UnrecognizedOptionException(String message, String option) {
      this(message);
      this.option = option;
   }

   public String getOption() {
      return this.option;
   }
}
