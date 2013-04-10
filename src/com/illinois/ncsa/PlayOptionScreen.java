package com.illinois.ncsa;

import java.util.List;

import android.content.SharedPreferences;
import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;
import framework.impl.AndroidGame;

public class PlayOptionScreen extends Screen {  
	boolean levelsPushed=false;
	boolean endlessPushed = false;
	public int elev;
	
    public PlayOptionScreen(Game game) {
        super(game);
        SharedPreferences sp = ((AndroidGame) game ).getContext().getSharedPreferences("gameScore", 0);
    	elev = sp.getInt("elev", 0);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        
        for(int i=0; i<len; i++){
        	TouchEvent event = touchEvents.get(i);
        	
        	if(event.type == TouchEvent.TOUCH_DOWN) {

                if(inBounds(event, 158, 146, 75, 75) ) {
                	levelsPushed = true;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                    endlessPushed = true;
                }

            }
        	
        	if(event.type == TouchEvent.TOUCH_UP){
        		if( inBounds(event, 0,270,42,42) ) {
        			game.setScreen(new MainMenuScreen(game));
        		}
                if(inBounds(event, 158, 146, 75, 75) ) {
                   game.setScreen(new LevelScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                    game.setScreen(new GameScreen(game, elev, true));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                levelsPushed = false;
                endlessPushed = false;
        	}
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.play, 0, 0);
        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
        
        if(levelsPushed)
        	g.drawPixmap(Assets.hoverm, 154, 140);
        else if(endlessPushed)
        	g.drawPixmap(Assets.hoverm, 241, 140);

        else;
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