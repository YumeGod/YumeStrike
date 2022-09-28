package org.apache.xalan.xsltc.runtime.output;

interface OutputBuffer {
   String close();

   OutputBuffer append(char var1);

   OutputBuffer append(String var1);

   OutputBuffer append(char[] var1, int var2, int var3);
}
