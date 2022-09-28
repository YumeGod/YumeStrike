package org.apache.fop.fo.flow;

import java.util.HashMap;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOTreeBuilderContext;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.FObjMixed;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.PropertyListMaker;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.Property;
import org.apache.fop.fo.properties.PropertyCache;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public class Marker extends FObjMixed {
   private String markerClassName;
   private PropertyListMaker savePropertyListMaker;
   private Map descendantPropertyLists = new HashMap();

   public Marker(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      if (this.findAncestor(16) < 0) {
         this.invalidChildError(this.locator, this.getParent().getName(), "http://www.w3.org/1999/XSL/Format", this.getName(), "rule.markerDescendantOfFlow");
      }

      this.markerClassName = pList.get(152).getString();
      if (this.markerClassName == null || this.markerClassName.equals("")) {
         this.missingPropertyError("marker-class-name");
      }

   }

   protected MarkerPropertyList getPropertyListFor(FONode foNode) {
      return (MarkerPropertyList)this.descendantPropertyLists.get(foNode);
   }

   protected void startOfNode() {
      FOTreeBuilderContext builderContext = this.getBuilderContext();
      this.savePropertyListMaker = builderContext.getPropertyListMaker();
      builderContext.setPropertyListMaker(new PropertyListMaker() {
         public PropertyList make(FObj fobj, PropertyList parentPropertyList) {
            PropertyList pList = Marker.this.new MarkerPropertyList(fobj, parentPropertyList);
            Marker.this.descendantPropertyLists.put(fobj, pList);
            return pList;
         }
      });
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getBuilderContext().setPropertyListMaker(this.savePropertyListMaker);
      this.savePropertyListMaker = null;
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isBlockOrInlineItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   protected boolean inMarker() {
      return true;
   }

   public String getMarkerClassName() {
      return this.markerClassName;
   }

   public String getLocalName() {
      return "marker";
   }

   public int getNameId() {
      return 44;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(super.toString());
      sb.append(" {").append(this.getMarkerClassName()).append("}");
      return sb.toString();
   }

   public static final class MarkerAttribute {
      private static PropertyCache attributeCache = new PropertyCache(MarkerAttribute.class);
      protected String namespace;
      protected String qname;
      protected String name;
      protected String value;

      private MarkerAttribute(String namespace, String qname, String name, String value) {
         this.namespace = namespace;
         this.qname = qname;
         this.name = name == null ? qname : name;
         this.value = value;
      }

      private static MarkerAttribute getInstance(String namespace, String qname, String name, String value) {
         return attributeCache.fetch(new MarkerAttribute(namespace, qname, name, value));
      }

      public int hashCode() {
         int hash = 17;
         hash = 37 * hash + (this.namespace == null ? 0 : this.namespace.hashCode());
         hash = 37 * hash + (this.qname == null ? 0 : this.qname.hashCode());
         hash = 37 * hash + (this.name == null ? 0 : this.name.hashCode());
         hash = 37 * hash + (this.value == null ? 0 : this.value.hashCode());
         return hash;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof MarkerAttribute)) {
            return false;
         } else {
            MarkerAttribute attr = (MarkerAttribute)o;
            return (attr.namespace == this.namespace || attr.namespace != null && attr.namespace.equals(this.namespace)) && (attr.qname == this.qname || attr.qname != null && attr.qname.equals(this.qname)) && (attr.name == this.name || attr.name != null && attr.name.equals(this.name)) && (attr.value == this.value || attr.value != null && attr.value.equals(this.value));
         }
      }
   }

   protected class MarkerPropertyList extends PropertyList implements Attributes {
      private MarkerAttribute[] attribs;

      public MarkerPropertyList(FObj fobj, PropertyList parentPropertyList) {
         super(fobj, (PropertyList)null);
      }

      public void addAttributesToList(Attributes attributes) throws ValidationException {
         this.attribs = new MarkerAttribute[attributes.getLength()];
         int i = attributes.getLength();

         while(true) {
            --i;
            if (i < 0) {
               return;
            }

            String namespace = attributes.getURI(i);
            String qname = attributes.getQName(i);
            String name = attributes.getLocalName(i);
            String value = attributes.getValue(i);
            this.attribs[i] = Marker.MarkerAttribute.getInstance(namespace, qname, name, value);
         }
      }

      public void putExplicit(int propId, Property value) {
      }

      public Property getExplicit(int propId) {
         return null;
      }

      public int getLength() {
         return this.attribs == null ? 0 : this.attribs.length;
      }

      public String getURI(int index) {
         return this.attribs != null && index < this.attribs.length && index >= 0 && this.attribs[index] != null ? this.attribs[index].namespace : null;
      }

      public String getLocalName(int index) {
         return this.attribs != null && index < this.attribs.length && index >= 0 && this.attribs[index] != null ? this.attribs[index].name : null;
      }

      public String getQName(int index) {
         return this.attribs != null && index < this.attribs.length && index >= 0 && this.attribs[index] != null ? this.attribs[index].qname : null;
      }

      public String getType(int index) {
         return "CDATA";
      }

      public String getValue(int index) {
         return this.attribs != null && index < this.attribs.length && index >= 0 && this.attribs[index] != null ? this.attribs[index].value : null;
      }

      public int getIndex(String name, String namespace) {
         int index = -1;
         if (this.attribs != null && name != null && namespace != null) {
            int i = this.attribs.length;

            do {
               --i;
            } while(i >= 0 && (this.attribs[i] == null || !namespace.equals(this.attribs[i].namespace) || !name.equals(this.attribs[i].name)));
         }

         return index;
      }

      public int getIndex(String qname) {
         int index = -1;
         if (this.attribs != null && qname != null) {
            int i = this.attribs.length;

            do {
               --i;
            } while(i >= 0 && (this.attribs[i] == null || !qname.equals(this.attribs[i].qname)));
         }

         return index;
      }

      public String getType(String name, String namespace) {
         return "CDATA";
      }

      public String getType(String qname) {
         return "CDATA";
      }

      public String getValue(String name, String namespace) {
         int index = this.getIndex(name, namespace);
         return index > 0 ? this.getValue(index) : null;
      }

      public String getValue(String qname) {
         int index = this.getIndex(qname);
         return index > 0 ? this.getValue(index) : null;
      }
   }
}
