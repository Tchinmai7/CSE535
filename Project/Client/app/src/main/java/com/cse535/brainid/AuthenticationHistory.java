package com.cse535.brainid;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

public class AuthenticationHistory extends RealmObject {
    private int numCloud;
    private int numFog;
    private int totalAuthAttempts;
    private List<Integer> accuracies = new ArrayList<>();
    private List<Double> cloudLatencies = new ArrayList<>();
    private List<Double> fogLatencies = new ArrayList<>();
    private List<Double> fogPower = new ArrayList<>();
    private List<Double> cloudPower = new ArrayList<>();
    private List<Double> cloudExecutionTimes = new ArrayList<>();
    private List<Double> fogExecutionTimes = new ArrayList<>();
    private List<String> classifiersUsed = new ArrayList<>();

    public List<String> getClassifiersUsed() {
        return classifiersUsed;
    }

    public void setClassifiersUsed(List<String> classifiersUsed) {
        this.classifiersUsed = classifiersUsed;
    }


    public int getNumCloud() {
        return numCloud;
    }

    public void setNumCloud(int numCloud) {
        this.numCloud = numCloud;
    }

    public int getNumFog() {
        return numFog;
    }

    public void setNumFog(int numFog) {
        this.numFog = numFog;
    }

    public int getTotalAuthAttempts() {
        return totalAuthAttempts;
    }

    public void setTotalAuthAttempts(int totalAuthAttempts) {
        this.totalAuthAttempts = totalAuthAttempts;
    }

    public List<Integer> getAccuracies() {
        return accuracies;
    }

    public void setAccuracies(int accuracy) {
        this.accuracies.add(accuracy);
    }

    public List<Double> getCloudLatencies() {
        return cloudLatencies;
    }

    public void setCloudLatencies(List<Double> cloudLatencies) {
        this.cloudLatencies = cloudLatencies;
    }

    public List<Double> getFogLatencies() {
        return fogLatencies;
    }

    public void setFogLatencies(List<Double> fogLatencies) {
        this.fogLatencies = fogLatencies;
    }

    public List<Double> getFogPower() {
        return fogPower;
    }

    public void setFogPower(List<Double> fogPower) {
        this.fogPower = fogPower;
    }

    public List<Double> getCloudPower() {
        return cloudPower;
    }

    public void setCloudPower(List<Double> cloudPower) {
        this.cloudPower = cloudPower;
    }

    public List<Double> getCloudExecutionTimes() {
        return cloudExecutionTimes;
    }

    public void setCloudExecutionTimes(List<Double> cloudExecutionTimes) {
        this.cloudExecutionTimes = cloudExecutionTimes;
    }

    public List<Double> getFogExecutionTimes() {
        return fogExecutionTimes;
    }

    public void setFogExecutionTimes(List<Double> fogExecutionTimes) {
        this.fogExecutionTimes = fogExecutionTimes;
    }

}
