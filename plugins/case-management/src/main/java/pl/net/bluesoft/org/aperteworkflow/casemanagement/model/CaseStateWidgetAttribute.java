package pl.net.bluesoft.org.aperteworkflow.casemanagement.model;

import org.hibernate.annotations.Index;
import pl.net.bluesoft.rnd.processtool.model.PersistentEntity;
import pl.net.bluesoft.rnd.processtool.model.config.IStateWidgetAttribute;
import pl.net.bluesoft.util.lang.Lang;

import javax.persistence.*;

/**
 * Created by pkuciapski on 2014-04-18.
 */
@Entity
@Table(name = "pt_case_state_widget_attr")
@org.hibernate.annotations.Table(
        appliesTo = "pt_case_state_widget_attr",
        indexes = {
                @Index(name = "idx_pt_case_state_wid_attr_pk",
                        columnNames = {"id"}
                )
        })
public class CaseStateWidgetAttribute extends PersistentEntity implements IStateWidgetAttribute {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = CaseStateWidget.CASE_STATE_WIDGET_ID)
    @Index(name = "idx_pt_case_state_wid_attr_id")
    private CaseStateWidget caseStateWidget;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    public CaseStateWidget getCaseStateWidget() {
        return caseStateWidget;
    }

    public void setCaseStateWidget(CaseStateWidget caseStateWidget) {
        this.caseStateWidget = caseStateWidget;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
	public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.key;
    }

	public CaseStateWidgetAttribute deepClone() {
		CaseStateWidgetAttribute result = new CaseStateWidgetAttribute();
		result.key = key;
		result.value = value;
		return result;
	}

	public boolean isSimilar(CaseStateWidgetAttribute attribute) {
		return Lang.equals(key, attribute.key) && Lang.equals(value, attribute.value);
	}
}
