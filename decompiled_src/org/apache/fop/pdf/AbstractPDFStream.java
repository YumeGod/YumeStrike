package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.fop.util.CloseBlockerOutputStream;

public abstract class AbstractPDFStream extends PDFDictionary {
   private PDFFilterList filters;

   protected void setupFilterList() {
      if (!this.getFilterList().isInitialized()) {
         this.getFilterList().addDefaultFilters(this.getDocumentSafely().getFilterMap(), this.getDefaultFilterName());
      }

      this.prepareImplicitFilters();
      this.getDocument().applyEncryption(this);
   }

   protected String getDefaultFilterName() {
      return "default";
   }

   public PDFFilterList getFilterList() {
      if (this.filters == null) {
         if (this.getDocument() == null) {
            this.filters = new PDFFilterList();
         } else {
            this.filters = new PDFFilterList(this.getDocument().isEncryptionActive());
         }

         boolean hasFilterEntries = this.get("Filter") != null;
         if (hasFilterEntries) {
            this.filters.setDisableAllFilters(true);
         }
      }

      return this.filters;
   }

   protected abstract int getSizeHint() throws IOException;

   protected abstract void outputRawStreamData(OutputStream var1) throws IOException;

   protected int outputStreamData(StreamCache encodedStream, OutputStream out) throws IOException {
      int length = 0;
      byte[] p = encode("stream\n");
      out.write(p);
      length += p.length;
      encodedStream.outputContents(out);
      length += encodedStream.getSize();
      p = encode("\nendstream");
      out.write(p);
      length += p.length;
      return length;
   }

   protected StreamCache encodeStream() throws IOException {
      StreamCache encodedStream = StreamCacheFactory.getInstance().createStreamCache(this.getSizeHint());
      OutputStream filteredOutput = this.getFilterList().applyFilters(encodedStream.getOutputStream());
      this.outputRawStreamData(filteredOutput);
      filteredOutput.flush();
      filteredOutput.close();
      return encodedStream;
   }

   protected int encodeAndWriteStream(OutputStream out, PDFNumber refLength) throws IOException {
      int bytesWritten = 0;
      byte[] buf = encode("stream\n");
      out.write(buf);
      bytesWritten += buf.length;
      CloseBlockerOutputStream cbout = new CloseBlockerOutputStream(out);
      CountingOutputStream cout = new CountingOutputStream(cbout);
      OutputStream filteredOutput = this.getFilterList().applyFilters(cout);
      this.outputRawStreamData(filteredOutput);
      filteredOutput.close();
      refLength.setNumber(new Integer(cout.getCount()));
      bytesWritten += cout.getCount();
      buf = encode("\nendstream");
      out.write(buf);
      bytesWritten += buf.length;
      return bytesWritten;
   }

   protected int output(OutputStream stream) throws IOException {
      this.setupFilterList();
      CountingOutputStream cout = new CountingOutputStream(stream);
      Writer writer = PDFDocument.getWriterFor(cout);
      writer.write(this.getObjectID());
      StreamCache encodedStream = null;
      PDFNumber refLength = null;
      Object lengthEntry;
      if (this.getDocument().isEncodingOnTheFly()) {
         refLength = new PDFNumber();
         this.getDocumentSafely().registerObject(refLength);
         lengthEntry = refLength;
      } else {
         encodedStream = this.encodeStream();
         lengthEntry = new Integer(encodedStream.getSize() + 1);
      }

      this.populateStreamDict(lengthEntry);
      this.writeDictionary(cout, writer);
      writer.flush();
      if (encodedStream == null) {
         this.encodeAndWriteStream(cout, refLength);
      } else {
         this.outputStreamData(encodedStream, cout);
         encodedStream.clear();
      }

      writer.write("\nendobj\n");
      writer.flush();
      return cout.getCount();
   }

   protected void populateStreamDict(Object lengthEntry) {
      this.put("Length", lengthEntry);
      if (!this.getFilterList().isDisableAllFilters()) {
         this.getFilterList().putFilterDictEntries(this);
      }

   }

   protected void prepareImplicitFilters() {
   }
}
