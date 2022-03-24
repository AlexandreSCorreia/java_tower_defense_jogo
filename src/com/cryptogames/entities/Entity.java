package com.cryptogames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.cryptogames.main.Game;
import com.cryptogames.word.Camera;
import com.cryptogames.word.Node;
import com.cryptogames.word.Vector2i;
import com.cryptogames.word.World;

public class Entity {

	protected double x,y,speed;
	protected int width,height;
	protected BufferedImage sprite;
	//Manipular ordem de reenderização
	public int derp;
	//Algoritmo *A
	public List<Node> path;
	public static Random rand = new Random();

	public static BufferedImage ENEMY_SPRITE = Game.spritesheet.getSprite(32, 0, 16, 16);

	public Entity(double x,double y,int width,int height, double speed,BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		this.sprite = sprite;	
	}
	
	//Ordem de reenderização
	//Esse metodo irá comparar 2 entities
	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
			
			@Override
			public int compare(Entity n0, Entity n1) {
				if(n1.derp < n0.derp) {
					//colocar ele na primeira posicao da lista
					return +1;
				}
				if(n1.derp > n0.derp) {
					//colocar ele na ultima posicao da lista
					return -1;
				}
				
				return 0;
			}
		};
	
	//Algoritmo *A
		protected void followPath(List<Node>path) {
		if(path != null) {
			//Significa que ainda tenho caminho para percorrer
			if(path.size() > 0) {
				//Pegar o ultimo item da lista
				Vector2i target = path.get(path.size()-1).tile;
				//vezes 16 pois irei passar a posicao em tile
				//Convertendo para pixels
				if(x < target.x * 16) {
					x++;
				}else if(x > target.x*16) {
					x--;
				}
				
				if(y < target.y * 16) {
					y++;
				}else if(y > target.y*16) {
					y--;
				}
				
				
				//Se eu cheguei na minha posicao
				if(x == target.x * 16 && y == target.y * 16) {
					//Significa que já cheguei a onde queria então
					//Possso remover da lista e ir ao proximo caminho
					path.remove(path.size() - 1 );
				}
			}
		}
	}
	
	protected void updateCamera() {
		//Fazer a camera acompanhar o player
		//Posicao da camera é igual a posicao do meu player - metade da largura ou altura do mapa
		//Ajustando a camera para mostrar somente o mapa
		//Para o Clamp passa primeiro a posição atual, a posicao minima, e a posicao mapa que é o tamanho do mundo menos o tamanho da tela
		Camera.x  = Camera.Clamp(this.getX() - (Game.WIDTH/2), 0, (World.WIDTH *16) - Game.WIDTH);
		Camera.y  = Camera.Clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT*16) - Game.HEIGHT);
	}
	
	public static double calculateDistance(int x1, int y1,int x2, int y2) {	
		//sqrt me retorna a distanci com base no angulo do meu player e do inimigo
		//https://www.todamateria.com.br/distancia-entre-dois-pontos/
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	//Sistema de colisao
	protected static boolean isColliding(Entity e1, Entity e2) {
		//Criando um retangulo como mascara
		Rectangle e1Mask = new Rectangle(e1.getX(),e1.getY(),e1.getWidth(),e1.getHeight());
		Rectangle e2Mask = new Rectangle(e2.getX(),e2.getY(),e2.getWidth(),e2.getHeight());
		//Retorna verdadeiro ou falso se colidir ou não com a mascara
		return e1Mask.intersects(e2Mask);
	}
	
	
	public void tick() {
		
	}
	
	//reenderizando sprite
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
	

	public int getX() {
		return (int)x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return (int)y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
