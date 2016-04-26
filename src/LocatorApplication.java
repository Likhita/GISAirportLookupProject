import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.event.*;
import java.awt.*;
import com.esri.mo2.ui.bean.*; 
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.ren.LayerProperties;
import com.esri.mo2.cs.geom.Envelope;
import com.esri.mo2.data.feat.*; 
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.map.draw.*;
import com.esri.mo2.ui.bean.Tool;
import com.esri.mo2.ui.dlg.AboutBox;

import java.util.ArrayList;

public class LocatorApplication extends JFrame {
	private static final long serialVersionUID = 1L;
	static Map map = new Map();
	static boolean fullMap = true;
	static Envelope envelope;
	static com.esri.mo2.map.dpy.Layer layer4;
	static boolean helpToolOn;
	Legend legend;
	Legend legend2;
	Layer layer = new Layer();
	Layer layer2 = new Layer();
	Layer airportLayer= new Layer();
	Layer layer3 = null;
	com.esri.mo2.map.dpy.Layer activeLayer;
	int activeLayerIndex;
	JMenuBar mbar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu theme = new JMenu("Theme");
	JMenu layercontrol = new JMenu("LayerControl");
	JMenu help = new JMenu("Help");
	JMenuItem attribitem = new JMenuItem("open attribute table", new ImageIcon("GISPictures\\tableview.gif"));
	JMenuItem createlayeritem  = new JMenuItem("create layer from selection", new ImageIcon("GISPictures\\Icon0915b.jpg"));
	static JMenuItem promoteitem = new JMenuItem("promote selected layer", new ImageIcon("GISPictures\\promote1.gif"));
	JMenuItem demoteitem = new JMenuItem("demote selected layer", new ImageIcon("GISPictures\\demote1.gif"));
	JMenuItem printitem = new JMenuItem("print",new ImageIcon("GISPictures\\print.gif"));
	JMenuItem addlyritem = new JMenuItem("add layer",new ImageIcon("GISPictures\\addtheme.gif"));
	JMenuItem remlyritem = new JMenuItem("remove layer",new ImageIcon("GISPictures\\delete.gif"));
	JMenuItem propsitem = new JMenuItem("Legend Editor",new ImageIcon("GISPictures\\properties.gif"));
	JMenu helptopics = new JMenu("Help Topics");
	JMenuItem tocitem = new JMenuItem("Table of Contents",new ImageIcon("GISPictures\\helptopic.gif"));
	JMenuItem legenditem = new JMenuItem("Legend Editor",new ImageIcon("GISPictures\\helptopic.gif"));
	JMenuItem layercontrolitem = new JMenuItem("Layer Control",new ImageIcon("GISPictures\\helptopic.gif"));
	JMenuItem helptoolitem = new JMenuItem("Help Tool",new ImageIcon("GISPictures\\help2.gif"));
	JMenuItem contactitem = new JMenuItem("Contact us");
	JMenuItem aboutitem = new JMenuItem("About MOJO...");
	Toc toc = new Toc();
	String s1 = "ShapeFile\\CountyBoundaryLL.shp";
	//String s2 = "C:\\ESRI\\MOJ20\\Samples\\Data\\USA\\capitals.shp";
	String datapathname = "";
	String legendname = "";
	ZoomPanToolBar zptb = new ZoomPanToolBar();
	static SelectionToolBar stb = new SelectionToolBar();
	JToolBar jtb = new JToolBar();
	AddXYtheme addXYthemeDefault;
	ComponentListener complistener;
	JLabel statusLabel = new JLabel("status bar    LOC");
	java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
	JPanel myjp = new JPanel();
	JPanel myjp2 = new JPanel();
	JButton prtjb = new JButton(new ImageIcon("GISPictures\\print.gif"));
	JButton addlyrjb = new JButton(new ImageIcon("GISPictures\\addtheme.gif"));
	JButton ptrjb = new JButton(new ImageIcon("GISPictures\\pointer.gif"));
	JButton hotjb = new JButton(new ImageIcon("GISPictures\\hotlink.gif"));
	JButton XYjb = new JButton("Add XY");
	JButton helpjb = new JButton(new ImageIcon("GISPictures\\help2.gif"));
	Arrow arrow = new Arrow();
	static HelpTool helpTool = new HelpTool();
	ActionListener lis;
	ActionListener layerlis;
	ActionListener layercontrollis;
	ActionListener helplis;
	TocAdapter mytocadapter;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image bolt = tk.getImage("GISPictures\\hotlink_32x32-32.gif");
	java.awt.Cursor boltCursor = tk.createCustomCursor(bolt,new java.awt.Point(11,26),"bolt");
	MyPickAdapter picklis = new MyPickAdapter();
	Identify hotlink = new Identify(); //the Identify class implements a PickListener,
	static String airportName = null;
	private ArrayList<String> helpText = new ArrayList<String>(3);

