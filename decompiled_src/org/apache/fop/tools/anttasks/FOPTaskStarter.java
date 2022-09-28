package org.apache.fop.tools.anttasks;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.cli.InputHandler;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.GlobPatternMapper;
import org.xml.sax.SAXException;

class FOPTaskStarter {
   private FopFactory fopFactory = FopFactory.newInstance();
   private Fop task;
   private String baseURL = null;
   protected Log logger = null;
   private static final String[][] SHORT_NAMES = new String[][]{{"pdf", "application/pdf"}, {"ps", "application/postscript"}, {"mif", "application/mif"}, {"rtf", "application/rtf"}, {"pcl", "application/x-pcl"}, {"txt", "text/plain"}, {"at", "application/X-fop-areatree"}, {"xml", "application/X-fop-areatree"}, {"tiff", "image/tiff"}, {"tif", "image/tiff"}, {"png", "image/png"}, {"afp", "application/x-afp"}};
   private static final String[][] EXTENSIONS = new String[][]{{"application/X-fop-areatree", ".at.xml"}, {"application/X-fop-awt-preview", null}, {"application/X-fop-print", null}, {"application/pdf", ".pdf"}, {"application/postscript", ".ps"}, {"application/x-pcl", ".pcl"}, {"application/vnd.hp-PCL", ".pcl"}, {"text/plain", ".txt"}, {"application/rtf", ".rtf"}, {"text/richtext", ".rtf"}, {"text/rtf", ".rtf"}, {"application/mif", ".mif"}, {"image/svg+xml", ".svg"}, {"image/png", ".png"}, {"image/jpeg", ".jpg"}, {"image/tiff", ".tif"}, {"application/x-afp", ".afp"}, {"application/vnd.ibm.modcap", ".afp"}, {"text/xsl", ".fo"}};

   public void setLogger(Log logger) {
      this.logger = logger;
   }

   protected Log getLogger() {
      return this.logger;
   }

   FOPTaskStarter(Fop task) throws SAXException, IOException {
      this.task = task;
      if (task.getUserconfig() != null) {
         this.fopFactory.setUserConfig(task.getUserconfig());
      }

   }

   private String normalizeOutputFormat(String format) {
      if (format == null) {
         return "application/pdf";
      } else {
         for(int i = 0; i < SHORT_NAMES.length; ++i) {
            if (SHORT_NAMES[i][0].equals(format)) {
               return SHORT_NAMES[i][1];
            }
         }

         return format;
      }
   }

   private String determineExtension(String outputFormat) {
      for(int i = 0; i < EXTENSIONS.length; ++i) {
         if (EXTENSIONS[i][0].equals(outputFormat)) {
            String ext = EXTENSIONS[i][1];
            if (ext == null) {
               throw new RuntimeException("Output format '" + outputFormat + "' does not produce a file.");
            }

            return ext;
         }
      }

      return ".unk";
   }

   private File replaceExtension(File file, String expectedExt, String newExt) {
      String name = file.getName();
      if (name.toLowerCase().endsWith(expectedExt)) {
         name = name.substring(0, name.length() - expectedExt.length());
      }

      name = name.concat(newExt);
      return new File(file.getParentFile(), name);
   }

