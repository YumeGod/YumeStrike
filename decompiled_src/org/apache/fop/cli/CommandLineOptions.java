package org.apache.fop.cli;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.swing.UIManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.Version;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.pdf.PDFAMode;
import org.apache.fop.pdf.PDFEncryptionManager;
import org.apache.fop.pdf.PDFEncryptionParams;
import org.apache.fop.pdf.PDFXMode;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.awt.AWTRenderer;
import org.apache.fop.render.intermediate.IFContext;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFSerializer;
import org.apache.fop.render.print.PagesMode;
import org.apache.fop.render.xml.XMLRenderer;
import org.apache.fop.util.CommandLineLogger;
import org.xml.sax.SAXException;

public class CommandLineOptions {
   public static final int RENDER_NONE = -1;
   public static final int NOT_SET = 0;
   public static final int FO_INPUT = 1;
   public static final int XSLT_INPUT = 2;
   public static final int AREATREE_INPUT = 3;
   public static final int IF_INPUT = 4;
   public static final int IMAGE_INPUT = 5;
   private Boolean showConfiguration;
   private Boolean suppressLowLevelAreas;
   private File userConfigFile;
   private File fofile;
   private File xsltfile;
   private File xmlfile;
   private File areatreefile;
   private File iffile;
   private File imagefile;
   private File outfile;
   private int inputmode;
   private String outputmode;
   private boolean useStdIn;
   private boolean useStdOut;
   private boolean useCatalogResolver;
   private Map renderingOptions;
   private int targetResolution;
   private boolean conserveMemoryPolicy;
   private FopFactory factory;
   private FOUserAgent foUserAgent;
   private InputHandler inputHandler;
   private Log log;
   private Vector xsltParams;
   private String mimicRenderer;

   public CommandLineOptions() {
      this.showConfiguration = Boolean.FALSE;
      this.suppressLowLevelAreas = Boolean.FALSE;
      this.userConfigFile = null;
      this.fofile = null;
      this.xsltfile = null;
      this.xmlfile = null;
      this.areatreefile = null;
      this.iffile = null;
      this.imagefile = null;
      this.outfile = null;
      this.inputmode = 0;
      this.outputmode = null;
      this.useStdIn = false;
      this.useStdOut = false;
      this.useCatalogResolver = false;
      this.renderingOptions = new HashMap();
      this.targetResolution = 0;
      this.conserveMemoryPolicy = false;
      this.factory = FopFactory.newInstance();
      this.xsltParams = null;
      this.mimicRenderer = null;
      LogFactory logFactory = LogFactory.getFactory();
      if (System.getProperty("org.apache.commons.logging.Log") == null) {
         logFactory.setAttribute("org.apache.commons.logging.Log", CommandLineLogger.class.getName());
         this.setLogLevel("info");
      }

      this.log = LogFactory.getLog("FOP");
   }

   public boolean parse(String[] args) throws FOPException, IOException {
      boolean optionsParsed = true;

      try {
         optionsParsed = this.parseOptions(args);
         if (!optionsParsed) {
            return false;
         }

         if (this.showConfiguration == Boolean.TRUE) {
            this.dumpConfiguration();
         }

         this.checkSettings();
         this.setUserConfig();
         this.foUserAgent = this.factory.newFOUserAgent();
         this.foUserAgent.getRendererOptions().putAll(this.renderingOptions);
         if (this.targetResolution != 0) {
            this.foUserAgent.setTargetResolution(this.targetResolution);
         }

         this.addXSLTParameter("fop-output-format", this.getOutputFormat());
         this.addXSLTParameter("fop-version", Version.getVersion());
         this.foUserAgent.setConserveMemoryPolicy(this.conserveMemoryPolicy);
      } catch (FOPException var6) {
         printUsage(System.err);
         throw var6;
      } catch (FileNotFoundException var7) {
         printUsage(System.err);
         throw var7;
      }

      this.inputHandler = this.createInputHandler();
      if ("application/X-fop-awt-preview".equals(this.outputmode)) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (Exception var5) {
            System.err.println("Couldn't set system look & feel!");
         }

         AWTRenderer renderer = new AWTRenderer(true);
         renderer.setRenderable(this.inputHandler);
         renderer.setUserAgent(this.foUserAgent);
         this.foUserAgent.setRendererOverride(renderer);
      } else if ("application/X-fop-areatree".equals(this.outputmode) && this.mimicRenderer != null) {
         Renderer targetRenderer = this.foUserAgent.getRendererFactory().createRenderer(this.foUserAgent, this.mimicRenderer);
         XMLRenderer xmlRenderer = new XMLRenderer();
         xmlRenderer.setUserAgent(this.foUserAgent);
         xmlRenderer.mimicRenderer(targetRenderer);
         this.foUserAgent.setRendererOverride(xmlRenderer);
      } else if ("application/X-fop-intermediate-format".equals(this.outputmode) && this.mimicRenderer != null) {
         IFSerializer serializer = new IFSerializer();
         serializer.setContext(new IFContext(this.foUserAgent));
         IFDocumentHandler targetHandler = this.foUserAgent.getRendererFactory().createDocumentHandler(this.foUserAgent, this.mimicRenderer);
         serializer.mimicDocumentHandler(targetHandler);
         this.foUserAgent.setDocumentHandlerOverride(serializer);
      }

