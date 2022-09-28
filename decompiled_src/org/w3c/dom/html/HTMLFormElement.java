package org.w3c.dom.html;

public interface HTMLFormElement extends HTMLElement {
   HTMLCollection getElements();

   int getLength();

   String getName();

   void setName(String var1);

   String getAcceptCharset();

   void setAcceptCharset(String var1);

   String getAction();

   void setAction(String var1);

   String getEnctype();

   void setEnctype(String var1);

   String getMethod();

   void setMethod(String var1);

   String getTarget();

   void setTarget(String var1);

   void submit();

   void reset();
}
