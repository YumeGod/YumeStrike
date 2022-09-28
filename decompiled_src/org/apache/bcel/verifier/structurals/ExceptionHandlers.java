package org.apache.bcel.verifier.structurals;

import java.util.HashSet;
import java.util.Hashtable;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.MethodGen;

public class ExceptionHandlers {
   private Hashtable exceptionhandlers = new Hashtable();

   public ExceptionHandlers(MethodGen mg) {
      CodeExceptionGen[] cegs = mg.getExceptionHandlers();

      for(int i = 0; i < cegs.length; ++i) {
         ExceptionHandler eh = new ExceptionHandler(cegs[i].getCatchType(), cegs[i].getHandlerPC());

         for(InstructionHandle ih = cegs[i].getStartPC(); ih != cegs[i].getEndPC().getNext(); ih = ih.getNext()) {
            HashSet hs = (HashSet)this.exceptionhandlers.get(ih);
            if (hs == null) {
               hs = new HashSet();
               this.exceptionhandlers.put(ih, hs);
            }

            hs.add(eh);
         }
      }

   }

   public ExceptionHandler[] getExceptionHandlers(InstructionHandle ih) {
      HashSet hs = (HashSet)this.exceptionhandlers.get(ih);
      if (hs == null) {
         return new ExceptionHandler[0];
      } else {
         ExceptionHandler[] ret = new ExceptionHandler[hs.size()];
         return (ExceptionHandler[])hs.toArray(ret);
      }
   }
}
