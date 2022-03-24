package com.cryptogames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cryptogames.main.Game;

public class UI {
	public static BufferedImage HEART = Game.spritesheet.getSprite(0, 16, 13, 12);
	public void render(Graphics g) {
		for(int i=0; i < (int)(Game.vida);i++) {
			//i*40, para dar o espaçamento entre as reenderizaçoes
			g.drawImage(HEART, 20 + (i*40),10,36,36, null);
		}
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,25));
		g.drawString("$ " + Game.dinheiro, (Game.WIDTH *Game.SCALE) - 80, 30);
	}
}
