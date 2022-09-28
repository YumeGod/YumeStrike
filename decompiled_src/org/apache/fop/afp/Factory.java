package org.apache.fop.afp;

import java.io.OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.goca.GraphicsData;
import org.apache.fop.afp.ioca.ImageContent;
import org.apache.fop.afp.ioca.ImageRasterData;
import org.apache.fop.afp.ioca.ImageSegment;
import org.apache.fop.afp.ioca.ImageSizeParameter;
import org.apache.fop.afp.modca.ActiveEnvironmentGroup;
import org.apache.fop.afp.modca.ContainerDataDescriptor;
import org.apache.fop.afp.modca.Document;
import org.apache.fop.afp.modca.GraphicsDataDescriptor;
import org.apache.fop.afp.modca.GraphicsObject;
import org.apache.fop.afp.modca.IMImageObject;
import org.apache.fop.afp.modca.ImageDataDescriptor;
import org.apache.fop.afp.modca.ImageObject;
import org.apache.fop.afp.modca.IncludeObject;
import org.apache.fop.afp.modca.IncludePageSegment;
import org.apache.fop.afp.modca.InvokeMediumMap;
import org.apache.fop.afp.modca.MapCodedFont;
import org.apache.fop.afp.modca.MapContainerData;
import org.apache.fop.afp.modca.MapDataResource;
import org.apache.fop.afp.modca.ObjectAreaDescriptor;
import org.apache.fop.afp.modca.ObjectAreaPosition;
import org.apache.fop.afp.modca.ObjectContainer;
import org.apache.fop.afp.modca.ObjectEnvironmentGroup;
import org.apache.fop.afp.modca.Overlay;
import org.apache.fop.afp.modca.PageDescriptor;
import org.apache.fop.afp.modca.PageGroup;
import org.apache.fop.afp.modca.PageObject;
import org.apache.fop.afp.modca.PresentationEnvironmentControl;
import org.apache.fop.afp.modca.PresentationTextDescriptor;
import org.apache.fop.afp.modca.PresentationTextObject;
import org.apache.fop.afp.modca.ResourceEnvironmentGroup;
import org.apache.fop.afp.modca.ResourceGroup;
import org.apache.fop.afp.modca.ResourceObject;
import org.apache.fop.afp.modca.StreamedResourceGroup;
import org.apache.fop.afp.modca.TagLogicalElement;
import org.apache.fop.afp.util.StringUtils;

public class Factory {
   private static final Log log;
   private static final String OBJECT_ENVIRONMENT_GROUP_NAME_PREFIX = "OEG";
   private static final String ACTIVE_ENVIRONMENT_GROUP_NAME_PREFIX = "AEG";
   private static final String IMAGE_NAME_PREFIX = "IMG";
   private static final String GRAPHIC_NAME_PREFIX = "GRA";
   private static final String BARCODE_NAME_PREFIX = "BAR";
   private static final String OBJECT_CONTAINER_NAME_PREFIX = "OC";
   private static final String RESOURCE_NAME_PREFIX = "RES";
   private static final String RESOURCE_GROUP_NAME_PREFIX = "RG";
   private static final String PAGE_GROUP_NAME_PREFIX = "PGP";
   private static final String PAGE_NAME_PREFIX = "PGN";
   private static final String OVERLAY_NAME_PREFIX = "OVL";
   private static final String PRESENTATION_TEXT_NAME_PREFIX = "PT";
   private static final String DOCUMENT_NAME_PREFIX = "DOC";
   private static final String IM_IMAGE_NAME_PREFIX = "IMIMG";
   private static final String IMAGE_SEGMENT_NAME_PREFIX = "IS";
   private int pageGroupCount = 0;
   private int pageCount = 0;
   private int imageCount = 0;
   private int imImageCount = 0;
   private int imageSegmentCount = 0;
   private int graphicCount = 0;
   private int objectContainerCount = 0;
   private int resourceCount = 0;
   private int resourceGroupCount = 0;
   private int overlayCount = 0;
   private int textObjectCount = 0;
   private int activeEnvironmentGroupCount = 0;
   private int documentCount = 0;
   private int objectEnvironmentGroupCount = 0;

