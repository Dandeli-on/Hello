package controller;

import model.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AI {
    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    private GameMode gameMode;
    private  Chessboard model;

    public AI(GameMode gameMode, Chessboard model){
     this.gameMode=gameMode;
     this.model=model;
    }

   public Step generateMoveAI1(PlayerColor playerColor){
       List<Step> validSteps = model.getValidStep(playerColor);
       System.out.println(validSteps);
       if (validSteps.size()>0){
           int a = (int) (Math.random() * validSteps.size());
       return validSteps.get(a);
       }
       return  null;
   }
   public Step generateMoveAI2(PlayerColor playerColor){
         return model.bestStep(playerColor);
    }


    public Step generateMoveAI3(PlayerColor playerColor){
        List<Step> validSteps = model.valueSteps(playerColor);
        validSteps.sort(new Comparator<Step>() {
            @Override
            public int compare(Step o1, Step o2) {
                if (o1.getValue()>o2.getValue()){ return -1;}
                if (o1.getValue()<o2.getValue()) return 1;
                return 0;
            }
        });

        if (validSteps.size()>0){
            int a = (int) (Math.random() *2);
            return validSteps.get(a);
        }
        return  null;
    }


}
