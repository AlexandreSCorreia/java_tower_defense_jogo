package com.cryptogames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.cryptogames.main.Game;
import com.cryptogames.word.AStar;
import com.cryptogames.word.Camera;
import com.cryptogames.word.Vector2i;
import com.cryptogames.word.World;

public class Enemy extends Entity{

	public boolean right = true,left=false;
	public double life = 30;
	
	public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
		path = AStar.findPath(Game.world, new Vector2i(World.xINITIAL,World.yINITIAL),new Vector2i(World.xFINAL,World.yFINAL));
	}
	
	public void tick() {
		/*derp=2;
		
		if(World.isFree((int)(x+speed),(int)y)) {
			x+=speed;
		}else if(World.isFree((int)x,(int)(y+speed))) {
			y+=speed;
		}else if(World.isFree((int)x,(int)(y-speed))) {
			y-=speed;
		}*/
	
		followPath(path);
		if(x >= Game.WIDTH) {
			//Perdemos life aqui
			Game.vida-=Entity.rand.nextDouble();
			Game.entities.remove(this);
			return;
		}
		
		if(life <= 0) {
			Game.entities.remove(this);
			Game.dinheiro +=2;
			return;
		}
	}
	
	
	public void render(Graphics g) {
		super.render(g);
		
		g.setColor(Color.RED);
		g.fillRect((int)(x-7),(int)(y-5),30, 4);
		
		g.setColor(Color.GREEN);
		g.fillRect((int)(x-7),(int)(y-5),(int)((life/30)*30), 4);
	}

}
