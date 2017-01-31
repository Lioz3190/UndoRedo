/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ups.m2ihm.drawingtool.model;

import fr.ups.m2ihm.drawingtool.model.core.DrawingToolCore;
import fr.ups.m2ihm.drawingtool.model.core.Rectangle;
import fr.ups.m2ihm.drawingtool.model.core.Shape;
import fr.ups.m2ihm.drawingtool.undomanager.Command;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Lioz
 */
public class CreateMacroCommand implements Command {
    private final DrawingToolCore core;
    private final Stack<Command> macroList;
    
    public CreateMacroCommand(DrawingToolCore core_,Stack<Command> macroList_){
        this.core = core_;
        this.macroList = macroList_;
    }
    
    public Stack<Command> getShapes(){
        return macroList;
    }
    
    @Override
    public void execute() {
        for ( int i = 0 ; i < macroList.size() ; i++){
             macroList.get(i).execute();
        }
    }

    @Override
    public void undo() {     
        for( int i = 0 ; i < macroList.size() ;i++){
            macroList.get(i).undo();
        }
    }


    public boolean isInclude(Rectangle rec) {
        for ( int i = 0 ; i < macroList.size() ; i++){
            if(!macroList.get(i).isInclude(rec)){
                return false;
            }
        }
        return true;
    }

}
