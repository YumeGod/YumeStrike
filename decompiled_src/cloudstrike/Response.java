package cloudstrike;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Response {
   public String status;
   public String mimeType;
   public InputStream data;
   public Map header;
   public Map params;
   public String uri;
   public long size;
   public long offset;

   public Response() {
      this.header = new LinkedHashMap();
      this.params = new LinkedHashMap();
      this.uri = "";
      this.size = 0L;
      this.offset = 0L;
      this.status = "200 OK";
   }

   public Response(String status, String mimeType, InputStream data) {
      this.header = new LinkedHashMap();
      this.params = new LinkedHashMap();
      this.uri = "";
      this.size = 0L;
      this.offset = 0L;
      this.status = status;
      this.mimeType = mimeType;
      this.data = data;
   }

   public Response(String status, String mimeType, InputStream data, long size) {
      this(status, mimeType, data);
      this.size = size;
      this.addHeader("Content-Length", size + "");
   }

   public Response(String status, String mimeType, String txt) {
      this.header = new LinkedHashMap();
      this.params = new LinkedHashMap();
      this.uri = "";
      this.size = 0L;
      this.offset = 0L;
      byte[] r = toBytes(txt);
      this.status = status;
      this.mimeType = mimeType;
      this.data = new ByteArrayInputStream(r);
      this.size = (long)r.length;
      this.addHeader("Content-Length", this.size + "");
   }

   public static final byte[] toBytes(String data) {
      int length = data.length();
      byte[] r = new byte[length];

      for(int x = 0; x < length; ++x) {
         r[x] = (byte)data.charAt(x);
      }

      return r;
   }

   public void addHeader(String entry) {
      String[] data = entry.split(": ");
      if (data.length == 1) {
         this.addHeader(data[0], "");
      } else {
         this.addHeader(data[0], data[1]);
      }

   }

   public void addHeader(String name, String value) {
      if ("".equals(value)) {
         this.header.remove(name);
      } else {
         this.header.put(name, value);
      }

   }

   public void addParameter(String entry) {
      String[] data = entry.split("=");
      if (data.length == 1) {
         this.params.put(data[0], "");
      } else {
         this.params.put(data[0], data[1]);
      }

   }

   public void orderHeaders(List order) {
      Map changed = new LinkedHashMap();
      Set missing = new LinkedHashSet(this.header.keySet());
      missing.removeAll(order);
      Iterator i = order.iterator();

      while(i.hasNext()) {
         String next = (String)i.next();
         if (this.header.containsKey(next)) {
            changed.put(next, this.header.get(next));
         } else if ("Date".equals(next)) {
            changed.put("Date", NanoHTTPD.getDate());
         } else if ("Content-Type".equals(next)) {
            changed.put("Content-Type", this.mimeType);
         }
      }

      Iterator j = missing.iterator();

      while(j.hasNext()) {
         String next = (String)j.next();
         changed.put(next, this.header.get(next));
      }

      this.header = changed;
   }
}
