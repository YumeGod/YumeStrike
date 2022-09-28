package org.apache.fop.fo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fo.flow.Marker;
import org.apache.fop.fo.properties.PropertyMaker;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class FObj extends FONode implements Constants {
   private static final PropertyMaker[] propertyListTable = FOPropertyMapping.getGenericMappings();
   protected FONode firstChild;
   private List extensionAttachments = null;
   private Map foreignAttributes = null;
   private boolean isOutOfLineFODescendant = false;
   private Map markers = null;
   private String id = null;

   public FObj(FONode parent) {
      super(parent);
      if (parent != null && parent instanceof FObj) {
         if (((FObj)parent).getIsOutOfLineFODescendant()) {
            this.isOutOfLineFODescendant = true;
         } else {
            int foID = this.getNameId();
            if (foID == 15 || foID == 24 || foID == 25) {
               this.isOutOfLineFODescendant = true;
            }
         }
      }

   }

   public FONode clone(FONode parent, boolean removeChildren) throws FOPException {
      FObj fobj = (FObj)super.clone(parent, removeChildren);
      if (removeChildren) {
         fobj.firstChild = null;
      }

      return fobj;
   }

   public static PropertyMaker getPropertyMakerFor(int propId) {
      return propertyListTable[propId];
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      this.setLocator(locator);
      pList.addAttributesToList(attlist);
      if (!this.inMarker() || "marker".equals(elementName)) {
         pList.setWritingMode();
         this.bind(pList);
      }

   }

   protected PropertyList createPropertyList(PropertyList parent, FOEventHandler foEventHandler) throws FOPException {
      return this.getBuilderContext().getPropertyListMaker().make(this, parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      this.id = pList.get(122).getString();
   }

   protected void startOfNode() throws FOPException {
      if (this.id != null) {
         this.checkId(this.id);
      }

   }

   private void checkId(String id) throws ValidationException {
      if (!this.inMarker() && !id.equals("")) {
         Set idrefs = this.getBuilderContext().getIDReferences();
         if (!idrefs.contains(id)) {
            idrefs.add(id);
         } else {
            this.getFOValidationEventProducer().idNotUnique(this, this.getName(), id, true, this.locator);
         }
      }

   }

   boolean getIsOutOfLineFODescendant() {
      return this.isOutOfLineFODescendant;
   }

   protected void addChildNode(FONode child) throws FOPException {
      if (child.getNameId() == 44) {
         this.addMarker((Marker)child);
      } else {
         ExtensionAttachment attachment = child.getExtensionAttachment();
         if (attachment != null) {
            this.addExtensionAttachment(attachment);
         } else if (this.firstChild == null) {
            this.firstChild = child;
         } else {
            FONode prevChild;
            for(prevChild = this.firstChild; prevChild.siblings != null && prevChild.siblings[1] != null; prevChild = prevChild.siblings[1]) {
            }

            FONode.attachSiblings(prevChild, child);
         }
      }

   }

   protected static void addChildTo(FONode child, FONode parent) throws FOPException {
      parent.addChildNode(child);
   }

   public void removeChild(FONode child) {
      FONode nextChild = null;
      if (child.siblings != null) {
         nextChild = child.siblings[1];
      }

      if (child == this.firstChild) {
         this.firstChild = nextChild;
         if (this.firstChild != null) {
            this.firstChild.siblings[0] = null;
         }
      } else {
         FONode prevChild = child.siblings[0];
         prevChild.siblings[1] = nextChild;
         if (nextChild != null) {
            nextChild.siblings[0] = prevChild;
         }
      }

   }

   public FObj findNearestAncestorFObj() {
      FONode par;
      for(par = this.parent; par != null && !(par instanceof FObj); par = par.parent) {
      }

      return (FObj)par;
   }

   public boolean generatesReferenceAreas() {
      return false;
   }

   public FONode.FONodeIterator getChildNodes() {
      return this.hasChildren() ? new FObjIterator(this) : null;
   }

   public boolean hasChildren() {
      return this.firstChild != null;
   }

   public FONode.FONodeIterator getChildNodes(FONode childNode) {
      FONode.FONodeIterator it = this.getChildNodes();
      if (it == null) {
         return null;
      } else if (this.firstChild == childNode) {
         return it;
      } else {
         while(it.hasNext() && it.nextNode().siblings[1] != childNode) {
         }

         return it.hasNext() ? it : null;
      }
   }

   void notifyChildRemoval(FONode node) {
   }

   protected void addMarker(Marker marker) {
      String mcname = marker.getMarkerClassName();
      if (this.firstChild != null) {
         Iterator iter = this.getChildNodes();

         while(iter.hasNext()) {
            FONode node = (FONode)iter.next();
            if (node instanceof FObj || node instanceof FOText && ((FOText)node).willCreateArea()) {
               this.getFOValidationEventProducer().markerNotInitialChild(this, this.getName(), mcname, this.locator);
               return;
            }

            if (node instanceof FOText) {
               iter.remove();
               this.notifyChildRemoval(node);
            }
         }
      }

      if (this.markers == null) {
         this.markers = new HashMap();
      }

      if (!this.markers.containsKey(mcname)) {
         this.markers.put(mcname, marker);
      } else {
         this.getFOValidationEventProducer().markerNotUniqueForSameParent(this, this.getName(), mcname, this.locator);
      }

   }

   public boolean hasMarkers() {
      return this.markers != null && !this.markers.isEmpty();
   }

   public Map getMarkers() {
      return this.markers;
   }

   protected String getContextInfoAlt() {
      StringBuffer sb = new StringBuffer();
      if (this.getLocalName() != null) {
         sb.append(this.getName());
         sb.append(", ");
      }

      if (this.hasId()) {
         sb.append("id=").append(this.getId());
         return sb.toString();
      } else {
         String s = this.gatherContextInfo();
         if (s != null) {
            sb.append("\"");
            if (s.length() < 32) {
               sb.append(s);
            } else {
               sb.append(s.substring(0, 32));
               sb.append("...");
            }

            sb.append("\"");
            return sb.toString();
         } else {
            return null;
         }
      }
   }

   protected String gatherContextInfo() {
      if (this.getLocator() != null) {
         return super.gatherContextInfo();
      } else {
         ListIterator iter = this.getChildNodes();
         if (iter == null) {
            return null;
         } else {
            StringBuffer sb = new StringBuffer();

            while(iter.hasNext()) {
               FONode node = (FONode)iter.next();
               String s = node.gatherContextInfo();
               if (s != null) {
                  if (sb.length() > 0) {
                     sb.append(", ");
                  }

                  sb.append(s);
               }
            }

            return sb.length() > 0 ? sb.toString() : null;
         }
      }
   }

   protected boolean isBlockItem(String nsURI, String lName) {
      return "http://www.w3.org/1999/XSL/Format".equals(nsURI) && ("block".equals(lName) || "table".equals(lName) || "table-and-caption".equals(lName) || "block-container".equals(lName) || "list-block".equals(lName) || "float".equals(lName) || this.isNeutralItem(nsURI, lName));
   }

   protected boolean isInlineItem(String nsURI, String lName) {
      return "http://www.w3.org/1999/XSL/Format".equals(nsURI) && ("bidi-override".equals(lName) || "character".equals(lName) || "external-graphic".equals(lName) || "instream-foreign-object".equals(lName) || "inline".equals(lName) || "inline-container".equals(lName) || "leader".equals(lName) || "page-number".equals(lName) || "page-number-citation".equals(lName) || "page-number-citation-last".equals(lName) || "basic-link".equals(lName) || "multi-toggle".equals(lName) && (this.getNameId() == 45 || this.findAncestor(45) > 0) || "footnote".equals(lName) && !this.isOutOfLineFODescendant || this.isNeutralItem(nsURI, lName));
   }

   protected boolean isBlockOrInlineItem(String nsURI, String lName) {
      return this.isBlockItem(nsURI, lName) || this.isInlineItem(nsURI, lName);
   }

   boolean isNeutralItem(String nsURI, String lName) {
      return "http://www.w3.org/1999/XSL/Format".equals(nsURI) && ("multi-switch".equals(lName) || "multi-properties".equals(lName) || "wrapper".equals(lName) || !this.isOutOfLineFODescendant && "float".equals(lName) || "retrieve-marker".equals(lName) || "retrieve-table-marker".equals(lName));
   }

   protected int findAncestor(int ancestorID) {
      int found = 1;

      for(FONode temp = this.getParent(); temp != null; temp = temp.getParent()) {
         if (temp.getNameId() == ancestorID) {
            return found;
         }

         ++found;
      }

      return -1;
   }

   public void clearChildNodes() {
      this.firstChild = null;
   }

   public String getId() {
      return this.id;
   }

   public boolean hasId() {
      return this.id != null && this.id.length() > 0;
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/1999/XSL/Format";
   }

   public String getNormalNamespacePrefix() {
      return "fo";
   }

   void addExtensionAttachment(ExtensionAttachment attachment) {
      if (attachment == null) {
         throw new NullPointerException("Parameter attachment must not be null");
      } else {
         if (this.extensionAttachments == null) {
            this.extensionAttachments = new ArrayList();
         }

         if (log.isDebugEnabled()) {
            log.debug("ExtensionAttachment of category " + attachment.getCategory() + " added to " + this.getName() + ": " + attachment);
         }

         this.extensionAttachments.add(attachment);
      }
   }

   public List getExtensionAttachments() {
      return this.extensionAttachments == null ? Collections.EMPTY_LIST : this.extensionAttachments;
   }

   public boolean hasExtensionAttachments() {
      return this.extensionAttachments != null;
   }

   public void addForeignAttribute(QName attributeName, String value) {
      if (attributeName == null) {
         throw new NullPointerException("Parameter attributeName must not be null");
      } else {
         if (this.foreignAttributes == null) {
            this.foreignAttributes = new HashMap();
         }

         this.foreignAttributes.put(attributeName, value);
      }
   }

   public Map getForeignAttributes() {
      return this.foreignAttributes == null ? Collections.EMPTY_MAP : this.foreignAttributes;
   }

   public String toString() {
      return super.toString() + "[@id=" + this.id + "]";
   }

   public class FObjIterator implements FONode.FONodeIterator {
      private static final int F_NONE_ALLOWED = 0;
      private static final int F_SET_ALLOWED = 1;
      private static final int F_REMOVE_ALLOWED = 2;
      private FONode currentNode;
      private final FObj parentNode;
      private int currentIndex;
      private int flags = 0;

      FObjIterator(FObj parent) {
         this.parentNode = parent;
         this.currentNode = parent.firstChild;
         this.currentIndex = 0;
         this.flags = 0;
      }

      public FObj parentNode() {
         return this.parentNode;
      }

      public Object next() {
         if (this.currentNode == null) {
            throw new NoSuchElementException();
         } else {
            if (this.currentIndex != 0) {
               if (this.currentNode.siblings == null || this.currentNode.siblings[1] == null) {
                  throw new NoSuchElementException();
               }

               this.currentNode = this.currentNode.siblings[1];
            }

            ++this.currentIndex;
            this.flags |= 3;
            return this.currentNode;
         }
      }

      public Object previous() {
         if (this.currentNode.siblings != null && this.currentNode.siblings[0] != null) {
            --this.currentIndex;
            this.currentNode = this.currentNode.siblings[0];
            this.flags |= 3;
            return this.currentNode;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void set(Object o) {
         if ((this.flags & 1) == 1) {
            FONode newNode = (FONode)o;
            if (this.currentNode == this.parentNode.firstChild) {
               this.parentNode.firstChild = newNode;
            } else {
               FONode.attachSiblings(this.currentNode.siblings[0], newNode);
            }

            if (this.currentNode.siblings != null && this.currentNode.siblings[1] != null) {
               FONode.attachSiblings(newNode, this.currentNode.siblings[1]);
            }

         } else {
            throw new IllegalStateException();
         }
      }

      public void add(Object o) {
         FONode newNode = (FONode)o;
         if (this.currentIndex == -1) {
            if (this.currentNode != null) {
               FONode.attachSiblings(newNode, this.currentNode);
            }

            this.parentNode.firstChild = newNode;
            this.currentIndex = 0;
            this.currentNode = newNode;
         } else {
            if (this.currentNode.siblings != null && this.currentNode.siblings[1] != null) {
               FONode.attachSiblings((FONode)o, this.currentNode.siblings[1]);
            }

            FONode.attachSiblings(this.currentNode, (FONode)o);
         }

         this.flags &= 0;
      }

      public boolean hasNext() {
         return this.currentNode != null && (this.currentIndex == 0 || this.currentNode.siblings != null && this.currentNode.siblings[1] != null);
      }

      public boolean hasPrevious() {
         return this.currentIndex != 0 || this.currentNode.siblings != null && this.currentNode.siblings[0] != null;
      }

      public int nextIndex() {
         return this.currentIndex + 1;
      }

      public int previousIndex() {
         return this.currentIndex - 1;
      }

      public void remove() {
         if ((this.flags & 2) != 2) {
            throw new IllegalStateException();
         } else {
            this.parentNode.removeChild(this.currentNode);
            if (this.currentIndex == 0) {
               this.currentNode = this.parentNode.firstChild;
            } else if (this.currentNode.siblings != null && this.currentNode.siblings[0] != null) {
               this.currentNode = this.currentNode.siblings[0];
               --this.currentIndex;
            } else {
               this.currentNode = null;
            }

            this.flags &= 0;
         }
      }

      public FONode lastNode() {
         while(this.currentNode != null && this.currentNode.siblings != null && this.currentNode.siblings[1] != null) {
            this.currentNode = this.currentNode.siblings[1];
            ++this.currentIndex;
         }

         return this.currentNode;
      }

      public FONode firstNode() {
         this.currentNode = this.parentNode.firstChild;
         this.currentIndex = 0;
         return this.currentNode;
      }

      public FONode nextNode() {
         return (FONode)this.next();
      }

      public FONode previousNode() {
         return (FONode)this.previous();
      }
   }
}
