package net.masterthought.cucumber;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

/**
 * Contains historical information about all and failed features, trendTableRows and steps.
 *
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class Trends {

    private String[] buildNumbers = new String[0];

    private int[] passedFeatures = new int[0];
    private int[] failedFeatures = new int[0];
    private int[] totalFeatures = new int[0];

    private int[] passedScenarios = new int[0];
    private int[] failedScenarios = new int[0];
    private int[] totalScenarios = new int[0];

    private int[] passedSteps = new int[0];
    private int[] failedSteps = new int[0];
    private int[] skippedSteps = new int[0];
    private int[] pendingSteps = new int[0];
    private int[] undefinedSteps = new int[0];
    private int[] totalSteps = new int[0];

    private long[] durations = new long[0];

    private  FeatureScenario[][] featuresDetail = new FeatureScenario[0][0];

    public String[] getBuildNumbers() {
        return buildNumbers;
    }

    public FeatureScenario[][] getFeaturesDetail() {
        return featuresDetail;
    }

    public ArrayList<TrendTableRow> collectTrendFeatureScenario() {

        ArrayList<TrendTableRow> trendTableRows = new ArrayList<>();

        for (int i = 0 ; i < buildNumbers.length;i++){

            for (int j=0;j<featuresDetail[i].length;j++){
                FeatureScenario featureScenario = featuresDetail[i][j];
                TrendTableRow trendTableRow = new TrendTableRow(featureScenario.getDeviceName(), featureScenario.getFeatureName(), featureScenario.getScenarioName(), featureScenario.getId());
                if(!isAlreadyInTrenTableRows(trendTableRow,trendTableRows)){
                    trendTableRows.add(trendTableRow);
                }
            }
        }

        for (int i = 0 ; i < buildNumbers.length;i++){
            FeatureScenario[] featureScenarioPerBuild = featuresDetail[i];
            for (TrendTableRow trendTableRow: trendTableRows) {
                if (getStatus(trendTableRow, featureScenarioPerBuild)!=null){
                    trendTableRow.setStatus(getStatus(trendTableRow, featureScenarioPerBuild));
                }else {
                    trendTableRow.setStatus("-");
                }
            }
        }

        return trendTableRows;
    }

    private boolean isAlreadyInTrenTableRows(TrendTableRow trendTableRow, ArrayList<TrendTableRow> TrendTableRows){
        boolean found = false;
        for (TrendTableRow ttr: TrendTableRows) {
            if(ttr.getId().equals(trendTableRow.getId())){
                found = true;
                break;
            }
        }

        return found;
    }

    private String getStatus(TrendTableRow trendTableRow, FeatureScenario[] featureScenarios){
        String status = null;
        for (int i=0; i < featureScenarios.length ; i++){
            String status_tmp = featureScenarios[i].getStatus();
            if (trendTableRow.getId().equals(featureScenarios[i].getId())){
                status = status_tmp;
            }
        }

        return status;
    }

    public int[] getFailedFeatures() {
        return failedFeatures;
    }

    public int[] getPassedFeatures() {
        return passedFeatures;
    }

    public int[] getTotalFeatures() {
        return totalFeatures;
    }

    public int[] getPassedScenarios() {
        return passedScenarios;
    }

    public int[] getFailedScenarios() {
        return failedScenarios;
    }

    public int[] getTotalScenarios() {
        return totalScenarios;
    }

    public int[] getPassedSteps() {
        return passedSteps;
    }

    public int[] getFailedSteps() {
        return failedSteps;
    }

    public int[] getSkippedSteps() {
        return skippedSteps;
    }

    public int[] getPendingSteps() {
        return pendingSteps;
    }

    public int[] getUndefinedSteps() {
        return undefinedSteps;
    }

    public int[] getTotalSteps() {
        return totalSteps;
    }

    public long[] getDurations() {
        return durations;
    }

    /**
     * Adds build into the trends.
     * @param buildNumber number of the build
     * @param reportable stats for the generated report
     */
    public void addBuild(String buildNumber, Reportable reportable) {

        buildNumbers = (String[]) ArrayUtils.add(buildNumbers, buildNumber);

        passedFeatures = ArrayUtils.add(passedFeatures, reportable.getPassedFeatures());
        failedFeatures = ArrayUtils.add(failedFeatures, reportable.getFailedFeatures());
        totalFeatures = ArrayUtils.add(totalFeatures, reportable.getFeatures());

        passedScenarios = ArrayUtils.add(passedScenarios, reportable.getPassedScenarios());
        failedScenarios = ArrayUtils.add(failedScenarios, reportable.getFailedScenarios());
        totalScenarios = ArrayUtils.add(totalScenarios, reportable.getScenarios());

        passedSteps = ArrayUtils.add(passedSteps, reportable.getPassedSteps());
        failedSteps = ArrayUtils.add(failedSteps, reportable.getFailedSteps());
        skippedSteps = ArrayUtils.add(skippedSteps, reportable.getSkippedSteps());
        pendingSteps = ArrayUtils.add(pendingSteps, reportable.getPendingSteps());
        undefinedSteps = ArrayUtils.add(undefinedSteps, reportable.getUndefinedSteps());
        totalSteps = ArrayUtils.add(totalSteps, reportable.getSteps());

        durations = ArrayUtils.add(durations, reportable.getDuration());

        featuresDetail = (FeatureScenario[][]) ArrayUtils.add(featuresDetail, reportable.getFeatureDetails());


        // this should be removed later but for now correct features and save valid data
        applyPatchForFeatures();
        if (pendingSteps.length < buildNumbers.length) {
            fillMissingSteps();
        }
        if (durations.length < buildNumbers.length) {
            fillMissingDurations();
        }

        //this code to handle current user that want to update to this version
        if (featuresDetail.length < buildNumbers.length){
            fillMissingFeaturesDetail();
        }
    }

    /**
     * Removes elements that points to the oldest items.
     * Leave trends unchanged if the limit is bigger than current trends length.
     *
     * @param limit number of elements that will be leave
     */
    public void limitItems(int limit) {
        buildNumbers = copyLastElements(buildNumbers, limit);

        passedFeatures = copyLastElements(passedFeatures, limit);
        failedFeatures = copyLastElements(failedFeatures, limit);
        totalFeatures = copyLastElements(totalFeatures, limit);

        passedScenarios = copyLastElements(passedScenarios, limit);
        failedScenarios = copyLastElements(failedScenarios, limit);
        totalScenarios = copyLastElements(totalScenarios, limit);

        passedSteps = copyLastElements(passedSteps, limit);
        failedSteps = copyLastElements(failedSteps, limit);
        skippedSteps = copyLastElements(skippedSteps, limit);
        pendingSteps = copyLastElements(pendingSteps, limit);
        undefinedSteps = copyLastElements(undefinedSteps, limit);
        totalSteps = copyLastElements(totalSteps, limit);

        durations = copyLastElements(durations, limit);

        featuresDetail = copyLastElements(featuresDetail,limit);
    }

    private static FeatureScenario[][] copyLastElements(FeatureScenario[][] srcArray, int copyingLimit){
        if (srcArray.length <= copyingLimit) {
            return srcArray;
        }

        FeatureScenario[][] featureScenarios = new FeatureScenario[copyingLimit][];
        System.arraycopy(srcArray,srcArray.length-copyingLimit,featureScenarios,0,copyingLimit);

        return featureScenarios;
    }

    private static int[] copyLastElements(int[] srcArray, int copyingLimit) {
        // if there is less elements than the limit then return array unchanged
        if (srcArray.length <= copyingLimit) {
            return srcArray;
        }

        int[] dest = new int[copyingLimit];
        System.arraycopy(srcArray, srcArray.length - copyingLimit, dest, 0, copyingLimit);

        return dest;
    }

    private static long[] copyLastElements(long[] srcArray, int copyingLimit) {
        // if there is less elements than the limit then return array unchanged
        if (srcArray.length <= copyingLimit) {
            return srcArray;
        }

        long[] dest = new long[copyingLimit];
        System.arraycopy(srcArray, srcArray.length - copyingLimit, dest, 0, copyingLimit);

        return dest;
    }

    private static String[] copyLastElements(String[] srcArray, int copyingLimit) {
        // if there is less elements than the limit then return array unchanged
        if (srcArray.length <= copyingLimit) {
            return srcArray;
        }

        String[] dest = new String[copyingLimit];
        System.arraycopy(srcArray, srcArray.length - copyingLimit, dest, 0, copyingLimit);

        return dest;
    }

    /**
     * Due to the error with old implementation where total features
     * were passed instead of failures (and vice versa) following correction must be applied for trends generated
     * between release 3.0.0 and 3.1.0.
     */
    private void applyPatchForFeatures() {
        for (int i = 0; i < totalFeatures.length; i++) {
            int total = totalFeatures[i];
            int failures = getFailedFeatures()[i];
            if (total < failures) {
                // this data must be changed since it was generated by invalid code
                int tmp = total;
                totalFeatures[i] = failures;
                failedFeatures[i] = tmp;
            }
        }
    }

    /**
     * Since pending and undefined steps were added later
     * there is need to fill missing data for those statuses.
     */
    private void fillMissingSteps() {
        // correct only pending and undefined steps
        passedFeatures = fillMissingArray(passedFeatures);
        passedScenarios = fillMissingArray(passedScenarios);

        passedSteps = fillMissingArray(passedSteps);
        skippedSteps = fillMissingArray(skippedSteps);
        pendingSteps = fillMissingArray(pendingSteps);
        undefinedSteps = fillMissingArray(undefinedSteps);
    }

    private int[] fillMissingArray(int[] arrayToExtend) {
        int[] extendedArray = new int[buildNumbers.length];
        System.arraycopy(arrayToExtend, 0, extendedArray, buildNumbers.length - arrayToExtend.length, arrayToExtend.length);
        return extendedArray;
    }

    /**
     * Since durations were added later there is need to fill missing data for those statuses.
     */
    private void fillMissingDurations() {
        long[] extendedArray = new long[buildNumbers.length];
        Arrays.fill(extendedArray, -1);
        System.arraycopy(durations, 0, extendedArray, buildNumbers.length - durations.length, durations.length);
        durations = extendedArray;
    }

    private void fillMissingFeaturesDetail(){
        FeatureScenario[][] extendedFeaturesDetail = new FeatureScenario[buildNumbers.length][];
        FeatureScenario[] featureScenarios = new FeatureScenario[0];

        Arrays.fill(extendedFeaturesDetail, featureScenarios);
        System.arraycopy(featuresDetail, 0, extendedFeaturesDetail, buildNumbers.length - featuresDetail.length, featuresDetail.length);
        featuresDetail = extendedFeaturesDetail;
    }
}
