package com.himdo.JMusic;

public class Conversions {
	public static long intToSeconds(long x){
		return x*1000000;
	}
	public static int microToMinutes(long x){
		return (int) ((x /1000000)/60);
	}
	public static int microToSeconds(long x){
		return (int) ((x /1000000)%60);
	}
}
