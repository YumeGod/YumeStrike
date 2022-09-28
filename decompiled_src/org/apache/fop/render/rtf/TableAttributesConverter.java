package org.apache.fop.render.rtf;

import java.awt.Color;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableHeader;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;

public final class TableAttributesConverter {
   private TableAttributesConverter() {
   }

   static RtfAttributes convertTableAttributes(Table fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      attrib.setTwips("trleft", fobj.getCommonMarginBlock().marginLeft);
      return attrib;
   }

   static RtfAttributes convertTablePartAttributes(TablePart part) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      return attrib;
   }

   static RtfAttributes convertCellAttributes(TableCell fobj) throws FOPException {
      FOPRtfAttributes attrib = new FOPRtfAttributes();
      CommonBorderPaddingBackground border = fobj.getCommonBorderPaddingBackground();
      Color color = border.backgroundColor;
      if (color == null) {
         CommonBorderPaddingBackground brd = null;
         if (fobj.getParent() instanceof TableRow) {
            TableRow parentRow = (TableRow)fobj.getParent();
            brd = parentRow.getCommonBorderPaddingBackground();
            color = brd.backgroundColor;
         } else if (fobj.getParent() instanceof TableHeader) {
            TableHeader parentHeader = (TableHeader)fobj.getParent();
            brd = parentHeader.getCommonBorderPaddingBackground();
            color = brd.backgroundColor;
         }

         if (color == null && fobj.getParent() != null && fobj.getParent().getParent() != null && fobj.getParent().getParent().getParent() instanceof Table) {
            Table table = (Table)fobj.getParent().getParent().getParent();
            brd = table.getCommonBorderPaddingBackground();
            color = brd.backgroundColor;
         }
      }

      if (color != null && (color.getAlpha() != 0 || color.getRed() != 0 || color.getGreen() != 0 || color.getBlue() != 0)) {
         attrib.set("clcbpat", color);
      }

      BorderAttributesConverter.makeBorder(border, 0, attrib, "clbrdrt");
      BorderAttributesConverter.makeBorder(border, 1, attrib, "clbrdrb");
      BorderAttributesConverter.makeBorder(border, 2, attrib, "clbrdrl");
      BorderAttributesConverter.makeBorder(border, 3, attrib, "clbrdrr");
      boolean reproduceMSWordBug = true;
      int padding;
      if (reproduceMSWordBug) {
         padding = border.getPaddingStart(false, (PercentBaseContext)null);
         if (padding != 0) {
            attrib.setTwips("clpadt", padding);
            attrib.set("clpadft", 3);
         }

         padding = border.getPaddingBefore(false, (PercentBaseContext)null);
         if (padding != 0) {
            attrib.setTwips("clpadl", padding);
            attrib.set("clpadfl", 3);
         }
      } else {
         padding = border.getPaddingStart(false, (PercentBaseContext)null);
         if (padding != 0) {
            attrib.setTwips("clpadl", padding);
            attrib.set("clpadfl", 3);
         }

         padding = border.getPaddingBefore(false, (PercentBaseContext)null);
         if (padding != 0) {
            attrib.setTwips("clpadt", padding);
            attrib.set("clpadft", 3);
         }
      }

      padding = border.getPaddingEnd(false, (PercentBaseContext)null);
      if (padding != 0) {
         attrib.setTwips("clpadr", padding);
         attrib.set("clpadfr", 3);
      }

      padding = border.getPaddingAfter(false, (PercentBaseContext)null);
      if (padding != 0) {
         attrib.setTwips("clpadb", padding);
         attrib.set("clpadfb", 3);
      }

      int n = fobj.getNumberColumnsSpanned();
      if (n > 1) {
         attrib.set("number-columns-spanned", n);
      }

      switch (fobj.getDisplayAlign()) {
         case 3:
            attrib.set("clvertalb");
            break;
         case 13:
            attrib.set("clvertalt");
            break;
         case 23:
            attrib.set("clvertalc");
      }

      return attrib;
   }

   static RtfAttributes convertRowAttributes(TableRow fobj, RtfAttributes rtfatts) throws FOPException {
      RtfAttributes attrib = null;
      if (rtfatts == null) {
         attrib = new RtfAttributes();
      } else {
         attrib = rtfatts;
      }

      if (fobj.getKeepTogether().getWithinPage().getEnum() == 7) {
         attrib.set("trkeep");
      }

      if (fobj.getKeepWithNext().getWithinPage().getEnum() == 7) {
         attrib.set("knext");
      }

      if (fobj.getKeepWithPrevious().getWithinPage().getEnum() == 7) {
         attrib.set("kprevious");
      }

      if (fobj.getHeight().getEnum() != 9) {
         attrib.set("trrh", fobj.getHeight().getValue() / 50);
      }

      CommonBorderPaddingBackground border = fobj.getCommonBorderPaddingBackground();
      BorderAttributesConverter.makeBorder(border, 0, attrib, "clbrdrt");
      BorderAttributesConverter.makeBorder(border, 1, attrib, "clbrdrb");
      BorderAttributesConverter.makeBorder(border, 2, attrib, "clbrdrl");
      BorderAttributesConverter.makeBorder(border, 3, attrib, "clbrdrr");
      return attrib;
   }
}
