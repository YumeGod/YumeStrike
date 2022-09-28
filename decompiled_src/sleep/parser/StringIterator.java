package sleep.parser;

import java.util.LinkedList;

public class StringIterator {
   protected int position;
   protected int lineNo;
   protected char[] text;
   protected String texts;
   protected int begin;
   protected LinkedList mark1;
   protected LinkedList mark2;

   public StringIterator(String var1) {
      this(var1, 0);
   }

   public String toString() {
      return this.texts;
   }

   public StringIterator(String var1, int var2) {
      this.position = 0;
      this.begin = 0;
      this.mark1 = new LinkedList();
      this.mark2 = new LinkedList();
      this.texts = var1;
      this.text = var1.toCharArray();
      this.lineNo = var2;
   }

   public boolean hasNext() {
      return this.position < this.text.length;
   }

   public boolean hasNext(int var1) {
      return this.position + var1 - 1 < this.text.length;
   }

   public int getLineNumber() {
      return this.lineNo;
   }

   public Token getErrorToken() {
      return new Token(this.getEntireLine(), this.getLineNumber(), this.getLineMarker());
   }

   public String getEntireLine() {
      int var1;
      for(var1 = this.position; var1 < this.text.length && this.text[var1] != '\n'; ++var1) {
      }

      return this.texts.substring(this.begin, var1);
   }

   public int getLineMarker() {
      return this.position - this.begin;
   }

   public boolean isNextString(String var1) {
      return this.position + var1.length() <= this.text.length && this.texts.substring(this.position, this.position + var1.length()).equals(var1);
   }

   public boolean isNextChar(char var1) {
      return this.hasNext() && this.text[this.position] == var1;
   }

   public char peek() {
      return this.hasNext() ? this.text[this.position] : '\u0000';
   }

   public void skip(int var1) {
      this.position += var1;
   }

   public String next(int var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(this.next());
      }

      return var2.toString();
   }

   public char next() {
      char var1 = this.text[this.position];
      if (this.position > 0 && this.text[this.position - 1] == '\n') {
         ++this.lineNo;
         this.begin = this.position;
      }

      ++this.position;
      return var1;
   }

   public void mark() {
      this.mark1.add(0, new Integer(this.position));
      this.mark2.add(0, new Integer(this.lineNo));
   }

   public String reset() {
      Integer var1 = (Integer)this.mark1.removeFirst();
      Integer var2 = (Integer)this.mark2.removeFirst();
      return this.texts.substring(var1, this.position);
   }

   public static void main(String[] var0) {
      StringIterator var1 = new StringIterator(var0[0]);
      StringBuffer var2 = new StringBuffer();

      while(var1.hasNext()) {
         char var3 = var1.next();
         var2.append(var3);
         if (var3 == '\n') {
            System.out.print(var1.getLineNumber() + ": " + var2.toString());
            var2 = new StringBuffer();
         }
      }

   }
}
