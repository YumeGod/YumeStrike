package org.apache.xml.dtm.ref;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.ext.LexicalHandler;

public class IncrementalSAXSource_Xerces implements IncrementalSAXSource {
   Method fParseSomeSetup = null;
   Method fParseSome = null;
   Object fPullParserConfig = null;
   Method fConfigSetInput = null;
   Method fConfigParse = null;
   Method fSetInputSource = null;
   Constructor fConfigInputSourceCtor = null;
   Method fConfigSetByteStream = null;
   Method fConfigSetCharStream = null;
   Method fConfigSetEncoding = null;
   Method fReset = null;
   SAXParser fIncrementalParser;
   private boolean fParseInProgress = false;
   private static final Object[] noparms = new Object[0];
   private static final Object[] parmsfalse;
   // $FF: synthetic field
   static Class class$org$apache$xerces$parsers$SAXParser;
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$java$io$InputStream;
   // $FF: synthetic field
   static Class class$java$io$Reader;
   // $FF: synthetic field
   static Class class$org$xml$sax$InputSource;

   public IncrementalSAXSource_Xerces() throws NoSuchMethodException {
      try {
         Class xniConfigClass = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLParserConfiguration", ObjectFactory.findClassLoader(), true);
         Class[] args1 = new Class[]{xniConfigClass};
         Constructor ctor = (class$org$apache$xerces$parsers$SAXParser == null ? (class$org$apache$xerces$parsers$SAXParser = class$("org.apache.xerces.parsers.SAXParser")) : class$org$apache$xerces$parsers$SAXParser).getConstructor(args1);
         Class xniStdConfigClass = ObjectFactory.findProviderClass("org.apache.xerces.parsers.StandardParserConfiguration", ObjectFactory.findClassLoader(), true);
         this.fPullParserConfig = xniStdConfigClass.newInstance();
         Object[] args2 = new Object[]{this.fPullParserConfig};
         this.fIncrementalParser = (SAXParser)ctor.newInstance(args2);
         Class fXniInputSourceClass = ObjectFactory.findProviderClass("org.apache.xerces.xni.parser.XMLInputSource", ObjectFactory.findClassLoader(), true);
         Class[] args3 = new Class[]{fXniInputSourceClass};
         this.fConfigSetInput = xniStdConfigClass.getMethod("setInputSource", args3);
         Class[] args4 = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
         this.fConfigInputSourceCtor = fXniInputSourceClass.getConstructor(args4);
         Class[] args5 = new Class[]{class$java$io$InputStream == null ? (class$java$io$InputStream = class$("java.io.InputStream")) : class$java$io$InputStream};
         this.fConfigSetByteStream = fXniInputSourceClass.getMethod("setByteStream", args5);
         Class[] args6 = new Class[]{class$java$io$Reader == null ? (class$java$io$Reader = class$("java.io.Reader")) : class$java$io$Reader};
         this.fConfigSetCharStream = fXniInputSourceClass.getMethod("setCharacterStream", args6);
         Class[] args7 = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
         this.fConfigSetEncoding = fXniInputSourceClass.getMethod("setEncoding", args7);
         Class[] argsb = new Class[]{Boolean.TYPE};
         this.fConfigParse = xniStdConfigClass.getMethod("parse", argsb);
         Class[] noargs = new Class[0];
         this.fReset = this.fIncrementalParser.getClass().getMethod("reset", noargs);
      } catch (Exception var14) {
         IncrementalSAXSource_Xerces dummy = new IncrementalSAXSource_Xerces(new SAXParser());
         this.fParseSomeSetup = dummy.fParseSomeSetup;
         this.fParseSome = dummy.fParseSome;
         this.fIncrementalParser = dummy.fIncrementalParser;
      }

   }

   public IncrementalSAXSource_Xerces(SAXParser parser) throws NoSuchMethodException {
      this.fIncrementalParser = parser;
      Class me = parser.getClass();
      Class[] parms = new Class[]{class$org$xml$sax$InputSource == null ? (class$org$xml$sax$InputSource = class$("org.xml.sax.InputSource")) : class$org$xml$sax$InputSource};
      this.fParseSomeSetup = me.getMethod("parseSomeSetup", parms);
      parms = new Class[0];
      this.fParseSome = me.getMethod("parseSome", parms);
   }

   public static IncrementalSAXSource createIncrementalSAXSource() {
      try {
         return new IncrementalSAXSource_Xerces();
      } catch (NoSuchMethodException var2) {
         IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
         iss.setXMLReader(new SAXParser());
         return iss;
      }
   }

   public static IncrementalSAXSource createIncrementalSAXSource(SAXParser parser) {
      try {
         return new IncrementalSAXSource_Xerces(parser);
      } catch (NoSuchMethodException var3) {
         IncrementalSAXSource_Filter iss = new IncrementalSAXSource_Filter();
         iss.setXMLReader(parser);
         return iss;
      }
   }

