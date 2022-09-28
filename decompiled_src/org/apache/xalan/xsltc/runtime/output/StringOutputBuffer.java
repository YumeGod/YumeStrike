package org.apache.xalan.xsltc.runtime.output;

class StringOutputBuffer implements OutputBuffer {
   private StringBuffer _buffer = new StringBuffer();

   public StringOutputBuffer() {
   }

   public String close() {
      return this._buffer.toString();
   }

   public OutputBuffer append(String s) {
      this._buffer.append(s);
      return this;
   }

   public OutputBuffer append(char[] s, int from, int to) {
      this._buffer.append(s, from, to);
      return this;
   }

   public OutputBuffer append(char ch) {
      this._buffer.append(ch);
      return this;
   }
}
