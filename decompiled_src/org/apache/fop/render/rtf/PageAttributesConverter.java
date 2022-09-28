package org.apache.fop.render.rtf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.expr.NumericOp;
import org.apache.fop.fo.pagination.RegionBA;
import org.apache.fop.fo.pagination.RegionBody;
import org.apache.fop.fo.pagination.SimplePageMaster;
import org.apache.fop.fo.properties.CommonMarginBlock;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;

final class PageAttributesConverter {
   private static Log log = new SimpleLog("FOP/RTF");

   private PageAttributesConverter() {
   }

   static RtfAttributes convertPageAttributes(SimplePageMaster pagemaster) {
      FOPRtfAttributes attrib = new FOPRtfAttributes();

      try {
         RegionBA before = (RegionBA)pagemaster.getRegion(57);
         RegionBody body = (RegionBody)pagemaster.getRegion(58);
         RegionBA after = (RegionBA)pagemaster.getRegion(56);
         attrib.setTwips("paperw", pagemaster.getPageWidth());
         attrib.setTwips("paperh", pagemaster.getPageHeight());
         attrib.set("itap", "0");
         Object widthRaw = attrib.getValue("paperw");
         Object heightRaw = attrib.getValue("paperh");
         if (widthRaw instanceof Integer && heightRaw instanceof Integer && (Integer)widthRaw > (Integer)heightRaw) {
            attrib.set("landscape");
         }

         Length pageTop = pagemaster.getCommonMarginBlock().marginTop;
         Length pageBottom = pagemaster.getCommonMarginBlock().marginBottom;
         Length pageLeft = pagemaster.getCommonMarginBlock().marginLeft;
         Length pageRight = pagemaster.getCommonMarginBlock().marginRight;
         Length bodyTop = pageTop;
         Length bodyBottom = pageBottom;
         Length bodyLeft = pageLeft;
         Length bodyRight = pageRight;
         if (body != null) {
            CommonMarginBlock bodyMargin = body.getCommonMarginBlock();
            bodyTop = (Length)NumericOp.addition(pageTop, bodyMargin.marginTop);
            bodyBottom = (Length)NumericOp.addition(pageBottom, bodyMargin.marginBottom);
            bodyLeft = (Length)NumericOp.addition(pageLeft, bodyMargin.marginLeft);
            bodyRight = (Length)NumericOp.addition(pageRight, bodyMargin.marginRight);
         }

         attrib.setTwips("margt", bodyTop);
         attrib.setTwips("margb", bodyBottom);
         attrib.setTwips("margl", bodyLeft);
         attrib.setTwips("margr", bodyRight);
         Length beforeTop = pageTop;
         if (before != null) {
            beforeTop = (Length)NumericOp.addition(pageTop, before.getExtent());
         }

         attrib.setTwips("headery", beforeTop);
         Length afterBottom = pageBottom;
         if (after != null) {
            afterBottom = (Length)NumericOp.addition(pageBottom, after.getExtent());
         }

         attrib.setTwips("footery", afterBottom);
      } catch (Exception var17) {
         log.error("Exception in convertPageAttributes: " + var17.getMessage() + "- page attributes ignored");
         attrib = new FOPRtfAttributes();
      }

      return attrib;
   }
}
