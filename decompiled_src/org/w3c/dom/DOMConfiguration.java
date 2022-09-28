package org.w3c.dom;

public interface DOMConfiguration {
   void setParameter(String var1, Object var2) throws DOMException;

   Object getParameter(String var1) throws DOMException;

   boolean canSetParameter(String var1, Object var2);

   DOMStringList getParameterNames();
}
