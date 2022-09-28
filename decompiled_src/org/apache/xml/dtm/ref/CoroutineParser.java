package org.apache.xml.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/** @deprecated */
public interface CoroutineParser {
   int getParserCoroutineID();

   CoroutineManager getCoroutineManager();

   void setContentHandler(ContentHandler var1);

   void setLexHandler(LexicalHandler var1);

   Object doParse(InputSource var1, int var2);

   Object doMore(boolean var1, int var2);

   void doTerminate(int var1);

   void init(CoroutineManager var1, int var2, XMLReader var3);
}
