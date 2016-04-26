import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


class HotPickDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	String airportName = LocatorApplication.airportName;
	JSlider imageSlider;
	JLabel caption;
	JLabel picture;
	String captionText;
	String imageName;
	JEditorPane textArea;
	JScrollPane contentScrollPane; 
	JPanel container = new JPanel();
	JPanel jpanel = new JPanel();
	JPanel jpanel1 = new JPanel();
	JPanel jpanel2 = new JPanel();
	static String[][] airportDetails={{"San Diego International Airport","Airport view","GISPictures\\SAN.gif","GISPictures\\SAN_map.gif","GISPictures\\SAN_map.gif"},
			{"McClellan-Palomar Airport","Airport images","GISPictures\\Palomar_view.gif","GISPictures\\Palomar_map.gif","GISPictures\\Palomar_map.gif"},
			{"Naval Air Station North Island","Airport images","GISPictures\\Naval_carriers.gif","GISPictures\\Naval_map.gif","GISPictures\\NAS_view.gif"},
			{"Naval Outlying Landing Field Imperial Beach","Airport images","GISPictures\\Imperial_logo.gif","GISPictures\\Imperial_logo.gif","GISPictures\\Imperial_logo.gif"},
			{"MCAS Camp Pendleton","Airport images","GISPictures\\Mcas_logo.gif","GISPictures\\Mcas_map.gif","GISPictures\\Mcas_map.gif"},
			{"MCAS Miramar","Airport images","GISPictures\\Miramar_view.gif","GISPictures\\Miramar_map.gif","GISPictures\\Miramar_map.gif"},
			{"Brown Field Municipal Airport","Airport images","GISPictures\\Brown_map.gif","GISPictures\\Brown_map.gif","GISPictures\\Brown_map.gif"},
			{"Gillespie Field Airport","Airport images","GISPictures\\Gill_map.gif","GISPictures\\Gill_map.gif","GISPictures\\Gill_map.gif"},
			{"Montgomery Field Airport","Airport images","GISPictures\\Montgo_ariel.gif","GISPictures\\Montgo_entrance.gif","GISPictures\\Montgo_map.gif"},
			{"Oceanside Municipal Airport","Airport images","GISPictures\\Oceanside_plane.gif","GISPictures\\Oceanside_plane.gif","GISPictures\\Oceanside_plane.gif"}};
	Hashtable<String,String> airportText= new Hashtable<String,String>();
	
	HotPickDialog() throws IOException {
		setTitle(airportName);
		setBounds(100,100,600,600);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		setLayout(new GridLayout(3,1,1,1));
		//initializeAirportText();
		textArea= new JEditorPane("text/html", "");
		textArea.setMargin(new Insets(5,5,5,5));
		//textArea.setSize(50, Integer.MAX_VALUE);
		textArea.setEditable(false);
		textArea.setText("");
		contentScrollPane = new JScrollPane(textArea);
		StringBuffer buffer = new StringBuffer();
		String strLine=null;
		imageSlider = new JSlider(JSlider.HORIZONTAL,0,2,0);
		imageSlider.setMajorTickSpacing(1);
		//imageSlider.setPaintTicks(true);
		//imageSlider.setPaintLabels(true);
		imageSlider.setPreferredSize(new Dimension(400, 100));
		caption= new JLabel("Airport Pictures");
		caption.setAlignmentX(Component.CENTER_ALIGNMENT);
		Event e = new Event();
		imageSlider.addChangeListener(e);
		for (int i = 0;i<airportDetails.length;i++)  {
			if (airportDetails[i][0].equals(airportName)) {
				captionText = airportDetails[i][1];
				imageName = airportDetails[i][2];
				break;
			}
		}
		
		for(int i=0;i<airportDetails.length;i++){
			FileInputStream inText;
			System.out.println(airportDetails[i][0]);
			System.out.println(airportName);
				//if (airportName.equalsIgnoreCase(airportDetails[i][0])) {
					inText = new FileInputStream("airport"+i+".txt");
					DataInputStream in = new DataInputStream(inText);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					while ((strLine = br.readLine()) != null)   {
						buffer.append(strLine);
						buffer.append("\n");
					}
					System.out.println("here");
					in.close();
					textArea.setText(buffer.toString());
				//}
		}
		//label is created to hold the image
		BufferedImage myPicture = ImageIO.read(new File(imageName));
        picture = new JLabel(new ImageIcon(myPicture));
        picture.setHorizontalAlignment(JLabel.CENTER);
        picture.setAlignmentX(Component.CENTER_ALIGNMENT);
        picture.setPreferredSize(new Dimension(200,200));
        picture.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),BorderFactory.createEmptyBorder(10,10,10,10)));
        
        //Panel is created, Slider and 'hour' label is added to the table
        JPanel slider= new JPanel();
        //jpanel1.add(new JLabel(captionText));
        slider.setLayout(new GridLayout(1,1,0,0));
        slider.add(picture,BorderLayout.CENTER);
       slider.add(jpanel1,BorderLayout.LINE_END);
        slider.add(caption);
        slider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        		airportName+" Images", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		JLabel label = new JLabel(airportName +":   ");
		jpanel.add(label);
		//getContentPane().add(jpanel,BorderLayout.LINE_START);
		getContentPane().add(picture,BorderLayout.NORTH);
		getContentPane().add(imageSlider,BorderLayout.CENTER);
		getContentPane().add(contentScrollPane,BorderLayout.SOUTH);
		//pack();
	}
	
//	void initializeAirportText(){
//		textArea= new JEditorPane("text/html", "");
//		textArea.setMargin(new Insets(5,5,5,5));
//		//textArea.setSize(50, Integer.MAX_VALUE);
//		textArea.setEditable(false);
//		textArea.setText("");
//		contentScrollPane = new JScrollPane(textArea);
//		StringBuffer buffer = new StringBuffer();
//		String strLine=null;
////		for(int i=1;i<airportDetails[i][0].length();i++){
////			airportText.put(airportDetails[i][0], "airport"+i+".txt");
////		}
//		
//	}
	
	class Event implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int index= imageSlider.getValue();
			for (int i = 0;i<airportDetails.length;i++)  {
				  if (airportDetails[i][0].equals(airportName)) {
				    imageName = airportDetails[i][2+index];
				    break;
				  }
			    }
			picture.setIcon(new ImageIcon(imageName));
		}
	}
}

