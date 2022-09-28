package org.apache.fop.fonts;

public interface FontEventListener {
   void fontSubstituted(Object var1, FontTriplet var2, FontTriplet var3);

   void fontLoadingErrorAtAutoDetection(Object var1, String var2, Exception var3);

   void glyphNotAvailable(Object var1, char var2, String var3);
}
