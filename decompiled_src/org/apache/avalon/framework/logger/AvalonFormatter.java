package org.apache.avalon.framework.logger;

import org.apache.avalon.framework.ExceptionUtil;
import org.apache.log.LogEvent;
import org.apache.log.format.ExtendedPatternFormatter;
import org.apache.log.format.PatternFormatter;
import org.apache.log.util.StackIntrospector;

public class AvalonFormatter extends ExtendedPatternFormatter {
   private static final int TYPE_CLASS = 9;
   private static final String TYPE_CLASS_STR = "class";
   private static final String TYPE_CLASS_SHORT_STR = "short";
   public static final int DEFAULT_STACK_DEPTH = 8;
   public static final boolean DEFAULT_PRINT_CASCADING = true;
   private final int m_stackDepth;
   private final boolean m_printCascading;
   // $FF: synthetic field
   static Class class$org$apache$avalon$framework$logger$Logger;

   public AvalonFormatter(String pattern) {
      this(pattern, 8, true);
   }

   public AvalonFormatter(String pattern, int stackDepth, boolean printCascading) {
      super(pattern);
      this.m_stackDepth = stackDepth;
      this.m_printCascading = printCascading;
   }

   protected String getStackTrace(Throwable throwable, String format) {
      return null == throwable ? "" : ExceptionUtil.printStackTrace(throwable, this.m_stackDepth, this.m_printCascading);
   }

   protected int getTypeIdFor(String type) {
      return type.equalsIgnoreCase("class") ? 9 : super.getTypeIdFor(type);
   }

   protected String formatPatternRun(LogEvent event, PatternFormatter.PatternRun run) {
      switch (run.m_type) {
         case 9:
            return this.getClass(run.m_format);
         default:
            return super.formatPatternRun(event, run);
      }
   }

   private String getClass(String format) {
      Class clazz = StackIntrospector.getCallerClass(class$org$apache$avalon$framework$logger$Logger == null ? (class$org$apache$avalon$framework$logger$Logger = class$("org.apache.avalon.framework.logger.Logger")) : class$org$apache$avalon$framework$logger$Logger);
      if (null == clazz) {
         return "Unknown-class";
      } else {
         String className = clazz.getName();
         if ("short".equalsIgnoreCase(format)) {
            int pos = className.lastIndexOf(46);
            if (pos >= 0) {
               className = className.substring(pos + 1);
            }
         }

         return className;
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
