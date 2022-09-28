package org.apache.batik.transcoder.svg2svg;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.transcoder.AbstractTranscoder;
import org.apache.batik.transcoder.ErrorHandler;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.IntegerKey;
import org.apache.batik.transcoder.keys.StringKey;
import org.w3c.dom.Document;

public class SVGTranscoder extends AbstractTranscoder {
   public static final ErrorHandler DEFAULT_ERROR_HANDLER = new ErrorHandler() {
      public void error(TranscoderException var1) throws TranscoderException {
         throw var1;
      }

      public void fatalError(TranscoderException var1) throws TranscoderException {
         throw var1;
      }

      public void warning(TranscoderException var1) throws TranscoderException {
      }
   };
   public static final TranscodingHints.Key KEY_NEWLINE = new NewlineKey();
   public static final NewlineValue VALUE_NEWLINE_CR = new NewlineValue("\r");
   public static final NewlineValue VALUE_NEWLINE_CR_LF = new NewlineValue("\r\n");
   public static final NewlineValue VALUE_NEWLINE_LF = new NewlineValue("\n");
   public static final TranscodingHints.Key KEY_FORMAT = new BooleanKey();
   public static final Boolean VALUE_FORMAT_ON;
   public static final Boolean VALUE_FORMAT_OFF;
   public static final TranscodingHints.Key KEY_TABULATION_WIDTH;
   public static final TranscodingHints.Key KEY_DOCUMENT_WIDTH;
   public static final TranscodingHints.Key KEY_DOCTYPE;
   public static final DoctypeValue VALUE_DOCTYPE_CHANGE;
   public static final DoctypeValue VALUE_DOCTYPE_REMOVE;
   public static final DoctypeValue VALUE_DOCTYPE_KEEP_UNCHANGED;
   public static final TranscodingHints.Key KEY_PUBLIC_ID;
   public static final TranscodingHints.Key KEY_SYSTEM_ID;
   public static final TranscodingHints.Key KEY_XML_DECLARATION;

   public SVGTranscoder() {
      this.setErrorHandler(DEFAULT_ERROR_HANDLER);
   }

   public void transcode(TranscoderInput var1, TranscoderOutput var2) throws TranscoderException {
      Object var3 = var1.getReader();
      Writer var4 = var2.getWriter();
      if (var3 == null) {
         Document var5 = var1.getDocument();
         if (var5 == null) {
            throw new Error("Reader or Document expected");
         }

         StringWriter var6 = new StringWriter(1024);

         try {
            DOMUtilities.writeDocument(var5, var6);
         } catch (IOException var8) {
            throw new Error("IO:" + var8.getMessage());
         }

         var3 = new StringReader(var6.toString());
      }

      if (var4 == null) {
         throw new Error("Writer expected");
      } else {
         this.prettyPrint((Reader)var3, var4);
      }
   }

   protected void prettyPrint(Reader var1, Writer var2) throws TranscoderException {
      try {
         PrettyPrinter var3 = new PrettyPrinter();
         NewlineValue var4 = (NewlineValue)this.hints.get(KEY_NEWLINE);
         if (var4 != null) {
            var3.setNewline(var4.getValue());
         }

         Boolean var5 = (Boolean)this.hints.get(KEY_FORMAT);
         if (var5 != null) {
            var3.setFormat(var5);
         }

         Integer var6 = (Integer)this.hints.get(KEY_TABULATION_WIDTH);
         if (var6 != null) {
            var3.setTabulationWidth(var6);
         }

         var6 = (Integer)this.hints.get(KEY_DOCUMENT_WIDTH);
         if (var6 != null) {
            var3.setDocumentWidth(var6);
         }

         DoctypeValue var7 = (DoctypeValue)this.hints.get(KEY_DOCTYPE);
         if (var7 != null) {
            var3.setDoctypeOption(var7.getValue());
         }

         String var8 = (String)this.hints.get(KEY_PUBLIC_ID);
         if (var8 != null) {
            var3.setPublicId(var8);
         }

         var8 = (String)this.hints.get(KEY_SYSTEM_ID);
         if (var8 != null) {
            var3.setSystemId(var8);
         }

         var8 = (String)this.hints.get(KEY_XML_DECLARATION);
         if (var8 != null) {
            var3.setXMLDeclaration(var8);
         }

         var3.print(var1, var2);
         var2.flush();
      } catch (IOException var9) {
         this.getErrorHandler().fatalError(new TranscoderException(var9.getMessage()));
      }

   }

   static {
      VALUE_FORMAT_ON = Boolean.TRUE;
      VALUE_FORMAT_OFF = Boolean.FALSE;
      KEY_TABULATION_WIDTH = new IntegerKey();
      KEY_DOCUMENT_WIDTH = new IntegerKey();
      KEY_DOCTYPE = new DoctypeKey();
      VALUE_DOCTYPE_CHANGE = new DoctypeValue(0);
      VALUE_DOCTYPE_REMOVE = new DoctypeValue(1);
      VALUE_DOCTYPE_KEEP_UNCHANGED = new DoctypeValue(2);
      KEY_PUBLIC_ID = new StringKey();
      KEY_SYSTEM_ID = new StringKey();
      KEY_XML_DECLARATION = new StringKey();
   }

   protected static class DoctypeValue {
      final int value;

      protected DoctypeValue(int var1) {
         this.value = var1;
      }

      public int getValue() {
         return this.value;
      }
   }

   protected static class DoctypeKey extends TranscodingHints.Key {
      public boolean isCompatibleValue(Object var1) {
         return var1 instanceof DoctypeValue;
      }
   }

   protected static class NewlineValue {
      protected final String value;

      protected NewlineValue(String var1) {
         this.value = var1;
      }

      public String getValue() {
         return this.value;
      }
   }

   protected static class NewlineKey extends TranscodingHints.Key {
      public boolean isCompatibleValue(Object var1) {
         return var1 instanceof NewlineValue;
      }
   }
}
