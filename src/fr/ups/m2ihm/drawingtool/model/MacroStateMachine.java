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
import fr.ups.m2ihm.drawingtool.model.core.Rectangle;
import fr.ups.m2ihm.drawingtool.model.core.Shape;
import fr.ups.m2ihm.drawingtool.undomanager.Command;
import fr.ups.m2ihm.drawingtool.undomanager.UndoManager;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author Lioz
 */
public class MacroStateMachine implements DrawingStateMachine {
    private UndoManager undoManager;
    private Point p0,p;
    private final Stack<Command> macroList;

    
    private final PropertyChangeSupport support;
    private final Map<DrawingEventType, Boolean> eventAvailability;
    private RecordManager recordManager;
    
   
    public MacroStateMachine(){
        support = new PropertyChangeSupport(this);
        macroList = new Stack<>();
        eventAvailability = new EnumMap<>(DrawingEventType.class);
        for (DrawingEventType eventType : values()) {
            eventAvailability.put(eventType, null);
        
        }
       
    }

    public RecordManager getRecordManager(){
        return recordManager;
    }
    
    public void setRecordManager(RecordManager recordManager){
        this.recordManager = recordManager;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }
    public void setRecoManager(RecordManager rm){
        this.recordManager = rm;
    }
    private void beginDraw(Point point, DrawingToolCore core) {
        switch (currentState) {
            case IDLE:
                gotoState(PossibleState.MACRO);
                p0 = point;
                
                break;
            case MACRO:
                break;
        }
    }

    private void endDraw(DrawingToolCore core) {
        switch (currentState) {
            case IDLE:
                break;
            case MACRO:

                if ( getRecordManager().getCurrentRecord() != null){
                    Stack<Command> macroList = new Stack<>();
                    Shape s1 = ((CreateShapeCommand)getRecordManager().getCurrentRecord().get(0)).getShape();
                    if (s1 instanceof Line)
                        p = ((Line) s1).getSource();
                    else if (s1 instanceof Rectangle)
                        p = ((Rectangle) s1).getUpperLeftCorner();
                    for ( int i = 0; i < getRecordManager().getCurrentRecord().size();i++){
                        Shape shape = ((CreateShapeCommand)getRecordManager().getCurrentRecord().get(i)).getShape();
                        if (shape instanceof Line){
                            Point source = ((Line) shape).getSource();
                            Point destination = ((Line) shape).getDestination();
                            Command cmd = new CreateShapeCommand(core,new Line(
                                    new Point(source.x+p0.x-p.x,source.y+p0.y-p.y),
                                    new Point(destination.x+p0.x-p.x,destination.y+p0.y-p.y)));
                            macroList.push(cmd);
                        }
                        else if (shape instanceof Rectangle){
                            Point hautGauche = ((Rectangle) shape).getUpperLeftCorner();
                            Point basDroit = ((Rectangle) shape).getLowerRightCorner();
                            Command cmd = new CreateShapeCommand(core,new Rectangle(
                                    new Point(hautGauche.x+p0.x-p.x,hautGauche.y+p0.y-p.y),
                                    new Point(basDroit.x+p0.x-p.x,basDroit.y+p0.y-p.y)));
                            macroList.push(cmd);
                        }
                    }
                    //core.createShape(oldGhost);
                    Command com = new CreateMacroCommand(core, macroList);
                    getUndoManager().registerCommand(com);
                    getRecordManager().registerCommand(com);
                }
                gotoState(MacroStateMachine.PossibleState.IDLE);
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
        gotoState(PossibleState.IDLE);
        firePropertyChange(SHAPES_PROPERTY, null, core.getShapes());
    }

    public void handleEvent(DrawingEvent event, DrawingToolCore core) {
        switch(event.getEventType()){
            case BEGIN_DRAW:
                beginDraw(event.getPoint(), core);
                break;
            case END_DRAW:
                endDraw(core);
                break;
        }
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
