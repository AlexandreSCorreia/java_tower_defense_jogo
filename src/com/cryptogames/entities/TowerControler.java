package com.cryptogames.entities;

import java.awt.image.BufferedImage;

import com.cryptogames.main.Game;
import com.cryptogames.word.World;

public class TowerControler extends Entity{

	public boolean isPressed = false;
	public int xTarget=0,yTarget=0;
	
	public TowerControler(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		
	}

	
	public void tick() {
		if(isPressed) {
			isPressed = false;
			boolean liberado = true;
			//Vamos criar uma torre
			//(xTarget/16)*16 porque o jogo funciona num sistema de grid
			int xx =(xTarget/16)*16;
			int yy =(yTarget/16)*16;
			
			Player player = new Player(xx,yy,16,16,0,Game.spritesheet.getSprite(16, 16, 16, 16));
			
			for(int i=0; i < Game.entities.size();i++) {
				Entity e = Game.entities.get(i);
				if(e instanceof Player) {
					if(Entity.isColliding(e, player)) {
						liberado=false;
						System.out.println("Já existe torre nesta posição");
					}
					
				}	
			}
			//Se não estiver liberado
			//Verificando se a posicao não é rua
			if(World.isFree(xx, yy)) {
				liberado=false;
				System.out.println("Não pode adicionar torres nas ruas!");
			}
			if(liberado) {
				if(Game.dinheiro >= 2) {
					Game.entities.add(player);
					Game.dinheiro-=2;
				}else {
					System.out.println("Saldo insuficiente para comprar torres!");
				}
				
			}
		}
		if(Game.vida <= 0) {
			//Game Over
			System.exit(1);
		}
	}
}
