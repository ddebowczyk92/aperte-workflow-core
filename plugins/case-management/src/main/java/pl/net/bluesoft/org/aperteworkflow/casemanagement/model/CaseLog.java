package pl.net.bluesoft.org.aperteworkflow.casemanagement.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import pl.net.bluesoft.rnd.processtool.model.PersistentEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rafał Surowiecki
 * Date: 26.05.14
 * Time: 12:51
 */
@Entity
@Table(name = "pt_case_log")
@org.hibernate.annotations.Table(
        appliesTo = "pt_case_log",
        indexes = {
                @Index(name = "idx_pt_case_log_case_id", columnNames = Case.CASE_ID)
        })
public class CaseLog  extends PersistentEntity{

	public static final String LOG_TYPE_CASE_CHANGE = "CASE_CHANGE";
	private Date entryDate;
	private String eventI18NKey;
	private String logType;
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(length = Integer.MAX_VALUE)
	private String logValue;
	private String userLogin;

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEventI18NKey(String eventI18NKey) {
		this.eventI18NKey = eventI18NKey;
	}

	public String getEventI18NKey() {
		return eventI18NKey;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogValue(String logValue) {
		this.logValue = logValue;
	}

	public String getLogValue() {
		return logValue;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
}
