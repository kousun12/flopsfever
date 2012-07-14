package com.illinois.ncsa;

import framework.Game;
import framework.Graphics;
import framework.Graphics.PixmapFormat;
import framework.Pixmap;
import framework.Screen;

public class LoadingScreen extends Screen {
	int x=0;
	Pixmap splash;
    public LoadingScreen(Game game) {
        super(game);
        splash = game.getGraphics().newPixmap("launch.png", PixmapFormat.RGB565);
    }

    @Override
    public void update(float deltaTime) {
    	//if(x>0){
	        Graphics g = game.getGraphics();
	    	//Assets.launch = g.newPixmap("launch.png", PixmapFormat.RGB565);
	        Assets.about = g.newPixmap("about.png", PixmapFormat.RGB565);
	        Assets.cores = g.newPixmap("cores.png", PixmapFormat.ARGB4444);
	        Assets.failure = g.newPixmap("failure.png", PixmapFormat.ARGB4444);
	        Assets.help = g.newPixmap("help.png", PixmapFormat.RGB565);
	        Assets.icon = g.newPixmap("icon.png", PixmapFormat.ARGB4444);
	        Assets.medals = g.newPixmap("medals.png", PixmapFormat.ARGB4444);
	        Assets.menu = g.newPixmap("menu.png", PixmapFormat.RGB565);
	        Assets.nav = g.newPixmap("nav.png", PixmapFormat.RGB565);
	        Assets.play = g.newPixmap("play.png", PixmapFormat.ARGB4444);
	        Assets.rack = g.newPixmap("rack.png", PixmapFormat.ARGB4444);
	        Assets.score = g.newPixmap("score.png", PixmapFormat.RGB565);
	        Assets.selector = g.newPixmap("selector.png", PixmapFormat.ARGB4444);
	        Assets.tasksMono = g.newPixmap("tasks_mono.png", PixmapFormat.ARGB4444);
	        Assets.tasks = g.newPixmap("tasks.png", PixmapFormat.ARGB4444);
	        Assets.timeline = g.newPixmap("timeline.png", PixmapFormat.ARGB4444);
	        Assets.hoverl = g.newPixmap("hover_levels.png", PixmapFormat.ARGB4444);
	        Assets.hoverm = g.newPixmap("hover_menu.png", PixmapFormat.ARGB4444);
	        
	
	        //Assets.accept = game.getAudio().newSound("accept.ogg");
	        Assets.click = game.getAudio().newSound("click.ogg");
	        //Assets.reject = game.getAudio().newSound("reject.ogg");
	        //Assets.fail = game.getAudio().newSound("fail.ogg");
	        Settings.load(game.getFileIO());
    	//}
        //x+=1;
        //if(x>4)
        	game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void present(float deltaTime) {
    	//Graphics g = game.getGraphics();
    	//g.clear(0);
    	//g.drawPixmap(splash,0,0);
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