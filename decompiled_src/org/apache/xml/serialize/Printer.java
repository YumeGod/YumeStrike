package org.apache.xml.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Printer {
   protected final OutputFormat _format;
   protected Writer _writer;
   protected StringWriter _dtdWriter;
   protected Writer _docWriter;
   protected IOException _exception;
   private static final int BufferSize = 4096;
   private final char[] _buffer = new char[4096];
   private int _pos = 0;

   public Printer(Writer var1, OutputFormat var2) {
      this._writer = var1;
      this._format = var2;
      this._exception = null;
      this._dtdWriter = null;
      this._docWriter = null;
      this._pos = 0;
   }

   public IOException getException() {
      return this._exception;
   }

   public void enterDTD() throws IOException {
      if (this._dtdWriter == null) {
         this.flushLine(false);
         this._dtdWriter = new StringWriter();
         this._docWriter = this._writer;
         this._writer = this._dtdWriter;
      }

   }

   public String leaveDTD() throws IOException {
      if (this._writer == this._dtdWriter) {
         this.flushLine(false);
         this._writer = this._docWriter;
         return this._dtdWriter.toString();
      } else {
         return null;
      }
   }

   public void printText(String var1) throws IOException {
      try {
         int var2 = var1.length();

         for(int var3 = 0; var3 < var2; ++var3) {
            if (this._pos == 4096) {
               this._writer.write(this._buffer);
               this._pos = 0;
            }

            this._buffer[this._pos] = var1.charAt(var3);
            ++this._pos;
         }

      } catch (IOException var4) {
         if (this._exception == null) {
            this._exception = var4;
         }

         throw var4;
      }
   }

   public void printText(StringBuffer var1) throws IOException {
      try {
         int var2 = var1.length();

         for(int var3 = 0; var3 < var2; ++var3) {
            if (this._pos == 4096) {
               this._writer.write(this._buffer);
               this._pos = 0;
            }

            this._buffer[this._pos] = var1.charAt(var3);
            ++this._pos;
         }

      } catch (IOException var4) {
         if (this._exception == null) {
            this._exception = var4;
         }

         throw var4;
      }
   }

   public void printText(char[] var1, int var2, int var3) throws IOException {
      try {
         while(var3-- > 0) {
            if (this._pos == 4096) {
               this._writer.write(this._buffer);
               this._pos = 0;
            }

            this._buffer[this._pos] = var1[var2];
            ++var2;
            ++this._pos;
         }

      } catch (IOException var5) {
         if (this._exception == null) {
            this._exception = var5;
         }

         throw var5;
      }
   }

   public void printText(char var1) throws IOException {
      try {
         if (this._pos == 4096) {
            this._writer.write(this._buffer);
            this._pos = 0;
         }

         this._buffer[this._pos] = var1;
         ++this._pos;
      } catch (IOException var3) {
         if (this._exception == null) {
            this._exception = var3;
         }

         throw var3;
      }
   }

   public void printSpace() throws IOException {
      try {
         if (this._pos == 4096) {
            this._writer.write(this._buffer);
            this._pos = 0;
         }

         this._buffer[this._pos] = ' ';
         ++this._pos;
      } catch (IOException var2) {
         if (this._exception == null) {
            this._exception = var2;
         }

         throw var2;
      }
   }

   public void breakLine() throws IOException {
      try {
         if (this._pos == 4096) {
            this._writer.write(this._buffer);
            this._pos = 0;
         }

         this._buffer[this._pos] = '\n';
         ++this._pos;
      } catch (IOException var2) {
         if (this._exception == null) {
            this._exception = var2;
         }

         throw var2;
      }
   }

   public void breakLine(boolean var1) throws IOException {
      this.breakLine();
   }

   public void flushLine(boolean var1) throws IOException {
      try {
         this._writer.write(this._buffer, 0, this._pos);
      } catch (IOException var3) {
         if (this._exception == null) {
            this._exception = var3;
         }
      }

      this._pos = 0;
   }

   public void flush() throws IOException {
      try {
         this._writer.write(this._buffer, 0, this._pos);
         this._writer.flush();
      } catch (IOException var2) {
         if (this._exception == null) {
            this._exception = var2;
         }

         throw var2;
      }

      this._pos = 0;
   }

   public void indent() {
   }

   public void unindent() {
   }

   public int getNextIndent() {
      return 0;
   }

   public void setNextIndent(int var1) {
   }

   public void setThisIndent(int var1) {
   }
}
