package com.xmlmind.fo.properties.expression;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class SimpleCharStream {
   public static final boolean staticFlag = false;
   int bufsize;
   int available;
   int tokenBegin;
   public int bufpos;
   protected int[] bufline;
   protected int[] bufcolumn;
   protected int column;
   protected int line;
   protected boolean prevCharIsCR;
   protected boolean prevCharIsLF;
   protected Reader inputStream;
   protected char[] buffer;
   protected int maxNextCharInd;
   protected int inBuf;

   protected void ExpandBuff(boolean var1) {
      char[] var2 = new char[this.bufsize + 2048];
      int[] var3 = new int[this.bufsize + 2048];
      int[] var4 = new int[this.bufsize + 2048];

      try {
         if (var1) {
            System.arraycopy(this.buffer, this.tokenBegin, var2, 0, this.bufsize - this.tokenBegin);
            System.arraycopy(this.buffer, 0, var2, this.bufsize - this.tokenBegin, this.bufpos);
            this.buffer = var2;
            System.arraycopy(this.bufline, this.tokenBegin, var3, 0, this.bufsize - this.tokenBegin);
            System.arraycopy(this.bufline, 0, var3, this.bufsize - this.tokenBegin, this.bufpos);
            this.bufline = var3;
            System.arraycopy(this.bufcolumn, this.tokenBegin, var4, 0, this.bufsize - this.tokenBegin);
            System.arraycopy(this.bufcolumn, 0, var4, this.bufsize - this.tokenBegin, this.bufpos);
            this.bufcolumn = var4;
            this.maxNextCharInd = this.bufpos += this.bufsize - this.tokenBegin;
         } else {
            System.arraycopy(this.buffer, this.tokenBegin, var2, 0, this.bufsize - this.tokenBegin);
            this.buffer = var2;
            System.arraycopy(this.bufline, this.tokenBegin, var3, 0, this.bufsize - this.tokenBegin);
            this.bufline = var3;
            System.arraycopy(this.bufcolumn, this.tokenBegin, var4, 0, this.bufsize - this.tokenBegin);
            this.bufcolumn = var4;
            this.maxNextCharInd = this.bufpos -= this.tokenBegin;
         }
      } catch (Throwable var6) {
         throw new Error(var6.getMessage());
      }

      this.bufsize += 2048;
      this.available = this.bufsize;
      this.tokenBegin = 0;
   }

   protected void FillBuff() throws IOException {
      if (this.maxNextCharInd == this.available) {
         if (this.available == this.bufsize) {
            if (this.tokenBegin > 2048) {
               this.bufpos = this.maxNextCharInd = 0;
               this.available = this.tokenBegin;
            } else if (this.tokenBegin < 0) {
               this.bufpos = this.maxNextCharInd = 0;
            } else {
               this.ExpandBuff(false);
            }
         } else if (this.available > this.tokenBegin) {
            this.available = this.bufsize;
         } else if (this.tokenBegin - this.available < 2048) {
            this.ExpandBuff(true);
         } else {
            this.available = this.tokenBegin;
         }
      }

      try {
         int var1;
         if ((var1 = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd)) == -1) {
            this.inputStream.close();
            throw new IOException();
         } else {
            this.maxNextCharInd += var1;
         }
      } catch (IOException var3) {
         --this.bufpos;
         this.backup(0);
         if (this.tokenBegin == -1) {
            this.tokenBegin = this.bufpos;
         }

         throw var3;
      }
   }

   public char BeginToken() throws IOException {
      this.tokenBegin = -1;
      char var1 = this.readChar();
      this.tokenBegin = this.bufpos;
      return var1;
   }

   protected void UpdateLineColumn(char var1) {
      ++this.column;
      if (this.prevCharIsLF) {
         this.prevCharIsLF = false;
         this.line += this.column = 1;
      } else if (this.prevCharIsCR) {
         this.prevCharIsCR = false;
         if (var1 == '\n') {
            this.prevCharIsLF = true;
         } else {
            this.line += this.column = 1;
         }
      }

      switch (var1) {
         case '\t':
            --this.column;
            this.column += 8 - (this.column & 7);
            break;
         case '\n':
            this.prevCharIsLF = true;
         case '\u000b':
         case '\f':
         default:
            break;
         case '\r':
            this.prevCharIsCR = true;
      }

      this.bufline[this.bufpos] = this.line;
      this.bufcolumn[this.bufpos] = this.column;
   }

   public char readChar() throws IOException {
      if (this.inBuf > 0) {
         --this.inBuf;
         if (++this.bufpos == this.bufsize) {
            this.bufpos = 0;
         }

         return this.buffer[this.bufpos];
      } else {
         if (++this.bufpos >= this.maxNextCharInd) {
            this.FillBuff();
         }

         char var1 = this.buffer[this.bufpos];
         this.UpdateLineColumn(var1);
         return var1;
      }
   }

   /** @deprecated */
   public int getColumn() {
      return this.bufcolumn[this.bufpos];
   }

   /** @deprecated */
   public int getLine() {
      return this.bufline[this.bufpos];
   }

   public int getEndColumn() {
      return this.bufcolumn[this.bufpos];
   }

   public int getEndLine() {
      return this.bufline[this.bufpos];
   }

   public int getBeginColumn() {
      return this.bufcolumn[this.tokenBegin];
   }

   public int getBeginLine() {
      return this.bufline[this.tokenBegin];
   }

   public void backup(int var1) {
      this.inBuf += var1;
      if ((this.bufpos -= var1) < 0) {
         this.bufpos += this.bufsize;
      }

   }

   public SimpleCharStream(Reader var1, int var2, int var3, int var4) {
      this.bufpos = -1;
      this.column = 0;
      this.line = 1;
      this.prevCharIsCR = false;
      this.prevCharIsLF = false;
      this.maxNextCharInd = 0;
      this.inBuf = 0;
      this.inputStream = var1;
      this.line = var2;
      this.column = var3 - 1;
      this.available = this.bufsize = var4;
      this.buffer = new char[var4];
      this.bufline = new int[var4];
      this.bufcolumn = new int[var4];
   }

   public SimpleCharStream(Reader var1, int var2, int var3) {
      this((Reader)var1, var2, var3, 4096);
   }

   public SimpleCharStream(Reader var1) {
      this((Reader)var1, 1, 1, 4096);
   }

   public void ReInit(Reader var1, int var2, int var3, int var4) {
      this.inputStream = var1;
      this.line = var2;
      this.column = var3 - 1;
      if (this.buffer == null || var4 != this.buffer.length) {
         this.available = this.bufsize = var4;
         this.buffer = new char[var4];
         this.bufline = new int[var4];
         this.bufcolumn = new int[var4];
      }

      this.prevCharIsLF = this.prevCharIsCR = false;
      this.tokenBegin = this.inBuf = this.maxNextCharInd = 0;
      this.bufpos = -1;
   }

   public void ReInit(Reader var1, int var2, int var3) {
      this.ReInit((Reader)var1, var2, var3, 4096);
   }

   public void ReInit(Reader var1) {
      this.ReInit((Reader)var1, 1, 1, 4096);
   }

   public SimpleCharStream(InputStream var1, int var2, int var3, int var4) {
      this((Reader)(new InputStreamReader(var1)), var2, var3, 4096);
   }

   public SimpleCharStream(InputStream var1, int var2, int var3) {
      this((InputStream)var1, var2, var3, 4096);
   }

   public SimpleCharStream(InputStream var1) {
      this((InputStream)var1, 1, 1, 4096);
   }

   public void ReInit(InputStream var1, int var2, int var3, int var4) {
      this.ReInit((Reader)(new InputStreamReader(var1)), var2, var3, 4096);
   }

   public void ReInit(InputStream var1) {
      this.ReInit((InputStream)var1, 1, 1, 4096);
   }

   public void ReInit(InputStream var1, int var2, int var3) {
      this.ReInit((InputStream)var1, var2, var3, 4096);
   }

   public String GetImage() {
      return this.bufpos >= this.tokenBegin ? new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1) : new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
   }

   public char[] GetSuffix(int var1) {
      char[] var2 = new char[var1];
      if (this.bufpos + 1 >= var1) {
         System.arraycopy(this.buffer, this.bufpos - var1 + 1, var2, 0, var1);
      } else {
         System.arraycopy(this.buffer, this.bufsize - (var1 - this.bufpos - 1), var2, 0, var1 - this.bufpos - 1);
         System.arraycopy(this.buffer, 0, var2, var1 - this.bufpos - 1, this.bufpos + 1);
      }

      return var2;
   }

   public void Done() {
      this.buffer = null;
      this.bufline = null;
      this.bufcolumn = null;
   }

   public void adjustBeginLineColumn(int var1, int var2) {
      int var3 = this.tokenBegin;
      int var4;
      if (this.bufpos >= this.tokenBegin) {
         var4 = this.bufpos - this.tokenBegin + this.inBuf + 1;
      } else {
         var4 = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
      }

      int var5 = 0;
      int var6 = 0;
      boolean var7 = false;
      boolean var8 = false;

      int var9;
      int var10000;
      for(var9 = 0; var5 < var4; ++var5) {
         var10000 = this.bufline[var6 = var3 % this.bufsize];
         ++var3;
         int var10;
         if (var10000 != this.bufline[var10 = var3 % this.bufsize]) {
            break;
         }

         this.bufline[var6] = var1;
         int var11 = var9 + this.bufcolumn[var10] - this.bufcolumn[var6];
         this.bufcolumn[var6] = var2 + var9;
         var9 = var11;
      }

      if (var5 < var4) {
         this.bufline[var6] = var1++;
         this.bufcolumn[var6] = var2 + var9;

         while(var5++ < var4) {
            var10000 = this.bufline[var6 = var3 % this.bufsize];
            ++var3;
            if (var10000 != this.bufline[var3 % this.bufsize]) {
               this.bufline[var6] = var1++;
            } else {
               this.bufline[var6] = var1;
            }
         }
      }

      this.line = this.bufline[var6];
      this.column = this.bufcolumn[var6];
   }
}
