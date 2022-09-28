package org.apache.fop.render;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import org.apache.batik.parser.AWTTransformProducer;
import org.apache.fop.area.Area;
import org.apache.fop.area.Block;
import org.apache.fop.area.BlockViewport;
import org.apache.fop.area.CTM;
import org.apache.fop.area.NormalFlow;
import org.apache.fop.area.RegionReference;
import org.apache.fop.area.RegionViewport;
import org.apache.fop.area.Trait;
import org.apache.fop.area.inline.ForeignObject;
import org.apache.fop.area.inline.InlineArea;
import org.apache.fop.area.inline.Viewport;
import org.apache.fop.fonts.FontMetrics;
import org.apache.fop.traits.BorderProps;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.util.QName;
import org.apache.xmlgraphics.util.UnitConv;
import org.w3c.dom.Document;

public abstract class AbstractPathOrientedRenderer extends PrintRenderer {
   private static final int BEFORE = 0;
   private static final int END = 1;
   private static final int AFTER = 2;
   private static final int START = 3;
   protected static final QName FOX_TRANSFORM = new QName("http://xmlgraphics.apache.org/fop/extensions", "fox:transform");

   protected void handleBlockTraits(Block block) {
      int borderPaddingStart = block.getBorderAndPaddingWidthStart();
      int borderPaddingBefore = block.getBorderAndPaddingWidthBefore();
      float startx = (float)this.currentIPPosition / 1000.0F;
      float starty = (float)this.currentBPPosition / 1000.0F;
      float width = (float)block.getIPD() / 1000.0F;
      float height = (float)block.getBPD() / 1000.0F;
      startx += (float)block.getStartIndent() / 1000.0F;
      startx -= (float)block.getBorderAndPaddingWidthStart() / 1000.0F;
      width += (float)borderPaddingStart / 1000.0F;
      width += (float)block.getBorderAndPaddingWidthEnd() / 1000.0F;
      height += (float)borderPaddingBefore / 1000.0F;
      height += (float)block.getBorderAndPaddingWidthAfter() / 1000.0F;
      this.drawBackAndBorders(block, startx, starty, width, height);
   }

   protected void handleRegionTraits(RegionViewport region) {
      Rectangle2D viewArea = region.getViewArea();
      RegionReference referenceArea = region.getRegionReference();
      float startx = (float)(viewArea.getX() / 1000.0);
      float starty = (float)(viewArea.getY() / 1000.0);
      float width = (float)(viewArea.getWidth() / 1000.0);
      float height = (float)(viewArea.getHeight() / 1000.0);
      this.currentBPPosition = referenceArea.getBorderAndPaddingWidthBefore();
      this.currentIPPosition = referenceArea.getBorderAndPaddingWidthStart();
      this.drawBackAndBorders(region, referenceArea, startx, starty, width, height);
   }

   protected void drawBackAndBorders(Area area, float startx, float starty, float width, float height) {
      this.drawBackAndBorders(area, area, startx, starty, width, height);
   }

   protected void drawBackAndBorders(Area backgroundArea, Area borderArea, float startx, float starty, float width, float height) {
      BorderProps bpsBefore = (BorderProps)borderArea.getTrait(Trait.BORDER_BEFORE);
      BorderProps bpsAfter = (BorderProps)borderArea.getTrait(Trait.BORDER_AFTER);
      BorderProps bpsStart = (BorderProps)borderArea.getTrait(Trait.BORDER_START);
      BorderProps bpsEnd = (BorderProps)borderArea.getTrait(Trait.BORDER_END);
      this.drawBackground(startx, starty, width, height, (Trait.Background)backgroundArea.getTrait(Trait.BACKGROUND), bpsBefore, bpsAfter, bpsStart, bpsEnd);
      this.drawBorders(startx, starty, width, height, bpsBefore, bpsAfter, bpsStart, bpsEnd);
   }

