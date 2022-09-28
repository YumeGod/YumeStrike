package org.apache.fop.fo;

import java.util.ListIterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.fo.pagination.Root;
import org.apache.fop.util.ContentHandlerFactory;
import org.apache.fop.util.text.AdvancedMessageFormat;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

public abstract class FONode implements Cloneable {
   protected static final String FO_URI = "http://www.w3.org/1999/XSL/Format";
   protected static final String FOX_URI = "http://xmlgraphics.apache.org/fop/extensions";
   protected FONode parent;
   protected FONode[] siblings;
   protected Locator locator;
   protected static Log log;

   protected FONode(FONode parent) {
      this.parent = parent;
   }

   public FONode clone(FONode cloneparent, boolean removeChildren) throws FOPException {
      FONode foNode = (FONode)this.clone();
      foNode.parent = cloneparent;
      foNode.siblings = null;
      return foNode;
   }

   protected Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new AssertionError();
      }
   }

   public void bind(PropertyList propertyList) throws FOPException {
   }

   public void setLocator(Locator locator) {
      if (locator != null) {
         this.locator = new LocatorImpl(locator);
      }

   }

   public Locator getLocator() {
      return this.locator;
   }

   public FOEventHandler getFOEventHandler() {
      return this.parent.getFOEventHandler();
   }

   public FOTreeBuilderContext getBuilderContext() {
      return this.parent.getBuilderContext();
   }

   protected boolean inMarker() {
      return this.getBuilderContext().inMarker();
   }

   public FOUserAgent getUserAgent() {
      return this.getFOEventHandler().getUserAgent();
   }

   public Log getLogger() {
      return log;
   }

   public void processNode(String elementName, Locator locator, Attributes attlist, PropertyList pList) throws FOPException {
      if (log.isDebugEnabled()) {
         log.debug("Unhandled element: " + elementName + (locator != null ? " at " + getLocatorString(locator) : ""));
      }

   }

   protected PropertyList createPropertyList(PropertyList pList, FOEventHandler foEventHandler) throws FOPException {
      return null;
   }

   protected void validateChildNode(Locator loc, String namespaceURI, String localName) throws ValidationException {
   }

   protected static void validateChildNode(FONode fo, Locator loc, String namespaceURI, String localName) throws ValidationException {
      fo.validateChildNode(loc, namespaceURI, localName);
   }

   /** @deprecated */
   protected void addCharacters(char[] data, int start, int end, PropertyList pList, Locator locator) throws FOPException {
   }

   protected void characters(char[] data, int start, int length, PropertyList pList, Locator locator) throws FOPException {
      this.addCharacters(data, start, start + length, pList, locator);
   }

   protected void startOfNode() throws FOPException {
   }

   protected void endOfNode() throws FOPException {
      this.finalizeNode();
   }

   protected void addChildNode(FONode child) throws FOPException {
   }

   public void removeChild(FONode child) {
   }

   public void finalizeNode() throws FOPException {
   }

   public FONode getParent() {
      return this.parent;
   }

   public FONodeIterator getChildNodes() {
      return null;
   }

   public FONodeIterator getChildNodes(FONode childNode) {
      return null;
   }

   public CharIterator charIterator() {
      return new OneCharIterator('\u0000');
   }

   public static String getNodeString(String namespaceURI, String localName) {
      if (namespaceURI.equals("http://www.w3.org/1999/XSL/Format")) {
         return "fo:" + localName;
      } else if (namespaceURI.equals("http://xmlgraphics.apache.org/fop/extensions")) {
         return "fox:" + localName;
      } else if (namespaceURI.equals("http://xmlgraphics.apache.org/fop/internal")) {
         return "foi:" + localName;
      } else {
         return namespaceURI.equals("http://www.w3.org/2000/svg") ? "svg:" + localName : "(Namespace URI: \"" + namespaceURI + "\", " + "Local Name: \"" + localName + "\")";
      }
   }

   protected FOValidationEventProducer getFOValidationEventProducer() {
      return FOValidationEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
   }

   protected void tooManyNodesError(Locator loc, String nsURI, String lName) throws ValidationException {
      this.tooManyNodesError(loc, new QName(nsURI, lName));
   }

   protected void tooManyNodesError(Locator loc, QName offendingNode) throws ValidationException {
      this.getFOValidationEventProducer().tooManyNodes(this, this.getName(), offendingNode, loc);
   }

   protected void tooManyNodesError(Locator loc, String offendingNode) throws ValidationException {
      this.tooManyNodesError(loc, new QName("http://www.w3.org/1999/XSL/Format", offendingNode));
   }

   protected void nodesOutOfOrderError(Locator loc, String tooLateNode, String tooEarlyNode) throws ValidationException {
      this.nodesOutOfOrderError(loc, tooLateNode, tooEarlyNode, false);
   }

   protected void nodesOutOfOrderError(Locator loc, String tooLateNode, String tooEarlyNode, boolean canRecover) throws ValidationException {
      this.getFOValidationEventProducer().nodeOutOfOrder(this, this.getName(), tooLateNode, tooEarlyNode, canRecover, loc);
   }

   protected void invalidChildError(Locator loc, String nsURI, String lName) throws ValidationException {
      this.invalidChildError(loc, this.getName(), nsURI, lName, (String)null);
   }

   protected void invalidChildError(Locator loc, String parentName, String nsURI, String lName, String ruleViolated) throws ValidationException {
      this.getFOValidationEventProducer().invalidChild(this, parentName, new QName(nsURI, lName), ruleViolated, loc);
   }

   protected void missingChildElementError(String contentModel) throws ValidationException {
      this.getFOValidationEventProducer().missingChildElement(this, this.getName(), contentModel, false, this.locator);
   }

   protected void missingChildElementError(String contentModel, boolean canRecover) throws ValidationException {
      this.getFOValidationEventProducer().missingChildElement(this, this.getName(), contentModel, canRecover, this.locator);
   }

   protected void missingPropertyError(String propertyName) throws ValidationException {
      this.getFOValidationEventProducer().missingProperty(this, this.getName(), propertyName, this.locator);
   }

   protected static String errorText(Locator loc) {
      return "Error(" + getLocatorString(loc) + "): ";
   }

   protected static String warningText(Locator loc) {
      return "Warning(" + getLocatorString(loc) + "): ";
   }

   public static String getLocatorString(Locator loc) {
      return loc == null ? "Unknown location" : loc.getLineNumber() + "/" + loc.getColumnNumber();
   }

   public static String decorateWithContextInfo(String text, FONode node) {
      if (node != null) {
         StringBuffer sb = new StringBuffer(text);
         sb.append(" (").append(node.getContextInfo()).append(")");
         return sb.toString();
      } else {
         return text;
      }
   }

   /** @deprecated */
   public String getContextInfo() {
      StringBuffer sb = new StringBuffer();
      if (this.getLocalName() != null) {
         sb.append(this.getName());
         sb.append(", ");
      }

      if (this.locator != null) {
         sb.append("location: ");
         sb.append(getLocatorString(this.locator));
      } else {
         String s = this.gatherContextInfo();
         if (s != null) {
            sb.append("\"");
            sb.append(s);
            sb.append("\"");
         } else {
            sb.append("no context info available");
         }
      }

      if (sb.length() > 80) {
         sb.setLength(80);
      }

      return sb.toString();
   }

   protected String getContextInfoAlt() {
      String s = this.gatherContextInfo();
      if (s != null) {
         StringBuffer sb = new StringBuffer();
         if (this.getLocalName() != null) {
            sb.append(this.getName());
            sb.append(", ");
         }

         sb.append("\"");
         sb.append(s);
         sb.append("\"");
         return sb.toString();
      } else {
         return null;
      }
   }

   protected String gatherContextInfo() {
      return null;
   }

   public Root getRoot() {
      return this.parent.getRoot();
   }

   public String getName() {
      return this.getName(this.getNormalNamespacePrefix());
   }

   public String getName(String prefix) {
      if (prefix != null) {
         StringBuffer sb = new StringBuffer();
         sb.append(prefix).append(':').append(this.getLocalName());
         return sb.toString();
      } else {
         return this.getLocalName();
      }
   }

   public abstract String getLocalName();

   public abstract String getNormalNamespacePrefix();

   public String getNamespaceURI() {
      return null;
   }

   public int getNameId() {
      return 0;
   }

   public ExtensionAttachment getExtensionAttachment() {
      return null;
   }

   public ContentHandlerFactory getContentHandlerFactory() {
      return null;
   }

   protected boolean canHaveMarkers() {
      int foId = this.getNameId();
      switch (foId) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 16:
         case 35:
         case 36:
         case 40:
         case 41:
         case 42:
         case 43:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 77:
         case 78:
         case 81:
            return true;
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 37:
         case 38:
         case 39:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 76:
         case 79:
         case 80:
         default:
            return false;
      }
   }

   protected static void attachSiblings(FONode precedingSibling, FONode followingSibling) {
      if (precedingSibling.siblings == null) {
         precedingSibling.siblings = new FONode[2];
      }

      if (followingSibling.siblings == null) {
         followingSibling.siblings = new FONode[2];
      }

      precedingSibling.siblings[1] = followingSibling;
      followingSibling.siblings[0] = precedingSibling;
   }

   static {
      log = LogFactory.getLog(FONode.class);
   }

   public interface FONodeIterator extends ListIterator {
      FObj parentNode();

      FONode nextNode();

      FONode previousNode();

      FONode firstNode();

      FONode lastNode();
   }

   public static class GatherContextInfoFunction implements AdvancedMessageFormat.Function {
      public Object evaluate(Map params) {
         Object obj = params.get("source");
         if (obj instanceof PropertyList) {
            PropertyList propList = (PropertyList)obj;
            obj = propList.getFObj();
         }

         if (obj instanceof FONode) {
            FONode node = (FONode)obj;
            return node.getContextInfoAlt();
         } else {
            return null;
         }
      }

      public Object getName() {
         return "gatherContextInfo";
      }
   }
}
