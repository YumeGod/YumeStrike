package org.apache.fop.pdf;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.CIDFont;
import org.apache.fop.fonts.CIDSubset;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.CustomFont;
import org.apache.fop.fonts.FontDescriptor;
import org.apache.fop.fonts.FontMetrics;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.MultiByteFont;
import org.apache.fop.fonts.SimpleSingleByteEncoding;
import org.apache.fop.fonts.SingleByteEncoding;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.fonts.truetype.FontFileReader;
import org.apache.fop.fonts.truetype.TTFSubSetFile;
import org.apache.fop.fonts.type1.PFBData;
import org.apache.fop.fonts.type1.PFBParser;
import org.apache.xmlgraphics.xmp.Metadata;

public class PDFFactory {
   private PDFDocument document;
   private Log log;

   public PDFFactory(PDFDocument document) {
      this.log = LogFactory.getLog(PDFFactory.class);
      this.document = document;
   }

   public final PDFDocument getDocument() {
      return this.document;
   }

   public PDFRoot makeRoot(PDFPages pages) {
      PDFRoot pdfRoot = new PDFRoot(++this.document.objectcount, pages);
      pdfRoot.setDocument(this.getDocument());
      this.getDocument().addTrailerObject(pdfRoot);
      return pdfRoot;
   }

   public PDFPages makePages() {
      PDFPages pdfPages = new PDFPages(++this.document.objectcount);
      pdfPages.setDocument(this.getDocument());
      this.getDocument().addTrailerObject(pdfPages);
      return pdfPages;
   }

   public PDFResources makeResources() {
      PDFResources pdfResources = new PDFResources(++this.document.objectcount);
      pdfResources.setDocument(this.getDocument());
      this.getDocument().addTrailerObject(pdfResources);
      return pdfResources;
   }

   protected PDFInfo makeInfo(String prod) {
      PDFInfo pdfInfo = new PDFInfo();
      pdfInfo.setProducer(prod);
      this.getDocument().registerObject(pdfInfo);
      return pdfInfo;
   }

   public PDFMetadata makeMetadata(Metadata meta, boolean readOnly) {
      PDFMetadata pdfMetadata = new PDFMetadata(meta, readOnly);
      this.getDocument().registerObject(pdfMetadata);
      return pdfMetadata;
   }

   public PDFOutputIntent makeOutputIntent() {
      PDFOutputIntent outputIntent = new PDFOutputIntent();
      this.getDocument().registerObject(outputIntent);
      return outputIntent;
   }

   public PDFPage makePage(PDFResources resources, int pageIndex, Rectangle2D mediaBox, Rectangle2D cropBox, Rectangle2D bleedBox, Rectangle2D trimBox) {
      PDFPage page = new PDFPage(resources, pageIndex, mediaBox, cropBox, bleedBox, trimBox);
      this.getDocument().assignObjectNumber(page);
      this.getDocument().getPages().addPage(page);
      return page;
   }

   public PDFPage makePage(PDFResources resources, int pageWidth, int pageHeight, int pageIndex) {
      Rectangle2D mediaBox = new Rectangle2D.Double(0.0, 0.0, (double)pageWidth, (double)pageHeight);
      return this.makePage(resources, pageIndex, mediaBox, mediaBox, mediaBox, mediaBox);
   }

   public PDFPage makePage(PDFResources resources, int pageWidth, int pageHeight) {
      return this.makePage(resources, pageWidth, pageHeight, -1);
   }

   public PDFFunction makeFunction(int theFunctionType, List theDomain, List theRange, List theSize, int theBitsPerSample, int theOrder, List theEncode, List theDecode, StringBuffer theFunctionDataStream, List theFilter) {
      PDFFunction function = new PDFFunction(theFunctionType, theDomain, theRange, theSize, theBitsPerSample, theOrder, theEncode, theDecode, theFunctionDataStream, theFilter);
      PDFFunction oldfunc = this.getDocument().findFunction(function);
      if (oldfunc == null) {
         this.getDocument().registerObject(function);
      } else {
         function = oldfunc;
      }

      return function;
   }

   public PDFFunction makeFunction(int theFunctionType, List theDomain, List theRange, List theCZero, List theCOne, double theInterpolationExponentN) {
      PDFFunction function = new PDFFunction(theFunctionType, theDomain, theRange, theCZero, theCOne, theInterpolationExponentN);
      PDFFunction oldfunc = this.getDocument().findFunction(function);
      if (oldfunc == null) {
         this.getDocument().registerObject(function);
      } else {
         function = oldfunc;
      }

      return function;
   }

