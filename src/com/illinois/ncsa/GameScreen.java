package com.illinois.ncsa;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;
import framework.impl.AndroidGame;
import framework.math.Vector2;

@SuppressLint("NewApi")
public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver,
        GameWin
    }
    
    Graphics g = game.getGraphics();
    
    public final int[][][] allExes = {
    		{{0},{1,2,3},{2,5},{}},
    		{{1,6},{3},{0,7},{4}},
    		{{1,4,5},{},{1},{0,6}},
    		{{7},{3,6},{7},{0,7}},
    		{{6},{0},{2,3,7},{7}},
    		{{1},{2,4},{0},{3,6}},
    		{{0,1,2},{4},{0,2},{}},
    		{{6},{},{2,4,5,7},{4}},
    		{{3,5,7},{5},{5},{7}},
    		{{1,2,7},{5},{},{1,3}},
    		{{1,7},{1},{5},{0,2}},
    		{{0},{3},{0,1},{0,7}}
    };
    //load all 12 levels
    public final int[][] allShapes = {
    		{10,1,0,10,10,1,2,8,4,6,1,3,1,9,2,3,6,9,6,8,1,3,4,0,1,4,1,10,1,10},
    		{10,1,0,6,8,3,2,0,9,4,6,6,10,5,8,7,1,5,9,4,6,7,1,1,4,9,1,5,10,1,10},
    		{6,4,7,3,10,9,2,6,0,1,1,3,3,0,4,8,8,10,4,8,2,3,3,4,2,4,9,9,10,1,10},
    		{1,6,6,4,9,10,0,6,1,6,3,3,7,2,10,2,2,2,10,4,3,5,10,5,9,7,1,1,10,1,10},
    		{0,6,8,4,10,10,2,9,2,10,4,2,7,7,7,2,6,7,4,8,4,3,4,6,3,10,1,10,10,1,10},
    		{1,2,3,0,2,3,10,6,0,8,1,8,2,1,9,6,2,2,8,2,0,4,1,7,3,1,9,2,10,1,10},
    		{1,2,3,0,2,3,10,6,0,8,1,8,2,1,9,6,2,2,8,2,0,4,1,7,3,1,9,2,10,1,10},
    		{2,1,4,5,6,0,8,6,3,5,7,2,3,7,1,0,1,8,0,9,6,9,1,7,0,3,8,8,10,1,10},
    		{3,0,5,0,9,8,6,7,6,2,1,8,5,2,4,4,1,3,2,5,0,2,1,7,7,6,7,4,10,1,10},
    		{4,10,6,5,2,5,4,8,8,10,6,2,7,8,7,9,1,9,5,1,6,7,0,7,4,9,6,0,10,1,10},
    		{5,10,6,0,5,3,2,8,0,7,1,8,9,3,10,2,1,4,7,9,0,1,0,7,1,0,5,6,10,1,10},
    		{6,9,7,5,9,0,0,9,3,4,6,2,0,9,2,7,0,10,9,5,6,5,10,7,9,3,5,2,10,1,10}
    };
    
    
    public final int SCREEN_HEIGHT = g.getHeight();
    public final int SCREEN_WIDTH = g.getWidth();
    
    public final int SPRITE_SPACING = 50;
    public final float QUEUE_SCALE = .3f;
    public final int AE = SCREEN_HEIGHT / 16; //ALLOWED ERROR FOR PLACING SPRITES
    public final int MARGIN = SCREEN_WIDTH*19/480;
    public final int PROCESSOR_SPACING = (2*SPRITE_SPACING + SCREEN_WIDTH*16/480);
    public final int TOP_MARGIN = SCREEN_HEIGHT*73/320;
    public float runningtime;
    public int[][] occupied;
    public boolean pauseIn = false;
    public int level;
    public boolean endless;
    public boolean finishnext = false;
    public int highestScore;
    int currScore;
    boolean winDrawn;
    
    GameState state = GameState.Ready;
    World world;
    float accumulator = 0f;
    float acc2 = 0f;
    
    public GameScreen(Game game, int l, boolean e) {
        super(game);
        level = l;
        world = new World(SCREEN_WIDTH,SCREEN_HEIGHT, allShapes[level]);
        occupied = allExes[l];
        endless = e;
        winDrawn = false;
    	for(int i=0; i<occupied.length; i++){
    		for(int j=0; j<occupied[i].length; j++){
    			world.cores[i][(occupied[i][j])] = World.XED;
    		}
    	}
    }

    @Override
    public void update(float deltaTime) {
    	
    	
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        if(state == GameState.Ready)
            updateReady(touchEvents);
        if(state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if(state == GameState.Paused)
            updatePaused(touchEvents);
        if(state == GameState.GameOver)
            updateGameOver(touchEvents);
        if(state == GameState.GameWin)
            updateGameWin(touchEvents); 
    }
    
    private void updateReady(List<TouchEvent> touchEvents) {

    }
    
    int initialTouchx=0;
    int initialTouchy=0;
    int initialJobx=0;
    int initialJoby=0;
    boolean tUp = true;
    boolean tDragged = false;
    boolean jobPressed = false;
    Job currJob = null;
    boolean startedright = false;
    
    private Vector2 onValidCore(Vector2 loc){
    	int processor;
    	int x,y;
    	int core;
    	if(loc.x<(MARGIN - AE) || loc.x>(MARGIN+3*PROCESSOR_SPACING+SPRITE_SPACING+AE) || loc.y<(TOP_MARGIN - AE) || loc.y>(SCREEN_HEIGHT*(248+AE)/320))
    		return null;
    	if(loc.x<(MARGIN+SPRITE_SPACING+AE) && loc.x>(MARGIN-AE))
    		processor = 0;
    	else if (loc.x<(MARGIN+PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+PROCESSOR_SPACING-AE))
    		processor = 1;
    	else if (loc.x<(MARGIN+2*PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+2*PROCESSOR_SPACING-AE))
    		processor = 2;
    	else if (loc.x<(MARGIN+3*PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+3*PROCESSOR_SPACING-AE))
    		processor = 3;
    	else 
    		return null;
    	
    	x = Math.round((loc.x - (MARGIN + processor * PROCESSOR_SPACING)) / SPRITE_SPACING);
    	//float test = loc.y - TOP_MARGIN;
    	y = Math.round((loc.y - TOP_MARGIN) / SPRITE_SPACING);
    	core = x + 2 * y;
    	
    	int[] states = new int[8];
    	for(int i=0; i<8; i++){
    		states[i] = world.cores[processor][i];
    	}
    	return new Vector2(processor,core);
    	
    }

    
    
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {        

    	int len = touchEvents.size();
        //List<Vector2> locations; f

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            switch(event.type){
            
     
            
            case TouchEvent.TOUCH_UP:
            	if(event.y<TOP_MARGIN-30&&startedright){
            		for(int l=0; l<world.queue.getSize();l++){
            			Job j = world.queue.getJobinPosition(l);
            			if(j.onJob(event.x, event.y, SPRITE_SPACING)){
            				j.isSelected = false;
            				jobPressed = false;
            				j.rotate();
                    		tUp = true;
                    		break;
            			}
            		}
            	}
            	else if(inBounds(event, 0,270,52,52)){
            		state = GameState.Paused;
                	g.clear(Color.BLACK);
                	g.drawPixmap(Assets.cores, 0,0);
                    g.drawRect(0,0, SCREEN_WIDTH+1,SCREEN_HEIGHT+1, 0xdd000000);
                    g.drawText("RESUME", 110, 170, 0xFFFFFFFF, 30);
                	g.drawText("MENU", 299, 170, 0xFFFFFFFF, 30);
            		
            	}
            	else{
	            	tUp = true;
	            	jobPressed = false;
	            	
	            	if(currJob!=null && startedright){
	            		//Sprite currSprite;
	            		//Vector2 spriteLocation = new Vector2();
	            		boolean canBePlaced = true;
	            		int p = -1;
	            		int c = -1;
	            		int xloc;
	            		int yloc;
	            		int xoff;
	            		int yoff;
	            		Vector2 firstSpritePos = onValidCore(currJob.position);
	            		//TODO: Change this to the standard method to find core/processor 
	//            		for(int j=0; j<currJob.getNumPieces(); j++){
	//            			currSprite = currJob.getSprite(j);
	//            			spritePos = currSprite.getPos();
	//            			spriteLocation.set( (spritePos%2)*(SPRITE_SPACING)+ currJob.position.x, (SPRITE_SPACING*(spritePos/2))+currJob.position.y );
	//            			spriteLocation = onValidCore(spriteLocation);
	//
	//            			if(spriteLocation==null){
	//            				canBePlaced = false;
	//            				break;
	//            			}
	//            			else{            			
	//            				x = (int)spriteLocation.x;
	//            				y = (int)spriteLocation.y;
	//            				
	//            				if(y>7 || y<0 || x<0 || x>3 || (world.cores[x][y]!=World.FREE)){
	//            					canBePlaced = false;
	//            					break;
	//            				}
	//            			}
	//            		}
	            		if(firstSpritePos==null){
	            			canBePlaced = false;
	            		}
	            		else{
	            			p = (int)firstSpritePos.x;
	            			c = (int)firstSpritePos.y;
	            			
	            			if(c%2!=0 && currJob.width==2){
	            				canBePlaced=false;
	            			}
	            			else{
		            			for(int j=0; j<currJob.getNumPieces(); j++){
		            				int currspritepos = c + currJob.getSprite(j).getPos();
			            			if( currspritepos>7 || currspritepos<0 || p<0 || p>3 || (world.cores[p][currspritepos]!=World.FREE)){
			        					canBePlaced = false;
			        				}
		            			}
	            			}
	            			
	            		}
	            		
	            		currJob.isSelected = false;
	            		if(canBePlaced){
	            			//updateStates(World.PROCESSING);
	            			xoff = c % 2;
	            			yoff = c / 2;
	            			
	                		xloc = MARGIN + ( p * PROCESSOR_SPACING ) + xoff*SPRITE_SPACING;
	                		yloc = TOP_MARGIN + yoff*SPRITE_SPACING;
	                		currJob.setPos(xloc, yloc);
	            			world.runJob(currJob,p,c);
	            			currJob = null;
	            		}
	            		
	            	}
            	}
            
            		
            	break;

            case TouchEvent.TOUCH_DOWN:
            		if(event.y>TOP_MARGIN-30){
            			if(inBounds(event, 0,270,52,52))
            				pauseIn = true;
            			else
            				startedright = false;
            		}
            		else
            			startedright = true;
            	break;
            	
            	
            case TouchEvent.TOUCH_DRAGGED:
            	
            	if(tUp){
            		initialTouchx = event.x;
            		initialTouchy = event.y;
            		
	        		for(int k=0; k<world.queue.getSize(); k++){
	        			Job j = world.queue.getJobinPosition(k);
	        			if(j.onJob(event.x, event.y, SPRITE_SPACING) == true){
	        				currJob = j;
	        				currJob.isSelected = true;
	        				jobPressed = true;
	        				initialJobx = (int)j.position.x;
	        				initialJoby = (int)j.position.y;
	        				break;
	        			}
	        			else 
	        				jobPressed = false;
	        		}
	        		
            		tUp = false;
            	}
        		
            	if(jobPressed){
            		currJob.position.x = initialJobx + event.x - initialTouchx;
            		currJob.position.y = initialJoby + event.y - initialTouchy;
            	}
            	break;
            
            }
        }
        
        world.update(deltaTime);
        
        
        if(world.gameOver) {
        	state = GameState.GameOver;
        	SharedPreferences sp = ((AndroidGame) game ).getContext().getSharedPreferences("gameScore", 0);
        	highestScore = endless? sp.getInt("endlessHigh",0) : sp.getInt("highestScore"+level, 0);
        	currScore = endless? world.score + sp.getInt("currEndless", 0) : world.score;
        	if(endless){
        		Editor e = sp.edit();
        		if(currScore>highestScore){
        			e.putInt("endlessHigh", currScore);
        		}
        		e.putInt("currEndless", 0);
        		e.putInt("elev", 0);
        		e.commit();
        	}
        	else{

        	}
        }
        else if(world.timeUp){
        	SharedPreferences sp = ((AndroidGame) game ).getContext().getSharedPreferences("gameScore", 0);
        	highestScore = endless? sp.getInt("endlessHigh",0) : sp.getInt("highestScore"+level, 0);
        	currScore = endless? world.score + sp.getInt("currEndless", 0) : world.score;
        	Editor e = sp.edit();
        	if(!endless){
        		if(currScore>highestScore){
        			e.putInt("highestScore"+level, currScore);
        			e.commit();
        		}
        	}
        	else{
        		e.putInt("elev", level+1);
        		e.putInt("currEndless", currScore);
        	
        		e.commit();
        	}
        	state = GameState.GameWin;
        }
    }
    
    private void updatePaused(List<TouchEvent> touchEvents) {
    	//g.drawText("MENU", 299, 170, 0xFFFFFFFF, 30);
        int len = touchEvents.size();
        
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 70 && event.x <= 259) {
                    if(event.y > 136 && event.y <= 186) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        state = GameState.Running;
                        return;
                    }
                }
                    if(event.x > 259 && event.x <= 419) {
                        if(event.y > 136 && event.y <= 176) {
                            if(Settings.soundEnabled)
                                Assets.click.play(1);
                            game.setScreen(new MainMenuScreen(game));
                            return;
                        }
                    }
            }
        }
    }
    
    private void updateGameOver(List<TouchEvent> touchEvents) {
    	
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event,130,252,100,30)&&!endless) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new GameScreen(game,level,endless));
                    return;
                }
                if(inBounds(event,240,252,100,30)){
                	if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
            
        }
