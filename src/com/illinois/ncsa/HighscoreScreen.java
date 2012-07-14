package com.illinois.ncsa;

import java.util.List;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;

public class HighscoreScreen extends Screen {


    public HighscoreScreen(Game game) {
        super(game);

    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        
        for(int i=0; i<len; i++){
        	TouchEvent event = touchEvents.get(i);
        	if(event.type == TouchEvent.TOUCH_UP){
        		if( inBounds(event, 10,280,32,32) ) {
        			game.setScreen(new MainMenuScreen(game));
        		}

                if(inBounds(event, 70, 146, 75, 75) ) {
                    //game.setScreen(new PlayOptionScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 158, 146, 75, 75) ) {
                    //game.setScreen(new AboutScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                   // game.setScreen(new HelpScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 334, 146, 75, 75) ) {
                   // game.setScreen(new HighscoreScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
        	}
        }
    }
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.score, 0, 0);
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
