package org.apache.fop.fo.pagination;

import java.util.HashMap;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class Declarations extends FObj {
   private Map colorProfiles = null;

   public Declarations(FONode parent) {
      super(parent);
      ((Root)parent).setDeclarations(this);
   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !localName.equals("color-profile")) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild != null) {
         FONode.FONodeIterator iter = this.getChildNodes();

         while(iter.hasNext()) {
            FONode node = iter.nextNode();
            if (node.getName().equals("fo:color-profile")) {
               ColorProfile cp = (ColorProfile)node;
               if (!"".equals(cp.getColorProfileName())) {
                  this.addColorProfile(cp);
               } else {
                  this.getFOValidationEventProducer().missingProperty(this, cp.getName(), "color-profile-name", this.locator);
               }
            } else {
               log.debug("Ignoring element " + node.getName() + " inside fo:declarations.");
            }
         }
      }

      this.firstChild = null;
   }

   private void addColorProfile(ColorProfile cp) {
      if (this.colorProfiles == null) {
         this.colorProfiles = new HashMap();
      }

      if (this.colorProfiles.get(cp.getColorProfileName()) != null) {
         this.getFOValidationEventProducer().colorProfileNameNotUnique(this, cp.getName(), cp.getColorProfileName(), this.locator);
      }

      this.colorProfiles.put(cp.getColorProfileName(), cp);
   }

   public String getLocalName() {
      return "declarations";
   }

   public int getNameId() {
      return 13;
   }

   public ColorProfile getColorProfile(String cpName) {
      ColorProfile profile = null;
      if (this.colorProfiles != null) {
         profile = (ColorProfile)this.colorProfiles.get(cpName);
      }

      return profile;
   }
}
