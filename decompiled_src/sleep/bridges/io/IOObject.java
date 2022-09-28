package sleep.bridges.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptEnvironment;
import sleep.runtime.SleepUtils;

public class IOObject {
   protected InputStreamReader readeru = null;
   protected DataInputStream readerb = null;
   protected BufferedInputStream reader = null;
   protected InputStream in = null;
   protected OutputStreamWriter writeru = null;
   protected DataOutputStream writerb = null;
   protected OutputStream out = null;
   protected Thread thread = null;
   protected Scalar token = null;
   private boolean stripTheLineSeparator = false;
   private static final String lineSeparator = System.getProperty("line.separator");

   public Object getSource() {
      return null;
   }

   public void setEncoding(String var1) throws UnsupportedEncodingException {
      if (this.writerb != null) {
         this.writeru = new OutputStreamWriter(this.writerb, var1);
      }

      if (this.readerb != null) {
         this.readeru = new InputStreamReader(this.readerb, var1);
      }

   }

   public void setThread(Thread var1) {
      this.thread = var1;
   }

   public Thread getThread() {
      return this.thread;
   }

   public Scalar wait(ScriptEnvironment var1, long var2) {
      if (this.getThread() != null && this.getThread().isAlive()) {
         try {
            this.getThread().join(var2);
            if (this.getThread().isAlive()) {
               var1.flagError(new IOException("wait on object timed out"));
               return SleepUtils.getEmptyScalar();
            }
         } catch (Exception var5) {
            var1.flagError(var5);
            return SleepUtils.getEmptyScalar();
         }
      }

      return this.getToken();
   }

   public Scalar getToken() {
      return this.token == null ? SleepUtils.getEmptyScalar() : this.token;
   }

   public void setToken(Scalar var1) {
      this.token = var1;
   }

   public static void setConsole(ScriptEnvironment var0, IOObject var1) {
      var0.getScriptInstance().getMetadata().put("%console%", var1);
   }

   public static IOObject getConsole(ScriptEnvironment var0) {
      IOObject var1 = (IOObject)var0.getScriptInstance().getMetadata().get("%console%");
      if (var1 == null) {
         var1 = new IOObject();
         var1.openRead(System.in);
         var1.openWrite(System.out);
         setConsole(var0, var1);
      }

      return var1;
   }

   public InputStream getInputStream() {
      return this.in;
   }

   public OutputStream getOutputStream() {
      return this.out;
   }

   public void openRead(InputStream var1) {
      this.in = var1;
      if (this.in != null) {
         this.reader = new BufferedInputStream(this.in, 8192);
         this.readerb = new DataInputStream(this.reader);
         this.readeru = new InputStreamReader(this.readerb);
      }

   }

   public void openWrite(OutputStream var1) {
      this.out = var1;
      if (this.out != null) {
         this.writerb = new DataOutputStream(this.out);
         this.writeru = new OutputStreamWriter(this.writerb);
      }

   }

   public void close() {
      try {
         if (this.in != null) {
            this.in.notifyAll();
         }

         if (this.out != null) {
            this.out.notifyAll();
         }
      } catch (Exception var8) {
      }

      try {
         if (this.readeru != null) {
            this.readeru.close();
         }

         if (this.writeru != null) {
            this.writeru.close();
         }

         if (this.reader != null) {
            this.reader.close();
         }

         if (this.readerb != null) {
            this.readerb.close();
         }

         if (this.writerb != null) {
            this.writerb.close();
         }

         if (this.in != null) {
            this.in.close();
         }

         if (this.out != null) {
            this.out.close();
         }
      } catch (Exception var6) {
      } finally {
         this.in = null;
         this.out = null;
         this.reader = null;
         this.readerb = null;
         this.writerb = null;
         this.readeru = null;
         this.writeru = null;
      }

   }

   public String readLine() {
      try {
         if (this.readeru != null) {
            StringBuffer var1 = new StringBuffer(8192);
            int var2 = this.readeru.read();
            if (this.stripTheLineSeparator && var2 == 10) {
               var2 = this.readeru.read();
            }

            for(this.stripTheLineSeparator = false; var2 != -1; var2 = this.readeru.read()) {
               if (var2 == 10) {
                  return var1.toString();
               }

               if (var2 == 13) {
                  this.stripTheLineSeparator = true;
                  return var1.toString();
               }

               var1.append((char)var2);
            }

            this.close();
            if (var1.length() > 0) {
               return var1.toString();
            }

            return null;
         }
      } catch (Exception var3) {
         this.close();
      }

      return null;
   }

   public String readCharacter() {
      try {
         if (this.readeru != null) {
            int var1 = this.readeru.read();
            if (var1 != -1) {
               return (char)var1 + "";
            }

            this.close();
         }
      } catch (Exception var2) {
         this.close();
      }

      return null;
   }

   public boolean isEOF() {
      return this.reader == null;
   }

   public void sendEOF() {
      try {
         if (this.writerb != null) {
            this.writerb.close();
         }

         if (this.out != null) {
            this.out.close();
         }
      } catch (Exception var2) {
      }

   }

   public BufferedInputStream getInputBuffer() {
      return this.reader;
   }

   public DataInputStream getReader() {
      return this.readerb;
   }

   public DataOutputStream getWriter() {
      return this.writerb;
   }

   public void printLine(String var1) {
      this.print(var1 + lineSeparator);
   }

   public void print(String var1) {
      try {
         if (this.writeru != null) {
            this.writeru.write(var1, 0, var1.length());
            this.writeru.flush();
         }
      } catch (Exception var3) {
         this.close();
      }

   }
}
