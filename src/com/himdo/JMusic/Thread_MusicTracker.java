package com.himdo.JMusic;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class Thread_MusicTracker extends Thread{

	private Thread t;
	   private String threadName;
	   private JSlider CurrentSpot;
	   private JButton stopPlay;
	   private JLabel timeLeft;
	   
	   Thread_MusicTracker( String name, JSlider CurrentSpotScroller,JButton stop,JLabel remaining) {
	      threadName = name;
	      CurrentSpot = CurrentSpotScroller;
	      stopPlay = stop;
	      timeLeft = remaining;
	      System.out.println("Creating " +  threadName );
	   }
	   
	   
	   
	   public void run() {
		   System.out.println("Thread Starting");
		   while(true){

			   CurrentSpot.setValue(0);
				   while(this.getName().equals("die")){
					   try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			   }
			   while(!Thread.interrupted()){
				   try {
					   while(music.getCurrentSongTime()==-1){
						   //wait for song
						   Thread.sleep(100);
					   }
					   
					   long songTime = music.getSongTime();
					   long currentSongTime=music.getCurrentSongTime();
					   float percent=((float)currentSongTime/(float)songTime)*100;

					   StringBuilder setTimeLeft = new StringBuilder("");
					   if(music.getRemainingMinutes()!=0)
						   setTimeLeft.append(music.getRemainingMinutes()+"min ");
					   setTimeLeft.append(music.getRemainingSeconds()+"sec");
					   timeLeft.setText(setTimeLeft.toString());
					   //setTimeLeft;
					   //System.out.println("\n\nsong time:" + (int)songTime+"\nCurrent time: "+ (int)currentSongTime+"\npercentage: "+percent);
					   
					   CurrentSpot.setValue((int)percent);
					   if(CurrentSpot.getValue()==100){
						   stopPlay.setText("Play");
					   }else{
						   stopPlay.setText("Stop"); 
					   }
					   if(music.loop){
						   if(percent>=100){
							   music.setTime(0, true);
						   }
					   }
					   Thread.sleep(100);
					   if(this.getName().equals("die")){
						   break;
					   }
				   } catch (InterruptedException e) {
					   e.printStackTrace();
				   }
			   }
		   }
	   }
	   
	   public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
}
