/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package fr.ups.m2ihm.drawingtool.undomanager;

import fr.ups.m2ihm.drawingtool.model.core.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author David
 */
public class UndoManager {

    public final static String REGISTER_AVAILABLE_PROPERTY = "registerAvailable";
    public final static String UNDO_COMMANDS_PROPERTY = "undo";
    public final static String REDO_COMMANDS_PROPERTY = "redo";
    private final PropertyChangeSupport support;
    private final Map<String, Boolean> eventAvailability;
    private final Stack<Command> undoableCommands;
    private final Stack<Command> redoableCommands;
    
    public Stack<Command> getUndoableCommands() {
        return undoableCommands;
    }

    public Boolean isUndoEnabled() {
        return PossibleState.UNDO_ONLY.equals(currentState) || PossibleState.UNDO_REDOABLE.equals(currentState);
    }

    public Boolean isRedoEnabled() {
        return PossibleState.REDO_ONLY.equals(currentState) || PossibleState.UNDO_REDOABLE.equals(currentState);
    }

    private enum PossibleState {

        IDLE, UNDO_ONLY, REDO_ONLY, UNDO_REDOABLE
    }
    private PossibleState currentState;

    private void gotoState(PossibleState state) {
        currentState = state;
        switch (currentState) {
            case IDLE:
                enableEvents(true, false, false);
                break;
            case UNDO_ONLY:
                enableEvents(true, true, false);
                break;
            case REDO_ONLY:
                enableEvents(true, false, true);
                break;
            case UNDO_REDOABLE:
                enableEvents(true, true, true);
                break;
        }
    }

    public void init() {
        gotoState(PossibleState.IDLE);
        firePropertyChange(UNDO_COMMANDS_PROPERTY, null, Collections.unmodifiableList(undoableCommands));
        firePropertyChange(REDO_COMMANDS_PROPERTY, null, Collections.unmodifiableList(redoableCommands));
    }

    public UndoManager() {
        undoableCommands = new Stack<>();
        redoableCommands = new Stack<>();
        support = new PropertyChangeSupport(this);
        eventAvailability = new HashMap<>();
        eventAvailability.put(REGISTER_AVAILABLE_PROPERTY, null);
        eventAvailability.put(UndoEvent.UNDO.getPropertyName(), null);
        eventAvailability.put(UndoEvent.REDO.getPropertyName(), null);
        eventAvailability.put(UNDO_COMMANDS_PROPERTY, null);
        eventAvailability.put(REDO_COMMANDS_PROPERTY, null);
    }

    public void registerCommand(Command command) {
        switch (currentState) {
            case IDLE:
                gotoState(PossibleState.UNDO_ONLY);
                command.execute();
                undoableCommands.push(command);
                redoableCommands.clear();
                firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                break;
            case UNDO_ONLY:
                gotoState(PossibleState.UNDO_ONLY);
                command.execute();
                undoableCommands.push(command);
                redoableCommands.clear();
                firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                break;
            case REDO_ONLY:
                gotoState(PossibleState.UNDO_ONLY);
                command.execute();
                undoableCommands.push(command);
                redoableCommands.clear();
                firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                break;
            case UNDO_REDOABLE:
                gotoState(PossibleState.UNDO_ONLY);
                command.execute();
                undoableCommands.push(command);
                redoableCommands.clear();
                firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                break;
        }
    }

    public void undo() {
        Command undoneCommand;
        switch (currentState) {
            case IDLE:
                break;
            case UNDO_ONLY:
                if (undoableCommands.size() == 1) {
                    gotoState(PossibleState.REDO_ONLY);
                    undoneCommand = undoableCommands.pop();
                    undoneCommand.undo();
                    redoableCommands.push(undoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (undoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    undoneCommand = undoableCommands.pop();
                    undoneCommand.undo();
                    redoableCommands.push(undoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
            case REDO_ONLY:
                break;
            case UNDO_REDOABLE:
                if (undoableCommands.size() == 1) {
                    gotoState(PossibleState.REDO_ONLY);
                    undoneCommand = undoableCommands.pop();
                    undoneCommand.undo();
                    redoableCommands.push(undoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (undoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    undoneCommand = undoableCommands.pop();
                    undoneCommand.undo();
                    redoableCommands.push(undoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
        }
    }

    public void redo() {
        Command redoneCommand;
        switch (currentState) {
            case IDLE:
                break;
            case UNDO_ONLY:
                break;
            case REDO_ONLY:
                if (redoableCommands.size() == 1) {
                    gotoState(PossibleState.UNDO_ONLY);
                    redoneCommand = redoableCommands.pop();
                    redoneCommand.execute();
                    undoableCommands.push(redoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (redoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    redoneCommand = redoableCommands.pop();
                    redoneCommand.execute();
                    undoableCommands.push(redoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
            case UNDO_REDOABLE:
                if (redoableCommands.size() == 1) {
                    gotoState(PossibleState.UNDO_ONLY);
                    redoneCommand = redoableCommands.pop();
                    redoneCommand.execute();
                    undoableCommands.push(redoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (redoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    redoneCommand = redoableCommands.pop();
                    redoneCommand.execute();
                    undoableCommands.push(redoneCommand);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
        }
    }
    
    public Command commandToUndo(Rectangle rec){
        System.out.println(undoableCommands.size());
        for (int i = undoableCommands.size()-1; i >= 0 ; i--){
            if (undoableCommands.get(i).isInclude(rec)){
                return undoableCommands.get(i);
            }
        }
        return null;
    }
    
     public void undoRegional(Command com) {
        switch (currentState) {
            case IDLE:
                break;
            case UNDO_ONLY:
                if (undoableCommands.size() == 1) {
                    gotoState(PossibleState.REDO_ONLY);
                    undoableCommands.remove(com);
                    com.undo();
                    redoableCommands.push(com);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (undoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    undoableCommands.remove(com);
                    com.undo();
                    redoableCommands.push(com);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
            case REDO_ONLY:
                break;
            case UNDO_REDOABLE:
                if (undoableCommands.size() == 1) {
                    gotoState(PossibleState.REDO_ONLY);
                    undoableCommands.remove(com);
                    com.undo();
                    redoableCommands.push(com);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                } else if (undoableCommands.size() > 1) {
                    gotoState(PossibleState.UNDO_REDOABLE);
                    undoableCommands.remove(com);
                    com.undo();
                    redoableCommands.push(com);
                    firePropertyChange(UNDO_COMMANDS_PROPERTY, null, undoableCommands);
                    firePropertyChange(REDO_COMMANDS_PROPERTY, null, redoableCommands);
                }
                break;
        }
    }

    private void enableEvents(
            boolean registerEnabled,
            boolean undoEnabled,
            boolean redoEnabled) {
        fireEventAvailabilityChanged(REGISTER_AVAILABLE_PROPERTY, registerEnabled);
        fireEventAvailabilityChanged(UndoEvent.UNDO.getPropertyName(), undoEnabled);
        fireEventAvailabilityChanged(UndoEvent.REDO.getPropertyName(), redoEnabled);

    }

    private void fireEventAvailabilityChanged(String propertyName, boolean newAvailability) {
        Boolean oldAvailability = eventAvailability.get(propertyName);
        eventAvailability.put(propertyName, newAvailability);
        firePropertyChange(propertyName, oldAvailability, newAvailability);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }
}
