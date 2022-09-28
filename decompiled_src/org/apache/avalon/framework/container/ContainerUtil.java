package org.apache.avalon.framework.container;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Executable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

public final class ContainerUtil {
   private ContainerUtil() {
   }

   public static void shutdown(Object object) throws Exception {
      stop(object);
      dispose(object);
   }

   public static void enableLogging(Object object, Logger logger) {
      if (object instanceof LogEnabled) {
         if (null == logger) {
            String message = "logger is null";
            throw new IllegalArgumentException("logger is null");
         }

         ((LogEnabled)object).enableLogging(logger);
      }

   }

   public static void contextualize(Object object, Context context) throws ContextException {
      if (object instanceof Contextualizable) {
         if (null == context) {
            String message = "context is null";
            throw new IllegalArgumentException("context is null");
         }

         ((Contextualizable)object).contextualize(context);
      }

   }

   public static void service(Object object, ServiceManager serviceManager) throws ServiceException {
      if (object instanceof Serviceable) {
         if (null == serviceManager) {
            String message = "ServiceManager is null";
            throw new IllegalArgumentException("ServiceManager is null");
         }

         ((Serviceable)object).service(serviceManager);
      }

   }

   /** @deprecated */
   public static void compose(Object object, ComponentManager componentManager) throws ComponentException {
      if (object instanceof Composable) {
         if (null == componentManager) {
            String message = "componentManager is null";
            throw new IllegalArgumentException("componentManager is null");
         }

         ((Composable)object).compose(componentManager);
      }

   }

   public static void configure(Object object, Configuration configuration) throws ConfigurationException {
      if (object instanceof Configurable) {
         if (null == configuration) {
            String message = "configuration is null";
            throw new IllegalArgumentException("configuration is null");
         }

         ((Configurable)object).configure(configuration);
      }

   }

   public static void parameterize(Object object, Parameters parameters) throws ParameterException {
      if (object instanceof Parameterizable) {
         if (null == parameters) {
            String message = "parameters is null";
            throw new IllegalArgumentException("parameters is null");
         }

         ((Parameterizable)object).parameterize(parameters);
      }

   }

   public static void initialize(Object object) throws Exception {
      if (object instanceof Initializable) {
         ((Initializable)object).initialize();
      }

   }

   public static void start(Object object) throws Exception {
      if (object instanceof Startable) {
         ((Startable)object).start();
      }

   }

   public static void execute(Object object) throws Exception {
      if (object instanceof Executable) {
         ((Executable)object).execute();
      }

   }

   public static void stop(Object object) throws Exception {
      if (object instanceof Startable) {
         ((Startable)object).stop();
      }

   }

   public static void dispose(Object object) {
      if (object instanceof Disposable) {
         ((Disposable)object).dispose();
      }

   }
}
