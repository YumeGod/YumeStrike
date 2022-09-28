package org.apache.xml.utils;

import java.util.Locale;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

public interface XMLString {
   void dispatchCharactersEvents(ContentHandler var1) throws SAXException;

   void dispatchAsComment(LexicalHandler var1) throws SAXException;

   XMLString fixWhiteSpace(boolean var1, boolean var2, boolean var3);

   int length();

   char charAt(int var1);

   void getChars(int var1, int var2, char[] var3, int var4);

   boolean equals(XMLString var1);

   boolean equals(Object var1);

   boolean equalsIgnoreCase(String var1);

   int compareTo(XMLString var1);

   int compareToIgnoreCase(XMLString var1);

   boolean startsWith(String var1, int var2);

   boolean startsWith(XMLString var1, int var2);

   boolean startsWith(String var1);

   boolean startsWith(XMLString var1);

   boolean endsWith(String var1);

   int hashCode();

   int indexOf(int var1);

   int indexOf(int var1, int var2);

   int lastIndexOf(int var1);

   int lastIndexOf(int var1, int var2);

   int indexOf(String var1);

   int indexOf(XMLString var1);

   int indexOf(String var1, int var2);

   int lastIndexOf(String var1);

   int lastIndexOf(String var1, int var2);

   XMLString substring(int var1);

   XMLString substring(int var1, int var2);

   XMLString concat(String var1);

   XMLString toLowerCase(Locale var1);

   XMLString toLowerCase();

   XMLString toUpperCase(Locale var1);

   XMLString toUpperCase();

   XMLString trim();

   String toString();

   boolean hasString();

   double toDouble();
}
