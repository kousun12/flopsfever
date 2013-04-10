package com.illinois.ncsa;

import java.util.List;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;

public class LevelScreen extends Screen{
	boolean[] selected = new boolean[12];
	int currSelected;
	
	 public LevelScreen(Game game) {
	        super(game);
	        game.getInput().getTouchEvents().clear();
	    }

	    @Override
	    public void update(float deltaTime) {
	        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	        game.getInput().getKeyEvents();
	        int len = touchEvents.size();
	        
	        for(int i=0; i<len; i++){
	        	TouchEvent event = touchEvents.get(i);
	        	
	        		if(event.type==TouchEvent.TOUCH_DOWN){
	        		//start at 65,130 and go 60px down/right
	                if(inBounds(event, 65, 130, 60, 60) ) {
	                    currSelected = 0;
	                }
	                else if(inBounds(event, 125, 130, 60, 60) ) {
	                	currSelected = 1;
		                }
	                else if(inBounds(event, 185, 130, 60, 60) ) {
	                	currSelected = 2;
		                }
	                else if(inBounds(event, 245, 130, 60, 60) ) {
	                	currSelected = 3;
		                }
	                else if(inBounds(event, 305, 130, 60, 60) ) {
	                	currSelected = 4;
		                }
	                else if(inBounds(event, 365, 130, 60, 60) ) {
	                	currSelected = 5;
		                }
	                else if(inBounds(event, 65, 190, 60, 60) ) {
	                	currSelected = 6;
		                }
	                else if(inBounds(event, 125, 190, 60, 60) ) {
	                	currSelected = 7;
		                }
	                else if(inBounds(event, 185, 190, 60, 60) ) {
	                	currSelected = 8;
		                }
	                else if(inBounds(event, 245, 190, 60, 60) ) {
	                	currSelected = 9;
		                }
	                else if(inBounds(event, 305, 190, 60, 60) ) {
	                	currSelected = 10;
		                }
	                else if(inBounds(event, 365, 190, 60, 60) ) {
	                	currSelected = 11;
		                }
	                selected[currSelected] = true;
	            }
	        	
	        	if(event.type == TouchEvent.TOUCH_UP){
	        		if( inBounds(event, 0,270,42,42) ) {
	        			//game.setScreen(new MainMenuScreen(game));
	        			game.setScreen(new PlayOptionScreen(game));
	        		}
	        		//start at 65,130 and go 60px down/right
	                if(inBounds(event, 65, 130, 60, 60) ) {
	                   game.setScreen(new GameScreen(game, 0, false));
	                    if(Settings.soundEnabled)
	                        Assets.click.play(1);
	                    return;
	                }
	                else if(inBounds(event, 125, 130, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 1, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 185, 130, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 2, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 245, 130, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 3, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 305, 130, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 4, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 365, 130, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 5, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 65, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 6, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 125, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 7, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 185, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 8, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 245, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 9, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 305, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 10, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                else if(inBounds(event, 365, 190, 60, 60) ) {
		                   game.setScreen(new GameScreen(game, 11, false));
		                    if(Settings.soundEnabled)
		                        Assets.click.play(1);
		                    return;
		                }
	                
	                
	                if(currSelected>-1)
	                	selected[currSelected] = false;
	        	}
	        }
	    }

	    @Override
	    public void present(float deltaTime) {
	        Graphics g = game.getGraphics();      
	        g.drawPixmap(Assets.rack, 0, 0);
	        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
	        
	        if(selected[currSelected]){
	        	int yoff = currSelected/6;
	        	int xoff = currSelected<6? currSelected:currSelected-6; 
	        	g.drawPixmap(Assets.hoverm, 60 + (xoff*60), 124 + (yoff*60),0,0,85,43,.706f);
	        }
	    }
	    
	    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
	        if(event.x > x && event.x < x + width - 1 && 
	           event.y > y && event.y < y + height - 1) 
	            return true;
	        else
	            return false;
	    }
	    
	    @Override
	    public void pause() {

	    }

	    @Override
	    public void resume() {

	    }

	    @Override
	    public void dispose() {

	    }
}
