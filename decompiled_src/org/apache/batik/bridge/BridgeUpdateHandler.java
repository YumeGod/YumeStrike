package org.apache.batik.bridge;

import org.apache.batik.css.engine.CSSEngineEvent;
import org.apache.batik.dom.svg.AnimatedLiveAttributeValue;
import org.w3c.dom.events.MutationEvent;

public interface BridgeUpdateHandler {
   void handleDOMAttrModifiedEvent(MutationEvent var1);

   void handleDOMNodeInsertedEvent(MutationEvent var1);

   void handleDOMNodeRemovedEvent(MutationEvent var1);

   void handleDOMCharacterDataModified(MutationEvent var1);

   void handleCSSEngineEvent(CSSEngineEvent var1);

   void handleAnimatedAttributeChanged(AnimatedLiveAttributeValue var1);

   void handleOtherAnimationChanged(String var1);

   void dispose();
}
