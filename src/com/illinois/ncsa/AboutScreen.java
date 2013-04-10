package com.illinois.ncsa;

import java.util.List;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;

public class AboutScreen extends Screen {      
    public AboutScreen(Game game) {
        super(game);
    }
    /*
     * (non-Javadoc)
     * @see framework.Screen#update(float)
     * Things to do still:
     * Fix the game finish screen - 1hr
     * levels - 2hrs
     * high score submit - 2hrs
     * endless - 1hr
     * 
     * 
     */
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        
        for(int i=0; i<len; i++){
        	TouchEvent event = touchEvents.get(i);
        	if(event.type == TouchEvent.TOUCH_UP){
        		if( inBounds(event, 0,270,42,42) ) {
        			game.setScreen(new MainMenuScreen(game));
        		}
        	}
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.about, 0, 0);
        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
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