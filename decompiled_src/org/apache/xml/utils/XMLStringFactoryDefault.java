package org.apache.xml.utils;

public class XMLStringFactoryDefault extends XMLStringFactory {
   private static final XMLStringDefault EMPTY_STR = new XMLStringDefault("");

   public XMLString newstr(String string) {
      return new XMLStringDefault(string);
   }

   public XMLString newstr(FastStringBuffer fsb, int start, int length) {
      return new XMLStringDefault(fsb.getString(start, length));
   }

   public XMLString newstr(char[] string, int start, int length) {
      return new XMLStringDefault(new String(string, start, length));
   }

   public XMLString emptystr() {
      return EMPTY_STR;
   }
}
