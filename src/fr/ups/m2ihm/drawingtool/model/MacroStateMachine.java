/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ups.m2ihm.drawingtool.model;

import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.BEGIN_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.CANCEL_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.END_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.values;
import static fr.ups.m2ihm.drawingtool.model.DrawingStateMachine.GHOST_PROPERTY;
import static fr.ups.m2ihm.drawingtool.model.DrawingStateMachine.SHAPES_PROPERTY;
import fr.ups.m2ihm.drawingtool.model.core.DrawingToolCore;
import fr.ups.m2ihm.drawingtool.model.core.Line;
import fr.ups.m2ihm.drawingtool.model.core.Shape;
import fr.ups.m2ihm.drawingtool.undomanager.Command;
import fr.ups.m2ihm.drawingtool.undomanager.UndoManager;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Lioz
 */
public class MacroStateMachine implements DrawingStateMachine {
    private UndoManager undoManager;
    private Shape ghost;
    private final ArrayList<Shape> macroList;
    private final PropertyChangeSupport support;
    private final Map<DrawingEventType, Boolean> eventAvailability;
   
    public MacroStateMachine(){
        support = new PropertyChangeSupport(this);
        macroList = new ArrayList<>();
        eventAvailability = new EnumMap<>(DrawingEventType.class);
        for (DrawingEventType eventType : values()) {
            eventAvailability.put(eventType, null);
        }
    }
    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }
    private void beginDraw(Shape shape, DrawingToolCore core) {
        switch (currentState) {
            case IDLE:
                gotoState(PossibleState.MACRO);
                ghost = shape;
                macroList.add(shape);
                break;
            case MACRO:
                break;
        }
    }

    private void cancelDraw(DrawingToolCore core) {
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:
                Shape oldGhost = ghost;
                ghost = null;
                gotoState(MacroStateMachine.PossibleState.IDLE);
                firePropertyChange(GHOST_PROPERTY, oldGhost, ghost);
                
                break;
        }
    }
    private void draw(Shape shape, DrawingToolCore core) {
        Shape oldGhost;
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:
                oldGhost = ghost;
                macroList.add(shape);
                gotoState(MacroStateMachine.PossibleState.MACRO);
                firePropertyChange(GHOST_PROPERTY, oldGhost, ghost);
                break;
        }
    }

    private void endDraw(DrawingToolCore core) {
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:
                Shape oldGhost = ghost;
                ghost = null;

                //core.createShape(oldGhost);
                Command com = new CreateMacroCommand(core, macroList);
                getUndoManager().registerCommand(com);
                
                gotoState(MacroStateMachine.PossibleState.IDLE);
                firePropertyChange(GHOST_PROPERTY, oldGhost, ghost);
                firePropertyChange(SHAPES_PROPERTY, null, core.getShapes());
                break;
        }
    }

   private void gotoState(MacroStateMachine.PossibleState possibleState) {
        currentState = possibleState;
        enableEvents(currentState.beginDrawEnabled, currentState.endDrawEnabled, currentState.drawEnabled, currentState.cancelDrawEnabled);
    }

    private void enableEvents(
            boolean beginDrawEnabled,
            boolean endDrawEnabled,
            boolean drawEnabled,
            boolean cancelDrawEnabled) {
        fireEventAvailabilityChanged(BEGIN_DRAW, beginDrawEnabled);
        fireEventAvailabilityChanged(CANCEL_DRAW, cancelDrawEnabled);
        fireEventAvailabilityChanged(DRAW, drawEnabled);
        fireEventAvailabilityChanged(END_DRAW, endDrawEnabled);

    }

    private enum PossibleState {
        IDLE(true, false, false, false), BEGIN(false, true, true, true), MACRO(false, true, true, true);
        public final boolean beginDrawEnabled;
        public final boolean endDrawEnabled;
        public final boolean drawEnabled;
        public final boolean cancelDrawEnabled;

        private PossibleState(boolean beginDrawEnabled, boolean endDrawEnabled, boolean drawEnabled, boolean cancelDrawEnabled) {
            this.beginDrawEnabled = beginDrawEnabled;
            this.endDrawEnabled = endDrawEnabled;
            this.drawEnabled = drawEnabled;
            this.cancelDrawEnabled = cancelDrawEnabled;
        }
    }
    private PossibleState currentState;
    private void fireEventAvailabilityChanged(DrawingEventType drawingEventType, boolean newAvailability) {
        Boolean oldAvailability = eventAvailability.get(drawingEventType);
        eventAvailability.put(drawingEventType, newAvailability);
        firePropertyChange(drawingEventType.getPropertyName(), oldAvailability, newAvailability);
    }
    
    @Override
    public void init(DrawingToolCore core) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleEvent(DrawingEvent event, DrawingToolCore core) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addPropertyListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public void addPropertyListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    @Override
    public void removePropertyListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public void removePropertyListener(String propertyName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyName, listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        support.firePropertyChange(propertyName, oldValue, newValue);
    }

}
