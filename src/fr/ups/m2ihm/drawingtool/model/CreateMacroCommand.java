/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ups.m2ihm.drawingtool.model;

import fr.ups.m2ihm.drawingtool.model.core.DrawingToolCore;
import fr.ups.m2ihm.drawingtool.model.core.Shape;
import fr.ups.m2ihm.drawingtool.undomanager.Command;
import java.util.ArrayList;

/**
 *
 * @author Lioz
 */
public class CreateMacroCommand implements Command {
    private final DrawingToolCore core;
    private final ArrayList<Shape> macroList;
    
    public CreateMacroCommand(DrawingToolCore core_,ArrayList<Shape> macroList_){
        this.core = core_;
        this.macroList = macroList_;
    }
    
    @Override
    public void execute() {
        for ( int i = 0 ; i < macroList.size() ; i++){
            core.createShape(macroList.get(i));
        }
    }

    @Override
    public void undo() {       
        core.removeShape(macroList.get(macroList.size()-1));
    }
}
