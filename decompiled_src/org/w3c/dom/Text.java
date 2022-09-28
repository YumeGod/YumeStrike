package org.w3c.dom;

public interface Text extends CharacterData {
   Text splitText(int var1) throws DOMException;

   boolean isElementContentWhitespace();

   String getWholeText();

   Text replaceWholeText(String var1) throws DOMException;
}
