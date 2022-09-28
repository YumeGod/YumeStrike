package org.apache.xmlgraphics.image.loader.pipeline;

import org.apache.xmlgraphics.image.loader.spi.ImageConverter;
import org.apache.xmlgraphics.image.loader.util.Penalty;
import org.apache.xmlgraphics.util.dijkstra.Edge;
import org.apache.xmlgraphics.util.dijkstra.Vertex;

class ImageConversionEdge implements Edge {
   private ImageRepresentation source;
   private ImageRepresentation target;
   private ImageConverter converter;
   private int penalty;

   public ImageConversionEdge(ImageConverter converter, Penalty penalty) {
      this.converter = converter;
      this.source = new ImageRepresentation(converter.getSourceFlavor());
      this.target = new ImageRepresentation(converter.getTargetFlavor());
      this.penalty = Math.max(0, penalty.getValue());
   }

   public ImageConverter getImageConverter() {
      return this.converter;
   }

   public int getPenalty() {
      return this.penalty;
   }

   public Vertex getStart() {
      return this.source;
   }

   public Vertex getEnd() {
      return this.target;
   }
}
