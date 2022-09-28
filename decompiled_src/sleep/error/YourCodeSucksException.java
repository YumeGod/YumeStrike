package sleep.error;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;

public class YourCodeSucksException extends RuntimeException {
   LinkedList allErrors;

   public YourCodeSucksException(LinkedList var1) {
      this.allErrors = var1;
   }

   public String getMessage() {
      StringBuffer var1 = new StringBuffer(this.allErrors.size() + " error(s): ");
      Iterator var2 = this.getErrors().iterator();

      while(var2.hasNext()) {
         SyntaxError var3 = (SyntaxError)var2.next();
         var1.append(var3.getDescription());
         var1.append(" at " + var3.getLineNumber());
         if (var2.hasNext()) {
            var1.append("; ");
         }
      }

      return var1.toString();
   }

   public String toString() {
      return "YourCodeSucksException: " + this.getMessage();
   }

   public void printErrors(OutputStream var1) {
      PrintWriter var2 = new PrintWriter(var1);
      var2.print(this.formatErrors());
      var2.flush();
   }

   public String formatErrors() {
      StringBuffer var1 = new StringBuffer();
      LinkedList var2 = this.getErrors();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         SyntaxError var4 = (SyntaxError)var3.next();
         var1.append("Error: " + var4.getDescription() + " at line " + var4.getLineNumber() + "\n");
         var1.append("       " + var4.getCodeSnippet() + "\n");
         if (var4.getMarker() != null) {
            var1.append("       " + var4.getMarker() + "\n");
         }
      }

      return var1.toString();
   }

   public LinkedList getErrors() {
      return this.allErrors;
   }
}
