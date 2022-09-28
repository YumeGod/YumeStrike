package org.apache.fop.fonts.autodetect;

public class MacFontDirFinder extends NativeFontDirFinder {
   protected String[] getSearchableDirectories() {
      return new String[]{System.getProperty("user.home") + "/Library/Fonts/", "/Library/Fonts/", "/System/Library/Fonts/", "/Network/Library/Fonts/"};
   }
}
