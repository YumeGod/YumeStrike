package org.apache.batik.apps.svgbrowser;

import org.w3c.dom.Element;

public interface NodePickerController {
   boolean isEditable();

   boolean canEdit(Element var1);
}
