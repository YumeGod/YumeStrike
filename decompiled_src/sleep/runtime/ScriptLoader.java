package sleep.runtime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import sleep.bridges.BasicIO;
import sleep.bridges.BasicNumbers;
import sleep.bridges.BasicStrings;
import sleep.bridges.BasicUtilities;
import sleep.bridges.DefaultEnvironment;
import sleep.bridges.DefaultVariable;
import sleep.bridges.FileSystemBridge;
import sleep.bridges.RegexBridge;
import sleep.bridges.TimeDateBridge;
import sleep.engine.Block;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Loadable;
import sleep.parser.Parser;
import sleep.taint.TaintModeGeneratedSteps;
import sleep.taint.TaintUtils;

public class ScriptLoader {
   protected static Map BLOCK_CACHE = null;
   protected LinkedList loadedScripts = new LinkedList();
   protected Map scripts = new HashMap();
   protected LinkedList bridgesg = new LinkedList();
   protected LinkedList bridgess = new LinkedList();
   protected LinkedList paths;
   protected boolean disableConversions = false;
   private static CharsetDecoder decoder = null;
   private String charset = null;

   private Block retrieveCacheEntry(String var1) {
      if (BLOCK_CACHE != null && BLOCK_CACHE.containsKey(var1)) {
         Object[] var2 = (Object[])((Object[])BLOCK_CACHE.get(var1));
         return (Block)var2[0];
      } else {
         return null;
      }
   }

   private static boolean isCacheHit(String var0) {
      return BLOCK_CACHE != null && BLOCK_CACHE.containsKey(var0);
   }

   public void touch(String var1, long var2) {
      if (BLOCK_CACHE != null && BLOCK_CACHE.containsKey(var1)) {
         Object[] var4 = (Object[])((Object[])BLOCK_CACHE.get(var1));
         long var5 = (Long)var4[1];
         if (var2 > var5) {
            BLOCK_CACHE.remove(var1);
         }
      }

   }

   public ScriptLoader() {
      this.initDefaultBridges();
   }

   public Map setGlobalCache(boolean var1) {
      if (var1 && BLOCK_CACHE == null) {
         BLOCK_CACHE = Collections.synchronizedMap(new HashMap());
      }

      if (!var1) {
         BLOCK_CACHE = null;
      }

      return BLOCK_CACHE;
   }

   protected void initDefaultBridges() {
      this.addGlobalBridge(new BasicNumbers());
      this.addGlobalBridge(new BasicStrings());
      this.addGlobalBridge(new BasicUtilities());
      this.addGlobalBridge(new BasicIO());
      this.addGlobalBridge(new FileSystemBridge());
      this.addGlobalBridge(new DefaultEnvironment());
      this.addGlobalBridge(new DefaultVariable());
      this.addGlobalBridge(new RegexBridge());
      this.addGlobalBridge(new TimeDateBridge());
   }

   public void addGlobalBridge(Loadable var1) {
      this.bridgesg.add(var1);
   }

   public void addSpecificBridge(Loadable var1) {
      this.bridgess.add(var1);
   }

   public Map getScriptsByKey() {
      return this.scripts;
   }

   public boolean isLoaded(String var1) {
      return this.scripts.containsKey(var1);
   }

   public ScriptEnvironment getFirstScriptEnvironment() {
      if (this.loadedScripts.size() > 0) {
         ScriptInstance var1 = (ScriptInstance)this.loadedScripts.getFirst();
         return var1.getScriptEnvironment();
      } else {
         return null;
      }
   }

   public LinkedList getScripts() {
      return this.loadedScripts;
   }

