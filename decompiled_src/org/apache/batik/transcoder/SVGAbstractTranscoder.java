package org.apache.batik.transcoder;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.batik.bridge.BaseScriptingEnvironment;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.DefaultScriptSecurity;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.NoLoadScriptSecurity;
import org.apache.batik.bridge.RelaxedScriptSecurity;
import org.apache.batik.bridge.SVGUtilities;
import org.apache.batik.bridge.ScriptSecurity;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.bridge.ViewBox;
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.dom.svg.SVGOMDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.DocumentFactory;
import org.apache.batik.gvt.CanvasGraphicsNode;
import org.apache.batik.gvt.CompositeGraphicsNode;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.transcoder.keys.BooleanKey;
import org.apache.batik.transcoder.keys.FloatKey;
import org.apache.batik.transcoder.keys.LengthKey;
import org.apache.batik.transcoder.keys.Rectangle2DKey;
import org.apache.batik.transcoder.keys.StringKey;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGSVGElement;

public abstract class SVGAbstractTranscoder extends XMLAbstractTranscoder {
   public static final String DEFAULT_DEFAULT_FONT_FAMILY = "Arial, Helvetica, sans-serif";
   protected Rectangle2D curAOI;
   protected AffineTransform curTxf;
   protected GraphicsNode root;
   protected BridgeContext ctx;
   protected GVTBuilder builder;
   protected float width = 400.0F;
   protected float height = 400.0F;
   protected UserAgent userAgent = this.createUserAgent();
   public static final TranscodingHints.Key KEY_WIDTH = new LengthKey();
   public static final TranscodingHints.Key KEY_HEIGHT = new LengthKey();
   public static final TranscodingHints.Key KEY_MAX_WIDTH = new LengthKey();
   public static final TranscodingHints.Key KEY_MAX_HEIGHT = new LengthKey();
   public static final TranscodingHints.Key KEY_AOI = new Rectangle2DKey();
   public static final TranscodingHints.Key KEY_LANGUAGE = new StringKey();
   public static final TranscodingHints.Key KEY_MEDIA = new StringKey();
   public static final TranscodingHints.Key KEY_DEFAULT_FONT_FAMILY = new StringKey();
   public static final TranscodingHints.Key KEY_ALTERNATE_STYLESHEET = new StringKey();
   public static final TranscodingHints.Key KEY_USER_STYLESHEET_URI = new StringKey();
   public static final TranscodingHints.Key KEY_PIXEL_UNIT_TO_MILLIMETER = new FloatKey();
   /** @deprecated */
   public static final TranscodingHints.Key KEY_PIXEL_TO_MM;
   public static final TranscodingHints.Key KEY_EXECUTE_ONLOAD;
   public static final TranscodingHints.Key KEY_SNAPSHOT_TIME;
   public static final TranscodingHints.Key KEY_ALLOWED_SCRIPT_TYPES;
   public static final String DEFAULT_ALLOWED_SCRIPT_TYPES = "text/ecmascript, application/ecmascript, text/javascript, application/javascript, application/java-archive";
   public static final TranscodingHints.Key KEY_CONSTRAIN_SCRIPT_ORIGIN;

   protected SVGAbstractTranscoder() {
      this.hints.put(KEY_DOCUMENT_ELEMENT_NAMESPACE_URI, "http://www.w3.org/2000/svg");
      this.hints.put(KEY_DOCUMENT_ELEMENT, "svg");
      this.hints.put(KEY_DOM_IMPLEMENTATION, SVGDOMImplementation.getDOMImplementation());
      this.hints.put(KEY_MEDIA, "screen");
      this.hints.put(KEY_DEFAULT_FONT_FAMILY, "Arial, Helvetica, sans-serif");
      this.hints.put(KEY_EXECUTE_ONLOAD, Boolean.FALSE);
      this.hints.put(KEY_ALLOWED_SCRIPT_TYPES, "text/ecmascript, application/ecmascript, text/javascript, application/javascript, application/java-archive");
   }

