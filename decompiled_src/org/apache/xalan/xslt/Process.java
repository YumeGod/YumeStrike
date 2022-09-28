package org.apache.xalan.xslt;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.Version;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.trace.PrintTraceListener;
import org.apache.xalan.trace.TraceManager;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.res.XMLMessages;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.WrappedRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Process {
   protected static void printArgOptions(ResourceBundle resbundle) {
      System.out.println(resbundle.getString("xslProc_option"));
      System.out.println("\n\t\t\t" + resbundle.getString("xslProc_common_options") + "\n");
      System.out.println(resbundle.getString("optionXSLTC"));
      System.out.println(resbundle.getString("optionIN"));
      System.out.println(resbundle.getString("optionXSL"));
      System.out.println(resbundle.getString("optionOUT"));
      System.out.println(resbundle.getString("optionV"));
      System.out.println(resbundle.getString("optionEDUMP"));
      System.out.println(resbundle.getString("optionXML"));
      System.out.println(resbundle.getString("optionTEXT"));
      System.out.println(resbundle.getString("optionHTML"));
      System.out.println(resbundle.getString("optionPARAM"));
      System.out.println(resbundle.getString("optionMEDIA"));
      System.out.println(resbundle.getString("optionFLAVOR"));
      System.out.println(resbundle.getString("optionDIAG"));
      System.out.println(resbundle.getString("optionURIRESOLVER"));
      System.out.println(resbundle.getString("optionENTITYRESOLVER"));
      waitForReturnKey(resbundle);
      System.out.println(resbundle.getString("optionCONTENTHANDLER"));
      System.out.println(resbundle.getString("optionSECUREPROCESSING"));
      System.out.println("\n\t\t\t" + resbundle.getString("xslProc_xalan_options") + "\n");
      System.out.println(resbundle.getString("optionQC"));
      System.out.println(resbundle.getString("optionTT"));
      System.out.println(resbundle.getString("optionTG"));
      System.out.println(resbundle.getString("optionTS"));
      System.out.println(resbundle.getString("optionTTC"));
      System.out.println(resbundle.getString("optionTCLASS"));
      System.out.println(resbundle.getString("optionLINENUMBERS"));
      System.out.println(resbundle.getString("optionINCREMENTAL"));
      System.out.println(resbundle.getString("optionNOOPTIMIMIZE"));
      System.out.println(resbundle.getString("optionRL"));
      System.out.println("\n\t\t\t" + resbundle.getString("xslProc_xsltc_options") + "\n");
      System.out.println(resbundle.getString("optionXO"));
      waitForReturnKey(resbundle);
      System.out.println(resbundle.getString("optionXD"));
      System.out.println(resbundle.getString("optionXJ"));
      System.out.println(resbundle.getString("optionXP"));
      System.out.println(resbundle.getString("optionXN"));
      System.out.println(resbundle.getString("optionXX"));
      System.out.println(resbundle.getString("optionXT"));
   }

   public static void main(String[] argv) {
      boolean doStackDumpOnError = false;
      boolean setQuietMode = false;
      boolean doDiag = false;
      String msg = null;
      boolean isSecureProcessing = false;
      PrintWriter diagnosticsWriter = new PrintWriter(System.err, true);
      PrintWriter dumpWriter = diagnosticsWriter;
      ResourceBundle resbundle = XMLMessages.loadResourceBundle("org.apache.xalan.res.XSLTErrorResources");
      String flavor = "s2s";
      if (argv.length < 1) {
         printArgOptions(resbundle);
      } else {
         boolean useXSLTC = false;

         for(int i = 0; i < argv.length; ++i) {
            if ("-XSLTC".equalsIgnoreCase(argv[i])) {
               useXSLTC = true;
            }
         }

         if (useXSLTC) {
            String key = "javax.xml.transform.TransformerFactory";
            String value = "org.apache.xalan.xsltc.trax.TransformerFactoryImpl";
            Properties props = System.getProperties();
            props.put(key, value);
            System.setProperties(props);
         }

         TransformerFactory tfactory;
         try {
            tfactory = TransformerFactory.newInstance();
            tfactory.setErrorListener(new DefaultErrorHandler());
         } catch (TransformerFactoryConfigurationError var67) {
            var67.printStackTrace(diagnosticsWriter);
            msg = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", (Object[])null);
            diagnosticsWriter.println(msg);
            tfactory = null;
            doExit(msg);
         }

         boolean formatOutput = false;
         boolean useSourceLocation = false;
         String inFileName = null;
         String outFileName = null;
         String dumpFileName = null;
         String xslFileName = null;
         String treedumpFileName = null;
         PrintTraceListener tracer = null;
         String outputType = null;
         String media = null;
         Vector params = new Vector();
         boolean quietConflictWarnings = false;
         URIResolver uriResolver = null;
         EntityResolver entityResolver = null;
         ContentHandler contentHandler = null;
         int recursionLimit = -1;

         for(int i = 0; i < argv.length; ++i) {
            if (!"-XSLTC".equalsIgnoreCase(argv[i])) {
               if ("-TT".equalsIgnoreCase(argv[i])) {
                  if (!useXSLTC) {
                     if (null == tracer) {
                        tracer = new PrintTraceListener(diagnosticsWriter);
                     }

                     tracer.m_traceTemplates = true;
                  } else {
                     printInvalidXSLTCOption("-TT");
                  }
               } else if ("-TG".equalsIgnoreCase(argv[i])) {
                  if (!useXSLTC) {
                     if (null == tracer) {
                        tracer = new PrintTraceListener(diagnosticsWriter);
                     }

                     tracer.m_traceGeneration = true;
                  } else {
                     printInvalidXSLTCOption("-TG");
                  }
               } else if ("-TS".equalsIgnoreCase(argv[i])) {
                  if (!useXSLTC) {
                     if (null == tracer) {
                        tracer = new PrintTraceListener(diagnosticsWriter);
                     }

                     tracer.m_traceSelection = true;
                  } else {
                     printInvalidXSLTCOption("-TS");
                  }
               } else if ("-TTC".equalsIgnoreCase(argv[i])) {
                  if (!useXSLTC) {
                     if (null == tracer) {
                        tracer = new PrintTraceListener(diagnosticsWriter);
                     }

                     tracer.m_traceElements = true;
                  } else {
                     printInvalidXSLTCOption("-TTC");
                  }
               } else if ("-INDENT".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                     ++i;
                     int var73 = Integer.parseInt(argv[i]);
                  } else {
                     boolean var72 = false;
                  }
               } else if ("-IN".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                     ++i;
                     inFileName = argv[i];
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-IN"}));
                  }
               } else if ("-MEDIA".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length) {
                     ++i;
                     media = argv[i];
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-MEDIA"}));
                  }
               } else if ("-OUT".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                     ++i;
                     outFileName = argv[i];
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-OUT"}));
                  }
               } else if ("-XSL".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                     ++i;
                     xslFileName = argv[i];
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XSL"}));
                  }
               } else if ("-FLAVOR".equalsIgnoreCase(argv[i])) {
                  if (i + 1 < argv.length) {
                     ++i;
                     flavor = argv[i];
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-FLAVOR"}));
                  }
               } else if ("-PARAM".equalsIgnoreCase(argv[i])) {
                  if (i + 2 < argv.length) {
                     ++i;
                     String name = argv[i];
                     params.addElement(name);
                     ++i;
                     String expression = argv[i];
                     params.addElement(expression);
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-PARAM"}));
                  }
               } else if (!"-E".equalsIgnoreCase(argv[i])) {
                  if ("-V".equalsIgnoreCase(argv[i])) {
                     diagnosticsWriter.println(resbundle.getString("version") + Version.getVersion() + ", " + resbundle.getString("version2"));
                  } else if ("-QC".equalsIgnoreCase(argv[i])) {
                     if (!useXSLTC) {
                        quietConflictWarnings = true;
                     } else {
                        printInvalidXSLTCOption("-QC");
                     }
                  } else if ("-Q".equalsIgnoreCase(argv[i])) {
                     setQuietMode = true;
                  } else if ("-DIAG".equalsIgnoreCase(argv[i])) {
                     doDiag = true;
                  } else if ("-XML".equalsIgnoreCase(argv[i])) {
                     outputType = "xml";
                  } else if ("-TEXT".equalsIgnoreCase(argv[i])) {
                     outputType = "text";
                  } else if ("-HTML".equalsIgnoreCase(argv[i])) {
                     outputType = "html";
                  } else if ("-EDUMP".equalsIgnoreCase(argv[i])) {
                     doStackDumpOnError = true;
                     if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                        ++i;
                        dumpFileName = argv[i];
                     }
                  } else if ("-URIRESOLVER".equalsIgnoreCase(argv[i])) {
                     if (i + 1 < argv.length) {
                        try {
                           ++i;
                           uriResolver = (URIResolver)ObjectFactory.newInstance(argv[i], ObjectFactory.findClassLoader(), true);
                           tfactory.setURIResolver(uriResolver);
                        } catch (ObjectFactory.ConfigurationError var66) {
                           msg = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-URIResolver"});
                           System.err.println(msg);
                           doExit(msg);
                        }
                     } else {
                        msg = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-URIResolver"});
                        System.err.println(msg);
                        doExit(msg);
                     }
                  } else if ("-ENTITYRESOLVER".equalsIgnoreCase(argv[i])) {
                     if (i + 1 < argv.length) {
                        try {
                           ++i;
                           entityResolver = (EntityResolver)ObjectFactory.newInstance(argv[i], ObjectFactory.findClassLoader(), true);
                        } catch (ObjectFactory.ConfigurationError var65) {
                           msg = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-EntityResolver"});
                           System.err.println(msg);
                           doExit(msg);
                        }
                     } else {
                        msg = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-EntityResolver"});
                        System.err.println(msg);
                        doExit(msg);
                     }
                  } else if ("-CONTENTHANDLER".equalsIgnoreCase(argv[i])) {
                     if (i + 1 < argv.length) {
                        try {
                           ++i;
                           contentHandler = (ContentHandler)ObjectFactory.newInstance(argv[i], ObjectFactory.findClassLoader(), true);
                        } catch (ObjectFactory.ConfigurationError var64) {
                           msg = XSLMessages.createMessage("ER_CLASS_NOT_FOUND_FOR_OPTION", new Object[]{"-ContentHandler"});
                           System.err.println(msg);
                           doExit(msg);
                        }
                     } else {
                        msg = XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-ContentHandler"});
                        System.err.println(msg);
                        doExit(msg);
                     }
                  } else if ("-L".equalsIgnoreCase(argv[i])) {
                     if (!useXSLTC) {
                        tfactory.setAttribute("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
                     } else {
                        printInvalidXSLTCOption("-L");
                     }
                  } else if ("-INCREMENTAL".equalsIgnoreCase(argv[i])) {
                     if (!useXSLTC) {
                        tfactory.setAttribute("http://xml.apache.org/xalan/features/incremental", Boolean.TRUE);
                     } else {
                        printInvalidXSLTCOption("-INCREMENTAL");
                     }
                  } else if ("-NOOPTIMIZE".equalsIgnoreCase(argv[i])) {
                     if (!useXSLTC) {
                        tfactory.setAttribute("http://xml.apache.org/xalan/features/optimize", Boolean.FALSE);
                     } else {
                        printInvalidXSLTCOption("-NOOPTIMIZE");
                     }
                  } else if ("-RL".equalsIgnoreCase(argv[i])) {
                     if (!useXSLTC) {
                        if (i + 1 < argv.length) {
                           ++i;
                           recursionLimit = Integer.parseInt(argv[i]);
                        } else {
                           System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-rl"}));
                        }
                     } else {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                        }

                        printInvalidXSLTCOption("-RL");
                     }
                  } else if ("-XO".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           tfactory.setAttribute("generate-translet", "true");
                           ++i;
                           tfactory.setAttribute("translet-name", argv[i]);
                        } else {
                           tfactory.setAttribute("generate-translet", "true");
                        }
                     } else {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                        }

                        printInvalidXalanOption("-XO");
                     }
                  } else if ("-XD".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                           tfactory.setAttribute("destination-directory", argv[i]);
                        } else {
                           System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XD"}));
                        }
                     } else {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                        }

                        printInvalidXalanOption("-XD");
                     }
                  } else if ("-XJ".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           tfactory.setAttribute("generate-translet", "true");
                           ++i;
                           tfactory.setAttribute("jar-name", argv[i]);
                        } else {
                           System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XJ"}));
                        }
                     } else {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                        }

                        printInvalidXalanOption("-XJ");
                     }
                  } else if ("-XP".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                           tfactory.setAttribute("package-name", argv[i]);
                        } else {
                           System.err.println(XSLMessages.createMessage("ER_MISSING_ARG_FOR_OPTION", new Object[]{"-XP"}));
                        }
                     } else {
                        if (i + 1 < argv.length && argv[i + 1].charAt(0) != '-') {
                           ++i;
                        }

                        printInvalidXalanOption("-XP");
                     }
                  } else if ("-XN".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        tfactory.setAttribute("enable-inlining", "true");
                     } else {
                        printInvalidXalanOption("-XN");
                     }
                  } else if ("-XX".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        tfactory.setAttribute("debug", "true");
                     } else {
                        printInvalidXalanOption("-XX");
                     }
                  } else if ("-XT".equalsIgnoreCase(argv[i])) {
                     if (useXSLTC) {
                        tfactory.setAttribute("auto-translet", "true");
                     } else {
                        printInvalidXalanOption("-XT");
                     }
                  } else if ("-SECURE".equalsIgnoreCase(argv[i])) {
                     isSecureProcessing = true;

                     try {
                        tfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                     } catch (TransformerConfigurationException var63) {
                     }
                  } else {
                     System.err.println(XSLMessages.createMessage("ER_INVALID_OPTION", new Object[]{argv[i]}));
                  }
               }
            }
         }

         if (inFileName == null && xslFileName == null) {
            msg = resbundle.getString("xslProc_no_input");
            System.err.println(msg);
            doExit(msg);
         }

         try {
            long start = System.currentTimeMillis();
            if (null != dumpFileName) {
               dumpWriter = new PrintWriter(new FileWriter(dumpFileName));
            }

            Templates stylesheet = null;
            if (null != xslFileName) {
               if (flavor.equals("d2d")) {
                  DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                  dfactory.setNamespaceAware(true);
                  if (isSecureProcessing) {
                     try {
                        dfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                     } catch (ParserConfigurationException var62) {
                     }
                  }

                  DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
                  Node xslDOM = docBuilder.parse(new InputSource(xslFileName));
                  stylesheet = tfactory.newTemplates(new DOMSource(xslDOM, xslFileName));
               } else {
                  stylesheet = tfactory.newTemplates(new StreamSource(xslFileName));
               }
            }

            StreamResult strResult;
            if (null != outFileName) {
               strResult = new StreamResult(new FileOutputStream(outFileName));
               strResult.setSystemId(outFileName);
            } else {
               strResult = new StreamResult(System.out);
            }

            SAXTransformerFactory stf = (SAXTransformerFactory)tfactory;
            if (!useXSLTC && useSourceLocation) {
               stf.setAttribute("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
            }

            if (null == stylesheet) {
               Source source = stf.getAssociatedStylesheet(new StreamSource(inFileName), media, (String)null, (String)null);
               if (null == source) {
                  if (null != media) {
                     throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_IN_MEDIA", new Object[]{inFileName, media}));
                  }

                  throw new TransformerException(XSLMessages.createMessage("ER_NO_STYLESHEET_PI", new Object[]{inFileName}));
               }

               stylesheet = tfactory.newTemplates(source);
            }

            if (null == stylesheet) {
               msg = XSLMessages.createMessage("ER_NOT_SUCCESSFUL", (Object[])null);
               diagnosticsWriter.println(msg);
               doExit(msg);
            } else {
               Transformer transformer = flavor.equals("th") ? null : stylesheet.newTransformer();
               transformer.setErrorListener(new DefaultErrorHandler());
               if (null != outputType) {
                  transformer.setOutputProperty("method", outputType);
               }

               if (transformer instanceof TransformerImpl) {
                  TransformerImpl impl = (TransformerImpl)transformer;
                  TraceManager tm = impl.getTraceManager();
                  if (null != tracer) {
                     tm.addTraceListener(tracer);
                  }

                  impl.setQuietConflictWarnings(quietConflictWarnings);
                  if (useSourceLocation) {
                     impl.setProperty("http://xml.apache.org/xalan/properties/source-location", Boolean.TRUE);
                  }

                  if (recursionLimit > 0) {
                     impl.setRecursionLimit(recursionLimit);
                  }
               }

               int nParams = params.size();

               for(int i = 0; i < nParams; i += 2) {
                  transformer.setParameter((String)params.elementAt(i), (String)params.elementAt(i + 1));
               }

               if (uriResolver != null) {
                  transformer.setURIResolver(uriResolver);
               }

               if (null != inFileName) {
                  if (flavor.equals("d2d")) {
                     DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
                     dfactory.setCoalescing(true);
                     dfactory.setNamespaceAware(true);
                     if (isSecureProcessing) {
                        try {
                           dfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                        } catch (ParserConfigurationException var61) {
                        }
                     }

                     DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
                     if (entityResolver != null) {
                        docBuilder.setEntityResolver(entityResolver);
                     }

                     Node xmlDoc = docBuilder.parse(new InputSource(inFileName));
                     Document doc = docBuilder.newDocument();
                     DocumentFragment outNode = doc.createDocumentFragment();
                     transformer.transform(new DOMSource(xmlDoc, inFileName), new DOMResult(outNode));
                     Transformer serializer = stf.newTransformer();
                     serializer.setErrorListener(new DefaultErrorHandler());
                     Properties serializationProps = stylesheet.getOutputProperties();
                     serializer.setOutputProperties(serializationProps);
                     if (contentHandler != null) {
                        SAXResult result = new SAXResult(contentHandler);
                        serializer.transform(new DOMSource(outNode), result);
                     } else {
                        serializer.transform(new DOMSource(outNode), strResult);
                     }
                  } else if (flavor.equals("th")) {
                     for(int i = 0; i < 1; ++i) {
                        XMLReader reader = null;

                        try {
                           SAXParserFactory factory = SAXParserFactory.newInstance();
                           factory.setNamespaceAware(true);
                           if (isSecureProcessing) {
                              try {
                                 factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                              } catch (SAXException var56) {
                              }
                           }

                           SAXParser jaxpParser = factory.newSAXParser();
                           reader = jaxpParser.getXMLReader();
                        } catch (ParserConfigurationException var57) {
                           throw new SAXException(var57);
                        } catch (FactoryConfigurationError var58) {
                           throw new SAXException(var58.toString());
                        } catch (NoSuchMethodError var59) {
                        } catch (AbstractMethodError var60) {
                        }

                        if (null == reader) {
                           reader = XMLReaderFactory.createXMLReader();
                        }

                        if (!useXSLTC) {
                           stf.setAttribute("http://xml.apache.org/xalan/features/incremental", Boolean.TRUE);
                        }

                        TransformerHandler th = stf.newTransformerHandler(stylesheet);
                        reader.setContentHandler(th);
                        reader.setDTDHandler(th);
                        if (th instanceof ErrorHandler) {
                           reader.setErrorHandler((ErrorHandler)th);
                        }

                        try {
                           reader.setProperty("http://xml.org/sax/properties/lexical-handler", th);
                        } catch (SAXNotRecognizedException var54) {
                        } catch (SAXNotSupportedException var55) {
                        }

                        try {
                           reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
                        } catch (SAXException var53) {
                        }

                        th.setResult(strResult);
                        reader.parse(new InputSource(inFileName));
                     }
                  } else if (entityResolver != null) {
                     XMLReader reader = null;

                     try {
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        if (isSecureProcessing) {
                           try {
                              factory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
                           } catch (SAXException var48) {
                           }
                        }

                        SAXParser jaxpParser = factory.newSAXParser();
                        reader = jaxpParser.getXMLReader();
                     } catch (ParserConfigurationException var49) {
                        throw new SAXException(var49);
                     } catch (FactoryConfigurationError var50) {
                        throw new SAXException(var50.toString());
                     } catch (NoSuchMethodError var51) {
                     } catch (AbstractMethodError var52) {
                     }

                     if (null == reader) {
                        reader = XMLReaderFactory.createXMLReader();
                     }

                     reader.setEntityResolver(entityResolver);
                     if (contentHandler != null) {
                        SAXResult result = new SAXResult(contentHandler);
                        transformer.transform(new SAXSource(reader, new InputSource(inFileName)), result);
                     } else {
                        transformer.transform(new SAXSource(reader, new InputSource(inFileName)), strResult);
                     }
                  } else if (contentHandler != null) {
                     SAXResult result = new SAXResult(contentHandler);
                     transformer.transform(new StreamSource(inFileName), result);
                  } else {
                     transformer.transform(new StreamSource(inFileName), strResult);
                  }
               } else {
                  StringReader reader = new StringReader("<?xml version=\"1.0\"?> <doc/>");
                  transformer.transform(new StreamSource(reader), strResult);
               }
            }

            if (null != outFileName && strResult != null) {
               OutputStream out = strResult.getOutputStream();
               Writer writer = strResult.getWriter();

               try {
                  if (out != null) {
                     out.close();
                  }

                  if (writer != null) {
                     writer.close();
                  }
               } catch (IOException var47) {
               }
            }

            long stop = System.currentTimeMillis();
            long millisecondsDuration = stop - start;
            if (doDiag) {
               Object[] msgArgs = new Object[]{inFileName, xslFileName, new Long(millisecondsDuration)};
               msg = XSLMessages.createMessage("diagTiming", msgArgs);
               diagnosticsWriter.println('\n');
               diagnosticsWriter.println(msg);
            }
         } catch (Throwable var68) {
            Object throwable;
            for(throwable = var68; throwable instanceof WrappedRuntimeException; throwable = ((WrappedRuntimeException)throwable).getException()) {
            }

            if (throwable instanceof NullPointerException || throwable instanceof ClassCastException) {
               doStackDumpOnError = true;
            }

            diagnosticsWriter.println();
            if (doStackDumpOnError) {
               ((Throwable)throwable).printStackTrace(diagnosticsWriter);
            } else {
               DefaultErrorHandler.printLocation((PrintWriter)diagnosticsWriter, (Throwable)throwable);
               diagnosticsWriter.println(XSLMessages.createMessage("ER_XSLT_ERROR", (Object[])null) + " (" + throwable.getClass().getName() + "): " + ((Throwable)throwable).getMessage());
            }

            if (null != dumpFileName) {
               diagnosticsWriter.close();
            }

            doExit(((Throwable)throwable).getMessage());
         }

         if (null != dumpFileName) {
            dumpWriter.close();
         }

         if (null != diagnosticsWriter) {
         }
      }

   }

   static void doExit(String msg) {
      throw new RuntimeException(msg);
   }

   private static void waitForReturnKey(ResourceBundle resbundle) {
      System.out.println(resbundle.getString("xslProc_return_to_continue"));

      try {
         while(System.in.read() != 10) {
         }
      } catch (IOException var2) {
      }

   }

   private static void printInvalidXSLTCOption(String option) {
      System.err.println(XSLMessages.createMessage("xslProc_invalid_xsltc_option", new Object[]{option}));
   }

   private static void printInvalidXalanOption(String option) {
      System.err.println(XSLMessages.createMessage("xslProc_invalid_xalan_option", new Object[]{option}));
   }
}
