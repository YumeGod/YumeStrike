package org.apache.batik.bridge;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.util.Map;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.dom.AbstractNode;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.ext.awt.image.PadMode;
import org.apache.batik.ext.awt.image.renderable.AffineRable8Bit;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.PadRable8Bit;
import org.apache.batik.ext.awt.image.spi.BrokenLinkProvider;
import org.apache.batik.ext.awt.image.spi.ImageTagRegistry;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.Platform;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.util.SoftReferenceCache;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class CursorManager implements SVGConstants, ErrorConstants {
   protected static Map cursorMap;
   public static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(0);
   public static final Cursor ANCHOR_CURSOR = Cursor.getPredefinedCursor(12);
   public static final Cursor TEXT_CURSOR = Cursor.getPredefinedCursor(2);
   public static final int DEFAULT_PREFERRED_WIDTH = 32;
   public static final int DEFAULT_PREFERRED_HEIGHT = 32;
   protected BridgeContext ctx;
   protected CursorCache cursorCache = new CursorCache();
   // $FF: synthetic field
   static Class class$org$apache$batik$bridge$CursorManager;

   public CursorManager(BridgeContext var1) {
      this.ctx = var1;
   }

   public static Cursor getPredefinedCursor(String var0) {
      return (Cursor)cursorMap.get(var0);
   }

   public Cursor convertCursor(Element var1) {
      Value var2 = CSSUtilities.getComputedStyle(var1, 10);
      String var3 = "auto";
      if (var2 != null) {
         if (var2.getCssValueType() == 1 && var2.getPrimitiveType() == 21) {
            var3 = var2.getStringValue();
            return this.convertBuiltInCursor(var1, var3);
         }

         if (var2.getCssValueType() == 2) {
            int var4 = var2.getLength();
            if (var4 == 1) {
               var2 = var2.item(0);
               if (var2.getPrimitiveType() == 21) {
                  var3 = var2.getStringValue();
                  return this.convertBuiltInCursor(var1, var3);
               }
            } else if (var4 > 1) {
               return this.convertSVGCursor(var1, var2);
            }
         }
      }

      return this.convertBuiltInCursor(var1, var3);
   }

   public Cursor convertBuiltInCursor(Element var1, String var2) {
      Cursor var3 = null;
      if (var2.charAt(0) == 'a') {
         String var4 = var1.getNamespaceURI();
         if ("http://www.w3.org/2000/svg".equals(var4)) {
            String var5 = var1.getLocalName();
            if ("a".equals(var5)) {
               var3 = ANCHOR_CURSOR;
            } else if (!"text".equals(var5) && !"tspan".equals(var5) && !"tref".equals(var5)) {
               if ("image".equals(var5)) {
                  return null;
               }

               var3 = DEFAULT_CURSOR;
            } else {
               var3 = TEXT_CURSOR;
            }
         } else {
            var3 = DEFAULT_CURSOR;
         }
      } else {
         var3 = getPredefinedCursor(var2);
      }

      return var3;
   }

   public Cursor convertSVGCursor(Element var1, Value var2) {
      int var3 = var2.getLength();
      Element var4 = null;

      for(int var5 = 0; var5 < var3 - 1; ++var5) {
         Value var6 = var2.item(var5);
         if (var6.getPrimitiveType() == 20) {
            String var7 = var6.getStringValue();

            try {
               var4 = this.ctx.getReferencedElement(var1, var7);
            } catch (BridgeException var10) {
               if (!"uri.badTarget".equals(var10.getCode())) {
                  throw var10;
               }
            }

            if (var4 != null) {
               String var8 = var4.getNamespaceURI();
               if ("http://www.w3.org/2000/svg".equals(var8) && "cursor".equals(var4.getLocalName())) {
                  Cursor var9 = this.convertSVGCursorElement(var4);
                  if (var9 != null) {
                     return var9;
                  }
               }
            }
         }
      }

      Value var11 = var2.item(var3 - 1);
      String var12 = "auto";
      if (var11.getPrimitiveType() == 21) {
         var12 = var11.getStringValue();
      }

      return this.convertBuiltInCursor(var1, var12);
   }

   public Cursor convertSVGCursorElement(Element var1) {
      String var2 = XLinkSupport.getXLinkHref(var1);
      if (var2.length() == 0) {
         throw new BridgeException(this.ctx, var1, "attribute.missing", new Object[]{"xlink:href"});
      } else {
         String var3 = AbstractNode.getBaseURI(var1);
         ParsedURL var4;
         if (var3 == null) {
            var4 = new ParsedURL(var2);
         } else {
            var4 = new ParsedURL(var3, var2);
         }

         org.apache.batik.parser.UnitProcessor.Context var5 = UnitProcessor.createContext(this.ctx, var1);
         String var6 = var1.getAttributeNS((String)null, "x");
         float var7 = 0.0F;
         if (var6.length() != 0) {
            var7 = UnitProcessor.svgHorizontalCoordinateToUserSpace(var6, "x", var5);
         }

         var6 = var1.getAttributeNS((String)null, "y");
         float var8 = 0.0F;
         if (var6.length() != 0) {
            var8 = UnitProcessor.svgVerticalCoordinateToUserSpace(var6, "y", var5);
         }

         CursorDescriptor var9 = new CursorDescriptor(var4, var7, var8);
         Cursor var10 = this.cursorCache.getCursor(var9);
         if (var10 != null) {
            return var10;
         } else {
            Point2D.Float var11 = new Point2D.Float(var7, var8);
            Filter var12 = this.cursorHrefToFilter(var1, var4, var11);
            if (var12 == null) {
               this.cursorCache.clearCursor(var9);
               return null;
            } else {
               Rectangle var13 = var12.getBounds2D().getBounds();
               RenderedImage var14 = var12.createScaledRendering(var13.width, var13.height, (RenderingHints)null);
               Image var15 = null;
               if (var14 instanceof Image) {
                  var15 = (Image)var14;
               } else {
                  var15 = this.renderedImageToImage(var14);
               }

               var11.x = var11.x < 0.0F ? 0.0F : var11.x;
               var11.y = var11.y < 0.0F ? 0.0F : var11.y;
               var11.x = var11.x > (float)(var13.width - 1) ? (float)(var13.width - 1) : var11.x;
               var11.y = var11.y > (float)(var13.height - 1) ? (float)(var13.height - 1) : var11.y;
               Cursor var16 = Toolkit.getDefaultToolkit().createCustomCursor(var15, new Point(Math.round(var11.x), Math.round(var11.y)), var4.toString());
               this.cursorCache.putCursor(var9, var16);
               return var16;
            }
         }
      }
   }

   protected Filter cursorHrefToFilter(Element var1, ParsedURL var2, Point2D var3) {
      AffineRable8Bit var4 = null;
      String var5 = var2.toString();
      Dimension var6 = null;
      DocumentLoader var7 = this.ctx.getDocumentLoader();
      SVGDocument var8 = (SVGDocument)var1.getOwnerDocument();
      URIResolver var9 = this.ctx.createURIResolver(var8, var7);

      try {
         SVGSVGElement var10 = null;
         Node var11 = var9.getNode(var5, var1);
         if (var11.getNodeType() != 9) {
            throw new BridgeException(this.ctx, var1, "uri.image.invalid", new Object[]{var5});
         }

         SVGDocument var12 = (SVGDocument)var11;
         this.ctx.initializeDocument(var12);
         var10 = var12.getRootElement();
         GraphicsNode var26 = this.ctx.getGVTBuilder().build(this.ctx, (Element)var10);
         float var13 = 32.0F;
         float var14 = 32.0F;
         org.apache.batik.parser.UnitProcessor.Context var15 = UnitProcessor.createContext(this.ctx, var10);
         String var16 = var10.getAttribute("width");
         if (var16.length() != 0) {
            var13 = UnitProcessor.svgHorizontalLengthToUserSpace(var16, "width", var15);
         }

         var16 = var10.getAttribute("height");
         if (var16.length() != 0) {
            var14 = UnitProcessor.svgVerticalLengthToUserSpace(var16, "height", var15);
         }

         var6 = Toolkit.getDefaultToolkit().getBestCursorSize(Math.round(var13), Math.round(var14));
         AffineTransform var17 = ViewBox.getPreserveAspectRatioTransform(var10, (float)var6.width, (float)var6.height, this.ctx);
         Filter var18 = var26.getGraphicsNodeRable(true);
         var4 = new AffineRable8Bit(var18, var17);
      } catch (BridgeException var19) {
         throw var19;
      } catch (SecurityException var20) {
         throw new BridgeException(this.ctx, var1, var20, "uri.unsecure", new Object[]{var5});
      } catch (Exception var21) {
      }

      if (var4 == null) {
         ImageTagRegistry var22 = ImageTagRegistry.getRegistry();
         Filter var24 = var22.readURL(var2);
         if (var24 == null) {
            return null;
         }

         if (BrokenLinkProvider.hasBrokenLinkProperty(var24)) {
            return null;
         }

         Rectangle var27 = var24.getBounds2D().getBounds();
         var6 = Toolkit.getDefaultToolkit().getBestCursorSize(var27.width, var27.height);
         if (var27 == null || var27.width <= 0 || var27.height <= 0) {
            return null;
         }

         AffineTransform var29 = new AffineTransform();
         if (var27.width > var6.width || var27.height > var6.height) {
            var29 = ViewBox.getPreserveAspectRatioTransform(new float[]{0.0F, 0.0F, (float)var27.width, (float)var27.height}, (short)2, true, (float)var6.width, (float)var6.height);
         }

         var4 = new AffineRable8Bit(var24, var29);
      }

      AffineTransform var23 = var4.getAffine();
      var23.transform(var3, var3);
      Rectangle var25 = new Rectangle(0, 0, var6.width, var6.height);
      PadRable8Bit var28 = new PadRable8Bit(var4, var25, PadMode.ZERO_PAD);
      return var28;
   }

   protected Image renderedImageToImage(RenderedImage var1) {
      int var2 = var1.getMinX();
      int var3 = var1.getMinY();
      SampleModel var4 = var1.getSampleModel();
      ColorModel var5 = var1.getColorModel();
      WritableRaster var6 = Raster.createWritableRaster(var4, new Point(var2, var3));
      var1.copyData(var6);
      return new BufferedImage(var5, var6, var5.isAlphaPremultiplied(), (Hashtable)null);
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      Toolkit var0 = Toolkit.getDefaultToolkit();
      cursorMap = new Hashtable();
      cursorMap.put("crosshair", Cursor.getPredefinedCursor(1));
      cursorMap.put("default", Cursor.getPredefinedCursor(0));
      cursorMap.put("pointer", Cursor.getPredefinedCursor(12));
      cursorMap.put("e-resize", Cursor.getPredefinedCursor(11));
      cursorMap.put("ne-resize", Cursor.getPredefinedCursor(7));
      cursorMap.put("nw-resize", Cursor.getPredefinedCursor(6));
      cursorMap.put("n-resize", Cursor.getPredefinedCursor(8));
      cursorMap.put("se-resize", Cursor.getPredefinedCursor(5));
      cursorMap.put("sw-resize", Cursor.getPredefinedCursor(4));
      cursorMap.put("s-resize", Cursor.getPredefinedCursor(9));
      cursorMap.put("w-resize", Cursor.getPredefinedCursor(10));
      cursorMap.put("text", Cursor.getPredefinedCursor(2));
      cursorMap.put("wait", Cursor.getPredefinedCursor(3));
      Cursor var1 = Cursor.getPredefinedCursor(13);
      if (Platform.isOSX) {
         try {
            Image var2 = var0.createImage((class$org$apache$batik$bridge$CursorManager == null ? (class$org$apache$batik$bridge$CursorManager = class$("org.apache.batik.bridge.CursorManager")) : class$org$apache$batik$bridge$CursorManager).getResource("resources/move.gif"));
            var1 = var0.createCustomCursor(var2, new Point(11, 11), "move");
         } catch (Exception var5) {
         }
      }

      cursorMap.put("move", var1);

      Cursor var6;
      try {
         Image var3 = var0.createImage((class$org$apache$batik$bridge$CursorManager == null ? (class$org$apache$batik$bridge$CursorManager = class$("org.apache.batik.bridge.CursorManager")) : class$org$apache$batik$bridge$CursorManager).getResource("resources/help.gif"));
         var6 = var0.createCustomCursor(var3, new Point(1, 3), "help");
      } catch (Exception var4) {
         var6 = Cursor.getPredefinedCursor(12);
      }

      cursorMap.put("help", var6);
   }

   static class CursorCache extends SoftReferenceCache {
      public CursorCache() {
      }

      public Cursor getCursor(CursorDescriptor var1) {
         return (Cursor)this.requestImpl(var1);
      }

      public void putCursor(CursorDescriptor var1, Cursor var2) {
         this.putImpl(var1, var2);
      }

      public void clearCursor(CursorDescriptor var1) {
         this.clearImpl(var1);
      }
   }

   static class CursorDescriptor {
      ParsedURL purl;
      float x;
      float y;
      String desc;

      public CursorDescriptor(ParsedURL var1, float var2, float var3) {
         if (var1 == null) {
            throw new IllegalArgumentException();
         } else {
            this.purl = var1;
            this.x = var2;
            this.y = var3;
            this.desc = this.getClass().getName() + "\n\t:[" + this.purl + "]\n\t:[" + var2 + "]:[" + var3 + "]";
         }
      }

      public boolean equals(Object var1) {
         if (var1 != null && var1 instanceof CursorDescriptor) {
            CursorDescriptor var2 = (CursorDescriptor)var1;
            boolean var3 = this.purl.equals(var2.purl) && this.x == var2.x && this.y == var2.y;
            return var3;
         } else {
            return false;
         }
      }

      public String toString() {
         return this.desc;
      }

      public int hashCode() {
         return this.desc.hashCode();
      }
   }
}
