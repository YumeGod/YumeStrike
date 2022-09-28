package org.apache.fop.render;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.fop.apps.FOUserAgent;

public class RendererContext {
   private final String mime;
   private final AbstractRenderer renderer;
   private FOUserAgent userAgent;
   private final Map props = new HashMap();

   public RendererContext(AbstractRenderer renderer, String mime) {
      this.renderer = renderer;
      this.mime = mime;
   }

   public AbstractRenderer getRenderer() {
      return this.renderer;
   }

   public String getMimeType() {
      return this.mime;
   }

   public void setUserAgent(FOUserAgent ua) {
      this.userAgent = ua;
   }

   public FOUserAgent getUserAgent() {
      return this.userAgent;
   }

   public void setProperty(String name, Object val) {
      this.props.put(name, val);
   }

   public Object getProperty(String prop) {
      return this.props.get(prop);
   }

   public static RendererContextWrapper wrapRendererContext(RendererContext context) {
      RendererContextWrapper wrapper = new RendererContextWrapper(context);
      return wrapper;
   }

   public String toString() {
      StringBuffer stringBuffer = new StringBuffer("RendererContext{\n");
      Iterator it = this.props.keySet().iterator();

      while(it.hasNext()) {
         String key = (String)it.next();
         Object value = this.props.get(key);
         stringBuffer.append("\t" + key + "=" + value + "\n");
      }

      stringBuffer.append("}");
      return stringBuffer.toString();
   }

   public static class RendererContextWrapper {
      protected RendererContext context;

      public RendererContextWrapper(RendererContext context) {
         this.context = context;
      }

      public FOUserAgent getUserAgent() {
         return this.context.getUserAgent();
      }

      public int getCurrentXPosition() {
         return (Integer)this.context.getProperty("xpos");
      }

      public int getCurrentYPosition() {
         return (Integer)this.context.getProperty("ypos");
      }

      public int getWidth() {
         return (Integer)this.context.getProperty("width");
      }

      public int getHeight() {
         return (Integer)this.context.getProperty("height");
      }

      public Map getForeignAttributes() {
         return (Map)this.context.getProperty("foreign-attributes");
      }

      public String toString() {
         return "RendererContextWrapper{userAgent=" + this.getUserAgent() + "x=" + this.getCurrentXPosition() + "y=" + this.getCurrentYPosition() + "width=" + this.getWidth() + "height=" + this.getHeight() + "foreignAttributes=" + this.getForeignAttributes() + "}";
      }
   }
}
