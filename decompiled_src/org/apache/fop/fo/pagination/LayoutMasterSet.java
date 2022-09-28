package org.apache.fop.fo.pagination;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class LayoutMasterSet extends FObj {
   private Map simplePageMasters;
   private Map pageSequenceMasters;

   public LayoutMasterSet(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
   }

   protected void startOfNode() throws FOPException {
      this.getRoot().setLayoutMasterSet(this);
      this.simplePageMasters = new HashMap();
      this.pageSequenceMasters = new HashMap();
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("(simple-page-master|page-sequence-master)+");
      }

      this.checkRegionNames();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !localName.equals("simple-page-master") && !localName.equals("page-sequence-master")) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   private void checkRegionNames() throws ValidationException {
      Map allRegions = new HashMap();
      Iterator spm = this.simplePageMasters.values().iterator();

      while(spm.hasNext()) {
         SimplePageMaster simplePageMaster = (SimplePageMaster)spm.next();
         Map spmRegions = simplePageMaster.getRegions();

         Region region;
         for(Iterator e = spmRegions.values().iterator(); e.hasNext(); allRegions.put(region.getRegionName(), region.getDefaultRegionName())) {
            region = (Region)e.next();
            if (allRegions.containsKey(region.getRegionName())) {
               String defaultRegionName = (String)allRegions.get(region.getRegionName());
               if (!defaultRegionName.equals(region.getDefaultRegionName())) {
                  this.getFOValidationEventProducer().regionNameMappedToMultipleRegionClasses(this, region.getRegionName(), defaultRegionName, region.getDefaultRegionName(), this.getLocator());
               }
            }
         }
      }

   }

   protected void addSimplePageMaster(SimplePageMaster sPM) throws ValidationException {
      String masterName = sPM.getMasterName();
      if (this.existsName(masterName)) {
         this.getFOValidationEventProducer().masterNameNotUnique(this, this.getName(), masterName, sPM.getLocator());
      }

      this.simplePageMasters.put(masterName, sPM);
   }

   private boolean existsName(String masterName) {
      return this.simplePageMasters.containsKey(masterName) || this.pageSequenceMasters.containsKey(masterName);
   }

   public SimplePageMaster getSimplePageMaster(String masterName) {
      return (SimplePageMaster)this.simplePageMasters.get(masterName);
   }

   protected void addPageSequenceMaster(String masterName, PageSequenceMaster pSM) throws ValidationException {
      if (this.existsName(masterName)) {
         this.getFOValidationEventProducer().masterNameNotUnique(this, this.getName(), masterName, pSM.getLocator());
      }

      this.pageSequenceMasters.put(masterName, pSM);
   }

   public PageSequenceMaster getPageSequenceMaster(String masterName) {
      return (PageSequenceMaster)this.pageSequenceMasters.get(masterName);
   }

   public boolean regionNameExists(String regionName) {
      Iterator e = this.simplePageMasters.values().iterator();

      do {
         if (!e.hasNext()) {
            return false;
         }
      } while(!((SimplePageMaster)e.next()).regionNameExists(regionName));

      return true;
   }

   public String getLocalName() {
      return "layout-master-set";
   }

   public int getNameId() {
      return 38;
   }
}
