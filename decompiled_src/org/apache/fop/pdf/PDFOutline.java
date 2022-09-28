package org.apache.fop.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFOutline extends PDFObject {
   private List subentries = new ArrayList();
   private PDFOutline parent = null;
   private PDFOutline prev = null;
   private PDFOutline next = null;
   private PDFOutline first = null;
   private PDFOutline last = null;
   private int count = 0;
   private boolean openItem = false;
   private String title;
   private String actionRef;

   public PDFOutline(String title, String action, boolean openItem) {
      this.title = title;
      this.actionRef = action;
      this.openItem = openItem;
   }

   public void setTitle(String t) {
      this.title = t;
   }

   public void addOutline(PDFOutline outline) {
      if (this.subentries.size() > 0) {
         outline.prev = (PDFOutline)this.subentries.get(this.subentries.size() - 1);
         outline.prev.next = outline;
      } else {
         this.first = outline;
      }

      this.subentries.add(outline);
      outline.parent = this;
      this.incrementCount();
      this.last = outline;
   }

   private void incrementCount() {
      ++this.count;
      if (this.parent != null) {
         this.parent.incrementCount();
      }

   }

   protected byte[] toPDF() {
      ByteArrayOutputStream bout = new ByteArrayOutputStream(128);

      try {
         bout.write(encode(this.getObjectID()));
         bout.write(encode("<<"));
         if (this.parent == null) {
            if (this.first != null && this.last != null) {
               bout.write(encode(" /First " + this.first.referencePDF() + "\n"));
               bout.write(encode(" /Last " + this.last.referencePDF() + "\n"));
            }
         } else {
            bout.write(encode(" /Title "));
            bout.write(this.encodeText(this.title));
            bout.write(encode("\n"));
            bout.write(encode(" /Parent " + this.parent.referencePDF() + "\n"));
            if (this.prev != null) {
               bout.write(encode(" /Prev " + this.prev.referencePDF() + "\n"));
            }

            if (this.next != null) {
               bout.write(encode(" /Next " + this.next.referencePDF() + "\n"));
            }

            if (this.first != null && this.last != null) {
               bout.write(encode(" /First " + this.first.referencePDF() + "\n"));
               bout.write(encode(" /Last " + this.last.referencePDF() + "\n"));
            }

            if (this.count > 0) {
               bout.write(encode(" /Count " + (this.openItem ? "" : "-") + this.count + "\n"));
            }

            if (this.actionRef != null) {
               bout.write(encode(" /A " + this.actionRef + "\n"));
            }
         }

         bout.write(encode(">> endobj\n"));
      } catch (IOException var3) {
         log.error("Ignored I/O exception", var3);
      }

      return bout.toByteArray();
   }
}
