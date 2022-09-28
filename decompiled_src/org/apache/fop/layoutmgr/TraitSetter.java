package org.apache.fop.layoutmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.area.Area;
import org.apache.fop.area.Trait;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.datatypes.SimplePercentBaseContext;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.CommonTextDecoration;
import org.apache.fop.fonts.Font;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.MinOptMax;

public class TraitSetter {
   protected static Log log;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public static void setBorderPaddingTraits(Area area, CommonBorderPaddingBackground bpProps, boolean bNotFirst, boolean bNotLast, PercentBaseContext context) {
      int iBP = bpProps.getPadding(2, bNotFirst, context);
      if (iBP > 0) {
         area.addTrait(Trait.PADDING_START, new Integer(iBP));
      }

      iBP = bpProps.getPadding(3, bNotLast, context);
      if (iBP > 0) {
         area.addTrait(Trait.PADDING_END, new Integer(iBP));
      }

      iBP = bpProps.getPadding(0, false, context);
      if (iBP > 0) {
         area.addTrait(Trait.PADDING_BEFORE, new Integer(iBP));
      }

      iBP = bpProps.getPadding(1, false, context);
      if (iBP > 0) {
         area.addTrait(Trait.PADDING_AFTER, new Integer(iBP));
      }

      addBorderTrait(area, bpProps, bNotFirst, 2, 0, Trait.BORDER_START);
      addBorderTrait(area, bpProps, bNotLast, 3, 0, Trait.BORDER_END);
      addBorderTrait(area, bpProps, false, 0, 0, Trait.BORDER_BEFORE);
      addBorderTrait(area, bpProps, false, 1, 0, Trait.BORDER_AFTER);
   }

   private static void addBorderTrait(Area area, CommonBorderPaddingBackground bpProps, boolean bDiscard, int iSide, int mode, Object oTrait) {
      int iBP = bpProps.getBorderWidth(iSide, bDiscard);
      if (iBP > 0) {
         area.addTrait(oTrait, new BorderProps(bpProps.getBorderStyle(iSide), iBP, bpProps.getBorderColor(iSide), mode));
      }

   }

   /** @deprecated */
   public static void addBorders(Area area, CommonBorderPaddingBackground bordProps, PercentBaseContext context) {
      BorderProps bps = getBorderProps(bordProps, 0);
      if (bps != null) {
         area.addTrait(Trait.BORDER_BEFORE, bps);
      }

      bps = getBorderProps(bordProps, 1);
      if (bps != null) {
         area.addTrait(Trait.BORDER_AFTER, bps);
      }

      bps = getBorderProps(bordProps, 2);
      if (bps != null) {
         area.addTrait(Trait.BORDER_START, bps);
      }

      bps = getBorderProps(bordProps, 3);
      if (bps != null) {
         area.addTrait(Trait.BORDER_END, bps);
      }

      addPadding(area, bordProps, context);
   }

   public static void addBorders(Area area, CommonBorderPaddingBackground bordProps, boolean discardBefore, boolean discardAfter, boolean discardStart, boolean discardEnd, PercentBaseContext context) {
      BorderProps bps = getBorderProps(bordProps, 0);
      if (bps != null && !discardBefore) {
         area.addTrait(Trait.BORDER_BEFORE, bps);
      }

      bps = getBorderProps(bordProps, 1);
      if (bps != null && !discardAfter) {
         area.addTrait(Trait.BORDER_AFTER, bps);
      }

      bps = getBorderProps(bordProps, 2);
      if (bps != null && !discardStart) {
         area.addTrait(Trait.BORDER_START, bps);
      }

      bps = getBorderProps(bordProps, 3);
      if (bps != null && !discardEnd) {
         area.addTrait(Trait.BORDER_END, bps);
      }

   }

   public static void addCollapsingBorders(Area area, CommonBorderPaddingBackground.BorderInfo borderBefore, CommonBorderPaddingBackground.BorderInfo borderAfter, CommonBorderPaddingBackground.BorderInfo borderStart, CommonBorderPaddingBackground.BorderInfo borderEnd, boolean[] outer) {
      BorderProps bps = getCollapsingBorderProps(borderBefore, outer[0]);
      if (bps != null) {
         area.addTrait(Trait.BORDER_BEFORE, bps);
      }

      bps = getCollapsingBorderProps(borderAfter, outer[1]);
      if (bps != null) {
         area.addTrait(Trait.BORDER_AFTER, bps);
      }

      bps = getCollapsingBorderProps(borderStart, outer[2]);
      if (bps != null) {
         area.addTrait(Trait.BORDER_START, bps);
      }

      bps = getCollapsingBorderProps(borderEnd, outer[3]);
      if (bps != null) {
         area.addTrait(Trait.BORDER_END, bps);
      }

   }

   private static void addPadding(Area area, CommonBorderPaddingBackground bordProps, PercentBaseContext context) {
      addPadding(area, bordProps, false, false, false, false, context);
   }

