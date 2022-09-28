package importers;

import common.CommonUtils;
import common.MudgeSanity;
import common.OperatingSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NmapXML extends Importer {
   public NmapXML(ImportHandler var1) {
      super(var1);
   }

   public boolean isNmapXML(File var1) {
      String var2 = CommonUtils.peekFile(var1, 1024);
      return var2.startsWith("<?xml") && var2.indexOf("<nmaprun") > 0;
   }

   public boolean parse(File var1) throws Exception {
      if (!this.isNmapXML(var1)) {
         return false;
      } else {
         try {
            SAXParserFactory var2 = SAXParserFactory.newInstance();
            SAXParser var3 = var2.newSAXParser();
            var3.parse((InputStream)(new FileInputStream(var1)), (DefaultHandler)(new NmapHandler()));
            return true;
         } catch (Exception var4) {
            MudgeSanity.logException("Nmap XML is partially corrupt: " + var1, var4, false);
            return true;
         }
      }
   }

   class NmapHandler extends DefaultHandler {
      protected String host;
      protected boolean up = false;
      protected String port;
      protected String product = null;
      protected String version = null;
      protected boolean hasport = false;
      protected OperatingSystem os = null;
      protected int osscore = 0;

      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if ("host".equals(var3)) {
            this.os = null;
            this.osscore = 0;
            this.port = null;
            this.up = false;
            this.host = null;
            this.product = null;
            this.version = null;
            this.hasport = false;
         } else if ("status".equals(var3)) {
            this.up = "up".equals(var4.getValue("state"));
         } else if ("address".equals(var3) && "ipv4".equals(var4.getValue("addrtype"))) {
            this.host = var4.getValue("addr");
         } else if ("address".equals(var3) && "ipv6".equals(var4.getValue("addrtype"))) {
            this.host = var4.getValue("addr");
         } else if ("port".equals(var3)) {
            this.port = var4.getValue("portid");
            this.product = null;
            this.version = null;
         } else if ("service".equals(var3)) {
            this.product = var4.getValue("product");
            this.version = var4.getValue("version");
         } else if ("state".equals(var3)) {
            this.hasport = true;
         } else if ("os".equals(var3)) {
            this.os = null;
            this.osscore = 0;
         } else if ("osclass".equals(var3)) {
            String var5 = var4.getValue("osfamily");
            String var6 = var4.getValue("osgen");
            int var7 = CommonUtils.toNumber(var4.getValue("accuracy"), 0);
            OperatingSystem var8 = new OperatingSystem(var5 + " " + var6);
            if (var7 > this.osscore && !var8.isUnknown()) {
               this.os = var8;
               this.osscore = var7;
            }
         }

      }

      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.hasport && "host".equals(var3)) {
            if (this.os != null) {
               NmapXML.this.host(this.host, (String)null, this.os.getName(), this.os.getVersion());
            } else {
               NmapXML.this.host(this.host, (String)null, (String)null, 0.0);
            }
         } else if (this.up && "service".equals(var3)) {
            if (this.product != null && this.version != null) {
               NmapXML.this.service(this.host, this.port, this.product + " " + this.version);
            } else if (this.product != null) {
               NmapXML.this.service(this.host, this.port, this.product);
            } else {
               NmapXML.this.service(this.host, this.port, (String)null);
            }
         }

      }
   }
}
