package com.illinois.ncsa;

import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;

public class ScoreScreen extends Screen {   
	
	Graphics g = game.getGraphics();
    public ScoreScreen(Game game, boolean endless) {
        super(game);
        g.clear(Color.BLACK);
        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
        g.drawText("LOADING", 160, 25, Assets.neonBlue, 30);
        
        ParseQuery query;
        if(endless)
            query = new ParseQuery("EScore");
        else
        	query = new ParseQuery("Score");
    	query.setLimit(10);
    	query.orderByDescending("score");
    	query.findInBackground(new FindCallback() {
    		public void done(List<ParseObject> scoreList, ParseException e) {
    			if (e == null) {
    				g.clear(Color.BLACK);
    				g.drawText("HIGH SCORES", 140, 25, Assets.neonBlue, 30);
    				g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
    				for(int i=0;i<10;i++){
    					int color = i%2==0? Color.WHITE:0x66ffffff;
    					try{
    						ParseObject po = scoreList.get(i);
        					g.drawText((i+1) + ". " + po.getString("name"), 20, 60 + i*22, color, 18);
        					g.drawText(po.getInt("score") + "", 300, 60 + i*22, color, 18);    					
    					}
    					catch(NullPointerException npe){
    						g.drawText((i+1) + ". ", 20, 60 + i*22, color, 18);
    					}
    					catch(IndexOutOfBoundsException iobe){
    						g.drawText((i+1) + ". ", 20, 60 + i*22, color, 18);
    					}
    				}
    				
    				

    			} else {
    				Log.d("score", "Error: " + e.getMessage());
    			}
    		}
    	});
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        
        for(int i=0; i<len; i++){
        	TouchEvent event = touchEvents.get(i);
        	if(event.type == TouchEvent.TOUCH_UP){
        		if( inBounds(event, 0,270,42,42) ) {
        			game.setScreen(new HighscoreScreen(game));
        		}
        	}
        }
    }

    @Override
    public void present(float deltaTime) {
        
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