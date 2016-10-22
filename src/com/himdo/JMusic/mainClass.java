package com.himdo.JMusic;

import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.himdo.JMusic.version.Version;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.MouseMotionAdapter;

@SuppressWarnings("serial")
public class mainClass extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainClass frame = new mainClass();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	public mainClass() {
		setTitle("JMusicPlayer v"+Version.getVersion());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 624, 438);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		
		mnFile.add(mntmOpen);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mntmAddPaths = new JMenuItem("Add Paths");
		
		mnSettings.add(mntmAddPaths);
		
		JMenuItem mntmChangeLog = new JMenuItem("Change Log");
		
		mnSettings.add(mntmChangeLog);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel Remaining = new JLabel("0");
		Remaining.setAutoscrolls(true);
		Remaining.setBounds(244, 5, 119, 14);
		contentPane.add(Remaining);
		
		JButton btnStop = new JButton("Stop");
		
		
		btnStop.setBounds(10, 53, 89, 23);
		contentPane.add(btnStop);
		
		JButton btnPause = new JButton("Pause");
		
		btnPause.setBounds(109, 53, 89, 23);
		contentPane.add(btnPause);
		
		JSlider VolumeSlider = new JSlider();
		VolumeSlider.setValue(100);
		
		
		VolumeSlider.setBounds(408, 53, 190, 26);
		contentPane.add(VolumeSlider);
		
		JLabel lblVolume = new JLabel("Volume:");
		lblVolume.setBounds(352, 57, 46, 14);
		contentPane.add(lblVolume);
		
		JToggleButton tglbtnLoop = new JToggleButton("Loop");
		
		
		tglbtnLoop.setBounds(208, 53, 121, 23);
		contentPane.add(tglbtnLoop);
		
		JFormattedTextField SongName = new JFormattedTextField();
		SongName.setHorizontalAlignment(SwingConstants.CENTER);
		SongName.setEditable(false);
		SongName.setText("No Loaded Song");
		SongName.setBounds(10, 121, 588, 26);
		contentPane.add(SongName);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 158, 588, 2);
		contentPane.add(separator);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 171, 588, 196);
		contentPane.add(scrollPane);
		
		JList songList = new JList();
		
		
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(songList);
		
		JButton forward15 = new JButton("+15");
		
		forward15.setBounds(509, 87, 89, 23);
		contentPane.add(forward15);
		
		JButton backward15 = new JButton("-15");
		
		backward15.setBounds(410, 87, 89, 23);
		contentPane.add(backward15);
		
		JSlider CurrentSpotScroller = new JSlider();
		CurrentSpotScroller.setValue(0);
		CurrentSpotScroller.setBounds(10, 20, 588, 26);
		contentPane.add(CurrentSpotScroller);
		
		JButton btnPlaySelected = new JButton("Play Selected");
		
		
		ArrayList<String> SongPathLists = new ArrayList<>();
		
		//new thread to track song percentage
		Thread_MusicTracker tracker = new Thread_MusicTracker("tracker",CurrentSpotScroller,btnStop,Remaining);
		tracker.setName("die");
		
		if(!tracker.isAlive())
			tracker.start();
		
		//add songs to the selection area
		try{
			DefaultComboBoxModel SongNames = new DefaultComboBoxModel();

			File f = new File("src/com/himdo/JMusic/Settings/Paths.txt");
			
			BufferedReader br = new BufferedReader(new FileReader(f));
			while (br.ready()){
				String folderNames=br.readLine();
				File folder = new File(folderNames);
				File[] listOfFiles = folder.listFiles();
				
				for (File file : listOfFiles) {
				    if (file.isFile()) {
				    	if(file.getName().toLowerCase().endsWith(".wav")){
				    		SongPathLists.add(file.getAbsolutePath());
				    		SongNames.addElement(file.getName());
				    		System.out.println(file.getName());
				    	}
				    }
				}
			}
			songList.setModel(SongNames);
			
			
			btnPlaySelected.setBounds(10, 87, 130, 23);
			contentPane.add(btnPlaySelected);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Shows Tool Tip after hovering over a song
		songList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				JList l = (JList)arg0.getSource();
	            ListModel m = l.getModel();
	            int index = l.locationToIndex(arg0.getPoint());
	            if( index>-1 ) {
	                l.setToolTipText(SongPathLists.get(index));//m.getElementAt(index).toString());
	            }
			}
		});
		//End Tool Tip//
		
		//Start Settings Menu//
		mntmAddPaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PathsGui gui = new PathsGui();
				gui.setModalityType(Dialog.ModalityType.APPLICATION_MODAL );
				gui.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				gui.setVisible(true);

				try{
					DefaultComboBoxModel SongNames = new DefaultComboBoxModel();

					File f = new File("src/com/himdo/JMusic/Settings/Paths.txt");
					
					BufferedReader br = new BufferedReader(new FileReader(f));
					while (br.ready()){
						String folderNames=br.readLine();
						File folder = new File(folderNames);
						File[] listOfFiles = folder.listFiles();
						
						for (File file : listOfFiles) {
						    if (file.isFile()) {
						    	if(file.getName().toLowerCase().endsWith(".wav")){
						    		SongPathLists.add(file.getAbsolutePath());
						    		SongNames.addElement(file.getName());
						    		System.out.println(file.getName());
						    	}
						    }
						}
					}
					songList.setModel(SongNames);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		mntmChangeLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.changeLog();
			}
		});
		//End Setting Menu//
		
		
		
		
		//Start Music Options Functions//
		tglbtnLoop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				music.loop = tglbtnLoop.isSelected();
				music.setLoop();
				tglbtnLoop.setSelected(music.loop);
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(btnStop.getText().equals("Stop")){
					music.setTime(0,false);
					music.stop();
					music.loop=false;
					tracker.setName("die");
					tglbtnLoop.setSelected(false);
					btnPause.setText("Pause");
					SongName.setText("No Loaded Song");
				}else if(btnStop.getText().equals("Play")){
					music.playAgain();
				}
			}
		});
		
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				music.pause();
				if(music.paused){
					btnPause.setText("Continue");
				}else{
					btnPause.setText("Pause");
				}
			}
		});
		
		VolumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				music.changeVolume(VolumeSlider.getValue());
			}
		});
		//Start Music Options Functions//
		
		
		
		
		//Start Playing Songs Functions//
		btnPlaySelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selected = songList.getSelectedIndex();
				if(selected <0)
					return;
				music.stop();
				
				File f = new File(SongPathLists.get(selected));
				music.play(f);
				tracker.setName("tracker");

				SongName.setText(f.getName()+"\tTotal Time:"+music.getMinutes()+" min "+music.getSeconds()+ "sec");
			}
		});
		
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser musicChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("wav file", "WAV", "wav");
				musicChooser.setFileFilter(filter);

				if (musicChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					music.stop();
					File file = musicChooser.getSelectedFile();
					music.play(file);
					tracker.setName("tracker");
					
					SongName.setText(file.getName()+"\tTotal Time:"+music.getMinutes()+" min "+music.getSeconds()+ "sec");
				}
			}
		});
		//End Playing Songs Functions//
		
		
		
		
		//Start Time Control Functions//
		forward15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				music.setTime(music.getCurrentSongTime()+Conversions.intToSeconds(15),true);
			}
		});
		
		backward15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				music.setTime(music.getCurrentSongTime()+Conversions.intToSeconds(-15),true);	
			}
		});
		//End Time Control Functions//
	}
}
