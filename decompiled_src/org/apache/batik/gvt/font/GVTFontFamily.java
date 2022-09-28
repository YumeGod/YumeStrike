package org.apache.batik.gvt.font;

import java.text.AttributedCharacterIterator;
import java.util.Map;

public interface GVTFontFamily {
   String getFamilyName();

   GVTFontFace getFontFace();

   GVTFont deriveFont(float var1, AttributedCharacterIterator var2);

   GVTFont deriveFont(float var1, Map var2);
}