   protected void drawBackground(float startx, float starty, float width, float height, Trait.Background back, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      if (back != null) {
         this.endTextObject();
         float sx = startx;
         float sy = starty;
         float paddRectWidth = width;
         float paddRectHeight = height;
         if (bpsStart != null) {
            sx = startx + (float)bpsStart.width / 1000.0F;
            paddRectWidth = width - (float)bpsStart.width / 1000.0F;
         }

         if (bpsBefore != null) {
            sy = starty + (float)bpsBefore.width / 1000.0F;
            paddRectHeight = height - (float)bpsBefore.width / 1000.0F;
         }

         if (bpsEnd != null) {
            paddRectWidth -= (float)bpsEnd.width / 1000.0F;
         }

         if (bpsAfter != null) {
            paddRectHeight -= (float)bpsAfter.width / 1000.0F;
         }

         if (back.getColor() != null) {
            this.updateColor(back.getColor(), true);
            this.fillRect(sx, sy, paddRectWidth, paddRectHeight);
         }

         if (back.getImageInfo() != null) {
            ImageSize imageSize = back.getImageInfo().getSize();
            this.saveGraphicsState();
            this.clipRect(sx, sy, paddRectWidth, paddRectHeight);
            int horzCount = (int)(paddRectWidth * 1000.0F / (float)imageSize.getWidthMpt() + 1.0F);
            int vertCount = (int)(paddRectHeight * 1000.0F / (float)imageSize.getHeightMpt() + 1.0F);
            if (back.getRepeat() == 96) {
               horzCount = 1;
               vertCount = 1;
            } else if (back.getRepeat() == 113) {
               vertCount = 1;
            } else if (back.getRepeat() == 114) {
               horzCount = 1;
            }

            sx *= 1000.0F;
            sy *= 1000.0F;
            if (horzCount == 1) {
               sx += (float)back.getHoriz();
            }

            if (vertCount == 1) {
               sy += (float)back.getVertical();
            }

            for(int x = 0; x < horzCount; ++x) {
               for(int y = 0; y < vertCount; ++y) {
                  Rectangle2D pos = new Rectangle2D.Float(sx - (float)this.currentIPPosition + (float)(x * imageSize.getWidthMpt()), sy - (float)this.currentBPPosition + (float)(y * imageSize.getHeightMpt()), (float)imageSize.getWidthMpt(), (float)imageSize.getHeightMpt());
                  this.drawImage(back.getURL(), pos);
               }
            }

            this.restoreGraphicsState();
         }
      }

   }

   protected void drawBorders(float startx, float starty, float width, float height, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      Rectangle2D.Float borderRect = new Rectangle2D.Float(startx, starty, width, height);
      this.drawBorders(borderRect, bpsBefore, bpsAfter, bpsStart, bpsEnd);
   }

