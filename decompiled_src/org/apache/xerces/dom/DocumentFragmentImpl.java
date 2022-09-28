package org.apache.xerces.dom;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Text;

public class DocumentFragmentImpl extends ParentNode implements DocumentFragment {
   static final long serialVersionUID = -7596449967279236746L;

   public DocumentFragmentImpl(CoreDocumentImpl var1) {
      super(var1);
   }

   public DocumentFragmentImpl() {
   }

   public short getNodeType() {
      return 11;
   }

   public String getNodeName() {
      return "#document-fragment";
   }

   public void normalize() {
      if (!this.isNormalized()) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         ChildNode var2;
         for(ChildNode var1 = super.firstChild; var1 != null; var1 = var2) {
            var2 = var1.nextSibling;
            if (var1.getNodeType() == 3) {
               if (var2 != null && var2.getNodeType() == 3) {
                  ((Text)var1).appendData(var2.getNodeValue());
                  this.removeChild(var2);
                  var2 = var1;
               } else if (var1.getNodeValue() == null || var1.getNodeValue().length() == 0) {
                  this.removeChild(var1);
               }
            }

            var1.normalize();
         }

         this.isNormalized(true);
      }
   }
}
