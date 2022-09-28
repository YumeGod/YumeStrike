package com.mxgraph.swing.util;

import com.mxgraph.util.mxRectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.plaf.UIResource;

public class mxGraphTransferable implements Transferable, UIResource, Serializable {
   private static final long serialVersionUID = 5123819419918087664L;
   public static DataFlavor dataFlavor;
   private static DataFlavor[] htmlFlavors;
   private static DataFlavor[] stringFlavors;
   private static DataFlavor[] plainFlavors;
   private static DataFlavor[] imageFlavors;
   protected Object[] cells;
   protected mxRectangle bounds;
   protected ImageIcon image;

   public mxGraphTransferable(Object[] var1, mxRectangle var2) {
      this(var1, var2, (ImageIcon)null);
   }

   public mxGraphTransferable(Object[] var1, mxRectangle var2, ImageIcon var3) {
      this.cells = var1;
      this.bounds = var2;
      this.image = var3;
   }

   public Object[] getCells() {
      return this.cells;
   }

   public mxRectangle getBounds() {
      return this.bounds;
   }

   public ImageIcon getImage() {
      return this.image;
   }

   public DataFlavor[] getTransferDataFlavors() {
      DataFlavor[] var1 = this.getRicherFlavors();
      int var2 = var1 != null ? var1.length : 0;
      int var3 = this.isHtmlSupported() ? htmlFlavors.length : 0;
      int var4 = this.isPlainSupported() ? plainFlavors.length : 0;
      int var5 = this.isPlainSupported() ? stringFlavors.length : 0;
      int var6 = this.isImageSupported() ? stringFlavors.length : 0;
      int var7 = var2 + var3 + var4 + var5 + var6;
      DataFlavor[] var8 = new DataFlavor[var7];
      int var9 = 0;
      if (var2 > 0) {
         System.arraycopy(var1, 0, var8, var9, var2);
         var9 += var2;
      }

      if (var3 > 0) {
         System.arraycopy(htmlFlavors, 0, var8, var9, var3);
         var9 += var3;
      }

      if (var4 > 0) {
         System.arraycopy(plainFlavors, 0, var8, var9, var4);
         var9 += var4;
      }

      if (var5 > 0) {
         System.arraycopy(stringFlavors, 0, var8, var9, var5);
         var9 += var5;
      }

      if (var6 > 0) {
         System.arraycopy(imageFlavors, 0, var8, var9, var6);
         int var10000 = var9 + var6;
      }

      return var8;
   }

   protected DataFlavor[] getRicherFlavors() {
      return new DataFlavor[]{dataFlavor};
   }

   public boolean isDataFlavorSupported(DataFlavor var1) {
      DataFlavor[] var2 = this.getTransferDataFlavors();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3] != null && var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
      if (this.isRicherFlavor(var1)) {
         return this.getRicherData(var1);
      } else {
         if (this.isImageFlavor(var1)) {
            if (this.image != null && this.image.getImage() instanceof RenderedImage) {
               if (var1.equals(DataFlavor.imageFlavor)) {
                  return this.image.getImage();
               }

               ByteArrayOutputStream var2 = new ByteArrayOutputStream();
               ImageIO.write((RenderedImage)this.image.getImage(), "bmp", var2);
               return new ByteArrayInputStream(var2.toByteArray());
            }
         } else {
            String var3;
            if (this.isHtmlFlavor(var1)) {
               var3 = this.getHtmlData();
               var3 = var3 == null ? "" : var3;
               if (String.class.equals(var1.getRepresentationClass())) {
                  return var3;
               }

               if (Reader.class.equals(var1.getRepresentationClass())) {
                  return new StringReader(var3);
               }

               if (InputStream.class.equals(var1.getRepresentationClass())) {
                  return new ByteArrayInputStream(var3.getBytes());
               }
            } else if (this.isPlainFlavor(var1)) {
               var3 = this.getPlainData();
               var3 = var3 == null ? "" : var3;
               if (String.class.equals(var1.getRepresentationClass())) {
                  return var3;
               }

               if (Reader.class.equals(var1.getRepresentationClass())) {
                  return new StringReader(var3);
               }

               if (InputStream.class.equals(var1.getRepresentationClass())) {
                  return new ByteArrayInputStream(var3.getBytes());
               }
            } else if (this.isStringFlavor(var1)) {
               var3 = this.getPlainData();
               var3 = var3 == null ? "" : var3;
               return var3;
            }
         }

         throw new UnsupportedFlavorException(var1);
      }
   }

   protected boolean isRicherFlavor(DataFlavor var1) {
      DataFlavor[] var2 = this.getRicherFlavors();
      int var3 = var2 != null ? var2.length : 0;

      for(int var4 = 0; var4 < var3; ++var4) {
         if (var2[var4].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public Object getRicherData(DataFlavor var1) throws UnsupportedFlavorException {
      if (var1.equals(dataFlavor)) {
         return this;
      } else {
         throw new UnsupportedFlavorException(var1);
      }
   }

   protected boolean isHtmlFlavor(DataFlavor var1) {
      DataFlavor[] var2 = htmlFlavors;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   protected boolean isHtmlSupported() {
      return false;
   }

   protected String getHtmlData() {
      return null;
   }

   protected boolean isImageFlavor(DataFlavor var1) {
      int var2 = imageFlavors != null ? imageFlavors.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         if (imageFlavors[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public boolean isImageSupported() {
      return this.image != null;
   }

   protected boolean isPlainFlavor(DataFlavor var1) {
      DataFlavor[] var2 = plainFlavors;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   protected boolean isPlainSupported() {
      return false;
   }

   protected String getPlainData() {
      return null;
   }

   protected boolean isStringFlavor(DataFlavor var1) {
      DataFlavor[] var2 = stringFlavors;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         if (var2[var3].equals(var1)) {
            return true;
         }
      }

      return false;
   }

   static {
      try {
         htmlFlavors = new DataFlavor[3];
         htmlFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
         htmlFlavors[1] = new DataFlavor("text/html;class=java.io.Reader");
         htmlFlavors[2] = new DataFlavor("text/html;charset=unicode;class=java.io.InputStream");
         plainFlavors = new DataFlavor[3];
         plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
         plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
         plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
         stringFlavors = new DataFlavor[2];
         stringFlavors[0] = new DataFlavor("application/x-java-jvm-local-objectref;class=java.lang.String");
         stringFlavors[1] = DataFlavor.stringFlavor;
         imageFlavors = new DataFlavor[2];
         imageFlavors[0] = DataFlavor.imageFlavor;
         imageFlavors[1] = new DataFlavor("image/bmp");
      } catch (ClassNotFoundException var2) {
         System.err.println("error initializing javax.swing.plaf.basic.BasicTranserable");
      }

      try {
         dataFlavor = new DataFlavor("application/x-java-serialized-object; class=com.mxgraph.swing.util.mxGraphTransferable");
      } catch (ClassNotFoundException var1) {
      }

   }
}
