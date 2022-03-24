package com.cryptogames.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import com.cryptogames.entities.Entity;
import com.cryptogames.entities.Player;
import com.cryptogames.entities.TowerControler;
import com.cryptogames.graficos.Spritesheet;
import com.cryptogames.graficos.UI;
import com.cryptogames.word.World;


//MouseMotionListener capturar movimento do mouse se movendo
public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{

	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE= 3;
	private static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	
	private BufferedImage imagem; 
	
	public static World world;
	public static List<Entity> entities;
	public static Spritesheet spritesheet;

	
	public UI ui;
	//Vida
	public static double vida = 10;
	//Dinheiro
	public static int dinheiro = 100;
	//towerControler
	public TowerControler towerControler;
	
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
		
		
	}
	
	
	public Game() {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		imagem = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		/*Inicializando Objetos...*/
		//TowerControler é uma entidade sem vida por conta disso posso deixa lá zerada
	
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		world = new World("/level1.png");
		ui =  new UI();
		towerControler = new TowerControler(0,0,0,0,0,null);
	}
	
	void initFrame() {
		//Criando janela
		frame = new JFrame("Tower Defense");
		frame.add(this);//Passando o canvas para a janela usar as propriedades
		//Desabilitar redimensionar a tela
		frame.setResizable(false);
		//Metodo para apos adicionar o canvas calcular certo as dimensoes
		frame.pack();
		//Inicializar janela no centro da tela
		frame.setLocationRelativeTo(null);
		//Garantir que quando a janela for fechada ela fechará de fato
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Para quando a janela for inicializada ficar visivel
		frame.setVisible(true);
		
	}
	
	
	synchronized void start() {
		//Instanciando a thread e falando que a biblioteca com as propriedades estão nesta classe
		thread = new Thread(this);
		//inicializando variavel responsavel por dizer que o game está rodando
		isRunning = true;
		//inicializando thread
		thread.start();
	}
	
	synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}
		
	}
	
	void tick() {
	//Carregando sprite
	for(int i=0; i < entities.size();i++) {
			Entity e = entities.get(i);
			e.tick();
		}
	
	//TowerControler
	towerControler.tick();
	}
	
	
	void render() {	
		//Criar os graficos do game
		BufferStrategy bs = this.getBufferStrategy();
		if(bs==null) {
			//Sequencia de buffer para otimizar a reenderização
			this.createBufferStrategy(3);
			return;
		}		
		//Passando BufferedImage como grafico para reenderizar
		Graphics g = imagem.getGraphics();
		//setando uma cor
		g.setColor(new Color(122,102,255));
		//Criando um retangulo
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//Reenderiza game
		//Ordem de reenderizacao
		world.render(g);
		Collections.sort(entities,Entity.nodeSorter);
		//Reenderizando sprite
		for(int i=0; i < entities.size();i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		g.dispose();
		//Pegando os graficos
		g = bs.getDrawGraphics();
		//para mostrar a imagem
		//passar imagem, depois posição e por fim largura
		g.drawImage(imagem,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		//Colocar como ultimo para ficar acima de tudo
		ui.render(g);
		//Mostrando render
		bs.show();
	}
	
	
	
	//Game Looping
	@Override
	public void run() {
		requestFocus();
		//Essa variavel irá receber o tempo atual do pc em nano segundos
		long lastTime = System.nanoTime();
		//Taxa de atualização FPS
		double amountOfTicks = 60.0;
		//dividindo 1 segundo por amountOfTicks
		double ns = 1000000000/amountOfTicks;
		//
		double delta=0;
		int frames = 0;
		//Vai me retornar o tempo atuali só que em milesegundos
		double time = System.currentTimeMillis();
		while(isRunning) {
			//capturando o segundo de agora
			long now = System.nanoTime();
			//calculo para saber o tempo exato para fazer a atualização
			delta += (now - lastTime) / ns;
			//atualizando que o ultimo tempo é agora
			lastTime = now;
			//quando delta for um é o momento que acontecerá a atualização
			 if(delta >= 1) {
				 tick();
				 render();
				 frames++;
				 //como delta deu 1 ou maior irei decrementar ele
				 delta--;
			 }
			 
			 //Mostrar FPS
			 if(System.currentTimeMillis() - time >= 1000) {
				 System.out.println("FPS: "+frames);
				 frames = 0;
				 time +=1000;
			 }
		}
		//Caso de algum erro no looping parar as threads
		stop();
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
			
	}
	@Override
	public void mousePressed(MouseEvent e) {
		towerControler.isPressed=true;
		//Dividir por 3 por causa do SCALE da janela
		towerControler.xTarget = e.getX()/3;
		towerControler.yTarget = e.getY()/3;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
			
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
			
	}
	@Override
	public void mouseDragged(MouseEvent e) {
			
	}
	//Rotacao Avançada
	@Override
	public void mouseMoved(MouseEvent e) {
			
	}

}