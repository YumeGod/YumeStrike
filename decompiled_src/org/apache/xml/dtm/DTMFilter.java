package org.apache.xml.dtm;

public interface DTMFilter {
   int SHOW_ALL = -1;
   int SHOW_ELEMENT = 1;
   int SHOW_ATTRIBUTE = 2;
   int SHOW_TEXT = 4;
   int SHOW_CDATA_SECTION = 8;
   int SHOW_ENTITY_REFERENCE = 16;
   int SHOW_ENTITY = 32;
   int SHOW_PROCESSING_INSTRUCTION = 64;
   int SHOW_COMMENT = 128;
   int SHOW_DOCUMENT = 256;
   int SHOW_DOCUMENT_TYPE = 512;
   int SHOW_DOCUMENT_FRAGMENT = 1024;
   int SHOW_NOTATION = 2048;
   int SHOW_NAMESPACE = 4096;
   int SHOW_BYFUNCTION = 65536;

   short acceptNode(int var1, int var2);

   short acceptNode(int var1, int var2, int var3);
}
