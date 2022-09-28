package org.apache.james.mime4j.stream;

class BasicBodyDescriptor implements BodyDescriptor {
   private final String mimeType;
   private final String mediaType;
   private final String subType;
   private final String boundary;
   private final String charset;
   private final String transferEncoding;
   private final long contentLength;

   BasicBodyDescriptor(String mimeType, String mediaType, String subType, String boundary, String charset, String transferEncoding, long contentLength) {
      this.mimeType = mimeType;
      this.mediaType = mediaType;
      this.subType = subType;
      this.boundary = boundary;
      this.charset = charset;
      this.transferEncoding = transferEncoding;
      this.contentLength = contentLength;
   }

   public String getMimeType() {
      return this.mimeType;
   }

   public String getMediaType() {
      return this.mediaType;
   }

   public String getSubType() {
      return this.subType;
   }

   public String getBoundary() {
      return this.boundary;
   }

   public String getCharset() {
      return this.charset;
   }

   public String getTransferEncoding() {
      return this.transferEncoding;
   }

   public long getContentLength() {
      return this.contentLength;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("[mimeType=");
      sb.append(this.mimeType);
      sb.append(", mediaType=");
      sb.append(this.mediaType);
      sb.append(", subType=");
      sb.append(this.subType);
      sb.append(", boundary=");
      sb.append(this.boundary);
      sb.append(", charset=");
      sb.append(this.charset);
      sb.append("]");
      return sb.toString();
   }
}
