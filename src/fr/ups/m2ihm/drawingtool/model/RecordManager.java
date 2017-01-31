/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ups.m2ihm.drawingtool.model;

import fr.ups.m2ihm.drawingtool.undomanager.Command;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Lioz
 */
public class RecordManager {
    public HashMap<String,Stack<Command>> macroList;
    private Stack<Command> record;
    private PropertyChangeSupport support;
    private Stack<Command> currentRecord;
    
    public RecordManager(){
        record = new Stack<>();
        support = new PropertyChangeSupport(this);
        macroList = new HashMap<>();
        currentRecord = new Stack<>();
        init();
    }
    
    public Stack<Command> getRecord(){
        return record;
    }
    
    
    public Stack<Command> getCurrentRecord(){
        return currentRecord;
    }
    
    
    public void setCurrentRecord(String Name){
        currentRecord = macroList.get(Name);
    }
    
    public void init(){
        currentState = State.IDLE;
    }
    
    public void beginRecording(){
        switch(currentState){
            case IDLE:
                currentState = State.MACRO;
                record = new Stack<>();
                System.out.println("begin");
                break;
            case MACRO:
                break;
                
        }
    }
    
    public void endRecording(String macroName){
        switch (currentState){
            case IDLE:
                break;
            case MACRO:
                currentState = State.IDLE;
                if(macroName != null && record.size() != 0){
                    macroList.put(macroName, record);
                    System.out.println("lol");
                }
                    
                break;
        }
    }
    
    public void registerCommand(Command command){
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:
                System.out.println("command");
                if (command instanceof CreateMacroCommand)
                    registerCommand((CreateMacroCommand)command);
                else 
                    record.push(command);
                break;
        }
    }
    public void registerCommand(CreateMacroCommand cmc) {
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:
                System.out.println("Macro");
                for ( Command command : cmc.getShapes())
                    registerCommand(command);       
                break;
        }
    }
    private enum State {
        IDLE,
        MACRO
    }
    private State currentState;
    
   
}
