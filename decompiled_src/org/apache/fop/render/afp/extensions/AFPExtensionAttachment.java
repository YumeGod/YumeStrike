package org.apache.fop.render.afp.extensions;

import java.io.Serializable;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.xmlgraphics.util.XMLizable;

public abstract class AFPExtensionAttachment implements ExtensionAttachment, Serializable, XMLizable {
   private static final long serialVersionUID = 7190606822558332901L;
   public static final String CATEGORY = "apache:fop:extensions:afp";
   protected static final String ATT_NAME = "name";
   protected String elementName;
   protected String name;

   public AFPExtensionAttachment(String elementName) {
      this.elementName = elementName;
   }

   public String getElementName() {
      return this.elementName;
   }

   protected boolean hasName() {
      return this.name != null;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCategory() {
      return "apache:fop:extensions:afp";
   }
}
