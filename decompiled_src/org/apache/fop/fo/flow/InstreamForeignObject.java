package org.apache.fop.fo.flow;

import java.awt.geom.Point2D;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.XMLObj;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Locator;

public class InstreamForeignObject extends AbstractGraphics {
   private Point2D intrinsicDimensions;
   private boolean instrisicSizeDetermined;
   private Length intrinsicAlignmentAdjust;

   public InstreamForeignObject(FONode parent) {
      super(parent);
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null) {
         this.missingChildElementError("one (1) non-XSL namespace child");
      }

      this.getFOEventHandler().foreignObject(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      } else if (this.firstChild != null) {
         this.tooManyNodesError(loc, new QName(nsURI, (String)null, localName));
      }

   }

   public String getLocalName() {
      return "instream-foreign-object";
   }

   public int getNameId() {
      return 37;
   }

   private void prepareIntrinsicSize() {
      if (!this.instrisicSizeDetermined) {
         XMLObj child = (XMLObj)this.firstChild;
         Point2D csize = new Point2D.Float(-1.0F, -1.0F);
         this.intrinsicDimensions = child.getDimension(csize);
         if (this.intrinsicDimensions == null) {
            ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.ifoNoIntrinsicSize(this, this.getLocator());
         }

         this.intrinsicAlignmentAdjust = child.getIntrinsicAlignmentAdjust();
         this.instrisicSizeDetermined = true;
      }

   }

   public int getIntrinsicWidth() {
      this.prepareIntrinsicSize();
      return this.intrinsicDimensions != null ? (int)(this.intrinsicDimensions.getX() * 1000.0) : 0;
   }

   public int getIntrinsicHeight() {
      this.prepareIntrinsicSize();
      return this.intrinsicDimensions != null ? (int)(this.intrinsicDimensions.getY() * 1000.0) : 0;
   }

   public Length getIntrinsicAlignmentAdjust() {
      this.prepareIntrinsicSize();
      return this.intrinsicAlignmentAdjust;
   }

   protected void addChildNode(FONode child) throws FOPException {
      super.addChildNode(child);
   }

   public XMLObj getChildXMLObj() {
      return (XMLObj)this.firstChild;
   }
}
