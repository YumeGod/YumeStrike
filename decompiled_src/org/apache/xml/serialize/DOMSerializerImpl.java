package org.apache.xml.serialize;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DOMErrorImpl;
import org.apache.xerces.dom.DOMLocatorImpl;
import org.apache.xerces.dom.DOMMessageFormatter;
import org.apache.xerces.dom.DOMNormalizer;
import org.apache.xerces.dom.DOMStringListImpl;
import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMStringList;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

public class DOMSerializerImpl implements LSSerializer, DOMConfiguration {
   private XMLSerializer serializer;
   private XML11Serializer xml11Serializer;
   private DOMStringList fRecognizedParameters;
   protected short features = 0;
   protected static final short NAMESPACES = 1;
   protected static final short WELLFORMED = 2;
   protected static final short ENTITIES = 4;
   protected static final short CDATA = 8;
   protected static final short SPLITCDATA = 16;
   protected static final short COMMENTS = 32;
   protected static final short DISCARDDEFAULT = 64;
   protected static final short INFOSET = 128;
   protected static final short XMLDECL = 256;
   protected static final short NSDECL = 512;
   protected static final short DOM_ELEMENT_CONTENT_WHITESPACE = 1024;
   private DOMErrorHandler fErrorHandler = null;
   private final DOMErrorImpl fError = new DOMErrorImpl();
   private final DOMLocatorImpl fLocator = new DOMLocatorImpl();
   private static final RuntimeException abort = new RuntimeException();

   public DOMSerializerImpl() {
      this.features = (short)(this.features | 1);
      this.features = (short)(this.features | 4);
      this.features = (short)(this.features | 32);
      this.features = (short)(this.features | 8);
      this.features = (short)(this.features | 16);
      this.features = (short)(this.features | 2);
      this.features = (short)(this.features | 512);
      this.features = (short)(this.features | 1024);
      this.features = (short)(this.features | 64);
      this.features = (short)(this.features | 256);
      this.serializer = new XMLSerializer();
      this.initSerializer(this.serializer);
   }

   public DOMConfiguration getDomConfig() {
      return this;
   }

