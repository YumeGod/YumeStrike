package org.apache.html.dom;

import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFrameSetElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLHtmlElement;
import org.w3c.dom.html.HTMLTitleElement;

public class HTMLDocumentImpl extends DocumentImpl implements HTMLDocument {
   private static final long serialVersionUID = 3258132457579427892L;
   private HTMLCollectionImpl _anchors;
   private HTMLCollectionImpl _forms;
   private HTMLCollectionImpl _images;
   private HTMLCollectionImpl _links;
   private HTMLCollectionImpl _applets;
   private StringWriter _writer;
   private static Hashtable _elementTypesHTML;
   private static final Class[] _elemClassSigHTML;
   // $FF: synthetic field
   static Class class$org$apache$html$dom$HTMLDocumentImpl;
   // $FF: synthetic field
   static Class class$java$lang$String;

   public HTMLDocumentImpl() {
      populateElementTypes();
   }

   public synchronized Element getDocumentElement() {
      for(Node var1 = this.getFirstChild(); var1 != null; var1 = var1.getNextSibling()) {
         if (var1 instanceof HTMLHtmlElement) {
            return (HTMLElement)var1;
         }
      }

      HTMLHtmlElementImpl var4 = new HTMLHtmlElementImpl(this, "HTML");

      Node var3;
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var3) {
         var3 = var2.getNextSibling();
         var4.appendChild(var2);
      }

