package org.apache.batik.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.Manifest;
import org.apache.batik.dom.AbstractElement;
import org.apache.batik.dom.events.AbstractEvent;
import org.apache.batik.dom.events.NodeEventTarget;
import org.apache.batik.dom.util.XLinkSupport;
import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterException;
import org.apache.batik.script.ScriptEventWrapper;
import org.apache.batik.script.ScriptHandler;
import org.apache.batik.util.ParsedURL;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.svg.EventListenerInitializer;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGSVGElement;

public class BaseScriptingEnvironment {
   public static final String INLINE_SCRIPT_DESCRIPTION = "BaseScriptingEnvironment.constant.inline.script.description";
   public static final String EVENT_SCRIPT_DESCRIPTION = "BaseScriptingEnvironment.constant.event.script.description";
   protected static final String EVENT_NAME = "event";
   protected static final String ALTERNATE_EVENT_NAME = "evt";
   protected static final String APPLICATION_ECMASCRIPT = "application/ecmascript";
   protected BridgeContext bridgeContext;
   protected UserAgent userAgent;
   protected Document document;
   protected ParsedURL docPURL;
   protected Set languages = new HashSet();
   protected Interpreter interpreter;

   public static boolean isDynamicDocument(BridgeContext var0, Document var1) {
      Element var2 = var1.getDocumentElement();
      if (var2 != null && "http://www.w3.org/2000/svg".equals(var2.getNamespaceURI())) {
         if (var2.getAttributeNS((String)null, "onabort").length() > 0) {
            return true;
         } else if (var2.getAttributeNS((String)null, "onerror").length() > 0) {
            return true;
         } else if (var2.getAttributeNS((String)null, "onresize").length() > 0) {
            return true;
         } else if (var2.getAttributeNS((String)null, "onunload").length() > 0) {
            return true;
         } else if (var2.getAttributeNS((String)null, "onscroll").length() > 0) {
            return true;
         } else {
            return var2.getAttributeNS((String)null, "onzoom").length() > 0 ? true : isDynamicElement(var0, var1.getDocumentElement());
         }
      } else {
         return false;
      }
   }

   public static boolean isDynamicElement(BridgeContext var0, Element var1) {
      List var2 = var0.getBridgeExtensions(var1.getOwnerDocument());
      return isDynamicElement(var1, var0, var2);
   }

