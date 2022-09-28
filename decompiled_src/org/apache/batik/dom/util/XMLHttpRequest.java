package org.apache.batik.dom.util;

import org.w3c.dom.Document;
import org.w3c.dom.events.EventListener;

public interface XMLHttpRequest {
   short UNSENT = 0;
   short OPENED = 1;
   short HEADERS_RECEIVED = 2;
   short LOADING = 3;
   short DONE = 4;

   EventListener getOnreadystatechange();

   void setOnreadystatechange(EventListener var1);

   short getReadyState();

   void open(String var1, String var2);

   void open(String var1, String var2, boolean var3);

   void open(String var1, String var2, boolean var3, String var4);

   void open(String var1, String var2, boolean var3, String var4, String var5);

   void setRequestHeader(String var1, String var2);

   void send();

   void send(String var1);

   void send(Document var1);

   void abort();

   String getAllResponseHeaders();

   String getResponseHeader(String var1);

   String getResponseText();

   String getResponseXML();

   short getStatus();

   String getStatusText();
}
