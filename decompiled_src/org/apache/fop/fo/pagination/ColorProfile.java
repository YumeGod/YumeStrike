package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class ColorProfile extends FObj {
   private String src;
   private String colorProfileName;
   private int renderingIntent;

   public ColorProfile(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.src = pList.get(232).getString();
      this.colorProfileName = pList.get(73).getString();
      this.renderingIntent = pList.get(204).getEnum();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getColorProfileName() {
      return this.colorProfileName;
   }

   public String getLocalName() {
      return "color-profile";
   }

   public int getNameId() {
      return 11;
   }

   public String getSrc() {
      return this.src;
   }

   public int getRenderingIntent() {
      return this.renderingIntent;
   }
}
