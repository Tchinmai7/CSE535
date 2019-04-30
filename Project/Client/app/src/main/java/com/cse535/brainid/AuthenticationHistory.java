package com.cse535.brainid;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AuthenticationHistory extends RealmObject {
    private int numCloud;
    private int numFog;
    private int totalAuthAttempts;
    private RealmList<Double> accuracies = new RealmList<>();
    private RealmList<Double> cloudLatencies = new RealmList<>();
    private RealmList<Double> fogLatencies = new RealmList<>();
    private RealmList<Double> fogPower = new RealmList<>();
    private RealmList<Double> cloudPower = new RealmList<>();
    private RealmList<Double> cloudExecutionTimes = new RealmList<>();
    private RealmList<Double> fogExecutionTimes = new RealmList<>();
    private RealmList<String> classifiersUsed = new RealmList<>();

    public RealmList<String> getClassifiersUsed() {
        return classifiersUsed;
    }

    public void addClassifier(String classfier) {
        this.classifiersUsed.add(classfier);
    }

    public void setClassifiersUsed(RealmList<String> classifiersUsed) {
        this.classifiersUsed = classifiersUsed;
    }


    public int getNumCloud() {
        return numCloud;
    }

    public void setNumCloud(int numCloud) {
        this.numCloud = numCloud;
    }
    public void addNumCloud() {this.numCloud += 1;}

    public int getNumFog() {
        return numFog;
    }

    public void setNumFog(int numFog) {
        this.numFog = numFog;
    }
    public void addNumFog() {this.numFog += 1;}
    public int getTotalAuthAttempts() {
        return totalAuthAttempts;
    }

    public void setTotalAuthAttempts(int totalAuthAttempts) {
        this.totalAuthAttempts = totalAuthAttempts;
    }
    public void addAuthAttempt() {
        this.totalAuthAttempts += 1;
    }
    public List<Double> getAccuracies() {
        return accuracies;
    }

    public void setAccuracies(double accuracy) {
        this.accuracies.add(accuracy);
    }
    public void addAccuracy(double accuracy) {
        this.accuracies.add(accuracy);
    }
    public List<Double> getCloudLatencies() {
        return cloudLatencies;
    }

    public void setCloudLatencies(RealmList<Double> cloudLatencies) {
        this.cloudLatencies = cloudLatencies;
    }
    public void addCloudLatency(Double latency) {
        this.cloudLatencies.add(latency);
    }
    public List<Double> getFogLatencies() {
        return fogLatencies;
    }

    public void setFogLatencies(RealmList<Double> fogLatencies) {
        this.fogLatencies = fogLatencies;
    }

    public void addFogLatency(Double latency) {
        this.fogLatencies.add(latency);
    }

    public List<Double> getFogPower() {
        return fogPower;
    }

    public void setFogPower(RealmList<Double> fogPower) {
        this.fogPower = fogPower;
    }
    public void addFogPower(Double power) {
        this.fogPower.add(power);
    }

    public List<Double> getCloudPower() {
        return cloudPower;
    }

    public void setCloudPower(RealmList<Double> cloudPower) {
        this.cloudPower = cloudPower;
    }
    public void addCloudPower(Double power) {
        this.cloudPower.add(power);
    }
    public List<Double> getCloudExecutionTimes() {
        return cloudExecutionTimes;
    }

    public void setCloudExecutionTimes(RealmList<Double> cloudExecutionTimes) {
        this.cloudExecutionTimes = cloudExecutionTimes;
    }
    public void addCloudExecutionTime(Double e) {
        this.cloudExecutionTimes.add(e);
    }

    public List<Double> getFogExecutionTimes() {
        return fogExecutionTimes;
    }

    public void setFogExecutionTimes(RealmList<Double> fogExecutionTimes) {
        this.fogExecutionTimes = fogExecutionTimes;
    }
    public void addFogExecutionTime(Double e) {
        this.fogExecutionTimes.add(e);
    }
}
