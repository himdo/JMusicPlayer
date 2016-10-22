package com.himdo.JMusic;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	public mainClass() {
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
		
		@SuppressWarnings("rawtypes")
		JList songList = new JList();
		songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(songList);
		
		JButton forward15 = new JButton("+15");
		
		forward15.setBounds(109, 87, 89, 23);
		contentPane.add(forward15);
		
		JButton backward15 = new JButton("-15");
		
		backward15.setBounds(10, 87, 89, 23);
		contentPane.add(backward15);
		
		JSlider CurrentSpotScroller = new JSlider();
		CurrentSpotScroller.setValue(0);
		CurrentSpotScroller.setBounds(10, 20, 588, 26);
		contentPane.add(CurrentSpotScroller);
		
		
		Thread_MusicTracker tracker = new Thread_MusicTracker("tracker",CurrentSpotScroller,btnStop,Remaining);
		
		
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
					if(!tracker.isAlive())
						tracker.start();
					
					SongName.setText(file.getName()+"\tTotal Time:"+music.getMinutes()+" min "+music.getSeconds()+ "sec");
				}
			}
		});
		
		mntmChangeLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				options.changeLog();
			}
		});
		
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
	}
}
