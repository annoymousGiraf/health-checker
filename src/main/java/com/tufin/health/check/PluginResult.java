package com.tufin.health.check;

public class PluginResult {
    private String status;
    private String details;
    private String solution_suggestion;

    PluginResult(){}
    PluginResult(String status, String details, String solution_suggestion) {
        this.status = status;
        this.details = details;
        this.solution_suggestion = solution_suggestion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSolution_suggestion() {
        return solution_suggestion;
    }

    public void setSolution_suggestion(String solution_suggestion) {
        this.solution_suggestion = solution_suggestion;
    }
}
