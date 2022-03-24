package com.cryptogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cryptogames.main.Game;

public class Player extends Entity{
	
	public int xTarget=0,yTarget=0;
	public boolean atacando= false;
	
	public Player(int x, int y, int width, int height,double speed, BufferedImage sprite) {
		super(x, y, width, height,speed, sprite);
		
	}

	public void tick() {
		//Calcular distancia entre essa torre e o inimigo mais proximo
		Enemy enemy = null;
		for(int i=0;i < Game.entities.size();i++) {
			Entity e = Game.entities.get(i);
			if(e instanceof Enemy) {
				int xEnemy = e.getX();
				int yEnemy = e.getY();
				
				//Calcular distancia
				if(calculateDistance(this.getX(),this.getY(),xEnemy, yEnemy) < 40) {
					enemy = (Enemy)e;
				}
			}
		}
		
		
		if(enemy != null) {
			atacando = true;
			xTarget = enemy.getX(); 
			yTarget = enemy.getY();
			if(Entity.rand.nextInt(100) < 10) {
				enemy.life-=Entity.rand.nextDouble();
			}
			
		}else {
			atacando = false;
		}
	}
	
	public void render(Graphics g) {
		super.render(g);
		//trar atack as torres!
		if(atacando) {
			//Reenderizar uma linha para mostrar o atack
			g.setColor(Color.RED);
			g.drawLine((int)x+6, (int)y+6, xTarget+6, yTarget+6);
		}
	}
	
}
