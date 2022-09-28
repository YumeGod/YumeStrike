package org.apache.batik.dom;

import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.Node;
import org.w3c.dom.stylesheets.StyleSheet;

public interface StyleSheetFactory {
   StyleSheet createStyleSheet(Node var1, HashTable var2);
}
