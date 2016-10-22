package com.himdo.JMusic;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PathsGui extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			PathsGui dialog = new PathsGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({"rawtypes","unchecked"})
	public PathsGui() {
		
		setBounds(100, 100, 584, 418);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 546, 167);
		contentPanel.add(scrollPane);
		
		JList PathLists = new JList();
		scrollPane.setViewportView(PathLists);
		
		
		JButton btnRemovePath = new JButton("Remove Path");
		
		btnRemovePath.setBounds(127, 198, 122, 23);
		contentPanel.add(btnRemovePath);
		
		JButton btnAddPath = new JButton("Add Path");
		
		btnAddPath.setBounds(10, 198, 107, 23);
		
		//set the path list to show the saved paths
		try{
			DefaultComboBoxModel RefreshNames = new DefaultComboBoxModel();

			File f = new File("src/com/himdo/JMusic/Settings/Paths.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	RefreshNames.addElement(line);
		    }
		    
	    	PathLists.setModel(RefreshNames);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		contentPanel.add(btnAddPath);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton saveButton = new JButton("Save Changes");
				
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
				
				
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File f = new File("src/com/himdo/JMusic/Settings/Paths.txt");
						if(!f.exists()){
							try {
								f.createNewFile();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
						
					try {
						FileWriter fw = new FileWriter(f.getAbsolutePath());
						BufferedWriter bw = new BufferedWriter(fw);
						
						for(int i =0;i < PathLists.getModel().getSize();i++){
							bw.write(PathLists.getModel().getElementAt(i).toString()+"\n");
						}
						bw.close();
				
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					dispose();
						
					}
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			}
		}
		
		
		btnAddPath.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("choosertitle");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);

			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			    	System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			    	System.out.println("getSelectedFile() : " + chooser.getSelectedFile());//this is the one to use
			     
			      
			    	DefaultComboBoxModel RefreshNames = new DefaultComboBoxModel();
			      
			    	for(int i = 0; i < PathLists.getModel().getSize();i++){

			    		RefreshNames.addElement(PathLists.getModel().getElementAt(i));
			    	}
			      
			    	RefreshNames.addElement(chooser.getSelectedFile());
			      
			    	PathLists.setModel(RefreshNames);
			    
			    } else {
			    	System.out.println("No Selection ");
			    }
			}
		});
		btnRemovePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = PathLists.getSelectedIndex();
				if(selected <0)
					return;
				DefaultComboBoxModel RefreshNames = new DefaultComboBoxModel();
				
		    	for(int i = 0; i < PathLists.getModel().getSize();i++){
		    		RefreshNames.addElement(PathLists.getModel().getElementAt(i));
		    	}
		    	RefreshNames.removeElementAt(selected);
		    	PathLists.setModel(RefreshNames);
			}
		});
		
		
		
	}
}
