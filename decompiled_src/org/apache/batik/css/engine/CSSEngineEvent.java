package org.apache.batik.css.engine;

import java.util.EventObject;
import org.w3c.dom.Element;

public class CSSEngineEvent extends EventObject {
   protected Element element;
   protected int[] properties;

   public CSSEngineEvent(CSSEngine var1, Element var2, int[] var3) {
      super(var1);
      this.element = var2;
      this.properties = var3;
   }

   public Element getElement() {
      return this.element;
   }

   public int[] getProperties() {
      return this.properties;
   }
}
