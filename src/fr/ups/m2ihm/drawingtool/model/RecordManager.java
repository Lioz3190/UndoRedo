/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ups.m2ihm.drawingtool.model;

import fr.ups.m2ihm.drawingtool.undomanager.Command;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Lioz
 */
public class RecordManager {
    private final Stack<Command> record;
    private final PropertyChangeSupport support;
    public RecordManager(){
        record = new Stack<>();
        support = new PropertyChangeSupport(this);
        
    }
    public void registerCommand(Command command) {
        switch (currentState) {
            case IDLE:
                currentState = State.MACRO;
                command.execute();
                record.push(command);
                break;
            case MACRO:
                currentState = State.MACRO;
                command.execute();
                record.push(command);
                break;
        }
    }
    private enum State {
        IDLE,
        MACRO
    }
    private State currentState;
    
}