   public static boolean isDynamicElement(Element var0, BridgeContext var1, List var2) {
      Iterator var3 = var2.iterator();

      BridgeExtension var4;
      do {
         if (!var3.hasNext()) {
            if ("http://www.w3.org/2000/svg".equals(var0.getNamespaceURI())) {
               if (var0.getAttributeNS((String)null, "onkeyup").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onkeydown").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onkeypress").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onload").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onerror").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onactivate").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onclick").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onfocusin").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onfocusout").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onmousedown").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onmousemove").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onmouseout").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onmouseover").length() > 0) {
                  return true;
               }

               if (var0.getAttributeNS((String)null, "onmouseup").length() > 0) {
                  return true;
               }
            }

            for(Node var5 = var0.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
               if (var5.getNodeType() == 1 && isDynamicElement(var1, (Element)var5)) {
                  return true;
               }
            }

            return false;
         }

         var4 = (BridgeExtension)var3.next();
      } while(!var4.isDynamicElement(var0));

      return true;
   }

   public BaseScriptingEnvironment(BridgeContext var1) {
      this.bridgeContext = var1;
      this.document = var1.getDocument();
      this.docPURL = new ParsedURL(((SVGDocument)this.document).getURL());
      this.userAgent = this.bridgeContext.getUserAgent();
   }

   public org.apache.batik.script.Window createWindow(Interpreter var1, String var2) {
      return new Window(var1, var2);
   }

   public org.apache.batik.script.Window createWindow() {
      return this.createWindow((Interpreter)null, (String)null);
   }

   public Interpreter getInterpreter() {
      if (this.interpreter != null) {
         return this.interpreter;
      } else {
         SVGSVGElement var1 = (SVGSVGElement)this.document.getDocumentElement();
         String var2 = var1.getContentScriptType();
         return this.getInterpreter(var2);
      }
   }

   public Interpreter getInterpreter(String var1) {
      this.interpreter = this.bridgeContext.getInterpreter(var1);
      if (this.interpreter == null) {
         if (this.languages.contains(var1)) {
            return null;
         } else {
            this.languages.add(var1);
            return null;
         }
      } else {
         if (!this.languages.contains(var1)) {
            this.languages.add(var1);
            this.initializeEnvironment(this.interpreter, var1);
         }

         return this.interpreter;
      }
   }

   public void initializeEnvironment(Interpreter var1, String var2) {
      var1.bindObject("window", this.createWindow(var1, var2));
   }

   public void loadScripts() {
      org.apache.batik.script.Window var1 = null;
      NodeList var2 = this.document.getElementsByTagNameNS("http://www.w3.org/2000/svg", "script");
      int var3 = var2.getLength();
      if (var3 != 0) {
         for(int var4 = 0; var4 < var3; ++var4) {
            AbstractElement var5 = (AbstractElement)var2.item(var4);
            String var6 = var5.getAttributeNS((String)null, "type");
            if (var6.length() == 0) {
               var6 = "text/ecmascript";
            }

            String var13;
            if (var6.equals("application/java-archive")) {
               try {
                  String var24 = XLinkSupport.getXLinkHref(var5);
                  ParsedURL var25 = new ParsedURL(var5.getBaseURI(), var24);
                  this.checkCompatibleScriptURL(var6, var25);
                  URL var29 = null;

                  try {
                     var29 = new URL(this.docPURL.toString());
                  } catch (MalformedURLException var18) {
                  }

                  DocumentJarClassLoader var26 = new DocumentJarClassLoader(new URL(var25.toString()), var29);
                  URL var28 = var26.findResource("META-INF/MANIFEST.MF");
                  if (var28 != null) {
                     Manifest var30 = new Manifest(var28.openStream());
                     var13 = var30.getMainAttributes().getValue("Script-Handler");
                     if (var13 != null) {
                        ScriptHandler var35 = (ScriptHandler)var26.loadClass(var13).newInstance();
                        if (var1 == null) {
                           var1 = this.createWindow();
                        }

                        var35.run(this.document, var1);
                     }

                     var13 = var30.getMainAttributes().getValue("SVG-Handler-Class");
                     if (var13 != null) {
                        EventListenerInitializer var36 = (EventListenerInitializer)var26.loadClass(var13).newInstance();
                        if (var1 == null) {
                           var1 = this.createWindow();
                        }

                        var36.initializeEventListeners((SVGDocument)this.document);
                     }
                  }
               } catch (Exception var20) {
                  if (this.userAgent != null) {
                     this.userAgent.displayError(var20);
                  }
               }
            } else {
               Interpreter var7 = this.getInterpreter(var6);
               if (var7 != null) {
                  try {
                     String var8 = XLinkSupport.getXLinkHref(var5);
                     String var9 = null;
                     Object var10 = null;
                     if (var8.length() > 0) {
                        var9 = var8;
                        ParsedURL var11 = new ParsedURL(var5.getBaseURI(), var8);
                        this.checkCompatibleScriptURL(var6, var11);
                        InputStream var12 = var11.openStream();
                        var13 = var11.getContentTypeMediaType();
                        String var14 = var11.getContentTypeCharset();
                        if (var14 != null) {
                           try {
                              var10 = new InputStreamReader(var12, var14);
                           } catch (UnsupportedEncodingException var19) {
                              var14 = null;
                           }
                        }

                        if (var10 == null) {
                           if ("application/ecmascript".equals(var13)) {
                              if (var11.hasContentTypeParameter("version")) {
                                 continue;
                              }

                              PushbackInputStream var15 = new PushbackInputStream(var12, 8);
                              byte[] var16 = new byte[4];
                              int var17 = var15.read(var16);
                              if (var17 > 0) {
                                 var15.unread(var16, 0, var17);
                                 if (var17 >= 2) {
                                    if (var16[0] == -1 && var16[1] == -2) {
                                       if (var17 >= 4 && var16[2] == 0 && var16[3] == 0) {
                                          var14 = "UTF32-LE";
                                          var15.skip(4L);
                                       } else {
                                          var14 = "UTF-16LE";
                                          var15.skip(2L);
                                       }
                                    } else if (var16[0] == -2 && var16[1] == -1) {
                                       var14 = "UTF-16BE";
                                       var15.skip(2L);
                                    } else if (var17 >= 3 && var16[0] == -17 && var16[1] == -69 && var16[2] == -65) {
                                       var14 = "UTF-8";
                                       var15.skip(3L);
                                    } else if (var17 >= 4 && var16[0] == 0 && var16[1] == 0 && var16[2] == -2 && var16[3] == -1) {
                                       var14 = "UTF-32BE";
                                       var15.skip(4L);
                                    }
                                 }

                                 if (var14 == null) {
                                    var14 = "UTF-8";
                                 }
                              }

                              var10 = new InputStreamReader(var15, var14);
                           } else {
                              var10 = new InputStreamReader(var12);
                           }
                        }
                     } else {
                        this.checkCompatibleScriptURL(var6, this.docPURL);
                        DocumentLoader var27 = this.bridgeContext.getDocumentLoader();
                        SVGDocument var31 = (SVGDocument)var5.getOwnerDocument();
                        int var34 = var27.getLineNumber(var5);
                        var9 = Messages.formatMessage("BaseScriptingEnvironment.constant.inline.script.description", new Object[]{var31.getURL(), "<" + var5.getNodeName() + ">", new Integer(var34)});
                        Node var32 = var5.getFirstChild();
                        if (var32 == null) {
                           continue;
                        }

                        StringBuffer var33;
                        for(var33 = new StringBuffer(); var32 != null; var32 = var32.getNextSibling()) {
                           if (var32.getNodeType() == 4 || var32.getNodeType() == 3) {
                              var33.append(var32.getNodeValue());
                           }
                        }

                        var10 = new StringReader(var33.toString());
                     }

                     var7.evaluate((Reader)var10, var9);
                  } catch (IOException var21) {
                     if (this.userAgent != null) {
                        this.userAgent.displayError(var21);
                     }

                     return;
                  } catch (InterpreterException var22) {
                     System.err.println("InterpExcept: " + var22);
                     this.handleInterpreterException(var22);
                     return;
                  } catch (SecurityException var23) {
                     if (this.userAgent != null) {
                        this.userAgent.displayError(var23);
                     }
                  }
               }
            }
         }

      }
   }

   protected void checkCompatibleScriptURL(String var1, ParsedURL var2) {
      this.userAgent.checkLoadScript(var1, var2, this.docPURL);
   }

   public void dispatchSVGLoadEvent() {
      SVGSVGElement var1 = (SVGSVGElement)this.document.getDocumentElement();
      String var2 = var1.getContentScriptType();
      long var3 = System.currentTimeMillis();
      this.bridgeContext.getAnimationEngine().start(var3);
      this.dispatchSVGLoad(var1, true, var2);
   }

   protected void dispatchSVGLoad(Element var1, boolean var2, String var3) {
      for(Node var4 = var1.getFirstChild(); var4 != null; var4 = var4.getNextSibling()) {
         if (var4.getNodeType() == 1) {
            this.dispatchSVGLoad((Element)var4, var2, var3);
         }
      }

      DocumentEvent var15 = (DocumentEvent)var1.getOwnerDocument();
      AbstractEvent var5 = (AbstractEvent)var15.createEvent("SVGEvents");
      String var6;
      if (this.bridgeContext.isSVG12()) {
         var6 = "load";
      } else {
         var6 = "SVGLoad";
      }

      var5.initEventNS("http://www.w3.org/2001/xml-events", var6, false, false);
      NodeEventTarget var7 = (NodeEventTarget)var1;
      final String var8 = var1.getAttributeNS((String)null, "onload");
      if (var8.length() == 0) {
         var7.dispatchEvent(var5);
      } else {
         final Interpreter var9 = this.getInterpreter();
         if (var9 == null) {
            var7.dispatchEvent(var5);
         } else {
            if (var2) {
               this.checkCompatibleScriptURL(var3, this.docPURL);
               var2 = false;
            }

            DocumentLoader var10 = this.bridgeContext.getDocumentLoader();
            SVGDocument var11 = (SVGDocument)var1.getOwnerDocument();
            int var12 = var10.getLineNumber(var1);
            final String var13 = Messages.formatMessage("BaseScriptingEnvironment.constant.event.script.description", new Object[]{var11.getURL(), "onload", new Integer(var12)});
            EventListener var14 = new EventListener() {
               public void handleEvent(Event var1) {
                  try {
                     Object var2;
                     if (var1 instanceof ScriptEventWrapper) {
                        var2 = ((ScriptEventWrapper)var1).getEventObject();
                     } else {
                        var2 = var1;
                     }

                     var9.bindObject("event", var2);
                     var9.bindObject("evt", var2);
                     var9.evaluate(new StringReader(var8), var13);
                  } catch (IOException var3) {
                  } catch (InterpreterException var4) {
                     BaseScriptingEnvironment.this.handleInterpreterException(var4);
                  }

               }
            };
            var7.addEventListenerNS("http://www.w3.org/2001/xml-events", var6, var14, false, (Object)null);
            var7.dispatchEvent(var5);
            var7.removeEventListenerNS("http://www.w3.org/2001/xml-events", var6, var14, false);
         }
      }
   }

   protected void dispatchSVGZoomEvent() {
      if (this.bridgeContext.isSVG12()) {
         this.dispatchSVGDocEvent("zoom");
      } else {
         this.dispatchSVGDocEvent("SVGZoom");
      }

   }

   protected void dispatchSVGScrollEvent() {
      if (this.bridgeContext.isSVG12()) {
         this.dispatchSVGDocEvent("scroll");
      } else {
         this.dispatchSVGDocEvent("SVGScroll");
      }

   }

   protected void dispatchSVGResizeEvent() {
      if (this.bridgeContext.isSVG12()) {
         this.dispatchSVGDocEvent("resize");
      } else {
         this.dispatchSVGDocEvent("SVGResize");
      }

   }

   protected void dispatchSVGDocEvent(String var1) {
      SVGSVGElement var2 = (SVGSVGElement)this.document.getDocumentElement();
      DocumentEvent var4 = (DocumentEvent)this.document;
      AbstractEvent var5 = (AbstractEvent)var4.createEvent("SVGEvents");
      var5.initEventNS("http://www.w3.org/2001/xml-events", var1, false, false);
      var2.dispatchEvent(var5);
   }

   protected void handleInterpreterException(InterpreterException var1) {
      if (this.userAgent != null) {
         Exception var2 = var1.getException();
         this.userAgent.displayError((Exception)(var2 == null ? var1 : var2));
      }

   }

   protected void handleSecurityException(SecurityException var1) {
      if (this.userAgent != null) {
         this.userAgent.displayError(var1);
      }

   }

   protected class Window implements org.apache.batik.script.Window {
      protected Interpreter interpreter;
      protected String language;

      public Window(Interpreter var2, String var3) {
         this.interpreter = var2;
         this.language = var3;
      }

      public Object setInterval(String var1, long var2) {
         return null;
      }

      public Object setInterval(Runnable var1, long var2) {
         return null;
      }

      public void clearInterval(Object var1) {
      }

      public Object setTimeout(String var1, long var2) {
         return null;
      }

      public Object setTimeout(Runnable var1, long var2) {
         return null;
      }

      public void clearTimeout(Object var1) {
      }

      public Node parseXML(String var1, Document var2) {
         return null;
      }

      public void getURL(String var1, org.apache.batik.script.Window.URLResponseHandler var2) {
         this.getURL(var1, var2, "UTF8");
      }

      public void getURL(String var1, org.apache.batik.script.Window.URLResponseHandler var2, String var3) {
      }

      public void postURL(String var1, String var2, org.apache.batik.script.Window.URLResponseHandler var3) {
         this.postURL(var1, var2, var3, "text/plain", (String)null);
      }

      public void postURL(String var1, String var2, org.apache.batik.script.Window.URLResponseHandler var3, String var4) {
         this.postURL(var1, var2, var3, var4, (String)null);
      }

      public void postURL(String var1, String var2, org.apache.batik.script.Window.URLResponseHandler var3, String var4, String var5) {
      }

      public void alert(String var1) {
      }

      public boolean confirm(String var1) {
         return false;
      }

      public String prompt(String var1) {
         return null;
      }

      public String prompt(String var1, String var2) {
         return null;
      }

      public BridgeContext getBridgeContext() {
         return BaseScriptingEnvironment.this.bridgeContext;
      }

      public Interpreter getInterpreter() {
         return this.interpreter;
      }
   }
}