   public PDFFunction makeFunction(int theFunctionType, List theDomain, List theRange, List theFunctions, List theBounds, List theEncode) {
      PDFFunction function = new PDFFunction(theFunctionType, theDomain, theRange, theFunctions, theBounds, theEncode);
      PDFFunction oldfunc = this.getDocument().findFunction(function);
      if (oldfunc == null) {
         this.getDocument().registerObject(function);
      } else {
         function = oldfunc;
      }

      return function;
   }

   public PDFFunction makeFunction(int theNumber, int theFunctionType, List theDomain, List theRange, StringBuffer theFunctionDataStream) {
      PDFFunction function = new PDFFunction(theFunctionType, theDomain, theRange, theFunctionDataStream);
      PDFFunction oldfunc = this.getDocument().findFunction(function);
      if (oldfunc == null) {
         this.getDocument().registerObject(function);
      } else {
         function = oldfunc;
      }

      return function;
   }

   public PDFShading makeShading(PDFResourceContext res, int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, List theDomain, List theMatrix, PDFFunction theFunction) {
      PDFShading shading = new PDFShading(theShadingType, theColorSpace, theBackground, theBBox, theAntiAlias, theDomain, theMatrix, theFunction);
      PDFShading oldshad = this.getDocument().findShading(shading);
      if (oldshad == null) {
         this.getDocument().registerObject(shading);
      } else {
         shading = oldshad;
      }

      if (res != null) {
         res.getPDFResources().addShading(shading);
      } else {
         this.getDocument().getResources().addShading(shading);
      }

      return shading;
   }

   public PDFShading makeShading(PDFResourceContext res, int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, List theCoords, List theDomain, PDFFunction theFunction, List theExtend) {
      PDFShading shading = new PDFShading(theShadingType, theColorSpace, theBackground, theBBox, theAntiAlias, theCoords, theDomain, theFunction, theExtend);
      PDFShading oldshad = this.getDocument().findShading(shading);
      if (oldshad == null) {
         this.getDocument().registerObject(shading);
      } else {
         shading = oldshad;
      }

      if (res != null) {
         res.getPDFResources().addShading(shading);
      } else {
         this.getDocument().getResources().addShading(shading);
      }

      return shading;
   }

   public PDFShading makeShading(PDFResourceContext res, int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, int theBitsPerCoordinate, int theBitsPerComponent, int theBitsPerFlag, List theDecode, PDFFunction theFunction) {
      PDFShading shading = new PDFShading(theShadingType, theColorSpace, theBackground, theBBox, theAntiAlias, theBitsPerCoordinate, theBitsPerComponent, theBitsPerFlag, theDecode, theFunction);
      PDFShading oldshad = this.getDocument().findShading(shading);
      if (oldshad == null) {
         this.getDocument().registerObject(shading);
      } else {
         shading = oldshad;
      }

      if (res != null) {
         res.getPDFResources().addShading(shading);
      } else {
         this.getDocument().getResources().addShading(shading);
      }

      return shading;
   }

   public PDFShading makeShading(PDFResourceContext res, int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, int theBitsPerCoordinate, int theBitsPerComponent, List theDecode, int theVerticesPerRow, PDFFunction theFunction) {
      PDFShading shading = new PDFShading(theShadingType, theColorSpace, theBackground, theBBox, theAntiAlias, theBitsPerCoordinate, theBitsPerComponent, theDecode, theVerticesPerRow, theFunction);
      PDFShading oldshad = this.getDocument().findShading(shading);
      if (oldshad == null) {
         this.getDocument().registerObject(shading);
      } else {
         shading = oldshad;
      }

      if (res != null) {
         res.getPDFResources().addShading(shading);
      } else {
         this.getDocument().getResources().addShading(shading);
      }

      return shading;
   }

   public PDFPattern makePattern(PDFResourceContext res, int thePatternType, PDFResources theResources, int thePaintType, int theTilingType, List theBBox, double theXStep, double theYStep, List theMatrix, List theXUID, StringBuffer thePatternDataStream) {
      PDFPattern pattern = new PDFPattern(theResources, 1, thePaintType, theTilingType, theBBox, theXStep, theYStep, theMatrix, theXUID, thePatternDataStream);
      PDFPattern oldpatt = this.getDocument().findPattern(pattern);
      if (oldpatt == null) {
         this.getDocument().registerObject(pattern);
      } else {
         pattern = oldpatt;
      }

      if (res != null) {
         res.getPDFResources().addPattern(pattern);
      } else {
         this.getDocument().getResources().addPattern(pattern);
      }

      return pattern;
   }

