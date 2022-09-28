package org.w3c.dom.stylesheets;

import org.w3c.dom.Node;

public interface StyleSheet {
   String getType();

   boolean getDisabled();

   void setDisabled(boolean var1);

   Node getOwnerNode();

   StyleSheet getParentStyleSheet();

   String getHref();

   String getTitle();

   MediaList getMedia();
}
