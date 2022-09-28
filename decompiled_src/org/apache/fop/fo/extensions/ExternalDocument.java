package org.apache.fop.fo.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.GraphicsProperties;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.pagination.AbstractPageSequence;
import org.apache.fop.fo.properties.LengthRangeProperty;
import org.xml.sax.Locator;

public class ExternalDocument extends AbstractPageSequence implements GraphicsProperties {
   private LengthRangeProperty blockProgressionDimension;
   private Length contentHeight;
   private Length contentWidth;
   private int displayAlign;
   private Length height;
   private LengthRangeProperty inlineProgressionDimension;
   private int overflow;
   private int scaling;
   private String src;
   private int textAlign;
   private Length width;

   public ExternalDocument(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.blockProgressionDimension = pList.get(17).getLengthRange();
      this.contentHeight = pList.get(78).getLength();
      this.contentWidth = pList.get(80).getLength();
      this.displayAlign = pList.get(87).getEnum();
      this.height = pList.get(115).getLength();
      this.inlineProgressionDimension = pList.get(127).getLengthRange();
      this.overflow = pList.get(169).getEnum();
      this.scaling = pList.get(215).getEnum();
      this.textAlign = pList.get(245).getEnum();
      this.width = pList.get(264).getLength();
      this.src = pList.get(232).getString();
      if (this.src == null || this.src.length() == 0) {
         this.missingPropertyError("src");
      }

   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startExternalDocument(this);
   }

   protected void endOfNode() throws FOPException {
      this.getFOEventHandler().endExternalDocument(this);
      super.endOfNode();
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      this.invalidChildError(loc, nsURI, localName);
   }

   public String getSrc() {
      return this.src;
   }

   public LengthRangeProperty getInlineProgressionDimension() {
      return this.inlineProgressionDimension;
   }

   public LengthRangeProperty getBlockProgressionDimension() {
      return this.blockProgressionDimension;
   }

   public Length getHeight() {
      return this.height;
   }

   public Length getWidth() {
      return this.width;
   }

   public Length getContentHeight() {
      return this.contentHeight;
   }

   public Length getContentWidth() {
      return this.contentWidth;
   }

   public int getScaling() {
      return this.scaling;
   }

   public int getOverflow() {
      return this.overflow;
   }

   public int getDisplayAlign() {
      return this.displayAlign;
   }

   public int getTextAlign() {
      return this.textAlign;
   }

   public String getNamespaceURI() {
      return "http://xmlgraphics.apache.org/fop/extensions";
   }

   public String getNormalNamespacePrefix() {
      return "fox";
   }

   public String getLocalName() {
      return "external-document";
   }
}
