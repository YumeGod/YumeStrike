package org.apache.wml;

public interface WMLMetaElement extends WMLElement {
   void setName(String var1);

   String getName();

   void setHttpEquiv(String var1);

   String getHttpEquiv();

   void setForua(boolean var1);

   boolean getForua();

   void setScheme(String var1);

   String getScheme();

   void setContent(String var1);

   String getContent();
}