   protected void drawBorders(Rectangle2D.Float borderRect, BorderProps bpsBefore, BorderProps bpsAfter, BorderProps bpsStart, BorderProps bpsEnd) {
      boolean[] border = new boolean[]{bpsBefore != null, bpsEnd != null, bpsAfter != null, bpsStart != null};
      float startx = borderRect.x;
      float starty = borderRect.y;
      float width = borderRect.width;
      float height = borderRect.height;
      float[] borderWidth = new float[]{border[0] ? (float)bpsBefore.width / 1000.0F : 0.0F, border[1] ? (float)bpsEnd.width / 1000.0F : 0.0F, border[2] ? (float)bpsAfter.width / 1000.0F : 0.0F, border[3] ? (float)bpsStart.width / 1000.0F : 0.0F};
      float[] clipw = new float[]{(float)BorderProps.getClippedWidth(bpsBefore) / 1000.0F, (float)BorderProps.getClippedWidth(bpsEnd) / 1000.0F, (float)BorderProps.getClippedWidth(bpsAfter) / 1000.0F, (float)BorderProps.getClippedWidth(bpsStart) / 1000.0F};
      starty += clipw[0];
      height -= clipw[0];
      height -= clipw[2];
      startx += clipw[3];
      width -= clipw[3];
      width -= clipw[1];
      boolean[] slant = new boolean[]{border[3] && border[0], border[0] && border[1], border[1] && border[2], border[2] && border[3]};
      float sy2;
      float ey1;
      float ey2;
      float outerx;
      float clipx;
      float innerx;
      float sy1a;
      float ey1a;
      if (bpsBefore != null) {
         this.endTextObject();
         sy2 = slant[0] ? startx + borderWidth[3] - clipw[3] : startx;
         ey1 = startx + width;
         ey2 = slant[1] ? ey1 - borderWidth[1] + clipw[1] : ey1;
         outerx = starty - clipw[0];
         clipx = outerx + clipw[0];
         innerx = outerx + borderWidth[0];
         this.saveGraphicsState();
         this.moveTo(startx, clipx);
         sy1a = startx;
         ey1a = ey1;
         if (bpsBefore.mode == 2) {
            if (bpsStart != null && bpsStart.mode == 2) {
               sy1a = startx - clipw[3];
            }

            if (bpsEnd != null && bpsEnd.mode == 2) {
               ey1a = ey1 + clipw[1];
            }

            this.lineTo(sy1a, outerx);
            this.lineTo(ey1a, outerx);
         }

         this.lineTo(ey1, clipx);
         this.lineTo(ey2, innerx);
         this.lineTo(sy2, innerx);
         this.closePath();
         this.clip();
         this.drawBorderLine(sy1a, outerx, ey1a, innerx, true, true, bpsBefore.style, bpsBefore.color);
         this.restoreGraphicsState();
      }

      if (bpsEnd != null) {
         this.endTextObject();
         sy2 = slant[1] ? starty + borderWidth[0] - clipw[0] : starty;
         ey1 = starty + height;
         ey2 = slant[2] ? ey1 - borderWidth[2] + clipw[2] : ey1;
         outerx = startx + width + clipw[1];
         clipx = outerx - clipw[1];
         innerx = outerx - borderWidth[1];
         this.saveGraphicsState();
         this.moveTo(clipx, starty);
         sy1a = starty;
         ey1a = ey1;
         if (bpsEnd.mode == 2) {
            if (bpsBefore != null && bpsBefore.mode == 2) {
               sy1a = starty - clipw[0];
            }

            if (bpsAfter != null && bpsAfter.mode == 2) {
               ey1a = ey1 + clipw[2];
            }

            this.lineTo(outerx, sy1a);
            this.lineTo(outerx, ey1a);
         }

         this.lineTo(clipx, ey1);
         this.lineTo(innerx, ey2);
         this.lineTo(innerx, sy2);
         this.closePath();
         this.clip();
         this.drawBorderLine(innerx, sy1a, outerx, ey1a, false, false, bpsEnd.style, bpsEnd.color);
         this.restoreGraphicsState();
      }

      if (bpsAfter != null) {
         this.endTextObject();
         sy2 = slant[3] ? startx + borderWidth[3] - clipw[3] : startx;
         ey1 = startx + width;
         ey2 = slant[2] ? ey1 - borderWidth[1] + clipw[1] : ey1;
         outerx = starty + height + clipw[2];
         clipx = outerx - clipw[2];
         innerx = outerx - borderWidth[2];
         this.saveGraphicsState();
         this.moveTo(ey1, clipx);
         sy1a = startx;
         ey1a = ey1;
         if (bpsAfter.mode == 2) {
            if (bpsStart != null && bpsStart.mode == 2) {
               sy1a = startx - clipw[3];
            }

            if (bpsEnd != null && bpsEnd.mode == 2) {
               ey1a = ey1 + clipw[1];
            }

            this.lineTo(ey1a, outerx);
            this.lineTo(sy1a, outerx);
         }

         this.lineTo(startx, clipx);
         this.lineTo(sy2, innerx);
         this.lineTo(ey2, innerx);
         this.closePath();
         this.clip();
         this.drawBorderLine(sy1a, innerx, ey1a, outerx, true, false, bpsAfter.style, bpsAfter.color);
         this.restoreGraphicsState();
      }

      if (bpsStart != null) {
         this.endTextObject();
         sy2 = slant[0] ? starty + borderWidth[0] - clipw[0] : starty;
         ey1 = starty + height;
         ey2 = slant[3] ? ey1 - borderWidth[2] + clipw[2] : ey1;
         outerx = startx - clipw[3];
         clipx = outerx + clipw[3];
         innerx = outerx + borderWidth[3];
         this.saveGraphicsState();
         this.moveTo(clipx, ey1);
         sy1a = starty;
         ey1a = ey1;
         if (bpsStart.mode == 2) {
            if (bpsBefore != null && bpsBefore.mode == 2) {
               sy1a = starty - clipw[0];
            }

            if (bpsAfter != null && bpsAfter.mode == 2) {
               ey1a = ey1 + clipw[2];
            }

            this.lineTo(outerx, ey1a);
            this.lineTo(outerx, sy1a);
         }

         this.lineTo(clipx, starty);
         this.lineTo(innerx, sy2);
         this.lineTo(innerx, ey2);
         this.closePath();
         this.clip();
         this.drawBorderLine(outerx, sy1a, innerx, ey1a, false, true, bpsStart.style, bpsStart.color);
         this.restoreGraphicsState();
      }

   }

