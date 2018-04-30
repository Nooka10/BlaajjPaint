/*
Author: Adrien
 */

// parent de la gomme et du pinceau

package controller.tools;

import javafx.scene.canvas.Canvas;

/**
 * Classe mère du pinceau et de la gomme, permet aux enfants de réagire au changement de thickness et d'opacité
 * @Author Adrien
 */
public abstract class ToolDrawer extends Tool {
    protected double opacity;
    protected double thickness;

    public ToolDrawer(double opacity, double thickness) {
        super();
        this.opacity = opacity;
        this.thickness = thickness;
    }

    public void setOpacity(double opacity){
        this.opacity = opacity;
        onOpacitySet();     // évenement qui est appelé au moment ou l'opacitée est changée
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        onThicknessSet();   // évènement qu iest appellé au momen tou l'épaisseur est changée
    }

    /**
     * Evènement appelé sur les enfants au moment ou l'opacité est changée
     * Doit être surchargé par les enfants
     * @Author Adrien
     */
    abstract protected void onOpacitySet();

    /**
     * Evènement appelé sur les enfants au moment ou l'épaisseur est changée
     * Doit être surchargé par les enfants
     * @Author Adrien
     */
    abstract protected void onThicknessSet();
}
