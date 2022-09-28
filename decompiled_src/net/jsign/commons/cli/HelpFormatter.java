package net.jsign.commons.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HelpFormatter {
   public static final int DEFAULT_WIDTH = 74;
   public static final int DEFAULT_LEFT_PAD = 1;
   public static final int DEFAULT_DESC_PAD = 3;
   public static final String DEFAULT_SYNTAX_PREFIX = "usage: ";
   public static final String DEFAULT_OPT_PREFIX = "-";
   public static final String DEFAULT_LONG_OPT_PREFIX = "--";
   public static final String DEFAULT_LONG_OPT_SEPARATOR = " ";
   public static final String DEFAULT_ARG_NAME = "arg";
   /** @deprecated */
   @Deprecated
   public int defaultWidth = 74;
   /** @deprecated */
   @Deprecated
   public int defaultLeftPad = 1;
   /** @deprecated */
   @Deprecated
   public int defaultDescPad = 3;
   /** @deprecated */
   @Deprecated
   public String defaultSyntaxPrefix = "usage: ";
   /** @deprecated */
   @Deprecated
   public String defaultNewLine = System.getProperty("line.separator");
   /** @deprecated */
   @Deprecated
   public String defaultOptPrefix = "-";
   /** @deprecated */
   @Deprecated
   public String defaultLongOptPrefix = "--";
   /** @deprecated */
   @Deprecated
   public String defaultArgName = "arg";
   protected Comparator optionComparator = new OptionComparator();
   private String longOptSeparator = " ";

   public void setWidth(int width) {
      this.defaultWidth = width;
   }

   public int getWidth() {
      return this.defaultWidth;
   }

   public void setLeftPadding(int padding) {
      this.defaultLeftPad = padding;
   }

   public int getLeftPadding() {
      return this.defaultLeftPad;
   }

   public void setDescPadding(int padding) {
      this.defaultDescPad = padding;
   }

   public int getDescPadding() {
      return this.defaultDescPad;
   }

   public void setSyntaxPrefix(String prefix) {
      this.defaultSyntaxPrefix = prefix;
   }

   public String getSyntaxPrefix() {
      return this.defaultSyntaxPrefix;
   }

   public void setNewLine(String newline) {
      this.defaultNewLine = newline;
   }

   public String getNewLine() {
      return this.defaultNewLine;
   }

   public void setOptPrefix(String prefix) {
      this.defaultOptPrefix = prefix;
   }

   public String getOptPrefix() {
      return this.defaultOptPrefix;
   }

   public void setLongOptPrefix(String prefix) {
      this.defaultLongOptPrefix = prefix;
   }

   public String getLongOptPrefix() {
      return this.defaultLongOptPrefix;
   }

   public void setLongOptSeparator(String longOptSeparator) {
      this.longOptSeparator = longOptSeparator;
   }

   public String getLongOptSeparator() {
      return this.longOptSeparator;
   }

   public void setArgName(String name) {
      this.defaultArgName = name;
   }

   public String getArgName() {
      return this.defaultArgName;
   }

   public Comparator getOptionComparator() {
      return this.optionComparator;
   }

   public void setOptionComparator(Comparator comparator) {
      this.optionComparator = comparator;
   }

   public void printHelp(String cmdLineSyntax, Options options) {
      this.printHelp(this.getWidth(), cmdLineSyntax, (String)null, options, (String)null, false);
   }

   public void printHelp(String cmdLineSyntax, Options options, boolean autoUsage) {
      this.printHelp(this.getWidth(), cmdLineSyntax, (String)null, options, (String)null, autoUsage);
   }

   public void printHelp(String cmdLineSyntax, String header, Options options, String footer) {
      this.printHelp(cmdLineSyntax, header, options, footer, false);
   }

   public void printHelp(String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
      this.printHelp(this.getWidth(), cmdLineSyntax, header, options, footer, autoUsage);
   }

   public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer) {
      this.printHelp(width, cmdLineSyntax, header, options, footer, false);
   }

   public void printHelp(int width, String cmdLineSyntax, String header, Options options, String footer, boolean autoUsage) {
      PrintWriter pw = new PrintWriter(System.out);
      this.printHelp(pw, width, cmdLineSyntax, header, options, this.getLeftPadding(), this.getDescPadding(), footer, autoUsage);
      pw.flush();
   }

   public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer) {
      this.printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, false);
   }

   public void printHelp(PrintWriter pw, int width, String cmdLineSyntax, String header, Options options, int leftPad, int descPad, String footer, boolean autoUsage) {
      if (cmdLineSyntax != null && cmdLineSyntax.length() != 0) {
         if (autoUsage) {
            this.printUsage(pw, width, cmdLineSyntax, options);
         } else {
            this.printUsage(pw, width, cmdLineSyntax);
         }

         if (header != null && header.trim().length() > 0) {
            this.printWrapped(pw, width, header);
         }

         this.printOptions(pw, width, options, leftPad, descPad);
         if (footer != null && footer.trim().length() > 0) {
            this.printWrapped(pw, width, footer);
         }

      } else {
         throw new IllegalArgumentException("cmdLineSyntax not provided");
      }
   }

   public void printUsage(PrintWriter pw, int width, String app, Options options) {
      StringBuffer buff = (new StringBuffer(this.getSyntaxPrefix())).append(app).append(" ");
      Collection processedGroups = new ArrayList();
      List optList = new ArrayList(options.getOptions());
      if (this.getOptionComparator() != null) {
         Collections.sort(optList, this.getOptionComparator());
      }

      Iterator it = optList.iterator();

      while(it.hasNext()) {
         Option option = (Option)it.next();
         OptionGroup group = options.getOptionGroup(option);
         if (group != null) {
            if (!processedGroups.contains(group)) {
               processedGroups.add(group);
               this.appendOptionGroup(buff, group);
            }
         } else {
            this.appendOption(buff, option, option.isRequired());
         }

         if (it.hasNext()) {
            buff.append(" ");
         }
      }

      this.printWrapped(pw, width, buff.toString().indexOf(32) + 1, buff.toString());
   }

   private void appendOptionGroup(StringBuffer buff, OptionGroup group) {
      if (!group.isRequired()) {
         buff.append("[");
      }

      List optList = new ArrayList(group.getOptions());
      if (this.getOptionComparator() != null) {
         Collections.sort(optList, this.getOptionComparator());
      }

      Iterator it = optList.iterator();

      while(it.hasNext()) {
         this.appendOption(buff, (Option)it.next(), true);
         if (it.hasNext()) {
            buff.append(" | ");
         }
      }

      if (!group.isRequired()) {
         buff.append("]");
      }

   }

   private void appendOption(StringBuffer buff, Option option, boolean required) {
      if (!required) {
         buff.append("[");
      }

      if (option.getOpt() != null) {
         buff.append("-").append(option.getOpt());
      } else {
         buff.append("--").append(option.getLongOpt());
      }

      if (option.hasArg() && (option.getArgName() == null || option.getArgName().length() != 0)) {
         buff.append(option.getOpt() == null ? this.longOptSeparator : " ");
         buff.append("<").append(option.getArgName() != null ? option.getArgName() : this.getArgName()).append(">");
      }

      if (!required) {
         buff.append("]");
      }

   }

   public void printUsage(PrintWriter pw, int width, String cmdLineSyntax) {
      int argPos = cmdLineSyntax.indexOf(32) + 1;
      this.printWrapped(pw, width, this.getSyntaxPrefix().length() + argPos, this.getSyntaxPrefix() + cmdLineSyntax);
   }

   public void printOptions(PrintWriter pw, int width, Options options, int leftPad, int descPad) {
      StringBuffer sb = new StringBuffer();
      this.renderOptions(sb, width, options, leftPad, descPad);
      pw.println(sb.toString());
   }

   public void printWrapped(PrintWriter pw, int width, String text) {
      this.printWrapped(pw, width, 0, text);
   }

   public void printWrapped(PrintWriter pw, int width, int nextLineTabStop, String text) {
      StringBuffer sb = new StringBuffer(text.length());
      this.renderWrappedTextBlock(sb, width, nextLineTabStop, text);
      pw.println(sb.toString());
   }

   protected StringBuffer renderOptions(StringBuffer sb, int width, Options options, int leftPad, int descPad) {
      String lpad = this.createPadding(leftPad);
      String dpad = this.createPadding(descPad);
      int max = 0;
      List prefixList = new ArrayList();
      List optList = options.helpOptions();
      if (this.getOptionComparator() != null) {
         Collections.sort(optList, this.getOptionComparator());
      }

      StringBuffer optBuf;
      for(Iterator var11 = optList.iterator(); var11.hasNext(); max = optBuf.length() > max ? optBuf.length() : max) {
         Option option = (Option)var11.next();
         optBuf = new StringBuffer();
         if (option.getOpt() == null) {
            optBuf.append(lpad).append("   ").append(this.getLongOptPrefix()).append(option.getLongOpt());
         } else {
            optBuf.append(lpad).append(this.getOptPrefix()).append(option.getOpt());
            if (option.hasLongOpt()) {
               optBuf.append(',').append(this.getLongOptPrefix()).append(option.getLongOpt());
            }
         }

         if (option.hasArg()) {
            String argName = option.getArgName();
            if (argName != null && argName.length() == 0) {
               optBuf.append(' ');
            } else {
               optBuf.append(option.hasLongOpt() ? this.longOptSeparator : " ");
               optBuf.append("<").append(argName != null ? option.getArgName() : this.getArgName()).append(">");
            }
         }

         prefixList.add(optBuf);
      }

      int x = 0;
      Iterator it = optList.iterator();

      while(it.hasNext()) {
         Option option = (Option)it.next();
         StringBuilder optBuf = new StringBuilder(((StringBuffer)prefixList.get(x++)).toString());
         if (optBuf.length() < max) {
            optBuf.append(this.createPadding(max - optBuf.length()));
         }

         optBuf.append(dpad);
         int nextLineTabStop = max + descPad;
         if (option.getDescription() != null) {
            optBuf.append(option.getDescription());
         }

         this.renderWrappedText(sb, width, nextLineTabStop, optBuf.toString());
         if (it.hasNext()) {
            sb.append(this.getNewLine());
         }
      }

      return sb;
   }

   protected StringBuffer renderWrappedText(StringBuffer sb, int width, int nextLineTabStop, String text) {
      int pos = this.findWrapPos(text, width, 0);
      if (pos == -1) {
         sb.append(this.rtrim(text));
         return sb;
      } else {
         sb.append(this.rtrim(text.substring(0, pos))).append(this.getNewLine());
         if (nextLineTabStop >= width) {
            nextLineTabStop = 1;
         }

         String padding = this.createPadding(nextLineTabStop);

         while(true) {
            text = padding + text.substring(pos).trim();
            pos = this.findWrapPos(text, width, 0);
            if (pos == -1) {
               sb.append(text);
               return sb;
            }

            if (text.length() > width && pos == nextLineTabStop - 1) {
               pos = width;
            }

            sb.append(this.rtrim(text.substring(0, pos))).append(this.getNewLine());
         }
      }
   }

   private Appendable renderWrappedTextBlock(StringBuffer sb, int width, int nextLineTabStop, String text) {
      try {
         BufferedReader in = new BufferedReader(new StringReader(text));

         String line;
         for(boolean firstLine = true; (line = in.readLine()) != null; this.renderWrappedText(sb, width, nextLineTabStop, line)) {
            if (!firstLine) {
               sb.append(this.getNewLine());
            } else {
               firstLine = false;
            }
         }
      } catch (IOException var8) {
      }

      return sb;
   }

   protected int findWrapPos(String text, int width, int startPos) {
      int pos = text.indexOf(10, startPos);
      if (pos != -1 && pos <= width) {
         return pos + 1;
      } else {
         pos = text.indexOf(9, startPos);
         if (pos != -1 && pos <= width) {
            return pos + 1;
         } else if (startPos + width >= text.length()) {
            return -1;
         } else {
            for(pos = startPos + width; pos >= startPos; --pos) {
               char c = text.charAt(pos);
               if (c == ' ' || c == '\n' || c == '\r') {
                  break;
               }
            }

            if (pos > startPos) {
               return pos;
            } else {
               pos = startPos + width;
               return pos == text.length() ? -1 : pos;
            }
         }
      }
   }

   protected String createPadding(int len) {
      char[] padding = new char[len];
      Arrays.fill(padding, ' ');
      return new String(padding);
   }

   protected String rtrim(String s) {
      if (s != null && s.length() != 0) {
         int pos;
         for(pos = s.length(); pos > 0 && Character.isWhitespace(s.charAt(pos - 1)); --pos) {
         }

         return s.substring(0, pos);
      } else {
         return s;
      }
   }

   private static class OptionComparator implements Comparator, Serializable {
      private static final long serialVersionUID = 5305467873966684014L;

      private OptionComparator() {
      }

      public int compare(Option opt1, Option opt2) {
         return opt1.getKey().compareToIgnoreCase(opt2.getKey());
      }

      // $FF: synthetic method
      OptionComparator(Object x0) {
         this();
      }
   }
}
