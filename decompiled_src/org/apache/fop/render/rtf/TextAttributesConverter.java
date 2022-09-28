package org.apache.fop.render.rtf;

import java.awt.Color;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.CommonFont;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.fo.properties.CommonTextDecoration;
import org.apache.fop.fo.properties.PercentLength;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfColorTable;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfFontManager;

final class TextAttributesConverter {
   private static Log log;

   private TextAttributesConverter() {
   }

   public static RtfAttributes convertAttributes(Block fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrFont(fobj.getCommonFont(), attrib);
      attrFontColor(fobj.getColor(), attrib);
      attrBlockBackgroundColor(fobj.getCommonBorderPaddingBackground(), attrib);
      attrBlockMargin(fobj.getCommonMarginBlock(), attrib);
      attrBlockTextAlign(fobj.getTextAlign(), attrib);
      attrBorder(fobj.getCommonBorderPaddingBackground(), attrib, fobj);
      return attrib;
   }

   public static RtfAttributes convertBlockContainerAttributes(BlockContainer fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrBackgroundColor(fobj.getCommonBorderPaddingBackground(), attrib);
      attrBlockMargin(fobj.getCommonMarginBlock(), attrib);
      attrBorder(fobj.getCommonBorderPaddingBackground(), attrib, fobj);
      return attrib;
   }

   public static RtfAttributes convertCharacterAttributes(FOText fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrFont(fobj.getCommonFont(), attrib);
      attrFontColor(fobj.getColor(), attrib);
      attrTextDecoration(fobj.getTextDecoration(), attrib);
      attrBaseLineShift(fobj.getBaseLineShift(), attrib);
      return attrib;
   }

   public static RtfAttributes convertCharacterAttributes(PageNumber fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrFont(fobj.getCommonFont(), attrib);
      attrTextDecoration(fobj.getTextDecoration(), attrib);
      attrBackgroundColor(fobj.getCommonBorderPaddingBackground(), attrib);
      return attrib;
   }

   public static RtfAttributes convertCharacterAttributes(Inline fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrFont(fobj.getCommonFont(), attrib);
      attrFontColor(fobj.getColor(), attrib);
      attrBackgroundColor(fobj.getCommonBorderPaddingBackground(), attrib);
      attrInlineBorder(fobj.getCommonBorderPaddingBackground(), attrib);
      return attrib;
   }

   public static RtfAttributes convertLeaderAttributes(Leader fobj, PercentBaseContext context) throws FOPException {
      boolean tab = false;
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrib.set("f", RtfFontManager.getInstance().getFontNumber(fobj.getCommonFont().getFirstFontFamily()));
      if (fobj.getLeaderLength() != null) {
         attrib.set("lwidth", convertMptToTwips(fobj.getLeaderLength().getMaximum(context).getLength().getValue(context)));
         if (fobj.getLeaderLength().getMaximum(context) instanceof PercentLength && ((PercentLength)fobj.getLeaderLength().getMaximum(context)).getString().equals("100.0%")) {
            attrib.set("tabuse", 1);
            tab = true;
         }
      }

      attrFontColor(fobj.getColor(), attrib);
      if (fobj.getLeaderPatternWidth() != null) {
      }

      switch (fobj.getLeaderPattern()) {
         case 35:
            if (tab) {
               attrib.set("tablead", "tldot");
            } else {
               attrib.set("tablead", "uld");
            }
            break;
         case 123:
            if (fobj.getRuleThickness() != null) {
               log.warn("RTF: fo:leader rule-thickness not supported");
            }

            switch (fobj.getRuleStyle()) {
               case 31:
                  if (tab) {
                     attrib.set("tablead", "tlmdot");
                  } else {
                     attrib.set("tablead", "uldash");
                  }
                  break;
               case 36:
                  if (tab) {
                     attrib.set("tablead", "tldot");
                  } else {
                     attrib.set("tablead", "uld");
                  }
                  break;
               case 37:
                  if (tab) {
                     attrib.set("tablead", "tleq");
                  } else {
                     attrib.set("tablead", "uldb");
                  }
                  break;
               case 55:
                  if (tab) {
                     attrib.set("tablead", "tlhyph");
                  } else {
                     attrib.set("tablead", "ulhwave");
                  }
                  break;
               case 119:
                  if (tab) {
                     attrib.set("tablead", "tlul");
                  } else {
                     attrib.set("tablead", "ulthdashdd");
                  }
                  break;
               case 133:
                  if (tab) {
                     attrib.set("tablead", "tlth");
                  } else {
                     attrib.set("tablead", "ulth");
                  }
            }
         case 134:
         default:
            break;
         case 158:
            log.warn("RTF: fo:leader use-content not supported");
      }

      if (fobj.getLeaderAlignment() == 109) {
         log.warn("RTF: fo:leader reference-area not supported");
      }

      return attrib;
   }

   private static int convertMptToTwips(int mpt) {
      return Math.round(FoUnitsConverter.getInstance().convertMptToTwips(mpt));
   }

