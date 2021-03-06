package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.List;

public class TrendTableRow {

    private String deviceName;
    private String featureName;
    private String scenarioName;
    private ArrayList<String> statuses;
    private String id;

    public TrendTableRow(String deviceName, String featureName, String scenarioName, String id){
        this.deviceName = deviceName;
        this.featureName = featureName;
        this.scenarioName = scenarioName;
        this.statuses = new ArrayList<>();
        this.id = id;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatus(String name){
        statuses.add(name);
    }

    public String getFeatureName(){
        return featureName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getId(){
        return  id;
    }

}
