package org.apache.fop.fo.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class ExtensionObj extends FObj {
   public ExtensionObj(FONode parent) {
      super(parent);
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
   }

   protected PropertyList createPropertyList(PropertyList parent, FOEventHandler foEventHandler) throws FOPException {
      return null;
   }
}
