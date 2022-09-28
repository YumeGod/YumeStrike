package org.apache.batik.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.batik.util.io.NormalizingReader;
import org.apache.batik.util.io.StreamNormalizingReader;
import org.apache.batik.util.io.StringNormalizingReader;

public abstract class AbstractScanner {
   protected NormalizingReader reader;
   protected int current;
   protected char[] buffer = new char[128];
   protected int position;
   protected int type;
   protected int previousType;
   protected int start;
   protected int end;
   protected int blankCharacters;

   public AbstractScanner(Reader var1) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new ParseException(var3);
      }
   }

   public AbstractScanner(InputStream var1, String var2) throws ParseException {
      try {
         this.reader = new StreamNormalizingReader(var1, var2);
         this.current = this.nextChar();
      } catch (IOException var4) {
         throw new ParseException(var4);
      }
   }

   public AbstractScanner(String var1) throws ParseException {
      try {
         this.reader = new StringNormalizingReader(var1);
         this.current = this.nextChar();
      } catch (IOException var3) {
         throw new ParseException(var3);
      }
   }

   public int getLine() {
      return this.reader.getLine();
   }

   public int getColumn() {
      return this.reader.getColumn();
   }

   public char[] getBuffer() {
      return this.buffer;
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }

   public void clearBuffer() {
      if (this.position <= 0) {
         this.position = 0;
      } else {
         this.buffer[0] = this.buffer[this.position - 1];
         this.position = 1;
      }

   }

   public int getType() {
      return this.type;
   }

   public String getStringValue() {
      return new String(this.buffer, this.start, this.end - this.start);
   }

   public int next() throws ParseException {
      this.blankCharacters = 0;
      this.start = this.position - 1;
      this.previousType = this.type;
      this.nextToken();
      this.end = this.position - this.endGap();
      return this.type;
   }

   protected abstract int endGap();

   protected abstract void nextToken() throws ParseException;

   protected static boolean isEqualIgnoreCase(int var0, char var1) {
      return var0 == -1 ? false : Character.toLowerCase((char)var0) == var1;
   }

   protected int nextChar() throws IOException {
      this.current = this.reader.read();
      if (this.current == -1) {
         return this.current;
      } else {
         if (this.position == this.buffer.length) {
            char[] var1 = new char[1 + this.position + this.position / 2];
            System.arraycopy(this.buffer, 0, var1, 0, this.position);
            this.buffer = var1;
         }

         return this.buffer[this.position++] = (char)this.current;
      }
   }
}
