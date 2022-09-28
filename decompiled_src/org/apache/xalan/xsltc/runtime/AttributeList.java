package org.apache.xalan.xsltc.runtime;

import java.util.Vector;

public class AttributeList implements org.xml.sax.Attributes {
   private static final String EMPTYSTRING = "";
   private static final String CDATASTRING = "CDATA";
   private Hashtable _attributes;
   private Vector _names;
   private Vector _qnames;
   private Vector _values;
   private Vector _uris;
   private int _length;

   public AttributeList() {
      this._length = 0;
   }

   public AttributeList(org.xml.sax.Attributes attributes) {
      this();
      if (attributes != null) {
         int count = attributes.getLength();

         for(int i = 0; i < count; ++i) {
            this.add(attributes.getQName(i), attributes.getValue(i));
         }
      }

   }

   private void alloc() {
      this._attributes = new Hashtable();
      this._names = new Vector();
      this._values = new Vector();
      this._qnames = new Vector();
      this._uris = new Vector();
   }

   public int getLength() {
      return this._length;
   }

   public String getURI(int index) {
      return index < this._length ? (String)this._uris.elementAt(index) : null;
   }

   public String getLocalName(int index) {
      return index < this._length ? (String)this._names.elementAt(index) : null;
   }

   public String getQName(int pos) {
      return pos < this._length ? (String)this._qnames.elementAt(pos) : null;
   }

   public String getType(int index) {
      return "CDATA";
   }

   public int getIndex(String namespaceURI, String localPart) {
      return -1;
   }

   public int getIndex(String qname) {
      return -1;
   }

   public String getType(String uri, String localName) {
      return "CDATA";
   }

   public String getType(String qname) {
      return "CDATA";
   }

   public String getValue(int pos) {
      return pos < this._length ? (String)this._values.elementAt(pos) : null;
   }

   public String getValue(String qname) {
      if (this._attributes != null) {
         Integer obj = (Integer)this._attributes.get(qname);
         return obj == null ? null : this.getValue(obj);
      } else {
         return null;
      }
   }

   public String getValue(String uri, String localName) {
      return this.getValue(uri + ':' + localName);
   }

   public void add(String qname, String value) {
      if (this._attributes == null) {
         this.alloc();
      }

      Integer obj = (Integer)this._attributes.get(qname);
      int col;
      if (obj == null) {
         this._attributes.put(qname, new Integer(this._length++));
         this._qnames.addElement(qname);
         this._values.addElement(value);
         col = qname.lastIndexOf(58);
         if (col > -1) {
            this._uris.addElement(qname.substring(0, col));
            this._names.addElement(qname.substring(col + 1));
         } else {
            this._uris.addElement("");
            this._names.addElement(qname);
         }
      } else {
         col = obj;
         this._values.set(col, value);
      }

   }

   public void clear() {
      this._length = 0;
      if (this._attributes != null) {
         this._attributes.clear();
         this._names.removeAllElements();
         this._values.removeAllElements();
         this._qnames.removeAllElements();
         this._uris.removeAllElements();
      }

   }
}
