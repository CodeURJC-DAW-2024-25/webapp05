package es.codeurjc.daw.alphagym.dtos;

public class Intensity {
    
    private int intensity;
    private boolean selected;

    public Intensity() {
    }

    public Intensity(int intensity, boolean selected) {
        this.intensity = intensity;
        this.selected = selected;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
