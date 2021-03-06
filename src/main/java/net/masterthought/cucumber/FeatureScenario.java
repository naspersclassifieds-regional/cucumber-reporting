package net.masterthought.cucumber;

public class FeatureScenario {

    private String featureName;
    private String scenarioName;
    private String status;
    private String deviceName;
    private String id;

    public FeatureScenario(){
    }

    public FeatureScenario(String deviceName, String featureName, String scenarioName, String status, String line){
        this.deviceName = deviceName;
        this.featureName = featureName;
        this.scenarioName = scenarioName;
        this.status = status;
        this.id = deviceName + ";" + featureName + ";" + scenarioName + ";" + line;
    }


    public String getFeatureName() {
        return featureName;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public String getStatus() {
        return status;
    }

    public String getDeviceName(){
        return deviceName;
    }

    public String getId(){
        return id;
    }
}
