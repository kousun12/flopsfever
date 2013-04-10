package com.illinois.ncsa;

import java.util.List;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;
import framework.impl.AndroidGame;

public class HighscoreScreen extends Screen {
	boolean subLevels = false;
	boolean subEndless = false;
	boolean viewLevels = false;
	boolean viewEndless = false;
	int total;
	int endlesstotal;
	String name;
	long id;
	boolean submittedLevels = false;
	boolean submittedEndless = false;


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
        	
        	if(event.type == TouchEvent.TOUCH_DOWN) {

                if(inBounds(event, 70, 146, 75, 75) ) {
                	subLevels = true;
                }
                if(inBounds(event, 158, 146, 75, 75) ) {
                	subEndless = true;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                    viewLevels = true;
                }
                if(inBounds(event, 334, 146, 75, 75) ) {
                    viewEndless = true;
                }
            }
        	
        	if(event.type == TouchEvent.TOUCH_UP){
        		if( inBounds(event, 0,270,52,52) ) {
        			game.setScreen(new MainMenuScreen(game));
        		}

                if(inBounds(event, 70, 146, 75, 75)) {
                    subLevels = false;
                    if(!submittedLevels){
                    	if(Settings.soundEnabled)
                    		Assets.click.play(1);

                    	SharedPreferences sp = ((AndroidGame) game ).getContext().getSharedPreferences("gameScore", 0);
                    	total=0;
                    	name = sp.getString("user", "anon");
                    	id = sp.getLong("id", 0);
                    	for(int j=0;j<12;j++){
                    		total+=sp.getInt("highestScore"+j, 0);
                    	}

                    	ParseQuery query = new ParseQuery("Score");
                    	query.whereEqualTo("name", name);
                    	query.whereEqualTo("id", id);
                    	query.findInBackground(new FindCallback() {
                    		public void done(List<ParseObject> scoreList, ParseException e) {
                    			if (e == null) {
                    				if(scoreList.size()==0){
                    					ParseObject scoreObject = new ParseObject("Score");
                                    	scoreObject.put("name", name);
                                    	scoreObject.put("score", total);
                                    	scoreObject.put("id", id);
                                    	scoreObject.saveInBackground();
                    					return;
                    				}
                    				else{
                    					
                    					ParseObject po = scoreList.get(0);
                    					po.put("score", total);
                    					po.saveInBackground();
                    					
                    				}
                    				
                    				Toast.makeText(((AndroidGame)game).getContext(),"Submitted Score of: " + total, Toast.LENGTH_LONG).show();
                    			} else {
                    				Log.d("score", "Error: " + e.getMessage());
                    			}
                    		}
                    	});

                    	

                    	submittedLevels = true;
                    }
                    return;
                }
                if(inBounds(event, 158, 146, 75, 75) ) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    subEndless = false;
                    if(!submittedEndless){
                    	SharedPreferences sp = ((AndroidGame) game ).getContext().getSharedPreferences("gameScore", 0);
                    	name = sp.getString("user", "anon");
                    	id = sp.getLong("id", 0);
                    	endlesstotal = sp.getInt("endlessHigh", 0);

                    	ParseQuery query = new ParseQuery("EScore");
                    	query.whereEqualTo("name", name);
                    	query.whereEqualTo("id", id);
                    	query.findInBackground(new FindCallback() {
                    		public void done(List<ParseObject> scoreList, ParseException e) {
                    			if (e == null) {
                    				if(scoreList.size()==0){
                    					ParseObject scoreObject = new ParseObject("EScore");
                    					scoreObject.put("name", name);
                    					scoreObject.put("score", endlesstotal);
                    					scoreObject.put("id", id);
                    					scoreObject.saveInBackground(new SaveCallback() {
                    						   public void done(ParseException e) {
                    							     if (e == null) {
                    	                    				Toast.makeText(((AndroidGame)game).getContext(),"Submitted Score of: " + endlesstotal, Toast.LENGTH_LONG).show();
                    							     } else {
                    							     }
                    							   }
                    							 });
                    					return;
                    				}
                    				else{

                    					ParseObject po = scoreList.get(0);
                    					po.put("score", endlesstotal);
                    					po.saveInBackground(new SaveCallback() {
                 						   public void done(ParseException e) {
              							     if (e == null) {
              	                    				Toast.makeText(((AndroidGame)game).getContext(),"Submitted Score of: " + endlesstotal, Toast.LENGTH_LONG).show();
              							     } else {
              							     }
              							   }
              							 });

                    				}
                    				

                    			} else {
                    				Log.d("score", "Error: " + e.getMessage());
                    			}
                    		}
                    	});
                    	submittedEndless=true;
                    }
                    
                    return;
                }
                if(inBounds(event, 246, 146, 75, 75) ) {
                    viewLevels = false;
                    game.setScreen(new ScoreScreen(game,false));
                    
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 334, 146, 75, 75) ) {
                   
                   game.setScreen(new ScoreScreen(game,true));

                   viewEndless = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    return;
                }
                
                subLevels = false;
                subEndless = false;
                viewLevels = false;
                viewEndless = false;
        	}
        }
    }
    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.score, 0, 0);
        g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);

        if(subLevels)
        	g.drawPixmap(Assets.hoverm, 64, 140);
        else if(subEndless)
        	g.drawPixmap(Assets.hoverm, 154, 140);
        else if(viewLevels)
        	g.drawPixmap(Assets.hoverm, 241, 140);
        else if(viewEndless)
        	g.drawPixmap(Assets.hoverm, 330, 140);
        else{
        	g.drawPixmap(Assets.score, 0, 0);
            g.drawPixmap(Assets.nav, 10, 280, 0, 0, 32, 32);
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
