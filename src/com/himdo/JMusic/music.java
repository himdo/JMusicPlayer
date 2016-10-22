package com.himdo.JMusic;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class music {
	private static AudioInputStream stream;
	private static AudioFormat format;
	private static DataLine.Info info;
	private static Clip clip;
	private static FloatControl volume;
	private static long clipTime = 0;
	
	public static boolean paused = false;
	public static boolean loop=false;
	
	public static void play(File music){
		try{			
			if(music.getName().toLowerCase().endsWith(".wav")){
				paused = false;
				loop=false;
				clipTime=0;
				stream = AudioSystem.getAudioInputStream(music);
				format = stream.getFormat();
				info = new DataLine.Info(Clip.class, format);
				clip = (Clip) AudioSystem.getLine(info);
				clip.open(stream);
				volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				clip.start();
				
			}else{
				System.out.println("not the right type");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void playAgain(){
		if(clip == null)
			return;
		clipTime=0;
		clip.setMicrosecondPosition(clipTime);
		clip.start();
		paused=false;
	}
	
	public static void setTime(long l,boolean play){
		if(clip == null)
			return;
		clipTime=l;
		clip.setMicrosecondPosition(clipTime);
		if(play){
			if(!paused)
				clip.start();
		}
	}
	
	public static void stop(){
		if(clip != null){
			if(paused)
				pause();
			if(clip.isActive()){
				loop=false;
				clip.stop();
				clip.flush();
				clip.close();
				paused=false;
				clipTime=0;
			}
		}
	}
	
	public static void pause(){
		if(clip == null || !clip.isOpen()){
			return;
		}
		if(!paused){
			paused=true;
			clipTime = clip.getMicrosecondPosition();
			
			clip.stop();
		}else if(paused==true){
			
			clip.setMicrosecondPosition(clipTime);
			clip.start();
			paused=false;
		}
	}

	@SuppressWarnings("static-access")
	public static void setLoop(){
		if(clip == null || !clip.isOpen()){
			loop=false;
			return;
		}
		if(loop){
			clip.loop(clip.LOOP_CONTINUOUSLY);	
		}else{
			clip.loop(0);
		}
	}
	
	public static void changeVolume(float value) {
		if(volume != null){
			try{
				System.out.println(volume);
				float max = volume.getMaximum();
	            float min = volume.getMinimum(); // negative values all seem to be zero?
	            float range = max - min;

	            volume.setValue(min + (range * ((value/100))));
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			System.out.println("volume = null");
		}
	}

	public static long getCurrentSongTime()
	{
		if(clip == null)
			return -1;
		
		return clip.getMicrosecondPosition();
	}

	public static long getSongTime() {
		if(clip == null)
			return -1;
		return clip.getMicrosecondLength();
		 
	}

	public static int getRemainingMinutes(){
		if (clip == null)
			return -1;
		return Conversions.microToMinutes(getSongTime()-getCurrentSongTime());
	}
	public static int getRemainingSeconds(){
		if (clip == null)
			return -1;
		return Conversions.microToSeconds(getSongTime()-getCurrentSongTime());
	}
	
	public static int getMinutes() {
		if(clip == null)
			return -1;
		return Conversions.microToMinutes(getSongTime());
	}
	
	public static int getSeconds(){
		if(clip == null)
			return -1;		
		return Conversions.microToSeconds(getSongTime());
	}
}
