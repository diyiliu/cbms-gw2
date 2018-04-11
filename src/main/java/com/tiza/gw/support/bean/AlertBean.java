package com.tiza.gw.support.bean;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: AlertBean
 * Author: DIYILIU
 * Update: 2016-01-28 13:58
 */
public class AlertBean {

    private Map params;
    private String function;
    private String description;

    public static String OUTCOME = "result";

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getDescription() {
        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(description);

        StringBuffer strBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(strBuffer, params.get(matcher.group(1)).toString());
        }
        matcher.appendTail(strBuffer);

        return strBuffer.toString();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean doAlert() throws ScriptException {

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("JavaScript");

        for (Iterator<Map.Entry> iterator = params.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = iterator.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            engine.put(key, String.valueOf(value));
        }
        engine.eval(function);
        return (Boolean) engine.get(OUTCOME);
    }

    public static void main(String[] args) {

        try {
            Map map = new HashMap();
            map.put("WaterTemperature", "-40");
            map.put("maxThreshold", "-95");

            String exp = "var result = parseFloat(WaterTemperature) > parseFloat(maxThreshold);";
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("JavaScript");

            for (Iterator<Map.Entry> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry entry = iterator.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                engine.put(key, value);
            }
            engine.eval(exp);

            System.out.println(engine.get(OUTCOME));
        } catch (ScriptException e) {
            e.printStackTrace();
        }


    }
}