	class MyPickAdapter implements PickListener {
		public void beginPick(PickEvent pe){};
		// this fires even when you click outside the states layer
		public void endPick(PickEvent pe){}
		public void foundData(PickEvent pe){
			com.esri.mo2.data.feat.Cursor c = pe.getCursor();
			Feature f = null;
			Fields fields = null;
			if (c != null){
				f = (Feature)c.next();
				fields = f.getFields();
				String aname = fields.getField(2).getName(); 
				System.out.println("Column name :"+aname);
				airportName = (String)f.getValue(2);
				System.out.println("\n Airport name:"+airportName);
				try {
					//if(!airportName.equals("San Diego County")){
						HotPickDialog hotpick = new HotPickDialog();
						hotpick.setVisible(true);
						System.out.println("here");
					//}
				} catch(Exception e){}
			}
		}
	};

	public LocatorApplication() {
		super("Airport Locator Application");
		helpToolOn = false;
		this.setBounds(50,50,900,650);
		zptb.setMap(map);
		stb.setMap(map);
		setJMenuBar(mbar);
		ActionListener lisZoom = new ActionListener() {
			public void actionPerformed(ActionEvent ae){
				fullMap = false;
			}}; 
			ActionListener lisFullExt = new ActionListener() {
				public void actionPerformed(ActionEvent ae){
					fullMap = true;
				}};
				MouseAdapter mlLisZoom = new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
							try {
								HelpDialog helpdialog = new HelpDialog((String)helpText.get(4));
								helpdialog.setVisible(true);
							} catch(IOException e){}
						}
					}
				};
				MouseAdapter mlLisZoomActive = new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
							try {
								HelpDialog helpdialog = new HelpDialog((String)helpText.get(5));
								helpdialog.setVisible(true);
							} catch(IOException e){}
						}
					}
				};

				JButton zoomInButton = (JButton)zptb.getActionComponent("ZoomIn");
				JButton zoomFullExtentButton =(JButton)zptb.getActionComponent("ZoomToFullExtent");
				JButton zoomToSelectedLayerButton =(JButton)zptb.getActionComponent("ZoomToSelectedLayer");
				zoomInButton.addActionListener(lisZoom);
				zoomInButton.addMouseListener(mlLisZoom);
				zoomFullExtentButton.addActionListener(lisFullExt);
				zoomToSelectedLayerButton.addActionListener(lisZoom);
				zoomToSelectedLayerButton.addMouseListener(mlLisZoomActive);
				complistener = new ComponentAdapter () {
					public void componentResized(ComponentEvent ce) {
						if(fullMap) {
							map.setExtent(envelope);
							map.zoom(1.0);    //scale is scale factor in pixels
							map.redraw();
						}
					}
				};
				addComponentListener(complistener);
				lis = new ActionListener() {
					public void actionPerformed(ActionEvent ae){
						Object source = ae.getSource();
						if (source == prtjb || source instanceof JMenuItem ) {
							com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
							mapPrint.setMap(map);
							mapPrint.doPrint();
						}
						else if (source == ptrjb) {
							map.setSelectedTool(arrow);
						}
						else if (source == hotjb) {
							hotlink.setCursor(boltCursor);
							map.setSelectedTool(hotlink);
						}
						else if (source == XYjb) {
							try {
								AddXYtheme addXYtheme = new AddXYtheme();
								addXYtheme.setMap(map);
								addXYtheme.setVisible(false);
								map.redraw();
							} catch (IOException e){}
						}
						else if (source == helpjb) {
							helpToolOn = true;
							map.setSelectedTool(helpTool);
						}
						else {
							try {
								AddLayerDialog aldlg = new AddLayerDialog();
								aldlg.setMap(map);
								aldlg.setVisible(true);
							} catch(IOException e){}
						}
					}};

					layercontrollis = new ActionListener() {
						public void actionPerformed(ActionEvent ae){
							String source = ae.getActionCommand();
							System.out.println(activeLayerIndex+" active index");
							if (source == "promote selected layer")
								map.getLayerset().moveLayer(activeLayerIndex,++activeLayerIndex);
							else
								map.getLayerset().moveLayer(activeLayerIndex,--activeLayerIndex);
							enableDisableButtons();
							map.redraw();
						}};

						layerlis = new ActionListener() {
							public void actionPerformed(ActionEvent ae){
								Object source = ae.getSource();
								if (source instanceof JMenuItem) {
									String arg = ae.getActionCommand();
									if(arg == "add layer") {
										try {
											AddLayerDialog aldlg = new AddLayerDialog();
											aldlg.setMap(map);
											aldlg.setVisible(true);
										} catch(IOException e){}
									}
									else if(arg == "remove layer") {
										try {
											com.esri.mo2.map.dpy.Layer dpylayer =
													legend.getLayer();
											map.getLayerset().removeLayer(dpylayer);
											map.redraw();
											remlyritem.setEnabled(false);
											propsitem.setEnabled(false);
											attribitem.setEnabled(false);
											promoteitem.setEnabled(false);
											demoteitem.setEnabled(false);
											stb.setSelectedLayer(null);
											zptb.setSelectedLayer(null);
											stb.setSelectedLayers(null);
										} catch(Exception e) {}
									}
									else if(arg == "Legend Editor") {
										LayerProperties lp = new LayerProperties();
										lp.setLegend(legend);
										lp.setSelectedTabIndex(0);
										lp.setVisible(true);
									}
									else if (arg == "open attribute table") {
										try {
											layer4 = legend.getLayer();
											AttributeTableDialog attrtab = new AttributeTableDialog();
											attrtab.setVisible(true);
										} catch(IOException ioe){}
									}
									else if (arg=="create layer from selection") {
										BaseSimpleRenderer sbr = new BaseSimpleRenderer();
										SimplePolygonSymbol sps = new SimplePolygonSymbol();
										sps.setPaint(AoFillStyle.getPaint(
												AoFillStyle.SOLID_FILL,new java.awt.Color(255,255,0)));
										sps.setBoundary(true);
										layer4 = legend.getLayer();
										FeatureLayer flayer2 = (FeatureLayer)layer4;
										// select, e.g., Montana and then click the
										// create layer menuitem; next line verifies a selection was made
										System.out.println("has selected" + flayer2.hasSelection());
										//next line creates the 'set' of selections
										if (flayer2.hasSelection()) {
											SelectionSet selectset = flayer2.getSelectionSet();
											// next line makes a new feature layer of the selections
											FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
											sbr.setLayer(selectedlayer);
											sbr.setSymbol(sps);
											selectedlayer.setRenderer(sbr);
											Layerset layerset = map.getLayerset();
											// next line places a new visible layer, e.g. Montana, on the map
											layerset.addLayer(selectedlayer);
											//selectedlayer.setVisible(true);
											if(stb.getSelectedLayers() != null)
												promoteitem.setEnabled(true);
											try {
												legend2 = toc.findLegend(selectedlayer);
											} catch (Exception e) {}

											CreateShapeFileDialog csd = new CreateShapeFileDialog(selectedlayer);
											csd.setVisible(true);
											Flash flash = new Flash(legend2);
											flash.start();
											map.redraw(); // necessary to see color immediately

										}
									}
								}
							}};
							toc.setMap(map);
							mytocadapter = new TocAdapter() {
								public void click(TocEvent e) {
									legend = e.getLegend();
									activeLayer = legend.getLayer();
									stb.setSelectedLayer(activeLayer);
									zptb.setSelectedLayer(activeLayer);
									// get active layer index for promote and demote
									activeLayerIndex = map.getLayerset().indexOf(activeLayer);
									// layer indices are in order added, not toc order.
									com.esri.mo2.map.dpy.Layer[] layers = {activeLayer};
									hotlink.setSelectedLayers(layers);// replaces setToc from MOJ10
									remlyritem.setEnabled(true);
									propsitem.setEnabled(true);
									attribitem.setEnabled(true);
									enableDisableButtons();
								}
							};
							map.addMouseMotionListener(new MouseMotionAdapter() {
								public void mouseMoved(MouseEvent me) {
									com.esri.mo2.cs.geom.Point worldPoint = null;
									if (map.getLayerCount() > 0) {
										worldPoint = map.transformPixelToWorld(me.getX(),me.getY());
										String s = "X:"+df.format(worldPoint.getX())+" "+
												"Y:"+df.format(worldPoint.getY());
										statusLabel.setText(s);
									}
									else
										statusLabel.setText("X:0.000 Y:0.000");
								}
							});

							helplis = new ActionListener(){
								public void actionPerformed(ActionEvent ae){
									Object source = ae.getSource();
									if (source instanceof JMenuItem) {
										String arg = ae.getActionCommand();
										if(arg == "About MOJO...") {
											AboutBox aboutbox = new AboutBox();
											aboutbox.setProductName("MOJO");
											aboutbox.setProductVersion("2.0");
											aboutbox.setVisible(true);
										}
										else if(arg == "Contact us") {
											try {
												String s = "\n\n\n\n Any enquiries should be addressed to \n\n\n           eckberg@rohan.sdsu.edu";
												HelpDialog helpdialog = new HelpDialog(s);
												helpdialog.setVisible(true);
											} catch(IOException e){}
										}
										else if(arg == "Table of Contents") {
											try {
												HelpDialog helpdialog = new HelpDialog((String)helpText.get(0));
												helpdialog.setVisible(true);
											} catch(IOException e){}
										}
										else if(arg == "Legend Editor") {
											try {
												HelpDialog helpdialog = new HelpDialog((String)helpText.get(1));
												helpdialog.setVisible(true);
											} catch(IOException e){}
										}
										else if(arg == "Layer Control") {
											try {
												HelpDialog helpdialog = new HelpDialog((String)helpText.get(2));
												helpdialog.setVisible(true);
											} catch(IOException e){}
										}
										else if(arg == "Help Tool") {
											try {
												HelpDialog helpdialog = new HelpDialog((String)helpText.get(3));
												helpdialog.setVisible(true);
											} catch(IOException e){}
										}
									}
								}};
								toc.addTocListener(mytocadapter);
								remlyritem.setEnabled(false); // assume no layer initially selected
								propsitem.setEnabled(false);
								attribitem.setEnabled(false);
								promoteitem.setEnabled(false);
								demoteitem.setEnabled(false);
								printitem.addActionListener(lis);
								addlyritem.addActionListener(layerlis);
								remlyritem.addActionListener(layerlis);
								propsitem.addActionListener(layerlis);
								attribitem.addActionListener(layerlis);
								createlayeritem.addActionListener(layerlis);
								promoteitem.addActionListener(layercontrollis);
								demoteitem.addActionListener(layercontrollis);
								tocitem.addActionListener(helplis);
								legenditem.addActionListener(helplis);
								layercontrolitem.addActionListener(helplis);
								helptoolitem.addActionListener(helplis);
								contactitem.addActionListener(helplis);
								aboutitem.addActionListener(helplis);
								file.add(addlyritem);
								file.add(printitem);
								file.add(remlyritem);
								file.add(propsitem);
								theme.add(attribitem);
								theme.add(createlayeritem);
								layercontrol.add(promoteitem);
								layercontrol.add(demoteitem);
								help.add(helptopics);
								helptopics.add(tocitem);
								helptopics.add(legenditem);
								helptopics.add(layercontrolitem);
								help.add(helptoolitem);
								help.add(contactitem);
								help.add(aboutitem);
								mbar.add(file);
								mbar.add(theme);
								mbar.add(layercontrol);
								mbar.add(help);
								prtjb.addActionListener(lis);
								prtjb.setToolTipText("print map");
								addlyrjb.addActionListener(lis);
								addlyrjb.setToolTipText("add layer");
								hotlink.addPickListener(picklis);
								hotlink.setPickWidth(10);
								hotjb.addActionListener(lis);
								hotjb.setToolTipText("hotlink tool--click somthing to maybe see a picture");
								ptrjb.addActionListener(lis);
								prtjb.setToolTipText("pointer");
								XYjb.addActionListener(lis);
								helpjb.addActionListener(lis);
								XYjb.setToolTipText("add a layer of points from a file");
								helpjb.setToolTipText("left click here, then right click on a tool to learn about that tool;"+" click arrow tool when done");
								jtb.add(prtjb);
								jtb.add(addlyrjb);
								jtb.add(ptrjb);
								jtb.add(hotjb);
								jtb.add(XYjb);
								jtb.add(helpjb);
								myjp.add(jtb);
								myjp.add(zptb);
								myjp.add(stb);
								myjp2.add(statusLabel);
								setuphelpText();
								getContentPane().add(map, BorderLayout.CENTER);
								getContentPane().add(myjp,BorderLayout.NORTH);
								getContentPane().add(myjp2,BorderLayout.SOUTH);
								addShapefileToMap(layer,s1);
								//addShapefileToMap(layer2,s2);
								addXYthemeDefault=new AddXYtheme(new File("ShapeFile\\data.csv"));
								addXYthemeDefault.setMap(map);
								addXYthemeDefault.setVisible(false);
								map.redraw();
								map.zoom(1.0);  
								getContentPane().add(toc, BorderLayout.WEST);
	}
	private void addShapefileToMap(Layer layer,String s) {
		String datapath = s; 
		layer.setDataset("0;"+datapath);
		map.add(layer);
	}

	private void enableDisableButtons() {
		int layerCount = map.getLayerset().getSize();
		if (layerCount < 2) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(false);
		}
		else if (activeLayerIndex == 0) {
			demoteitem.setEnabled(false);
			promoteitem.setEnabled(true);
		}
		else if (activeLayerIndex == layerCount - 1) {
			promoteitem.setEnabled(false);
			demoteitem.setEnabled(true);
		}
		else {
			promoteitem.setEnabled(true);
			demoteitem.setEnabled(true);
		}
	}

	public static void main(String[] args) {
		LocatorApplication qstart = new LocatorApplication();
		qstart.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Thanks, Quick Start exits");
				System.exit(0);
			}
		});
		qstart.setVisible(true);
		envelope = map.getExtent();
	}

	private void setuphelpText() {
		String s0 =
				"    The toc, or table of contents, is to the left of the map. \n" +
						"    Each entry is called a 'legend' and represents a map 'layer' or \n" +
						"    'theme'.  If you click on a legend, that layer is called the \n" +
						"    active layer, or selected layer.  Its display (rendering) properties \n" +
						"    can be controlled using the Legend Editor, and the legends can be \n" +
						"    reordered using Layer Control.  Both Legend Editor and Layer Control \n" +
						"    are separate Help Topics.  This line is e... x... t... e... n... t... e...d"  +
						"    to test the scrollpane.";
		helpText.add(s0);
		String s1 = "  The Legend Editor is a menu item found under the File menu. \n" +
				"    Given that a layer is selected by clicking on its legend in the table of \n" +
				"    contents, clicking on Legend Editor will open a window giving you choices \n" +
				"    about how to display that layer.  For example you can control the color \n" +
				"    used to display the layer on the map, or whether to use multiple colors ";
		helpText.add(s1);
		String s2 = "  Layer Control is a Menu on the menu bar.  If you have selected a \n"+
				" layer by clicking on a legend in the toc (table of contents) to the left of \n" +
				" the map, then the promote and demote tools will become usable.  Clicking on \n" +
				" promote will raise the selected legend one position higher in the toc, and \n" +
				" clicking on demote will lower that legend one position in the toc.";
		helpText.add(s2);
		String s3 = "    This tool will allow you to learn about certain other tools. \n" +
				"    You begin with a standard left mouse button click on the Help Tool itself.\n" +
				"    RIGHT click on another tool and a window may give you information about the \n" +
				"    intended usage of the tool.  Click on the arrow tool to stop using the \n" +
				"    help tool.";
		helpText.add(s3);
		String s4 = "If you click on the Zoom In tool, and then click on the map, you \n" +
				" will see a part of the map in greater detail.  You can zoom in multiple times.\n" +
				" You can also sketch a rectangular part of the map, and zoom to that.  You can\n" +
				" undo a Zoom In with a Zoom Out or with a Zoom to Full Extent";
		helpText.add(s4);
		String s5 = "You must have a selected layer to use the Zoom to Active Layer tool.\n" +
				"    If you then click on Zoom to Active Layer, you will be shown enough of \n" +
				"    the full map to see all of the features in the layer you select.  If you \n" +
				"    select a layer that shows where glaciers are, then you do not need to \n" +
				"    see Hawaii, or any southern states, so you will see Alaska, and northern \n" +
				"    mainland states.";
		helpText.add(s5);

	}
}

class Arrow extends Tool {
	private static final long serialVersionUID = 1L;

	public void mouseClicked(MouseEvent me){
	}
}

class Flash extends Thread {
	Legend legend;
	Flash(Legend legendin) {
		legend = legendin;
	}
	public void run() {
		for (int i=0;i<12;i++) {
			try {
				Thread.sleep(500);
				legend.toggleSelected();
			} catch (Exception e) {}
		}
	}
}