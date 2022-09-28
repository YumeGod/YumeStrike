package org.apache.fop.render;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.area.LineArea;
import org.apache.fop.area.OffDocumentItem;
import org.apache.fop.area.PageSequence;
import org.apache.fop.area.PageViewport;
import org.apache.fop.fonts.FontInfo;

public interface Renderer {
   String ROLE = Renderer.class.getName();

   String getMimeType();

   void startRenderer(OutputStream var1) throws IOException;

   void stopRenderer() throws IOException;

   void setUserAgent(FOUserAgent var1);

   FOUserAgent getUserAgent();

   void setupFontInfo(FontInfo var1) throws FOPException;

   boolean supportsOutOfOrder();

   void processOffDocumentItem(OffDocumentItem var1);

   Graphics2DAdapter getGraphics2DAdapter();

   ImageAdapter getImageAdapter();

   void preparePage(PageViewport var1);

   /** @deprecated */
   void startPageSequence(LineArea var1);

   void startPageSequence(PageSequence var1);

   void renderPage(PageViewport var1) throws IOException, FOPException;
}
