package demo.helper.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {

    Map<String, Object> temporaryData = new HashMap<>();

    public GlobalData() {
    }

    public void set(String key, Object value) {
        temporaryData.put(key, value);
    }

    public Object get(String key) {
        return temporaryData.get(key);
    }

    public String getString(String key) {
        return temporaryData.get(key).toString();
    }

    public int getInt(String key) {
        return Integer.parseInt(temporaryData.get(key).toString());
    }

    public double getDouble(String key) {
        return Double.parseDouble(temporaryData.get(key).toString());
    }

    public void setList(String key, List<String> list) {
        temporaryData.put(key, list);
    }

    public List<String> getList(String key) {
        Object value = temporaryData.get(key);
        if (value instanceof List<?>) {
            return (List<String>) value;
        }
        return null;
    }

    public boolean isKeySet(String key) {
        return temporaryData.containsKey(key) && temporaryData.get(key) != null;
    }

    public boolean isGlobalDataContains(String value) {
        for (Object dataValue : temporaryData.values()) {
            if (dataValue != null && dataValue.toString().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public int countValuesContaining(String substring) {
        int count = 0;
        for (String key : temporaryData.keySet()) {
            if (key.contains(substring)) {
                count++;
            }
        }
        return count;
    }

    public int sumIntValuesForKeysContaining(String substring) {
        int sum = 0;
        for (Map.Entry<String, Object> entry : temporaryData.entrySet()) {
            if (entry.getKey().contains(substring)) {
                try {
                    String valueStr = entry.getValue() != null ? entry.getValue().toString() : "0";
                    int value = Integer.parseInt(valueStr);
                    sum += value;
                } catch (NumberFormatException e) {
                }
            }
        }
        return sum;
    }

}
