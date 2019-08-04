package com.ltphoto.photo;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class AtlasSpriteToPath {

	private String modid;
	private String path;

	public AtlasSpriteToPath(String texture) {
		String[] pathArray = texture.split("'");
		pathArray = pathArray[1].split(":");
		modid = pathArray[0];
		path = pathArray[1];
	}
	
	
	//"assets/minecraft/textures/blocks/brick.png"
	public String getPath() {
		path = "assets/"+modid+"/textures/"+path+".png";
		return path;
	}
	
}
