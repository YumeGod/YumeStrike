package org.apache.fop.fo;

import java.awt.geom.Point2D;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.util.ContentHandlerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public abstract class XMLObj extends FONode implements ContentHandlerFactory.ObjectBuiltListener {
   private Attributes attr = null;
   protected Element element;
   protected Document doc;
   protected String name;
   private static HashMap ns = new HashMap();

   public XMLObj(FONode parent) {
      super(parent);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList propertyList) throws FOPException {
      this.setLocator(locator);
      this.name = elementName;
      this.attr = attlist;
   }

   public Document getDOMDocument() {
      return this.doc;
   }

   public Point2D getDimension(Point2D view) {
      return null;
   }

   public Length getIntrinsicAlignmentAdjust() {
      return null;
   }

   public String getLocalName() {
      return this.name;
   }

   public void addElement(Document doc, Element parent) {
      this.doc = doc;
      this.element = doc.createElementNS(this.getNamespaceURI(), this.name);
      setAttributes(this.element, this.attr);
      this.attr = null;
      parent.appendChild(this.element);
   }

   private static void setAttributes(Element element, Attributes attr) {
      for(int count = 0; count < attr.getLength(); ++count) {
         String rf = attr.getValue(count);
         String qname = attr.getQName(count);
         int idx = qname.indexOf(":");
         if (idx == -1) {
            element.setAttribute(qname, rf);
         } else {
            String pref = qname.substring(0, idx);
            String tail = qname.substring(idx + 1);
            if (pref.equals("xmlns")) {
               ns.put(tail, rf);
            } else {
               element.setAttributeNS((String)ns.get(pref), tail, rf);
            }
         }
      }

   }

   public void buildTopLevel(Document doc, Element svgRoot) {
      setAttributes(this.element, this.attr);
   }

   public Document createBasicDocument() {
      this.doc = null;
      this.element = null;

      try {
         DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
         fact.setNamespaceAware(true);
         this.doc = fact.newDocumentBuilder().newDocument();
         Element el = this.doc.createElementNS(this.getNamespaceURI(), this.name);
         this.doc.appendChild(el);
         this.element = this.doc.getDocumentElement();
         this.buildTopLevel(this.doc, this.element);
         if (!this.element.hasAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns")) {
            this.element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", this.getNamespaceURI());
         }
      } catch (Exception var3) {
         log.error("Error while trying to instantiate a DOM Document", var3);
      }

      return this.doc;
   }

   protected void addChildNode(FONode child) {
      if (child instanceof XMLObj) {
         ((XMLObj)child).addElement(this.doc, this.element);
      } else {
         log.debug("Invalid element: " + child.getName() + " inside foreign xml markup");
      }

   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) throws FOPException {
      super.characters(data, start, length, pList, locator);
      String str = new String(data, start, length);
      Text text = this.doc.createTextNode(str);
      this.element.appendChild(text);
   }

   public void notifyObjectBuilt(Object obj) {
      this.doc = (Document)obj;
      this.element = this.doc.getDocumentElement();
   }

   static {
      ns.put("xlink", "http://www.w3.org/1999/xlink");
   }
}
