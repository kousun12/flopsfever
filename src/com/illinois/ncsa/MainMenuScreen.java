package com.illinois.ncsa;

import java.util.List;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;

public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);               
    }   

    
    boolean playPushed = false;
    boolean helpPushed = false;
    boolean aboutPushed = false;
    boolean scoresPushed = false;
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();       
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_DOWN) {

                if(inBounds(event, 70, 146, 75, 75) ) {
                	playPushed = true;
                }
                if(inBounds(event, 158, 146, 75, 75) ) {
                	aboutPushed = true;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                    helpPushed = true;
                }
                if(inBounds(event, 334, 146, 75, 75) ) {
                    scoresPushed = true;
                }
            }
            
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                }
                if(inBounds(event, 70, 146, 75, 75) && playPushed ) {
                	
                    game.setScreen(new PlayOptionScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 158, 146, 75, 75) && aboutPushed ) {
                	
                    game.setScreen(new AboutScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 246, 146, 75, 75) && helpPushed) {
                	
                    game.setScreen(new HelpScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 334, 146, 75, 75) && scoresPushed ) {
                	
                    game.setScreen(new HighscoreScreen(game));
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                playPushed = false;
                aboutPushed = false;
                helpPushed = false;
                scoresPushed = false;
            }
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
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.menu, 0, 0);
        
        if(playPushed)
        	g.drawPixmap(Assets.hoverm, 64, 140);
        else if(aboutPushed)
        	g.drawPixmap(Assets.hoverm, 154, 140);
        else if(helpPushed)
        	g.drawPixmap(Assets.hoverm, 241, 140);
        else if(scoresPushed)
        	g.drawPixmap(Assets.hoverm, 330, 140);
        else;
        	

        //if(Settings.soundEnabled)
          //  g.drawPixmap(Assets.nav, 0, 416, 0, 0, 64, 64);
        //else
          //  g.drawPixmap(Assets.nav, 0, 416, 64, 0, 64, 64);
    }

    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
