package org.apache.xml.serializer;

import org.xml.sax.Attributes;

public interface SerializerTrace {
   int EVENTTYPE_STARTDOCUMENT = 1;
   int EVENTTYPE_ENDDOCUMENT = 2;
   int EVENTTYPE_STARTELEMENT = 3;
   int EVENTTYPE_ENDELEMENT = 4;
   int EVENTTYPE_CHARACTERS = 5;
   int EVENTTYPE_IGNORABLEWHITESPACE = 6;
   int EVENTTYPE_PI = 7;
   int EVENTTYPE_COMMENT = 8;
   int EVENTTYPE_ENTITYREF = 9;
   int EVENTTYPE_CDATA = 10;
   int EVENTTYPE_OUTPUT_PSEUDO_CHARACTERS = 11;
   int EVENTTYPE_OUTPUT_CHARACTERS = 12;

   boolean hasTraceListeners();

   void fireGenerateEvent(int var1);

   void fireGenerateEvent(int var1, String var2, Attributes var3);

   void fireGenerateEvent(int var1, char[] var2, int var3, int var4);

   void fireGenerateEvent(int var1, String var2, String var3);

   void fireGenerateEvent(int var1, String var2);
}
