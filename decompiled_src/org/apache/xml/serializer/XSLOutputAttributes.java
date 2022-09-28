package org.apache.xml.serializer;

import java.util.Vector;

interface XSLOutputAttributes {
   String getDoctypePublic();

   String getDoctypeSystem();

   String getEncoding();

   boolean getIndent();

   int getIndentAmount();

   String getMediaType();

   boolean getOmitXMLDeclaration();

   String getStandalone();

   String getVersion();

   void setCdataSectionElements(Vector var1);

   void setDoctype(String var1, String var2);

   void setDoctypePublic(String var1);

   void setDoctypeSystem(String var1);

   void setEncoding(String var1);

   void setIndent(boolean var1);

   void setMediaType(String var1);

   void setOmitXMLDeclaration(boolean var1);

   void setStandalone(String var1);

   void setVersion(String var1);
}
