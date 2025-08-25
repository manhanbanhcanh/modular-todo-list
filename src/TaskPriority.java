import java.awt.*;
public enum TaskPriority {
    LOW("Low", new Color(0,128,0)),
    MEDIUM("Medium", new Color(255,165,0)),
    HIGH("High", new Color(200,0,0));

    private final String label;
    private final java.awt.Color color;

TaskPriority(String label, Color color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString(){
        return label;
    }

    public static TaskPriority fromString(String text){
        for (TaskPriority p : TaskPriority.values()) {
            if (p.label.equalsIgnoreCase(text)) {
                return p;
            }
        }
        return MEDIUM;
    }
}