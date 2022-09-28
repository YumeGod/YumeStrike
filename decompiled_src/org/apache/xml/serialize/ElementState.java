package org.apache.xml.serialize;

import java.util.Hashtable;

public class ElementState {
   public String rawName;
   public String localName;
   public String namespaceURI;
   public boolean preserveSpace;
   public boolean empty;
   public boolean afterElement;
   public boolean afterComment;
   public boolean doCData;
   public boolean unescaped;
   public boolean inCData;
   public Hashtable prefixes;
}
