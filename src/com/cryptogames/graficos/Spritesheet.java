package com.cryptogames.graficos;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Spritesheet {

	private BufferedImage spritesheet;
	
	//pega a sprite da pasta res 
	public Spritesheet(String path) {
		try {
			spritesheet = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//pega a imagem na posição passada
	public BufferedImage getSprite(int x,int y, int width, int height) {
		return spritesheet.getSubimage(x, y, width, height);
	}
}
