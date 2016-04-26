import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.esri.mo2.ui.bean.Tool;


class HelpDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	JTextArea helptextarea;
	public HelpDialog(String inputText) throws IOException {
		setBounds(70,70,460,250);
		setTitle("Help");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		helptextarea = new JTextArea(inputText,7,40);
		JScrollPane scrollpane = new JScrollPane(helptextarea);
		helptextarea.setEditable(false);
		getContentPane().add(scrollpane,"Center");
	}
}
class HelpTool extends Tool {

	private static final long serialVersionUID = 1L;
}