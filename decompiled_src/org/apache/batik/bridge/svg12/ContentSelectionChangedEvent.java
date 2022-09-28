package org.apache.batik.bridge.svg12;

import java.util.EventObject;
import org.apache.batik.dom.svg12.XBLOMContentElement;

public class ContentSelectionChangedEvent extends EventObject {
   public ContentSelectionChangedEvent(XBLOMContentElement var1) {
      super(var1);
   }

   public XBLOMContentElement getContentElement() {
      return (XBLOMContentElement)this.source;
   }
}
