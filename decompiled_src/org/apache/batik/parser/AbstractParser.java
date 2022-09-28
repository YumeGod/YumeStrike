package org.apache.batik.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.io.NormalizingReader;
import org.apache.batik.util.io.StreamNormalizingReader;
import org.apache.batik.util.io.StringNormalizingReader;

public abstract class AbstractParser implements Parser {
   public static final String BUNDLE_CLASSNAME = "org.apache.batik.parser.resources.Messages";
   protected ErrorHandler errorHandler = new DefaultErrorHandler();
   protected LocalizableSupport localizableSupport;
   protected NormalizingReader reader;
   protected int current;
   // $FF: synthetic field
   static Class class$org$apache$batik$parser$AbstractParser;

   public AbstractParser() {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.parser.resources.Messages", (class$org$apache$batik$parser$AbstractParser == null ? (class$org$apache$batik$parser$AbstractParser = class$("org.apache.batik.parser.AbstractParser")) : class$org$apache$batik$parser$AbstractParser).getClassLoader());
   }

   public int getCurrent() {
      return this.current;
   }

   public void setLocale(Locale var1) {
      this.localizableSupport.setLocale(var1);
   }

   public Locale getLocale() {
      return this.localizableSupport.getLocale();
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      return this.localizableSupport.formatMessage(var1, var2);
   }

   public void setErrorHandler(ErrorHandler var1) {
      this.errorHandler = var1;
   }

   public void parse(Reader var1) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1);
         this.doParse();
      } catch (IOException var3) {
         this.errorHandler.error(new ParseException(this.createErrorMessage("io.exception", (Object[])null), var3));
      }

   }

   public void parse(InputStream var1, String var2) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1, var2);
         this.doParse();
      } catch (IOException var4) {
         this.errorHandler.error(new ParseException(this.createErrorMessage("io.exception", (Object[])null), var4));
      }

   }

   public void parse(String var1) throws ParseException {
      try {
         this.reader = new StringNormalizingReader(var1);
         this.doParse();
      } catch (IOException var3) {
         this.errorHandler.error(new ParseException(this.createErrorMessage("io.exception", (Object[])null), var3));
      }

   }

   protected abstract void doParse() throws ParseException, IOException;

   protected void reportError(String var1, Object[] var2) throws ParseException {
      this.errorHandler.error(new ParseException(this.createErrorMessage(var1, var2), this.reader.getLine(), this.reader.getColumn()));
   }

   protected void reportCharacterExpectedError(char var1, int var2) {
      this.reportError("character.expected", new Object[]{new Character(var1), new Integer(var2)});
   }

   protected void reportUnexpectedCharacterError(int var1) {
      this.reportError("character.unexpected", new Object[]{new Integer(var1)});
   }

   protected String createErrorMessage(String var1, Object[] var2) {
      try {
         return this.formatMessage(var1, var2);
      } catch (MissingResourceException var4) {
         return var1;
      }
   }

   protected String getBundleClassName() {
      return "org.apache.batik.parser.resources.Messages";
   }

   protected void skipSpaces() throws IOException {
      while(true) {
         switch (this.current) {
            case 9:
            case 10:
            case 13:
            case 32:
               this.current = this.reader.read();
               break;
            default:
               return;
         }
      }
   }

   protected void skipCommaSpaces() throws IOException {
      while(true) {
         switch (this.current) {
            case 9:
            case 10:
            case 13:
            case 32:
               this.current = this.reader.read();
               break;
            default:
               if (this.current == 44) {
                  while(true) {
                     switch (this.current = this.reader.read()) {
                        case 9:
                        case 10:
                        case 13:
                        case 32:
                           break;
                        default:
                           return;
                     }
                  }
               }

               return;
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
