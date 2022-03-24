package com.cryptogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cryptogames.main.Game;

public class Spawner extends Entity{

	private int time = 60;
	private int curTime = 0;
	public Spawner(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

	}

	public void tick() {
		//Criar Inimigos!
		curTime++;
		if(curTime == time) {
			//Hora de criar o inimigo
			curTime=0;
			time = Entity.rand.nextInt(60 - 30) + 30;
			Enemy enemy = new Enemy(this.getX(),this.getY(),16,16,Entity.rand.nextDouble() + Entity.rand.nextInt(2),Entity.ENEMY_SPRITE);
			Game.entities.add(enemy);
		}
	}
	
	public void render(Graphics g){
		//g.setColor(Color.red);
		//g.fillRect((int)x,(int) y, width, height);
	}
}
