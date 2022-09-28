package org.apache.batik.dom.svg;

import org.w3c.dom.Element;

public class LiveAttributeException extends RuntimeException {
   public static final short ERR_ATTRIBUTE_MISSING = 0;
   public static final short ERR_ATTRIBUTE_MALFORMED = 1;
   public static final short ERR_ATTRIBUTE_NEGATIVE = 2;
   protected Element e;
   protected String attributeName;
   protected short code;
   protected String value;

   public LiveAttributeException(Element var1, String var2, short var3, String var4) {
      this.e = var1;
      this.attributeName = var2;
      this.code = var3;
      this.value = var4;
   }

   public Element getElement() {
      return this.e;
   }

   public String getAttributeName() {
      return this.attributeName;
   }

   public short getCode() {
      return this.code;
   }

   public String getValue() {
      return this.value;
   }
}
