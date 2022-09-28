package org.apache.fop.render.rtf;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.ResourceEventProducer;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FOEventHandler;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FOText;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.XMLObj;
import org.apache.fop.fo.flow.AbstractGraphics;
import org.apache.fop.fo.flow.BasicLink;
import org.apache.fop.fo.flow.Block;
import org.apache.fop.fo.flow.BlockContainer;
import org.apache.fop.fo.flow.Character;
import org.apache.fop.fo.flow.ExternalGraphic;
import org.apache.fop.fo.flow.Footnote;
import org.apache.fop.fo.flow.FootnoteBody;
import org.apache.fop.fo.flow.Inline;
import org.apache.fop.fo.flow.InstreamForeignObject;
import org.apache.fop.fo.flow.Leader;
import org.apache.fop.fo.flow.ListBlock;
import org.apache.fop.fo.flow.ListItem;
import org.apache.fop.fo.flow.ListItemBody;
import org.apache.fop.fo.flow.ListItemLabel;
import org.apache.fop.fo.flow.PageNumber;
import org.apache.fop.fo.flow.PageNumberCitation;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableBody;
import org.apache.fop.fo.flow.table.TableCell;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.flow.table.TableFooter;
import org.apache.fop.fo.flow.table.TableHeader;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.fo.flow.table.TableRow;
import org.apache.fop.fo.pagination.Flow;
import org.apache.fop.fo.pagination.PageSequence;
import org.apache.fop.fo.pagination.PageSequenceMaster;
import org.apache.fop.fo.pagination.Region;
import org.apache.fop.fo.pagination.SimplePageMaster;
import org.apache.fop.fo.pagination.StaticContent;
import org.apache.fop.fo.properties.CommonBorderPaddingBackground;
import org.apache.fop.fo.properties.EnumLength;
import org.apache.fop.fonts.FontSetup;
import org.apache.fop.layoutmgr.inline.ImageLayout;
import org.apache.fop.layoutmgr.table.ColumnSetup;
import org.apache.fop.render.DefaultFontResolver;
import org.apache.fop.render.RendererEventProducer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfAfterContainer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfBeforeContainer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfListContainer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfOptions;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfTableContainer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.IRtfTextrunContainer;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAfter;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfAttributes;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfBefore;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfDocumentArea;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfElement;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfExternalGraphic;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfFile;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfFootnote;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfHyperLink;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfList;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfListItem;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfSection;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfTable;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfTableCell;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfTableRow;
import org.apache.fop.render.rtf.rtflib.rtfdoc.RtfTextrun;
import org.apache.fop.render.rtf.rtflib.tools.BuilderContext;
import org.apache.fop.render.rtf.rtflib.tools.PercentContext;
import org.apache.fop.render.rtf.rtflib.tools.TableContext;
import org.apache.xmlgraphics.image.loader.Image;
import org.apache.xmlgraphics.image.loader.ImageException;
import org.apache.xmlgraphics.image.loader.ImageFlavor;
import org.apache.xmlgraphics.image.loader.ImageInfo;
import org.apache.xmlgraphics.image.loader.ImageManager;
import org.apache.xmlgraphics.image.loader.ImageSessionContext;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.apache.xmlgraphics.image.loader.impl.ImageRawStream;
import org.apache.xmlgraphics.image.loader.impl.ImageXMLDOM;
import org.apache.xmlgraphics.image.loader.util.ImageUtil;
import org.w3c.dom.Document;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class RTFHandler extends FOEventHandler {
   private RtfFile rtfFile;
   private final OutputStream os;
   private static Log log;
   private RtfSection sect;
   private RtfDocumentArea docArea;
   private boolean bDefer;
   private boolean bPrevHeaderSpecified = false;
   private boolean bPrevFooterSpecified = false;
   private boolean bHeaderSpecified = false;
   private boolean bFooterSpecified = false;
   private BuilderContext builderContext = new BuilderContext((IRtfOptions)null);
   private SimplePageMaster pagemaster;
   private int nestedTableDepth = 1;
   private PercentContext percentManager = new PercentContext();
   private static final ImageFlavor[] FLAVORS;

   public RTFHandler(FOUserAgent userAgent, OutputStream os) {
      super(userAgent);
      this.os = os;
      this.bDefer = true;
      FontSetup.setup(this.fontInfo, (List)null, new DefaultFontResolver(userAgent));
   }

   protected void handleIOTrouble(IOException ioe) {
      RendererEventProducer eventProducer = RendererEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
      eventProducer.ioError(this, ioe);
   }

   public void startDocument() throws SAXException {
      try {
         this.rtfFile = new RtfFile(new OutputStreamWriter(this.os));
         this.docArea = this.rtfFile.startDocumentArea();
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   public void endDocument() throws SAXException {
      try {
         this.rtfFile.flush();
      } catch (IOException var2) {
         throw new SAXException(var2);
      }
   }

   public void startPageSequence(PageSequence pageSeq) {
      try {
         if (this.pagemaster == null) {
            String reference = pageSeq.getMasterReference();
            this.pagemaster = pageSeq.getRoot().getLayoutMasterSet().getSimplePageMaster(reference);
            if (this.pagemaster == null) {
               RTFEventProducer eventProducer = RTFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
               eventProducer.onlySPMSupported(this, reference, pageSeq.getLocator());
               PageSequenceMaster master = pageSeq.getRoot().getLayoutMasterSet().getPageSequenceMaster(reference);
               this.pagemaster = master.getNextSimplePageMaster(false, false, false, false);
            }
         }

         if (this.bDefer) {
            return;
         }

         this.sect = this.docArea.newSection();
         if (this.pagemaster != null) {
            this.sect.getRtfAttributes().set(PageAttributesConverter.convertPageAttributes(this.pagemaster));
         } else {
            RTFEventProducer eventProducer = RTFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.noSPMFound(this, pageSeq.getLocator());
         }

         this.builderContext.pushContainer(this.sect);
         int useAblePageWidth = this.pagemaster.getPageWidth().getValue() - this.pagemaster.getCommonMarginBlock().marginLeft.getValue() - this.pagemaster.getCommonMarginBlock().marginRight.getValue() - this.sect.getRtfAttributes().getValueAsInteger("margl") - this.sect.getRtfAttributes().getValueAsInteger("margr");
         this.percentManager.setDimension(pageSeq, useAblePageWidth);
         this.bHeaderSpecified = false;
         this.bFooterSpecified = false;
      } catch (IOException var5) {
         this.handleIOTrouble(var5);
      }

   }

   public void endPageSequence(PageSequence pageSeq) {
      if (this.bDefer) {
         this.bDefer = false;
         this.recurseFONode(pageSeq);
         this.pagemaster = null;
         this.bDefer = true;
      } else {
         this.builderContext.popContainer();
         this.pagemaster = null;
      }
   }

   public void startFlow(Flow fl) {
      if (!this.bDefer) {
         try {
            log.debug("starting flow: " + fl.getFlowName());
            boolean handled = false;
            Region regionBody = this.pagemaster.getRegion(58);
            Region regionBefore = this.pagemaster.getRegion(57);
            Region regionAfter = this.pagemaster.getRegion(56);
            if (fl.getFlowName().equals(regionBody.getRegionName())) {
               RtfAttributes attr;
               if (this.bPrevHeaderSpecified && !this.bHeaderSpecified) {
                  attr = new RtfAttributes();
                  attr.set("header");
                  IRtfBeforeContainer contBefore = (IRtfBeforeContainer)this.builderContext.getContainer(IRtfBeforeContainer.class, true, this);
                  contBefore.newBefore(attr);
               }

               if (this.bPrevFooterSpecified && !this.bFooterSpecified) {
                  attr = new RtfAttributes();
                  attr.set("footer");
                  IRtfAfterContainer contAfter = (IRtfAfterContainer)this.builderContext.getContainer(IRtfAfterContainer.class, true, this);
                  contAfter.newAfter(attr);
               }

               handled = true;
            } else {
               RtfAttributes afterAttributes;
               if (regionBefore != null && fl.getFlowName().equals(regionBefore.getRegionName())) {
                  this.bHeaderSpecified = true;
                  this.bPrevHeaderSpecified = true;
                  IRtfBeforeContainer c = (IRtfBeforeContainer)this.builderContext.getContainer(IRtfBeforeContainer.class, true, this);
                  afterAttributes = ((RtfElement)c).getRtfAttributes();
                  if (afterAttributes == null) {
                     afterAttributes = new RtfAttributes();
                  }

                  afterAttributes.set("header");
                  RtfBefore before = c.newBefore(afterAttributes);
                  this.builderContext.pushContainer(before);
                  handled = true;
               } else if (regionAfter != null && fl.getFlowName().equals(regionAfter.getRegionName())) {
                  this.bFooterSpecified = true;
                  this.bPrevFooterSpecified = true;
                  IRtfAfterContainer c = (IRtfAfterContainer)this.builderContext.getContainer(IRtfAfterContainer.class, true, this);
                  afterAttributes = ((RtfElement)c).getRtfAttributes();
                  if (afterAttributes == null) {
                     afterAttributes = new RtfAttributes();
                  }

                  afterAttributes.set("footer");
                  RtfAfter after = c.newAfter(afterAttributes);
                  this.builderContext.pushContainer(after);
                  handled = true;
               }
            }

            if (!handled) {
               log.warn("A " + fl.getLocalName() + " has been skipped: " + fl.getFlowName());
            }
         } catch (IOException var9) {
            this.handleIOTrouble(var9);
         } catch (Exception var10) {
            log.error("startFlow: " + var10.getMessage());
            throw new RuntimeException(var10.getMessage());
         }

      }
   }

   public void endFlow(Flow fl) {
      if (!this.bDefer) {
         try {
            Region regionBody = this.pagemaster.getRegion(58);
            Region regionBefore = this.pagemaster.getRegion(57);
            Region regionAfter = this.pagemaster.getRegion(56);
            if (!fl.getFlowName().equals(regionBody.getRegionName())) {
               if (regionBefore != null && fl.getFlowName().equals(regionBefore.getRegionName())) {
                  this.builderContext.popContainer();
               } else if (regionAfter != null && fl.getFlowName().equals(regionAfter.getRegionName())) {
                  this.builderContext.popContainer();
               }
            }

         } catch (Exception var5) {
            log.error("endFlow: " + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }
      }
   }

   public void startBlock(Block bl) {
      if (!this.bDefer) {
         try {
            RtfAttributes rtfAttr = TextAttributesConverter.convertAttributes(bl);
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addParagraphBreak();
            textrun.pushBlockAttributes(rtfAttr);
            textrun.addBookmark(bl.getId());
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("startBlock: " + var6.getMessage());
            throw new RuntimeException("Exception: " + var6);
         }

      }
   }

   public void endBlock(Block bl) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addParagraphBreak();
            textrun.popBlockAttributes();
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (Exception var5) {
            log.error("startBlock:" + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }

      }
   }

   public void startBlockContainer(BlockContainer blc) {
      if (!this.bDefer) {
         try {
            RtfAttributes rtfAttr = TextAttributesConverter.convertBlockContainerAttributes(blc);
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addParagraphBreak();
            textrun.pushBlockAttributes(rtfAttr);
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("startBlock: " + var6.getMessage());
            throw new RuntimeException("Exception: " + var6);
         }

      }
   }

   public void endBlockContainer(BlockContainer bl) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addParagraphBreak();
            textrun.popBlockAttributes();
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (Exception var5) {
            log.error("startBlock:" + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }

      }
   }

   public void startTable(Table tbl) {
      if (!this.bDefer) {
         TableContext tableContext = new TableContext(this.builderContext);

         try {
            IRtfTableContainer tc = (IRtfTableContainer)this.builderContext.getContainer(IRtfTableContainer.class, true, (Object)null);
            RtfAttributes atts = TableAttributesConverter.convertTableAttributes(tbl);
            RtfTable table = tc.newTable(atts, tableContext);
            table.setNestedTableDepth(this.nestedTableDepth);
            ++this.nestedTableDepth;
            CommonBorderPaddingBackground border = tbl.getCommonBorderPaddingBackground();
            RtfAttributes borderAttributes = new RtfAttributes();
            BorderAttributesConverter.makeBorder(border, 0, borderAttributes, "clbrdrt");
            BorderAttributesConverter.makeBorder(border, 1, borderAttributes, "clbrdrb");
            BorderAttributesConverter.makeBorder(border, 2, borderAttributes, "clbrdrl");
            BorderAttributesConverter.makeBorder(border, 3, borderAttributes, "clbrdrr");
            table.setBorderAttributes(borderAttributes);
            this.builderContext.pushContainer(table);
         } catch (IOException var8) {
            this.handleIOTrouble(var8);
         } catch (Exception var9) {
            log.error("startTable:" + var9.getMessage());
            throw new RuntimeException(var9.getMessage());
         }

         this.builderContext.pushTableContext(tableContext);
      }
   }

   public void endTable(Table tbl) {
      if (!this.bDefer) {
         --this.nestedTableDepth;
         this.builderContext.popTableContext();
         this.builderContext.popContainer();
      }
   }

   public void startColumn(TableColumn tc) {
      if (!this.bDefer) {
         try {
            int iWidth = tc.getColumnWidth().getValue(this.percentManager);
            this.percentManager.setDimension(tc, iWidth);
            Float width = new Float(FoUnitsConverter.getInstance().convertMptToTwips(iWidth));
            this.builderContext.getTableContext().setNextColumnWidth(width);
            this.builderContext.getTableContext().setNextColumnRowSpanning(new Integer(0), (RtfAttributes)null);
            this.builderContext.getTableContext().setNextFirstSpanningCol(false);
         } catch (Exception var4) {
            log.error("startColumn: " + var4.getMessage());
            throw new RuntimeException(var4.getMessage());
         }
      }
   }

   public void endColumn(TableColumn tc) {
      if (!this.bDefer) {
         ;
      }
   }

   public void startHeader(TableHeader header) {
      this.startPart(header);
   }

   public void endHeader(TableHeader header) {
      this.endPart(header);
   }

   public void startFooter(TableFooter footer) {
      this.startPart(footer);
   }

   public void endFooter(TableFooter footer) {
      this.endPart(footer);
   }

   public void startInline(Inline inl) {
      if (!this.bDefer) {
         try {
            RtfAttributes rtfAttr = TextAttributesConverter.convertCharacterAttributes(inl);
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.pushInlineAttributes(rtfAttr);
            textrun.addBookmark(inl.getId());
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (FOPException var6) {
            log.error("startInline:" + var6.getMessage());
            throw new RuntimeException(var6.getMessage());
         } catch (Exception var7) {
            log.error("startInline:" + var7.getMessage());
            throw new RuntimeException(var7.getMessage());
         }

      }
   }

   public void endInline(Inline inl) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.popInlineAttributes();
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (Exception var5) {
            log.error("startInline:" + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }

      }
   }

   private void startPart(TablePart part) {
      if (!this.bDefer) {
         try {
            RtfAttributes atts = TableAttributesConverter.convertTablePartAttributes(part);
            RtfTable tbl = (RtfTable)this.builderContext.getContainer(RtfTable.class, true, this);
            tbl.setHeaderAttribs(atts);
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (Exception var5) {
            log.error("startPart: " + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }

      }
   }

   private void endPart(TablePart tb) {
      if (!this.bDefer) {
         try {
            RtfTable tbl = (RtfTable)this.builderContext.getContainer(RtfTable.class, true, this);
            tbl.setHeaderAttribs((RtfAttributes)null);
         } catch (IOException var3) {
            this.handleIOTrouble(var3);
         } catch (Exception var4) {
            log.error("endPart: " + var4.getMessage());
            throw new RuntimeException(var4.getMessage());
         }

      }
   }

   public void startBody(TableBody body) {
      this.startPart(body);
   }

   public void endBody(TableBody body) {
      this.endPart(body);
   }

   public void startRow(TableRow tr) {
      if (!this.bDefer) {
         try {
            RtfTable tbl = (RtfTable)this.builderContext.getContainer(RtfTable.class, true, (Object)null);
            RtfAttributes atts = TableAttributesConverter.convertRowAttributes(tr, tbl.getHeaderAttribs());
            if (tr.getParent() instanceof TableHeader) {
               atts.set("trhdr");
            }

            this.builderContext.pushContainer(tbl.newTableRow(atts));
            this.builderContext.getTableContext().selectFirstColumn();
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (Exception var5) {
            log.error("startRow: " + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }

      }
   }

   public void endRow(TableRow tr) {
      if (!this.bDefer) {
         try {
            TableContext tctx = this.builderContext.getTableContext();

            for(RtfTableRow row = (RtfTableRow)this.builderContext.getContainer(RtfTableRow.class, true, (Object)null); tctx.getNumberOfColumns() > tctx.getColumnIndex() && tctx.getColumnRowSpanningNumber() > 0; tctx.selectNextColumn()) {
               RtfTableCell vCell = row.newTableCellMergedVertically((int)tctx.getColumnWidth(), tctx.getColumnRowSpanningAttrs());
               if (!tctx.getFirstSpanningCol()) {
                  vCell.setHMerge(2);
               }
            }
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("endRow: " + var6.getMessage());
            throw new RuntimeException(var6.getMessage());
         }

         this.builderContext.popContainer();
         this.builderContext.getTableContext().decreaseRowSpannings();
      }
   }

   public void startCell(TableCell tc) {
      if (!this.bDefer) {
         try {
            TableContext tctx = this.builderContext.getTableContext();
            RtfTableRow row = (RtfTableRow)this.builderContext.getContainer(RtfTableRow.class, true, (Object)null);
            int numberRowsSpanned = tc.getNumberRowsSpanned();

            int numberColumnsSpanned;
            for(numberColumnsSpanned = tc.getNumberColumnsSpanned(); tctx.getNumberOfColumns() > tctx.getColumnIndex() && tctx.getColumnRowSpanningNumber() > 0; tctx.selectNextColumn()) {
               RtfTableCell vCell = row.newTableCellMergedVertically((int)tctx.getColumnWidth(), tctx.getColumnRowSpanningAttrs());
               if (!tctx.getFirstSpanningCol()) {
                  vCell.setHMerge(2);
               }
            }

            float width = tctx.getColumnWidth();
            RtfAttributes atts = TableAttributesConverter.convertCellAttributes(tc);
            RtfTableCell cell = row.newTableCell((int)width, atts);
            if (numberRowsSpanned > 1) {
               cell.setVMerge(1);
               tctx.setCurrentColumnRowSpanning(new Integer(numberRowsSpanned), cell.getRtfAttributes());
            } else {
               tctx.setCurrentColumnRowSpanning(new Integer(numberRowsSpanned), (RtfAttributes)null);
            }

            if (numberColumnsSpanned > 0) {
               tctx.setCurrentFirstSpanningCol(true);

               for(int i = 0; i < numberColumnsSpanned - 1; ++i) {
                  tctx.selectNextColumn();
                  width += tctx.getColumnWidth();
                  tctx.setCurrentFirstSpanningCol(false);
                  RtfTableCell hCell = row.newTableCellMergedHorizontally(0, (RtfAttributes)null);
                  if (numberRowsSpanned > 1) {
                     hCell.setVMerge(1);
                     tctx.setCurrentColumnRowSpanning(new Integer(numberRowsSpanned), cell.getRtfAttributes());
                  } else {
                     tctx.setCurrentColumnRowSpanning(new Integer(numberRowsSpanned), cell.getRtfAttributes());
                  }
               }
            }

            this.percentManager.setDimension(tc, (int)width * 50);
            this.builderContext.pushContainer(cell);
         } catch (IOException var11) {
            this.handleIOTrouble(var11);
         } catch (Exception var12) {
            log.error("startCell: " + var12.getMessage());
            throw new RuntimeException(var12.getMessage());
         }

      }
   }

   public void endCell(TableCell tc) {
      if (!this.bDefer) {
         this.builderContext.popContainer();
         this.builderContext.getTableContext().selectNextColumn();
      }
   }

   public void startList(ListBlock lb) {
      if (!this.bDefer) {
         try {
            IRtfListContainer c = (IRtfListContainer)this.builderContext.getContainer(IRtfListContainer.class, true, this);
            RtfList newList = c.newList(ListAttributesConverter.convertAttributes(lb));
            this.builderContext.pushContainer(newList);
         } catch (IOException var4) {
            this.handleIOTrouble(var4);
         } catch (FOPException var5) {
            log.error("startList: " + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         } catch (Exception var6) {
            log.error("startList: " + var6.getMessage());
            throw new RuntimeException(var6.getMessage());
         }

      }
   }

   public void endList(ListBlock lb) {
      if (!this.bDefer) {
         this.builderContext.popContainer();
      }
   }

   public void startListItem(ListItem li) {
      if (!this.bDefer) {
         try {
            RtfList list = (RtfList)this.builderContext.getContainer(RtfList.class, true, this);
            if (list.getChildCount() > 0) {
               this.endListBody();
               this.endList((ListBlock)li.getParent());
               this.startList((ListBlock)li.getParent());
               this.startListBody();
               list = (RtfList)this.builderContext.getContainer(RtfList.class, true, this);
            }

            this.builderContext.pushContainer(list.newListItem());
         } catch (IOException var3) {
            this.handleIOTrouble(var3);
         } catch (Exception var4) {
            log.error("startList: " + var4.getMessage());
            throw new RuntimeException(var4.getMessage());
         }

      }
   }

   public void endListItem(ListItem li) {
      if (!this.bDefer) {
         this.builderContext.popContainer();
      }
   }

   public void startListLabel() {
      if (!this.bDefer) {
         try {
            RtfListItem item = (RtfListItem)this.builderContext.getContainer(RtfListItem.class, true, this);
            RtfListItem.RtfListItemLabel label = item.new RtfListItemLabel(item);
            this.builderContext.pushContainer(label);
         } catch (IOException var3) {
            this.handleIOTrouble(var3);
         } catch (Exception var4) {
            log.error("startPageNumber: " + var4.getMessage());
            throw new RuntimeException(var4.getMessage());
         }

      }
   }

   public void endListLabel() {
      if (!this.bDefer) {
         this.builderContext.popContainer();
      }
   }

   public void startListBody() {
   }

   public void endListBody() {
   }

   public void startStatic() {
   }

   public void endStatic() {
   }

   public void startMarkup() {
   }

   public void endMarkup() {
   }

   public void startLink(BasicLink basicLink) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            RtfHyperLink link = textrun.addHyperlink(new RtfAttributes());
            if (basicLink.hasExternalDestination()) {
               link.setExternalURL(basicLink.getExternalDestination());
            } else {
               link.setInternalURL(basicLink.getInternalDestination());
            }

            this.builderContext.pushContainer(link);
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("startLink: " + var6.getMessage());
            throw new RuntimeException(var6.getMessage());
         }

      }
   }

   public void endLink() {
      if (!this.bDefer) {
         this.builderContext.popContainer();
      }
   }

   public void image(ExternalGraphic eg) {
      if (!this.bDefer) {
         String uri = eg.getURL();
         ImageInfo info = null;

         ResourceEventProducer eventProducer;
         try {
            FOUserAgent userAgent = eg.getUserAgent();
            ImageManager manager = userAgent.getFactory().getImageManager();
            info = manager.getImageInfo(uri, userAgent.getImageSessionContext());
            this.putGraphic(eg, (ImageInfo)info);
         } catch (ImageException var6) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageError(this, info != null ? info.toString() : uri, var6, (Locator)null);
         } catch (FileNotFoundException var7) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageNotFound(this, info != null ? info.toString() : uri, var7, (Locator)null);
         } catch (IOException var8) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageIOError(this, info != null ? info.toString() : uri, var8, (Locator)null);
         }

      }
   }

   public void foreignObject(InstreamForeignObject ifo) {
      if (!this.bDefer) {
         ResourceEventProducer eventProducer;
         try {
            XMLObj child = ifo.getChildXMLObj();
            Document doc = child.getDOMDocument();
            String ns = child.getNamespaceURI();
            ImageInfo info = new ImageInfo((String)null, (String)null);
            FOUserAgent ua = ifo.getUserAgent();
            ImageSize size = new ImageSize();
            size.setResolution((double)ua.getSourceResolution());
            Point2D csize = new Point2D.Float(-1.0F, -1.0F);
            Point2D intrinsicDimensions = child.getDimension(csize);
            if (intrinsicDimensions == null) {
               ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
               eventProducer.ifoNoIntrinsicSize(this, child.getLocator());
               return;
            }

            size.setSizeInMillipoints((int)Math.round(intrinsicDimensions.getX() * 1000.0), (int)Math.round(intrinsicDimensions.getY() * 1000.0));
            size.calcPixelsFromSize();
            info.setSize(size);
            ImageXMLDOM image = new ImageXMLDOM(info, doc, ns);
            FOUserAgent userAgent = ifo.getUserAgent();
            ImageManager manager = userAgent.getFactory().getImageManager();
            Map hints = ImageUtil.getDefaultHints(ua.getImageSessionContext());
            Image converted = manager.convertImage(image, FLAVORS, hints);
            this.putGraphic(ifo, (Image)converted);
         } catch (ImageException var15) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageError(this, (String)null, var15, (Locator)null);
         } catch (IOException var16) {
            eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.imageIOError(this, (String)null, var16, (Locator)null);
         }

      }
   }

   private void putGraphic(AbstractGraphics abstractGraphic, ImageInfo info) throws IOException {
      try {
         FOUserAgent userAgent = abstractGraphic.getUserAgent();
         ImageManager manager = userAgent.getFactory().getImageManager();
         ImageSessionContext sessionContext = userAgent.getImageSessionContext();
         Map hints = ImageUtil.getDefaultHints(sessionContext);
         Image image = manager.getImage(info, FLAVORS, hints, sessionContext);
         this.putGraphic(abstractGraphic, image);
      } catch (ImageException var8) {
         ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageError(this, (String)null, var8, (Locator)null);
      }

   }

   private void putGraphic(AbstractGraphics abstractGraphic, Image image) throws IOException {
      byte[] rawData = null;
      final ImageInfo info = image.getInfo();
      if (image instanceof ImageRawStream) {
         ImageRawStream rawImage = (ImageRawStream)image;
         InputStream in = rawImage.createInputStream();

         try {
            rawData = IOUtils.toByteArray(in);
         } finally {
            IOUtils.closeQuietly(in);
         }
      }

      if (rawData == null) {
         ResourceEventProducer eventProducer = ResourceEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.imageWritingError(this, (Exception)null);
      } else {
         this.percentManager.setDimension(abstractGraphic);
         PercentBaseContext pContext = new PercentBaseContext() {
            public int getBaseLength(int lengthBase, FObj fobj) {
               switch (lengthBase) {
                  case 7:
                     return info.getSize().getWidthMpt();
                  case 8:
                     return info.getSize().getHeightMpt();
                  default:
                     return RTFHandler.this.percentManager.getBaseLength(lengthBase, fobj);
               }
            }
         };
         ImageLayout layout = new ImageLayout(abstractGraphic, pContext, image.getInfo().getSize().getDimensionMpt());
         IRtfTextrunContainer c = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
         RtfExternalGraphic rtfGraphic = c.getTextrun().newImage();
         if (info.getOriginalURI() != null) {
            rtfGraphic.setURL(info.getOriginalURI());
         }

         rtfGraphic.setImageData(rawData);
         FoUnitsConverter converter = FoUnitsConverter.getInstance();
         Dimension viewport = layout.getViewportSize();
         Rectangle placement = layout.getPlacement();
         int cropLeft = Math.round(converter.convertMptToTwips(-placement.x));
         int cropTop = Math.round(converter.convertMptToTwips(-placement.y));
         int cropRight = Math.round(converter.convertMptToTwips(-1 * (viewport.width - placement.x - placement.width)));
         int cropBottom = Math.round(converter.convertMptToTwips(-1 * (viewport.height - placement.y - placement.height)));
         rtfGraphic.setCropping(cropLeft, cropTop, cropRight, cropBottom);
         int width = Math.round(converter.convertMptToTwips(viewport.width));
         int height = Math.round(converter.convertMptToTwips(viewport.height));
         width += cropLeft + cropRight;
         height += cropTop + cropBottom;
         rtfGraphic.setWidthTwips(width);
         rtfGraphic.setHeightTwips(height);
         int compression = 0;
         if (compression != 0 && !rtfGraphic.setCompressionRate(compression)) {
            log.warn("The compression rate " + compression + " is invalid. The value has to be between 1 and 100 %.");
         }

      }
   }

   public void pageRef() {
   }

   public void startFootnote(Footnote footnote) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            RtfFootnote rtfFootnote = textrun.addFootnote();
            this.builderContext.pushContainer(rtfFootnote);
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("startFootnote: " + var6.getMessage());
            throw new RuntimeException("Exception: " + var6);
         }

      }
   }

   public void endFootnote(Footnote footnote) {
      if (!this.bDefer) {
         this.builderContext.popContainer();
      }
   }

   public void startFootnoteBody(FootnoteBody body) {
      if (!this.bDefer) {
         try {
            RtfFootnote rtfFootnote = (RtfFootnote)this.builderContext.getContainer(RtfFootnote.class, true, this);
            rtfFootnote.startBody();
         } catch (IOException var3) {
            this.handleIOTrouble(var3);
         } catch (Exception var4) {
            log.error("startFootnoteBody: " + var4.getMessage());
            throw new RuntimeException("Exception: " + var4);
         }

      }
   }

   public void endFootnoteBody(FootnoteBody body) {
      if (!this.bDefer) {
         try {
            RtfFootnote rtfFootnote = (RtfFootnote)this.builderContext.getContainer(RtfFootnote.class, true, this);
            rtfFootnote.endBody();
         } catch (IOException var3) {
            this.handleIOTrouble(var3);
         } catch (Exception var4) {
            log.error("endFootnoteBody: " + var4.getMessage());
            throw new RuntimeException("Exception: " + var4);
         }

      }
   }

   public void leader(Leader l) {
      if (!this.bDefer) {
         try {
            this.percentManager.setDimension(l);
            RtfAttributes rtfAttr = TextAttributesConverter.convertLeaderAttributes(l, this.percentManager);
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addLeader(rtfAttr);
         } catch (Exception var5) {
            log.error("startLeader: " + var5.getMessage());
            throw new RuntimeException(var5.getMessage());
         }
      }
   }

   public void text(FOText text, char[] data, int start, int length) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            RtfAttributes rtfAttr = TextAttributesConverter.convertCharacterAttributes(text);
            textrun.pushInlineAttributes(rtfAttr);
            textrun.addString(new String(data, start, length - start));
            textrun.popInlineAttributes();
         } catch (IOException var8) {
            this.handleIOTrouble(var8);
         } catch (Exception var9) {
            log.error("characters:" + var9.getMessage());
            throw new RuntimeException(var9.getMessage());
         }

      }
   }

   public void startPageNumber(PageNumber pagenum) {
      if (!this.bDefer) {
         try {
            RtfAttributes rtfAttr = TextAttributesConverter.convertCharacterAttributes(pagenum);
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addPageNumber(rtfAttr);
         } catch (IOException var5) {
            this.handleIOTrouble(var5);
         } catch (Exception var6) {
            log.error("startPageNumber: " + var6.getMessage());
            throw new RuntimeException(var6.getMessage());
         }

      }
   }

   public void endPageNumber(PageNumber pagenum) {
      if (!this.bDefer) {
         ;
      }
   }

   public void startPageNumberCitation(PageNumberCitation l) {
      if (!this.bDefer) {
         try {
            IRtfTextrunContainer container = (IRtfTextrunContainer)this.builderContext.getContainer(IRtfTextrunContainer.class, true, this);
            RtfTextrun textrun = container.getTextrun();
            textrun.addPageNumberCitation(l.getRefId());
         } catch (Exception var4) {
            log.error("startPageNumberCitation: " + var4.getMessage());
            throw new RuntimeException(var4.getMessage());
         }
      }
   }

   private void prepareTable(Table tab) {
      this.percentManager.setDimension(tab);
      int tabDiff = tab.getCommonBorderPaddingBackground().getBorderStartWidth(false) / 2 + tab.getCommonBorderPaddingBackground().getBorderEndWidth(false);
      if (!(tab.getInlineProgressionDimension().getMaximum((PercentBaseContext)null).getLength() instanceof EnumLength)) {
         this.percentManager.setDimension(tab, tab.getInlineProgressionDimension().getMaximum((PercentBaseContext)null).getLength().getValue(this.percentManager) - tabDiff);
      } else {
         this.percentManager.setDimension(tab, this.percentManager.getBaseLength(5, tab) - tabDiff);
      }

      ColumnSetup columnSetup = new ColumnSetup(tab);
      float tableWidth = (float)this.percentManager.getBaseLength(5, tab);
      float tableUnit = columnSetup.computeTableUnit(this.percentManager, Math.round(tableWidth));
      this.percentManager.setTableUnit(tab, Math.round(tableUnit));
   }

   private void invokeDeferredEvent(FONode foNode, boolean bStart) {
      if (foNode instanceof PageSequence) {
         if (bStart) {
            this.startPageSequence((PageSequence)foNode);
         } else {
            this.endPageSequence((PageSequence)foNode);
         }
      } else if (foNode instanceof Flow) {
         if (bStart) {
            this.startFlow((Flow)foNode);
         } else {
            this.endFlow((Flow)foNode);
         }
      } else if (foNode instanceof StaticContent) {
         if (bStart) {
            this.startStatic();
         } else {
            this.endStatic();
         }
      } else if (foNode instanceof ExternalGraphic) {
         if (bStart) {
            this.image((ExternalGraphic)foNode);
         }
      } else if (foNode instanceof InstreamForeignObject) {
         if (bStart) {
            this.foreignObject((InstreamForeignObject)foNode);
         }
      } else if (foNode instanceof Block) {
         if (bStart) {
            this.startBlock((Block)foNode);
         } else {
            this.endBlock((Block)foNode);
         }
      } else if (foNode instanceof BlockContainer) {
         if (bStart) {
            this.startBlockContainer((BlockContainer)foNode);
         } else {
            this.endBlockContainer((BlockContainer)foNode);
         }
      } else if (foNode instanceof BasicLink) {
         if (bStart) {
            this.startLink((BasicLink)foNode);
         } else {
            this.endLink();
         }
      } else if (foNode instanceof Inline) {
         if (bStart) {
            this.startInline((Inline)foNode);
         } else {
            this.endInline((Inline)foNode);
         }
      } else if (foNode instanceof FOText) {
         if (bStart) {
            FOText text = (FOText)foNode;
            this.text(text, text.getCharArray(), 0, text.length());
         }
      } else if (foNode instanceof Character) {
         if (bStart) {
            Character c = (Character)foNode;
            this.character(c);
         }
      } else if (foNode instanceof PageNumber) {
         if (bStart) {
            this.startPageNumber((PageNumber)foNode);
         } else {
            this.endPageNumber((PageNumber)foNode);
         }
      } else if (foNode instanceof Footnote) {
         if (bStart) {
            this.startFootnote((Footnote)foNode);
         } else {
            this.endFootnote((Footnote)foNode);
         }
      } else if (foNode instanceof FootnoteBody) {
         if (bStart) {
            this.startFootnoteBody((FootnoteBody)foNode);
         } else {
            this.endFootnoteBody((FootnoteBody)foNode);
         }
      } else if (foNode instanceof ListBlock) {
         if (bStart) {
            this.startList((ListBlock)foNode);
         } else {
            this.endList((ListBlock)foNode);
         }
      } else if (foNode instanceof ListItemBody) {
         if (bStart) {
            this.startListBody();
         } else {
            this.endListBody();
         }
      } else if (foNode instanceof ListItem) {
         if (bStart) {
            this.startListItem((ListItem)foNode);
         } else {
            this.endListItem((ListItem)foNode);
         }
      } else if (foNode instanceof ListItemLabel) {
         if (bStart) {
            this.startListLabel();
         } else {
            this.endListLabel();
         }
      } else if (foNode instanceof Table) {
         if (bStart) {
            this.startTable((Table)foNode);
         } else {
            this.endTable((Table)foNode);
         }
      } else if (foNode instanceof TableHeader) {
         if (bStart) {
            this.startHeader((TableHeader)foNode);
         } else {
            this.endHeader((TableHeader)foNode);
         }
      } else if (foNode instanceof TableFooter) {
         if (bStart) {
            this.startFooter((TableFooter)foNode);
         } else {
            this.endFooter((TableFooter)foNode);
         }
      } else if (foNode instanceof TableBody) {
         if (bStart) {
            this.startBody((TableBody)foNode);
         } else {
            this.endBody((TableBody)foNode);
         }
      } else if (foNode instanceof TableColumn) {
         if (bStart) {
            this.startColumn((TableColumn)foNode);
         } else {
            this.endColumn((TableColumn)foNode);
         }
      } else if (foNode instanceof TableRow) {
         if (bStart) {
            this.startRow((TableRow)foNode);
         } else {
            this.endRow((TableRow)foNode);
         }
      } else if (foNode instanceof TableCell) {
         if (bStart) {
            this.startCell((TableCell)foNode);
         } else {
            this.endCell((TableCell)foNode);
         }
      } else if (foNode instanceof Leader) {
         if (bStart) {
            this.leader((Leader)foNode);
         }
      } else if (foNode instanceof PageNumberCitation) {
         if (bStart) {
            this.startPageNumberCitation((PageNumberCitation)foNode);
         } else {
            this.endPageNumberCitation((PageNumberCitation)foNode);
         }
      } else {
         RTFEventProducer eventProducer = RTFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
         eventProducer.ignoredDeferredEvent(this, foNode, bStart, foNode.getLocator());
      }

   }

   private void recurseFONode(FONode foNode) {
      this.invokeDeferredEvent(foNode, true);
      if (foNode instanceof PageSequence) {
         PageSequence pageSequence = (PageSequence)foNode;
         Region regionBefore = this.pagemaster.getRegion(57);
         if (regionBefore != null) {
            FONode staticBefore = (FONode)pageSequence.getFlowMap().get(regionBefore.getRegionName());
            if (staticBefore != null) {
               this.recurseFONode(staticBefore);
            }
         }

         Region regionAfter = this.pagemaster.getRegion(56);
         if (regionAfter != null) {
            FONode staticAfter = (FONode)pageSequence.getFlowMap().get(regionAfter.getRegionName());
            if (staticAfter != null) {
               this.recurseFONode(staticAfter);
            }
         }

         this.recurseFONode(pageSequence.getMainFlow());
      } else if (foNode instanceof Table) {
         Table table = (Table)foNode;
         if (table.getColumns() != null) {
            this.prepareTable(table);
            Iterator it = table.getColumns().iterator();

            while(it.hasNext()) {
               this.recurseFONode((FONode)it.next());
            }
         } else {
            RTFEventProducer eventProducer = RTFEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.explicitTableColumnsRequired(this, table.getLocator());
         }

         if (table.getTableHeader() != null) {
            this.recurseFONode(table.getTableHeader());
         }

         if (table.getTableFooter() != null) {
            this.recurseFONode(table.getTableFooter());
         }

         if (foNode.getChildNodes() != null) {
            Iterator it = foNode.getChildNodes();

            while(it.hasNext()) {
               this.recurseFONode((FONode)it.next());
            }
         }
      } else if (foNode instanceof ListItem) {
         ListItem item = (ListItem)foNode;
         this.recurseFONode(item.getLabel());
         this.recurseFONode(item.getBody());
      } else if (foNode instanceof Footnote) {
         Footnote fn = (Footnote)foNode;
         this.recurseFONode(fn.getFootnoteCitation());
         this.recurseFONode(fn.getFootnoteBody());
      } else {
         FONode fn;
         if (foNode.getChildNodes() != null) {
            for(Iterator it = foNode.getChildNodes(); it.hasNext(); this.recurseFONode(fn)) {
               fn = (FONode)it.next();
               if (log.isTraceEnabled()) {
                  log.trace("  ChildNode for " + fn + " (" + fn.getName() + ")");
               }
            }
         }
      }

      this.invokeDeferredEvent(foNode, false);
   }

   static {
      log = LogFactory.getLog(RTFHandler.class);
      FLAVORS = new ImageFlavor[]{ImageFlavor.RAW_EMF, ImageFlavor.RAW_PNG, ImageFlavor.RAW_JPEG};
   }
}