   public PDFPattern makePattern(PDFResourceContext res, int thePatternType, PDFShading theShading, List theXUID, StringBuffer theExtGState, List theMatrix) {
      PDFPattern pattern = new PDFPattern(2, theShading, theXUID, theExtGState, theMatrix);
      PDFPattern oldpatt = this.getDocument().findPattern(pattern);
      if (oldpatt == null) {
         this.getDocument().registerObject(pattern);
      } else {
         pattern = oldpatt;
      }

      if (res != null) {
         res.getPDFResources().addPattern(pattern);
      } else {
         this.getDocument().getResources().addPattern(pattern);
      }

      return pattern;
   }

   public PDFPattern makeGradient(PDFResourceContext res, boolean radial, PDFDeviceColorSpace theColorspace, List theColors, List theBounds, List theCoords, List theMatrix) {
      double interpolation = 1.0;
      List theFunctions = new ArrayList();
      int lastPosition = theColors.size() - 1;

      for(int currentPosition = 0; currentPosition < lastPosition; ++currentPosition) {
         PDFColor currentColor = (PDFColor)theColors.get(currentPosition);
         PDFColor nextColor = (PDFColor)theColors.get(currentPosition + 1);
         if (this.getDocument().getColorSpace() != currentColor.getColorSpace()) {
            currentColor.setColorSpace(this.getDocument().getColorSpace());
         }

         if (this.getDocument().getColorSpace() != nextColor.getColorSpace()) {
            nextColor.setColorSpace(this.getDocument().getColorSpace());
         }

         List theCzero = currentColor.getVector();
         List theCone = nextColor.getVector();
         PDFFunction myfunc = this.makeFunction(2, (List)null, (List)null, theCzero, theCone, interpolation);
         theFunctions.add(myfunc);
      }

      PDFFunction myfunky = this.makeFunction(3, (List)null, (List)null, theFunctions, theBounds, (List)null);
      PDFShading myShad;
      if (radial) {
         if (theCoords.size() == 6) {
            myShad = this.makeShading(res, 3, this.getDocument().getPDFColorSpace(), (List)null, (List)null, false, theCoords, (List)null, myfunky, (List)null);
         } else {
            List newCoords = new ArrayList();
            newCoords.add(theCoords.get(0));
            newCoords.add(theCoords.get(1));
            newCoords.add(theCoords.get(2));
            newCoords.add(theCoords.get(0));
            newCoords.add(theCoords.get(1));
            newCoords.add(new Double(0.0));
            myShad = this.makeShading(res, 3, this.getDocument().getPDFColorSpace(), (List)null, (List)null, false, newCoords, (List)null, myfunky, (List)null);
         }
      } else {
         myShad = this.makeShading(res, 2, this.getDocument().getPDFColorSpace(), (List)null, (List)null, false, theCoords, (List)null, myfunky, (List)null);
      }

      PDFPattern myPattern = this.makePattern(res, 2, myShad, (List)null, (StringBuffer)null, theMatrix);
      return myPattern;
   }

   protected PDFDestination getUniqueDestination(PDFDestination newdest) {
      PDFDestination existing = this.getDocument().findDestination(newdest);
      if (existing != null) {
         return existing;
      } else {
         this.getDocument().addDestination(newdest);
         return newdest;
      }
   }

   public PDFDestination makeDestination(String idRef, Object goToRef) {
      PDFDestination destination = new PDFDestination(idRef, goToRef);
      return this.getUniqueDestination(destination);
   }

   public PDFNames makeNames() {
      PDFNames names = new PDFNames();
      this.getDocument().registerObject(names);
      return names;
   }

   public PDFPageLabels makePageLabels() {
      PDFPageLabels pageLabels = new PDFPageLabels();
      this.getDocument().assignObjectNumber(pageLabels);
      this.getDocument().addTrailerObject(pageLabels);
      return pageLabels;
   }

   public PDFStructTreeRoot makeStructTreeRoot(PDFParentTree parentTree) {
      PDFStructTreeRoot structTreeRoot = new PDFStructTreeRoot(parentTree);
      this.getDocument().assignObjectNumber(structTreeRoot);
      this.getDocument().addTrailerObject(structTreeRoot);
      this.getDocument().getRoot().setStructTreeRoot(structTreeRoot);
      return structTreeRoot;
   }

