package org.apache.fop.fo.pagination;

import java.util.HashMap;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class PageSequence extends AbstractPageSequence {
   private String country;
   private String language;
   private String masterReference;
   private Map flowMap;
   private SimplePageMaster simplePageMaster;
   private PageSequenceMaster pageSequenceMaster;
   private Title titleFO;
   private Flow mainFlow = null;

   public PageSequence(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.country = pList.get(81).getString();
      this.language = pList.get(134).getString();
      this.masterReference = pList.get(154).getString();
      if (this.masterReference == null || this.masterReference.equals("")) {
         this.missingPropertyError("master-reference");
      }

   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.flowMap = new HashMap();
      this.simplePageMaster = this.getRoot().getLayoutMasterSet().getSimplePageMaster(this.masterReference);
      if (this.simplePageMaster == null) {
         this.pageSequenceMaster = this.getRoot().getLayoutMasterSet().getPageSequenceMaster(this.masterReference);
         if (this.pageSequenceMaster == null) {
            this.getFOValidationEventProducer().masterNotFound(this, this.getName(), this.masterReference, this.getLocator());
         }
      }

      this.getFOEventHandler().startPageSequence(this);
   }

   protected void endOfNode() throws FOPException {
      if (this.mainFlow == null) {
         this.missingChildElementError("(title?,static-content*,flow)");
      }

      this.getFOEventHandler().endPageSequence(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         if ("title".equals(localName)) {
            if (this.titleFO != null) {
               this.tooManyNodesError(loc, "fo:title");
            } else if (!this.flowMap.isEmpty()) {
               this.nodesOutOfOrderError(loc, "fo:title", "fo:static-content");
            } else if (this.mainFlow != null) {
               this.nodesOutOfOrderError(loc, "fo:title", "fo:flow");
            }
         } else if ("static-content".equals(localName)) {
            if (this.mainFlow != null) {
               this.nodesOutOfOrderError(loc, "fo:static-content", "fo:flow");
            }
         } else if ("flow".equals(localName)) {
            if (this.mainFlow != null) {
               this.tooManyNodesError(loc, "fo:flow");
            }
         } else {
            this.invalidChildError(loc, nsURI, localName);
         }
      }

   }

   public void addChildNode(FONode child) throws FOPException {
      int childId = child.getNameId();
      switch (childId) {
         case 16:
            this.mainFlow = (Flow)child;
            this.addFlow(this.mainFlow);
            break;
         case 70:
            this.addFlow((StaticContent)child);
            this.flowMap.put(((StaticContent)child).getFlowName(), child);
            break;
         case 80:
            this.titleFO = (Title)child;
            break;
         default:
            super.addChildNode(child);
      }

   }

   private void addFlow(Flow flow) throws ValidationException {
      String flowName = flow.getFlowName();
      if (this.hasFlowName(flowName)) {
         this.getFOValidationEventProducer().duplicateFlowNameInPageSequence(this, flow.getName(), flowName, flow.getLocator());
      }

      if (!this.getRoot().getLayoutMasterSet().regionNameExists(flowName) && !flowName.equals("xsl-before-float-separator") && !flowName.equals("xsl-footnote-separator")) {
         this.getFOValidationEventProducer().flowNameNotMapped(this, flow.getName(), flowName, flow.getLocator());
      }

   }

   public StaticContent getStaticContent(String name) {
      return (StaticContent)this.flowMap.get(name);
   }

   public Title getTitleFO() {
      return this.titleFO;
   }

   public Flow getMainFlow() {
      return this.mainFlow;
   }

   public boolean hasFlowName(String flowName) {
      return this.flowMap.containsKey(flowName);
   }

   public Map getFlowMap() {
      return this.flowMap;
   }

   public SimplePageMaster getNextSimplePageMaster(int page, boolean isFirstPage, boolean isLastPage, boolean isBlank) throws PageProductionException {
      if (this.pageSequenceMaster == null) {
         return this.simplePageMaster;
      } else {
         boolean isOddPage = page % 2 == 1;
         if (log.isDebugEnabled()) {
            log.debug("getNextSimplePageMaster(page=" + page + " isOdd=" + isOddPage + " isFirst=" + isFirstPage + " isLast=" + isLastPage + " isBlank=" + isBlank + ")");
         }

         return this.pageSequenceMaster.getNextSimplePageMaster(isOddPage, isFirstPage, isLastPage, isBlank);
      }
   }

   public boolean goToPreviousSimplePageMaster() {
      return this.pageSequenceMaster == null || this.pageSequenceMaster.goToPreviousSimplePageMaster();
   }

   public boolean hasPagePositionLast() {
      return this.pageSequenceMaster != null && this.pageSequenceMaster.hasPagePositionLast();
   }

   public boolean hasPagePositionOnly() {
      return this.pageSequenceMaster != null && this.pageSequenceMaster.hasPagePositionOnly();
   }

   public String getMasterReference() {
      return this.masterReference;
   }

   public String getLocalName() {
      return "page-sequence";
   }

   public int getNameId() {
      return 53;
   }

   public String getCountry() {
      return this.country;
   }

   public String getLanguage() {
      return this.language;
   }

   public void releasePageSequence() {
      this.mainFlow = null;
      this.flowMap.clear();
   }
}
