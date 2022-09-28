package org.apache.xalan.xsltc.compiler;

final class QName {
   private final String _localname;
   private String _prefix;
   private String _namespace;
   private String _stringRep;
   private int _hashCode;

   public QName(String namespace, String prefix, String localname) {
      this._namespace = namespace;
      this._prefix = prefix;
      this._localname = localname;
      this._stringRep = namespace != null && !namespace.equals("") ? namespace + ':' + localname : localname;
      this._hashCode = this._stringRep.hashCode() + 19;
   }

   public void clearNamespace() {
      this._namespace = "";
   }

   public String toString() {
      return this._stringRep;
   }

   public String getStringRep() {
      return this._stringRep;
   }

   public boolean equals(Object other) {
      return this == other;
   }

   public String getLocalPart() {
      return this._localname;
   }

   public String getNamespace() {
      return this._namespace;
   }

   public String getPrefix() {
      return this._prefix;
   }

   public int hashCode() {
      return this._hashCode;
   }

   public String dump() {
      return new String("QName: " + this._namespace + "(" + this._prefix + "):" + this._localname);
   }
}
