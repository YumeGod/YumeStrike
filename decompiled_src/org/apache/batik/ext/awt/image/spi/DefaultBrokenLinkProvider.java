package org.apache.batik.ext.awt.image.spi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.ext.awt.image.renderable.Filter;
import org.apache.batik.ext.awt.image.renderable.RedRable;
import org.apache.batik.i18n.LocalizableSupport;

public class DefaultBrokenLinkProvider extends BrokenLinkProvider {
   static Filter brokenLinkImg = null;
   static final String MESSAGE_RSRC = "resources.Messages";
   static final Color BROKEN_LINK_COLOR = new Color(255, 255, 255, 190);
   // $FF: synthetic field
   static Class class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider;

   public static String formatMessage(Object var0, String var1, Object[] var2) {
      ClassLoader var3 = null;

      try {
         var3 = (class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider == null ? (class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider = class$("org.apache.batik.ext.awt.image.spi.DefaultBrokenLinkProvider")) : class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider).getClassLoader();
         var3 = var0.getClass().getClassLoader();
      } catch (SecurityException var5) {
      }

      LocalizableSupport var4 = new LocalizableSupport("resources.Messages", var0.getClass(), var3);
      return var4.formatMessage(var1, var2);
   }

   public Filter getBrokenLinkImage(Object var1, String var2, Object[] var3) {
      synchronized(class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider == null ? (class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider = class$("org.apache.batik.ext.awt.image.spi.DefaultBrokenLinkProvider")) : class$org$apache$batik$ext$awt$image$spi$DefaultBrokenLinkProvider) {
         if (brokenLinkImg != null) {
            return brokenLinkImg;
         } else {
            BufferedImage var5 = new BufferedImage(100, 100, 2);
            Hashtable var6 = new Hashtable();
            var6.put("org.apache.batik.BrokenLinkImage", formatMessage(var1, var2, var3));
            var5 = new BufferedImage(var5.getColorModel(), var5.getRaster(), var5.isAlphaPremultiplied(), var6);
            Graphics2D var7 = var5.createGraphics();
            var7.setColor(BROKEN_LINK_COLOR);
            var7.fillRect(0, 0, 100, 100);
            var7.setColor(Color.black);
            var7.drawRect(2, 2, 96, 96);
            var7.drawString("Broken Image", 6, 50);
            var7.dispose();
            brokenLinkImg = new RedRable(GraphicsUtil.wrap(var5));
            return brokenLinkImg;
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
}
