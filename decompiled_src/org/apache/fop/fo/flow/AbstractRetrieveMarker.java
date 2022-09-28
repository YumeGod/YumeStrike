package org.apache.fop.fo.flow;

import java.util.Iterator;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.XMLObj;
import org.apache.fop.fo.flow.table.Table;
import org.xml.sax.Locator;

public abstract class AbstractRetrieveMarker extends FObjMixed {
   private PropertyList propertyList;
   private String retrieveClassName;

   public AbstractRetrieveMarker(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.retrieveClassName = pList.get(207).getString();
      if (this.retrieveClassName == null || this.retrieveClassName.equals("")) {
         this.missingPropertyError("retrieve-class-name");
      }

      this.propertyList = pList.getParentPropertyList();
   }

   private PropertyList createPropertyListFor(FObj fo, PropertyList parent) {
      return this.getBuilderContext().getPropertyListMaker().make(fo, parent);
   }

   private void cloneSingleNode(FONode child, FONode newParent, Marker marker, PropertyList parentPropertyList) throws FOPException {
      if (child != null) {
         FONode newChild = child.clone(newParent, true);
         if (child instanceof FObj) {
            PropertyList newPropertyList = this.createPropertyListFor((FObj)newChild, parentPropertyList);
            Marker.MarkerPropertyList pList = marker.getPropertyListFor(child);
            newChild.processNode(child.getLocalName(), this.getLocator(), pList, newPropertyList);
            addChildTo(newChild, newParent);
            if (newChild.getNameId() == 71) {
               Table t = (Table)child;
               this.cloneSubtree(t.getColumns().iterator(), newChild, marker, newPropertyList);
               this.cloneSingleNode(t.getTableHeader(), newChild, marker, newPropertyList);
               this.cloneSingleNode(t.getTableFooter(), newChild, marker, newPropertyList);
            }

            this.cloneSubtree(child.getChildNodes(), newChild, marker, newPropertyList);
         } else if (child instanceof FOText) {
            FOText ft = (FOText)newChild;
            ft.bind(parentPropertyList);
            addChildTo(newChild, newParent);
         } else if (child instanceof XMLObj) {
            addChildTo(newChild, newParent);
         }

         newChild.finalizeNode();
      }

   }

   private void cloneSubtree(Iterator parentIter, FONode newParent, Marker marker, PropertyList parentPropertyList) throws FOPException {
      if (parentIter != null) {
         while(parentIter.hasNext()) {
            FONode child = (FONode)parentIter.next();
            this.cloneSingleNode(child, newParent, marker, parentPropertyList);
         }
      }

   }

   private void cloneFromMarker(Marker marker) throws FOPException {
      this.cloneSubtree(marker.getChildNodes(), this, marker, this.propertyList);
      handleWhiteSpaceFor(this, (FONode)null);
   }

   public void bindMarker(Marker marker) {
      if (this.firstChild != null) {
         this.currentTextNode = null;
         this.firstChild = null;
      }

      if (marker.getChildNodes() != null) {
         try {
            this.cloneFromMarker(marker);
         } catch (FOPException var3) {
            this.getFOValidationEventProducer().markerCloningFailed(this, marker.getMarkerClassName(), var3, this.getLocator());
         }
      } else if (log.isDebugEnabled()) {
         log.debug("Empty marker retrieved...");
      }

   }

   public String getRetrieveClassName() {
      return this.retrieveClassName;
   }
}
