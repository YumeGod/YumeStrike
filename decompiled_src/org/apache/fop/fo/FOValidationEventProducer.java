package org.apache.fop.fo;

import org.apache.fop.apps.FOPException;
import org.apache.fop.events.EventBroadcaster;
import org.apache.fop.events.EventProducer;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.xmlgraphics.util.QName;
import org.xml.sax.Locator;

public interface FOValidationEventProducer extends EventProducer {
   void tooManyNodes(Object var1, String var2, QName var3, Locator var4) throws ValidationException;

   void nodeOutOfOrder(Object var1, String var2, String var3, String var4, boolean var5, Locator var6) throws ValidationException;

   void invalidChild(Object var1, String var2, QName var3, String var4, Locator var5) throws ValidationException;

   void missingChildElement(Object var1, String var2, String var3, boolean var4, Locator var5) throws ValidationException;

   void missingProperty(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void idNotUnique(Object var1, String var2, String var3, boolean var4, Locator var5) throws ValidationException;

   void colorProfileNameNotUnique(Object var1, String var2, String var3, Locator var4);

   void masterNameNotUnique(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void markerBlockContainerAbsolutePosition(Object var1, Locator var2);

   void markerNotInitialChild(Object var1, String var2, String var3, Locator var4);

   void markerNotUniqueForSameParent(Object var1, String var2, String var3, Locator var4);

   void invalidProperty(Object var1, String var2, QName var3, boolean var4, Locator var5) throws ValidationException;

   void invalidPropertyValue(Object var1, String var2, String var3, String var4, PropertyException var5, Locator var6);

   void unimplementedFeature(Object var1, String var2, String var3, Locator var4);

   void missingLinkDestination(Object var1, String var2, Locator var3) throws ValidationException;

   void markerCloningFailed(Object var1, String var2, FOPException var3, Locator var4);

   void regionNameMappedToMultipleRegionClasses(Object var1, String var2, String var3, String var4, Locator var5) throws ValidationException;

   void duplicateFlowNameInPageSequence(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void flowNameNotMapped(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void masterNotFound(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void illegalRegionName(Object var1, String var2, String var3, Locator var4) throws ValidationException;

   void nonZeroBorderPaddingOnRegion(Object var1, String var2, String var3, boolean var4, Locator var5) throws ValidationException;

   void columnCountErrorOnRegionBodyOverflowScroll(Object var1, String var2, Locator var3) throws ValidationException;

   void invalidFORoot(Object var1, String var2, Locator var3) throws ValidationException;

   void emptyDocument(Object var1) throws ValidationException;

   void unknownFormattingObject(Object var1, String var2, QName var3, Locator var4);

   void altTextMissing(Object var1, String var2, Locator var3);

   public static final class Provider {
      private Provider() {
      }

      public static FOValidationEventProducer get(EventBroadcaster broadcaster) {
         return (FOValidationEventProducer)broadcaster.getEventProducerFor(FOValidationEventProducer.class);
      }
   }
}
