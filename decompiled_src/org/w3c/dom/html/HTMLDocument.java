package org.w3c.dom.html;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public interface HTMLDocument extends Document {
   String getTitle();

   void setTitle(String var1);

   String getReferrer();

   String getDomain();

   String getURL();

   HTMLElement getBody();

   void setBody(HTMLElement var1);

   HTMLCollection getImages();

   HTMLCollection getApplets();

   HTMLCollection getLinks();

   HTMLCollection getForms();

   HTMLCollection getAnchors();

   String getCookie();

   void setCookie(String var1);

   void open();

   void close();

   void write(String var1);

   void writeln(String var1);

   Element getElementById(String var1);

   NodeList getElementsByName(String var1);
}
