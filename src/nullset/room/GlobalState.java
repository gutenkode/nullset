package nullset.room;

import java.util.HashMap;

/**
 * Holds information about the state of the game world between different rooms.
 */
public class GlobalState {

    private boolean securityAlert;
    private boolean[] flags;
    private HashMap<String, String> vars;

    protected GlobalState() {
        securityAlert = false;
        flags = new boolean[10];
        vars = new HashMap<>();
    }

    public boolean isAlertTriggered() {
        return securityAlert;
    }
    public void triggerAlert(boolean t) {
        securityAlert = t;
    }

    public void toggleFlagState(int index) {
        flags[index] = !flags[index];
    }
    public void setFlagState(int index, boolean state) {
        flags[index] = state;
    }
    public boolean getFlagState(int index) {
        return flags[index];
    }

    public void setVar(String k, String v) { vars.put(k,v); }
    public String getVar(String k) {
        String s = vars.get(k);
        if (s == null)
            return "";
        return s;
    }
}
