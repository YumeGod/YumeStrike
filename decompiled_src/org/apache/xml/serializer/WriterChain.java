package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

interface WriterChain {
   void write(int var1) throws IOException;

   void write(char[] var1) throws IOException;

   void write(char[] var1, int var2, int var3) throws IOException;

   void write(String var1) throws IOException;

   void write(String var1, int var2, int var3) throws IOException;

   void flush() throws IOException;

   void close() throws IOException;

   Writer getWriter();

   OutputStream getOutputStream();
}
