package org.apache.fop.render.print;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.util.Map;
import org.apache.fop.apps.FOUserAgent;

public class PrintRenderer extends PageableRenderer {
   public static final String PRINTER_JOB = "printerjob";
   public static final String COPIES = "copies";
   private int copies;
   private PrinterJob printerJob;

   public PrintRenderer() {
      this.copies = 1;
   }

   /** @deprecated */
   public PrintRenderer(PrinterJob printerJob) {
      this();
      this.printerJob = printerJob;
      printerJob.setPageable(this);
   }

   private void initializePrinterJob() {
      if (this.printerJob == null) {
         this.printerJob = PrinterJob.getPrinterJob();
         this.printerJob.setJobName("FOP Document");
         this.printerJob.setCopies(this.copies);
         if (System.getProperty("dialog") != null && !this.printerJob.printDialog()) {
            throw new RuntimeException("Printing cancelled by operator");
         }

         this.printerJob.setPageable(this);
      }

   }

   public void setUserAgent(FOUserAgent agent) {
      super.setUserAgent(agent);
      Map rendererOptions = agent.getRendererOptions();
      Object printerJobO = rendererOptions.get("printerjob");
      if (printerJobO != null) {
         if (!(printerJobO instanceof PrinterJob)) {
            throw new IllegalArgumentException("Renderer option printerjob must be an instance of java.awt.print.PrinterJob, but an instance of " + printerJobO.getClass().getName() + " was given.");
         }

         this.printerJob = (PrinterJob)printerJobO;
         this.printerJob.setPageable(this);
      }

      Object o = rendererOptions.get("copies");
      if (o != null) {
         this.copies = this.getPositiveInteger(o);
      }

      this.initializePrinterJob();
   }

   public PrinterJob getPrinterJob() {
      return this.printerJob;
   }

   public int getEndNumber() {
      return this.endNumber;
   }

   public void setEndPage(int end) {
      this.endNumber = end;
   }

   public int getStartPage() {
      return this.startNumber;
   }

   public void setStartPage(int start) {
      this.startNumber = start;
   }

   public void stopRenderer() throws IOException {
      super.stopRenderer();

      try {
         this.printerJob.print();
      } catch (PrinterException var2) {
         log.error(var2);
         throw new IOException("Unable to print: " + var2.getClass().getName() + ": " + var2.getMessage());
      }

      this.clearViewportList();
   }
}