   public PDFStructElem makeStructureElement(PDFName structureType, PDFObject parent) {
      PDFStructElem structElem = new PDFStructElem(parent, structureType);
      this.getDocument().assignObjectNumber(structElem);
      this.getDocument().addTrailerObject(structElem);
      return structElem;
   }

   public PDFDests makeDests(List destinationList) {
      boolean deep = true;
      PDFDests dests = new PDFDests();
      PDFArray kids = new PDFArray(dests);
      Iterator iter = destinationList.iterator();

      while(iter.hasNext()) {
         PDFDestination dest = (PDFDestination)iter.next();
         PDFNameTreeNode node = new PDFNameTreeNode();
         this.getDocument().registerObject(node);
         node.setLowerLimit(dest.getIDRef());
         node.setUpperLimit(dest.getIDRef());
         node.setNames(new PDFArray(node));
         PDFArray names = node.getNames();
         names.add(dest);
         kids.add(node);
      }

      dests.setLowerLimit(((PDFNameTreeNode)kids.get(0)).getLowerLimit());
      dests.setUpperLimit(((PDFNameTreeNode)kids.get(kids.length() - 1)).getUpperLimit());
      dests.setKids(kids);
      this.getDocument().registerObject(dests);
      return dests;
   }

   public PDFNameTreeNode makeNameTreeNode() {
      PDFNameTreeNode node = new PDFNameTreeNode();
      this.getDocument().registerObject(node);
      return node;
   }

   public PDFLink makeLink(Rectangle2D rect, PDFAction pdfAction) {
      if (rect != null && pdfAction != null) {
         PDFLink link = new PDFLink(rect);
         link.setAction(pdfAction);
         this.getDocument().registerObject(link);
         return link;
      } else {
         return null;
      }
   }

   public PDFLink makeLink(Rectangle2D rect, String page, String dest) {
      PDFLink link = new PDFLink(rect);
      this.getDocument().registerObject(link);
      PDFGoTo gt = new PDFGoTo(page);
      gt.setDestination(dest);
      this.getDocument().registerObject(gt);
      PDFInternalLink internalLink = new PDFInternalLink(gt.referencePDF());
      link.setAction(internalLink);
      return link;
   }

   public PDFLink makeLink(Rectangle2D rect, String destination, int linkType, float yoffset) {
      PDFLink link = new PDFLink(rect);
      if (linkType == 0) {
         link.setAction(this.getExternalAction(destination, false));
      } else {
         String goToReference = this.getGoToReference(destination, yoffset);
         PDFInternalLink internalLink = new PDFInternalLink(goToReference);
         link.setAction(internalLink);
      }

      PDFLink oldlink = this.getDocument().findLink(link);
      if (oldlink == null) {
         this.getDocument().registerObject(link);
      } else {
         link = oldlink;
      }

      return link;
   }

   public PDFAction getExternalAction(String target, boolean newWindow) {
      String targetLo = target.toLowerCase();
      if (targetLo.startsWith("http://")) {
         return new PDFUri(target);
      } else if (targetLo.startsWith("file://")) {
         target = target.substring("file://".length());
         return this.getLaunchAction(target);
      } else if (targetLo.endsWith(".pdf")) {
         return this.getGoToPDFAction(target, (String)null, -1, newWindow);
      } else {
         int index;
         String filename;
         if ((index = targetLo.indexOf(".pdf#page=")) > 0) {
            filename = target.substring(0, index + 4);
            int page = Integer.parseInt(target.substring(index + 10));
            return this.getGoToPDFAction(filename, (String)null, page, newWindow);
         } else if ((index = targetLo.indexOf(".pdf#dest=")) > 0) {
            filename = target.substring(0, index + 4);
            String dest = target.substring(index + 10);
            return this.getGoToPDFAction(filename, dest, -1, newWindow);
         } else {
            return new PDFUri(target);
         }
      }
   }

   public String getGoToReference(String pdfPageRef, float yoffset) {
      return this.getPDFGoTo(pdfPageRef, new Point2D.Float(0.0F, yoffset)).referencePDF();
   }

