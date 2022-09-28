package org.apache.fop.apps;

import java.io.OutputStream;
import org.apache.fop.accessibility.Accessibility;
import org.apache.fop.fo.FOTreeBuilder;
import org.xml.sax.helpers.DefaultHandler;

public class Fop {
   private String outputFormat = null;
   private OutputStream stream = null;
   private FOUserAgent foUserAgent = null;
   private FOTreeBuilder foTreeBuilder = null;

   Fop(String outputFormat, FOUserAgent ua, OutputStream stream) throws FOPException {
      this.outputFormat = outputFormat;
      this.foUserAgent = ua;
      if (this.foUserAgent == null) {
         this.foUserAgent = FopFactory.newInstance().newFOUserAgent();
      }

      this.stream = stream;
      this.createDefaultHandler();
   }

   public FOUserAgent getUserAgent() {
      return this.foUserAgent;
   }

   private void createDefaultHandler() throws FOPException {
      this.foTreeBuilder = new FOTreeBuilder(this.outputFormat, this.foUserAgent, this.stream);
   }

   public DefaultHandler getDefaultHandler() throws FOPException {
      if (this.foTreeBuilder == null) {
         this.createDefaultHandler();
      }

      return (DefaultHandler)(this.foUserAgent.isAccessibilityEnabled() ? Accessibility.decorateDefaultHandler(this.foTreeBuilder, this.foUserAgent) : this.foTreeBuilder);
   }

   public FormattingResults getResults() {
      if (this.foTreeBuilder == null) {
         throw new IllegalStateException("Results are only available after calling getDefaultHandler().");
      } else {
         return this.foTreeBuilder.getResults();
      }
   }
}
