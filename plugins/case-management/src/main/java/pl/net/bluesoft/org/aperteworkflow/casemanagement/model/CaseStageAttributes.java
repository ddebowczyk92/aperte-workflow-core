package pl.net.bluesoft.org.aperteworkflow.casemanagement.model;

/**
 * Created by pkuciapski on 2014-06-26.
 */
public enum CaseStageAttributes {
    STAGE_FILES("stageFiles");

    private final String value;

    CaseStageAttributes(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
