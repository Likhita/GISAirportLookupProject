import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.data.feat.BaseDataID;
import com.esri.mo2.data.feat.BaseFeature;
import com.esri.mo2.data.feat.BaseFeatureClass;
import com.esri.mo2.data.feat.BaseField;
import com.esri.mo2.data.feat.BaseFields;
import com.esri.mo2.data.feat.Feature;
import com.esri.mo2.data.feat.Field;
import com.esri.mo2.data.feat.MapDataset;
import com.esri.mo2.map.dpy.BaseFeatureLayer;
import com.esri.mo2.map.draw.BaseSimpleRenderer;
import com.esri.mo2.map.draw.TrueTypeMarkerSymbol;
import com.esri.mo2.ui.bean.Map;


public class AddXYtheme extends JDialog {

	private static final long serialVersionUID = 1L;
	Map map;
	Vector<Object> s2 = new Vector<Object>();
	JFileChooser jfc = new JFileChooser();
	BasePointsArray bpa = new BasePointsArray();
	AddXYtheme() throws IOException {
		setBounds(50,50,520,430);
        jfc.showOpenDialog(this);
        try {
        	File file  = jfc.getSelectedFile();
        	FileReader fred = new FileReader(file);
        	BufferedReader in = new BufferedReader(fred);
        	String s; 
        	double x,y;
        	int n = 0;
        	while ((s = in.readLine()) != null) {
        		StringTokenizer st = new StringTokenizer(s,",");
                x = Double.parseDouble(st.nextToken());
                y = Double.parseDouble(st.nextToken());
                bpa.insertPoint(n++,new com.esri.mo2.cs.geom.Point(x,y));
                s2.addElement(st.nextToken());
          }
        } catch (IOException e){}
        XYfeatureLayer xyfl = new XYfeatureLayer(bpa,map,s2);
        xyfl.setVisible(true);
        map = LocatorApplication.map;
        map.getLayerset().addLayer(xyfl);
        map.redraw();
  }
	
	AddXYtheme(File fileName) throws NumberFormatException {
		setBounds(50,50,520,430);
		FileReader fred;
		try {
			fred = new FileReader(fileName);
			BufferedReader in = new BufferedReader(fred);
			String s; 
			double x,y;
			int n = 0;
			while ((s = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(s,",");
			    x = Double.parseDouble(st.nextToken());
			    y = Double.parseDouble(st.nextToken());
			    bpa.insertPoint(n++,new com.esri.mo2.cs.geom.Point(x,y));
			    s2.addElement(st.nextToken());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	XYfeatureLayer xyfl2 = new XYfeatureLayer(bpa,map,s2);
    	xyfl2.setVisible(true);
    	map = LocatorApplication.map;
    	map.getLayerset().addLayer(xyfl2);
    	map.redraw();
	}

  public void setMap(com.esri.mo2.ui.bean.Map map1){
          map = map1;
  }
}

class XYfeatureLayer extends BaseFeatureLayer {
  BaseFields fields;
  private java.util.Vector<Object> featureVector;
  public XYfeatureLayer(BasePointsArray bpa,Map map,Vector<Object> s2) {
        createFeaturesAndFields(bpa,map,s2);
        BaseFeatureClass bfc = getFeatureClass("AirportLocations",bpa);
        setFeatureClass(bfc);
        BaseSimpleRenderer srd = new BaseSimpleRenderer();
        //TrueTypeMarkerSymbol ttm = new TrueTypeMarkerSymbol();
        com.esri.mo2.map.draw.RasterMarkerSymbol rms =new com.esri.mo2.map.draw.RasterMarkerSymbol();
        rms.setSizeX(30);
        System.out.println("raster time");
        rms.setSizeY(30);
        rms.setImageString("GISPictures\\plane2.gif");
        //ttm.setFont(new Font("ESRI Transportation & Civic",Font.PLAIN,90));
        //ttm.setColor(new Color(130,130,130));
        //ttm.setCharacter("98");
        srd.setSymbol(rms);
        setRenderer(srd);
        // without setting layer capabilities, the points will not
        // display (but the toc entry will still appear)
        XYLayerCapabilities lc = new XYLayerCapabilities();
        setCapabilities(lc);
  }
  private void createFeaturesAndFields(BasePointsArray bpa,Map map,Vector<Object> s2) {
        featureVector = new java.util.Vector<Object>();
        fields = new BaseFields();
        createDbfFields();
        for(int i=0;i<bpa.size();i++) {
          BaseFeature feature = new BaseFeature();  //feature is a row
          feature.setFields(fields);
          com.esri.mo2.cs.geom.Point p = new
            com.esri.mo2.cs.geom.Point(bpa.getPoint(i));
          feature.setValue(0,p);
          feature.setValue(1,new Integer(0));  // point data
          feature.setValue(2,(String)s2.elementAt(i));
          feature.setDataID(new BaseDataID("MyPoints",i));
          featureVector.addElement(feature);
        }
  }
  private void createDbfFields() {
        fields.addField(new BaseField("#SHAPE#",Field.ESRI_SHAPE,0,0));
        fields.addField(new BaseField("ID",java.sql.Types.INTEGER,9,0));
        fields.addField(new BaseField("Name",java.sql.Types.VARCHAR,16,0));
  }
  public BaseFeatureClass getFeatureClass(String name,BasePointsArray bpa){
    com.esri.mo2.map.mem.MemoryFeatureClass featClass = null;
    try {
          featClass = new com.esri.mo2.map.mem.MemoryFeatureClass(MapDataset.POINT,
            fields);
    } catch (IllegalArgumentException iae) {}
    featClass.setName(name);
    for (int i=0;i<bpa.size();i++) {
          featClass.addFeature((Feature) featureVector.elementAt(i));
    }
    return featClass;
  }
  
  private final class XYLayerCapabilities extends com.esri.mo2.map.dpy.LayerCapabilities {
	  
	  XYLayerCapabilities() {
		  for (int i=0;i<this.size(); i++) {
			  setAvailable(this.getCapabilityName(i),true);
			  setEnablingAllowed(this.getCapabilityName(i),true);
			  getCapability(i).setEnabled(true);
          }
	  }
  }
}
