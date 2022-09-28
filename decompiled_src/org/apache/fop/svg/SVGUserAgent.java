package org.apache.fop.svg;

import java.awt.geom.AffineTransform;
import org.apache.fop.apps.FOUserAgent;

public class SVGUserAgent extends SimpleSVGUserAgent {
   private SVGEventProducer eventProducer;
   private Exception lastException;

   public SVGUserAgent(FOUserAgent foUserAgent, AffineTransform at) {
      super(foUserAgent.getSourcePixelUnitToMillimeter(), at);
      this.eventProducer = SVGEventProducer.Provider.get(foUserAgent.getEventBroadcaster());
   }

   public SVGUserAgent(FOUserAgent foUserAgent) {
      this(foUserAgent, new AffineTransform());
   }

   public Exception getLastException() {
      return this.lastException;
   }

   public void displayError(String message) {
      this.eventProducer.error(this, message, (Exception)null);
   }

   public void displayError(Exception ex) {
      this.lastException = ex;
      this.eventProducer.error(this, ex.getLocalizedMessage(), ex);
   }

   public void displayMessage(String message) {
      this.eventProducer.info(this, message);
   }

   public void showAlert(String message) {
      this.eventProducer.alert(this, message);
   }
}
