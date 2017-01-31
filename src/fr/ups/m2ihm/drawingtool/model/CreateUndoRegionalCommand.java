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
import fr.ups.m2ihm.drawingtool.undomanager.UndoManager;

/**
 *
 * @author mannevva
 */
public class CreateUndoRegionalCommand implements Command{
    private Rectangle rec;
    private UndoManager undoManager;
    
     public CreateUndoRegionalCommand (Rectangle rec, UndoManager undoManager) {
        this.rec = rec;
        this.undoManager = undoManager;
    }

    @Override
    public void execute() {
        System.out.println("Execute com");
        Command com = undoManager.commandToUndo(rec);
        System.out.println(com!=null);
        if (com != null){
            undoManager.undoRegional(com);
        }
    }

    @Override
    public void undo() {
      //
    }

    @Override
    public boolean isInclude(Rectangle rec) {
       return false;
    }
    
}