   public ImageObject createImageObject() {
      String name = "IMG" + StringUtils.lpad(String.valueOf(++this.imageCount), '0', 5);
      ImageObject imageObject = new ImageObject(this, name);
      return imageObject;
   }

   public IMImageObject createIMImageObject() {
      String name = "IMIMG" + StringUtils.lpad(String.valueOf(++this.imImageCount), '0', 3);
      IMImageObject imImageObject = new IMImageObject(name);
      return imImageObject;
   }

   public GraphicsObject createGraphicsObject() {
      String name = "GRA" + StringUtils.lpad(String.valueOf(++this.graphicCount), '0', 5);
      GraphicsObject graphicsObj = new GraphicsObject(this, name);
      return graphicsObj;
   }

   public ObjectContainer createObjectContainer() {
      String name = "OC" + StringUtils.lpad(String.valueOf(++this.objectContainerCount), '0', 6);
      return new ObjectContainer(this, name);
   }

   public ResourceObject createResource(String resourceName) {
      return new ResourceObject(resourceName);
   }

   public ResourceObject createResource() {
      String name = "RES" + StringUtils.lpad(String.valueOf(++this.resourceCount), '0', 5);
      return this.createResource(name);
   }

   public PageGroup createPageGroup(int tleSequence) {
      String name = "PGP" + StringUtils.lpad(String.valueOf(++this.pageGroupCount), '0', 5);
      return new PageGroup(this, name, tleSequence);
   }

   public ActiveEnvironmentGroup createActiveEnvironmentGroup(int width, int height, int widthRes, int heightRes) {
      String name = "AEG" + StringUtils.lpad(String.valueOf(++this.activeEnvironmentGroupCount), '0', 5);
      return new ActiveEnvironmentGroup(this, name, width, height, widthRes, heightRes);
   }

   public ResourceGroup createResourceGroup() {
      String name = "RG" + StringUtils.lpad(String.valueOf(++this.resourceGroupCount), '0', 6);
      return new ResourceGroup(name);
   }

   public StreamedResourceGroup createStreamedResourceGroup(OutputStream os) {
      String name = "RG" + StringUtils.lpad(String.valueOf(++this.resourceGroupCount), '0', 6);
      return new StreamedResourceGroup(name, os);
   }

   public PageObject createPage(int pageWidth, int pageHeight, int pageRotation, int pageWidthRes, int pageHeightRes) {
      String pageName = "PGN" + StringUtils.lpad(String.valueOf(++this.pageCount), '0', 5);
      return new PageObject(this, pageName, pageWidth, pageHeight, pageRotation, pageWidthRes, pageHeightRes);
   }

   public PresentationTextObject createPresentationTextObject() {
      String textObjectName = "PT" + StringUtils.lpad(String.valueOf(++this.textObjectCount), '0', 6);
      return new PresentationTextObject(textObjectName);
   }

   public Overlay createOverlay(int width, int height, int widthRes, int heightRes, int overlayRotation) {
      String overlayName = "OVL" + StringUtils.lpad(String.valueOf(++this.overlayCount), '0', 5);
      Overlay overlay = new Overlay(this, overlayName, width, height, overlayRotation, widthRes, heightRes);
      return overlay;
   }

   public Document createDocument() {
      String documentName = "DOC" + StringUtils.lpad(String.valueOf(++this.documentCount), '0', 5);
      Document document = new Document(this, documentName);
      return document;
   }

   public MapCodedFont createMapCodedFont() {
      MapCodedFont mapCodedFont = new MapCodedFont();
      return mapCodedFont;
   }

   public IncludePageSegment createIncludePageSegment(String name, int x, int y) {
      IncludePageSegment includePageSegment = new IncludePageSegment(name, x, y);
      return includePageSegment;
   }

   public IncludeObject createInclude(String name) {
      IncludeObject includeObject = new IncludeObject(name);
      return includeObject;
   }

   public TagLogicalElement createTagLogicalElement(String name, String value, int tleSequence) {
      TagLogicalElement tle = new TagLogicalElement(name, value, tleSequence);
      return tle;
   }

   public DataStream createDataStream(AFPPaintingState paintingState, OutputStream outputStream) {
      DataStream dataStream = new DataStream(this, paintingState, outputStream);
      return dataStream;
   }

