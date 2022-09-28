package org.apache.batik.transcoder;

import java.awt.Toolkit;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.FloatKey;
import org.apache.batik.transcoder.keys.IntegerKey;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.XMLFilter;

public abstract class ToSVGAbstractTranscoder extends AbstractTranscoder implements SVGConstants {
   public static float PIXEL_TO_MILLIMETERS = 25.4F / (float)Toolkit.getDefaultToolkit().getScreenResolution();
   public static float PIXEL_PER_INCH = (float)Toolkit.getDefaultToolkit().getScreenResolution();
   public static final int TRANSCODER_ERROR_BASE = 65280;
   public static final int ERROR_NULL_INPUT = 65280;
   public static final int ERROR_INCOMPATIBLE_INPUT_TYPE = 65281;
   public static final int ERROR_INCOMPATIBLE_OUTPUT_TYPE = 65282;
   public static final TranscodingHints.Key KEY_WIDTH = new FloatKey();
   public static final TranscodingHints.Key KEY_HEIGHT = new FloatKey();
   public static final TranscodingHints.Key KEY_INPUT_WIDTH = new IntegerKey();
   public static final TranscodingHints.Key KEY_INPUT_HEIGHT = new IntegerKey();
   public static final TranscodingHints.Key KEY_XOFFSET = new IntegerKey();
   public static final TranscodingHints.Key KEY_YOFFSET = new IntegerKey();
   public static final TranscodingHints.Key KEY_ESCAPED = new BooleanKey();
   protected SVGGraphics2D svgGenerator;

   protected Document createDocument(TranscoderOutput var1) {
      Document var2;
      if (var1.getDocument() == null) {
         DOMImplementation var3 = SVGDOMImplementation.getDOMImplementation();
         var2 = var3.createDocument("http://www.w3.org/2000/svg", "svg", (DocumentType)null);
      } else {
         var2 = var1.getDocument();
      }

      return var2;
   }

   public SVGGraphics2D getGraphics2D() {
      return this.svgGenerator;
   }

   protected void writeSVGToOutput(SVGGraphics2D var1, Element var2, TranscoderOutput var3) throws TranscoderException {
      Document var4 = var3.getDocument();
      if (var4 == null) {
         XMLFilter var5 = var3.getXMLFilter();
         if (var5 != null) {
            this.handler.fatalError(new TranscoderException("65282"));
         }

         try {
            boolean var6 = false;
            if (this.hints.containsKey(KEY_ESCAPED)) {
               var6 = (Boolean)this.hints.get(KEY_ESCAPED);
            }

            OutputStream var7 = var3.getOutputStream();
            if (var7 != null) {
               var1.stream(var2, new OutputStreamWriter(var7), false, var6);
               return;
            }

            Writer var8 = var3.getWriter();
            if (var8 != null) {
               var1.stream(var2, var8, false, var6);
               return;
            }

            String var9 = var3.getURI();
            if (var9 != null) {
               try {
                  URL var10 = new URL(var9);
                  URLConnection var11 = var10.openConnection();
                  var7 = var11.getOutputStream();
                  var1.stream(var2, new OutputStreamWriter(var7), false, var6);
                  return;
               } catch (MalformedURLException var12) {
                  this.handler.fatalError(new TranscoderException(var12));
               } catch (IOException var13) {
                  this.handler.fatalError(new TranscoderException(var13));
               }
            }
         } catch (IOException var14) {
            throw new TranscoderException(var14);
         }

         throw new TranscoderException("65282");
      }
   }
}
