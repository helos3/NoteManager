package test.notemanager.models;

import java.util.HashMap;
import java.util.Map;

import test.notemanager.R;

public class Priorities {
    private static Map<Priority, Integer> valuesEnum;
    private static Map<String, Priority> valuesString;

    public static Map<Priority, Integer> getEnumInstance() {
        if (valuesEnum == null) initPrioritiesEnum();
        return valuesEnum;
    }

    private static void initPrioritiesEnum() {
        valuesEnum = new HashMap() {{
            put(Priority.NO_PRIORITY, R.drawable.importance_gray);
            put(Priority.LOW_PRIORITY, R.drawable.importance_green);
            put(Priority.MEDIUM_PRIORITY, R.drawable.importance_yellow);
            put(Priority.HIGH_PRIOITY, R.drawable.importance_red);
        }};
    }

}