   public void setContentHandler(ContentHandler handler) {
      this.fIncrementalParser.setContentHandler(handler);
   }

   public void setLexicalHandler(LexicalHandler handler) {
      try {
         this.fIncrementalParser.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
      } catch (SAXNotRecognizedException var4) {
      } catch (SAXNotSupportedException var5) {
      }

   }

   public void setDTDHandler(DTDHandler handler) {
      this.fIncrementalParser.setDTDHandler(handler);
   }

   public void startParse(InputSource source) throws SAXException {
      if (this.fIncrementalParser == null) {
         throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_NEEDS_SAXPARSER", (Object[])null));
      } else if (this.fParseInProgress) {
         throw new SAXException(XMLMessages.createXMLMessage("ER_STARTPARSE_WHILE_PARSING", (Object[])null));
      } else {
         boolean ok = false;

         try {
            ok = this.parseSomeSetup(source);
         } catch (Exception var4) {
            throw new SAXException(var4);
         }

         if (!ok) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_COULD_NOT_INIT_PARSER", (Object[])null));
         }
      }
   }

   public Object deliverMoreNodes(boolean parsemore) {
      if (!parsemore) {
         this.fParseInProgress = false;
         return Boolean.FALSE;
      } else {
         Object arg;
         try {
            boolean keepgoing = this.parseSome();
            arg = keepgoing ? Boolean.TRUE : Boolean.FALSE;
         } catch (SAXException var6) {
            arg = var6;
         } catch (IOException var7) {
            arg = var7;
         } catch (Exception var8) {
            arg = new SAXException(var8);
         }

         return arg;
      }
   }

   private boolean parseSomeSetup(InputSource source) throws SAXException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
      Object[] parm;
      Object ret;
      if (this.fConfigSetInput != null) {
         parm = new Object[]{source.getPublicId(), source.getSystemId(), null};
         ret = this.fConfigInputSourceCtor.newInstance(parm);
         Object[] parmsa = new Object[]{source.getByteStream()};
         this.fConfigSetByteStream.invoke(ret, parmsa);
         parmsa[0] = source.getCharacterStream();
         this.fConfigSetCharStream.invoke(ret, parmsa);
         parmsa[0] = source.getEncoding();
         this.fConfigSetEncoding.invoke(ret, parmsa);
         Object[] noparms = new Object[0];
         this.fReset.invoke(this.fIncrementalParser, noparms);
         parmsa[0] = ret;
         this.fConfigSetInput.invoke(this.fPullParserConfig, parmsa);
         return this.parseSome();
      } else {
         parm = new Object[]{source};
         ret = this.fParseSomeSetup.invoke(this.fIncrementalParser, parm);
         return (Boolean)ret;
      }
   }

   private boolean parseSome() throws SAXException, IOException, IllegalAccessException, InvocationTargetException {
      if (this.fConfigSetInput != null) {
         Object ret = (Boolean)this.fConfigParse.invoke(this.fPullParserConfig, parmsfalse);
         return (Boolean)ret;
      } else {
         Object ret = this.fParseSome.invoke(this.fIncrementalParser, noparms);
         return (Boolean)ret;
      }
   }

   public static void main(String[] args) {
      System.out.println("Starting...");
      CoroutineManager co = new CoroutineManager();
      int appCoroutineID = co.co_joinCoroutineSet(-1);
      if (appCoroutineID == -1) {
         System.out.println("ERROR: Couldn't allocate coroutine number.\n");
      } else {
         IncrementalSAXSource parser = createIncrementalSAXSource();
         XMLSerializer trace = new XMLSerializer(System.out, (OutputFormat)null);
         parser.setContentHandler(trace);
         parser.setLexicalHandler(trace);

         for(int arg = 0; arg < args.length; ++arg) {
            try {
               InputSource source = new InputSource(args[arg]);
               Object result = null;
               boolean more = true;
               parser.startParse(source);

               for(result = parser.deliverMoreNodes(more); result == Boolean.TRUE; result = parser.deliverMoreNodes(more)) {
                  System.out.println("\nSome parsing successful, trying more.\n");
                  if (arg + 1 < args.length && "!".equals(args[arg + 1])) {
                     ++arg;
                     more = false;
                  }
               }

               if (result instanceof Boolean && (Boolean)result == Boolean.FALSE) {
                  System.out.println("\nParser ended (EOF or on request).\n");
               } else if (result == null) {
                  System.out.println("\nUNEXPECTED: Parser says shut down prematurely.\n");
               } else if (result instanceof Exception) {
                  throw new WrappedRuntimeException((Exception)result);
               }
            } catch (SAXException var9) {
               var9.printStackTrace();
            }
         }

      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      parmsfalse = new Object[]{Boolean.FALSE};
   }
}
