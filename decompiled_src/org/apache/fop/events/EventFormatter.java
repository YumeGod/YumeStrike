package org.apache.fop.events;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.XMLResourceBundle;
import org.apache.fop.util.text.AdvancedMessageFormat;

public final class EventFormatter {
   private static final Pattern INCLUDES_PATTERN = Pattern.compile("\\{\\{.+\\}\\}");
   private static Log log;

   private EventFormatter() {
   }

   public static String format(Event event) {
      ResourceBundle bundle = null;
      String groupID = event.getEventGroupID();
      if (groupID != null) {
         try {
            bundle = XMLResourceBundle.getXMLBundle(groupID, EventFormatter.class.getClassLoader());
         } catch (MissingResourceException var4) {
            throw new IllegalStateException("No XMLResourceBundle for " + groupID + " available.");
         }
      }

      return format(event, bundle);
   }

   public static String format(Event event, Locale locale) {
      ResourceBundle bundle = null;
      String groupID = event.getEventGroupID();
      if (groupID != null) {
         try {
            bundle = XMLResourceBundle.getXMLBundle(groupID, locale, EventFormatter.class.getClassLoader());
         } catch (MissingResourceException var5) {
            if (log.isTraceEnabled()) {
               log.trace("No XMLResourceBundle for " + groupID + " available.");
            }
         }
      }

      if (bundle == null) {
         bundle = XMLResourceBundle.getXMLBundle(EventFormatter.class.getName(), locale, EventFormatter.class.getClassLoader());
      }

      return format(event, bundle);
   }

   private static String format(Event event, ResourceBundle bundle) {
      String template = bundle.getString(event.getEventKey());
      return format(event, processIncludes(template, bundle));
   }

   private static String processIncludes(String template, ResourceBundle bundle) {
      CharSequence input = template;

      int replacements;
      StringBuffer sb;
      do {
         sb = new StringBuffer(Math.max(16, ((CharSequence)input).length()));
         replacements = processIncludesInner((CharSequence)input, sb, bundle);
         input = sb;
      } while(replacements > 0);

      String s = sb.toString();
      return s;
   }

   private static int processIncludesInner(CharSequence template, StringBuffer sb, ResourceBundle bundle) {
      int replacements = 0;

      Matcher m;
      for(m = INCLUDES_PATTERN.matcher(template); m.find(); ++replacements) {
         String include = m.group();
         include = include.substring(2, include.length() - 2);
         m.appendReplacement(sb, bundle.getString(include));
      }

      m.appendTail(sb);
      return replacements;
   }

   public static String format(Event event, String pattern) {
      AdvancedMessageFormat format = new AdvancedMessageFormat(pattern);
      Map params = new HashMap(event.getParams());
      params.put("source", event.getSource());
      params.put("severity", event.getSeverity());
      return format.format(params);
   }

   static {
      log = LogFactory.getLog(EventFormatter.class);
   }

   public static class LookupFieldPartFactory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new LookupFieldPart(fieldName);
      }

      public String getFormat() {
         return "lookup";
      }
   }

   private static class LookupFieldPart implements AdvancedMessageFormat.Part {
      private String fieldName;

      public LookupFieldPart(String fieldName) {
         this.fieldName = fieldName;
      }

      public boolean isGenerated(Map params) {
         return this.getKey(params) != null;
      }

      public void write(StringBuffer sb, Map params) {
      }

      private String getKey(Map params) {
         return (String)params.get(this.fieldName);
      }

      public String toString() {
         return "{" + this.fieldName + ", lookup}";
      }
   }
}