   protected void inProcessScript(String var1, ScriptInstance var2) {
      var2.setName(var1);
      Iterator var3 = this.bridgess.iterator();

      while(var3.hasNext()) {
         ((Loadable)var3.next()).scriptLoaded(var2);
      }

      if (var2.getScriptEnvironment().getEnvironment().get("(isloaded)") != this) {
         var3 = this.bridgesg.iterator();

         while(var3.hasNext()) {
            ((Loadable)var3.next()).scriptLoaded(var2);
         }

         var2.getScriptEnvironment().getEnvironment().put("(isloaded)", this);
      }

   }

   public ScriptInstance loadSerialized(File var1, Hashtable var2) throws IOException, ClassNotFoundException {
      File var3 = new File(var1.getAbsolutePath() + ".bin");
      if (!var3.exists() || var1.exists() && var1.lastModified() >= var3.lastModified()) {
         ScriptInstance var4 = this.loadScript(var1, var2);
         saveSerialized(var4);
         return var4;
      } else {
         return this.loadSerialized(var1.getName(), new FileInputStream(var3), var2);
      }
   }

   public ScriptInstance loadSerialized(String var1, InputStream var2, Hashtable var3) throws IOException, ClassNotFoundException {
      ObjectInputStream var4 = new ObjectInputStream(var2);
      Block var5 = (Block)var4.readObject();
      return this.loadScript(var1, var5, var3);
   }

   public static void saveSerialized(ScriptInstance var0) throws IOException {
      saveSerialized(var0, new FileOutputStream(var0.getName() + ".bin"));
   }

   public static void saveSerialized(ScriptInstance var0, OutputStream var1) throws IOException {
      ObjectOutputStream var2 = new ObjectOutputStream(var1);
      var2.writeObject(var0.getRunnableBlock());
   }

   public ScriptInstance loadScriptNoReference(String var1, Block var2, Hashtable var3) {
      ScriptInstance var4 = new ScriptInstance(var3);
      var4.installBlock(var2);
      this.inProcessScript(var1, var4);
      return var4;
   }

   public ScriptInstance loadScript(String var1, Block var2, Hashtable var3) {
      ScriptInstance var4 = this.loadScriptNoReference(var1, var2, var3);
      if (!var1.equals("<interact mode>")) {
         this.loadedScripts.add(var4);
         this.scripts.put(var1, var4);
      }

      return var4;
   }

   public ScriptInstance loadScript(String var1, String var2, Hashtable var3) throws YourCodeSucksException {
      return this.loadScript(var1, this.compileScript(var1, var2), var3);
   }

   public Block compileScript(String var1, InputStream var2) throws YourCodeSucksException, IOException {
      if (isCacheHit(var1)) {
         var2.close();
         return this.retrieveCacheEntry(var1);
      } else {
         StringBuffer var3 = new StringBuffer(8192);
         BufferedReader var4 = new BufferedReader(this.getInputStreamReader(var2));

         for(String var5 = var4.readLine(); var5 != null; var5 = var4.readLine()) {
            var3.append("\n");
            var3.append(var5);
         }

         var4.close();
         var2.close();
         return this.compileScript(var1, var3.toString());
      }
   }

   public Block compileScript(File var1) throws IOException, YourCodeSucksException {
      this.touch(var1.getAbsolutePath(), var1.lastModified());
      return this.compileScript(var1.getAbsolutePath(), (InputStream)(new FileInputStream(var1)));
   }

   public Block compileScript(String var1) throws IOException, YourCodeSucksException {
      return this.compileScript(new File(var1));
   }

   public Block compileScript(String var1, String var2) throws YourCodeSucksException {
      if (isCacheHit(var1)) {
         return this.retrieveCacheEntry(var1);
      } else {
         Parser var3 = new Parser(var1, var2);
         if (TaintUtils.isTaintMode()) {
            var3.setCodeFactory(new TaintModeGeneratedSteps());
         }

         var3.parse();
         if (BLOCK_CACHE != null) {
            BLOCK_CACHE.put(var1, new Object[]{var3.getRunnableBlock(), new Long(System.currentTimeMillis())});
         }

         return var3.getRunnableBlock();
      }
   }