//        g.drawRect(130, 252, 100, 30, Color.BLACK);
//        g.drawRect(240, 252, 100, 30, Color.BLACK);
    }
    
    
    private void updateGameWin(List<TouchEvent> touchEvents){
    	
    	
    	//handle touchevents

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
            	if(!finishnext){
	                if(inBounds(event,438,280,32,32)) {
	                    finishnext=true;
	                }
            	}
            	else{
	                if(inBounds(event,10,280,32,32)) {
	                    finishnext=false;
	                }
	                else if(inBounds(event,170,179,137,26)){
	                	if(level==11)
	                		level=-1;
	                    game.setScreen(new GameScreen(game,level+1,endless));
	                }
					else if(inBounds(event,188,209,106,26) && !endless){
							game.setScreen(new GameScreen(game,level,endless));
					}
					else if(inBounds(event,202,239,80,26)){
						game.setScreen(new MainMenuScreen(game));
					}
//	                g.drawRect(170, 179, 137, 26, 0x33ff0000);
//	            	g.drawRect(188, 209, 106, 26, 0x33ff0000);
//	            	g.drawRect(202, 239, 80, 26, 0x33ff0000);
            	}
                
                
            }
        }
    }
    

    @Override
    public void present(float deltaTime) {

        if(state == GameState.Ready) 
            drawReadyUI(deltaTime);
        if(state == GameState.Running)
            drawRunningUI();
        if(state == GameState.Paused)
            drawPausedUI();
        if(state == GameState.GameOver)
            drawGameOverUI(deltaTime);
        if(state == GameState.GameWin)
            drawGameWinUI();         
    }
    
    private void drawWorld(World world){
    	drawQueue(world);
    	drawTimeline(world);
        g.drawPixmap(Assets.nav, 10, 280, 64, 0, 32, 32); 
    	drawScore(world);
    }
    
    private void drawScore(World world){
    	g.drawText(String.valueOf(world.score) + " TFLOPS", 280, 310, Assets.neonBlue, 28);
    }
    
    private void drawTimeline(World world){
    	g.drawText("LVL "+ (level+1), 115, 290, 0x33ffffff, 22);
    	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), (int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
    	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1 + (Assets.timeline.getWidth()*.85)*(world.time/60)), (int)(SCREEN_HEIGHT*.9), (int)(Assets.timeline.getWidth()*.933),0,(int)(Assets.timeline.getWidth()*.063), Assets.timeline.getHeight());
    }
    
    private void drawQueue(World world) {

    	Queue queue = world.queue;
    	Job cJob;
    	Job sJob = null;
    	for(int i=0; i<queue.getSize(); i++){
    		cJob = queue.getJobinPosition(i);
    		if(cJob.isSelected){
    			sJob = cJob;
    			drawJob(sJob,i);
    			continue;
    		}
    		drawJob(cJob,i);
    	}
    }
    
    private void drawRunningJobs(World world){
    	RunningJobQueue rq = world.rQueue;
    	RunningJob crJob;
    	
    	for(int i=0; i<rq.getSize(); i++){
    		crJob = rq.getJobinPosition(i);
    		drawJob(crJob, i);
    	}
    }
    
    
    private void drawJob(Job cJob, int i){
    	Graphics g = game.getGraphics();
		float scale = cJob.scaleFactor;
		if(cJob.isSelected || cJob.isProcessing)
			scale = 1f;
		else
			if(cJob.position.x > MARGIN + (int)(i*3.4*SPRITE_SPACING*QUEUE_SCALE) + SCREEN_WIDTH*15/480){
				int move = SCREEN_WIDTH*18/480;
				cJob.setPos(cJob.position.x-move, 0);
			}
			else
				cJob.setPos( MARGIN + (int)(i*3.4*SPRITE_SPACING*QUEUE_SCALE), 0 );
		
		switch(cJob.type){
		case Job.CHEMISTRY:
			for(int j=0; j < cJob.numPieces; j++){
				Sprite currSprite = cJob.getSprite(j);
				int xoffset = (int)((currSprite.position % 2) * SPRITE_SPACING * scale);
				int yoffset = (int)((currSprite.position / 2) * SPRITE_SPACING * scale);
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset, 138, 9, 45, 45, scale);
			}
			break;
			
		case Job.ENGINEERING:
			for(int j=0; j < cJob.numPieces; j++){
				Sprite currSprite = cJob.getSprite(j);
				int xoffset = (int)((currSprite.position % 2) * SPRITE_SPACING * scale);
				int yoffset = (int)((currSprite.position / 2) * SPRITE_SPACING * scale);
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset, 266, 9, 45, 45, scale);
			}
			break;
			
		case Job.EPIDEMIC:
			for(int j=0; j < cJob.numPieces; j++){
				Sprite currSprite = cJob.getSprite(j);
				int xoffset = (int)((currSprite.position % 2) * SPRITE_SPACING * scale);
				int yoffset = (int)((currSprite.position / 2) * SPRITE_SPACING * scale);
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset, 74, 9, 45, 45, scale);
			}
			break;
			
		case Job.GALAXY:
			for(int j=0; j < cJob.numPieces; j++){
				Sprite currSprite = cJob.getSprite(j);
				int xoffset = (int)((currSprite.position % 2) * SPRITE_SPACING * scale);
				int yoffset = (int)((currSprite.position / 2) * SPRITE_SPACING * scale);
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset, 10, 9, 45, 45, scale);
			}
			break;
			
		case Job.STORM:
			for(int j=0; j < cJob.numPieces; j++){
				Sprite currSprite = cJob.getSprite(j);
				int xoffset = (int)((currSprite.position % 2) * SPRITE_SPACING * scale);
				int yoffset = (int)((currSprite.position / 2) * SPRITE_SPACING * scale);
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset, 202, 9, 45, 45, scale);
			}
			break;
		
		}
		
    }
    
    private void drawReadyUI(float deltaTime) {
        int h = SCREEN_HEIGHT*(-1);
        
        int i = (int)(accumulator*200 + h)-50;
        
        if(i<0){
            if(i<h){
            	g.clear(Color.BLACK);
            	g.drawPixmap(Assets.cores,0,0);
            	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), (int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
            	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1 + (Assets.timeline.getWidth()*.85)), (int)(SCREEN_HEIGHT*.9), (int)(Assets.timeline.getWidth()*.933),0,(int)(Assets.timeline.getWidth()*.063), Assets.timeline.getHeight());
            	accumulator += deltaTime;
            }
            else{
	        	g.clear(Color.BLACK);
	        	g.drawPixmap(Assets.cores,0,i);
	        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), i+(int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
	        	g.drawPixmap(Assets.cores,0,i-h);
	        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), i-h+(int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
	        	accumulator += deltaTime;
            }
        }
        else{
        	g.clear(Color.BLACK);
        	g.drawPixmap(Assets.cores,0,0);
        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.07), (int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
        	state = GameState.Running;
        	accumulator = 0;
        }
        	
        
    }
    
    private void drawRunningUI() {
    	g.clear(Color.BLACK);
        drawRunningJobs(world);
        drawOccupied(world);
        g.drawPixmap(Assets.cores,0,0);
        drawQueueColor(world);
    	drawWorld(world);

    }
    
    private void drawOccupied(World w){
    	for(int i=0; i<occupied.length; i++){
    		for(int j=0; j<occupied[i].length; j++){
    			int xoffset = MARGIN + i*PROCESSOR_SPACING + ((occupied[i][j] % 2) * SPRITE_SPACING);
				int yoffset = TOP_MARGIN + ((occupied[i][j] / 2) * SPRITE_SPACING);
				g.drawPixmap(Assets.tasks, xoffset , yoffset, 458, 9, 45, 45, 1);
    		}
    	}
    }
    
    private void drawQueueColor(World world){
    	int color;
    	int r;
    	int b;
    	int green;
    	int a;
    	
    	a = 0xff;
    	r = (int)((world.queue.getSize())*168/8);
    	green = 100 - (int)((world.queue.getSize())*32/8);
    	b = 119 - (int)((world.queue.getSize())*57/8);
    	
    	color = (b | (green<<8) | (r<<16) | (a<<24));
    	
    	g.drawRect(0, 0, SCREEN_WIDTH + 1, TOP_MARGIN - 30, color);
    }
    
    private void drawPausedUI() {



    }
    boolean gameoverscreendone = false;
    private void drawGameOverUI(float deltaTime) {
    	
    	if(!gameoverscreendone){
    	int h = SCREEN_HEIGHT;
        
        int i = (int)(200*accumulator);
        
        if(i<h){
            	g.drawRect(0,0, SCREEN_WIDTH,SCREEN_HEIGHT, Color.BLACK);
            	g.drawPixmap(Assets.cores,0,i);
            	g.drawPixmap(Assets.failure, 0, i-h);
            	accumulator += deltaTime;
        }
        else{
        	g.drawPixmap(Assets.failure, 0, 0);
        	if(endless)
        		g.drawText("RETRY", 140, 280, 0x33000000, 26);
        	else
        	g.drawText("RETRY", 140, 280, 0xFFFFFFFF, 26);
        	
        	g.drawText("MENU", 270, 280, 0xFFFFFFFF, 26);
        }
        }
    	else{}
        

        	
        
    }
    
    private void drawGameWinUI() {
    	
    	g.clear(Color.BLACK);
    	int galaxy=world.finishedJobs.numGalaxy, epidemic=world.finishedJobs.numEpidemic, chemistry=world.finishedJobs.numChemistry, storm=world.finishedJobs.numStorm, engineering=world.finishedJobs.numEngineering;
    	if(finishnext){
    		g.drawPixmap(Assets.cores, 0,0);
            g.drawRect(0,0, SCREEN_WIDTH+1,SCREEN_HEIGHT+1, 0xdd000000);

        	
        	g.drawText("LEVEL " + (level+1) + " COMPLETE!", 77, 60, Assets.neonBlue, 32);
        	g.drawText("SCORE: " + currScore, 135, 100, Assets.neonBlue, 32);
        	g.drawText("PREV HIGH: " + highestScore, 81, 140, Assets.neonBlue, 32);
        	
            g.drawText("CONTINUE", 180, 200, 0xFFFFFFFF, 24);
            if(!endless)
            	g.drawText("REPLAY", 198, 230, 0xFFFFFFFF, 24);
            else
            	g.drawText("REPLAY", 198, 230, 0xdd000000, 24);

        	g.drawText("MENU", 212, 260, 0xFFFFFFFF, 24);
        	
//        	g.drawRect(170, 179, 137, 26, 0x33ff0000);
//        	g.drawRect(188, 209, 106, 26, 0x33ff0000);
//        	g.drawRect(202, 239, 80, 26, 0x33ff0000);
        	
	        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);

    	}
    	else{
	        g.drawText("Supercomputers drive science! You Completed:", 18, 35, Assets.neonBlue, 18);
	    	drawMono();
	    	g.drawText(galaxy + " Galaxy Simulations", 70, 85, Assets.neonBlue, 20);
	    	g.drawText(epidemic + " Epidemic Simulations", 70, 130, Assets.neonBlue, 20);
	    	g.drawText(chemistry + " Chemistry Simulations", 70, 173, Assets.neonBlue, 20);
	    	g.drawText(storm + " Storm Simulations", 70, 220, Assets.neonBlue, 20);
	    	g.drawText(engineering + " Engineering Simulations", 70, 265, Assets.neonBlue, 20);
	    	
	        g.drawPixmap(Assets.nav, 438, 280, 32, 0, 32, 32); 
    	}
    	
    	

    }
    
    private void drawMono(){
    	float scale = 0.9f;
    	g.drawPixmap(Assets.tasksMono, 20, 60, 10, 9, 45, 45, scale);
    	g.drawPixmap(Assets.tasksMono, 20, 105, 74, 9, 45, 45, scale);
    	g.drawPixmap(Assets.tasksMono, 20, 150, 138, 9, 45, 45, scale);
    	g.drawPixmap(Assets.tasksMono, 20, 195, 202, 9, 45, 45, scale);
    	g.drawPixmap(Assets.tasksMono, 20, 240, 266, 9, 45, 45, scale);

    }
    
    @Override
    public void pause() {
//        if(state == GameState.Running)
//            state = GameState.Paused;
        
        //if(world.gameOver) {
        //    Settings.addScore(world.score);
        //    Settings.save(game.getFileIO());
       // }
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }
}