package com.illinois.ncsa;

import framework.Game;
import framework.Graphics;
import framework.Screen;

public class SplashScreen extends Screen {      
    public SplashScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.launch, 0, 0);

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