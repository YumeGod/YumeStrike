package org.apache.batik.bridge.svg12;

import java.util.EventListener;
import org.w3c.dom.Element;

public interface BindingListener extends EventListener {
   void bindingChanged(Element var1, Element var2);
}