   public PDFGoTo getPDFGoTo(String pdfPageRef, Point2D position) {
      this.getDocument().getProfile().verifyActionAllowed();
      PDFGoTo gt = new PDFGoTo(pdfPageRef, position);
      PDFGoTo oldgt = this.getDocument().findGoTo(gt);
      if (oldgt == null) {
         this.getDocument().assignObjectNumber(gt);
         this.getDocument().addTrailerObject(gt);
      } else {
         gt = oldgt;
      }

      return gt;
   }

   private PDFGoToRemote getGoToPDFAction(String file, String dest, int page, boolean newWindow) {
      this.getDocument().getProfile().verifyActionAllowed();
      PDFFileSpec fileSpec = new PDFFileSpec(file);
      PDFFileSpec oldspec = this.getDocument().findFileSpec(fileSpec);
      if (oldspec == null) {
         this.getDocument().registerObject(fileSpec);
      } else {
         fileSpec = oldspec;
      }

      PDFGoToRemote remote;
      if (dest == null && page == -1) {
         remote = new PDFGoToRemote(fileSpec, newWindow);
      } else if (dest != null) {
         remote = new PDFGoToRemote(fileSpec, dest, newWindow);
      } else {
         remote = new PDFGoToRemote(fileSpec, page, newWindow);
      }

      PDFGoToRemote oldremote = this.getDocument().findGoToRemote(remote);
      if (oldremote == null) {
         this.getDocument().registerObject(remote);
      } else {
         remote = oldremote;
      }

      return remote;
   }

   private PDFLaunch getLaunchAction(String file) {
      this.getDocument().getProfile().verifyActionAllowed();
      PDFFileSpec fileSpec = new PDFFileSpec(file);
      PDFFileSpec oldSpec = this.getDocument().findFileSpec(fileSpec);
      if (oldSpec == null) {
         this.getDocument().registerObject(fileSpec);
      } else {
         fileSpec = oldSpec;
      }

      PDFLaunch launch = new PDFLaunch(fileSpec);
      PDFLaunch oldLaunch = this.getDocument().findLaunch(launch);
      if (oldLaunch == null) {
         this.getDocument().registerObject(launch);
      } else {
         launch = oldLaunch;
      }

      return launch;
   }

   public PDFOutline makeOutline(PDFOutline parent, String label, String actionRef, boolean showSubItems) {
      PDFOutline pdfOutline = new PDFOutline(label, actionRef, showSubItems);
      if (parent != null) {
         parent.addOutline(pdfOutline);
      }

      this.getDocument().registerObject(pdfOutline);
      return pdfOutline;
   }

   public PDFOutline makeOutline(PDFOutline parent, String label, PDFAction pdfAction, boolean showSubItems) {
      return pdfAction == null ? null : this.makeOutline(parent, label, pdfAction.getAction(), showSubItems);
   }

   public PDFOutline makeOutline(PDFOutline parent, String label, String destination, float yoffset, boolean showSubItems) {
      String goToRef = this.getGoToReference(destination, yoffset);
      return this.makeOutline(parent, label, goToRef, showSubItems);
   }

   public PDFEncoding makeEncoding(String encodingName) {
      PDFEncoding encoding = new PDFEncoding(encodingName);
      this.getDocument().registerObject(encoding);
      return encoding;
   }

