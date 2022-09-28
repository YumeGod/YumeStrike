package org.apache.fop.fonts;

import org.apache.fop.events.EventBroadcaster;

public class FontEventAdapter implements FontEventListener {
   private final EventBroadcaster eventBroadcaster;
   private FontEventProducer eventProducer;

   public FontEventAdapter(EventBroadcaster broadcaster) {
      this.eventBroadcaster = broadcaster;
   }

   private FontEventProducer getEventProducer() {
      if (this.eventProducer == null) {
         this.eventProducer = FontEventProducer.Provider.get(this.eventBroadcaster);
      }

      return this.eventProducer;
   }

   public void fontSubstituted(Object source, FontTriplet requested, FontTriplet effective) {
      this.getEventProducer().fontSubstituted(source, requested, effective);
   }

   public void fontLoadingErrorAtAutoDetection(Object source, String fontURL, Exception e) {
      this.getEventProducer().fontLoadingErrorAtAutoDetection(source, fontURL, e);
   }

   public void glyphNotAvailable(Object source, char ch, String fontName) {
      this.getEventProducer().glyphNotAvailable(source, ch, fontName);
   }
}
