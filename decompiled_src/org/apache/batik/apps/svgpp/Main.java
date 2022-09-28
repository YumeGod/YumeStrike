package org.apache.batik.apps.svgpp;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;

public class Main {
   public static final String BUNDLE_CLASSNAME = "org.apache.batik.apps.svgpp.resources.Messages";
   protected static LocalizableSupport localizableSupport;
   protected String[] arguments;
   protected int index;
   protected Map handlers = new HashMap();
   protected Transcoder transcoder;
   // $FF: synthetic field
   static Class class$org$apache$batik$apps$svgpp$Main;

   public static void main(String[] var0) {
      (new Main(var0)).run();
   }

   public Main(String[] var1) {
      this.handlers.put("-doctype", new DoctypeHandler());
      this.handlers.put("-doc-width", new DocWidthHandler());
      this.handlers.put("-newline", new NewlineHandler());
      this.handlers.put("-public-id", new PublicIdHandler());
      this.handlers.put("-no-format", new NoFormatHandler());
      this.handlers.put("-system-id", new SystemIdHandler());
      this.handlers.put("-tab-width", new TabWidthHandler());
      this.handlers.put("-xml-decl", new XMLDeclHandler());
      this.transcoder = new SVGTranscoder();
      this.arguments = var1;
   }

   public void run() {
      if (this.arguments.length == 0) {
         this.printUsage();
      } else {
         try {
            while(true) {
               OptionHandler var1 = (OptionHandler)this.handlers.get(this.arguments[this.index]);
               if (var1 == null) {
                  TranscoderInput var4 = new TranscoderInput(new FileReader(this.arguments[this.index++]));
                  TranscoderOutput var2;
                  if (this.index < this.arguments.length) {
                     var2 = new TranscoderOutput(new FileWriter(this.arguments[this.index]));
                  } else {
                     var2 = new TranscoderOutput(new OutputStreamWriter(System.out));
                  }

                  this.transcoder.transcode(var4, var2);
                  break;
               }

               var1.handleOption();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
            this.printUsage();
         }

      }
   }

   protected void printUsage() {
      this.printHeader();
      System.out.println(localizableSupport.formatMessage("syntax", (Object[])null));
      System.out.println();
      System.out.println(localizableSupport.formatMessage("options", (Object[])null));
      Iterator var1 = this.handlers.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         System.out.println(((OptionHandler)this.handlers.get(var2)).getDescription());
      }

   }

   protected void printHeader() {
      System.out.println(localizableSupport.formatMessage("header", (Object[])null));
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      localizableSupport = new LocalizableSupport("org.apache.batik.apps.svgpp.resources.Messages", (class$org$apache$batik$apps$svgpp$Main == null ? (class$org$apache$batik$apps$svgpp$Main = class$("org.apache.batik.apps.svgpp.Main")) : class$org$apache$batik$apps$svgpp$Main).getClassLoader());
   }

   protected class DocWidthHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_DOCUMENT_WIDTH, new Integer(Main.this.arguments[Main.this.index++]));
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("doc-width.description", (Object[])null);
      }
   }

   protected class TabWidthHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_TABULATION_WIDTH, new Integer(Main.this.arguments[Main.this.index++]));
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("tab-width.description", (Object[])null);
      }
   }

   protected class XMLDeclHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            String var1 = Main.this.arguments[Main.this.index++];
            Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_XML_DECLARATION, var1);
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("xml-decl.description", (Object[])null);
      }
   }

   protected class SystemIdHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            String var1 = Main.this.arguments[Main.this.index++];
            Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_SYSTEM_ID, var1);
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("system-id.description", (Object[])null);
      }
   }

   protected class PublicIdHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            String var1 = Main.this.arguments[Main.this.index++];
            Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_PUBLIC_ID, var1);
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("public-id.description", (Object[])null);
      }
   }

   protected class NoFormatHandler implements OptionHandler {
      public void handleOption() {
         ++Main.this.index;
         Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_FORMAT, Boolean.FALSE);
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("no-format.description", (Object[])null);
      }
   }

   protected class NewlineHandler implements OptionHandler {
      protected final Map values = new HashMap(6);

      protected NewlineHandler() {
         this.values.put("cr", SVGTranscoder.VALUE_NEWLINE_CR);
         this.values.put("cr-lf", SVGTranscoder.VALUE_NEWLINE_CR_LF);
         this.values.put("lf", SVGTranscoder.VALUE_NEWLINE_LF);
      }

      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            Object var1 = this.values.get(Main.this.arguments[Main.this.index++]);
            if (var1 == null) {
               throw new IllegalArgumentException();
            } else {
               Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_NEWLINE, var1);
            }
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("newline.description", (Object[])null);
      }
   }

   protected class DoctypeHandler implements OptionHandler {
      protected final Map values = new HashMap(6);

      protected DoctypeHandler() {
         this.values.put("remove", SVGTranscoder.VALUE_DOCTYPE_REMOVE);
         this.values.put("change", SVGTranscoder.VALUE_DOCTYPE_CHANGE);
      }

      public void handleOption() {
         ++Main.this.index;
         if (Main.this.index >= Main.this.arguments.length) {
            throw new IllegalArgumentException();
         } else {
            Object var1 = this.values.get(Main.this.arguments[Main.this.index++]);
            if (var1 == null) {
               throw new IllegalArgumentException();
            } else {
               Main.this.transcoder.addTranscodingHint(SVGTranscoder.KEY_DOCTYPE, var1);
            }
         }
      }

      public String getDescription() {
         return Main.localizableSupport.formatMessage("doctype.description", (Object[])null);
      }
   }

   protected interface OptionHandler {
      void handleOption();

      String getDescription();
   }
}