   public PDFFont makeFont(String fontname, String basefont, String encoding, FontMetrics metrics, FontDescriptor descriptor) {
      PDFFont preRegisteredfont = this.getDocument().findFont(fontname);
      if (preRegisteredfont != null) {
         return preRegisteredfont;
      } else {
         boolean forceToUnicode = true;
         if (descriptor == null) {
            PDFFont font = new PDFFont(fontname, FontType.TYPE1, basefont, encoding);
            this.getDocument().registerObject(font);
            if (forceToUnicode && !PDFEncoding.isPredefinedEncoding(encoding)) {
               CodePointMapping mapping;
               if (encoding != null) {
                  mapping = CodePointMapping.getMapping(encoding);
               } else {
                  Typeface tf = (Typeface)metrics;
                  mapping = CodePointMapping.getMapping(tf.getEncodingName());
               }

               this.generateToUnicodeCmap(font, mapping);
            }

            return font;
         } else {
            FontType fonttype = metrics.getFontType();
            PDFFontDescriptor pdfdesc = this.makeFontDescriptor(descriptor);
            PDFFont font = null;
            font = PDFFont.createFont(fontname, fonttype, basefont, (Object)null);
            this.getDocument().registerObject(font);
            if (fonttype == FontType.TYPE0) {
               font.setEncoding(encoding);
               CIDFont cidMetrics;
               if (metrics instanceof LazyFont) {
                  cidMetrics = (CIDFont)((LazyFont)metrics).getRealFont();
               } else {
                  cidMetrics = (CIDFont)metrics;
               }

               PDFCIDSystemInfo sysInfo = new PDFCIDSystemInfo(cidMetrics.getRegistry(), cidMetrics.getOrdering(), cidMetrics.getSupplement());
               PDFCIDFont cidFont = new PDFCIDFont(basefont, cidMetrics.getCIDType(), cidMetrics.getDefaultWidth(), this.getSubsetWidths(cidMetrics), sysInfo, (PDFCIDFontDescriptor)pdfdesc);
               this.getDocument().registerObject(cidFont);
               PDFCMap cmap = new PDFToUnicodeCMap(cidMetrics.getCIDSubset().getSubsetChars(), "fop-ucs-H", new PDFCIDSystemInfo("Adobe", "Identity", 0), false);
               this.getDocument().registerObject(cmap);
               ((PDFFontType0)font).setCMAP(cmap);
               ((PDFFontType0)font).setDescendantFonts(cidFont);
            } else {
               PDFFontNonBase14 nonBase14 = (PDFFontNonBase14)font;
               nonBase14.setDescriptor(pdfdesc);
               SingleByteFont singleByteFont;
               if (metrics instanceof LazyFont) {
                  singleByteFont = (SingleByteFont)((LazyFont)metrics).getRealFont();
               } else {
                  singleByteFont = (SingleByteFont)metrics;
               }

               int firstChar = singleByteFont.getFirstChar();
               int lastChar = singleByteFont.getLastChar();
               nonBase14.setWidthMetrics(firstChar, lastChar, new PDFArray((PDFObject)null, metrics.getWidths()));
               SingleByteEncoding mapping = singleByteFont.getEncoding();
               if (singleByteFont.isSymbolicFont()) {
                  if (forceToUnicode) {
                     this.generateToUnicodeCmap(nonBase14, mapping);
                  }
               } else if (PDFEncoding.isPredefinedEncoding(mapping.getName())) {
                  font.setEncoding(mapping.getName());
               } else {
                  Object pdfEncoding = this.createPDFEncoding(mapping, singleByteFont.getFontName());
                  if (pdfEncoding instanceof PDFEncoding) {
                     font.setEncoding((PDFEncoding)pdfEncoding);
                  } else {
                     font.setEncoding((String)pdfEncoding);
                  }

                  if (forceToUnicode) {
                     this.generateToUnicodeCmap(nonBase14, mapping);
                  }
               }

               if (singleByteFont.hasAdditionalEncodings()) {
                  int i = 0;

                  for(int c = singleByteFont.getAdditionalEncodingCount(); i < c; ++i) {
                     SimpleSingleByteEncoding addEncoding = singleByteFont.getAdditionalEncoding(i);
                     String name = fontname + "_" + (i + 1);
                     Object pdfenc = this.createPDFEncoding(addEncoding, singleByteFont.getFontName());
                     PDFFontNonBase14 addFont = (PDFFontNonBase14)PDFFont.createFont(name, fonttype, basefont, pdfenc);
                     addFont.setDescriptor(pdfdesc);
                     addFont.setWidthMetrics(addEncoding.getFirstChar(), addEncoding.getLastChar(), new PDFArray((PDFObject)null, singleByteFont.getAdditionalWidths(i)));
                     this.getDocument().registerObject(addFont);
                     this.getDocument().getResources().addFont(addFont);
                     if (forceToUnicode) {
                        this.generateToUnicodeCmap(addFont, addEncoding);
                     }
                  }
               }
            }

            return font;
         }
      }
   }

   private void generateToUnicodeCmap(PDFFont font, SingleByteEncoding encoding) {
      PDFCMap cmap = new PDFToUnicodeCMap(encoding.getUnicodeCharMap(), "fop-ucs-H", new PDFCIDSystemInfo("Adobe", "Identity", 0), true);
      this.getDocument().registerObject(cmap);
      font.setToUnicode(cmap);
   }

