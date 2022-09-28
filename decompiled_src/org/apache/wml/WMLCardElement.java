package org.apache.wml;

public interface WMLCardElement extends WMLElement {
   void setOnEnterBackward(String var1);

   String getOnEnterBackward();

   void setOnEnterForward(String var1);

   String getOnEnterForward();

   void setOnTimer(String var1);

   String getOnTimer();

   void setTitle(String var1);

   String getTitle();

   void setNewContext(boolean var1);

   boolean getNewContext();

   void setOrdered(boolean var1);

   boolean getOrdered();

   void setXmlLang(String var1);

   String getXmlLang();
}