   protected UserAgent createUserAgent() {
      return new SVGAbstractTranscoderUserAgent();
   }

   protected DocumentFactory createDocumentFactory(DOMImplementation var1, String var2) {
      return new SAXSVGDocumentFactory(var2);
   }

   public void transcode(TranscoderInput var1, TranscoderOutput var2) throws TranscoderException {
      super.transcode(var1, var2);
      if (this.ctx != null) {
         this.ctx.dispose();
      }

   }

   protected void transcode(Document var1, String var2, TranscoderOutput var3) throws TranscoderException {
      if (var1 != null && !(var1.getImplementation() instanceof SVGDOMImplementation)) {
         DOMImplementation var4 = (DOMImplementation)this.hints.get(KEY_DOM_IMPLEMENTATION);
         var1 = DOMUtilities.deepCloneDocument(var1, var4);
         if (var2 != null) {
            ParsedURL var5 = new ParsedURL(var2);
            ((SVGOMDocument)var1).setParsedURL(var5);
         }
      }

      if (this.hints.containsKey(KEY_WIDTH)) {
         this.width = (Float)this.hints.get(KEY_WIDTH);
      }

      if (this.hints.containsKey(KEY_HEIGHT)) {
         this.height = (Float)this.hints.get(KEY_HEIGHT);
      }

      SVGOMDocument var23 = (SVGOMDocument)var1;
      SVGSVGElement var24 = var23.getRootElement();
      this.ctx = this.createBridgeContext(var23);
      this.builder = new GVTBuilder();
      boolean var6 = this.hints.containsKey(KEY_EXECUTE_ONLOAD) && (Boolean)this.hints.get(KEY_EXECUTE_ONLOAD);

      GraphicsNode var7;
      float var9;
      try {
         if (var6) {
            this.ctx.setDynamicState(2);
         }

         var7 = this.builder.build(this.ctx, (Document)var23);
         if (this.ctx.isDynamic()) {
            BaseScriptingEnvironment var8 = new BaseScriptingEnvironment(this.ctx);
            var8.loadScripts();
            var8.dispatchSVGLoadEvent();
            if (this.hints.containsKey(KEY_SNAPSHOT_TIME)) {
               var9 = (Float)this.hints.get(KEY_SNAPSHOT_TIME);
               this.ctx.getAnimationEngine().setCurrentTime(var9);
            } else if (this.ctx.isSVG12()) {
               var9 = SVGUtilities.convertSnapshotTime(var24, (BridgeContext)null);
               this.ctx.getAnimationEngine().setCurrentTime(var9);
            }
         }
      } catch (BridgeException var22) {
         var22.printStackTrace();
         throw new TranscoderException(var22);
      }

      float var25 = (float)this.ctx.getDocumentSize().getWidth();
      var9 = (float)this.ctx.getDocumentSize().getHeight();
      this.setImageSize(var25, var9);
      AffineTransform var10;
      if (this.hints.containsKey(KEY_AOI)) {
         Rectangle2D var11 = (Rectangle2D)this.hints.get(KEY_AOI);
         var10 = new AffineTransform();
         double var12 = (double)this.width / var11.getWidth();
         double var14 = (double)this.height / var11.getHeight();
         double var16 = Math.min(var12, var14);
         var10.scale(var16, var16);
         double var18 = -var11.getX() + ((double)this.width / var16 - var11.getWidth()) / 2.0;
         double var20 = -var11.getY() + ((double)this.height / var16 - var11.getHeight()) / 2.0;
         var10.translate(var18, var20);
         this.curAOI = var11;
      } else {
         String var26 = (new ParsedURL(var2)).getRef();
         String var28 = var24.getAttributeNS((String)null, "viewBox");
         if (var26 != null && var26.length() != 0) {
            var10 = ViewBox.getViewTransform(var26, var24, this.width, this.height, this.ctx);
         } else if (var28 != null && var28.length() != 0) {
            String var29 = var24.getAttributeNS((String)null, "preserveAspectRatio");
            var10 = ViewBox.getPreserveAspectRatioTransform(var24, (String)var28, (String)var29, this.width, this.height, this.ctx);
         } else {
            float var13 = this.width / var25;
            float var30 = this.height / var9;
            float var15 = Math.min(var13, var30);
            var10 = AffineTransform.getScaleInstance((double)var15, (double)var15);
         }

         this.curAOI = new Rectangle2D.Float(0.0F, 0.0F, this.width, this.height);
      }

      CanvasGraphicsNode var27 = this.getCanvasGraphicsNode(var7);
      if (var27 != null) {
         var27.setViewingTransform(var10);
         this.curTxf = new AffineTransform();
      } else {
         this.curTxf = var10;
      }

      this.root = var7;
   }

