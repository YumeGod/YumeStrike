package org.apache.fop.pdf;

import org.apache.fop.fonts.FontType;

public class PDFFontTrueType extends PDFFontNonBase14 {
   public PDFFontTrueType(String fontname, String basefont, Object encoding) {
      super(fontname, FontType.TRUETYPE, basefont, encoding);
   }
}
