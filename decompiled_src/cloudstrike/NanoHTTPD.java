package cloudstrike;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NanoHTTPD {
   public static final String HTTP_OK = "200 OK";
   public static final String HTTP_PARTIAL_CONTENT = "206 Partial Content";
   public static final String HTTP_MULTISTATE = "207 Multi-Status";
   public static final String HTTP_REDIRECT = "301 Moved Permanently";
   public static final String HTTP_NOT_MODIFIED = "304 Not Modified";
   public static final String HTTP_FORBIDDEN = "403 Forbidden";
   public static final String HTTP_NOTFOUND = "404 Not Found";
   public static final String HTTP_BADREQUEST = "400 Bad Request";
   public static final String HTTP_TOOLARGE = "413 Entity Too Large";
   public static final String HTTP_RANGE_NOT_SATISFIABLE = "416 Range Not Satisfiable";
   public static final String HTTP_INTERNALERROR = "500 Internal Server Error";
   public static final String HTTP_NOTIMPLEMENTED = "501 Not Implemented";
   public static final String MIME_PLAINTEXT = "text/plain";
   public static final String MIME_HTML = "text/html";
   public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
   protected boolean isssl;
   private ServerSocket ss;
   protected boolean alive;
   protected Thread fred;
   protected ResponseFilter filter;
   private int myTcpPort;
   private static SimpleDateFormat gmtFrmt;

   public static final void print_info(String message) {
      System.out.println("\u001b[01;34m[*]\u001b[0m " + message);
   }

   public static final void print_warn(String message) {
      System.out.println("\u001b[01;33m[!]\u001b[0m " + message);
   }

   public static final void print_error(String message) {
      System.out.println("\u001b[01;31m[-]\u001b[0m " + message);
   }

   public static void logException(String activity, Throwable ex, boolean expected) {
      if (expected) {
         print_warn("Trapped " + ex.getClass().getName() + " during " + activity + " [" + Thread.currentThread().getName() + "]: " + ex.getMessage());
      } else {
         print_error("Trapped " + ex.getClass().getName() + " during " + activity + " [" + Thread.currentThread().getName() + "]: " + ex.getMessage());
         ex.printStackTrace();
      }

   }

   public Response serve(String uri, String method, Properties header, Properties parms) {
      return new Response("404 Not Found", "text/plain", "This is the default!");
   }

   public NanoHTTPD(int port) throws IOException {
      this(port, false, (InputStream)null, (String)null);
   }

   public boolean alwaysRaw(String uri) {
      return false;
   }

   public NanoHTTPD(int port, boolean ssl, InputStream keystore, String password) throws IOException {
      this.isssl = false;
      this.ss = null;
      this.alive = true;
      this.filter = null;
      this.myTcpPort = port;
      this.listen(ssl, keystore, password);
   }

   public boolean isSSL() {
      return this.isssl;
   }

   public SSLServerSocketFactory getSSLFactory(InputStream ksIs, String password) {
      try {
         if (ksIs == null) {
            ksIs = this.getClass().getClassLoader().getResourceAsStream("resources/ssl.store");
            password = "123456";
            print_warn("Web Server will use default SSL certificate (you don't want this).\n\tUse a valid SSL certificate with Cobalt Strike: https://www.cobaltstrike.com/help-malleable-c2#validssl");
         } else {
            print_info("Web Server will use user-specified SSL certifcate");
         }

         KeyStore ks = KeyStore.getInstance("JKS");
         ks.load(ksIs, password.toCharArray());
         ksIs.close();
         KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
         kmf.init(ks, password.toCharArray());
         SSLContext sslcontext = SSLContext.getInstance("SSL");
         sslcontext.init(kmf.getKeyManagers(), new TrustManager[]{new TrustEverything()}, new SecureRandom());
         SSLServerSocketFactory factory = sslcontext.getServerSocketFactory();
         return factory;
      } catch (Exception var7) {
         logException("SSL certificate setup", var7, false);
         return null;
      }
   }

   public void listen(boolean ssl, InputStream keystore, String password) throws IOException {
      if (ssl) {
         this.isssl = true;
         ServerSocketFactory factory = this.getSSLFactory(keystore, password);
         this.ss = factory.createServerSocket(this.myTcpPort, 32);
         ((SSLServerSocket)this.ss).setEnabledCipherSuites(((SSLServerSocket)this.ss).getSupportedCipherSuites());
      } else {
         this.ss = new ServerSocket(this.myTcpPort);
      }

      Thread t = new Thread(new Runnable() {
         public void run() {
            try {
               NanoHTTPD.this.ss.setSoTimeout(500);

               while(NanoHTTPD.this.alive) {
                  try {
                     Socket temp = NanoHTTPD.this.ss.accept();
                     if (temp != null) {
                        NanoHTTPD.this.new HTTPSession(temp);
                     }
                  } catch (SocketTimeoutException var2) {
                  }
               }
            } catch (IOException var3) {
               NanoHTTPD.print_error("Web Server on port " + NanoHTTPD.this.myTcpPort + " error: " + var3.getMessage());
               var3.printStackTrace();
            }

            NanoHTTPD.print_info("Web Server on port " + NanoHTTPD.this.myTcpPort + " stopped");
         }
      }, "Web Server on port " + this.myTcpPort);
      t.setDaemon(true);
      t.start();
      this.fred = t;
   }

   public void setResponseFilter(ResponseFilter filter) {
      this.filter = filter;
   }

   public void stop() {
      this.alive = false;
      this.fred.interrupt();

      try {
         this.ss.close();
      } catch (IOException var2) {
         logException("stop web server", var2, false);
      }

   }

   private String encodeUri(String uri) {
      try {
         return URLEncoder.encode(uri, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         return URLEncoder.encode(uri);
      }
   }

   public int getPort() {
      return this.myTcpPort;
   }

   public static String getDate() {
      return gmtFrmt.format(new Date());
   }

   public static void main(String[] args) throws IOException {
      new NanoHTTPD(443, true, (InputStream)null, (String)null);

      while(true) {
         Thread.yield();
      }
   }

   static {
      gmtFrmt = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
      gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
      System.setProperty("https.protocols", "SSLv3,SSLv2Hello,TLSv1");
   }

   private class HTTPSession implements Runnable {
      private Socket mySocket;

      public HTTPSession(Socket s) {
         this.mySocket = s;
         Thread t = new Thread(this, "HTTP session handler");
         t.setDaemon(true);
         t.start();
      }

      private String readLine(DataInputStream in) throws IOException {
         StringBuffer buffer = new StringBuffer();

         while(true) {
            int read = in.readUnsignedByte();
            if ((char)read == '\n') {
               return buffer.toString();
            }

            if ((char)read == '\r') {
               boolean var4 = false;
            } else {
               buffer.append((char)read);
            }
         }
      }

      public void run() {
         try {
            InputStream is = this.mySocket.getInputStream();
            if (is == null) {
               return;
            }

            DataInputStream in = new DataInputStream(is);
            String request = this.readLine(in);
            StringTokenizer st = new StringTokenizer(request == null ? "" : request);
            if (!st.hasMoreTokens()) {
               NanoHTTPD.print_error("Dropped HTTP client from " + this.mySocket.getInetAddress().toString() + " (provided no input)");
               throw new InterruptedException();
            }

            String method = st.nextToken();
            if (!st.hasMoreTokens()) {
               NanoHTTPD.print_error("Dropped HTTP client from " + this.mySocket.getInetAddress().toString() + " (missing URI)");
               throw new InterruptedException();
            }

            String uri = st.nextToken();
            Properties header = new Properties();
            Properties parms = new Properties();
            int qmi = uri.indexOf(63);
            if (qmi >= 0) {
               header.put("QUERY_STRING", uri.substring(qmi + 1));
               this.decodeParms(uri.substring(qmi + 1), parms);
               uri = this.decodePercent(uri.substring(0, qmi));
            } else {
               uri = this.decodePercent(uri);
            }

            String key;
            if (st.hasMoreTokens()) {
               for(String line = this.readLine(in); line.trim().length() > 0; line = this.readLine(in)) {
                  if (line.length() > 16384) {
                     NanoHTTPD.print_error("Dropped HTTP client from " + this.mySocket.getInetAddress().toString() + " (excess header length)");
                     this.sendError("413 Entity Too Large", "BAD REQUEST: header length is too large");
                  }

                  int p = line.indexOf(58);
                  if (p == -1) {
                     NanoHTTPD.print_error("Dropped HTTP client from " + this.mySocket.getInetAddress().toString() + " (malformed header)");
                     this.sendError("400 Bad Request", "BAD REQUEST: malformed header");
                  }

                  key = line.substring(0, p).trim();
                  if (key.toLowerCase().equals("content-length")) {
                     header.put("Content-Length", line.substring(p + 1).trim());
                  } else {
                     header.put(line.substring(0, p).trim(), line.substring(p + 1).trim());
                  }
               }
            }

            if (method.equalsIgnoreCase("POST")) {
               long size = 0L;
               key = header.getProperty("Content-Length");
               if (key != null) {
                  try {
                     size = (long)Integer.parseInt(key);
                  } catch (NumberFormatException var16) {
                  }
               }

               if (size > 10485760L) {
                  this.sendError("413 Entity Too Large", "BAD REQUEST: Request Entity Too Large");
               }

               if (size > 0L) {
                  byte[] all = new byte[(int)size];
                  in.readFully(all, 0, (int)size);
                  if (!"application/octet-stream".equals(header.getProperty("Content-Type")) && !NanoHTTPD.this.alwaysRaw(uri)) {
                     this.decodeParms(new String(all), parms);
                  } else {
                     ByteArrayInputStream bis = new ByteArrayInputStream(all);
                     parms.put("length", new Long((long)all.length));
                     parms.put("input", bis);
                  }
               }
            }

            header.put("REMOTE_ADDRESS", this.mySocket.getInetAddress().toString());
            Response r = NanoHTTPD.this.serve(uri, method, header, parms);
            if (NanoHTTPD.this.filter != null) {
               NanoHTTPD.this.filter.filterResponse(r);
            }

            if (method.equalsIgnoreCase("HEAD")) {
               r.data = null;
            }

            if (r == null) {
               this.sendError("500 Internal Server Error", "SERVER INTERNAL ERROR: Serve() returned a null response.");
            } else {
               this.sendResponse(r.status, r.mimeType, r.header, r.data);
            }

            in.close();
         } catch (IOException var17) {
            IOException ioe = var17;

            try {
               this.sendError("500 Internal Server Error", "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            } catch (Throwable var15) {
            }
         } catch (InterruptedException var18) {
         }

      }

      private String decodePercent(String str) throws InterruptedException {
         try {
            return URLDecoder.decode(str, "UTF-8");
         } catch (Exception var3) {
            this.sendError("400 Bad Request", "BAD REQUEST: Bad percent-encoding.");
            return null;
         }
      }

      private void decodeParms(String parms, Properties p) throws InterruptedException {
         if (parms != null) {
            StringTokenizer st = new StringTokenizer(parms, "&");

            while(st.hasMoreTokens()) {
               String e = st.nextToken();
               int sep = e.indexOf(61);
               if (sep >= 0) {
                  p.put(this.decodePercent(e.substring(0, sep)).trim(), this.decodePercent(e.substring(sep + 1)));
               }
            }

         }
      }

      private void sendError(String status, String msg) throws InterruptedException {
         this.sendResponse(status, "text/plain", (Map)null, new ByteArrayInputStream(msg.getBytes()));
         throw new InterruptedException();
      }

      private void sendResponse(String status, String mime, Map header, InputStream data) {
         try {
            if (status == null) {
               throw new Error("sendResponse(): Status can't be null.");
            }

            OutputStream out = this.mySocket.getOutputStream();
            PrintWriter pw = new PrintWriter(out);
            pw.print("HTTP/1.1 " + status + "\r\n");
            if (header == null || header.get("Date") == null) {
               pw.print("Date: " + NanoHTTPD.gmtFrmt.format(new Date()) + "\r\n");
            }

            if ((header == null || header.get("Content-Type") == null) && mime != null) {
               pw.print("Content-Type: " + mime + "\r\n");
            }

            if (header != null) {
               Iterator i = header.entrySet().iterator();

               while(i.hasNext()) {
                  Map.Entry entry = (Map.Entry)i.next();
                  String key = (String)entry.getKey();
                  String value = (String)entry.getValue();
                  pw.print(key + ": " + value + "\r\n");
               }
            }

            pw.print("\r\n");
            pw.flush();
            if (data != null) {
               byte[] buff = new byte[2048];

               while(true) {
                  int read = data.read(buff, 0, 2048);
                  if (read <= 0) {
                     break;
                  }

                  out.write(buff, 0, read);
               }
            }

            out.flush();
            out.close();
            if (data != null) {
               data.close();
            }
         } catch (IOException var12) {
            try {
               this.mySocket.close();
            } catch (Throwable var11) {
            }
         }

      }
   }

   public static class TrustEverything implements X509TrustManager {
      public void checkClientTrusted(X509Certificate[] ax509certificate, String authType) {
      }

      public void checkServerTrusted(X509Certificate[] ax509certificate, String authType) throws CertificateException {
      }

      public X509Certificate[] getAcceptedIssuers() {
         return new X509Certificate[0];
      }
   }
}