   protected CanvasGraphicsNode getCanvasGraphicsNode(GraphicsNode var1) {
      if (!(var1 instanceof CompositeGraphicsNode)) {
         return null;
      } else {
         CompositeGraphicsNode var2 = (CompositeGraphicsNode)var1;
         List var3 = var2.getChildren();
         if (var3.size() == 0) {
            return null;
         } else {
            var1 = (GraphicsNode)var3.get(0);
            return !(var1 instanceof CanvasGraphicsNode) ? null : (CanvasGraphicsNode)var1;
         }
      }
   }

   protected BridgeContext createBridgeContext(SVGOMDocument var1) {
      return this.createBridgeContext(var1.isSVG12() ? "1.2" : "1.x");
   }

   protected BridgeContext createBridgeContext() {
      return this.createBridgeContext("1.x");
   }

   protected BridgeContext createBridgeContext(String var1) {
      return (BridgeContext)("1.2".equals(var1) ? new SVG12BridgeContext(this.userAgent) : new BridgeContext(this.userAgent));
   }

   protected void setImageSize(float var1, float var2) {
      float var3 = -1.0F;
      if (this.hints.containsKey(KEY_WIDTH)) {
         var3 = (Float)this.hints.get(KEY_WIDTH);
      }

      float var4 = -1.0F;
      if (this.hints.containsKey(KEY_HEIGHT)) {
         var4 = (Float)this.hints.get(KEY_HEIGHT);
      }

      if (var3 > 0.0F && var4 > 0.0F) {
         this.width = var3;
         this.height = var4;
      } else if (var4 > 0.0F) {
         this.width = var1 * var4 / var2;
         this.height = var4;
      } else if (var3 > 0.0F) {
         this.width = var3;
         this.height = var2 * var3 / var1;
      } else {
         this.width = var1;
         this.height = var2;
      }

      float var5 = -1.0F;
      if (this.hints.containsKey(KEY_MAX_WIDTH)) {
         var5 = (Float)this.hints.get(KEY_MAX_WIDTH);
      }

      float var6 = -1.0F;
      if (this.hints.containsKey(KEY_MAX_HEIGHT)) {
         var6 = (Float)this.hints.get(KEY_MAX_HEIGHT);
      }

      if (var6 > 0.0F && this.height > var6) {
         this.width = var1 * var6 / var2;
         this.height = var6;
      }

      if (var5 > 0.0F && this.width > var5) {
         this.width = var5;
         this.height = var2 * var5 / var1;
      }

   }

   static {
      KEY_PIXEL_TO_MM = KEY_PIXEL_UNIT_TO_MILLIMETER;
      KEY_EXECUTE_ONLOAD = new BooleanKey();
      KEY_SNAPSHOT_TIME = new FloatKey();
      KEY_ALLOWED_SCRIPT_TYPES = new StringKey();
      KEY_CONSTRAIN_SCRIPT_ORIGIN = new BooleanKey();
   }

   protected class SVGAbstractTranscoderUserAgent extends UserAgentAdapter {
      protected List scripts;

      public SVGAbstractTranscoderUserAgent() {
         this.addStdFeatures();
      }

      public AffineTransform getTransform() {
         return SVGAbstractTranscoder.this.curTxf;
      }

