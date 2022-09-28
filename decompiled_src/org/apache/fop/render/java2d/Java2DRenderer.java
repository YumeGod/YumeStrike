package org.apache.fop.render.java2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.CTM;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.Leader;
import org.apache.fop.area.inline.SpaceArea;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.area.inline.WordArea;
import org.apache.fop.datatypes.URISpecification;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.Graphics2DAdapter;
import org.apache.fop.render.RendererContext;
import org.apache.fop.render.extensions.prepress.PageBoundaries;
import org.apache.fop.render.extensions.prepress.PageScale;
import org.apache.fop.render.pdf.CTMHelper;
import org.apache.fop.util.CharUtilities;
import org.apache.fop.util.ColorUtil;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.impl.ImageGraphics2D;
import org.apache.xmlgraphics.image.loader.impl.ImageRendered;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.xml.sax.Locator;

public abstract class Java2DRenderer extends AbstractPathOrientedRenderer implements Printable {
   public static final String JAVA2D_TRANSPARENT_PAGE_BACKGROUND = "transparent-page-background";
   protected double scaleFactor = 1.0;
   protected int pageWidth = 0;
   protected int pageHeight = 0;
   protected List pageViewportList = new ArrayList();
   private int currentPageNumber = 0;
   protected boolean antialiasing = true;
   protected boolean qualityRendering = true;
   protected boolean transparentPageBackground = false;
   protected Java2DGraphicsState state;
   private final Stack stateStack = new Stack();
   private boolean renderingDone;
   private GeneralPath currentPath = null;
   private static final ImageFlavor[] FLAVOURS;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public void setUserAgent(FOUserAgent foUserAgent) {
      super.setUserAgent(foUserAgent);
      this.userAgent.setRendererOverride(this);
      String s = (String)this.userAgent.getRendererOptions().get("transparent-page-background");
      if (s != null) {
         this.transparentPageBackground = "true".equalsIgnoreCase(s);
      }

   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public void setupFontInfo(FontInfo inFontInfo) {
      this.fontInfo = inFontInfo;
      Graphics2D graphics2D = Java2DFontMetrics.createFontMetricsGraphics2D();
      FontCollection[] fontCollections = new FontCollection[]{new Base14FontCollection(graphics2D), new InstalledFontCollection(graphics2D), new ConfiguredFontCollection(this.getFontResolver(), this.getFontList())};
      this.userAgent.getFactory().getFontManager().setup(this.getFontInfo(), fontCollections);
   }

   public Graphics2DAdapter getGraphics2DAdapter() {
      return new Java2DGraphics2DAdapter();
   }

   public void setScaleFactor(double newScaleFactor) {
      this.scaleFactor = newScaleFactor;
   }

   public double getScaleFactor() {
      return this.scaleFactor;
   }

   public void startRenderer(OutputStream out) throws IOException {
      super.startRenderer(out);
   }

   public void stopRenderer() throws IOException {
      log.debug("Java2DRenderer stopped");
      this.renderingDone = true;
      int numberOfPages = this.currentPageNumber;
      if (numberOfPages == 0) {
         new FOPException("No page could be rendered");
      }

   }

   public boolean isRenderingDone() {
      return this.renderingDone;
   }

   public int getCurrentPageNumber() {
      return this.currentPageNumber;
   }

   public void setCurrentPageNumber(int c) {
      this.currentPageNumber = c;
   }

   public int getNumberOfPages() {
      return this.pageViewportList.size();
   }

   public void clearViewportList() {
      this.pageViewportList.clear();
      this.setCurrentPageNumber(0);
   }

   public void renderPage(PageViewport pageViewport) throws IOException {
      this.rememberPage((PageViewport)pageViewport.clone());
      ++this.currentPageNumber;
   }

   protected void rememberPage(PageViewport pageViewport) {
      if (!$assertionsDisabled && pageViewport.getPageIndex() < 0) {
         throw new AssertionError();
      } else {
         this.pageViewportList.add(pageViewport);
      }
   }

   public BufferedImage getPageImage(PageViewport pageViewport) {
      this.currentPageViewport = pageViewport;

      BufferedImage var16;
      try {
         PageBoundaries boundaries = new PageBoundaries(pageViewport.getViewArea().getSize(), pageViewport.getForeignAttributes());
         Rectangle bounds = boundaries.getCropBox();
         Rectangle bleedBox = boundaries.getBleedBox();
         this.pageWidth = (int)Math.round(bounds.getWidth() / 1000.0);
         this.pageHeight = (int)Math.round(bounds.getHeight() / 1000.0);
         log.info("Rendering Page " + pageViewport.getPageNumberString() + " (pageWidth " + this.pageWidth + ", pageHeight " + this.pageHeight + ")");
         double scaleX = this.scaleFactor;
         double scaleY = this.scaleFactor;
         String scale = (String)this.currentPageViewport.getForeignAttributes().get(PageScale.EXT_PAGE_SCALE);
         Point2D scales = PageScale.getScale(scale);
         if (scales != null) {
            scaleX *= scales.getX();
            scaleY *= scales.getY();
         }

         scaleX = scaleX * 0.35277777910232544 / (double)this.userAgent.getTargetPixelUnitToMillimeter();
         scaleY = scaleY * 0.35277777910232544 / (double)this.userAgent.getTargetPixelUnitToMillimeter();
         int bitmapWidth = (int)((double)this.pageWidth * scaleX + 0.5);
         int bitmapHeight = (int)((double)this.pageHeight * scaleY + 0.5);
         BufferedImage currentPageImage = this.getBufferedImage(bitmapWidth, bitmapHeight);
         Graphics2D graphics = currentPageImage.createGraphics();
         graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
         if (this.antialiasing) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
         }

         if (this.qualityRendering) {
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
         }

         graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
         AffineTransform at = graphics.getTransform();
         at.scale(scaleX, scaleY);
         at.translate(bounds.getMinX() / -1000.0, bounds.getMinY() / -1000.0);
         graphics.setTransform(at);
         if (!this.transparentPageBackground) {
            graphics.setColor(Color.white);
            graphics.fillRect((int)Math.round(bleedBox.getMinX() / 1000.0), (int)Math.round(bleedBox.getMinY() / 1000.0), (int)Math.round(bleedBox.getWidth() / 1000.0), (int)Math.round(bleedBox.getHeight() / 1000.0));
         }

         this.state = new Java2DGraphicsState(graphics, this.fontInfo, at);

         try {
            this.currentBPPosition = 0;
            this.currentIPPosition = 0;
            this.renderPageAreas(pageViewport.getPage());
         } finally {
            this.state = null;
         }

         var16 = currentPageImage;
      } finally {
         this.currentPageViewport = null;
      }

      return var16;
   }

