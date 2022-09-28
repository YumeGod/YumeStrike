package org.apache.batik.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import org.apache.batik.util.EncodingUtilities;

public class StreamNormalizingReader extends NormalizingReader {
   protected CharDecoder charDecoder;
   protected int nextChar;
   protected int line;
   protected int column;
   protected static final Map charDecoderFactories = new HashMap(11);

   public StreamNormalizingReader(InputStream var1) throws IOException {
      this(var1, (String)null);
   }

   public StreamNormalizingReader(InputStream var1, String var2) throws IOException {
      this.nextChar = -1;
      this.line = 1;
      if (var2 == null) {
         var2 = "ISO-8859-1";
      }

      this.charDecoder = this.createCharDecoder(var1, var2);
   }

   public StreamNormalizingReader(Reader var1) throws IOException {
      this.nextChar = -1;
      this.line = 1;
      this.charDecoder = new GenericDecoder(var1);
   }

   protected StreamNormalizingReader() {
      this.nextChar = -1;
      this.line = 1;
   }

   public int read() throws IOException {
      int var1 = this.nextChar;
      if (var1 != -1) {
         this.nextChar = -1;
         if (var1 == 13) {
            this.column = 0;
            ++this.line;
         } else {
            ++this.column;
         }

         return var1;
      } else {
         var1 = this.charDecoder.readChar();
         switch (var1) {
            case 10:
               this.column = 0;
               ++this.line;
            default:
               return var1;
            case 13:
               this.column = 0;
               ++this.line;
               int var2 = this.charDecoder.readChar();
               if (var2 == 10) {
                  return 10;
               } else {
                  this.nextChar = var2;
                  return 10;
               }
         }
      }
   }

   public int getLine() {
      return this.line;
   }

   public int getColumn() {
      return this.column;
   }

   public void close() throws IOException {
      this.charDecoder.dispose();
      this.charDecoder = null;
   }

   protected CharDecoder createCharDecoder(InputStream var1, String var2) throws IOException {
      CharDecoderFactory var3 = (CharDecoderFactory)charDecoderFactories.get(var2.toUpperCase());
      if (var3 != null) {
         return var3.createCharDecoder(var1);
      } else {
         String var4 = EncodingUtilities.javaEncoding(var2);
         if (var4 == null) {
            var4 = var2;
         }

         return new GenericDecoder(var1, var4);
      }
   }

   static {
      ASCIIDecoderFactory var0 = new ASCIIDecoderFactory();
      charDecoderFactories.put("ASCII", var0);
      charDecoderFactories.put("US-ASCII", var0);
      charDecoderFactories.put("ISO-8859-1", new ISO_8859_1DecoderFactory());
      charDecoderFactories.put("UTF-8", new UTF8DecoderFactory());
      charDecoderFactories.put("UTF-16", new UTF16DecoderFactory());
   }

   protected static class UTF16DecoderFactory implements CharDecoderFactory {
      public CharDecoder createCharDecoder(InputStream var1) throws IOException {
         return new UTF16Decoder(var1);
      }
   }

   protected static class UTF8DecoderFactory implements CharDecoderFactory {
      public CharDecoder createCharDecoder(InputStream var1) throws IOException {
         return new UTF8Decoder(var1);
      }
   }

   protected static class ISO_8859_1DecoderFactory implements CharDecoderFactory {
      public CharDecoder createCharDecoder(InputStream var1) throws IOException {
         return new ISO_8859_1Decoder(var1);
      }
   }

   protected static class ASCIIDecoderFactory implements CharDecoderFactory {
      public CharDecoder createCharDecoder(InputStream var1) throws IOException {
         return new ASCIIDecoder(var1);
      }
   }

   protected interface CharDecoderFactory {
      CharDecoder createCharDecoder(InputStream var1) throws IOException;
   }
}
