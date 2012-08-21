package com.illinois.ncsa;

import java.util.List;

import android.graphics.Color;
import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;
import framework.math.Vector2;

public class GameScreen extends Screen {
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }
    
    Graphics g = game.getGraphics();
    public final int SCREEN_HEIGHT = g.getHeight();
    public final int SCREEN_WIDTH = g.getWidth();
    
    public final int SPRITE_SPACING = 50;
    public final float QUEUE_SCALE = .3f;
    public final int AE = SCREEN_HEIGHT / 16; //ALLOWED ERROR FOR PLACING SPRITES
    public final int MARGIN = SCREEN_WIDTH*19/480;
    public final int PROCESSOR_SPACING = (2*SPRITE_SPACING + SCREEN_WIDTH*16/480);
    public final int TOP_MARGIN = (SCREEN_HEIGHT*73/320);
    public float runningtime;

    
    GameState state = GameState.Ready;
    World world;
    //int oldScore = 0;
    //String score = "0";
    float accumulator = 0f;
    float acc2 = 0f;
    
    public GameScreen(Game game) {
        super(game);
        world = new World(SCREEN_WIDTH,SCREEN_HEIGHT);
        System.out.print("height: " + SCREEN_HEIGHT + " width: " + SCREEN_WIDTH);
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
    }
    
    private void updateReady(List<TouchEvent> touchEvents) {

    }
    
    int initialTouchx=0;
    int initialTouchy=0;
    int initialJobx=0;
    int initialJoby=0;
    boolean tUp = true;
    boolean jobPressed = false;
    Job currJob = null;
    
    
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
    	System.out.println(processor + ", " + core);

    	return new Vector2(processor,core);
    	
    }
    private void updateStates(int newState){
    	int processor;
    	Vector2 loc = currJob.position;
    	if(loc.x<(MARGIN - AE) || loc.x>(MARGIN+3*PROCESSOR_SPACING+SPRITE_SPACING+AE) || loc.y<(TOP_MARGIN - AE) || loc.y>(SCREEN_HEIGHT*(248+AE)/320))
    		return;
    	if(loc.x<(MARGIN+SPRITE_SPACING+AE) && loc.x>(MARGIN-AE))
    		processor = 0;
    	else if (loc.x<(MARGIN+PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+PROCESSOR_SPACING-AE))
    		processor = 1;
    	else if (loc.x<(MARGIN+2*PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+2*PROCESSOR_SPACING-AE))
    		processor = 2;
    	else if (loc.x<(MARGIN+3*PROCESSOR_SPACING+SPRITE_SPACING+AE) && loc.x>(MARGIN+3*PROCESSOR_SPACING-AE))
    		processor = 3;
    	else 
    		return;
    	for(int i=0; i<currJob.getNumPieces();i++){
    		Sprite currSprite = currJob.getSprite(i);
    		world.cores[processor][currSprite.position] = newState;
    	}
    }
    
    
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {        

    	int len = touchEvents.size();
        //List<Vector2> locations; f

        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            
            switch(event.type){
            
            case TouchEvent.TOUCH_UP:
            	tUp = true;
            	jobPressed = false;
            	
            	if(currJob!=null){
            		Sprite currSprite;
            		Vector2 spriteLocation = new Vector2();
            		int spritePos;
            		boolean canBePlaced = true;
            		int x = -1;
            		int y = -1;
            		int xloc;
            		int yloc;
            		int xoff;
            		int yoff;
            		
            		for(int j=0; j<currJob.getNumPieces(); j++){
            			currSprite = currJob.getSprite(j);
            			spritePos = currSprite.getPos();
            			spriteLocation.set( (spritePos%2)*(SPRITE_SPACING)+ currJob.position.x, (SPRITE_SPACING*(spritePos/2))+currJob.position.y );
            			spriteLocation = onValidCore(spriteLocation);

            			if(spriteLocation==null){
            				canBePlaced = false;
            				break;
            			}
            			else{            			
            				x = (int)spriteLocation.x;
            				y = (int)spriteLocation.y;
            				
            				if(y>7 || y<0 || x<0 || x>3 || (world.cores[x][y]!=World.FREE)){
            					canBePlaced = false;
            					break;
            				}
            			}
            		}
            		
            		if(canBePlaced){
            			updateStates(World.PROCESSING);
            			Vector2 firstSpritePos = onValidCore(currJob.position);
            			int fProcessor = (int)firstSpritePos.x;
            			int fCore = (int)firstSpritePos.y;
            			xoff = fCore % 2;
            			yoff = fCore / 2;
            			
                		xloc = MARGIN + ( fProcessor * PROCESSOR_SPACING ) + xoff*SPRITE_SPACING;
                		yloc = TOP_MARGIN + yoff*SPRITE_SPACING;
                		currJob.setPos(xloc, yloc);
            			world.runJob(currJob,fProcessor,fCore);
            		}
            		currJob.isSelected = false;
            	}
            	break;

            case TouchEvent.TOUCH_DOWN:

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
           // state = GameState.GameOver;
        }
    }
    
    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 80 && event.x <= 240) {
                    if(event.y > 100 && event.y <= 148) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        state = GameState.Running;
                        return;
                    }                    
                    if(event.y > 148 && event.y < 196) {
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
                if(event.x >= 128 && event.x <= 192 &&
                   event.y >= 200 && event.y <= 264) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
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
            drawGameOverUI();
      // Integer blahscore = world.score;
       //Log.e("score:", blahscore.toString());
        //drawText(g, world.score, g.getWidth() / 2 - 200, g.getHeight() - 50);                
    }
    
    private void drawWorld(World world){
    	drawQueue(world);
    	drawTimeline(world);
    }
    
    private void drawTimeline(World world){
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
				int move = SCREEN_WIDTH*15/480;
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
				g.drawPixmap(Assets.tasks, (int)cJob.position.x + xoffset , (int)cJob.position.y + yoffset,202, 9, 45, 45, scale);
			}
			break;
		
		}
		
    }
    
    private void drawReadyUI(float deltaTime) {
    	Graphics g = game.getGraphics();
        int h = SCREEN_HEIGHT*(-1);
        
        int i = (int)(accumulator*200 + h)-50;
        
        if(i<0){
            if(i<h){
            	g.drawRect(0,0, SCREEN_WIDTH,SCREEN_HEIGHT, Color.BLACK);
            	g.drawPixmap(Assets.cores,0,0);
            	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), (int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
            	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1 + (Assets.timeline.getWidth()*.85)), (int)(SCREEN_HEIGHT*.9), (int)(Assets.timeline.getWidth()*.933),0,(int)(Assets.timeline.getWidth()*.063), Assets.timeline.getHeight());
            	accumulator += deltaTime;
            }
            else{
	        	g.drawRect(0,0, SCREEN_WIDTH,SCREEN_HEIGHT, Color.BLACK);
	        	g.drawPixmap(Assets.cores,0,i);
	        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), i+(int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
	        	g.drawPixmap(Assets.cores,0,i-h);
	        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.1), i-h+(int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
	        	accumulator += deltaTime;
            }
        }
        else{
        	g.drawRect(0,0, SCREEN_WIDTH,SCREEN_HEIGHT, Color.BLACK);
        	g.drawPixmap(Assets.cores,0,0);
        	g.drawPixmap(Assets.timeline, (int)(SCREEN_WIDTH*.07), (int)(SCREEN_HEIGHT*.9), 0,0, (int)(Assets.timeline.getWidth()*.933), Assets.timeline.getHeight());
        	state = GameState.Running;
        }
        	
        
    }
    
    private void drawRunningUI() {
    	Graphics g = game.getGraphics();
        g.drawRect(0,0, SCREEN_WIDTH,SCREEN_HEIGHT, Color.BLACK);
        drawRunningJobs(world);
        g.drawPixmap(Assets.cores,0,0);
    	drawWorld(world);

    }
    
    private void drawPausedUI() {
    	Graphics g = game.getGraphics();
        g.drawPixmap(Assets.cores, 0,0);
    }

    private void drawGameOverUI() {
    	Graphics g = game.getGraphics();
        g.drawPixmap(Assets.failure, 0,0);

    }
    
    
    @Override
    public void pause() {
        if(state == GameState.Running)
            state = GameState.Paused;
        
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
}