   public void setParameter(String var1, Object var2) throws DOMException {
      if (var2 instanceof Boolean) {
         boolean var3 = (Boolean)var2;
         if (var1.equalsIgnoreCase("infoset")) {
            if (var3) {
               this.features = (short)(this.features & -5);
               this.features = (short)(this.features & -9);
               this.features = (short)(this.features | 1);
               this.features = (short)(this.features | 512);
               this.features = (short)(this.features | 2);
               this.features = (short)(this.features | 32);
            }
         } else if (var1.equalsIgnoreCase("xml-declaration")) {
            this.features = var3 ? (short)(this.features | 256) : (short)(this.features & -257);
         } else if (var1.equalsIgnoreCase("namespaces")) {
            this.features = var3 ? (short)(this.features | 1) : (short)(this.features & -2);
            this.serializer.fNamespaces = var3;
         } else if (var1.equalsIgnoreCase("split-cdata-sections")) {
            this.features = var3 ? (short)(this.features | 16) : (short)(this.features & -17);
         } else if (var1.equalsIgnoreCase("discard-default-content")) {
            this.features = var3 ? (short)(this.features | 64) : (short)(this.features & -65);
         } else if (var1.equalsIgnoreCase("well-formed")) {
            this.features = var3 ? (short)(this.features | 2) : (short)(this.features & -3);
         } else if (var1.equalsIgnoreCase("entities")) {
            this.features = var3 ? (short)(this.features | 4) : (short)(this.features & -5);
         } else if (var1.equalsIgnoreCase("cdata-sections")) {
            this.features = var3 ? (short)(this.features | 8) : (short)(this.features & -9);
         } else if (var1.equalsIgnoreCase("comments")) {
            this.features = var3 ? (short)(this.features | 32) : (short)(this.features & -33);
         } else {
            String var4;
            if (!var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("validate") && !var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("datatype-normalization") && !var1.equalsIgnoreCase("format-pretty-print") && !var1.equalsIgnoreCase("normalize-characters")) {
               if (var1.equalsIgnoreCase("namespace-declarations")) {
                  this.features = var3 ? (short)(this.features | 512) : (short)(this.features & -513);
                  this.serializer.fNamespacePrefixes = var3;
               } else {
                  if (!var1.equalsIgnoreCase("element-content-whitespace") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
                     var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
                     throw new DOMException((short)9, var4);
                  }

                  if (!var3) {
                     var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                     throw new DOMException((short)9, var4);
                  }
               }
            } else if (var3) {
               var4 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
               throw new DOMException((short)9, var4);
            }
         }
      } else {
         String var5;
         if (!var1.equalsIgnoreCase("error-handler")) {
            if (!var1.equalsIgnoreCase("resource-resolver") && !var1.equalsIgnoreCase("schema-location") && (!var1.equalsIgnoreCase("schema-type") || var2 == null)) {
               var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
               throw new DOMException((short)8, var5);
            }

            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
            throw new DOMException((short)9, var5);
         }

         if (var2 != null && !(var2 instanceof DOMErrorHandler)) {
            var5 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "TYPE_MISMATCH_ERR", new Object[]{var1});
            throw new DOMException((short)17, var5);
         }

         this.fErrorHandler = (DOMErrorHandler)var2;
      }

   }

   public boolean canSetParameter(String var1, Object var2) {
      if (var2 == null) {
         return true;
      } else if (var2 instanceof Boolean) {
         boolean var3 = (Boolean)var2;
         if (!var1.equalsIgnoreCase("namespaces") && !var1.equalsIgnoreCase("split-cdata-sections") && !var1.equalsIgnoreCase("discard-default-content") && !var1.equalsIgnoreCase("xml-declaration") && !var1.equalsIgnoreCase("well-formed") && !var1.equalsIgnoreCase("infoset") && !var1.equalsIgnoreCase("entities") && !var1.equalsIgnoreCase("cdata-sections") && !var1.equalsIgnoreCase("comments") && !var1.equalsIgnoreCase("namespace-declarations")) {
            if (!var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("validate") && !var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("datatype-normalization") && !var1.equalsIgnoreCase("format-pretty-print") && !var1.equalsIgnoreCase("normalize-characters")) {
               if (!var1.equalsIgnoreCase("element-content-whitespace") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
                  return false;
               } else {
                  return var3;
               }
            } else {
               return !var3;
            }
         } else {
            return true;
         }
      } else if (var1.equalsIgnoreCase("error-handler") && var2 == null || var2 instanceof DOMErrorHandler) {
         return true;
      } else {
         return false;
      }
   }

   public DOMStringList getParameterNames() {
      if (this.fRecognizedParameters == null) {
         Vector var1 = new Vector();
         var1.add("namespaces");
         var1.add("split-cdata-sections");
         var1.add("discard-default-content");
         var1.add("xml-declaration");
         var1.add("canonical-form");
         var1.add("validate-if-schema");
         var1.add("validate");
         var1.add("check-character-normalization");
         var1.add("datatype-normalization");
         var1.add("format-pretty-print");
         var1.add("normalize-characters");
         var1.add("well-formed");
         var1.add("infoset");
         var1.add("namespace-declarations");
         var1.add("element-content-whitespace");
         var1.add("entities");
         var1.add("cdata-sections");
         var1.add("comments");
         var1.add("ignore-unknown-character-denormalizations");
         var1.add("error-handler");
         this.fRecognizedParameters = new DOMStringListImpl(var1);
      }

      return this.fRecognizedParameters;
   }

   public Object getParameter(String var1) throws DOMException {
      if (var1.equalsIgnoreCase("comments")) {
         return (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("namespaces")) {
         return (this.features & 1) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("xml-declaration")) {
         return (this.features & 256) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("cdata-sections")) {
         return (this.features & 8) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("entities")) {
         return (this.features & 4) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("split-cdata-sections")) {
         return (this.features & 16) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("well-formed")) {
         return (this.features & 2) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (var1.equalsIgnoreCase("namespace-declarations")) {
         return (this.features & 512) != 0 ? Boolean.TRUE : Boolean.FALSE;
      } else if (!var1.equalsIgnoreCase("element-content-whitespace") && !var1.equalsIgnoreCase("ignore-unknown-character-denormalizations")) {
         if (var1.equalsIgnoreCase("discard-default-content")) {
            return (this.features & 64) != 0 ? Boolean.TRUE : Boolean.FALSE;
         } else if (var1.equalsIgnoreCase("infoset")) {
            return (this.features & 4) == 0 && (this.features & 8) == 0 && (this.features & 1) != 0 && (this.features & 512) != 0 && (this.features & 2) != 0 && (this.features & 32) != 0 ? Boolean.TRUE : Boolean.FALSE;
         } else if (!var1.equalsIgnoreCase("format-pretty-print") && !var1.equalsIgnoreCase("normalize-characters") && !var1.equalsIgnoreCase("canonical-form") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("check-character-normalization") && !var1.equalsIgnoreCase("validate") && !var1.equalsIgnoreCase("validate-if-schema") && !var1.equalsIgnoreCase("datatype-normalization")) {
            if (var1.equalsIgnoreCase("error-handler")) {
               return this.fErrorHandler;
            } else {
               String var2;
               if (!var1.equalsIgnoreCase("resource-resolver") && !var1.equalsIgnoreCase("schema-location") && !var1.equalsIgnoreCase("schema-type")) {
                  var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_FOUND", new Object[]{var1});
                  throw new DOMException((short)8, var2);
               } else {
                  var2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "FEATURE_NOT_SUPPORTED", new Object[]{var1});
                  throw new DOMException((short)9, var2);
               }
            }
         } else {
            return Boolean.FALSE;
         }
      } else {
         return Boolean.TRUE;
      }
   }

   public String writeToString(Node var1) throws DOMException, LSException {
      Document var2 = var1.getNodeType() == 9 ? (Document)var1 : var1.getOwnerDocument();
      java.lang.reflect.Method var3 = null;
      Object var4 = null;
      String var5 = null;

      try {
         var3 = var2.getClass().getMethod("getXmlVersion");
         if (var3 != null) {
            var5 = (String)var3.invoke(var2, (Object[])null);
         }
      } catch (Exception var11) {
      }

      if (var5 != null && var5.equals("1.1")) {
         if (this.xml11Serializer == null) {
            this.xml11Serializer = new XML11Serializer();
            this.initSerializer(this.xml11Serializer);
         }

         this.copySettings(this.serializer, this.xml11Serializer);
         var4 = this.xml11Serializer;
      } else {
         var4 = this.serializer;
      }

      StringWriter var6 = new StringWriter();

      try {
         this.prepareForSerialization((XMLSerializer)var4, var1);
         ((BaseMarkupSerializer)var4)._format.setEncoding("UTF-16");
         ((BaseMarkupSerializer)var4).setOutputCharStream(var6);
         if (var1.getNodeType() == 9) {
            ((BaseMarkupSerializer)var4).serialize((Document)var1);
         } else if (var1.getNodeType() == 11) {
            ((BaseMarkupSerializer)var4).serialize((DocumentFragment)var1);
         } else {
            if (var1.getNodeType() != 1) {
               String var7 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unable-to-serialize-node", (Object[])null);
               if (((BaseMarkupSerializer)var4).fDOMErrorHandler != null) {
                  DOMErrorImpl var8 = new DOMErrorImpl();
                  var8.fType = "unable-to-serialize-node";
                  var8.fMessage = var7;
                  var8.fSeverity = 3;
                  ((BaseMarkupSerializer)var4).fDOMErrorHandler.handleError(var8);
               }

               throw new LSException((short)82, var7);
            }

            ((BaseMarkupSerializer)var4).serialize((Element)var1);
         }
      } catch (LSException var12) {
         throw var12;
      } catch (RuntimeException var13) {
         if (var13 == DOMNormalizer.abort) {
            return null;
         }

         throw new LSException((short)82, var13.toString());
      } catch (IOException var14) {
         String var10 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "STRING_TOO_LONG", new Object[]{var14.getMessage()});
         throw new DOMException((short)2, var10);
      }

      return var6.toString();
   }

   public void setNewLine(String var1) {
      this.serializer._format.setLineSeparator(var1);
   }

   public String getNewLine() {
      return this.serializer._format.getLineSeparator();
   }

   public LSSerializerFilter getFilter() {
      return this.serializer.fDOMFilter;
   }

   public void setFilter(LSSerializerFilter var1) {
      this.serializer.fDOMFilter = var1;
   }

   private void initSerializer(XMLSerializer var1) {
      var1.fNSBinder = new NamespaceSupport();
      var1.fLocalNSBinder = new NamespaceSupport();
      var1.fSymbolTable = new SymbolTable();
   }

   private void copySettings(XMLSerializer var1, XMLSerializer var2) {
      var2.fDOMErrorHandler = this.fErrorHandler;
      var2._format.setEncoding(var1._format.getEncoding());
      var2._format.setLineSeparator(var1._format.getLineSeparator());
      var2.fDOMFilter = var1.fDOMFilter;
   }

   public boolean write(Node var1, LSOutput var2) throws LSException {
      if (var1 == null) {
         return false;
      } else {
         java.lang.reflect.Method var3 = null;
         Object var4 = null;
         String var5 = null;
         Document var6 = var1.getNodeType() == 9 ? (Document)var1 : var1.getOwnerDocument();

         try {
            var3 = var6.getClass().getMethod("getXmlVersion");
            if (var3 != null) {
               var5 = (String)var3.invoke(var6, (Object[])null);
            }
         } catch (Exception var20) {
         }

         if (var5 != null && var5.equals("1.1")) {
            if (this.xml11Serializer == null) {
               this.xml11Serializer = new XML11Serializer();
               this.initSerializer(this.xml11Serializer);
            }

            this.copySettings(this.serializer, this.xml11Serializer);
            var4 = this.xml11Serializer;
         } else {
            var4 = this.serializer;
         }

         String var7 = null;
         if ((var7 = var2.getEncoding()) == null) {
            java.lang.reflect.Method var8;
            try {
               var8 = var6.getClass().getMethod("getInputEncoding");
               if (var8 != null) {
                  var7 = (String)var8.invoke(var6, (Object[])null);
               }
            } catch (Exception var19) {
            }

            if (var7 == null) {
               try {
                  var8 = var6.getClass().getMethod("getXmlEncoding");
                  if (var8 != null) {
                     var7 = (String)var8.invoke(var6, (Object[])null);
                  }
               } catch (Exception var18) {
               }

               if (var7 == null) {
                  var7 = "UTF-8";
               }
            }
         }

         DOMErrorImpl var12;
         try {
            this.prepareForSerialization((XMLSerializer)var4, var1);
            ((BaseMarkupSerializer)var4)._format.setEncoding(var7);
            OutputStream var25 = var2.getByteStream();
            Writer var26 = var2.getCharacterStream();
            String var10 = var2.getSystemId();
            if (var26 == null) {
               if (var25 == null) {
                  String var11;
                  if (var10 == null) {
                     var11 = DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "no-output-specified", (Object[])null);
                     if (((BaseMarkupSerializer)var4).fDOMErrorHandler != null) {
                        var12 = new DOMErrorImpl();
                        var12.fType = "no-output-specified";
                        var12.fMessage = var11;
                        var12.fSeverity = 3;
                        ((BaseMarkupSerializer)var4).fDOMErrorHandler.handleError(var12);
                     }

                     throw new LSException((short)82, var11);
                  }

                  var11 = XMLEntityManager.expandSystemId(var10, (String)null, true);
                  URL var27 = new URL(var11 != null ? var11 : var10);
                  Object var13 = null;
                  String var14 = var27.getProtocol();
                  String var15 = var27.getHost();
                  if (var14.equals("file") && (var15 == null || var15.length() == 0 || var15.equals("localhost"))) {
                     var13 = new FileOutputStream(this.getPathWithoutEscapes(var27.getFile()));
                  } else {
                     URLConnection var16 = var27.openConnection();
                     var16.setDoInput(false);
                     var16.setDoOutput(true);
                     var16.setUseCaches(false);
                     if (var16 instanceof HttpURLConnection) {
                        HttpURLConnection var17 = (HttpURLConnection)var16;
                        var17.setRequestMethod("PUT");
                     }

                     var13 = var16.getOutputStream();
                  }

                  ((BaseMarkupSerializer)var4).setOutputByteStream((OutputStream)var13);
               } else {
                  ((BaseMarkupSerializer)var4).setOutputByteStream(var25);
               }
            } else {
               ((BaseMarkupSerializer)var4).setOutputCharStream(var26);
            }

            if (var1.getNodeType() == 9) {
               ((BaseMarkupSerializer)var4).serialize((Document)var1);
            } else if (var1.getNodeType() == 11) {
               ((BaseMarkupSerializer)var4).serialize((DocumentFragment)var1);
            } else {
               if (var1.getNodeType() != 1) {
                  return false;
               }

               ((BaseMarkupSerializer)var4).serialize((Element)var1);
            }

            return true;
         } catch (UnsupportedEncodingException var21) {
            if (((BaseMarkupSerializer)var4).fDOMErrorHandler != null) {
               DOMErrorImpl var9 = new DOMErrorImpl();
               var9.fException = var21;
               var9.fType = "unsupported-encoding";
               var9.fMessage = var21.getMessage();
               var9.fSeverity = 3;
               ((BaseMarkupSerializer)var4).fDOMErrorHandler.handleError(var9);
            }

            throw new LSException((short)82, DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "unsupported-encoding", (Object[])null));
         } catch (LSException var22) {
            throw var22;
         } catch (RuntimeException var23) {
            if (var23 == DOMNormalizer.abort) {
               return false;
            } else {
               throw new LSException((short)82, var23.toString());
            }
         } catch (Exception var24) {
            if (((BaseMarkupSerializer)var4).fDOMErrorHandler != null) {
               var12 = new DOMErrorImpl();
               var12.fException = var24;
               var12.fMessage = var24.getMessage();
               var12.fSeverity = 2;
               ((BaseMarkupSerializer)var4).fDOMErrorHandler.handleError(var12);
            }

            var24.printStackTrace();
            throw new LSException((short)82, var24.toString());
         }
      }
   }

   public boolean writeToURI(Node var1, String var2) throws LSException {
      if (var1 == null) {
         return false;
      } else {
         java.lang.reflect.Method var3 = null;
         Object var4 = null;
         String var5 = null;
         String var6 = null;
         Document var7 = var1.getNodeType() == 9 ? (Document)var1 : var1.getOwnerDocument();

         try {
            var3 = var7.getClass().getMethod("getXmlVersion");
            if (var3 != null) {
               var5 = (String)var3.invoke(var7, (Object[])null);
            }
         } catch (Exception var17) {
         }

         if (var5 != null && var5.equals("1.1")) {
            if (this.xml11Serializer == null) {
               this.xml11Serializer = new XML11Serializer();
               this.initSerializer(this.xml11Serializer);
            }

            this.copySettings(this.serializer, this.xml11Serializer);
            var4 = this.xml11Serializer;
         } else {
            var4 = this.serializer;
         }

         java.lang.reflect.Method var8;
         try {
            var8 = var7.getClass().getMethod("getInputEncoding");
            if (var8 != null) {
               var6 = (String)var8.invoke(var7, (Object[])null);
            }
         } catch (Exception var16) {
         }

         if (var6 == null) {
            try {
               var8 = var7.getClass().getMethod("getXmlEncoding");
               if (var8 != null) {
                  var6 = (String)var8.invoke(var7, (Object[])null);
               }
            } catch (Exception var15) {
            }

            if (var6 == null) {
               var6 = "UTF-8";
            }
         }

         try {
            this.prepareForSerialization((XMLSerializer)var4, var1);
            ((BaseMarkupSerializer)var4)._format.setEncoding(var6);
            String var21 = XMLEntityManager.expandSystemId(var2, (String)null, true);
            URL var9 = new URL(var21 != null ? var21 : var2);
            Object var10 = null;
            String var22 = var9.getProtocol();
            String var12 = var9.getHost();
            if (!var22.equals("file") || var12 != null && var12.length() != 0 && !var12.equals("localhost")) {
               URLConnection var13 = var9.openConnection();
               var13.setDoInput(false);
               var13.setDoOutput(true);
               var13.setUseCaches(false);
               if (var13 instanceof HttpURLConnection) {
                  HttpURLConnection var14 = (HttpURLConnection)var13;
                  var14.setRequestMethod("PUT");
               }

               var10 = var13.getOutputStream();
            } else {
               var10 = new FileOutputStream(this.getPathWithoutEscapes(var9.getFile()));
            }

            ((BaseMarkupSerializer)var4).setOutputByteStream((OutputStream)var10);
            if (var1.getNodeType() == 9) {
               ((BaseMarkupSerializer)var4).serialize((Document)var1);
            } else if (var1.getNodeType() == 11) {
               ((BaseMarkupSerializer)var4).serialize((DocumentFragment)var1);
            } else {
               if (var1.getNodeType() != 1) {
                  return false;
               }

               ((BaseMarkupSerializer)var4).serialize((Element)var1);
            }

            return true;
         } catch (LSException var18) {
            throw var18;
         } catch (RuntimeException var19) {
            if (var19 == DOMNormalizer.abort) {
               return false;
            } else {
               throw new LSException((short)82, var19.toString());
            }
         } catch (Exception var20) {
            if (((BaseMarkupSerializer)var4).fDOMErrorHandler != null) {
               DOMErrorImpl var11 = new DOMErrorImpl();
               var11.fException = var20;
               var11.fMessage = var20.getMessage();
               var11.fSeverity = 2;
               ((BaseMarkupSerializer)var4).fDOMErrorHandler.handleError(var11);
            }

            throw new LSException((short)82, var20.toString());
         }
      }
   }

   private void prepareForSerialization(XMLSerializer var1, Node var2) {
      var1.reset();
      var1.features = this.features;
      var1.fDOMErrorHandler = this.fErrorHandler;
      var1.fNamespaces = (this.features & 1) != 0;
      var1.fNamespacePrefixes = (this.features & 512) != 0;
      var1._format.setOmitComments((this.features & 32) == 0);
      var1._format.setOmitXMLDeclaration((this.features & 256) == 0);
      if ((this.features & 2) != 0) {
         Node var4 = var2;
         boolean var6 = true;
         Document var7 = var2.getNodeType() == 9 ? (Document)var2 : var2.getOwnerDocument();

         try {
            java.lang.reflect.Method var5 = var7.getClass().getMethod("isXMLVersionChanged()");
            if (var5 != null) {
               var6 = (Boolean)var5.invoke(var7, (Object[])null);
            }
         } catch (Exception var9) {
         }

         Node var3;
         if (var2.getFirstChild() == null) {
            this.verify(var2, var6, false);
         } else {
            for(; var2 != null; var2 = var3) {
               this.verify(var2, var6, false);
               var3 = var2.getFirstChild();

               while(var3 == null) {
                  var3 = var2.getNextSibling();
                  if (var3 == null) {
                     var2 = var2.getParentNode();
                     if (var4 == var2) {
                        var3 = null;
                        break;
                     }

                     var3 = var2.getNextSibling();
                  }
               }
            }
         }
      }

   }

   private void verify(Node var1, boolean var2, boolean var3) {
      short var4 = var1.getNodeType();
      this.fLocator.fRelatedNode = var1;
      boolean var5;
      switch (var4) {
         case 1:
            if (var2) {
               if ((this.features & 1) != 0) {
                  var5 = CoreDocumentImpl.isValidQName(var1.getPrefix(), var1.getLocalName(), var3);
               } else {
                  var5 = CoreDocumentImpl.isXMLName(var1.getNodeName(), var3);
               }

               if (!var5 && !var5 && this.fErrorHandler != null) {
                  String var10 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", var1.getNodeName()});
                  DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, var10, (short)3, "wf-invalid-character-in-node-name");
               }
            }

            NamedNodeMap var11 = var1.hasAttributes() ? var1.getAttributes() : null;
            if (var11 != null) {
               for(int var12 = 0; var12 < var11.getLength(); ++var12) {
                  Attr var13 = (Attr)var11.item(var12);
                  this.fLocator.fRelatedNode = var13;
                  DOMNormalizer.isAttrValueWF(this.fErrorHandler, this.fError, this.fLocator, var11, var13, var13.getValue(), var3);
                  if (var2) {
                     var5 = CoreDocumentImpl.isXMLName(var13.getNodeName(), var3);
                     if (!var5) {
                        String var9 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Attr", var1.getNodeName()});
                        DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, var9, (short)3, "wf-invalid-character-in-node-name");
                     }
                  }
               }
            }
         case 2:
         case 6:
         case 9:
         case 10:
         default:
            break;
         case 3:
            DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, var1.getNodeValue(), var3);
            break;
         case 4:
            DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, var1.getNodeValue(), var3);
            break;
         case 5:
            if (var2 && (this.features & 4) != 0) {
               CoreDocumentImpl.isXMLName(var1.getNodeName(), var3);
            }
            break;
         case 7:
            ProcessingInstruction var6 = (ProcessingInstruction)var1;
            String var7 = var6.getTarget();
            if (var2) {
               if (var3) {
                  var5 = XML11Char.isXML11ValidName(var7);
               } else {
                  var5 = XMLChar.isValidName(var7);
               }

               if (!var5) {
                  String var8 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "wf-invalid-character-in-node-name", new Object[]{"Element", var1.getNodeName()});
                  DOMNormalizer.reportDOMError(this.fErrorHandler, this.fError, this.fLocator, var8, (short)3, "wf-invalid-character-in-node-name");
               }
            }

            DOMNormalizer.isXMLCharWF(this.fErrorHandler, this.fError, this.fLocator, var6.getData(), var3);
            break;
         case 8:
            if ((this.features & 32) != 0) {
               DOMNormalizer.isCommentWF(this.fErrorHandler, this.fError, this.fLocator, ((Comment)var1).getData(), var3);
            }
      }

   }

   private String getPathWithoutEscapes(String var1) {
      if (var1 != null && var1.length() != 0 && var1.indexOf(37) != -1) {
         StringTokenizer var2 = new StringTokenizer(var1, "%");
         StringBuffer var3 = new StringBuffer(var1.length());
         int var4 = var2.countTokens();
         var3.append(var2.nextToken());

         for(int var5 = 1; var5 < var4; ++var5) {
            String var6 = var2.nextToken();
            var3.append((char)Integer.valueOf(var6.substring(0, 2), 16));
            var3.append(var6.substring(2));
         }

         return var3.toString();
      } else {
         return var1;
      }
   }
}
