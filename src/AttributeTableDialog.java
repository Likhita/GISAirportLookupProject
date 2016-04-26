import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.esri.mo2.data.feat.BaseQueryFilter;
import com.esri.mo2.data.feat.Feature;
import com.esri.mo2.data.feat.FeatureClass;
import com.esri.mo2.data.feat.Fields;
import com.esri.mo2.map.dpy.FeatureLayer;


public class AttributeTableDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	JPanel panel1 = new JPanel();
	com.esri.mo2.map.dpy.Layer layer = LocatorApplication.layer4;
	JTable jtable = new JTable(new MyTableModel());
	JScrollPane scroll = new JScrollPane(jtable);
	public AttributeTableDialog() throws IOException {
		setBounds(70,70,450,350);
		setTitle("Attribute Table");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		scroll.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// next line necessary for horiz scrollbar to work
		jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumn tc = null;
		int numCols = jtable.getColumnCount();
		//jtable.setPreferredScrollableViewportSize(
		//new java.awt.Dimension(440,340));
		for (int j=0;j<numCols;j++) {
			tc = jtable.getColumnModel().getColumn(j);
			tc.setMinWidth(50);
		}
		getContentPane().add(scroll,BorderLayout.CENTER);
	}
}

class MyTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	com.esri.mo2.map.dpy.Layer layer = LocatorApplication.layer4;
	ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	int row = 0, col = 0;
	BaseQueryFilter qfilter = new BaseQueryFilter();
	FeatureLayer flayer = (FeatureLayer) layer;
	FeatureClass fclass = flayer.getFeatureClass();
	String columnNames [] = fclass.getFields().getNames();
	Fields fields = fclass.getFields();

	MyTableModel() {
		qfilter.setSubFields(fields);
		com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
		while (cursor.hasMore()) {
			ArrayList<String> inner = new ArrayList<String>();
			Feature f = (com.esri.mo2.data.feat.Feature)cursor.next();
			inner.add(0,String.valueOf(row));
			for (int j=1;j<fields.getNumFields();j++) {
				inner.add(f.getValue(j).toString());
			}
			data.add(inner);
			row++;
		}
	}

	public int getColumnCount() {
		return fclass.getFields().getNumFields();
	}
	public int getRowCount() {
		return data.size();
	}
	public String getColumnName(int colIndx) {
		return columnNames[colIndx];
	}
	public Object getValueAt(int row, int col) {
		ArrayList<String> temp = new ArrayList<String>();
		temp =data.get(row);
		return temp.get(col);
	}
}