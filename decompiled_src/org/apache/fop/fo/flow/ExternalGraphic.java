package org.apache.fop.fo.flow;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.PropertyList;
import org.apache.fop.fo.ValidationException;
import org.apache.fop.fo.properties.FixedLength;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.xml.sax.Locator;

public class ExternalGraphic extends AbstractGraphics {
   private String src;
   private String url;
   private int intrinsicWidth;
   private int intrinsicHeight;
   private Length intrinsicAlignmentAdjust;

   public ExternalGraphic(FONode parent) {
      super(parent);
   }

   public void bind(PropertyList pList) throws FOPException {
      super.bind(pList);
      this.src = pList.get(232).getString();
      this.url = URISpecification.getURL(this.src);
      FOUserAgent userAgent = this.getUserAgent();
      ImageManager manager = userAgent.getFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         info = manager.getImageInfo(this.url, userAgent.getImageSessionContext());
      } catch (ImageException var7) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, this.url, var7, this.getLocator());
      } catch (FileNotFoundException var8) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, this.url, var8, this.getLocator());
      } catch (IOException var9) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, this.url, var9, this.getLocator());
      }

      if (info != null) {
         this.intrinsicWidth = info.getSize().getWidthMpt();
         this.intrinsicHeight = info.getSize().getHeightMpt();
         int baseline = info.getSize().getBaselinePositionFromBottom();
         if (baseline != 0) {
            this.intrinsicAlignmentAdjust = FixedLength.getInstance((double)(-baseline));
         }
      }

   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().image(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getSrc() {
      return this.src;
   }

   public String getURL() {
      return this.url;
   }

   public String getLocalName() {
      return "external-graphic";
   }

   public int getNameId() {
      return 14;
   }

   public int getIntrinsicWidth() {
      return this.intrinsicWidth;
   }

   public int getIntrinsicHeight() {
      return this.intrinsicHeight;
   }

   public Length getIntrinsicAlignmentAdjust() {
      return this.intrinsicAlignmentAdjust;
   }
}
