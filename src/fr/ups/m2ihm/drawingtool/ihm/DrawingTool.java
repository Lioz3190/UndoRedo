/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package fr.ups.m2ihm.drawingtool.ihm;

import fr.ups.m2ihm.drawingtool.model.*;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.BEGIN_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.CANCEL_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingEventType.END_DRAW;
import static fr.ups.m2ihm.drawingtool.model.DrawingStateMachine.GHOST_PROPERTY;
import static fr.ups.m2ihm.drawingtool.model.DrawingStateMachine.SHAPES_PROPERTY;
import static fr.ups.m2ihm.drawingtool.model.PaletteEventType.DRAW_LINE;
import static fr.ups.m2ihm.drawingtool.model.PaletteEventType.DRAW_MACRO;
import static fr.ups.m2ihm.drawingtool.model.PaletteEventType.DRAW_RECTANGLE;
import fr.ups.m2ihm.drawingtool.model.core.Line;
import fr.ups.m2ihm.drawingtool.model.core.Rectangle;
import fr.ups.m2ihm.drawingtool.model.core.Shape;
import fr.ups.m2ihm.drawingtool.undomanager.UndoManager;
import java.awt.Color;
import static java.awt.Color.green;
import static java.awt.Color.pink;
import static java.awt.Cursor.getDefaultCursor;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class DrawingTool extends javax.swing.JFrame {

    private final Color DEFAULT_GHOST_COLOR = green;
    private final Color DEFAULT_DRAWING_COLOR = pink;
    private final DrawingToolModel model;

    /**
     * Creates new form RubberBanding
     */
    public DrawingTool() {
        initComponents();
        model = new DefaultDrawingToolModel();

        model.addPropertyListener(GHOST_PROPERTY, (PropertyChangeEvent evt) -> {
            Shape shape1;
            DrawingShape drawingShape;
            if (evt.getOldValue() != null) {
                shape1 = (Shape) evt.getOldValue();
                if (shape1 instanceof Line) {
                    drawingShape = new DrawingLine((Line) evt.getOldValue(), DEFAULT_GHOST_COLOR);
                } else {
                    drawingShape = new DrawingRectangle((Rectangle) evt.getOldValue(), DEFAULT_GHOST_COLOR);
                }
                whiteBoardPanel.removeShape(drawingShape);
            }
            if (evt.getNewValue() != null) {
                shape1 = (Shape) evt.getNewValue();
                if (shape1 instanceof Line) {
                    drawingShape = new DrawingLine((Line) evt.getNewValue(), DEFAULT_GHOST_COLOR);
                } else {
                    drawingShape = new DrawingRectangle((Rectangle) evt.getNewValue(), DEFAULT_GHOST_COLOR);
                }
                whiteBoardPanel.addShape(drawingShape);
            }
        });

        model.addPropertyListener(SHAPES_PROPERTY, (PropertyChangeEvent evt) -> {
            DrawingShape drawingShape;
            Set<Shape> drawings = (Set<Shape>) evt.getNewValue();
            whiteBoardPanel.clear();
            for (Shape shape1 : drawings) {
                if (shape1 instanceof Line) {
                    drawingShape = new DrawingLine((Line) shape1, DEFAULT_DRAWING_COLOR);
                } else {
                    drawingShape = new DrawingRectangle((Rectangle) shape1, DEFAULT_DRAWING_COLOR);
                }
                whiteBoardPanel.addShape(drawingShape);
            }
        });

        model.addPropertyListener(UndoManager.UNDO_COMMANDS_PROPERTY, (e) -> {
            menuUndo.setEnabled(!((List) e.getNewValue()).isEmpty());
        });

        model.addPropertyListener(UndoManager.REDO_COMMANDS_PROPERTY, (e) -> {
            menuRedo.setEnabled(!((List) e.getNewValue()).isEmpty());
        });

        model.addPropertyListener(DRAW.getPropertyName(), (PropertyChangeEvent evt) -> {
            Boolean enabled1 = (Boolean) evt.getNewValue();
            setCursor(enabled1 ? getPredefinedCursor(CROSSHAIR_CURSOR) : getDefaultCursor());
        });

        model.addPropertyListener(DRAW_LINE.getPropertyName(), (PropertyChangeEvent evt) -> {
            btnLine.setEnabled((Boolean) evt.getNewValue());
        });

        model.addPropertyListener(DRAW_RECTANGLE.getPropertyName(), (PropertyChangeEvent evt) -> {
            btnRectangle.setEnabled((Boolean) evt.getNewValue());
        });
        
        model.addPropertyListener(DRAW_MACRO.getPropertyName(),(PropertyChangeEvent evt) ->{
            btnMacro.setEnabled((Boolean) evt.getNewValue());
        });

        model.init();

    }
    
     public String[] getChoices (int nb){
        String[] choices = new String[nb];
        for (int i = 0; i<nb; i++){
            choices[i] = String.valueOf(i+1);
        }
        return choices;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnLine = new javax.swing.JButton();
        btnRectangle = new javax.swing.JButton();
        btnMacro = new javax.swing.JButton();
        whiteBoardPanel = new fr.ups.m2ihm.drawingtool.ihm.WhiteBoardPanel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        menuUndo = new javax.swing.JMenuItem();
        menuRedo = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLine.setText("Line");
        btnLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLineActionPerformed(evt);
            }
        });

        btnRectangle.setText("Rectangle");
        btnRectangle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRectangleActionPerformed(evt);
            }
        });

        btnMacro.setText("Macro");
        btnMacro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMacroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRectangle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnMacro, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRectangle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMacro)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        whiteBoardPanel.setBackground(new java.awt.Color(255, 255, 255));
        whiteBoardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                whiteBoardPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                whiteBoardPanelMouseReleased(evt);
            }
        });
        whiteBoardPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                whiteBoardPanelMouseDragged(evt);
            }
        });
        whiteBoardPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                whiteBoardPanelKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout whiteBoardPanelLayout = new javax.swing.GroupLayout(whiteBoardPanel);
        whiteBoardPanel.setLayout(whiteBoardPanelLayout);
        whiteBoardPanelLayout.setHorizontalGroup(
            whiteBoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );
        whiteBoardPanelLayout.setVerticalGroup(
            whiteBoardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton1.setText("Undo Group");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jMenu2.setText("Edit");

        menuUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        menuUndo.setText("Undo");
        menuUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUndoActionPerformed(evt);
            }
        });
        jMenu2.add(menuUndo);

        menuRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        menuRedo.setText("Redo");
        menuRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRedoActionPerformed(evt);
            }
        });
        jMenu2.add(menuRedo);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(whiteBoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(0, 96, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(whiteBoardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void whiteBoardPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_whiteBoardPanelMousePressed
        DrawingEvent event = new DrawingEvent(BEGIN_DRAW, evt.getPoint());
        model.handleEvent(event);
    }//GEN-LAST:event_whiteBoardPanelMousePressed

    private void whiteBoardPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_whiteBoardPanelMouseReleased
        DrawingEvent event = new DrawingEvent(END_DRAW, evt.getPoint());
        model.handleEvent(event);
    }//GEN-LAST:event_whiteBoardPanelMouseReleased

    private void whiteBoardPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_whiteBoardPanelMouseDragged
        DrawingEvent event = new DrawingEvent(DRAW, evt.getPoint());
        model.handleEvent(event);
    }//GEN-LAST:event_whiteBoardPanelMouseDragged

    private void whiteBoardPanelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_whiteBoardPanelKeyPressed
        if (evt.getKeyCode() == VK_ESCAPE) {
            DrawingEvent event = new DrawingEvent(CANCEL_DRAW, null);
            model.handleEvent(event);
        }
    }//GEN-LAST:event_whiteBoardPanelKeyPressed

    private void btnLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLineActionPerformed
        PaletteEvent event = new PaletteEvent(DRAW_LINE);
        model.handleEvent(event);
    }//GEN-LAST:event_btnLineActionPerformed

    private void btnRectangleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRectangleActionPerformed
        PaletteEvent event = new PaletteEvent(DRAW_RECTANGLE);
        model.handleEvent(event);
    }//GEN-LAST:event_btnRectangleActionPerformed

    private void menuUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUndoActionPerformed
        model.undo();
    }//GEN-LAST:event_menuUndoActionPerformed

    private void menuRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRedoActionPerformed
        model.redo();
    }//GEN-LAST:event_menuRedoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String[] choices = getChoices(model.allUndo());
        JOptionPane d = new JOptionPane();
        String retour = (String) d.showInputDialog(null,
                                 "Combien de Undo",
                                 "Undo Groupé",
                                 JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
        for (int i = 0;i < Integer.parseInt(retour); i++){
            model.undo();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnMacroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMacroActionPerformed
        // TODO add your handling code here:
        PaletteEvent event = new PaletteEvent(DRAW_MACRO);
        model.handleEvent(event);
    }//GEN-LAST:event_btnMacroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLine;
    private javax.swing.JButton btnMacro;
    private javax.swing.JButton btnRectangle;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuItem menuRedo;
    private javax.swing.JMenuItem menuUndo;
    private fr.ups.m2ihm.drawingtool.ihm.WhiteBoardPanel whiteBoardPanel;
    // End of variables declaration//GEN-END:variables
}
