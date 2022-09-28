package org.apache.fop.pdf;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PDFDocument {
   private static final Integer LOCATION_PLACEHOLDER = new Integer(0);
   public static final int PDF_VERSION_1_3 = 3;
   public static final int PDF_VERSION_1_4 = 4;
   public static final String ENCODING = "ISO-8859-1";
   protected int objectcount = 0;
   private Log log = LogFactory.getLog("org.apache.fop.pdf");
   private int position = 0;
   private int xref;
   private List location = new ArrayList();
   private List trailerObjects = new ArrayList();
   private List objects = new LinkedList();
   private int pdfVersion = 4;
   private PDFProfile pdfProfile = new PDFProfile(this);
   private PDFRoot root;
   private PDFOutline outlineRoot = null;
   private PDFPages pages = this.getFactory().makePages();
   private PDFInfo info;
   private PDFResources resources;
   private PDFEncryption encryption;
   private PDFDeviceColorSpace colorspace = new PDFDeviceColorSpace(2);
   private int patternCount = 0;
   private int shadingCount = 0;
   private int xObjectCount = 0;
   private Map xObjectsMap = new HashMap();
   private Map fontMap = new HashMap();
   private Map filterMap = new HashMap();
   private List gstates = new ArrayList();
   private List functions = new ArrayList();
   private List shadings = new ArrayList();
   private List patterns = new ArrayList();
   private List links = new ArrayList();
   private List destinations;
   private List filespecs = new ArrayList();
   private List gotoremotes = new ArrayList();
   private List gotos = new ArrayList();
   private List launches = new ArrayList();
   private PDFDests dests;
   private PDFFactory factory = new PDFFactory(this);
   private boolean encodingOnTheFly = true;

   public PDFDocument(String prod) {
      this.root = this.getFactory().makeRoot(this.pages);
      this.resources = this.getFactory().makeResources();
      this.info = this.getFactory().makeInfo(prod);
   }

   public int getPDFVersion() {
      return this.pdfVersion;
   }

   public String getPDFVersionString() {
      switch (this.getPDFVersion()) {
         case 3:
            return "1.3";
         case 4:
            return "1.4";
         default:
            throw new IllegalStateException("Unsupported PDF version selected");
      }
   }

   public PDFProfile getProfile() {
      return this.pdfProfile;
   }

   public PDFFactory getFactory() {
      return this.factory;
   }

   public boolean isEncodingOnTheFly() {
      return this.encodingOnTheFly;
   }

   public static byte[] encode(String text) {
      try {
         return text.getBytes("ISO-8859-1");
      } catch (UnsupportedEncodingException var2) {
         return text.getBytes();
      }
   }

   public static Writer getWriterFor(OutputStream out) {
      try {
         return new BufferedWriter(new OutputStreamWriter(out, "ISO-8859-1"));
      } catch (UnsupportedEncodingException var2) {
         throw new Error("JVM doesn't support ISO-8859-1 encoding!");
      }
   }

   public void setProducer(String producer) {
      this.info.setProducer(producer);
   }

   public void setCreationDate(Date date) {
      this.info.setCreationDate(date);
   }

   public void setCreator(String creator) {
      this.info.setCreator(creator);
   }

   public void setFilterMap(Map map) {
      this.filterMap = map;
   }

   public Map getFilterMap() {
      return this.filterMap;
   }

   public PDFPages getPages() {
      return this.pages;
   }

   public PDFRoot getRoot() {
      return this.root;
   }

   public void enforceLanguageOnRoot() {
      if (this.root.getLanguage() == null) {
         String fallbackLanguage;
         if (this.getProfile().getPDFAMode().isPDFA1LevelA()) {
            fallbackLanguage = "x-unknown";
         } else {
            fallbackLanguage = "en";
         }

         this.root.setLanguage(fallbackLanguage);
      }

   }

   public PDFInfo getInfo() {
      return this.info;
   }

   public PDFObject registerObject(PDFObject obj) {
      this.assignObjectNumber(obj);
      this.addObject(obj);
      return obj;
   }

   public void assignObjectNumber(PDFObject obj) {
      if (obj == null) {
         throw new NullPointerException("obj must not be null");
      } else if (obj.hasObjectNumber()) {
         throw new IllegalStateException("Error registering a PDFObject: PDFObject already has an object number");
      } else {
         PDFDocument currentParent = obj.getDocument();
         if (currentParent != null && currentParent != this) {
            throw new IllegalStateException("Error registering a PDFObject: PDFObject already has a parent PDFDocument");
         } else {
            obj.setObjectNumber(++this.objectcount);
            if (currentParent == null) {
               obj.setDocument(this);
            }

         }
      }
   }

   public void addObject(PDFObject obj) {
      if (obj == null) {
         throw new NullPointerException("obj must not be null");
      } else if (!obj.hasObjectNumber()) {
         throw new IllegalStateException("Error adding a PDFObject: PDFObject doesn't have an object number");
      } else {
         this.objects.add(obj);
         if (obj instanceof PDFFunction) {
            this.functions.add(obj);
         }

         String patternName;
         if (obj instanceof PDFShading) {
            patternName = "Sh" + ++this.shadingCount;
            ((PDFShading)obj).setName(patternName);
            this.shadings.add(obj);
         }

         if (obj instanceof PDFPattern) {
            patternName = "Pa" + ++this.patternCount;
            ((PDFPattern)obj).setName(patternName);
            this.patterns.add(obj);
         }

         if (obj instanceof PDFFont) {
            PDFFont font = (PDFFont)obj;
            this.fontMap.put(font.getName(), font);
         }

         if (obj instanceof PDFGState) {
            this.gstates.add(obj);
         }

         if (obj instanceof PDFPage) {
            this.pages.notifyKidRegistered((PDFPage)obj);
         }

         if (obj instanceof PDFLaunch) {
            this.launches.add(obj);
         }

         if (obj instanceof PDFLink) {
            this.links.add(obj);
         }

         if (obj instanceof PDFFileSpec) {
            this.filespecs.add(obj);
         }

         if (obj instanceof PDFGoToRemote) {
            this.gotoremotes.add(obj);
         }

      }
   }

   public void addTrailerObject(PDFObject obj) {
      this.trailerObjects.add(obj);
      if (obj instanceof PDFGoTo) {
         this.gotos.add(obj);
      }

   }

   public void applyEncryption(AbstractPDFStream stream) {
      if (this.isEncryptionActive()) {
         this.encryption.applyFilter(stream);
      }

   }

   public void setEncryption(PDFEncryptionParams params) {
      this.getProfile().verifyEncryptionAllowed();
      this.encryption = PDFEncryptionManager.newInstance(++this.objectcount, params);
      if (this.encryption != null) {
         PDFObject pdfObject = (PDFObject)this.encryption;
         pdfObject.setDocument(this);
         this.addTrailerObject(pdfObject);
      } else {
         this.log.warn("PDF encryption is unavailable. PDF will be generated without encryption.");
      }

   }

   public boolean isEncryptionActive() {
      return this.encryption != null;
   }

   public PDFEncryption getEncryption() {
      return this.encryption;
   }

   private Object findPDFObject(List list, PDFObject compare) {
      Iterator iter = list.iterator();

      PDFObject obj;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         obj = (PDFObject)iter.next();
      } while(!compare.contentEquals(obj));

      return obj;
   }

   protected PDFFunction findFunction(PDFFunction compare) {
      return (PDFFunction)this.findPDFObject(this.functions, compare);
   }

   protected PDFShading findShading(PDFShading compare) {
      return (PDFShading)this.findPDFObject(this.shadings, compare);
   }

   protected PDFPattern findPattern(PDFPattern compare) {
      return (PDFPattern)this.findPDFObject(this.patterns, compare);
   }

   protected PDFFont findFont(String fontname) {
      return (PDFFont)this.fontMap.get(fontname);
   }

   protected PDFDestination findDestination(PDFDestination compare) {
      int index = this.getDestinationList().indexOf(compare);
      return index >= 0 ? (PDFDestination)this.getDestinationList().get(index) : null;
   }

   protected PDFLink findLink(PDFLink compare) {
      return (PDFLink)this.findPDFObject(this.links, compare);
   }

   protected PDFFileSpec findFileSpec(PDFFileSpec compare) {
      return (PDFFileSpec)this.findPDFObject(this.filespecs, compare);
   }

   protected PDFGoToRemote findGoToRemote(PDFGoToRemote compare) {
      return (PDFGoToRemote)this.findPDFObject(this.gotoremotes, compare);
   }

   protected PDFGoTo findGoTo(PDFGoTo compare) {
      return (PDFGoTo)this.findPDFObject(this.gotos, compare);
   }

   protected PDFLaunch findLaunch(PDFLaunch compare) {
      return (PDFLaunch)this.findPDFObject(this.launches, compare);
   }

   protected PDFGState findGState(PDFGState wanted, PDFGState current) {
      Iterator iter = this.gstates.iterator();

      PDFGState poss;
      PDFGState avail;
      do {
         if (!iter.hasNext()) {
            return null;
         }

         avail = (PDFGState)iter.next();
         poss = new PDFGState();
         poss.addValues(current);
         poss.addValues(avail);
      } while(!poss.equals(wanted));

      return avail;
   }

   public PDFDeviceColorSpace getPDFColorSpace() {
      return this.colorspace;
   }

   public int getColorSpace() {
      return this.getPDFColorSpace().getColorSpace();
   }

   public void setColorSpace(int theColorspace) {
      this.colorspace.setColorSpace(theColorspace);
   }

   public Map getFontMap() {
      return this.fontMap;
   }

   protected InputStream resolveURI(String uri) throws FileNotFoundException {
      try {
         return (new URL(uri)).openStream();
      } catch (Exception var3) {
         throw new FileNotFoundException("URI could not be resolved (" + var3.getMessage() + "): " + uri);
      }
   }

   /** @deprecated */
   public PDFImageXObject getImage(String key) {
      return (PDFImageXObject)this.xObjectsMap.get(key);
   }

   public PDFXObject getXObject(String key) {
      return (PDFXObject)this.xObjectsMap.get(key);
   }

   public PDFDests getDests() {
      return this.dests;
   }

   public void addDestination(PDFDestination destination) {
      if (this.destinations == null) {
         this.destinations = new ArrayList();
      }

      this.destinations.add(destination);
   }

   public List getDestinationList() {
      return this.hasDestinations() ? this.destinations : Collections.EMPTY_LIST;
   }

   public boolean hasDestinations() {
      return this.destinations != null && !this.destinations.isEmpty();
   }

   public PDFImageXObject addImage(PDFResourceContext res, PDFImage img) {
      String key = img.getKey();
      PDFImageXObject xObject = (PDFImageXObject)this.xObjectsMap.get(key);
      if (xObject != null) {
         if (res != null) {
            res.getPDFResources().addXObject(xObject);
         }

         return xObject;
      } else {
         img.setup(this);
         xObject = new PDFImageXObject(++this.xObjectCount, img);
         this.registerObject(xObject);
         this.resources.addXObject(xObject);
         if (res != null) {
            res.getPDFResources().addXObject(xObject);
         }

         this.xObjectsMap.put(key, xObject);
         return xObject;
      }
   }

   public PDFFormXObject addFormXObject(PDFResourceContext res, PDFStream cont, PDFReference formres, String key) {
      PDFFormXObject xObject = (PDFFormXObject)this.xObjectsMap.get(key);
      if (xObject != null) {
         if (res != null) {
            res.getPDFResources().addXObject(xObject);
         }

         return xObject;
      } else {
         xObject = new PDFFormXObject(++this.xObjectCount, cont, formres);
         this.registerObject(xObject);
         this.resources.addXObject(xObject);
         if (res != null) {
            res.getPDFResources().addXObject(xObject);
         }

         this.xObjectsMap.put(key, xObject);
         return xObject;
      }
   }

   public PDFOutline getOutlineRoot() {
      if (this.outlineRoot != null) {
         return this.outlineRoot;
      } else {
         this.outlineRoot = new PDFOutline((String)null, (String)null, true);
         this.assignObjectNumber(this.outlineRoot);
         this.addTrailerObject(this.outlineRoot);
         this.root.setRootOutline(this.outlineRoot);
         return this.outlineRoot;
      }
   }

   public PDFResources getResources() {
      return this.resources;
   }

   private void setLocation(int objidx, int position) {
      while(this.location.size() <= objidx) {
         this.location.add(LOCATION_PLACEHOLDER);
      }

      this.location.set(objidx, new Integer(position));
   }

   public void output(OutputStream stream) throws IOException {
      while(this.objects.size() > 0) {
         PDFObject object = (PDFObject)this.objects.remove(0);
         this.setLocation(object.getObjectNumber() - 1, this.position);
         this.position += object.output(stream);
      }

   }

   public void outputHeader(OutputStream stream) throws IOException {
      this.position = 0;
      this.getProfile().verifyPDFVersion();
      byte[] pdf = encode("%PDF-" + this.getPDFVersionString() + "\n");
      stream.write(pdf);
      this.position += pdf.length;
      byte[] bin = new byte[]{37, -86, -85, -84, -83, 10};
      stream.write(bin);
      this.position += bin.length;
   }

   protected String getIDEntry() {
      try {
         MessageDigest digest = MessageDigest.getInstance("MD5");
         DateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS");
         digest.update(encode(df.format(new Date())));
         digest.update(encode(String.valueOf(this.position)));
         digest.update(this.getInfo().toPDF());
         byte[] res = digest.digest();
         String s = PDFText.toHex(res);
         return "/ID [" + s + " " + s + "]";
      } catch (NoSuchAlgorithmException var5) {
         if (this.getProfile().isIDEntryRequired()) {
            throw new UnsupportedOperationException("MD5 not available: " + var5.getMessage());
         } else {
            return "";
         }
      }
   }

   public void outputTrailer(OutputStream stream) throws IOException {
      if (this.hasDestinations()) {
         Collections.sort(this.destinations, new DestinationComparator());
         this.dests = this.getFactory().makeDests(this.destinations);
         if (this.root.getNames() == null) {
            this.root.setNames(this.getFactory().makeNames());
         }

         this.root.getNames().setDests(this.dests);
      }

      this.output(stream);

      for(int count = 0; count < this.trailerObjects.size(); ++count) {
         PDFObject o = (PDFObject)this.trailerObjects.get(count);
         this.location.set(o.getObjectNumber() - 1, new Integer(this.position));
         this.position += o.output(stream);
      }

      this.position += this.outputXref(stream);
      StringBuffer pdf = new StringBuffer(128);
      pdf.append("trailer\n<<\n/Size ").append(this.objectcount + 1).append("\n/Root ").append(this.root.referencePDF()).append("\n/Info ").append(this.info.referencePDF()).append('\n');
      if (this.isEncryptionActive()) {
         pdf.append(this.encryption.getTrailerEntry());
      } else {
         pdf.append(this.getIDEntry());
      }

      pdf.append("\n>>\nstartxref\n").append(this.xref).append("\n%%EOF\n");
      stream.write(encode(pdf.toString()));
   }

   private int outputXref(OutputStream stream) throws IOException {
      this.xref = this.position;
      StringBuffer pdf = new StringBuffer(128);
      pdf.append("xref\n0 ");
      pdf.append(this.objectcount + 1);
      pdf.append("\n0000000000 65535 f \n");

      for(int count = 0; count < this.location.size(); ++count) {
         String padding = "0000000000";
         String s = this.location.get(count).toString();
         String loc = "0000000000".substring(s.length()) + s;
         pdf = pdf.append(loc).append(" 00000 n \n");
      }

      byte[] pdfBytes = encode(pdf.toString());
      stream.write(pdfBytes);
      return pdfBytes.length;
   }
}
