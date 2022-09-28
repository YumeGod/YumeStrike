package org.apache.fop.render.intermediate;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.FontInfo;

public interface IFDocumentHandlerConfigurator {
   void configure(IFDocumentHandler var1) throws FOPException;

   void setupFontInfo(IFDocumentHandler var1, FontInfo var2) throws FOPException;
}
