package com.alet.client.sounds.handler;

public interface ISoundHandler {
	
	public void loop(boolean loop);
	
	public void play();
	
	public void pause();
	
	public void stop();
	
	public void set(int tick);
	
	public int get();
}