   protected void renderInlineAreaBackAndBorders(InlineArea area) {
      float borderPaddingStart = (float)area.getBorderAndPaddingWidthStart() / 1000.0F;
      float borderPaddingBefore = (float)area.getBorderAndPaddingWidthBefore() / 1000.0F;
      float bpwidth = borderPaddingStart + (float)area.getBorderAndPaddingWidthEnd() / 1000.0F;
      float bpheight = borderPaddingBefore + (float)area.getBorderAndPaddingWidthAfter() / 1000.0F;
      float height = (float)area.getBPD() / 1000.0F;
      if (height != 0.0F || bpheight != 0.0F && bpwidth != 0.0F) {
         float x = (float)this.currentIPPosition / 1000.0F;
         float y = (float)(this.currentBPPosition + area.getOffset()) / 1000.0F;
         float width = (float)area.getIPD() / 1000.0F;
         this.drawBackAndBorders(area, x, y - borderPaddingBefore, width + bpwidth, height + bpheight);
      }

   }

   protected void renderBlockViewport(BlockViewport bv, List children) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      CTM ctm = bv.getCTM();
      int borderPaddingBefore = bv.getBorderAndPaddingWidthBefore();
      int positioning = bv.getPositioning();
      if (positioning != 2 && positioning != 3) {
         this.currentBPPosition += bv.getSpaceBefore();
         this.handleBlockTraits(bv);
         this.currentIPPosition += bv.getStartIndent();
         CTM tempctm = new CTM((double)this.containingIPPosition, (double)this.currentBPPosition);
         ctm = tempctm.multiply(ctm);
         this.currentBPPosition += borderPaddingBefore;
         Rectangle2D clippingRect = null;
         if (bv.getClip()) {
            clippingRect = new Rectangle(this.currentIPPosition, this.currentBPPosition, bv.getIPD(), bv.getBPD());
         }

         this.startVParea(ctm, clippingRect);
         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.renderBlocks(bv, children);
         this.endVParea();
         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
         this.currentBPPosition += bv.getAllocBPD();
      } else {
         List breakOutList = null;
         if (positioning == 3) {
            breakOutList = this.breakOutOfStateStack();
         }

         AffineTransform positionTransform = new AffineTransform();
         positionTransform.translate((double)bv.getXOffset(), (double)bv.getYOffset());
         int borderPaddingStart = bv.getBorderAndPaddingWidthStart();
         positionTransform.translate((double)(-borderPaddingStart), (double)(-borderPaddingBefore));
         String transf = bv.getForeignAttributeValue(FOX_TRANSFORM);
         if (transf != null) {
            AffineTransform freeTransform = AWTTransformProducer.createAffineTransform(transf);
            positionTransform.concatenate(freeTransform);
         }

         if (!positionTransform.isIdentity()) {
            this.establishTransformationMatrix(positionTransform);
         }

         float width = (float)bv.getIPD() / 1000.0F;
         float height = (float)bv.getBPD() / 1000.0F;
         float borderPaddingWidth = (float)(borderPaddingStart + bv.getBorderAndPaddingWidthEnd()) / 1000.0F;
         float borderPaddingHeight = (float)(borderPaddingBefore + bv.getBorderAndPaddingWidthAfter()) / 1000.0F;
         this.drawBackAndBorders(bv, 0.0F, 0.0F, width + borderPaddingWidth, height + borderPaddingHeight);
         AffineTransform contentRectTransform = new AffineTransform();
         contentRectTransform.translate((double)borderPaddingStart, (double)borderPaddingBefore);
         if (!contentRectTransform.isIdentity()) {
            this.establishTransformationMatrix(contentRectTransform);
         }

         if (bv.getClip()) {
            this.clipRect(0.0F, 0.0F, width, height);
         }

         AffineTransform contentTransform = ctm.toAffineTransform();
         if (!contentTransform.isIdentity()) {
            this.establishTransformationMatrix(contentTransform);
         }

         this.currentIPPosition = 0;
         this.currentBPPosition = 0;
         this.renderBlocks(bv, children);
         if (!contentTransform.isIdentity()) {
            this.restoreGraphicsState();
         }

         if (!contentRectTransform.isIdentity()) {
            this.restoreGraphicsState();
         }

         if (!positionTransform.isIdentity()) {
            this.restoreGraphicsState();
         }

         if (positioning == 3 && breakOutList != null) {
            this.restoreStateStackAfterBreakOut(breakOutList);
         }

         this.currentIPPosition = saveIP;
         this.currentBPPosition = saveBP;
      }

   }

   protected void renderReferenceArea(Block block) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      AffineTransform at = new AffineTransform();
      at.translate((double)this.currentIPPosition, (double)this.currentBPPosition);
      at.translate((double)block.getXOffset(), (double)block.getYOffset());
      at.translate(0.0, (double)block.getSpaceBefore());
      if (!at.isIdentity()) {
         this.establishTransformationMatrix(at);
      }

      this.currentIPPosition = 0;
      this.currentBPPosition = 0;
      this.handleBlockTraits(block);
      List children = block.getChildAreas();
      if (children != null) {
         this.renderBlocks(block, children);
      }

      if (!at.isIdentity()) {
         this.restoreGraphicsState();
      }

      this.currentIPPosition = saveIP;
      this.currentBPPosition = saveBP;
   }

   protected void renderFlow(NormalFlow flow) {
      int saveIP = this.currentIPPosition;
      int saveBP = this.currentBPPosition;
      AffineTransform at = new AffineTransform();
      at.translate((double)this.currentIPPosition, (double)this.currentBPPosition);
      if (!at.isIdentity()) {
         this.establishTransformationMatrix(at);
      }

      this.currentIPPosition = 0;
      this.currentBPPosition = 0;
      super.renderFlow(flow);
      if (!at.isIdentity()) {
         this.restoreGraphicsState();
      }

      this.currentIPPosition = saveIP;
      this.currentBPPosition = saveBP;
   }

   protected abstract void concatenateTransformationMatrix(AffineTransform var1);

   public void renderViewport(Viewport viewport) {
      float x = (float)this.currentIPPosition / 1000.0F;
      float y = (float)(this.currentBPPosition + viewport.getOffset()) / 1000.0F;
      float width = (float)viewport.getIPD() / 1000.0F;
      float height = (float)viewport.getBPD() / 1000.0F;
      float borderPaddingStart = (float)viewport.getBorderAndPaddingWidthStart() / 1000.0F;
      float borderPaddingBefore = (float)viewport.getBorderAndPaddingWidthBefore() / 1000.0F;
      float bpwidth = borderPaddingStart + (float)viewport.getBorderAndPaddingWidthEnd() / 1000.0F;
      float bpheight = borderPaddingBefore + (float)viewport.getBorderAndPaddingWidthAfter() / 1000.0F;
      this.drawBackAndBorders(viewport, x, y, width + bpwidth, height + bpheight);
      if (viewport.getClip()) {
         this.saveGraphicsState();
         this.clipRect(x + borderPaddingStart, y + borderPaddingBefore, width, height);
      }

      super.renderViewport(viewport);
      if (viewport.getClip()) {
         this.restoreGraphicsState();
      }

   }

   protected abstract void restoreStateStackAfterBreakOut(List var1);

   protected abstract List breakOutOfStateStack();

   protected abstract void saveGraphicsState();

   protected abstract void restoreGraphicsState();

   protected abstract void beginTextObject();

   protected abstract void endTextObject();

   protected void renderTextDecoration(FontMetrics fm, int fontsize, InlineArea inline, int baseline, int startx) {
      boolean hasTextDeco = inline.hasUnderline() || inline.hasOverline() || inline.hasLineThrough();
      if (hasTextDeco) {
         this.endTextObject();
         float descender = (float)fm.getDescender(fontsize) / 1000.0F;
         float capHeight = (float)fm.getCapHeight(fontsize) / 1000.0F;
         float halfLineWidth = descender / -8.0F / 2.0F;
         float endx = (float)(startx + inline.getIPD()) / 1000.0F;
         Color ct;
         float y;
         if (inline.hasUnderline()) {
            ct = (Color)inline.getTrait(Trait.UNDERLINE_COLOR);
            y = (float)baseline - descender / 2.0F;
            this.drawBorderLine((float)startx / 1000.0F, (y - halfLineWidth) / 1000.0F, endx, (y + halfLineWidth) / 1000.0F, true, true, 133, ct);
         }

         if (inline.hasOverline()) {
            ct = (Color)inline.getTrait(Trait.OVERLINE_COLOR);
            y = (float)((double)baseline - 1.1 * (double)capHeight);
            this.drawBorderLine((float)startx / 1000.0F, (y - halfLineWidth) / 1000.0F, endx, (y + halfLineWidth) / 1000.0F, true, true, 133, ct);
         }

         if (inline.hasLineThrough()) {
            ct = (Color)inline.getTrait(Trait.LINETHROUGH_COLOR);
            y = (float)((double)baseline - 0.45 * (double)capHeight);
            this.drawBorderLine((float)startx / 1000.0F, (y - halfLineWidth) / 1000.0F, endx, (y + halfLineWidth) / 1000.0F, true, true, 133, ct);
         }
      }

   }

   protected abstract void clip();

   protected abstract void clipRect(float var1, float var2, float var3, float var4);

   protected abstract void moveTo(float var1, float var2);

   protected abstract void lineTo(float var1, float var2);

   protected abstract void closePath();

   protected abstract void fillRect(float var1, float var2, float var3, float var4);

   protected abstract void updateColor(Color var1, boolean var2);

   protected abstract void drawImage(String var1, Rectangle2D var2, Map var3);

   protected final void drawImage(String url, Rectangle2D pos) {
      this.drawImage(url, pos, (Map)null);
   }

   protected abstract void drawBorderLine(float var1, float var2, float var3, float var4, boolean var5, boolean var6, int var7, Color var8);

   public void renderForeignObject(ForeignObject fo, Rectangle2D pos) {
      this.endTextObject();
      Document doc = fo.getDocument();
      String ns = fo.getNameSpace();
      this.renderDocument(doc, ns, pos, fo.getForeignAttributes());
   }

   protected void establishTransformationMatrix(AffineTransform at) {
      this.saveGraphicsState();
      this.concatenateTransformationMatrix(UnitConv.mptToPt(at));
   }
}
