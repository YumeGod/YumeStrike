package org.apache.fop.fonts;

public interface SingleByteEncoding {
   char NOT_FOUND_CODE_POINT = '\u0000';

   String getName();

   char mapChar(char var1);

   String[] getCharNameMap();

   char[] getUnicodeCharMap();
}
