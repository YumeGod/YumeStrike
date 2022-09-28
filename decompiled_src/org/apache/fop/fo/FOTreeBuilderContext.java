package org.apache.fop.fo;

import java.util.HashSet;
import java.util.Set;

public class FOTreeBuilderContext {
   private Set idReferences = new HashSet();
   protected PropertyListMaker propertyListMaker;
   protected XMLWhiteSpaceHandler whiteSpaceHandler = new XMLWhiteSpaceHandler();
   private boolean inMarker = false;

   public Set getIDReferences() {
      return this.idReferences;
   }

   public PropertyListMaker getPropertyListMaker() {
      return this.propertyListMaker;
   }

   public void setPropertyListMaker(PropertyListMaker propertyListMaker) {
      this.propertyListMaker = propertyListMaker;
   }

   public XMLWhiteSpaceHandler getXMLWhiteSpaceHandler() {
      return this.whiteSpaceHandler;
   }

   protected void switchMarkerContext(boolean inMarker) {
      this.inMarker = inMarker;
   }

   protected boolean inMarker() {
      return this.inMarker;
   }
}
