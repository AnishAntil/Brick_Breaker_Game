package demogame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;	

import javax.swing.JPanel;


public class GamePlay extends JPanel implements ActionListener, KeyListener{
	private boolean play=false;
	private int score=0;//add this 
	private int totalBrick=21;
	private Timer timer;
	private int delay=8;
	private int ballposX=120;
	private int ballposY=350;
	private int ballXdir=-1;
	private int ballYdir=-2;
	private int playerX=320;//350
	private MapGenerator map;
	
	public GamePlay() {
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(true);
		
		timer = new Timer(delay,this);
		timer.start();
		
		map=new MapGenerator(3,7);
	}
	
	public void paint(Graphics g) {
		//black canvas
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(0, 3, 3, 592);
		g.fillRect(691, 3, 3, 592);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//bricks
		
		map.draw((Graphics2D)g);
		
		//ball
		g.setColor(Color.red);
		g.fillOval(ballposX,ballposY,20,20);
		
		//score
		g.setColor(Color.green);
		g.setFont(new Font("serif",Font.BOLD,20));
		g.drawString("Score :"+score, 550, 30);
		
		
		//gameover
		if(ballposY>=570) {
			play=false;
			ballXdir=0;
			ballYdir=0;
			
			g.setColor(Color.green);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("GameOver!!, Score : "+score, 200, 300);
			
			g.setFont(new Font("serif",Font.BOLD,25));
			g.drawString("Press Enter to Restart!!", 230, 350);

		}
		
		if(totalBrick<=0) {
			
			play=false;
			ballXdir=0;
			ballYdir=0;
			
			g.setColor(Color.green);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Won!!, Score : "+score, 200, 300);
			
			g.setFont(new Font("serif",Font.BOLD,25));
			g.drawString("Press Enter to Restart!!", 230, 350);
			
		}

	}
	
	private void moveLeft() {
		play=true;
		playerX-=20;
	}

	private void moveRight() {
		play=true;
		playerX+=20;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(playerX<=0)
				playerX=0;
			else
				moveLeft();
		}
		
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(playerX>=600)
				playerX=600;
			else
				moveRight();
		}
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				score=0;
				totalBrick=21;
				ballposX=120;
				ballposY=350;
				ballXdir=-1;
				ballYdir=-2;
				playerX=320;
				
				map=new MapGenerator(3,7);
				
			}
		}
		
		repaint();
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(play) {
			
			if(ballposX<=0) {
				ballXdir=-ballXdir;
			}
			if(ballposX>=670) {
				ballXdir=-ballXdir;
			}
			if(ballposY<=0) {
				ballYdir=-ballYdir;
			}
			
			Rectangle ballRect=new Rectangle(ballposX,ballposY,20,20);
			Rectangle paddleRect=new Rectangle(playerX,550,100,8);
			
			if(ballRect.intersects(paddleRect)) {
				ballYdir=-ballYdir;
			}
			
			
			A:for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[i].length;j++)
				{
					if(map.map[i][j]>0) {
						
						int width=map.brickWidth;
						int height=map.brickHeight;
						int brickXpos=80+j*width;
						int brickYpos=50+i*height;
						
						Rectangle brickRect=new Rectangle(brickXpos,brickYpos,width,height);
						
						if(ballRect.intersects(brickRect)) {
							
							map.setBrick(0, i, j);
							totalBrick--;
							score+=5;
						
							if(ballposX+19<=brickXpos || ballposX+1>=brickXpos+width) {
								ballXdir=-ballXdir;
							}
							else {
								ballYdir=-ballYdir;
							}
							
							
							
							break A;
							
						}
						
					}
				}
			}
			
			ballposX+=ballXdir;
			ballposY+=ballYdir;
			
		}
		
		repaint();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
}
