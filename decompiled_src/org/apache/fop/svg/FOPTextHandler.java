package org.apache.fop.svg;

import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.xmlgraphics.java2d.TextHandler;

public interface FOPTextHandler extends TextHandler {
   void setOverrideFont(Font var1);

   FontInfo getFontInfo();
}