   public Object createPDFEncoding(SingleByteEncoding encoding, String fontNameHint) {
      CodePointMapping baseEncoding;
      if (fontNameHint.indexOf("Symbol") >= 0) {
         baseEncoding = CodePointMapping.getMapping("SymbolEncoding");
      } else {
         baseEncoding = CodePointMapping.getMapping("StandardEncoding");
      }

      PDFEncoding pdfEncoding = new PDFEncoding(baseEncoding.getName());
      PDFEncoding.DifferencesBuilder builder = pdfEncoding.createDifferencesBuilder();
      int start = -1;
      String[] baseNames = baseEncoding.getCharNameMap();
      String[] charNameMap = encoding.getCharNameMap();
      int i = 0;

      for(int ci = charNameMap.length; i < ci; ++i) {
         String basec = baseNames[i];
         String c = charNameMap[i];
         if (!basec.equals(c)) {
            if (start != i) {
               builder.addDifference(i);
               start = i;
            }

            builder.addName(c);
            ++start;
         }
      }

      if (builder.hasDifferences()) {
         pdfEncoding.setDifferences(builder.toPDFArray());
         return pdfEncoding;
      } else {
         return baseEncoding.getName();
      }
   }

   public PDFWArray getSubsetWidths(CIDFont cidFont) {
      PDFWArray warray = new PDFWArray();
      int[] widths = cidFont.getWidths();
      CIDSubset subset = cidFont.getCIDSubset();
      int[] tmpWidth = new int[subset.getSubsetSize()];
      int i = 0;

      for(int c = subset.getSubsetSize(); i < c; ++i) {
         int nwx = Math.max(0, subset.getGlyphIndexForSubsetIndex(i));
         tmpWidth[i] = widths[nwx];
      }

      warray.addEntry(0, tmpWidth);
      return warray;
   }

   public PDFFontDescriptor makeFontDescriptor(FontDescriptor desc) {
      PDFFontDescriptor descriptor = null;
      if (desc.getFontType() == FontType.TYPE0) {
         descriptor = new PDFCIDFontDescriptor(desc.getEmbedFontName(), desc.getFontBBox(), desc.getCapHeight(), desc.getFlags(), desc.getItalicAngle(), desc.getStemV(), (String)null);
      } else {
         descriptor = new PDFFontDescriptor(desc.getEmbedFontName(), desc.getAscender(), desc.getDescender(), desc.getCapHeight(), desc.getFlags(), new PDFRectangle(desc.getFontBBox()), desc.getItalicAngle(), desc.getStemV());
      }

      this.getDocument().registerObject((PDFObject)descriptor);
      if (desc.isEmbeddable()) {
         AbstractPDFStream stream = this.makeFontFile(desc);
         if (stream != null) {
            ((PDFFontDescriptor)descriptor).setFontFile(desc.getFontType(), stream);
            this.getDocument().registerObject(stream);
         }

         CustomFont font = this.getCustomFont(desc);
         if (font instanceof CIDFont) {
            CIDFont cidFont = (CIDFont)font;
            this.buildCIDSet((PDFFontDescriptor)descriptor, cidFont);
         }
      }

      return (PDFFontDescriptor)descriptor;
   }

   private void buildCIDSet(PDFFontDescriptor descriptor, CIDFont cidFont) {
      BitSet cidSubset = cidFont.getCIDSubset().getGlyphIndexBitSet();
      PDFStream cidSet = this.makeStream((String)null, true);
      ByteArrayOutputStream baout = new ByteArrayOutputStream(cidSubset.length() / 8 + 1);
      int value = 0;
      int i = 0;

      for(int c = cidSubset.length(); i < c; ++i) {
         int shift = i % 8;
         boolean b = cidSubset.get(i);
         if (b) {
            value |= 1 << 7 - shift;
         }

         if (shift == 7) {
            baout.write(value);
            value = 0;
         }
      }

      baout.write(value);

      try {
         cidSet.setData(baout.toByteArray());
         descriptor.setCIDSet(cidSet);
      } catch (IOException var11) {
         this.log.error("Failed to write CIDSet [" + cidFont + "] " + cidFont.getEmbedFontName(), var11);
      }

   }

