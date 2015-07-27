package pl.net.bluesoft.lot.casemanagement.model;

import org.hibernate.annotations.Index;
import pl.net.bluesoft.rnd.processtool.model.PersistentEntity;
import pl.net.bluesoft.util.lang.Lang;

import javax.persistence.*;

import static pl.net.bluesoft.lot.casemanagement.model.Constants.COMPLAINTS_SCHEMA;

/**
 * Created by pkuciapski on 2014-05-16.
 */
@Entity
@Table(name = "pt_case_state_process", schema = COMPLAINTS_SCHEMA)
@org.hibernate.annotations.Table(
        appliesTo = "pt_case_state_process",
        indexes = {
                @Index(name = "idx_pt_case_state_proc_pk",
                        columnNames = {"id"}
                )
        })
public class CaseStateProcess extends PersistentEntity {
    public static final String TABLE = COMPLAINTS_SCHEMA + "." + CaseStateProcess.class.getAnnotation(Table.class).name();
    @Column(name = "bpm_definition_key", nullable = false)
    private String bpmDefinitionKey;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = CaseStateDefinition.CASE_STATE_DEFINITION_ID)
    @Index(name = "idx_pt_case_state_proc_def_id")
    private CaseStateDefinition stateDefinition;

    public String getBpmDefinitionKey() {
        return bpmDefinitionKey;
    }

    public void setBpmDefinitionKey(String bpmDefinitionKey) {
        this.bpmDefinitionKey = bpmDefinitionKey;
    }

    public CaseStateDefinition getStateDefinition() {
        return stateDefinition;
    }

    public void setStateDefinition(CaseStateDefinition stateDefinition) {
        this.stateDefinition = stateDefinition;
    }

	public CaseStateProcess deepClone() {
		CaseStateProcess result = new CaseStateProcess();
		result.bpmDefinitionKey = bpmDefinitionKey;
		return result;
	}

	public boolean isSimilar(CaseStateProcess process) {
		return Lang.equals(bpmDefinitionKey, process.bpmDefinitionKey);
	}
}