   protected BufferedImage getBufferedImage(int bitmapWidth, int bitmapHeight) {
      return new BufferedImage(bitmapWidth, bitmapHeight, 2);
   }

   public PageViewport getPageViewport(int pageIndex) throws FOPException {
      if (pageIndex >= 0 && pageIndex < this.pageViewportList.size()) {
         return (PageViewport)this.pageViewportList.get(pageIndex);
      } else {
         throw new FOPException("Requested page number is out of range: " + pageIndex + "; only " + this.pageViewportList.size() + " page(s) available.");
      }
   }

   public BufferedImage getPageImage(int pageNum) throws FOPException {
      return this.getPageImage(this.getPageViewport(pageNum));
   }

   protected void saveGraphicsState() {
      this.stateStack.push(this.state);
      this.state = new Java2DGraphicsState(this.state);
   }

   protected void restoreGraphicsState() {
      this.state.dispose();
      this.state = (Java2DGraphicsState)this.stateStack.pop();
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      this.state.transform(at);
   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.saveGraphicsState();
      if (clippingRect != null) {
         this.clipRect((float)clippingRect.getX() / 1000.0F, (float)clippingRect.getY() / 1000.0F, (float)clippingRect.getWidth() / 1000.0F, (float)clippingRect.getHeight() / 1000.0F);
      }

      this.state.transform(new AffineTransform(CTMHelper.toPDFArray(ctm)));
   }

   protected void endVParea() {
      this.restoreGraphicsState();
   }

   protected List breakOutOfStateStack() {
      log.debug("Block.FIXED --> break out");

      ArrayList breakOutList;
      for(breakOutList = new ArrayList(); !this.stateStack.isEmpty(); this.state = (Java2DGraphicsState)this.stateStack.pop()) {
         breakOutList.add(0, this.state);
      }

      return breakOutList;
   }

