package org.apache.xalan.xsltc.runtime;

public class Parameter {
   public String _name;
   public Object _value;
   public boolean _isDefault;

   public Parameter(String name, Object value) {
      this._name = name;
      this._value = value;
      this._isDefault = true;
   }

   public Parameter(String name, Object value, boolean isDefault) {
      this._name = name;
      this._value = value;
      this._isDefault = isDefault;
   }
}
