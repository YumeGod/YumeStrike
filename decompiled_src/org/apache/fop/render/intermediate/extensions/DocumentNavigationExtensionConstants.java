package org.apache.fop.render.intermediate.extensions;

import org.apache.xmlgraphics.util.QName;

public interface DocumentNavigationExtensionConstants {
   String NAMESPACE = "http://xmlgraphics.apache.org/fop/intermediate/document-navigation";
   String PREFIX = "nav";
   QName BOOKMARK_TREE = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "bookmark-tree");
   QName BOOKMARK = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "bookmark");
   QName NAMED_DESTINATION = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "named-destination");
   QName LINK = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "link");
   QName GOTO_XY = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "goto-xy");
   QName GOTO_URI = new QName("http://xmlgraphics.apache.org/fop/intermediate/document-navigation", "nav", "goto-uri");
}