   public AbstractPDFStream makeFontFile(FontDescriptor desc) {
      if (desc.getFontType() == FontType.OTHER) {
         throw new IllegalArgumentException("Trying to embed unsupported font type: " + desc.getFontType());
      } else {
         CustomFont font = this.getCustomFont(desc);
         InputStream in = null;

         try {
            Source source = font.getEmbedFileSource();
            if (source == null && font.getEmbedResourceName() != null) {
               source = new StreamSource(this.getClass().getResourceAsStream(font.getEmbedResourceName()));
            }

            if (source == null) {
               return null;
            } else {
               if (source instanceof StreamSource) {
                  in = ((StreamSource)source).getInputStream();
               }

               if (in == null && ((Source)source).getSystemId() != null) {
                  try {
                     in = (new URL(((Source)source).getSystemId())).openStream();
                  } catch (MalformedURLException var15) {
                     new FileNotFoundException("File not found. URL could not be resolved: " + var15.getMessage());
                  }
               }

               if (in == null) {
                  return null;
               } else {
                  if (!(in instanceof BufferedInputStream)) {
                     in = new BufferedInputStream((InputStream)in);
                  }

                  if (in == null) {
                     return null;
                  } else {
                     Object var19;
                     try {
                        Object embeddedFont;
                        if (desc.getFontType() == FontType.TYPE0) {
                           MultiByteFont mbfont = (MultiByteFont)font;
                           FontFileReader reader = new FontFileReader((InputStream)in);
                           TTFSubSetFile subset = new TTFSubSetFile();
                           byte[] subsetFont = subset.readFont(reader, mbfont.getTTCName(), mbfont.getUsedGlyphs());
                           embeddedFont = new PDFTTFStream(subsetFont.length);
                           ((PDFTTFStream)embeddedFont).setData(subsetFont, subsetFont.length);
                        } else if (desc.getFontType() == FontType.TYPE1) {
                           PFBParser parser = new PFBParser();
                           PFBData pfb = parser.parsePFB((InputStream)in);
                           embeddedFont = new PDFT1Stream();
                           ((PDFT1Stream)embeddedFont).setData(pfb);
                        } else {
                           byte[] file = IOUtils.toByteArray((InputStream)in);
                           embeddedFont = new PDFTTFStream(file.length);
                           ((PDFTTFStream)embeddedFont).setData(file, file.length);
                        }

                        var19 = embeddedFont;
                     } finally {
                        ((InputStream)in).close();
                     }

                     return (AbstractPDFStream)var19;
                  }
               }
            }
         } catch (IOException var16) {
            this.log.error("Failed to embed font [" + desc + "] " + desc.getEmbedFontName(), var16);
            return null;
         }
      }
   }

   private CustomFont getCustomFont(FontDescriptor desc) {
      Typeface tempFont;
      if (desc instanceof LazyFont) {
         tempFont = ((LazyFont)desc).getRealFont();
      } else {
         tempFont = (Typeface)desc;
      }

      if (!(tempFont instanceof CustomFont)) {
         throw new IllegalArgumentException("FontDescriptor must be instance of CustomFont, but is a " + desc.getClass().getName());
      } else {
         return (CustomFont)tempFont;
      }
   }

   public PDFStream makeStream(String type, boolean add) {
      PDFStream obj = new PDFStream();
      obj.setDocument(this.getDocument());
      obj.getFilterList().addDefaultFilters(this.getDocument().getFilterMap(), type);
      if (add) {
         this.getDocument().registerObject(obj);
      }

      return obj;
   }

   public PDFICCStream makePDFICCStream() {
      PDFICCStream iccStream = new PDFICCStream();
      this.getDocument().registerObject(iccStream);
      return iccStream;
   }

   public PDFICCBasedColorSpace makeICCBasedColorSpace(PDFResourceContext res, String explicitName, PDFICCStream iccStream) {
      PDFICCBasedColorSpace cs = new PDFICCBasedColorSpace(explicitName, iccStream);
      this.getDocument().registerObject(cs);
      if (res != null) {
         res.getPDFResources().addColorSpace(cs);
      } else {
         this.getDocument().getResources().addColorSpace(cs);
      }

      return cs;
   }

   public PDFArray makeArray(int[] values) {
      PDFArray array = new PDFArray((PDFObject)null, values);
      this.getDocument().registerObject(array);
      return array;
   }

   public PDFGState makeGState(Map settings, PDFGState current) {
      PDFGState wanted = new PDFGState();
      wanted.addValues(PDFGState.DEFAULT);
      wanted.addValues(settings);
      PDFGState existing = this.getDocument().findGState(wanted, current);
      if (existing != null) {
         return existing;
      } else {
         PDFGState gstate = new PDFGState();
         gstate.addValues(settings);
         this.getDocument().registerObject(gstate);
         return gstate;
      }
   }

   public PDFAnnotList makeAnnotList() {
      PDFAnnotList obj = new PDFAnnotList();
      this.getDocument().assignObjectNumber(obj);
      return obj;
   }
}
