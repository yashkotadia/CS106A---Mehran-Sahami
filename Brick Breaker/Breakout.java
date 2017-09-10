/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  On some platforms 
  * these may NOT actually be the dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  On some platforms these may NOT actually
  * be the dimensions of the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW + 1)* BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Total Number of Bricks in the game. */
	private static final int NBRICKS = NBRICKS_PER_ROW * NBRICK_ROWS;
	
/** Delay time in milliseconds */
	private static final int DELAY=5;
	
/** Number of Bricks left in the game. */
	private int NBL = NBRICKS;
	
/** Number of  turns left */
	private int NTL = NTURNS;


/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		setUp();
		
		while(NTL > 0 && NBL!=0){
			message();
			waitForClick();
			setUpBall();
			remove(message);
			endTurn = 0;// Counter for ending the turn.
			while(endTurn==0 && NBL!=0){
				moveBall();
				checkForCollisionWithWall();
				checkForCollisionWithPaddle();
				checkForCollisionWithBrick();
				pause(DELAY);
			}
		}
		
		if(NBL==0){
			GLabel win = new GLabel("YOU WIN!!");
			win.setFont("London-36");
			add(win , (WIDTH-win.getWidth())/2 , (HEIGHT-win.getAscent())/2);
		}
		else{
			GLabel lose = new GLabel("Game Over!!");
			lose.setFont("London-36");
			add(lose , (WIDTH-lose.getWidth())/2 , (HEIGHT-lose.getAscent())/2);
		}
	}
	
/* Method: setUp() */
/** Initialises the game. */
	public void setUp(){
		setUpBricks();
		setUpPaddle();
		addMouseListeners();	
	}
	
