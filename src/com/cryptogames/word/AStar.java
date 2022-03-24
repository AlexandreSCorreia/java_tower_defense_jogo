package com.cryptogames.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

	public static double lastTime = System.currentTimeMillis();
	//Metodo para organizar a lista
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		@Override
		public int compare(Node n0, Node n1) {
			if(n1.fCost < n0.fCost) {
				return +1;
			}
			if(n1.fCost > n0.fCost) {
				return -1;
			}
			
			return 0;
		}
	};
	
	
	//Otimizar
	public static boolean clear() {
		if(System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		}
		return false;
	}
	//findPath em portugues significa achar caminho
	public static List<Node> findPath(World word,Vector2i start,Vector2i end){
		lastTime = System.currentTimeMillis();
		//Lista de posiçoes posiveis para encontrar o player
		List<Node> openList = new ArrayList<Node>();
		//Lista de posicoes que já foram testadas e que não achar o player
		List<Node> closedList = new ArrayList<Node>();
		
		
		//current é tradução : atual
		Node current  = new Node(start,null,0,getDistance(start,end));
		
		openList.add(current);
		while(openList.size() > 0) {
			Collections.sort(openList,nodeSorter);
			current = openList.get(0);
			//Aqui eu já cheguei ao meu destino, já conssigo
			// percorrer as posiçoes para encontrar o meu destino
			if(current.tile.equals(end)) {
				//Chegamos no ponto final
				//Basta retornar o valor
				List<Node> path = new ArrayList<Node>();
				while(current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				//Esvaziar da memoria minha open list
				openList.clear();
				closedList.clear();
				return path;
			}
			//Se não consseguiu achar irei remover da minha lista
			//Tipo o valor não será guardado aqui pois não é a minha posição final a onde quero chegar
			openList.remove(current);
			//Irei adicionar na lista onde está o caminho que não está livre
			closedList.add(current);
			
			
			
			//Verificar todas posicoes visinhas para ver se posso ou não seguir em frente
			for(int i = 0; i < 9; i++) {
				//é a posicao do meu jogado
				if(i == 4) continue;
				
				int x = current.tile.x;
				int y = current.tile.y;
				int xi = (i%3) - 1;
				int yi = (i/3) - 1;
				//Pegar o tile atual e verificar se não é uma parede
				Tiles tile =  World.tiles[x+xi+((y+yi)* World.WIDTH)];
				
				//Verificar se o tile exist
				if(tile == null)continue;
				//Verificar se o tile é uma parede
				if(tile instanceof WallTile) continue;
				if(i== 0) {
					Tiles test = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
					Tiles test2 = World.tiles[x+xi+((y+yi+1)*World.WIDTH)];
						if(test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
				}
				
				else if(i == 2) {
					Tiles test = World.tiles[x+xi-1+((y+yi)*World.WIDTH)];
					Tiles test2 = World.tiles[x+xi+((y+yi + 1)*World.WIDTH)];
						if(test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
				}
				
				
				else if(i == 6) {
					Tiles test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
					Tiles test2 = World.tiles[x+xi+1+((y+yi)*World.WIDTH)];
						if(test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
				}
				
				else if(i == 8) {
					Tiles test = World.tiles[x+xi+((y+yi-1)*World.WIDTH)];
					Tiles test2 = World.tiles[x+xi-1+((y+yi)*World.WIDTH)];
						if(test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
				}
				
				
				Vector2i a = new Vector2i(x+xi,y+yi);
				double gCost = current.gCost + getDistance(current.tile,a);
				double hCost = getDistance(a,end);
				
				
				Node node = new Node(a,current,gCost,hCost);
				
				//Verificar se esta posicao já está na lista
				if(vecInList(closedList,a) && gCost >= current.gCost ) continue; 
					
				if(!vecInList(openList,a)) {
					openList.add(node);
				}else if(gCost < current.gCost) {
					openList.remove(current);
					openList.add(node);
				}
			}
		}
		
		closedList.clear();
		return null;
	}
	
	//verificar se o node já está na lista
	public static boolean vecInList(List<Node> list, Vector2i vector) {
		for(int i=0;i < list.size();i++) {
			if(list.get(i).tile.equals(vector) ) {
				return true;
			}
		}
		
		return false;
	}
	
	
	//Caucular distancia entre dois pontos
	public static double getDistance(Vector2i tile,Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx*dx + dy*dy);
	}
}