      public void setTransform(AffineTransform var1) {
         SVGAbstractTranscoder.this.curTxf = var1;
      }

      public Dimension2D getViewportSize() {
         return new Dimension((int)SVGAbstractTranscoder.this.width, (int)SVGAbstractTranscoder.this.height);
      }

      public void displayError(String var1) {
         try {
            SVGAbstractTranscoder.this.handler.error(new TranscoderException(var1));
         } catch (TranscoderException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public void displayError(Exception var1) {
         try {
            var1.printStackTrace();
            SVGAbstractTranscoder.this.handler.error(new TranscoderException(var1));
         } catch (TranscoderException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public void displayMessage(String var1) {
         try {
            SVGAbstractTranscoder.this.handler.warning(new TranscoderException(var1));
         } catch (TranscoderException var3) {
            throw new RuntimeException(var3.getMessage());
         }
      }

      public float getPixelUnitToMillimeter() {
         Object var1 = SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_PIXEL_UNIT_TO_MILLIMETER);
         return var1 != null ? (Float)var1 : super.getPixelUnitToMillimeter();
      }

      public String getLanguages() {
         return SVGAbstractTranscoder.this.hints.containsKey(SVGAbstractTranscoder.KEY_LANGUAGE) ? (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_LANGUAGE) : super.getLanguages();
      }

      public String getMedia() {
         String var1 = (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_MEDIA);
         return var1 != null ? var1 : super.getMedia();
      }

      public String getDefaultFontFamily() {
         String var1 = (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_DEFAULT_FONT_FAMILY);
         return var1 != null ? var1 : super.getDefaultFontFamily();
      }

      public String getAlternateStyleSheet() {
         String var1 = (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_ALTERNATE_STYLESHEET);
         return var1 != null ? var1 : super.getAlternateStyleSheet();
      }

      public String getUserStyleSheetURI() {
         String var1 = (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_USER_STYLESHEET_URI);
         return var1 != null ? var1 : super.getUserStyleSheetURI();
      }

      public String getXMLParserClassName() {
         String var1 = (String)SVGAbstractTranscoder.this.hints.get(XMLAbstractTranscoder.KEY_XML_PARSER_CLASSNAME);
         return var1 != null ? var1 : super.getXMLParserClassName();
      }

      public boolean isXMLParserValidating() {
         Boolean var1 = (Boolean)SVGAbstractTranscoder.this.hints.get(XMLAbstractTranscoder.KEY_XML_PARSER_VALIDATING);
         return var1 != null ? var1 : super.isXMLParserValidating();
      }

      public ScriptSecurity getScriptSecurity(String var1, ParsedURL var2, ParsedURL var3) {
         if (this.scripts == null) {
            this.computeAllowedScripts();
         }

         if (!this.scripts.contains(var1)) {
            return new NoLoadScriptSecurity(var1);
         } else {
            boolean var4 = true;
            if (SVGAbstractTranscoder.this.hints.containsKey(SVGAbstractTranscoder.KEY_CONSTRAIN_SCRIPT_ORIGIN)) {
               var4 = (Boolean)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_CONSTRAIN_SCRIPT_ORIGIN);
            }

            return (ScriptSecurity)(var4 ? new DefaultScriptSecurity(var1, var2, var3) : new RelaxedScriptSecurity(var1, var2, var3));
         }
      }

      protected void computeAllowedScripts() {
         this.scripts = new LinkedList();
         if (SVGAbstractTranscoder.this.hints.containsKey(SVGAbstractTranscoder.KEY_ALLOWED_SCRIPT_TYPES)) {
            String var1 = (String)SVGAbstractTranscoder.this.hints.get(SVGAbstractTranscoder.KEY_ALLOWED_SCRIPT_TYPES);
            StringTokenizer var2 = new StringTokenizer(var1, ",");

            while(var2.hasMoreTokens()) {
               this.scripts.add(var2.nextToken());
            }

         }
      }
   }
}
