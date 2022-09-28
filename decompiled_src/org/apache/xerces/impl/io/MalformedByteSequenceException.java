package org.apache.xerces.impl.io;

import java.io.CharConversionException;
import java.util.Locale;
import org.apache.xerces.util.MessageFormatter;

public class MalformedByteSequenceException extends CharConversionException {
   static final long serialVersionUID = 8436382245048328739L;
   private MessageFormatter fFormatter;
   private Locale fLocale;
   private String fDomain;
   private String fKey;
   private Object[] fArguments;
   private String fMessage;

   public MalformedByteSequenceException(MessageFormatter var1, Locale var2, String var3, String var4, Object[] var5) {
      this.fFormatter = var1;
      this.fLocale = var2;
      this.fDomain = var3;
      this.fKey = var4;
      this.fArguments = var5;
   }

   public String getDomain() {
      return this.fDomain;
   }

   public String getKey() {
      return this.fKey;
   }

   public Object[] getArguments() {
      return this.fArguments;
   }

   public String getMessage() {
      if (this.fMessage == null) {
         this.fMessage = this.fFormatter.formatMessage(this.fLocale, this.fKey, this.fArguments);
         this.fFormatter = null;
         this.fLocale = null;
      }

      return this.fMessage;
   }
}
