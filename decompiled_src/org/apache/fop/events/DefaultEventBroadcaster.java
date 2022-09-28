package org.apache.fop.events;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.fop.events.model.EventMethodModel;
import org.apache.fop.events.model.EventModel;
import org.apache.fop.events.model.EventModelParser;
import org.apache.fop.events.model.EventProducerModel;
import org.apache.fop.events.model.EventSeverity;

public class DefaultEventBroadcaster implements EventBroadcaster {
   protected CompositeEventListener listeners = new CompositeEventListener();
   private static List eventModels = new ArrayList();
   private Map proxies = new HashMap();

   public void addEventListener(EventListener listener) {
      this.listeners.addEventListener(listener);
   }

   public void removeEventListener(EventListener listener) {
      this.listeners.removeEventListener(listener);
   }

   public boolean hasEventListeners() {
      return this.listeners.hasEventListeners();
   }

   public void broadcastEvent(Event event) {
      this.listeners.processEvent(event);
   }

   private static EventModel loadModel(Class resourceBaseClass) {
      String resourceName = "event-model.xml";
      InputStream in = resourceBaseClass.getResourceAsStream(resourceName);
      if (in == null) {
         throw new MissingResourceException("File " + resourceName + " not found", DefaultEventBroadcaster.class.getName(), "");
      } else {
         EventModel var3;
         try {
            var3 = EventModelParser.parse(new StreamSource(in));
         } catch (TransformerException var7) {
            throw new MissingResourceException("Error reading " + resourceName + ": " + var7.getMessage(), DefaultEventBroadcaster.class.getName(), "");
         } finally {
            IOUtils.closeQuietly(in);
         }

         return var3;
      }
   }

   public static synchronized void addEventModel(EventModel eventModel) {
      eventModels.add(eventModel);
   }

   private static synchronized EventProducerModel getEventProducerModel(Class clazz) {
      int i = 0;

      for(int c = eventModels.size(); i < c; ++i) {
         EventModel eventModel = (EventModel)eventModels.get(i);
         EventProducerModel producerModel = eventModel.getProducer(clazz);
         if (producerModel != null) {
            return producerModel;
         }
      }

      EventModel model = loadModel(clazz);
      addEventModel(model);
      return model.getProducer(clazz);
   }

   public EventProducer getEventProducerFor(Class clazz) {
      if (!EventProducer.class.isAssignableFrom(clazz)) {
         throw new IllegalArgumentException("Class must be an implementation of the EventProducer interface: " + clazz.getName());
      } else {
         EventProducer producer = (EventProducer)this.proxies.get(clazz);
         if (producer == null) {
            producer = this.createProxyFor(clazz);
            this.proxies.put(clazz, producer);
         }

         return producer;
      }
   }

   protected EventProducer createProxyFor(Class clazz) {
      final EventProducerModel producerModel = getEventProducerModel(clazz);
      if (producerModel == null) {
         throw new IllegalStateException("Event model doesn't contain the definition for " + clazz.getName());
      } else {
         return (EventProducer)Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               String methodName = method.getName();
               EventMethodModel methodModel = producerModel.getMethod(methodName);
               String eventID = producerModel.getInterfaceName() + "." + methodName;
               if (methodModel == null) {
                  throw new IllegalStateException("Event model isn't consistent with the EventProducer interface. Please rebuild FOP! Affected method: " + eventID);
               } else {
                  Map params = new HashMap();
                  int i = 1;

                  for(Iterator iter = methodModel.getParameters().iterator(); iter.hasNext(); ++i) {
                     EventMethodModel.Parameter param = (EventMethodModel.Parameter)iter.next();
                     params.put(param.getName(), args[i]);
                  }

                  Event ev = new Event(args[0], eventID, methodModel.getSeverity(), params);
                  DefaultEventBroadcaster.this.broadcastEvent(ev);
                  if (ev.getSeverity() == EventSeverity.FATAL) {
                     EventExceptionManager.throwException(ev, methodModel.getExceptionClass());
                  }

                  return null;
               }
            }
         });
      }
   }
}
