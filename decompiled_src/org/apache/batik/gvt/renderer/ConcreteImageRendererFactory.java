package org.apache.batik.gvt.renderer;

public class ConcreteImageRendererFactory implements ImageRendererFactory {
   static final boolean onMacOSX = "Mac OS X".equals(System.getProperty("os.name"));

   public Renderer createRenderer() {
      return this.createStaticImageRenderer();
   }

   public ImageRenderer createStaticImageRenderer() {
      return (ImageRenderer)(onMacOSX ? new MacRenderer() : new StaticRenderer());
   }

   public ImageRenderer createDynamicImageRenderer() {
      return (ImageRenderer)(onMacOSX ? new MacRenderer() : new DynamicRenderer());
   }
}