      return true;
   }

   public InputHandler getInputHandler() {
      return this.inputHandler;
   }

   public Log getLogger() {
      return this.log;
   }

   private void addXSLTParameter(String name, String value) {
      if (this.xsltParams == null) {
         this.xsltParams = new Vector();
      }

      this.xsltParams.addElement(name);
      this.xsltParams.addElement(value);
   }

   private boolean parseOptions(String[] args) throws FOPException {
      if (args.length == 0) {
         printVersion();
         printUsage(System.out);
         return false;
      } else {
         for(int i = 0; i < args.length; ++i) {
            if (!args[i].equals("-x") && !args[i].equals("--dump-config")) {
               if (args[i].equals("-c")) {
                  i += this.parseConfigurationOption(args, i);
               } else if (args[i].equals("-l")) {
                  i += this.parseLanguageOption(args, i);
               } else if (args[i].equals("-s")) {
                  this.suppressLowLevelAreas = Boolean.TRUE;
               } else if (args[i].equals("-d")) {
                  this.setLogOption("debug", "debug");
               } else if (args[i].equals("-r")) {
                  this.factory.setStrictValidation(false);
               } else if (args[i].equals("-conserve")) {
                  this.conserveMemoryPolicy = true;
               } else if (args[i].equals("-dpi")) {
                  i += this.parseResolution(args, i);
               } else if (!args[i].equals("-q") && !args[i].equals("--quiet")) {
                  if (args[i].equals("-fo")) {
                     i += this.parseFOInputOption(args, i);
                  } else if (args[i].equals("-xsl")) {
                     i += this.parseXSLInputOption(args, i);
                  } else if (args[i].equals("-xml")) {
                     i += this.parseXMLInputOption(args, i);
                  } else if (args[i].equals("-atin")) {
                     i += this.parseAreaTreeInputOption(args, i);
                  } else if (args[i].equals("-ifin")) {
                     i += this.parseIFInputOption(args, i);
                  } else if (args[i].equals("-imagein")) {
                     i += this.parseImageInputOption(args, i);
                  } else if (args[i].equals("-awt")) {
                     i += this.parseAWTOutputOption(args, i);
                  } else if (args[i].equals("-pdf")) {
                     i += this.parsePDFOutputOption(args, i, (String)null);
                  } else if (args[i].equals("-pdfa1b")) {
                     i += this.parsePDFOutputOption(args, i, "PDF/A-1b");
                  } else if (args[i].equals("-mif")) {
                     i += this.parseMIFOutputOption(args, i);
                  } else if (args[i].equals("-rtf")) {
                     i += this.parseRTFOutputOption(args, i);
                  } else if (args[i].equals("-tiff")) {
                     i += this.parseTIFFOutputOption(args, i);
                  } else if (args[i].equals("-png")) {
                     i += this.parsePNGOutputOption(args, i);
                  } else if (args[i].equals("-print")) {
                     if (i + 1 < args.length && args[i + 1].equals("help")) {
                        this.printUsagePrintOutput();
                        return false;
                     }

                     i += this.parsePrintOutputOption(args, i);
                  } else if (args[i].equals("-copies")) {
                     i += this.parseCopiesOption(args, i);
                  } else if (args[i].equals("-pcl")) {
                     i += this.parsePCLOutputOption(args, i);
                  } else if (args[i].equals("-ps")) {
                     i += this.parsePostscriptOutputOption(args, i);
                  } else if (args[i].equals("-txt")) {
                     i += this.parseTextOutputOption(args, i);
                  } else if (args[i].equals("-svg")) {
                     i += this.parseSVGOutputOption(args, i);
                  } else if (args[i].equals("-afp")) {
                     i += this.parseAFPOutputOption(args, i);
                  } else if (args[i].equals("-foout")) {
                     i += this.parseFOOutputOption(args, i);
                  } else if (args[i].equals("-out")) {
                     i += this.parseCustomOutputOption(args, i);
                  } else if (args[i].equals("-at")) {
                     i += this.parseAreaTreeOption(args, i);
                  } else if (args[i].equals("-if")) {
                     i += this.parseIntermediateFormatOption(args, i);
                  } else if (args[i].equals("-a")) {
                     this.renderingOptions.put("accessibility", Boolean.TRUE);
                  } else if (args[i].equals("-v")) {
                     printVersion();
                  } else if (args[i].equals("-param")) {
                     if (i + 2 >= args.length) {
                        throw new FOPException("invalid param usage: use -param <name> <value>");
                     }

                     ++i;
                     String name = args[i];
                     ++i;
                     String expression = args[i];
                     this.addXSLTParameter(name, expression);
                  } else if (args[i].equals("-catalog")) {
                     this.useCatalogResolver = true;
                  } else if (args[i].equals("-o")) {
                     i += this.parsePDFOwnerPassword(args, i);
                  } else if (args[i].equals("-u")) {
                     i += this.parsePDFUserPassword(args, i);
                  } else if (args[i].equals("-pdfprofile")) {
                     i += this.parsePDFProfile(args, i);
                  } else if (args[i].equals("-noprint")) {
                     this.getPDFEncryptionParams().setAllowPrint(false);
                  } else if (args[i].equals("-nocopy")) {
                     this.getPDFEncryptionParams().setAllowCopyContent(false);
                  } else if (args[i].equals("-noedit")) {
                     this.getPDFEncryptionParams().setAllowEditContent(false);
                  } else if (args[i].equals("-noannotations")) {
                     this.getPDFEncryptionParams().setAllowEditAnnotations(false);
                  } else {
                     if (args[i].equals("-version")) {
                        printVersion();
                        return false;
                     }

                     if (!this.isOption(args[i])) {
                        i += this.parseUnknownOption(args, i);
                     } else {
                        printUsage(System.err);
                        System.exit(1);
                     }
                  }
               } else {
                  this.setLogOption("quiet", "error");
               }
            } else {
               this.showConfiguration = Boolean.TRUE;
            }
         }

         return true;
      }
   }

   private int parseConfigurationOption(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.userConfigFile = new File(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("if you use '-c', you must specify the name of the configuration file");
      }
   }

   private int parseLanguageOption(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         Locale.setDefault(new Locale(args[i + 1], ""));
         return 1;
      } else {
         throw new FOPException("if you use '-l', you must specify a language");
      }
   }

   private int parseResolution(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.targetResolution = Integer.parseInt(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("if you use '-dpi', you must specify a resolution (dots per inch)");
      }
   }

   private int parseFOInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(1);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String filename = args[i + 1];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.fofile = new File(filename);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the fo file for the '-fo' option");
      }
   }

   private int parseXSLInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(2);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.xsltfile = new File(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the stylesheet file for the '-xsl' option");
      }
   }

   private int parseXMLInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(2);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String filename = args[i + 1];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.xmlfile = new File(filename);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the input file for the '-xml' option");
      }
   }

   private int parseAWTOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/X-fop-awt-preview");
      return 0;
   }

   private int parsePDFOutputOption(String[] args, int i, String pdfAMode) throws FOPException {
      this.setOutputMode("application/pdf");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         if (pdfAMode != null) {
            if (this.renderingOptions.get("pdf-a-mode") != null) {
               throw new FOPException("PDF/A mode already set");
            }

            this.renderingOptions.put("pdf-a-mode", pdfAMode);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the PDF output file");
      }
   }

   private void setOutputFile(String filename) {
      if (this.isSystemInOutFile(filename)) {
         this.useStdOut = true;
      } else {
         this.outfile = new File(filename);
      }

   }

   private boolean isOption(String arg) {
      return arg.length() > 1 && arg.startsWith("-");
   }

   private boolean isSystemInOutFile(String filename) {
      return "-".equals(filename);
   }

   private int parseMIFOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/mif");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the MIF output file");
      }
   }

   private int parseRTFOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/rtf");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the RTF output file");
      }
   }

   private int parseTIFFOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("image/tiff");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the TIFF output file");
      }
   }

   private int parsePNGOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("image/png");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the PNG output file");
      }
   }

   private int parsePrintOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/X-fop-print");
      if (i + 1 <= args.length && args[i + 1].charAt(0) != '-') {
         String arg = args[i + 1];
         String[] parts = arg.split(",");

         for(int j = 0; j < parts.length; ++j) {
            String s = parts[j];
            if (s.matches("\\d+")) {
               this.renderingOptions.put("start-page", new Integer(s));
            } else if (s.matches("\\d+-\\d+")) {
               String[] startend = s.split("-");
               this.renderingOptions.put("start-page", new Integer(startend[0]));
               this.renderingOptions.put("end-page", new Integer(startend[1]));
            } else {
               PagesMode mode = PagesMode.byName(s);
               this.renderingOptions.put("even-odd", mode);
            }
         }

         return 1;
      } else {
         return 0;
      }
   }

   private int parseCopiesOption(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.renderingOptions.put("copies", new Integer(args[i + 1]));
         return 1;
      } else {
         throw new FOPException("you must specify the number of copies");
      }
   }

   private int parsePCLOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/x-pcl");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the PDF output file");
      }
   }

   private int parsePostscriptOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/postscript");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the PostScript output file");
      }
   }

   private int parseTextOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("text/plain");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the text output file");
      }
   }

   private int parseSVGOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("image/svg+xml");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the SVG output file");
      }
   }

   private int parseAFPOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/x-afp");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the AFP output file");
      }
   }

   private int parseFOOutputOption(String[] args, int i) throws FOPException {
      this.setOutputMode("text/xsl");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.setOutputFile(args[i + 1]);
         return 1;
      } else {
         throw new FOPException("you must specify the FO output file");
      }
   }

   private int parseCustomOutputOption(String[] args, int i) throws FOPException {
      String mime = null;
      if (i + 1 < args.length || args[i + 1].charAt(0) != '-') {
         mime = args[i + 1];
         if ("list".equals(mime)) {
            String[] mimes = this.factory.getRendererFactory().listSupportedMimeTypes();
            System.out.println("Supported MIME types:");

            for(int j = 0; j < mimes.length; ++j) {
               System.out.println("  " + mimes[j]);
            }

            System.exit(0);
         }
      }

      if (i + 2 < args.length && !this.isOption(args[i + 1]) && !this.isOption(args[i + 2])) {
         this.setOutputMode(mime);
         this.setOutputFile(args[i + 2]);
         return 2;
      } else {
         throw new FOPException("you must specify the output format and the output file");
      }
   }

   private int parseUnknownOption(String[] args, int i) throws FOPException {
      if (this.inputmode == 0) {
         this.inputmode = 1;
         String filename = args[i];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.fofile = new File(filename);
         }
      } else {
         if (this.outputmode != null) {
            throw new FOPException("Don't know what to do with " + args[i]);
         }

         this.outputmode = "application/pdf";
         this.setOutputFile(args[i]);
      }

      return 0;
   }

   private int parseAreaTreeOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/X-fop-areatree");
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         if (i + 2 != args.length && !this.isOption(args[i + 2])) {
            this.mimicRenderer = args[i + 1];
            this.setOutputFile(args[i + 2]);
            return 2;
         } else {
            this.setOutputFile(args[i + 1]);
            return 1;
         }
      } else {
         throw new FOPException("you must specify the area-tree output file");
      }
   }

   private int parseIntermediateFormatOption(String[] args, int i) throws FOPException {
      this.setOutputMode("application/X-fop-intermediate-format");
      if (i + 1 != args.length && args[i + 1].charAt(0) != '-') {
         if (i + 2 != args.length && args[i + 2].charAt(0) != '-') {
            this.mimicRenderer = args[i + 1];
            this.setOutputFile(args[i + 2]);
            return 2;
         } else {
            this.setOutputFile(args[i + 1]);
            return 1;
         }
      } else {
         throw new FOPException("you must specify the intermediate format output file");
      }
   }

   private int parseAreaTreeInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(3);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String filename = args[i + 1];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.areatreefile = new File(filename);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the Area Tree file for the '-atin' option");
      }
   }

   private int parseIFInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(4);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String filename = args[i + 1];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.iffile = new File(filename);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the intermediate file for the '-ifin' option");
      }
   }

   private int parseImageInputOption(String[] args, int i) throws FOPException {
      this.setInputFormat(5);
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String filename = args[i + 1];
         if (this.isSystemInOutFile(filename)) {
            this.useStdIn = true;
         } else {
            this.imagefile = new File(filename);
         }

         return 1;
      } else {
         throw new FOPException("you must specify the image file for the '-imagein' option");
      }
   }

   private PDFEncryptionParams getPDFEncryptionParams() throws FOPException {
      PDFEncryptionParams params = (PDFEncryptionParams)this.renderingOptions.get("encryption-params");
      if (params == null) {
         if (!PDFEncryptionManager.checkAvailableAlgorithms()) {
            throw new FOPException("PDF encryption requested but it is not available. Please make sure MD5 and RC4 algorithms are available.");
         }

         params = new PDFEncryptionParams();
         this.renderingOptions.put("encryption-params", params);
      }

      return params;
   }

   private int parsePDFOwnerPassword(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.getPDFEncryptionParams().setOwnerPassword(args[i + 1]);
         return 1;
      } else {
         this.getPDFEncryptionParams().setOwnerPassword("");
         return 0;
      }
   }

   private int parsePDFUserPassword(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         this.getPDFEncryptionParams().setUserPassword(args[i + 1]);
         return 1;
      } else {
         this.getPDFEncryptionParams().setUserPassword("");
         return 0;
      }
   }

   private int parsePDFProfile(String[] args, int i) throws FOPException {
      if (i + 1 != args.length && !this.isOption(args[i + 1])) {
         String profile = args[i + 1];
         PDFAMode pdfAMode = PDFAMode.valueOf(profile);
         if (pdfAMode != null && pdfAMode != PDFAMode.DISABLED) {
            if (this.renderingOptions.get("pdf-a-mode") != null) {
               throw new FOPException("PDF/A mode already set");
            } else {
               this.renderingOptions.put("pdf-a-mode", pdfAMode.getName());
               return 1;
            }
         } else {
            PDFXMode pdfXMode = PDFXMode.valueOf(profile);
            if (pdfXMode != null && pdfXMode != PDFXMode.DISABLED) {
               if (this.renderingOptions.get("pdf-x-mode") != null) {
                  throw new FOPException("PDF/X mode already set");
               } else {
                  this.renderingOptions.put("pdf-x-mode", pdfXMode.getName());
                  return 1;
               }
            } else {
               throw new FOPException("Unsupported PDF profile: " + profile);
            }
         }
      } else {
         throw new FOPException("You must specify a PDF profile");
      }
   }

   private void setOutputMode(String mime) throws FOPException {
      if (this.outputmode == null) {
         this.outputmode = mime;
      } else {
         throw new FOPException("you can only set one output method");
      }
   }

   private void setLogOption(String option, String level) {
      if (!(this.log instanceof CommandLineLogger) && System.getProperty("org.apache.commons.logging.Log") != null) {
         if (this.log != null) {
            this.log.warn("The option " + option + " can only be used");
            this.log.warn("with FOP's command line logger,");
            this.log.warn("which is the default on the command line.");
            this.log.warn("Configure other loggers using Java system properties.");
         }
      } else {
         this.setLogLevel(level);
      }

   }

   private void setLogLevel(String level) {
      LogFactory.getFactory().setAttribute("level", level);
      if (this.log instanceof CommandLineLogger) {
         ((CommandLineLogger)this.log).setLogLevel(level);
      }

   }

   private void setInputFormat(int format) throws FOPException {
      if (this.inputmode != 0 && this.inputmode != format) {
         throw new FOPException("Only one input mode can be specified!");
      } else {
         this.inputmode = format;
      }
   }

   private void checkSettings() throws FOPException, FileNotFoundException {
      if (this.inputmode == 0) {
         throw new FOPException("No input file specified");
      } else if (this.outputmode == null) {
         throw new FOPException("No output file specified");
      } else if ((this.outputmode.equals("application/X-fop-awt-preview") || this.outputmode.equals("application/X-fop-print")) && this.outfile != null) {
         throw new FOPException("Output file may not be specified for AWT or PRINT output");
      } else {
         if (this.inputmode == 2) {
            if (this.xmlfile == null && !this.useStdIn) {
               throw new FOPException("XML file must be specified for the transform mode");
            }

            if (this.xsltfile == null) {
               throw new FOPException("XSLT file must be specified for the transform mode");
            }

            if (this.fofile != null) {
               this.log.warn("Can't use fo file with transform mode! Ignoring.\nYour input is \n xmlfile: " + this.xmlfile.getAbsolutePath() + "\nxsltfile: " + this.xsltfile.getAbsolutePath() + "\n  fofile: " + this.fofile.getAbsolutePath());
            }

            if (this.xmlfile != null && !this.xmlfile.exists()) {
               throw new FileNotFoundException("Error: xml file " + this.xmlfile.getAbsolutePath() + " not found ");
            }

            if (!this.xsltfile.exists()) {
               throw new FileNotFoundException("Error: xsl file " + this.xsltfile.getAbsolutePath() + " not found ");
            }
         } else if (this.inputmode == 1) {
            if (this.outputmode.equals("text/xsl")) {
               throw new FOPException("FO output mode is only available if you use -xml and -xsl");
            }

            if (this.fofile != null && !this.fofile.exists()) {
               throw new FileNotFoundException("Error: fo file " + this.fofile.getAbsolutePath() + " not found ");
            }
         } else if (this.inputmode == 3) {
            if (this.outputmode.equals("text/xsl")) {
               throw new FOPException("FO output mode is only available if you use -xml and -xsl");
            }

            if (this.outputmode.equals("application/X-fop-areatree")) {
               throw new FOPException("Area Tree Output is not available if Area Tree is used as input!");
            }

            if (this.areatreefile != null && !this.areatreefile.exists()) {
               throw new FileNotFoundException("Error: area tree file " + this.areatreefile.getAbsolutePath() + " not found ");
            }
         } else if (this.inputmode == 4) {
            if (this.outputmode.equals("text/xsl")) {
               throw new FOPException("FO output mode is only available if you use -xml and -xsl");
            }

            if (this.outputmode.equals("application/X-fop-areatree")) {
               throw new FOPException("Area Tree Output is not available if Intermediate Format is used as input!");
            }

            if (this.outputmode.equals("application/X-fop-intermediate-format")) {
               throw new FOPException("Intermediate Output is not available if Intermediate Format is used as input!");
            }

            if (this.iffile != null && !this.iffile.exists()) {
               throw new FileNotFoundException("Error: intermediate format file " + this.iffile.getAbsolutePath() + " not found ");
            }
         } else if (this.inputmode == 5) {
            if (this.outputmode.equals("text/xsl")) {
               throw new FOPException("FO output mode is only available if you use -xml and -xsl");
            }

            if (this.imagefile != null && !this.imagefile.exists()) {
               throw new FileNotFoundException("Error: image file " + this.imagefile.getAbsolutePath() + " not found ");
            }
         }

      }
   }

   private void setUserConfig() throws FOPException, IOException {
      if (this.userConfigFile != null) {
         try {
            this.factory.setUserConfig(this.userConfigFile);
         } catch (SAXException var2) {
            throw new FOPException(var2);
         }
      }
   }

   protected String getOutputFormat() throws FOPException {
      if (this.outputmode == null) {
         throw new FOPException("Renderer has not been set!");
      } else {
         if (this.outputmode.equals("application/X-fop-areatree")) {
            this.renderingOptions.put("fineDetail", this.isCoarseAreaXml());
         }

         return this.outputmode;
      }
   }

   private InputHandler createInputHandler() {
      switch (this.inputmode) {
         case 1:
            return new InputHandler(this.fofile);
         case 2:
            InputHandler handler = new InputHandler(this.xmlfile, this.xsltfile, this.xsltParams);
            if (this.useCatalogResolver) {
               handler.createCatalogResolver(this.foUserAgent);
            }

            return handler;
         case 3:
            return new AreaTreeInputHandler(this.areatreefile);
         case 4:
            return new IFInputHandler(this.iffile);
         case 5:
            return new ImageInputHandler(this.imagefile, this.xsltfile, this.xsltParams);
         default:
            throw new IllegalArgumentException("Error creating InputHandler object.");
      }
   }

   protected FOUserAgent getFOUserAgent() {
      return this.foUserAgent;
   }

   public File getFOFile() {
      return this.fofile;
   }

   public File getXMLFile() {
      return this.xmlfile;
   }

   public File getXSLFile() {
      return this.xsltfile;
   }

   public File getOutputFile() {
      return this.outfile;
   }

   public File getUserConfigFile() {
      return this.userConfigFile;
   }

   public Boolean isCoarseAreaXml() {
      return this.suppressLowLevelAreas;
   }

   public boolean isInputFromStdIn() {
      return this.useStdIn;
   }

   public boolean isOutputToStdOut() {
      return this.useStdOut;
   }

   public File getInputFile() {
      switch (this.inputmode) {
         case 1:
            return this.fofile;
         case 2:
            return this.xmlfile;
         default:
            return this.fofile;
      }
   }

   private static void printVersion() {
      System.out.println("FOP Version " + Version.getVersion());
   }

   public static void printUsage(PrintStream out) {
      out.println("\nUSAGE\nfop [options] [-fo|-xml] infile [-xsl file] [-awt|-pdf|-mif|-rtf|-tiff|-png|-pcl|-ps|-txt|-at [mime]|-print] <outfile>\n [OPTIONS]  \n  -version          print FOP version and exit\n  -d                debug mode   \n  -x                dump configuration settings  \n  -q                quiet mode  \n  -c cfg.xml        use additional configuration file cfg.xml\n  -l lang           the language to use for user information \n  -r                relaxed/less strict validation (where available)\n  -dpi xxx          target resolution in dots per inch (dpi) where xxx is a number\n  -s                for area tree XML, down to block areas only\n  -v                run in verbose mode (currently simply print FOP version and continue)\n\n  -o [password]     PDF file will be encrypted with option owner password\n  -u [password]     PDF file will be encrypted with option user password\n  -noprint          PDF file will be encrypted without printing permission\n  -nocopy           PDF file will be encrypted without copy content permission\n  -noedit           PDF file will be encrypted without edit content permission\n  -noannotations    PDF file will be encrypted without edit annotation permission\n  -a                enables accessibility features (Tagged PDF etc., default off)\n  -pdfprofile prof  PDF file will be generated with the specified profile\n                    (Examples for prof: PDF/A-1b or PDF/X-3:2003)\n\n  -conserve         Enable memory-conservation policy (trades memory-consumption for disk I/O)\n                    (Note: currently only influences whether the area tree is serialized.)\n\n [INPUT]  \n  infile            xsl:fo input file (the same as the next) \n                    (use '-' for infile to pipe input from stdin)\n  -fo  infile       xsl:fo input file  \n  -xml infile       xml input file, must be used together with -xsl \n  -atin infile      area tree input file \n  -ifin infile      intermediate format input file \n  -imagein infile   image input file (piping through stdin not supported)\n  -xsl stylesheet   xslt stylesheet \n \n  -param name value <value> to use for parameter <name> in xslt stylesheet\n                    (repeat '-param name value' for each parameter)\n \n  -catalog          use catalog resolver for input XML and XSLT files\n [OUTPUT] \n  outfile           input will be rendered as PDF into outfile\n                    (use '-' for outfile to pipe output to stdout)\n  -pdf outfile      input will be rendered as PDF (outfile req'd)\n  -pdfa1b outfile   input will be rendered as PDF/A-1b compliant PDF\n                    (outfile req'd, same as \"-pdf outfile -pdfprofile PDF/A-1b\")\n  -awt              input will be displayed on screen \n  -rtf outfile      input will be rendered as RTF (outfile req'd)\n  -pcl outfile      input will be rendered as PCL (outfile req'd) \n  -ps outfile       input will be rendered as PostScript (outfile req'd) \n  -afp outfile      input will be rendered as AFP (outfile req'd)\n  -tiff outfile     input will be rendered as TIFF (outfile req'd)\n  -png outfile      input will be rendered as PNG (outfile req'd)\n  -txt outfile      input will be rendered as plain text (outfile req'd) \n  -at [mime] out    representation of area tree as XML (outfile req'd) \n                    specify optional mime output to allow the AT to be converted\n                    to final format later\n  -if [mime] out    representation of document in intermediate format XML (outfile req'd)\n                    specify optional mime output to allow the IF to be converted\n                    to final format later\n  -print            input file will be rendered and sent to the printer \n                    see options with \"-print help\" \n  -out mime outfile input will be rendered using the given MIME type\n                    (outfile req'd) Example: \"-out application/pdf D:\\out.pdf\"\n                    (Tip: \"-out list\" prints the list of supported MIME types)\n  -svg outfile      input will be rendered as an SVG slides file (outfile req'd) \n                    Experimental feature - requires additional fop-sandbox.jar.\n\n  -foout outfile    input will only be XSL transformed. The intermediate \n                    XSL-FO file is saved and no rendering is performed. \n                    (Only available if you use -xml and -xsl parameters)\n\n\n [Examples]\n  fop foo.fo foo.pdf \n  fop -fo foo.fo -pdf foo.pdf (does the same as the previous line)\n  fop -xml foo.xml -xsl foo.xsl -pdf foo.pdf\n  fop -xml foo.xml -xsl foo.xsl -foout foo.fo\n  fop -xml - -xsl foo.xsl -pdf -\n  fop foo.fo -mif foo.mif\n  fop foo.fo -rtf foo.rtf\n  fop foo.fo -print\n  fop foo.fo -awt\n");
   }

   private void printUsagePrintOutput() {
      System.err.println("USAGE: -print [from[-to][,even|odd]] [-copies numCopies]\n\nExample:\nall pages:                        fop infile.fo -print\nall pages with two copies:        fop infile.fo -print -copies 2\nall pages starting with page 7:   fop infile.fo -print 7\npages 2 to 3:                     fop infile.fo -print 2-3\nonly even page between 10 and 20: fop infile.fo -print 10-20,even\n");
   }

   private void dumpConfiguration() {
      this.log.info("Input mode: ");
      switch (this.inputmode) {
         case 0:
            this.log.info("not set");
            break;
         case 1:
            this.log.info("FO ");
            if (this.isInputFromStdIn()) {
               this.log.info("fo input file: from stdin");
            } else {
               this.log.info("fo input file: " + this.fofile.toString());
            }
            break;
         case 2:
            this.log.info("xslt transformation");
            if (this.isInputFromStdIn()) {
               this.log.info("xml input file: from stdin");
            } else {
               this.log.info("xml input file: " + this.xmlfile.toString());
            }

            this.log.info("xslt stylesheet: " + this.xsltfile.toString());
            break;
         case 3:
            this.log.info("AT ");
            if (this.isInputFromStdIn()) {
               this.log.info("area tree input file: from stdin");
            } else {
               this.log.info("area tree input file: " + this.areatreefile.toString());
            }
            break;
         case 4:
            this.log.info("IF ");
            if (this.isInputFromStdIn()) {
               this.log.info("intermediate input file: from stdin");
            } else {
               this.log.info("intermediate input file: " + this.iffile.toString());
            }
            break;
         case 5:
            this.log.info("Image ");
            if (this.isInputFromStdIn()) {
               this.log.info("image input file: from stdin");
            } else {
               this.log.info("image input file: " + this.imagefile.toString());
            }
            break;
         default:
            this.log.info("unknown input type");
      }

      this.log.info("Output mode: ");
      if (this.outputmode == null) {
         this.log.info("not set");
      } else if ("application/X-fop-awt-preview".equals(this.outputmode)) {
         this.log.info("awt on screen");
         if (this.outfile != null) {
            this.log.error("awt mode, but outfile is set:");
            this.log.error("out file: " + this.outfile.toString());
         }
      } else if ("application/X-fop-print".equals(this.outputmode)) {
         this.log.info("print directly");
         if (this.outfile != null) {
            this.log.error("print mode, but outfile is set:");
            this.log.error("out file: " + this.outfile.toString());
         }
      } else if ("application/X-fop-areatree".equals(this.outputmode)) {
         this.log.info("area tree");
         if (this.mimicRenderer != null) {
            this.log.info("mimic renderer: " + this.mimicRenderer);
         }

         if (this.isOutputToStdOut()) {
            this.log.info("output file: to stdout");
         } else {
            this.log.info("output file: " + this.outfile.toString());
         }
      } else if ("application/X-fop-intermediate-format".equals(this.outputmode)) {
         this.log.info("intermediate format");
         this.log.info("output file: " + this.outfile.toString());
      } else {
         this.log.info(this.outputmode);
         if (this.isOutputToStdOut()) {
            this.log.info("output file: to stdout");
         } else {
            this.log.info("output file: " + this.outfile.toString());
         }
      }

      this.log.info("OPTIONS");
      if (this.userConfigFile != null) {
         this.log.info("user configuration file: " + this.userConfigFile.toString());
      } else {
         this.log.info("no user configuration file is used [default]");
      }

   }
}