   public ScriptInstance loadScript(String var1, InputStream var2) throws YourCodeSucksException, IOException {
      return this.loadScript(var1, (InputStream)var2, (Hashtable)null);
   }

   public ScriptInstance loadScript(String var1, InputStream var2, Hashtable var3) throws YourCodeSucksException, IOException {
      return this.loadScript(var1, this.compileScript(var1, var2), var3);
   }

   public ScriptInstance loadScript(String var1) throws IOException, YourCodeSucksException {
      return this.loadScript((File)(new File(var1)), (Hashtable)null);
   }

   public ScriptInstance loadScript(String var1, Hashtable var2) throws IOException, YourCodeSucksException {
      return this.loadScript(new File(var1), var2);
   }

   public ScriptInstance loadScript(File var1, Hashtable var2) throws IOException, YourCodeSucksException {
      ScriptInstance var3 = this.loadScript(var1.getAbsolutePath(), (InputStream)(new FileInputStream(var1)), var2);
      var3.associateFile(var1);
      return var3;
   }

   public ScriptInstance loadScript(File var1) throws IOException, YourCodeSucksException {
      return this.loadScript((File)var1, (Hashtable)null);
   }

   public void unloadScript(String var1) {
      this.unloadScript((ScriptInstance)this.scripts.get(var1));
   }

   public void unloadScript(ScriptInstance var1) {
      if (BLOCK_CACHE != null) {
         BLOCK_CACHE.remove(var1.getName());
      }

      this.loadedScripts.remove(var1);
      this.scripts.remove(var1.getName());
      var1.setUnloaded();
      Iterator var2 = this.bridgess.iterator();

      Loadable var3;
      while(var2.hasNext()) {
         var3 = (Loadable)var2.next();
         var3.scriptUnloaded(var1);
      }

      var2 = this.bridgesg.iterator();

      while(var2.hasNext()) {
         var3 = (Loadable)var2.next();
         var3.scriptUnloaded(var1);
      }

   }

   public Set getScriptsToUnload(Set var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      Set var3 = this.scripts.keySet();
      var2.addAll(var3);
      var2.removeAll(var1);
      return var2;
   }

   public Set getScriptsToLoad(Set var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      Set var3 = this.scripts.keySet();
      var2.addAll(var1);
      var2.removeAll(var3);
      return var2;
   }

   public void setCharsetConversion(boolean var1) {
      this.disableConversions = !var1;
   }

   public boolean isCharsetConversions() {
      return !this.disableConversions;
   }

   public String getCharset() {
      return this.charset;
   }

   public void setCharset(String var1) {
      this.charset = var1;
   }

   private InputStreamReader getInputStreamReader(InputStream var1) {
      if (this.disableConversions) {
         if (decoder == null) {
            decoder = new NoConversion();
         }

         return new InputStreamReader(var1, decoder);
      } else {
         if (this.charset != null) {
            try {
               return new InputStreamReader(var1, this.charset);
            } catch (UnsupportedEncodingException var3) {
               var3.printStackTrace();
            }
         }

         return new InputStreamReader(var1);
      }
   }

   private static class NoConversion extends CharsetDecoder {
      public NoConversion() {
         super((Charset)null, 1.0F, 1.0F);
      }

      protected CoderResult decodeLoop(ByteBuffer var1, CharBuffer var2) {
         int var3 = var1.position();

         try {
            CoderResult var8;
            for(; var1.hasRemaining(); ++var3) {
               if (!var2.hasRemaining()) {
                  var8 = CoderResult.OVERFLOW;
                  return var8;
               }

               int var4 = var1.get();
               if (var4 >= 0) {
                  var2.put((char)var4);
               } else {
                  var4 += 256;
                  var2.put((char)var4);
               }
            }

            var8 = CoderResult.UNDERFLOW;
            return var8;
         } finally {
            var1.position(var3);
         }
      }
   }
}