   protected void restoreStateStackAfterBreakOut(List breakOutList) {
      log.debug("Block.FIXED --> restoring context after break-out");

      Java2DGraphicsState s;
      for(Iterator it = breakOutList.iterator(); it.hasNext(); this.state = s) {
         s = (Java2DGraphicsState)it.next();
         this.stateStack.push(this.state);
      }

   }

   protected void updateColor(Color col, boolean fill) {
      this.state.updateColor(col);
   }

   protected void clip() {
      if (this.currentPath == null) {
         throw new IllegalStateException("No current path available!");
      } else {
         this.state.updateClip(this.currentPath);
         this.currentPath = null;
      }
   }

   protected void closePath() {
      this.currentPath.closePath();
   }

   protected void lineTo(float x, float y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.lineTo(x, y);
   }

   protected void moveTo(float x, float y) {
      if (this.currentPath == null) {
         this.currentPath = new GeneralPath();
      }

      this.currentPath.moveTo(x, y);
   }

   protected void clipRect(float x, float y, float width, float height) {
      this.state.updateClip(new Rectangle2D.Float(x, y, width, height));
   }

   protected void fillRect(float x, float y, float width, float height) {
      this.state.getGraph().fill(new Rectangle2D.Float(x, y, width, height));
   }

   protected void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      Graphics2D g2d = this.state.getGraph();
      float width = x2 - x1;
      float height = y2 - y1;
      drawBorderLine(new Rectangle2D.Float(x1, y1, width, height), horz, startOrBefore, style, col, g2d);
   }

   public static void drawBorderLine(Rectangle2D.Float lineRect, boolean horz, boolean startOrBefore, int style, Color col, Graphics2D g2d) {
      float x1 = lineRect.x;
      float y1 = lineRect.y;
      float x2 = x1 + lineRect.width;
      float y2 = y1 + lineRect.height;
      float w = lineRect.width;
      float h = lineRect.height;
      if (!(w < 0.0F) && !(h < 0.0F)) {
         float unit;
         float ym1;
         float ym;
         int rep;
         BasicStroke s;
         switch (style) {
            case 31:
               g2d.setColor(col);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  ym = y1 + h / 2.0F;
                  s = new BasicStroke(h, 0, 0, 10.0F, new float[]{unit}, 0.0F);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(x1, ym, x2, ym));
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  ym = x1 + w / 2.0F;
                  s = new BasicStroke(w, 0, 0, 10.0F, new float[]{unit}, 0.0F);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(ym, y1, ym, y2));
               }
               break;
            case 36:
               g2d.setColor(col);
               if (horz) {
                  unit = Math.abs(2.0F * h);
                  rep = (int)(w / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = w / (float)rep;
                  ym = y1 + h / 2.0F;
                  s = new BasicStroke(h, 1, 0, 10.0F, new float[]{0.0F, unit}, 0.0F);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(x1, ym, x2, ym));
               } else {
                  unit = Math.abs(2.0F * w);
                  rep = (int)(h / unit);
                  if (rep % 2 == 0) {
                     ++rep;
                  }

                  unit = h / (float)rep;
                  ym = x1 + w / 2.0F;
                  s = new BasicStroke(w, 1, 0, 10.0F, new float[]{0.0F, unit}, 0.0F);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(ym, y1, ym, y2));
               }
               break;
            case 37:
               g2d.setColor(col);
               if (horz) {
                  unit = h / 3.0F;
                  ym1 = y1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  s = new BasicStroke(unit);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(x1, ym1, x2, ym1));
                  g2d.draw(new Line2D.Float(x1, ym, x2, ym));
               } else {
                  unit = w / 3.0F;
                  ym1 = x1 + unit / 2.0F;
                  ym = ym1 + unit + unit;
                  s = new BasicStroke(unit);
                  g2d.setStroke(s);
                  g2d.draw(new Line2D.Float(ym1, y1, ym1, y2));
                  g2d.draw(new Line2D.Float(ym, y1, ym, y2));
               }
               break;
            case 55:
            case 119:
               unit = style == 55 ? 0.4F : -0.4F;
               Color lowercol;
               float h3;
               float ym1;
               Color uppercol;
               if (horz) {
                  uppercol = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = h / 3.0F;
                  ym1 = y1 + h3 / 2.0F;
                  g2d.setStroke(new BasicStroke(h3));
                  g2d.setColor(uppercol);
                  g2d.draw(new Line2D.Float(x1, ym1, x2, ym1));
                  g2d.setColor(col);
                  g2d.draw(new Line2D.Float(x1, ym1 + h3, x2, ym1 + h3));
                  g2d.setColor(lowercol);
                  g2d.draw(new Line2D.Float(x1, ym1 + h3 + h3, x2, ym1 + h3 + h3));
               } else {
                  uppercol = ColorUtil.lightenColor(col, -unit);
                  lowercol = ColorUtil.lightenColor(col, unit);
                  h3 = w / 3.0F;
                  ym1 = x1 + h3 / 2.0F;
                  g2d.setStroke(new BasicStroke(h3));
                  g2d.setColor(uppercol);
                  g2d.draw(new Line2D.Float(ym1, y1, ym1, y2));
                  g2d.setColor(col);
                  g2d.draw(new Line2D.Float(ym1 + h3, y1, ym1 + h3, y2));
                  g2d.setColor(lowercol);
                  g2d.draw(new Line2D.Float(ym1 + h3 + h3, y1, ym1 + h3 + h3, y2));
               }
            case 57:
               break;
            case 67:
            case 101:
               unit = style == 101 ? 0.4F : -0.4F;
               if (horz) {
                  col = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  g2d.setStroke(new BasicStroke(h));
                  ym1 = y1 + h / 2.0F;
                  g2d.setColor(col);
                  g2d.draw(new Line2D.Float(x1, ym1, x2, ym1));
               } else {
                  col = ColorUtil.lightenColor(col, (float)(startOrBefore ? 1 : -1) * unit);
                  ym1 = x1 + w / 2.0F;
                  g2d.setStroke(new BasicStroke(w));
                  g2d.setColor(col);
                  g2d.draw(new Line2D.Float(ym1, y1, ym1, y2));
               }
               break;
            default:
               g2d.setColor(col);
               if (horz) {
                  ym1 = y1 + h / 2.0F;
                  g2d.setStroke(new BasicStroke(h));
                  g2d.draw(new Line2D.Float(x1, ym1, x2, ym1));
               } else {
                  ym1 = x1 + w / 2.0F;
                  g2d.setStroke(new BasicStroke(w));
                  g2d.draw(new Line2D.Float(ym1, y1, ym1, y2));
               }
         }

      } else {
         log.error("Negative extent received. Border won't be painted.");
      }
   }

   public void renderText(TextArea text) {
      this.renderInlineAreaBackAndBorders(text);
      int rx = this.currentIPPosition + text.getBorderAndPaddingWidthStart();
      int bl = this.currentBPPosition + text.getOffset() + text.getBaselineOffset();
      int saveIP = this.currentIPPosition;
      Font font = this.getFontFromArea(text);
      this.state.updateFont(font.getFontName(), font.getFontSize());
      this.saveGraphicsState();
      AffineTransform at = new AffineTransform();
      at.translate((double)((float)rx / 1000.0F), (double)((float)bl / 1000.0F));
      this.state.transform(at);
      renderText(text, this.state.getGraph(), font);
      this.restoreGraphicsState();
      this.currentIPPosition = saveIP + text.getAllocIPD();
      Typeface tf = (Typeface)this.fontInfo.getFonts().get(font.getFontName());
      int fontsize = text.getTraitAsInteger(Trait.FONT_SIZE);
      this.renderTextDecoration(tf, fontsize, text, bl, rx);
   }

   public static void renderText(TextArea text, Graphics2D g2d, Font font) {
      Color col = (Color)text.getTrait(Trait.COLOR);
      g2d.setColor(col);
      float textCursor = 0.0F;
      Iterator iter = text.getChildAreas().iterator();

      while(true) {
         while(iter.hasNext()) {
            InlineArea child = (InlineArea)iter.next();
            String s;
            if (child instanceof WordArea) {
               WordArea word = (WordArea)child;
               s = word.getWord();
               int[] letterAdjust = word.getLetterAdjustArray();
               GlyphVector gv = g2d.getFont().createGlyphVector(g2d.getFontRenderContext(), s);
               double additionalWidth = 0.0;
               if (letterAdjust != null || text.getTextLetterSpaceAdjust() != 0 || text.getTextWordSpaceAdjust() != 0) {
                  int[] offsets = getGlyphOffsets(s, font, text, letterAdjust);
                  float cursor = 0.0F;

                  for(int i = 0; i < offsets.length; ++i) {
                     Point2D pt = gv.getGlyphPosition(i);
                     pt.setLocation((double)cursor, pt.getY());
                     gv.setGlyphPosition(i, pt);
                     cursor += (float)offsets[i] / 1000.0F;
                  }

                  additionalWidth = (double)cursor - gv.getLogicalBounds().getWidth();
               }

               g2d.drawGlyphVector(gv, textCursor, 0.0F);
               textCursor = (float)((double)textCursor + gv.getLogicalBounds().getWidth() + additionalWidth);
            } else {
               if (!(child instanceof SpaceArea)) {
                  throw new IllegalStateException("Unsupported child element: " + child);
               }

               SpaceArea space = (SpaceArea)child;
               s = space.getSpace();
               char sp = s.charAt(0);
               int tws = space.isAdjustable() ? text.getTextWordSpaceAdjust() + 2 * text.getTextLetterSpaceAdjust() : 0;
               textCursor += (float)(font.getCharWidth(sp) + tws) / 1000.0F;
            }
         }

         return;
      }
   }

   private static int[] getGlyphOffsets(String s, Font font, TextArea text, int[] letterAdjust) {
      int textLen = s.length();
      int[] offsets = new int[textLen];

      for(int i = 0; i < textLen; ++i) {
         char c = s.charAt(i);
         char mapped = font.mapChar(c);
         int wordSpace;
         if (CharUtilities.isAdjustableSpace(mapped)) {
            wordSpace = text.getTextWordSpaceAdjust();
         } else {
            wordSpace = 0;
         }

         int cw = font.getWidth(mapped);
         int ladj = letterAdjust != null && i < textLen - 1 ? letterAdjust[i + 1] : 0;
         int tls = i < textLen - 1 ? text.getTextLetterSpaceAdjust() : 0;
         offsets[i] = cw + ladj + tls + wordSpace;
      }

      return offsets;
   }

   public void renderLeader(Leader area) {
      this.renderInlineAreaBackAndBorders(area);
      float startx = (float)(this.currentIPPosition + area.getBorderAndPaddingWidthStart()) / 1000.0F;
      float starty = (float)(this.currentBPPosition + area.getOffset()) / 1000.0F;
      float endx = (float)(this.currentIPPosition + area.getBorderAndPaddingWidthStart() + area.getIPD()) / 1000.0F;
      Color col = (Color)area.getTrait(Trait.COLOR);
      this.state.updateColor(col);
      Line2D line = new Line2D.Float();
      line.setLine((double)startx, (double)starty, (double)endx, (double)starty);
      float ruleThickness = (float)area.getRuleThickness() / 1000.0F;
      int style = area.getRuleStyle();
      switch (style) {
         case 31:
         case 37:
         case 133:
            this.drawBorderLine(startx, starty, endx, starty + ruleThickness, true, true, style, col);
            break;
         case 36:
            this.state.updateStroke(ruleThickness, style);
            float rt2 = ruleThickness / 2.0F;
            line.setLine(line.getX1(), line.getY1() + (double)rt2, line.getX2(), line.getY2() + (double)rt2);
            this.state.getGraph().draw(line);
            break;
         case 55:
         case 119:
            float half = (float)area.getRuleThickness() / 2000.0F;
            this.state.updateColor(ColorUtil.lightenColor(col, 0.6F));
            this.moveTo(startx, starty);
            this.lineTo(endx, starty);
            this.lineTo(endx, starty + 2.0F * half);
            this.lineTo(startx, starty + 2.0F * half);
            this.closePath();
            this.state.getGraph().fill(this.currentPath);
            this.currentPath = null;
            this.state.updateColor(col);
            if (style == 55) {
               this.moveTo(startx, starty);
               this.lineTo(endx, starty);
               this.lineTo(endx, starty + half);
               this.lineTo(startx + half, starty + half);
               this.lineTo(startx, starty + 2.0F * half);
            } else {
               this.moveTo(endx, starty);
               this.lineTo(endx, starty + 2.0F * half);
               this.lineTo(startx, starty + 2.0F * half);
               this.lineTo(startx, starty + half);
               this.lineTo(endx - half, starty + half);
            }

            this.closePath();
            this.state.getGraph().fill(this.currentPath);
            this.currentPath = null;
         case 95:
      }

      super.renderLeader(area);
   }

   public void renderImage(Image image, Rectangle2D pos) {
      String url = image.getURL();
      this.drawImage(url, pos);
   }

   protected void drawImage(String uri, Rectangle2D pos, Map foreignAttributes) {
      int x = this.currentIPPosition + (int)Math.round(pos.getX());
      int y = this.currentBPPosition + (int)Math.round(pos.getY());
      uri = URISpecification.getURL(uri);
      ImageManager manager = this.getUserAgent().getFactory().getImageManager();
      ImageInfo info = null;

      ResourceEventProducer eventProducer;
      try {
         ImageSessionContext sessionContext = this.getUserAgent().getImageSessionContext();
         info = manager.getImageInfo(uri, sessionContext);
         Map hints = ImageUtil.getDefaultHints(sessionContext);
         org.apache.xmlgraphics.image.loader.Image img = manager.getImage(info, FLAVOURS, hints, sessionContext);
         if (img instanceof ImageGraphics2D) {
            ImageGraphics2D imageG2D = (ImageGraphics2D)img;
            int width = (int)pos.getWidth();
            int height = (int)pos.getHeight();
            RendererContext context = this.createRendererContext(x, y, width, height, foreignAttributes);
            this.getGraphics2DAdapter().paintImage(imageG2D.getGraphics2DImagePainter(), context, x, y, width, height);
         } else if (img instanceof ImageRendered) {
            ImageRendered imgRend = (ImageRendered)img;
            AffineTransform at = new AffineTransform();
            at.translate((double)((float)x / 1000.0F), (double)((float)y / 1000.0F));
            double sx = pos.getWidth() / (double)info.getSize().getWidthMpt();
            double sy = pos.getHeight() / (double)info.getSize().getHeightMpt();
            sx *= (double)this.userAgent.getSourceResolution() / info.getSize().getDpiHorizontal();
            sy *= (double)this.userAgent.getSourceResolution() / info.getSize().getDpiVertical();
            at.scale(sx, sy);
            this.state.getGraph().drawRenderedImage(imgRend.getRenderedImage(), at);
         } else if (img instanceof ImageXMLDOM) {
            ImageXMLDOM imgXML = (ImageXMLDOM)img;
            this.renderDocument(imgXML.getDocument(), imgXML.getRootNamespace(), pos, foreignAttributes);
         }
      } catch (ImageException var17) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, info != null ? info.toString() : uri, var17, (Locator)null);
      } catch (FileNotFoundException var18) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var18, (Locator)null);
      } catch (IOException var19) {
         eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageIOError(this, info != null ? info.toString() : uri, var19, (Locator)null);
      }

   }

   protected RendererContext createRendererContext(int x, int y, int width, int height, Map foreignAttributes) {
      RendererContext context = super.createRendererContext(x, y, width, height, foreignAttributes);
      context.setProperty("state", this.state);
      return context;
   }

   public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
      if (pageIndex >= this.getNumberOfPages()) {
         return 1;
      } else if (this.state != null) {
         throw new IllegalStateException("state must be null");
      } else {
         Graphics2D graphics = (Graphics2D)g;

         byte var6;
         try {
            PageViewport viewport = this.getPageViewport(pageIndex);
            AffineTransform at = graphics.getTransform();
            this.state = new Java2DGraphicsState(graphics, this.fontInfo, at);
            this.currentBPPosition = 0;
            this.currentIPPosition = 0;
            this.renderPageAreas(viewport.getPage());
            byte var7 = 0;
            return var7;
         } catch (FOPException var11) {
            log.error(var11);
            var6 = 1;
         } finally {
            this.state = null;
         }

         return var6;
      }
   }

   protected void beginTextObject() {
   }

   protected void endTextObject() {
   }

   public void setTransparentPageBackground(boolean transparentPageBackground) {
      this.transparentPageBackground = transparentPageBackground;
   }

   static {
      $assertionsDisabled = !Java2DRenderer.class.desiredAssertionStatus();
      FLAVOURS = new ImageFlavor[]{ImageFlavor.GRAPHICS2D, ImageFlavor.BUFFERED_IMAGE, ImageFlavor.RENDERED_IMAGE, ImageFlavor.XML_DOM};
   }
}