/* Method: setUpPaddle() */
/** Initialises the paddle */
	public void setUpPaddle(){
		paddle = new GRect( PADDLE_WIDTH , PADDLE_HEIGHT );
		paddle.setFilled(true);
		add(paddle , (WIDTH - PADDLE_WIDTH)/2 , HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
	}

/* Method: setUpBall() */
/** Places the ball and gives initial velocity */
	public void setUpBall(){
		ball = new GOval( WIDTH/2 , HEIGHT/2 , BALL_RADIUS , BALL_RADIUS);
		ball.setFilled(true);
		vx = rgen.nextDouble(1.0 , 2.0);
		if(rgen.nextBoolean(0.5)) vx = - vx;
		add(ball);
	}
	
/* Method: setUpBricks() */	
/** Sets up the Bricks */
	private void setUpBricks(){
		fillTwoRows(Color.RED , BRICK_SEP , BRICK_Y_OFFSET);
		fillTwoRows(Color.ORANGE , BRICK_SEP , BRICK_Y_OFFSET + 2 * (BRICK_SEP + BRICK_HEIGHT));
		fillTwoRows(Color.YELLOW , BRICK_SEP , BRICK_Y_OFFSET + 4 * (BRICK_SEP + BRICK_HEIGHT));
		fillTwoRows(Color.GREEN , BRICK_SEP , BRICK_Y_OFFSET + 6 * (BRICK_SEP + BRICK_HEIGHT));
		fillTwoRows(Color.BLUE , BRICK_SEP , BRICK_Y_OFFSET + 8 * (BRICK_SEP + BRICK_HEIGHT));
	}	
	
/* Method: fillTwoRows() */
/** Sets up two row of bricks according to given specifications	
 * @param color takes in the colour of the bricks
 * @param x takes in the x coordinate of the top left Brick.
 * @param y takes in the y coordinate of the top left Brick.
 */
	private void fillTwoRows(Color color, int x , int y){
		for(int i=0 ;i<2 ;i++){
			for(int j=0; j < NBRICKS_PER_ROW ; j++){
				GRect rect=new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				rect.setColor(color);
				rect.setFilled(true);
				rect.setFillColor(color);
				add(rect, x+ j * (BRICK_SEP + BRICK_WIDTH) , y + i * (BRICK_HEIGHT + BRICK_SEP));
			}
		}
		
	}
	
/* Method: mouseMoved() */
/** Tracks the mouse and paddle */
	public void mouseMoved(MouseEvent e){
		if(e.getX()>=PADDLE_WIDTH/2 && e.getX()<WIDTH-PADDLE_WIDTH/2)
		paddle.setLocation(e.getX() - PADDLE_WIDTH/2 , HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT );
	}

/* Method: moveBall() */
/** Moves the ball */
	private void moveBall(){
		ball.move(vx, vy);
	}

/* Method: checkForCollisionWithWall() */
/** Checks for collision with walls and changes the velocity of ball accordingly */
	private void checkForCollisionWithWall(){
		
		// For collision with RIGHT WALL
		if( ball.getX() + 2*BALL_RADIUS > WIDTH){
			vx = -vx;
			double diff = ball.getX() + 2* BALL_RADIUS - WIDTH;
			ball.move( -2*diff, 0);
		}
		
		// For collision with LEFT WALL
		else if(ball.getX() < 0){
			vx = -vx;
			double diff = ball.getX();
			ball.move( -2*diff, 0);
		}
		
		// For collision with TOP WALL
		else if(ball.getY()<0){
			vy = -vy;
			double diff = ball.getY();
			ball.move( 0 , -2*diff );
		}
		
		// For collision with BOTTOM WALL
		else if(ball.getY()+2*BALL_RADIUS > HEIGHT){
			remove(ball);
			NTL--; // Decreases the no. of turns left by one.
			endTurn++;
		}
	}
	
/* Method: checkForCollisionWithPaddle() */
/** Checks for collision with paddle and acts accordingly. */
	private void checkForCollisionWithPaddle(){
		double xPaddle = paddle.getX();// Stores the x coordinate of the paddle.
		
		/* Checks whether the bottom point of ball is below paddle but not
		 *  more than half of paddle height*/
		if((ball.getY() + 2*BALL_RADIUS) > paddle.getY() && (ball.getY() + 2*BALL_RADIUS) < paddle.getY() + PADDLE_HEIGHT/2){
			//Checks with bottom left and right corner
			if( ball.getX() >xPaddle  && ball.getX()<xPaddle + PADDLE_WIDTH 
					||ball.getX() + 2*BALL_RADIUS >xPaddle  &&ball.getX() + 2*BALL_RADIUS<xPaddle + PADDLE_WIDTH ){
				vy = -vy; // Inverts Velocity
				double diff = ball.getY()+2*BALL_RADIUS -paddle.getY();
				ball.move(0, -2*diff);
				bounceClip.play();
			}
		}
	}

/* Method: checkForCollisionWithBrick()*/
/** Checks for collision with bricks and acts accordingly.*/
	private void checkForCollisionWithBrick(){
		
		//Store the coordinates of the four corners of the ball.
		double xBallTopLeft = ball.getX();
		double yBallTopLeft = ball.getY();
		
		double xBallTopRight = ball.getX() + 2*BALL_RADIUS;
		double yBallTopRight = ball.getY();
		
		double xBallBottomLeft = ball.getX();
		double yBallBottomLeft = ball.getY() + 2*BALL_RADIUS;
		
		double xBallBottomRight = ball.getX() + 2*BALL_RADIUS;
		double yBallBottomRight = ball.getY() + 2*BALL_RADIUS;
		
		
		
		checkCollidingObject(xBallTopLeft , yBallTopLeft);
		if(nBricksBroken==0)
		checkCollidingObject(xBallTopRight , yBallTopRight);
		else if(nBricksBroken==0)
		checkCollidingObject(xBallBottomLeft , yBallBottomLeft);
		else if(nBricksBroken==0)
		checkCollidingObject(xBallBottomRight , yBallBottomRight);
				
	}
	
/* Method: checkCollidingObject() */
/** Checks if brick is present at given point.
 * @param x Stores the x coordinate of the point to be checked
 * @param y Stores the y coordinate of the point to be checked */
	private void checkCollidingObject(double x, double y){
		if(getElementAt(x,y) != paddle && getElementAt(x,y) != null){
			GObject brick = getElementAt(x,y);
			remove(brick);
			vy=-vy;
			NBL--;
			nBricksBroken++;
			bounceClip.play();
		}
	}
	
/* Method: message() */
/** Displays the no. of turns & bricks left. */
  	private void message(){
  		message = new GLabel("No. of turns left:" +NTL +" No. of Bricks left:" +NBL);
  		message.setFont("London-18");
  		add(message, (WIDTH-message.getWidth())/2 , (HEIGHT-message.getAscent())/2 );
  	}
  	
/** Object of the class Colour stores a dummy Colour variable. */
	Color color=new Color(0,0,0);

/** Instance variable for the Paddle */
	GRect paddle;
	
/** Instance variable for the ball */
	GOval ball;

/** X Velocity of the ball in pixels/cycle */
	private double vx;
	
/** Y Velocity of the ball in pixels/cycle */
	private double vy = 3;

/** Random Generator */
	private RandomGenerator rgen = new RandomGenerator();
	
/** Counter for ending the turn */
	private int endTurn;
	
/* Counter for number of bricks broken in one move */
	private int nBricksBroken=0;
	
/* Displays no. of bricks & turns left */
	private GLabel message;
	
/** Imports the bounce audio clip */
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
}