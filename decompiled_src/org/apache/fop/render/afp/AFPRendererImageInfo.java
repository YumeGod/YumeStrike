package org.apache.fop.render.afp;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import org.apache.fop.render.RendererContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class AFPRendererImageInfo {
   protected final String uri;
   protected final Rectangle2D pos;
   protected final Point origin;
   protected final Map foreignAttributes;
   protected final ImageInfo info;
   protected final Image img;
   protected RendererContext rendererContext;

   public AFPRendererImageInfo(String uri, Rectangle2D pos, Point origin, ImageInfo info, Image img, RendererContext rendererContext, Map foreignAttributes) {
      this.uri = uri;
      this.pos = pos;
      this.origin = origin;
      this.info = info;
      this.img = img;
      this.rendererContext = rendererContext;
      this.foreignAttributes = foreignAttributes;
   }

   public void setRendererContext(RendererContext rendererContext) {
      this.rendererContext = rendererContext;
   }

   public ImageInfo getImageInfo() {
      return this.info;
   }

   public Image getImage() {
      return this.img;
   }

   public RendererContext getRendererContext() {
      return this.rendererContext;
   }

   public Map getForeignAttributes() {
      return this.foreignAttributes;
   }

   public String getURI() {
      return this.uri;
   }

   public Point getOrigin() {
      return this.origin;
   }

   public Rectangle2D getPosition() {
      return this.pos;
   }

   public String toString() {
      return "AFPRendererImageInfo{\n\turi=" + this.uri + ",\n" + "\tinfo=" + this.info + ",\n" + "\tpos=" + this.pos + ",\n" + "\torigin=" + this.origin + ",\n" + "\timg=" + this.img + ",\n" + "\tforeignAttributes=" + this.foreignAttributes + ",\n" + "\trendererContext=" + this.rendererContext + "\n" + "}";
   }
}
