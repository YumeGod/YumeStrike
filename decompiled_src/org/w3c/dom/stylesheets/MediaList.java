package org.w3c.dom.stylesheets;

import org.w3c.dom.DOMException;

public interface MediaList {
   String getMediaText();

   void setMediaText(String var1) throws DOMException;

   int getLength();

   String item(int var1);

   void deleteMedium(String var1) throws DOMException;

   void appendMedium(String var1) throws DOMException;
}