   public void run() throws FOPException {
      if (this.task.getBasedir() != null) {
         try {
            this.baseURL = this.task.getBasedir().toURI().toURL().toExternalForm();
         } catch (MalformedURLException var19) {
            this.logger.error("Error creating base URL from base directory", var19);
         }
      } else {
         try {
            if (this.task.getFofile() != null) {
               this.baseURL = this.task.getFofile().getParentFile().toURI().toURL().toExternalForm();
            }
         } catch (MalformedURLException var18) {
            this.logger.error("Error creating base URL from XSL-FO input file", var18);
         }
      }

      this.task.log("Using base URL: " + this.baseURL, 4);
      String outputFormat = this.normalizeOutputFormat(this.task.getFormat());
      String newExtension = this.determineExtension(outputFormat);
      int actioncount = 0;
      int skippedcount = 0;
      File outf;
      if (this.task.getFofile() != null) {
         if (this.task.getFofile().exists()) {
            outf = this.task.getOutfile();
            if (outf == null) {
               throw new BuildException("outfile is required when fofile is used");
            }

            if (this.task.getOutdir() != null) {
               outf = new File(this.task.getOutdir(), outf.getName());
            }

            if (!this.task.getForce() && outf.exists() && this.task.getFofile().lastModified() <= outf.lastModified()) {
               if (outf.exists() && this.task.getFofile().lastModified() <= outf.lastModified()) {
                  ++skippedcount;
               }
            } else {
               this.render(this.task.getFofile(), outf, outputFormat);
               ++actioncount;
            }
         }
      } else if (this.task.getXmlFile() != null && this.task.getXsltFile() != null && this.task.getXmlFile().exists() && this.task.getXsltFile().exists()) {
         outf = this.task.getOutfile();
         if (outf == null) {
            throw new BuildException("outfile is required when fofile is used");
         }

         if (this.task.getOutdir() != null) {
            outf = new File(this.task.getOutdir(), outf.getName());
         }

         if (!this.task.getForce() && outf.exists() && this.task.getXmlFile().lastModified() <= outf.lastModified() && this.task.getXsltFile().lastModified() <= outf.lastModified()) {
            if (outf.exists() && (this.task.getXmlFile().lastModified() <= outf.lastModified() || this.task.getXsltFile().lastModified() <= outf.lastModified())) {
               ++skippedcount;
            }
         } else {
            this.render(this.task.getXmlFile(), this.task.getXsltFile(), outf, outputFormat);
            ++actioncount;
         }
      }

      GlobPatternMapper mapper = new GlobPatternMapper();
      String inputExtension = ".fo";
      File xsltFile = this.task.getXsltFile();
      if (xsltFile != null) {
         inputExtension = ".xml";
      }

      mapper.setFrom("*" + inputExtension);
      mapper.setTo("*" + newExtension);

      for(int i = 0; i < this.task.getFilesets().size(); ++i) {
         FileSet fs = (FileSet)this.task.getFilesets().get(i);
         DirectoryScanner ds = fs.getDirectoryScanner(this.task.getProject());
         String[] files = ds.getIncludedFiles();

         for(int j = 0; j < files.length; ++j) {
            File f = new File(fs.getDir(this.task.getProject()), files[j]);
            File outf = null;
            if (this.task.getOutdir() != null && files[j].endsWith(inputExtension)) {
               String[] sa = mapper.mapFileName(files[j]);
               outf = new File(this.task.getOutdir(), sa[0]);
            } else {
               outf = this.replaceExtension(f, inputExtension, newExtension);
               if (this.task.getOutdir() != null) {
                  outf = new File(this.task.getOutdir(), outf.getName());
               }
            }

            File dir = outf.getParentFile();
            if (!dir.exists()) {
               dir.mkdirs();
            }

            try {
               if (this.task.getRelativebase()) {
                  this.baseURL = f.getParentFile().toURI().toURL().toExternalForm();
               }

               if (this.baseURL == null) {
                  this.baseURL = fs.getDir(this.task.getProject()).toURI().toURL().toExternalForm();
               }
            } catch (Exception var17) {
               this.task.log("Error setting base URL", 4);
            }

            if (!this.task.getForce() && outf.exists() && f.lastModified() <= outf.lastModified()) {
               if (outf.exists() && f.lastModified() <= outf.lastModified()) {
                  ++skippedcount;
               }
            } else {
               if (xsltFile != null) {
                  this.render(f, xsltFile, outf, outputFormat);
               } else {
                  this.render(f, outf, outputFormat);
               }

               ++actioncount;
            }
         }
      }

      if (actioncount + skippedcount == 0) {
         this.task.log("No files processed. No files were selected by the filesets and no fofile was set.", 1);
      } else if (skippedcount > 0) {
         this.task.log(skippedcount + " xslfo file(s) skipped (no change found" + " since last generation; set force=\"true\" to override).", 2);
      }

   }

   private void renderInputHandler(InputHandler inputHandler, File outFile, String outputFormat) throws Exception {
      OutputStream out = null;

      BufferedOutputStream out;
      try {
         out = new FileOutputStream(outFile);
         out = new BufferedOutputStream(out);
      } catch (Exception var16) {
         throw new BuildException("Failed to open " + outFile, var16);
      }

      boolean success = false;

      try {
         FOUserAgent userAgent = this.fopFactory.newFOUserAgent();
         userAgent.setBaseURL(this.baseURL);
         inputHandler.renderTo(userAgent, outputFormat, out);
         success = true;
      } catch (Exception var17) {
         if (this.task.getThrowexceptions()) {
            throw new BuildException(var17);
         }

         throw var17;
      } finally {
         try {
            out.close();
         } catch (IOException var15) {
            this.logger.error("Error closing output file", var15);
         }

         if (!success) {
            outFile.delete();
         }

      }

   }

   private void render(File foFile, File outFile, String outputFormat) throws FOPException {
      InputHandler inputHandler = new InputHandler(foFile);

      try {
         this.renderInputHandler(inputHandler, outFile, outputFormat);
      } catch (Exception var6) {
         this.logger.error("Error rendering fo file: " + foFile, var6);
      }

      if (this.task.getLogFiles()) {
         this.task.log(foFile + " -> " + outFile, 2);
      }

   }

   private void render(File xmlFile, File xsltFile, File outFile, String outputFormat) {
      Vector xsltParams = null;
      InputHandler inputHandler = new InputHandler(xmlFile, xsltFile, (Vector)xsltParams);

      try {
         this.renderInputHandler(inputHandler, outFile, outputFormat);
      } catch (Exception var8) {
         this.logger.error("Error rendering xml/xslt files: " + xmlFile + ", " + xsltFile, var8);
      }

      if (this.task.getLogFiles()) {
         this.task.log("xml: " + xmlFile + ", xslt: " + xsltFile + " -> " + outFile, 2);
      }

   }
}
