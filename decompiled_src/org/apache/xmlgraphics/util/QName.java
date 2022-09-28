package org.apache.xmlgraphics.util;

import java.io.Serializable;

public class QName implements Serializable {
   private static final long serialVersionUID = -5225376740044770690L;
   private String namespaceURI;
   private String localName;
   private String prefix;
   private int hashCode;

   public QName(String namespaceURI, String prefix, String localName) {
      if (localName == null) {
         throw new NullPointerException("Parameter localName must not be null");
      } else if (localName.length() == 0) {
         throw new IllegalArgumentException("Parameter localName must not be empty");
      } else {
         this.namespaceURI = namespaceURI;
         this.prefix = prefix;
         this.localName = localName;
         this.hashCode = this.toHashString().hashCode();
      }
   }

   public QName(String namespaceURI, String qName) {
      if (qName == null) {
         throw new NullPointerException("Parameter localName must not be null");
      } else if (qName.length() == 0) {
         throw new IllegalArgumentException("Parameter localName must not be empty");
      } else {
         this.namespaceURI = namespaceURI;
         int p = qName.indexOf(58);
         if (p > 0) {
            this.prefix = qName.substring(0, p);
            this.localName = qName.substring(p + 1);
         } else {
            this.prefix = null;
            this.localName = qName;
         }

         this.hashCode = this.toHashString().hashCode();
      }
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getLocalName() {
      return this.localName;
   }

   public String getQName() {
      return this.getPrefix() != null ? this.getPrefix() + ':' + this.getLocalName() : this.getLocalName();
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else {
         if (obj instanceof QName) {
            QName other = (QName)obj;
            if (this.getNamespaceURI() == null && other.getNamespaceURI() == null || this.getNamespaceURI().equals(other.getNamespaceURI())) {
               return this.getLocalName().equals(other.getLocalName());
            }
         }

         return false;
      }
   }

   public String toString() {
      return this.prefix != null ? this.prefix + ":" + this.localName : this.toHashString();
   }

   private String toHashString() {
      return this.namespaceURI != null ? "{" + this.namespaceURI + "}" + this.localName : this.localName;
   }
}