      this.appendChild(var4);
      return (HTMLElement)var4;
   }

   public synchronized HTMLElement getHead() {
      Element var2 = this.getDocumentElement();
      HTMLHeadElementImpl var11;
      synchronized(var2) {
         Node var1;
         for(var1 = var2.getFirstChild(); var1 != null && !(var1 instanceof HTMLHeadElement); var1 = var1.getNextSibling()) {
         }

         if (var1 != null) {
            synchronized(var1) {
               Node var3 = var2.getFirstChild();

               while(true) {
                  if (var3 == null || var3 == var1) {
                     break;
                  }

                  Node var4 = var3.getNextSibling();
                  var1.insertBefore(var3, var1.getFirstChild());
                  var3 = var4;
               }
            }

            HTMLElement var7 = (HTMLElement)var1;
            return var7;
         }

         var11 = new HTMLHeadElementImpl(this, "HEAD");
         var2.insertBefore(var11, var2.getFirstChild());
      }

      return (HTMLElement)var11;
   }

   public synchronized String getTitle() {
      HTMLElement var1 = this.getHead();
      Node var3 = var1.getElementsByTagName("TITLE").item(0);
      NodeList var2 = var1.getElementsByTagName("TITLE");
      if (var2.getLength() > 0) {
         var3 = var2.item(0);
         return ((HTMLTitleElement)var3).getText();
      } else {
         return "";
      }
   }

   public synchronized void setTitle(String var1) {
      HTMLElement var2 = this.getHead();
      NodeList var3 = var2.getElementsByTagName("TITLE");
      if (var3.getLength() > 0) {
         Node var4 = var3.item(0);
         if (var4.getParentNode() != var2) {
            var2.appendChild(var4);
         }

         ((HTMLTitleElement)var4).setText(var1);
      } else {
         HTMLTitleElementImpl var5 = new HTMLTitleElementImpl(this, "TITLE");
         ((HTMLTitleElement)var5).setText(var1);
         var2.appendChild(var5);
      }

   }

   public synchronized HTMLElement getBody() {
      Element var1 = this.getDocumentElement();
      HTMLElement var2 = this.getHead();
      HTMLBodyElementImpl var12;
      synchronized(var1) {
         Node var3;
         for(var3 = var2.getNextSibling(); var3 != null && !(var3 instanceof HTMLBodyElement) && !(var3 instanceof HTMLFrameSetElement); var3 = var3.getNextSibling()) {
         }

         if (var3 != null) {
            synchronized(var3) {
               Node var4 = var2.getNextSibling();

               while(true) {
                  if (var4 == null || var4 == var3) {
                     break;
                  }

                  Node var5 = var4.getNextSibling();
                  var3.insertBefore(var4, var3.getFirstChild());
                  var4 = var5;
               }
            }

            HTMLElement var8 = (HTMLElement)var3;
            return var8;
         }

         var12 = new HTMLBodyElementImpl(this, "BODY");
         var1.appendChild(var12);
      }

      return (HTMLElement)var12;
   }

   public synchronized void setBody(HTMLElement var1) {
      synchronized(var1) {
         Element var2 = this.getDocumentElement();
         HTMLElement var4 = this.getHead();
         synchronized(var2) {
            NodeList var6 = this.getElementsByTagName("BODY");
            if (var6.getLength() > 0) {
               Node var3 = var6.item(0);
               synchronized(var3) {
                  for(Object var5 = var4; var5 != null; var5 = ((Node)var5).getNextSibling()) {
                     if (var5 instanceof Element) {
                        if (var5 != var3) {
                           var2.insertBefore(var1, (Node)var5);
                        } else {
                           var2.replaceChild(var1, var3);
                        }

                        return;
                     }
                  }

                  var2.appendChild(var1);
               }

               return;
            }

            var2.appendChild(var1);
         }

      }
   }

   public synchronized Element getElementById(String var1) {
      return this.getElementById(var1, this);
   }

   public NodeList getElementsByName(String var1) {
      return new NameNodeListImpl(this, var1);
   }

   public final NodeList getElementsByTagName(String var1) {
      return super.getElementsByTagName(var1.toUpperCase(Locale.ENGLISH));
   }

   public final NodeList getElementsByTagNameNS(String var1, String var2) {
      return var1 != null && var1.length() > 0 ? super.getElementsByTagNameNS(var1, var2.toUpperCase(Locale.ENGLISH)) : super.getElementsByTagName(var2.toUpperCase(Locale.ENGLISH));
   }

   public Element createElementNS(String var1, String var2, String var3) throws DOMException {
      return this.createElementNS(var1, var2);
   }

   public Element createElementNS(String var1, String var2) {
      return var1 != null && var1.length() != 0 ? super.createElementNS(var1, var2) : this.createElement(var2);
   }

   public Element createElement(String var1) throws DOMException {
      var1 = var1.toUpperCase(Locale.ENGLISH);
      Class var2 = (Class)_elementTypesHTML.get(var1);
      if (var2 != null) {
         try {
            Constructor var3 = var2.getConstructor(_elemClassSigHTML);
            return (Element)var3.newInstance(this, var1);
         } catch (Exception var6) {
            if (var6 instanceof InvocationTargetException) {
               Throwable var5 = ((InvocationTargetException)var6).getTargetException();
            }

            throw new IllegalStateException("HTM15 Tag '" + var1 + "' associated with an Element class that failed to construct.\n" + var1);
         }
      } else {
         return new HTMLElementImpl(this, var1);
      }
   }

   public Attr createAttribute(String var1) throws DOMException {
      return super.createAttribute(var1.toLowerCase(Locale.ENGLISH));
   }

   public String getReferrer() {
      return null;
   }

   public String getDomain() {
      return null;
   }

   public String getURL() {
      return null;
   }

   public String getCookie() {
      return null;
   }

   public void setCookie(String var1) {
   }

   public HTMLCollection getImages() {
      if (this._images == null) {
         this._images = new HTMLCollectionImpl(this.getBody(), (short)3);
      }

      return this._images;
   }

   public HTMLCollection getApplets() {
      if (this._applets == null) {
         this._applets = new HTMLCollectionImpl(this.getBody(), (short)4);
      }

      return this._applets;
   }

   public HTMLCollection getLinks() {
      if (this._links == null) {
         this._links = new HTMLCollectionImpl(this.getBody(), (short)5);
      }

      return this._links;
   }

   public HTMLCollection getForms() {
      if (this._forms == null) {
         this._forms = new HTMLCollectionImpl(this.getBody(), (short)2);
      }

      return this._forms;
   }

   public HTMLCollection getAnchors() {
      if (this._anchors == null) {
         this._anchors = new HTMLCollectionImpl(this.getBody(), (short)1);
      }

      return this._anchors;
   }

   public void open() {
      if (this._writer == null) {
         this._writer = new StringWriter();
      }

   }

   public void close() {
      if (this._writer != null) {
         this._writer = null;
      }

   }

   public void write(String var1) {
      if (this._writer != null) {
         this._writer.write(var1);
      }

   }

   public void writeln(String var1) {
      if (this._writer != null) {
         this._writer.write(var1 + "\n");
      }

   }

   public Node cloneNode(boolean var1) {
      HTMLDocumentImpl var2 = new HTMLDocumentImpl();
      if (var1) {
         for(NodeImpl var3 = (NodeImpl)this.getFirstChild(); var3 != null; var3 = (NodeImpl)var3.getNextSibling()) {
            var2.appendChild(var2.importNode(var3, true));
         }
      }

      return var2;
   }

   private Element getElementById(String var1, Node var2) {
      for(Node var3 = var2.getFirstChild(); var3 != null; var3 = var3.getNextSibling()) {
         if (var3 instanceof Element) {
            if (var1.equals(((Element)var3).getAttribute("id"))) {
               return (Element)var3;
            }

            Element var4 = this.getElementById(var1, var3);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return null;
   }

   private static synchronized void populateElementTypes() {
      if (_elementTypesHTML == null) {
         _elementTypesHTML = new Hashtable(63);
         populateElementType("A", "HTMLAnchorElementImpl");
         populateElementType("APPLET", "HTMLAppletElementImpl");
         populateElementType("AREA", "HTMLAreaElementImpl");
         populateElementType("BASE", "HTMLBaseElementImpl");
         populateElementType("BASEFONT", "HTMLBaseFontElementImpl");
         populateElementType("BLOCKQUOTE", "HTMLQuoteElementImpl");
         populateElementType("BODY", "HTMLBodyElementImpl");
         populateElementType("BR", "HTMLBRElementImpl");
         populateElementType("BUTTON", "HTMLButtonElementImpl");
         populateElementType("DEL", "HTMLModElementImpl");
         populateElementType("DIR", "HTMLDirectoryElementImpl");
         populateElementType("DIV", "HTMLDivElementImpl");
         populateElementType("DL", "HTMLDListElementImpl");
         populateElementType("FIELDSET", "HTMLFieldSetElementImpl");
         populateElementType("FONT", "HTMLFontElementImpl");
         populateElementType("FORM", "HTMLFormElementImpl");
         populateElementType("FRAME", "HTMLFrameElementImpl");
         populateElementType("FRAMESET", "HTMLFrameSetElementImpl");
         populateElementType("HEAD", "HTMLHeadElementImpl");
         populateElementType("H1", "HTMLHeadingElementImpl");
         populateElementType("H2", "HTMLHeadingElementImpl");
         populateElementType("H3", "HTMLHeadingElementImpl");
         populateElementType("H4", "HTMLHeadingElementImpl");
         populateElementType("H5", "HTMLHeadingElementImpl");
         populateElementType("H6", "HTMLHeadingElementImpl");
         populateElementType("HR", "HTMLHRElementImpl");
         populateElementType("HTML", "HTMLHtmlElementImpl");
         populateElementType("IFRAME", "HTMLIFrameElementImpl");
         populateElementType("IMG", "HTMLImageElementImpl");
         populateElementType("INPUT", "HTMLInputElementImpl");
         populateElementType("INS", "HTMLModElementImpl");
         populateElementType("ISINDEX", "HTMLIsIndexElementImpl");
         populateElementType("LABEL", "HTMLLabelElementImpl");
         populateElementType("LEGEND", "HTMLLegendElementImpl");
         populateElementType("LI", "HTMLLIElementImpl");
         populateElementType("LINK", "HTMLLinkElementImpl");
         populateElementType("MAP", "HTMLMapElementImpl");
         populateElementType("MENU", "HTMLMenuElementImpl");
         populateElementType("META", "HTMLMetaElementImpl");
         populateElementType("OBJECT", "HTMLObjectElementImpl");
         populateElementType("OL", "HTMLOListElementImpl");
         populateElementType("OPTGROUP", "HTMLOptGroupElementImpl");
         populateElementType("OPTION", "HTMLOptionElementImpl");
         populateElementType("P", "HTMLParagraphElementImpl");
         populateElementType("PARAM", "HTMLParamElementImpl");
         populateElementType("PRE", "HTMLPreElementImpl");
         populateElementType("Q", "HTMLQuoteElementImpl");
         populateElementType("SCRIPT", "HTMLScriptElementImpl");
         populateElementType("SELECT", "HTMLSelectElementImpl");
         populateElementType("STYLE", "HTMLStyleElementImpl");
         populateElementType("TABLE", "HTMLTableElementImpl");
         populateElementType("CAPTION", "HTMLTableCaptionElementImpl");
         populateElementType("TD", "HTMLTableCellElementImpl");
         populateElementType("TH", "HTMLTableCellElementImpl");
         populateElementType("COL", "HTMLTableColElementImpl");
         populateElementType("COLGROUP", "HTMLTableColElementImpl");
         populateElementType("TR", "HTMLTableRowElementImpl");
         populateElementType("TBODY", "HTMLTableSectionElementImpl");
         populateElementType("THEAD", "HTMLTableSectionElementImpl");
         populateElementType("TFOOT", "HTMLTableSectionElementImpl");
         populateElementType("TEXTAREA", "HTMLTextAreaElementImpl");
         populateElementType("TITLE", "HTMLTitleElementImpl");
         populateElementType("UL", "HTMLUListElementImpl");
      }
   }

   private static void populateElementType(String var0, String var1) {
      try {
         _elementTypesHTML.put(var0, ObjectFactory.findProviderClass("org.apache.html.dom." + var1, (class$org$apache$html$dom$HTMLDocumentImpl == null ? (class$org$apache$html$dom$HTMLDocumentImpl = class$("org.apache.html.dom.HTMLDocumentImpl")) : class$org$apache$html$dom$HTMLDocumentImpl).getClassLoader(), true));
      } catch (Exception var3) {
         new RuntimeException("HTM019 OpenXML Error: Could not find or execute class " + var1 + " implementing HTML element " + var0 + "\n" + var1 + "\t" + var0);
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      _elemClassSigHTML = new Class[]{class$org$apache$html$dom$HTMLDocumentImpl == null ? (class$org$apache$html$dom$HTMLDocumentImpl = class$("org.apache.html.dom.HTMLDocumentImpl")) : class$org$apache$html$dom$HTMLDocumentImpl, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
   }
}