   public static void addPadding(Area area, CommonBorderPaddingBackground bordProps, boolean discardBefore, boolean discardAfter, boolean discardStart, boolean discardEnd, PercentBaseContext context) {
      int padding = bordProps.getPadding(0, discardBefore, context);
      if (padding != 0) {
         area.addTrait(Trait.PADDING_BEFORE, new Integer(padding));
      }

      padding = bordProps.getPadding(1, discardAfter, context);
      if (padding != 0) {
         area.addTrait(Trait.PADDING_AFTER, new Integer(padding));
      }

      padding = bordProps.getPadding(2, discardStart, context);
      if (padding != 0) {
         area.addTrait(Trait.PADDING_START, new Integer(padding));
      }

      padding = bordProps.getPadding(3, discardEnd, context);
      if (padding != 0) {
         area.addTrait(Trait.PADDING_END, new Integer(padding));
      }

   }

   private static BorderProps getBorderProps(CommonBorderPaddingBackground bordProps, int side) {
      int width = bordProps.getBorderWidth(side, false);
      if (width != 0) {
         BorderProps bps = new BorderProps(bordProps.getBorderStyle(side), width, bordProps.getBorderColor(side), 0);
         return bps;
      } else {
         return null;
      }
   }

   private static BorderProps getCollapsingBorderProps(CommonBorderPaddingBackground.BorderInfo borderInfo, boolean outer) {
      if (!$assertionsDisabled && borderInfo == null) {
         throw new AssertionError();
      } else {
         int width = borderInfo.getRetainedWidth();
         if (width != 0) {
            BorderProps bps = new BorderProps(borderInfo.getStyle(), width, borderInfo.getColor(), outer ? 2 : 1);
            return bps;
         } else {
            return null;
         }
      }
   }

   public static void addBackground(Area area, CommonBorderPaddingBackground backProps, PercentBaseContext context, int ipdShift, int bpdShift, int referenceIPD, int referenceBPD) {
      if (backProps.hasBackground()) {
         Trait.Background back = new Trait.Background();
         back.setColor(backProps.backgroundColor);
         if (backProps.getImageInfo() != null) {
            back.setURL(backProps.backgroundImage);
            back.setImageInfo(backProps.getImageInfo());
            back.setRepeat(backProps.backgroundRepeat);
            SimplePercentBaseContext refContext;
            if (backProps.backgroundPositionHorizontal != null && (back.getRepeat() == 96 || back.getRepeat() == 114)) {
               if (area.getIPD() > 0) {
                  refContext = new SimplePercentBaseContext(context, 9, referenceIPD - back.getImageInfo().getSize().getWidthMpt());
                  back.setHoriz(ipdShift + backProps.backgroundPositionHorizontal.getValue(refContext));
               } else {
                  log.warn("Horizontal background image positioning ignored because the IPD was not set on the area. (Yes, it's a bug in FOP)");
               }
            }

            if (backProps.backgroundPositionVertical != null && (back.getRepeat() == 96 || back.getRepeat() == 113)) {
               if (area.getBPD() > 0) {
                  refContext = new SimplePercentBaseContext(context, 10, referenceBPD - back.getImageInfo().getSize().getHeightMpt());
                  back.setVertical(bpdShift + backProps.backgroundPositionVertical.getValue(refContext));
               } else {
                  log.warn("Vertical background image positioning ignored because the BPD was not set on the area. (Yes, it's a bug in FOP)");
               }
            }
         }

         area.addTrait(Trait.BACKGROUND, back);
      }
   }

   public static void addBackground(Area area, CommonBorderPaddingBackground backProps, PercentBaseContext context) {
      if (backProps.hasBackground()) {
         Trait.Background back = new Trait.Background();
         back.setColor(backProps.backgroundColor);
         if (backProps.getImageInfo() != null) {
            back.setURL(backProps.backgroundImage);
            back.setImageInfo(backProps.getImageInfo());
            back.setRepeat(backProps.backgroundRepeat);
            int height;
            int imageHeightMpt;
            int lengthBaseValue;
            SimplePercentBaseContext simplePercentBaseContext;
            int vertical;
            if (backProps.backgroundPositionHorizontal != null && (back.getRepeat() == 96 || back.getRepeat() == 114)) {
               if (area.getIPD() > 0) {
                  height = area.getIPD();
                  height += backProps.getPaddingStart(false, context);
                  height += backProps.getPaddingEnd(false, context);
                  imageHeightMpt = back.getImageInfo().getSize().getWidthMpt();
                  lengthBaseValue = height - imageHeightMpt;
                  simplePercentBaseContext = new SimplePercentBaseContext(context, 9, lengthBaseValue);
                  vertical = backProps.backgroundPositionHorizontal.getValue(simplePercentBaseContext);
                  back.setHoriz(vertical);
               } else {
                  log.warn("Horizontal background image positioning ignored because the IPD was not set on the area. (Yes, it's a bug in FOP)");
               }
            }

            if (backProps.backgroundPositionVertical != null && (back.getRepeat() == 96 || back.getRepeat() == 113)) {
               if (area.getBPD() > 0) {
                  height = area.getBPD();
                  height += backProps.getPaddingBefore(false, context);
                  height += backProps.getPaddingAfter(false, context);
                  imageHeightMpt = back.getImageInfo().getSize().getHeightMpt();
                  lengthBaseValue = height - imageHeightMpt;
                  simplePercentBaseContext = new SimplePercentBaseContext(context, 10, lengthBaseValue);
                  vertical = backProps.backgroundPositionVertical.getValue(simplePercentBaseContext);
                  back.setVertical(vertical);
               } else {
                  log.warn("Vertical background image positioning ignored because the BPD was not set on the area. (Yes, it's a bug in FOP)");
               }
            }
         }

         area.addTrait(Trait.BACKGROUND, back);
      }
   }

