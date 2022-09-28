package org.apache.xerces.dom;

import java.util.Vector;
import org.apache.xerces.dom3.as.ASAttributeDeclaration;
import org.apache.xerces.dom3.as.ASContentModel;
import org.apache.xerces.dom3.as.ASElementDeclaration;
import org.apache.xerces.dom3.as.ASEntityDeclaration;
import org.apache.xerces.dom3.as.ASModel;
import org.apache.xerces.dom3.as.ASNamedObjectMap;
import org.apache.xerces.dom3.as.ASNotationDeclaration;
import org.apache.xerces.dom3.as.ASObject;
import org.apache.xerces.dom3.as.ASObjectList;
import org.apache.xerces.dom3.as.DOMASException;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.w3c.dom.DOMException;

/** @deprecated */
public class ASModelImpl implements ASModel {
   boolean fNamespaceAware = true;
   protected Vector fASModels = new Vector();
   protected SchemaGrammar fGrammar = null;

   public ASModelImpl() {
   }

   public ASModelImpl(boolean var1) {
      this.fNamespaceAware = var1;
   }

   public short getAsNodeType() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public ASModel getOwnerASModel() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setOwnerASModel(ASModel var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public String getNodeName() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setNodeName(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public String getPrefix() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setPrefix(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public String getLocalName() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setLocalName(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public String getNamespaceURI() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setNamespaceURI(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public ASObject cloneASObject(boolean var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public boolean getIsNamespaceAware() {
      return this.fNamespaceAware;
   }

   public short getUsageLocation() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public String getAsLocation() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setAsLocation(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public String getAsHint() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void setAsHint(String var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public boolean getContainer() {
      return this.fGrammar != null;
   }

   public ASNamedObjectMap getElementDeclarations() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public ASNamedObjectMap getAttributeDeclarations() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public ASNamedObjectMap getNotationDeclarations() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public ASNamedObjectMap getEntityDeclarations() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public ASNamedObjectMap getContentModelDeclarations() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void addASModel(ASModel var1) {
      this.fASModels.addElement(var1);
   }

   public ASObjectList getASModels() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void removeAS(ASModel var1) {
      this.fASModels.removeElement(var1);
   }

   public boolean validate() {
      String var1 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var1);
   }

   public void importASObject(ASObject var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public void insertASObject(ASObject var1) {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public ASElementDeclaration createASElementDeclaration(String var1, String var2) throws DOMException {
      String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var3);
   }

   public ASAttributeDeclaration createASAttributeDeclaration(String var1, String var2) throws DOMException {
      String var3 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var3);
   }

   public ASNotationDeclaration createASNotationDeclaration(String var1, String var2, String var3, String var4) throws DOMException {
      String var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var5);
   }

   public ASEntityDeclaration createASEntityDeclaration(String var1) throws DOMException {
      String var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var2);
   }

   public ASContentModel createASContentModel(int var1, int var2, short var3) throws DOMASException {
      String var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_SUPPORTED_ERR", (Object[])null);
      throw new DOMException((short)9, var4);
   }

   public SchemaGrammar getGrammar() {
      return this.fGrammar;
   }

   public void setGrammar(SchemaGrammar var1) {
      this.fGrammar = var1;
   }

   public Vector getInternalASModels() {
      return this.fASModels;
   }
}
