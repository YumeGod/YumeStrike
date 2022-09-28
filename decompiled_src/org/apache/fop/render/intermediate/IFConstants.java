package org.apache.fop.render.intermediate;

import org.apache.fop.util.XMLConstants;

public interface IFConstants extends XMLConstants {
   String MIME_TYPE = "application/X-fop-intermediate-format";
   String NAMESPACE = "http://xmlgraphics.apache.org/fop/intermediate";
   String EL_DOCUMENT = "document";
   String EL_HEADER = "header";
   String EL_TRAILER = "trailer";
   String EL_PAGE_SEQUENCE = "page-sequence";
   String EL_PAGE = "page";
   String EL_PAGE_HEADER = "page-header";
   String EL_PAGE_TRAILER = "page-trailer";
   String EL_PAGE_CONTENT = "content";
   String EL_VIEWPORT = "viewport";
   String EL_GROUP = "g";
   String EL_IMAGE = "image";
   String EL_CLIP_RECT = "clip-rect";
   String EL_RECT = "rect";
   String EL_LINE = "line";
   String EL_BORDER_RECT = "border-rect";
   String EL_FONT = "font";
   String EL_TEXT = "text";
   String EL_STRUCTURE_TREE = "structure-tree";
}
