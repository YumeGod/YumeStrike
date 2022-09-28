package org.xml.sax;

public interface XMLFilter extends XMLReader {
   void setParent(XMLReader var1);

   XMLReader getParent();
}
