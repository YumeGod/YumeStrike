package org.apache.fop.area;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.xmlgraphics.util.QName;

public abstract class AreaTreeObject {
   protected Map foreignAttributes = null;
   protected List extensionAttachments = null;

   public void setForeignAttribute(QName name, String value) {
      if (this.foreignAttributes == null) {
         this.foreignAttributes = new HashMap();
      }

      this.foreignAttributes.put(name, value);
   }

   public void setForeignAttributes(Map atts) {
      if (atts.size() != 0) {
         Iterator iter = atts.entrySet().iterator();

         while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            String value = (String)entry.getValue();
            this.setForeignAttribute((QName)entry.getKey(), value);
         }

      }
   }

   public String getForeignAttributeValue(QName name) {
      return this.foreignAttributes != null ? (String)this.foreignAttributes.get(name) : null;
   }

   public Map getForeignAttributes() {
      return this.foreignAttributes != null ? Collections.unmodifiableMap(this.foreignAttributes) : Collections.EMPTY_MAP;
   }

   private void prepareExtensionAttachmentContainer() {
      if (this.extensionAttachments == null) {
         this.extensionAttachments = new ArrayList();
      }

   }

   public void addExtensionAttachment(ExtensionAttachment attachment) {
      this.prepareExtensionAttachmentContainer();
      this.extensionAttachments.add(attachment);
   }

   public void setExtensionAttachments(List extensionAttachments) {
      this.prepareExtensionAttachmentContainer();
      this.extensionAttachments.addAll(extensionAttachments);
   }

   public List getExtensionAttachments() {
      return this.extensionAttachments != null ? Collections.unmodifiableList(this.extensionAttachments) : Collections.EMPTY_LIST;
   }

   public boolean hasExtensionAttachments() {
      return this.extensionAttachments != null && !this.extensionAttachments.isEmpty();
   }
}