   public PageDescriptor createPageDescriptor(int width, int height, int widthRes, int heightRes) {
      PageDescriptor pageDescriptor = new PageDescriptor(width, height, widthRes, heightRes);
      return pageDescriptor;
   }

   public ObjectEnvironmentGroup createObjectEnvironmentGroup() {
      String oegName = "OEG" + StringUtils.lpad(String.valueOf(++this.objectEnvironmentGroupCount), '0', 5);
      ObjectEnvironmentGroup objectEnvironmentGroup = new ObjectEnvironmentGroup(oegName);
      return objectEnvironmentGroup;
   }

   public GraphicsData createGraphicsData() {
      GraphicsData graphicsData = new GraphicsData();
      return graphicsData;
   }

   public ObjectAreaDescriptor createObjectAreaDescriptor(int width, int height, int widthRes, int heightRes) {
      ObjectAreaDescriptor objectAreaDescriptor = new ObjectAreaDescriptor(width, height, widthRes, heightRes);
      return objectAreaDescriptor;
   }

   public ObjectAreaPosition createObjectAreaPosition(int x, int y, int rotation) {
      ObjectAreaPosition objectAreaPosition = new ObjectAreaPosition(x, y, rotation);
      return objectAreaPosition;
   }

   public ImageDataDescriptor createImageDataDescriptor(int width, int height, int widthRes, int heightRes) {
      ImageDataDescriptor imageDataDescriptor = new ImageDataDescriptor(width, height, widthRes, heightRes);
      return imageDataDescriptor;
   }

   public GraphicsDataDescriptor createGraphicsDataDescriptor(int xlwind, int xrwind, int ybwind, int ytwind, int widthRes, int heightRes) {
      GraphicsDataDescriptor graphicsDataDescriptor = new GraphicsDataDescriptor(xlwind, xrwind, ybwind, ytwind, widthRes, heightRes);
      return graphicsDataDescriptor;
   }

   public ContainerDataDescriptor createContainerDataDescriptor(int dataWidth, int dataHeight, int widthRes, int heightRes) {
      ContainerDataDescriptor containerDataDescriptor = new ContainerDataDescriptor(dataWidth, dataHeight, widthRes, heightRes);
      return containerDataDescriptor;
   }

   public MapContainerData createMapContainerData(byte optionValue) {
      MapContainerData mapContainerData = new MapContainerData(optionValue);
      return mapContainerData;
   }

   public MapDataResource createMapDataResource() {
      MapDataResource mapDataResource = new MapDataResource();
      return mapDataResource;
   }

   public PresentationTextDescriptor createPresentationTextDataDescriptor(int width, int height, int widthRes, int heightRes) {
      PresentationTextDescriptor presentationTextDescriptor = new PresentationTextDescriptor(width, height, widthRes, heightRes);
      return presentationTextDescriptor;
   }

   public PresentationEnvironmentControl createPresentationEnvironmentControl() {
      PresentationEnvironmentControl presentationEnvironmentControl = new PresentationEnvironmentControl();
      return presentationEnvironmentControl;
   }

   public InvokeMediumMap createInvokeMediumMap(String name) {
      InvokeMediumMap invokeMediumMap = new InvokeMediumMap(name);
      return invokeMediumMap;
   }

   public ResourceEnvironmentGroup createResourceEnvironmentGroup() {
      ResourceEnvironmentGroup resourceEnvironmentGroup = new ResourceEnvironmentGroup();
      return resourceEnvironmentGroup;
   }

   public ImageSegment createImageSegment() {
      String name = "IS" + StringUtils.lpad(String.valueOf(++this.imageSegmentCount), '0', 2);
      ImageSegment imageSegment = new ImageSegment(this, name);
      return imageSegment;
   }

   public ImageContent createImageContent() {
      ImageContent imageContent = new ImageContent();
      return imageContent;
   }

   public ImageRasterData createImageRasterData(byte[] rasterData) {
      ImageRasterData imageRasterData = new ImageRasterData(rasterData);
      return imageRasterData;
   }

   public ImageSizeParameter createImageSizeParameter(int hsize, int vsize, int hresol, int vresol) {
      ImageSizeParameter imageSizeParameter = new ImageSizeParameter(hsize, vsize, hresol, vresol);
      return imageSizeParameter;
   }

   static {
      log = LogFactory.getLog(Factory.class);
   }
}
