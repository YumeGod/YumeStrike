package org.apache.batik.gvt.renderer;

public interface ImageRendererFactory extends RendererFactory {
   ImageRenderer createStaticImageRenderer();

   ImageRenderer createDynamicImageRenderer();
}
