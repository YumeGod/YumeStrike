package org.apache.fop.area;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.SimplePercentBaseContext;
import org.apache.fop.fo.pagination.Region;
import org.apache.fop.fo.pagination.RegionBody;
import org.apache.fop.fo.pagination.SimplePageMaster;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.layoutmgr.TraitSetter;

public class Page extends AreaTreeObject implements Serializable, Cloneable {
   private RegionViewport regionBefore = null;
   private RegionViewport regionStart = null;
   private RegionViewport regionBody = null;
   private RegionViewport regionEnd = null;
   private RegionViewport regionAfter = null;
   private Map unresolved = null;
   private boolean fakeNonEmpty = false;

   public Page() {
   }

   public Page(SimplePageMaster spm) {
      FODimension pageViewPortDims = new FODimension(spm.getPageWidth().getValue(), spm.getPageHeight().getValue());
      CommonMarginBlock mProps = spm.getCommonMarginBlock();
      SimplePercentBaseContext pageWidthContext = new SimplePercentBaseContext((PercentBaseContext)null, 5, pageViewPortDims.ipd);
      SimplePercentBaseContext pageHeightContext = new SimplePercentBaseContext((PercentBaseContext)null, 5, pageViewPortDims.bpd);
      Rectangle pageRefRect = new Rectangle(mProps.marginLeft.getValue(pageWidthContext), mProps.marginTop.getValue(pageHeightContext), pageViewPortDims.ipd - mProps.marginLeft.getValue(pageWidthContext) - mProps.marginRight.getValue(pageWidthContext), pageViewPortDims.bpd - mProps.marginTop.getValue(pageHeightContext) - mProps.marginBottom.getValue(pageHeightContext));
      FODimension reldims = new FODimension(0, 0);
      CTM pageCTM = CTM.getCTMandRelDims(spm.getReferenceOrientation(), spm.getWritingMode(), pageRefRect, reldims);
      RegionReference rr = null;
      Iterator regenum = spm.getRegions().values().iterator();

      while(regenum.hasNext()) {
         Region r = (Region)regenum.next();
         RegionViewport rvp = this.makeRegionViewport(r, reldims, pageCTM, spm);
         if (r.getNameId() == 58) {
            rr = new BodyRegion((RegionBody)r, rvp);
         } else {
            rr = new RegionReference(r, rvp);
         }

         TraitSetter.addBorders((Area)rr, r.getCommonBorderPaddingBackground(), false, false, false, false, (PercentBaseContext)null);
         TraitSetter.addPadding((Area)rr, r.getCommonBorderPaddingBackground(), false, false, false, false, (PercentBaseContext)null);
         this.setRegionReferencePosition((RegionReference)rr, r, rvp.getViewArea());
         rvp.setRegionReference((RegionReference)rr);
         this.setRegionViewport(r.getNameId(), rvp);
      }

   }

   public void fakeNonEmpty() {
      this.fakeNonEmpty = true;
   }

   private RegionViewport makeRegionViewport(Region r, FODimension reldims, CTM pageCTM, SimplePageMaster spm) {
      Rectangle2D relRegionRect = r.getViewportRectangle(reldims, spm);
      Rectangle2D absRegionRect = pageCTM.transform(relRegionRect);
      RegionViewport rv = new RegionViewport(absRegionRect);
      rv.setBPD((int)relRegionRect.getHeight());
      rv.setIPD((int)relRegionRect.getWidth());
      TraitSetter.addBackground(rv, r.getCommonBorderPaddingBackground(), (PercentBaseContext)null);
      rv.setClip(r.getOverflow() == 57 || r.getOverflow() == 42);
      return rv;
   }

   private void setRegionReferencePosition(RegionReference rr, Region r, Rectangle2D absRegVPRect) {
      FODimension reldims = new FODimension(0, 0);
      rr.setCTM(CTM.getCTMandRelDims(r.getReferenceOrientation(), r.getWritingMode(), absRegVPRect, reldims));
      rr.setIPD(reldims.ipd - rr.getBorderAndPaddingWidthStart() - rr.getBorderAndPaddingWidthEnd());
      rr.setBPD(reldims.bpd - rr.getBorderAndPaddingWidthBefore() - rr.getBorderAndPaddingWidthAfter());
   }

   public void setRegionViewport(int areaclass, RegionViewport port) {
      if (areaclass == 57) {
         this.regionBefore = port;
      } else if (areaclass == 61) {
         this.regionStart = port;
      } else if (areaclass == 58) {
         this.regionBody = port;
      } else if (areaclass == 59) {
         this.regionEnd = port;
      } else if (areaclass == 56) {
         this.regionAfter = port;
      }

   }

   public RegionViewport getRegionViewport(int areaClass) {
      switch (areaClass) {
         case 56:
            return this.regionAfter;
         case 57:
            return this.regionBefore;
         case 58:
            return this.regionBody;
         case 59:
            return this.regionEnd;
         case 60:
         default:
            throw new IllegalArgumentException("No such area class with ID = " + areaClass);
         case 61:
            return this.regionStart;
      }
   }

   public boolean isEmpty() {
      if (this.fakeNonEmpty) {
         return false;
      } else if (this.regionBody == null) {
         return true;
      } else {
         BodyRegion body = (BodyRegion)this.regionBody.getRegionReference();
         return body.isEmpty();
      }
   }

   public Object clone() {
      Page p = new Page();
      if (this.regionBefore != null) {
         p.regionBefore = (RegionViewport)this.regionBefore.clone();
      }

      if (this.regionStart != null) {
         p.regionStart = (RegionViewport)this.regionStart.clone();
      }

      if (this.regionBody != null) {
         p.regionBody = (RegionViewport)this.regionBody.clone();
      }

      if (this.regionEnd != null) {
         p.regionEnd = (RegionViewport)this.regionEnd.clone();
      }

      if (this.regionAfter != null) {
         p.regionAfter = (RegionViewport)this.regionAfter.clone();
      }

      return p;
   }

   public void setUnresolvedReferences(Map unres) {
      this.unresolved = unres;
   }

   public Map getUnresolvedReferences() {
      return this.unresolved;
   }
}
