package pl.net.bluesoft.org.aperteworkflow.casemanagement.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import pl.net.bluesoft.rnd.processtool.model.IAttribute;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by pkuciapski on 2014-05-16.
 */
@Entity
@Table(name = "pt_case_s_l_attr")
@org.hibernate.annotations.Table(
        appliesTo = "pt_case_s_l_attr",
        indexes = {
                @Index(name = "idx_pt_case_s_l_attr_pk",
                        columnNames = {"id"}
                ),
                @Index(name = "idx_pt_case_s_l_attr_case_id", columnNames = Case.CASE_ID)
        })
public class CaseSimpleLargeAttribute extends AbstractCaseAttribute implements IAttribute {
    @Column(name = "value")
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	@Override
	public String toString() {
		return getKey() + '=' + value;
	}
}
