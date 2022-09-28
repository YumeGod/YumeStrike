package org.apache.batik.dom.events;

import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.DOMException;
import org.w3c.dom.events.Event;

public class DocumentEventSupport {
   public static final String EVENT_TYPE = "Event";
   public static final String MUTATION_EVENT_TYPE = "MutationEvent";
   public static final String MUTATION_NAME_EVENT_TYPE = "MutationNameEvent";
   public static final String MOUSE_EVENT_TYPE = "MouseEvent";
   public static final String UI_EVENT_TYPE = "UIEvent";
   public static final String KEYBOARD_EVENT_TYPE = "KeyboardEvent";
   public static final String TEXT_EVENT_TYPE = "TextEvent";
   public static final String CUSTOM_EVENT_TYPE = "CustomEvent";
   public static final String EVENT_DOM2_TYPE = "Events";
   public static final String MUTATION_EVENT_DOM2_TYPE = "MutationEvents";
   public static final String MOUSE_EVENT_DOM2_TYPE = "MouseEvents";
   public static final String UI_EVENT_DOM2_TYPE = "UIEvents";
   public static final String KEY_EVENT_DOM2_TYPE = "KeyEvents";
   protected HashTable eventFactories = new HashTable();

   public DocumentEventSupport() {
      this.eventFactories.put("Event".toLowerCase(), new SimpleEventFactory());
      this.eventFactories.put("MutationEvent".toLowerCase(), new MutationEventFactory());
      this.eventFactories.put("MutationNameEvent".toLowerCase(), new MutationNameEventFactory());
      this.eventFactories.put("MouseEvent".toLowerCase(), new MouseEventFactory());
      this.eventFactories.put("KeyboardEvent".toLowerCase(), new KeyboardEventFactory());
      this.eventFactories.put("UIEvent".toLowerCase(), new UIEventFactory());
      this.eventFactories.put("TextEvent".toLowerCase(), new TextEventFactory());
      this.eventFactories.put("CustomEvent".toLowerCase(), new CustomEventFactory());
      this.eventFactories.put("Events".toLowerCase(), new SimpleEventFactory());
      this.eventFactories.put("MutationEvents".toLowerCase(), new MutationEventFactory());
      this.eventFactories.put("MouseEvents".toLowerCase(), new MouseEventFactory());
      this.eventFactories.put("KeyEvents".toLowerCase(), new KeyEventFactory());
      this.eventFactories.put("UIEvents".toLowerCase(), new UIEventFactory());
   }

   public Event createEvent(String var1) throws DOMException {
      EventFactory var2 = (EventFactory)this.eventFactories.get(var1.toLowerCase());
      if (var2 == null) {
         throw new DOMException((short)9, "Bad event type: " + var1);
      } else {
         return var2.createEvent();
      }
   }

   public void registerEventFactory(String var1, EventFactory var2) {
      this.eventFactories.put(var1.toLowerCase(), var2);
   }

   protected static class CustomEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMCustomEvent();
      }
   }

   protected static class TextEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMTextEvent();
      }
   }

   protected static class UIEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMUIEvent();
      }
   }

   protected static class KeyboardEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMKeyboardEvent();
      }
   }

   protected static class KeyEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMKeyEvent();
      }
   }

   protected static class MouseEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMMouseEvent();
      }
   }

   protected static class MutationNameEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMMutationNameEvent();
      }
   }

   protected static class MutationEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMMutationEvent();
      }
   }

   protected static class SimpleEventFactory implements EventFactory {
      public Event createEvent() {
         return new DOMEvent();
      }
   }

   public interface EventFactory {
      Event createEvent();
   }
}
