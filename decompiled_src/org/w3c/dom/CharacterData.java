package org.w3c.dom;

public interface CharacterData extends Node {
   String getData() throws DOMException;

   void setData(String var1) throws DOMException;

   int getLength();

   String substringData(int var1, int var2) throws DOMException;

   void appendData(String var1) throws DOMException;

   void insertData(int var1, String var2) throws DOMException;

   void deleteData(int var1, int var2) throws DOMException;

   void replaceData(int var1, int var2, String var3) throws DOMException;
}
