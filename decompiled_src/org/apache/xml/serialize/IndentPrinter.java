package org.apache.xml.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class IndentPrinter extends Printer {
   private StringBuffer _line = new StringBuffer(80);
   private StringBuffer _text = new StringBuffer(20);
   private int _spaces = 0;
   private int _thisIndent;
   private int _nextIndent;

   public IndentPrinter(Writer var1, OutputFormat var2) {
      super(var1, var2);
      this._thisIndent = this._nextIndent = 0;
   }

   public void enterDTD() {
      if (super._dtdWriter == null) {
         this._line.append(this._text);
         this._text = new StringBuffer(20);
         this.flushLine(false);
         super._dtdWriter = new StringWriter();
         super._docWriter = super._writer;
         super._writer = super._dtdWriter;
      }

   }

   public String leaveDTD() {
      if (super._writer == super._dtdWriter) {
         this._line.append(this._text);
         this._text = new StringBuffer(20);
         this.flushLine(false);
         super._writer = super._docWriter;
         return super._dtdWriter.toString();
      } else {
         return null;
      }
   }

   public void printText(String var1) {
      this._text.append(var1);
   }

   public void printText(StringBuffer var1) {
      this._text.append(var1.toString());
   }

   public void printText(char var1) {
      this._text.append(var1);
   }

   public void printText(char[] var1, int var2, int var3) {
      this._text.append(var1, var2, var3);
   }

   public void printSpace() {
      if (this._text.length() > 0) {
         if (super._format.getLineWidth() > 0 && this._thisIndent + this._line.length() + this._spaces + this._text.length() > super._format.getLineWidth()) {
            this.flushLine(false);

            try {
               super._writer.write(super._format.getLineSeparator());
            } catch (IOException var2) {
               if (super._exception == null) {
                  super._exception = var2;
               }
            }
         }

         while(this._spaces > 0) {
            this._line.append(' ');
            --this._spaces;
         }

         this._line.append(this._text);
         this._text = new StringBuffer(20);
      }

      ++this._spaces;
   }

   public void breakLine() {
      this.breakLine(false);
   }

   public void breakLine(boolean var1) {
      if (this._text.length() > 0) {
         while(true) {
            if (this._spaces <= 0) {
               this._line.append(this._text);
               this._text = new StringBuffer(20);
               break;
            }

            this._line.append(' ');
            --this._spaces;
         }
      }

      this.flushLine(var1);

      try {
         super._writer.write(super._format.getLineSeparator());
      } catch (IOException var3) {
         if (super._exception == null) {
            super._exception = var3;
         }
      }

   }

   public void flushLine(boolean var1) {
      if (this._line.length() > 0) {
         try {
            if (super._format.getIndenting() && !var1) {
               int var2 = this._thisIndent;
               if (2 * var2 > super._format.getLineWidth() && super._format.getLineWidth() > 0) {
                  var2 = super._format.getLineWidth() / 2;
               }

               while(var2 > 0) {
                  super._writer.write(32);
                  --var2;
               }
            }

            this._thisIndent = this._nextIndent;
            this._spaces = 0;
            super._writer.write(this._line.toString());
            this._line = new StringBuffer(40);
         } catch (IOException var4) {
            if (super._exception == null) {
               super._exception = var4;
            }
         }
      }

   }

   public void flush() {
      if (this._line.length() > 0 || this._text.length() > 0) {
         this.breakLine();
      }

      try {
         super._writer.flush();
      } catch (IOException var2) {
         if (super._exception == null) {
            super._exception = var2;
         }
      }

   }

   public void indent() {
      this._nextIndent += super._format.getIndent();
   }

   public void unindent() {
      this._nextIndent -= super._format.getIndent();
      if (this._nextIndent < 0) {
         this._nextIndent = 0;
      }

      if (this._line.length() + this._spaces + this._text.length() == 0) {
         this._thisIndent = this._nextIndent;
      }

   }

   public int getNextIndent() {
      return this._nextIndent;
   }

   public void setNextIndent(int var1) {
      this._nextIndent = var1;
   }

   public void setThisIndent(int var1) {
      this._thisIndent = var1;
   }
}
