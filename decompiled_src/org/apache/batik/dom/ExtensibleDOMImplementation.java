package org.apache.batik.dom;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.MissingResourceException;
import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.value.ShorthandManager;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.css.parser.ExtendedParser;
import org.apache.batik.css.parser.ExtendedParserWrapper;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.i18n.Localizable;
import org.apache.batik.i18n.LocalizableSupport;
import org.apache.batik.util.DoublyIndexedTable;
import org.apache.batik.util.Service;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.css.sac.Parser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.css.DOMImplementationCSS;
import org.w3c.dom.css.ViewCSS;

public abstract class ExtensibleDOMImplementation extends AbstractDOMImplementation implements DOMImplementationCSS, StyleSheetFactory, Localizable {
   protected DoublyIndexedTable customFactories;
   protected List customValueManagers;
   protected List customShorthandManagers;
   protected static final String RESOURCES = "org.apache.batik.dom.resources.Messages";
   protected LocalizableSupport localizableSupport;
   protected static List extensions = null;
   // $FF: synthetic field
   static Class class$org$apache$batik$dom$DomExtension;

   public ExtensibleDOMImplementation() {
      this.initLocalizable();
      Iterator var1 = getDomExtensions().iterator();

      while(var1.hasNext()) {
         DomExtension var2 = (DomExtension)var1.next();
         var2.registerTags(this);
      }

   }

   public void setLocale(Locale var1) {
      this.localizableSupport.setLocale(var1);
   }

   public Locale getLocale() {
      return this.localizableSupport.getLocale();
   }

   protected void initLocalizable() {
      this.localizableSupport = new LocalizableSupport("org.apache.batik.dom.resources.Messages", this.getClass().getClassLoader());
   }

   public String formatMessage(String var1, Object[] var2) throws MissingResourceException {
      return this.localizableSupport.formatMessage(var1, var2);
   }

   public void registerCustomElementFactory(String var1, String var2, ElementFactory var3) {
      if (this.customFactories == null) {
         this.customFactories = new DoublyIndexedTable();
      }

      this.customFactories.put(var1, var2, var3);
   }

   public void registerCustomCSSValueManager(ValueManager var1) {
      if (this.customValueManagers == null) {
         this.customValueManagers = new LinkedList();
      }

      this.customValueManagers.add(var1);
   }

   public void registerCustomCSSShorthandManager(ShorthandManager var1) {
      if (this.customShorthandManagers == null) {
         this.customShorthandManagers = new LinkedList();
      }

      this.customShorthandManagers.add(var1);
   }

   public CSSEngine createCSSEngine(AbstractStylableDocument var1, CSSContext var2) {
      String var3 = XMLResourceDescriptor.getCSSParserClassName();

      Parser var4;
      try {
         var4 = (Parser)Class.forName(var3).newInstance();
      } catch (ClassNotFoundException var10) {
         throw new DOMException((short)15, this.formatMessage("css.parser.class", new Object[]{var3}));
      } catch (InstantiationException var11) {
         throw new DOMException((short)15, this.formatMessage("css.parser.creation", new Object[]{var3}));
      } catch (IllegalAccessException var12) {
         throw new DOMException((short)15, this.formatMessage("css.parser.access", new Object[]{var3}));
      }

      ExtendedParser var5 = ExtendedParserWrapper.wrap(var4);
      ValueManager[] var6;
      if (this.customValueManagers == null) {
         var6 = new ValueManager[0];
      } else {
         var6 = new ValueManager[this.customValueManagers.size()];
         Iterator var7 = this.customValueManagers.iterator();

         for(int var8 = 0; var7.hasNext(); var6[var8++] = (ValueManager)var7.next()) {
         }
      }

      ShorthandManager[] var13;
      if (this.customShorthandManagers == null) {
         var13 = new ShorthandManager[0];
      } else {
         var13 = new ShorthandManager[this.customShorthandManagers.size()];
         Iterator var14 = this.customShorthandManagers.iterator();

         for(int var9 = 0; var14.hasNext(); var13[var9++] = (ShorthandManager)var14.next()) {
         }
      }

      CSSEngine var15 = this.createCSSEngine(var1, var2, var5, var6, var13);
      var1.setCSSEngine(var15);
      return var15;
   }

   public abstract CSSEngine createCSSEngine(AbstractStylableDocument var1, CSSContext var2, ExtendedParser var3, ValueManager[] var4, ShorthandManager[] var5);

   public abstract ViewCSS createViewCSS(AbstractStylableDocument var1);

   public Element createElementNS(AbstractDocument var1, String var2, String var3) {
      if (var2 != null && var2.length() == 0) {
         var2 = null;
      }

      if (var2 == null) {
         return new GenericElement(var3.intern(), var1);
      } else {
         if (this.customFactories != null) {
            String var4 = DOMUtilities.getLocalName(var3);
            ElementFactory var5 = (ElementFactory)this.customFactories.get(var2, var4);
            if (var5 != null) {
               return var5.create(DOMUtilities.getPrefix(var3), var1);
            }
         }

         return new GenericElementNS(var2.intern(), var3.intern(), var1);
      }
   }

   protected static synchronized List getDomExtensions() {
      if (extensions != null) {
         return extensions;
      } else {
         extensions = new LinkedList();
         Iterator var0 = Service.providers(class$org$apache$batik$dom$DomExtension == null ? (class$org$apache$batik$dom$DomExtension = class$("org.apache.batik.dom.DomExtension")) : class$org$apache$batik$dom$DomExtension);

         while(true) {
            label29:
            while(var0.hasNext()) {
               DomExtension var1 = (DomExtension)var0.next();
               float var2 = var1.getPriority();
               ListIterator var3 = extensions.listIterator();

               DomExtension var4;
               do {
                  if (!var3.hasNext()) {
                     var3.add(var1);
                     continue label29;
                  }

                  var4 = (DomExtension)var3.next();
               } while(!(var4.getPriority() > var2));

               var3.previous();
               var3.add(var1);
            }

            return extensions;
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public interface ElementFactory {
      Element create(String var1, Document var2);
   }
}
