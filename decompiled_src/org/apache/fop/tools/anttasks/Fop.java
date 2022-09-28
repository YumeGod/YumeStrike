package org.apache.fop.tools.anttasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.fop.apps.FOPException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.SAXException;

public class Fop extends Task {
   private File foFile;
   private File xmlFile;
   private File xsltFile;
   private String xsltParams;
   private List filesets = new ArrayList();
   private File outFile;
   private File outDir;
   private String format;
   private File baseDir;
   private File userConfig;
   private int messageType = 3;
   private boolean logFiles = true;
   private boolean force = false;
   private boolean relativebase = false;
   private boolean throwExceptions = true;

   public void setUserconfig(File userConfig) {
      this.userConfig = userConfig;
   }

   public File getUserconfig() {
      return this.userConfig;
   }

   public void setFofile(File foFile) {
      this.foFile = foFile;
   }

   public File getFofile() {
      return this.foFile;
   }

   public File getXmlFile() {
      return this.xmlFile;
   }

   public void setXmlFile(File xmlFile) {
      this.xmlFile = xmlFile;
   }

   public File getXsltFile() {
      return this.xsltFile;
   }

   public void setXsltFile(File xsltFile) {
      this.xsltFile = xsltFile;
   }

   public String getXsltParams() {
      return this.xsltParams;
   }

   public void setXsltParams(String xsltParams) {
      this.xsltParams = xsltParams;
   }

   public void addFileset(FileSet set) {
      this.filesets.add(set);
   }

   public List getFilesets() {
      return this.filesets;
   }

   public void setRelativebase(boolean relbase) {
      this.relativebase = relbase;
   }

   public boolean getRelativebase() {
      return this.relativebase;
   }

   public void setForce(boolean force) {
      this.force = force;
   }

   public boolean getForce() {
      return this.force;
   }

   public void setOutfile(File outFile) {
      this.outFile = outFile;
   }

   public File getOutfile() {
      return this.outFile;
   }

   public void setOutdir(File outDir) {
      this.outDir = outDir;
   }

   public File getOutdir() {
      return this.outDir;
   }

   public void setFormat(String format) {
      this.format = format;
   }

   public String getFormat() {
      return this.format;
   }

   public void setThrowexceptions(boolean throwExceptions) {
      this.throwExceptions = throwExceptions;
   }

   public boolean getThrowexceptions() {
      return this.throwExceptions;
   }

   public void setMessagelevel(String messageLevel) {
      if (messageLevel.equalsIgnoreCase("info")) {
         this.messageType = 2;
      } else if (messageLevel.equalsIgnoreCase("verbose")) {
         this.messageType = 3;
      } else if (messageLevel.equalsIgnoreCase("debug")) {
         this.messageType = 4;
      } else if (!messageLevel.equalsIgnoreCase("err") && !messageLevel.equalsIgnoreCase("error")) {
         if (!messageLevel.equalsIgnoreCase("warn")) {
            this.log("messagelevel set to unknown value \"" + messageLevel + "\"", 0);
            throw new BuildException("unknown messagelevel");
         }

         this.messageType = 1;
      } else {
         this.messageType = 0;
      }

   }

   public int getMessageType() {
      return this.messageType;
   }

   public void setBasedir(File baseDir) {
      this.baseDir = baseDir;
   }

   public File getBasedir() {
      return this.baseDir != null ? this.baseDir : this.getProject().resolveFile(".");
   }

   public void setLogFiles(boolean logFiles) {
      this.logFiles = logFiles;
   }

   public boolean getLogFiles() {
      return this.logFiles;
   }

   public void execute() throws BuildException {
      int logLevel = true;
      byte logLevel;
      switch (this.getMessageType()) {
         case 0:
            logLevel = 5;
            break;
         case 1:
            logLevel = 4;
            break;
         case 2:
            logLevel = 3;
            break;
         case 3:
            logLevel = 2;
            break;
         case 4:
            logLevel = 2;
            break;
         default:
            logLevel = 3;
      }

      SimpleLog logger = new SimpleLog("FOP/Anttask");
      logger.setLevel(logLevel);

      try {
         FOPTaskStarter starter = new FOPTaskStarter(this);
         starter.setLogger(logger);
         starter.run();
      } catch (FOPException var4) {
         throw new BuildException(var4);
      } catch (IOException var5) {
         throw new BuildException(var5);
      } catch (SAXException var6) {
         throw new BuildException(var6);
      }
   }
}
