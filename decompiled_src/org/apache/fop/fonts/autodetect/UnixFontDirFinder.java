package org.apache.fop.fonts.autodetect;

public class UnixFontDirFinder extends NativeFontDirFinder {
   protected String[] getSearchableDirectories() {
      return new String[]{System.getProperty("user.home") + "/.fonts", "/usr/local/fonts", "/usr/local/share/fonts", "/usr/share/fonts", "/usr/X11R6/lib/X11/fonts"};
   }
}
