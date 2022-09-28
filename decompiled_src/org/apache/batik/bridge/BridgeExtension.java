package org.apache.batik.bridge;

import java.util.Iterator;
import org.w3c.dom.Element;

public interface BridgeExtension {
   float getPriority();

   Iterator getImplementedExtensions();

   String getAuthor();

   String getContactAddress();

   String getURL();

   String getDescription();

   void registerTags(BridgeContext var1);

   boolean isDynamicElement(Element var1);
}