   private static void attrFont(CommonFont font, FOPRtfAttributes rtfAttr) {
      rtfAttr.set("f", RtfFontManager.getInstance().getFontNumber(font.getFirstFontFamily()));
      rtfAttr.setHalfPoints("fs", font.fontSize);
      if (font.getFontWeight() != 175 && font.getFontWeight() != 176 && font.getFontWeight() != 177) {
         rtfAttr.set("b", 0);
      } else {
         rtfAttr.set("b", 1);
      }

      if (font.getFontStyle() == 164) {
         rtfAttr.set("i", 1);
      } else {
         rtfAttr.set("i", 0);
      }

   }

   private static void attrFontColor(Color colorType, RtfAttributes rtfAttr) {
      if (colorType != null && (colorType.getAlpha() != 0 || colorType.getRed() != 0 || colorType.getGreen() != 0 || colorType.getBlue() != 0)) {
         rtfAttr.set("cf", convertFOPColorToRTF(colorType));
      }

   }

   private static void attrTextDecoration(CommonTextDecoration textDecoration, RtfAttributes rtfAttr) {
      if (textDecoration == null) {
         rtfAttr.set("ul", 0);
         rtfAttr.set("strike", 0);
      } else {
         if (textDecoration.hasUnderline()) {
            rtfAttr.set("ul", 1);
         } else {
            rtfAttr.set("ul", 0);
         }

         if (textDecoration.hasLineThrough()) {
            rtfAttr.set("strike", 1);
         } else {
            rtfAttr.set("strike", 0);
         }

      }
   }

   private static void attrBlockMargin(CommonMarginBlock cmb, FOPRtfAttributes rtfAttr) {
      rtfAttr.setTwips("sb", cmb.spaceBefore.getOptimum((PercentBaseContext)null).getLength());
      rtfAttr.setTwips("sa", cmb.spaceAfter.getOptimum((PercentBaseContext)null).getLength());
      rtfAttr.setTwips("li", cmb.startIndent);
      rtfAttr.setTwips("ri", cmb.endIndent);
   }

   private static void attrBlockTextAlign(int alignment, RtfAttributes rtfAttr) {
      String rtfValue = null;
      switch (alignment) {
         case 23:
            rtfValue = "qc";
            break;
         case 39:
            rtfValue = "qr";
            break;
         case 70:
            rtfValue = "qj";
            break;
         default:
            rtfValue = "ql";
      }

      rtfAttr.set(rtfValue);
   }

   private static void attrBlockBackgroundColor(CommonBorderPaddingBackground bpb, RtfAttributes rtfAttr) {
      if (bpb.hasBackground()) {
         rtfAttr.set("shading", 10000);
         rtfAttr.set("cfpat", convertFOPColorToRTF(bpb.backgroundColor));
      }

   }

   private static void attrBorder(CommonBorderPaddingBackground bpb, RtfAttributes rtfAttr, FONode fobj) {
      if (hasBorder(fobj.getParent())) {
         attrInlineBorder(bpb, rtfAttr);
      } else {
         BorderAttributesConverter.makeBorder(bpb, 0, rtfAttr, "brdrt");
         BorderAttributesConverter.makeBorder(bpb, 1, rtfAttr, "brdrb");
         BorderAttributesConverter.makeBorder(bpb, 2, rtfAttr, "brdrl");
         BorderAttributesConverter.makeBorder(bpb, 3, rtfAttr, "brdrr");
      }
   }

   private static boolean hasBorder(FONode node) {
      while(node != null) {
         CommonBorderPaddingBackground commonBorderPaddingBackground = null;
         if (node instanceof Block) {
            Block block = (Block)node;
            commonBorderPaddingBackground = block.getCommonBorderPaddingBackground();
         } else if (node instanceof BlockContainer) {
            BlockContainer container = (BlockContainer)node;
            commonBorderPaddingBackground = container.getCommonBorderPaddingBackground();
         }

         if (commonBorderPaddingBackground != null && commonBorderPaddingBackground.hasBorder()) {
            return true;
         }

         node = node.getParent();
      }

      return false;
   }

   private static void attrInlineBorder(CommonBorderPaddingBackground bpb, RtfAttributes rtfAttr) {
      BorderAttributesConverter.makeBorder(bpb, 0, rtfAttr, "chbrdr");
   }

   private static void attrBackgroundColor(CommonBorderPaddingBackground bpb, RtfAttributes rtfAttr) {
      Color fopValue = bpb.backgroundColor;
      int rtfColor = false;
      if (fopValue != null && (fopValue.getRed() != 0 || fopValue.getGreen() != 0 || fopValue.getBlue() != 0 || fopValue.getAlpha() != 0)) {
         int rtfColor = convertFOPColorToRTF(fopValue);
         rtfAttr.set("chcbpat", rtfColor);
      }
   }

   private static void attrBaseLineShift(Length baselineShift, RtfAttributes rtfAttr) {
      int s = baselineShift.getEnum();
      if (s == 138) {
         rtfAttr.set("super");
      } else if (s == 137) {
         rtfAttr.set("sub");
      }

   }

   public static int convertFOPColorToRTF(Color fopColor) {
      int redComponent = fopColor.getRed();
      int greenComponent = fopColor.getGreen();
      int blueComponent = fopColor.getBlue();
      return RtfColorTable.getInstance().getColorNumber(redComponent, greenComponent, blueComponent);
   }

   static {
      log = LogFactory.getLog(TextAttributesConverter.class);
   }
}
