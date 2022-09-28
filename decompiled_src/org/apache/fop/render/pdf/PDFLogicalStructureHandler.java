package org.apache.fop.render.pdf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.pdf.PDFArray;
import org.apache.fop.pdf.PDFDictionary;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.pdf.PDFLink;
import org.apache.fop.pdf.PDFName;
import org.apache.fop.pdf.PDFPage;
import org.apache.fop.pdf.PDFParentTree;
import org.apache.fop.pdf.PDFStructElem;
import org.apache.fop.pdf.PDFStructTreeRoot;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class PDFLogicalStructureHandler {
   private static final PDFName MCR;
   private static final PDFName OBJR;
   private static final MarkedContentInfo ARTIFACT;
   private final PDFDocument pdfDoc;
   private final EventBroadcaster eventBroadcaster;
   private final Map structTreeMap = new HashMap();
   private final PDFParentTree parentTree = new PDFParentTree();
   private int parentTreeKey;
   private PDFPage currentPage;
   private PDFArray pageParentTreeArray;
   private PDFStructElem rootStructureElement;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   PDFLogicalStructureHandler(PDFDocument pdfDoc, EventBroadcaster eventBroadcaster) {
      this.pdfDoc = pdfDoc;
      this.eventBroadcaster = eventBroadcaster;
      PDFStructTreeRoot structTreeRoot = pdfDoc.getFactory().makeStructTreeRoot(this.parentTree);
      this.rootStructureElement = pdfDoc.getFactory().makeStructureElement(FOToPDFRoleMap.mapFormattingObject("root", structTreeRoot), structTreeRoot);
      structTreeRoot.addKid(this.rootStructureElement);
   }

   void processStructureTree(NodeList structureTree, Locale language) {
      this.pdfDoc.enforceLanguageOnRoot();
      PDFStructElem structElemPart = this.pdfDoc.getFactory().makeStructureElement(FOToPDFRoleMap.mapFormattingObject("page-sequence", this.rootStructureElement), this.rootStructureElement);
      this.rootStructureElement.addKid(structElemPart);
      if (language != null) {
         structElemPart.setLanguage(language);
      }

      int i = 0;

      for(int n = structureTree.getLength(); i < n; ++i) {
         Node node = structureTree.item(i);
         if (!$assertionsDisabled && !node.getLocalName().equals("flow") && !node.getLocalName().equals("static-content")) {
            throw new AssertionError();
         }

         PDFStructElem structElemSect = this.pdfDoc.getFactory().makeStructureElement(FOToPDFRoleMap.mapFormattingObject(node.getLocalName(), structElemPart), structElemPart);
         structElemPart.addKid(structElemSect);
         NodeList childNodes = node.getChildNodes();
         int j = 0;

         for(int m = childNodes.getLength(); j < m; ++j) {
            this.processNode(childNodes.item(j), structElemSect, true);
         }
      }

   }

   private void processNode(Node node, PDFStructElem parent, boolean addKid) {
      Node attr = node.getAttributes().getNamedItemNS("http://xmlgraphics.apache.org/fop/internal", "ptr");
      if (!$assertionsDisabled && attr == null) {
         throw new AssertionError();
      } else {
         String ptr = attr.getNodeValue();
         PDFStructElem structElem = this.pdfDoc.getFactory().makeStructureElement(FOToPDFRoleMap.mapFormattingObject(node, parent, this.eventBroadcaster), parent);
         if (addKid) {
            parent.addKid(structElem);
         }

         String nodeName = node.getLocalName();
         if (nodeName.equals("external-graphic") || nodeName.equals("instream-foreign-object")) {
            Node altTextNode = node.getAttributes().getNamedItemNS("http://xmlgraphics.apache.org/fop/extensions", "alt-text");
            if (altTextNode != null) {
               structElem.put("Alt", altTextNode.getNodeValue());
            } else {
               structElem.put("Alt", "No alternate text specified");
            }
         }

         this.structTreeMap.put(ptr, structElem);
         NodeList nodes = node.getChildNodes();
         int i = 0;

         for(int n = nodes.getLength(); i < n; ++i) {
            this.processNode(nodes.item(i), structElem, false);
         }

      }
   }

   private int getNextParentTreeKey() {
      return this.parentTreeKey++;
   }

   void startPage(PDFPage page) {
      this.currentPage = page;
      this.currentPage.setStructParents(this.getNextParentTreeKey());
      this.pageParentTreeArray = new PDFArray();
   }

   void endPage() {
      this.pdfDoc.registerObject(this.pageParentTreeArray);
      this.parentTree.getNums().put(this.currentPage.getStructParents(), this.pageParentTreeArray);
   }

   private MarkedContentInfo addToParentTree(String structurePointer) {
      PDFStructElem parent = (PDFStructElem)this.structTreeMap.get(structurePointer);
      if (parent == null) {
         return ARTIFACT;
      } else {
         this.pageParentTreeArray.add(parent);
         String type = parent.getStructureType().toString();
         int mcid = this.pageParentTreeArray.length() - 1;
         return new MarkedContentInfo(type, mcid, parent);
      }
   }

   MarkedContentInfo addTextContentItem(String structurePointer) {
      MarkedContentInfo mci = this.addToParentTree(structurePointer);
      if (mci != ARTIFACT) {
         PDFDictionary contentItem = new PDFDictionary();
         contentItem.put("Type", MCR);
         contentItem.put("Pg", this.currentPage);
         contentItem.put("MCID", mci.mcid);
         mci.parent.addKid(contentItem);
      }

      return mci;
   }

   MarkedContentInfo addImageContentItem(String structurePointer) {
      MarkedContentInfo mci = this.addToParentTree(structurePointer);
      if (mci != ARTIFACT) {
         mci.parent.setMCIDKid(mci.mcid);
         mci.parent.setPage(this.currentPage);
      }

      return mci;
   }

   void addLinkContentItem(PDFLink link, String structurePointer) {
      int structParent = this.getNextParentTreeKey();
      link.setStructParent(structParent);
      this.parentTree.getNums().put(structParent, link);
      PDFDictionary contentItem = new PDFDictionary();
      contentItem.put("Type", OBJR);
      contentItem.put("Pg", this.currentPage);
      contentItem.put("Obj", link);
      PDFStructElem parent = (PDFStructElem)this.structTreeMap.get(structurePointer);
      parent.addKid(contentItem);
   }

   static {
      $assertionsDisabled = !PDFLogicalStructureHandler.class.desiredAssertionStatus();
      MCR = new PDFName("MCR");
      OBJR = new PDFName("OBJR");
      ARTIFACT = new MarkedContentInfo((String)null, -1, (PDFStructElem)null);
   }

   static final class MarkedContentInfo {
      final String tag;
      final int mcid;
      private final PDFStructElem parent;

      private MarkedContentInfo(String tag, int mcid, PDFStructElem parent) {
         this.tag = tag;
         this.mcid = mcid;
         this.parent = parent;
      }

      // $FF: synthetic method
      MarkedContentInfo(String x0, int x1, PDFStructElem x2, Object x3) {
         this(x0, x1, x2);
      }
   }
}
