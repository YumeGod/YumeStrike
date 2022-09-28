package org.apache.fop.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.cli.InputHandler;
import org.apache.fop.tools.anttasks.FileCompare;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestConverter {
   private FopFactory fopFactory = FopFactory.newInstance();
   private boolean failOnly = false;
   private String outputFormat = "application/X-fop-areatree";
   private File destdir;
   private File compare = null;
   private String baseDir = "./";
   private Map differ = new HashMap();
   protected SimpleLog logger = null;

   public static void main(String[] args) {
      if (args == null || args.length == 0) {
         System.out.println("test suite file name required");
      }

      TestConverter tc = new TestConverter();
      String results = "results";
      String testFile = null;

      for(int count = 0; count < args.length; ++count) {
         if (args[count].equals("-failOnly")) {
            tc.setFailOnly(true);
         } else if (args[count].equals("-pdf")) {
            tc.setOutputFormat("application/pdf");
         } else if (args[count].equals("-rtf")) {
            tc.setOutputFormat("application/rtf");
         } else if (args[count].equals("-ps")) {
            tc.setOutputFormat("application/postscript");
         } else if (args[count].equals("-d")) {
            tc.setDebug(true);
         } else if (args[count].equals("-b")) {
            ++count;
            tc.setBaseDir(args[count]);
         } else if (args[count].equals("-results")) {
            ++count;
            results = args[count];
         } else {
            testFile = args[count];
         }
      }

      if (testFile == null) {
         System.out.println("test suite file name required");
      }

      tc.runTests(testFile, results, (String)null);
   }

   public TestConverter() {
      this.logger = new SimpleLog("FOP/Test");
      this.logger.setLevel(5);
   }

   public void setOutputFormat(String outputFormat) {
      this.outputFormat = outputFormat;
   }

   public void setFailOnly(boolean fail) {
      this.failOnly = fail;
   }

   public void setBaseDir(String str) {
      this.baseDir = str;
   }

   public void setDebug(boolean debug) {
      if (debug) {
         this.logger.setLevel(2);
      } else {
         this.logger.setLevel(5);
      }

   }

   public Map runTests(String fname, String dest, String compDir) {
      this.logger.debug("running tests in file:" + fname);

      try {
         if (compDir != null) {
            this.compare = new File(this.baseDir + "/" + compDir);
         }

         this.destdir = new File(this.baseDir + "/" + dest);
         this.destdir.mkdirs();
         File f = new File(this.baseDir + "/" + fname);
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder db = factory.newDocumentBuilder();
         Document doc = db.parse(f);
         NodeList suitelist = doc.getChildNodes();
         if (suitelist.getLength() == 0) {
            return this.differ;
         }

         Node testsuite = null;
         testsuite = doc.getDocumentElement();
         if (testsuite.hasAttributes()) {
            String profile = testsuite.getAttributes().getNamedItem("profile").getNodeValue();
            this.logger.debug("testing test suite:" + profile);
         }

         NodeList testcases = testsuite.getChildNodes();

         for(int count = 0; count < testcases.getLength(); ++count) {
            Node testcase = testcases.item(count);
            if (testcase.getNodeName().equals("testcases")) {
               this.runTestCase(testcase);
            }
         }
      } catch (Exception var13) {
         this.logger.error("Error while running tests", var13);
      }

      return this.differ;
   }

   protected void runTestCase(Node tcase) {
      if (tcase.hasAttributes()) {
         String profile = tcase.getAttributes().getNamedItem("profile").getNodeValue();
         this.logger.debug("testing profile:" + profile);
      }

      NodeList cases = tcase.getChildNodes();

      for(int count = 0; count < cases.getLength(); ++count) {
         Node node = cases.item(count);
         String nodename = node.getNodeName();
         if (nodename.equals("testcases")) {
            this.runTestCase(node);
         } else if (nodename.equals("test")) {
            this.runTest(tcase, node);
         } else if (nodename.equals("result")) {
         }
      }

   }

   protected void runTest(Node testcase, Node test) {
      String id = test.getAttributes().getNamedItem("id").getNodeValue();
      Node result = this.locateResult(testcase, id);
      boolean pass = false;
      String xml;
      if (result != null) {
         xml = result.getAttributes().getNamedItem("agreement").getNodeValue();
         pass = xml.equals("full");
      }

      if (!pass || !this.failOnly) {
         xml = test.getAttributes().getNamedItem("xml").getNodeValue();
         Node xslNode = test.getAttributes().getNamedItem("xsl");
         String xsl = null;
         if (xslNode != null) {
            xsl = xslNode.getNodeValue();
         }

         this.logger.debug("converting xml:" + xml + " and xsl:" + xsl + " to area tree");
         String res = xml;
         Node resNode = test.getAttributes().getNamedItem("results");
         if (resNode != null) {
            res = resNode.getNodeValue();
         }

         try {
            File xmlFile = new File(this.baseDir + "/" + xml);
            String baseURL = null;

            try {
               baseURL = xmlFile.getParentFile().toURI().toURL().toExternalForm();
            } catch (Exception var20) {
               this.logger.error("Error setting base directory");
            }

            InputHandler inputHandler = null;
            if (xsl == null) {
               inputHandler = new InputHandler(xmlFile);
            } else {
               inputHandler = new InputHandler(xmlFile, new File(this.baseDir + "/" + xsl), (Vector)null);
            }

            FOUserAgent userAgent = this.fopFactory.newFOUserAgent();
            userAgent.setBaseURL(baseURL);
            userAgent.getRendererOptions().put("fineDetail", new Boolean(false));
            userAgent.getRendererOptions().put("consistentOutput", new Boolean(true));
            userAgent.setProducer("Testsuite Converter");
            String outname = res;
            if (res.endsWith(".xml") || res.endsWith(".pdf")) {
               outname = res.substring(0, res.length() - 4);
            }

            File outputFile = new File(this.destdir, outname + this.makeResultExtension());
            outputFile.getParentFile().mkdirs();
            OutputStream outStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            this.logger.debug("ddir:" + this.destdir + " on:" + outputFile.getName());
            inputHandler.renderTo(userAgent, this.outputFormat, outStream);
            outStream.close();
            if (this.compare != null) {
               File f1 = new File(this.destdir, outname + ".at.xml");
               File f2 = new File(this.compare, outname + ".at.xml");
               if (!this.compareFiles(f1, f2)) {
                  this.differ.put(outname + ".at.xml", new Boolean(pass));
               }
            }
         } catch (Exception var21) {
            this.logger.error("Error while running tests", var21);
         }

      }
   }

   private String makeResultExtension() {
      if ("application/pdf".equals(this.outputFormat)) {
         return ".pdf";
      } else if ("application/rtf".equals(this.outputFormat)) {
         return ".rtf";
      } else {
         return "application/postscript".equals(this.outputFormat) ? ".ps" : ".at.xml";
      }
   }

   protected boolean compareFiles(File f1, File f2) {
      try {
         return FileCompare.compareFiles(f1, f2);
      } catch (Exception var4) {
         this.logger.error("Error while comparing files", var4);
         return false;
      }
   }

   private Node locateResult(Node testcase, String id) {
      NodeList cases = testcase.getChildNodes();

      for(int count = 0; count < cases.getLength(); ++count) {
         Node node = cases.item(count);
         String nodename = node.getNodeName();
         if (nodename.equals("result")) {
            String resultid = node.getAttributes().getNamedItem("id").getNodeValue();
            if (id.equals(resultid)) {
               return node;
            }
         }
      }

      return null;
   }
}
