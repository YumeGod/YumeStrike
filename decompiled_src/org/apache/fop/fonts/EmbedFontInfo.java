package org.apache.fop.fonts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class EmbedFontInfo implements Serializable {
   private static final long serialVersionUID = 8755432068669997368L;
   protected String metricsFile;
   protected String embedFile;
   protected boolean kerning;
   protected EncodingMode encodingMode;
   protected String postScriptName;
   protected String subFontName;
   private List fontTriplets;
   private transient boolean embedded;

   public EmbedFontInfo(String metricsFile, boolean kerning, List fontTriplets, String embedFile, String subFontName) {
      this.encodingMode = EncodingMode.AUTO;
      this.postScriptName = null;
      this.subFontName = null;
      this.fontTriplets = null;
      this.embedded = true;
      this.metricsFile = metricsFile;
      this.embedFile = embedFile;
      this.kerning = kerning;
      this.fontTriplets = fontTriplets;
      this.subFontName = subFontName;
   }

   public String getMetricsFile() {
      return this.metricsFile;
   }

   public String getEmbedFile() {
      return this.embedFile;
   }

   public boolean getKerning() {
      return this.kerning;
   }

   public String getSubFontName() {
      return this.subFontName;
   }

   public String getPostScriptName() {
      return this.postScriptName;
   }

   public void setPostScriptName(String postScriptName) {
      this.postScriptName = postScriptName;
   }

   public List getFontTriplets() {
      return this.fontTriplets;
   }

   public boolean isEmbedded() {
      return this.metricsFile != null && this.embedFile == null ? false : this.embedded;
   }

   public void setEmbedded(boolean value) {
      this.embedded = value;
   }

   public EncodingMode getEncodingMode() {
      return this.encodingMode;
   }

   public void setEncodingMode(EncodingMode mode) {
      if (mode == null) {
         throw new NullPointerException("mode must not be null");
      } else {
         this.encodingMode = mode;
      }
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.embedded = true;
   }

   public String toString() {
      return "metrics-url=" + this.metricsFile + ", embed-url=" + this.embedFile + ", kerning=" + this.kerning + ", enc-mode=" + this.encodingMode + ", font-triplet=" + this.fontTriplets + (this.getSubFontName() != null ? ", sub-font=" + this.getSubFontName() : "") + (this.isEmbedded() ? "" : ", NOT embedded");
   }
}
