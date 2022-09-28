package org.apache.fop.fo.pagination;

import java.awt.Rectangle;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.FODimension;
import org.apache.fop.datatypes.Numeric;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.xml.sax.Locator;

public abstract class Region extends FObj {
   private CommonBorderPaddingBackground commonBorderPaddingBackground;
   private int displayAlign;
   private int overflow;
   private String regionName;
   private Numeric referenceOrientation;
   private int writingMode;
   private SimplePageMaster layoutMaster;

   protected Region(FONode parent) {
      super(parent);
      this.layoutMaster = (SimplePageMaster)parent;
   }

   public void bind(PropertyList pList) throws FOPException {
      this.commonBorderPaddingBackground = pList.getBorderPaddingBackgroundProps();
      this.displayAlign = pList.get(87).getEnum();
      this.overflow = pList.get(169).getEnum();
      this.regionName = pList.get(199).getString();
      this.referenceOrientation = pList.get(197).getNumeric();
      this.writingMode = pList.getWritingMode();
      if (this.regionName.equals("")) {
         this.regionName = this.getDefaultRegionName();
      } else if (this.isReserved(this.getRegionName()) && !this.getRegionName().equals(this.getDefaultRegionName())) {
         this.getFOValidationEventProducer().illegalRegionName(this, this.getName(), this.regionName, this.getLocator());
      }

      if (this.getCommonBorderPaddingBackground().getBPPaddingAndBorder(false, (PercentBaseContext)null) != 0 || this.getCommonBorderPaddingBackground().getIPPaddingAndBorder(false, (PercentBaseContext)null) != 0) {
         this.getFOValidationEventProducer().nonZeroBorderPaddingOnRegion(this, this.getName(), this.regionName, true, this.getLocator());
      }

   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public abstract Rectangle getViewportRectangle(FODimension var1, SimplePageMaster var2);

   protected abstract String getDefaultRegionName();

   protected boolean isReserved(String name) {
      return name.equals("xsl-region-before") || name.equals("xsl-region-start") || name.equals("xsl-region-end") || name.equals("xsl-region-after") || name.equals("xsl-before-float-separator") || name.equals("xsl-footnote-separator");
   }

   protected PercentBaseContext getPageWidthContext(int lengthBase) {
      return this.layoutMaster.getPageWidthContext(lengthBase);
   }

   protected PercentBaseContext getPageHeightContext(int lengthBase) {
      return this.layoutMaster.getPageHeightContext(lengthBase);
   }

   public boolean generatesReferenceAreas() {
      return true;
   }

   protected Region getSiblingRegion(int regionId) {
      return this.layoutMaster.getRegion(regionId);
   }

   public CommonBorderPaddingBackground getCommonBorderPaddingBackground() {
      return this.commonBorderPaddingBackground;
   }

   public String getRegionName() {
      return this.regionName;
   }

   public int getWritingMode() {
      return this.writingMode;
   }

   public int getOverflow() {
      return this.overflow;
   }

   public int getDisplayAlign() {
      return this.displayAlign;
   }

   public int getReferenceOrientation() {
      return this.referenceOrientation.getValue();
   }
}
