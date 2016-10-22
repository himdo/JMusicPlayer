package com.himdo.JMusic;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class options {

	
	public static void changeLog(){	
		
		UIManager.put("OptionPane.minimumSize", new Dimension(500, 300));

		JPanel panel = new JPanel();// new GridLayout());
		panel.setLayout(null);

		

		panel.add(new JLabel("Change Log:")).setBounds(0, 0, 400, 20);
		JTextArea description = new JTextArea();
		description.setLineWrap(true);
		description.setEditable(false);
		
		JScrollPane scrollPane_Desc = new JScrollPane();
		scrollPane_Desc.setViewportView(description);
		panel.add(scrollPane_Desc).setBounds(0, 20, 400, 200);
		
		try {
			InputStream in =  mainClass.class.getResourceAsStream("Change Log"); 
	            
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    description.setText(everything);
		    br.close();
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		description.setCaretPosition(0);
		
		JOptionPane.showConfirmDialog(null, panel, "Change Log", JOptionPane.OK_CANCEL_OPTION);// ,
	
	}
}