   public static void addMargins(Area area, CommonBorderPaddingBackground bpProps, int startIndent, int endIndent, PercentBaseContext context) {
      if (startIndent != 0) {
         area.addTrait(Trait.START_INDENT, new Integer(startIndent));
      }

      int spaceStart = startIndent - bpProps.getBorderStartWidth(false) - bpProps.getPaddingStart(false, context);
      if (spaceStart != 0) {
         area.addTrait(Trait.SPACE_START, new Integer(spaceStart));
      }

      if (endIndent != 0) {
         area.addTrait(Trait.END_INDENT, new Integer(endIndent));
      }

      int spaceEnd = endIndent - bpProps.getBorderEndWidth(false) - bpProps.getPaddingEnd(false, context);
      if (spaceEnd != 0) {
         area.addTrait(Trait.SPACE_END, new Integer(spaceEnd));
      }

   }

   public static void addMargins(Area area, CommonBorderPaddingBackground bpProps, CommonMarginBlock marginProps, PercentBaseContext context) {
      int startIndent = marginProps.startIndent.getValue(context);
      int endIndent = marginProps.endIndent.getValue(context);
      addMargins(area, bpProps, startIndent, endIndent, context);
   }

   public static int getEffectiveSpace(double adjust, MinOptMax space) {
      if (space == null) {
         return 0;
      } else {
         int spaceOpt = space.getOpt();
         if (adjust > 0.0) {
            spaceOpt += (int)(adjust * (double)space.getStretch());
         } else {
            spaceOpt += (int)(adjust * (double)space.getShrink());
         }

         return spaceOpt;
      }
   }

   public static void addSpaceBeforeAfter(Area area, double adjust, MinOptMax spaceBefore, MinOptMax spaceAfter) {
      addSpaceTrait(area, Trait.SPACE_BEFORE, spaceBefore, adjust);
      addSpaceTrait(area, Trait.SPACE_AFTER, spaceAfter, adjust);
   }

   private static void addSpaceTrait(Area area, Integer spaceTrait, MinOptMax space, double adjust) {
      int effectiveSpace = getEffectiveSpace(adjust, space);
      if (effectiveSpace != 0) {
         area.addTrait(spaceTrait, new Integer(effectiveSpace));
      }

   }

   public static void addBreaks(Area area, int breakBefore, int breakAfter) {
   }

   public static void addFontTraits(Area area, Font font) {
      area.addTrait(Trait.FONT, font.getFontTriplet());
      area.addTrait(Trait.FONT_SIZE, new Integer(font.getFontSize()));
   }

   public static void addTextDecoration(Area area, CommonTextDecoration deco) {
      if (deco != null) {
         if (deco.hasUnderline()) {
            area.addTrait(Trait.UNDERLINE, Boolean.TRUE);
            area.addTrait(Trait.UNDERLINE_COLOR, deco.getUnderlineColor());
         }

         if (deco.hasOverline()) {
            area.addTrait(Trait.OVERLINE, Boolean.TRUE);
            area.addTrait(Trait.OVERLINE_COLOR, deco.getOverlineColor());
         }

         if (deco.hasLineThrough()) {
            area.addTrait(Trait.LINETHROUGH, Boolean.TRUE);
            area.addTrait(Trait.LINETHROUGH_COLOR, deco.getLineThroughColor());
         }

         if (deco.isBlinking()) {
            area.addTrait(Trait.BLINK, Boolean.TRUE);
         }
      }

   }

   public static void addPtr(Area area, String ptr) {
      if (ptr != null && ptr.length() > 0) {
         area.addTrait(Trait.PTR, ptr);
      }

   }

   public static void setProducerID(Area area, String id) {
      if (id != null && id.length() > 0) {
         area.addTrait(Trait.PROD_ID, id);
      }

   }

   static {
      $assertionsDisabled = !TraitSetter.class.desiredAssertionStatus();
      log = LogFactory.getLog(TraitSetter.class);
   }
}
