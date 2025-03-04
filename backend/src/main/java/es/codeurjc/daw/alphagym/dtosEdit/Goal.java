package es.codeurjc.daw.alphagym.dtosEdit;

public class Goal {
    private String goal;
    private boolean selected;

    public Goal() {
    }

    public Goal(String goal, boolean selected) {
        this.goal = goal;
        this.selected = selected;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String intensity) {
        this.goal = goal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
