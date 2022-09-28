package org.apache.batik.css.engine.value;

import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMException;

public abstract class AbstractValueFactory {
   public abstract String getPropertyName();

   protected static String resolveURI(ParsedURL var0, String var1) {
      return (new ParsedURL(var0, var1)).toString();
   }

   protected DOMException createInvalidIdentifierDOMException(String var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), var1};
      String var3 = Messages.formatMessage("invalid.identifier", var2);
      return new DOMException((short)12, var3);
   }

   protected DOMException createInvalidLexicalUnitDOMException(short var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), new Integer(var1)};
      String var3 = Messages.formatMessage("invalid.lexical.unit", var2);
      return new DOMException((short)9, var3);
   }

   protected DOMException createInvalidFloatTypeDOMException(short var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), new Integer(var1)};
      String var3 = Messages.formatMessage("invalid.float.type", var2);
      return new DOMException((short)15, var3);
   }

   protected DOMException createInvalidFloatValueDOMException(float var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), new Float(var1)};
      String var3 = Messages.formatMessage("invalid.float.value", var2);
      return new DOMException((short)15, var3);
   }

   protected DOMException createInvalidStringTypeDOMException(short var1) {
      Object[] var2 = new Object[]{this.getPropertyName(), new Integer(var1)};
      String var3 = Messages.formatMessage("invalid.string.type", var2);
      return new DOMException((short)15, var3);
   }

   protected DOMException createMalformedLexicalUnitDOMException() {
      Object[] var1 = new Object[]{this.getPropertyName()};
      String var2 = Messages.formatMessage("malformed.lexical.unit", var1);
      return new DOMException((short)15, var2);
   }

   protected DOMException createDOMException() {
      Object[] var1 = new Object[]{this.getPropertyName()};
      String var2 = Messages.formatMessage("invalid.access", var1);
      return new DOMException((short)9, var2);
   }
}
