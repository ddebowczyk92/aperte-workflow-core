package pl.net.bluesoft.lot.casemanagement.model.query;

import org.apache.commons.lang3.StringUtils;
import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static pl.net.bluesoft.lot.casemanagement.model.query.FindComplaintQueryParams.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rafał Surowiecki
 * Date: 21.05.14
 * Time: 17:26
 */
public class FindComplaintQueryBuilder {

    private static final String VIEW_ROLE_NAME = "SHOW_COMPLAINT_";
    private static final String EDIT_ROLE_NAME = "EDIT_COMPLAINT_";

    public static String SelectComplaintCase = "selectComplaintCase";
    public static String SelectComplaintCaseCount = "selectComplaintCaseCount";

    private Map<String, Object> params = new HashMap<String, Object>();

    public Map<String, Object> build() {
        return params;
    }

    public FindComplaintQueryBuilder withPersonName(String value) {
        params.put(PersonName, value);
        return this;
    }

    public FindComplaintQueryBuilder withPir(String value) {
        params.put(Pir, value);
        return this;
    }

    public FindComplaintQueryBuilder withFlightDate(String value, SimpleDateFormat format) {
        Date date = null;
        try {
            if (value != null) {
                date = format.parse(value);
                params.put(FlightDate, date);
            }
        } catch (ParseException e) {
            //ignore
        }

        return this;
    }

    public FindComplaintQueryBuilder withFlightDate(Date value) {
        params.put(FlightDate, value);
        return this;
    }

    public FindComplaintQueryBuilder withFlightNo(String value) {

		String flightNo = StringUtils.upperCase(value);

		params.put(FlightNo, flightNo);
        return this;
    }

    public FindComplaintQueryBuilder withAssignedPerson(String value) {
        params.put(AssignedPerson, value);
        return this;
    }

    public FindComplaintQueryBuilder withCaseNumber(String value) {
        params.put(CaseNumber, value);
        return this;
    }

    public FindComplaintQueryBuilder withCaseShortNumber(String value) {
        params.put(CaseShortNumber, value);
        return this;
    }

    public FindComplaintQueryBuilder withComplaintType(String value) {
        params.put(ComplaintType, value);
        return this;
    }

    public FindComplaintQueryBuilder withCreateDate(String value, SimpleDateFormat format) {
        Date date = null;
        try {
            if (value != null) {
                date = format.parse(value);
                params.put(CreateDate, date);
            }
        } catch (ParseException e) {
            //ignore
        }
        return this;
    }

    public FindComplaintQueryBuilder withCategories(String value) {
        params.put(Categories, value);
        return this;
    }

    public FindComplaintQueryBuilder withStages(String value) {
        params.put(Stages, value);
        return this;
    }

    public FindComplaintQueryBuilder forUser(UserData user) {
        String complaintType = (String) params.get(ComplaintType);
        if (complaintType != null) {
            if (!user.hasRole(VIEW_ROLE_NAME + complaintType) && !user.hasRole(EDIT_ROLE_NAME + complaintType)) {
                params.put(ComplaintType, "__DENY__");
            }
        } else {
            StringBuilder types = new StringBuilder();
            for (String role : user.getRoles()) {
                if (role.startsWith(VIEW_ROLE_NAME) || role.startsWith(EDIT_ROLE_NAME)) {
                    types.append(",").append(role.replace(VIEW_ROLE_NAME, "").replace(EDIT_ROLE_NAME, ""));
                }
            }
            params.put(ComplaintType, types.toString().replaceFirst(",", ""));
        }
        return this;
    }

    public FindComplaintQueryBuilder withFlightDateTo(String value, SimpleDateFormat format) {
        Date date = null;
        try {
            if (value != null) {
                date = format.parse(value);
                params.put(FlightDateTo, date);
            }
        } catch (ParseException e) {
            //ignore
        }
        return this;
    }

    public FindComplaintQueryBuilder withCreateDateTo(String value, SimpleDateFormat format) {
        Date date = null;
        try {
            if (value != null) {
                date = format.parse(value);
                params.put(CreateDateTo, date);
            }
        } catch (ParseException e) {
            //ignore
        }
        return this;
    }

    public FindComplaintQueryBuilder withTextSearch(String value) {
        params.put(TextSearch, value);
        return this;
    }

    public FindComplaintQueryBuilder withFlightDateRange(String value) {
        params.put(FlightDateRange, getBooleanValue(value));
        return this;
    }

    private Boolean getBooleanValue(String value) {
        Boolean b;
        try {
            b = Boolean.parseBoolean(value);
        } catch (Exception e) {
            b = Boolean.FALSE;
        }
        return b;
    }

    public FindComplaintQueryBuilder withCreateDateRange(String value) {
        params.put(CreateDateRange, getBooleanValue(value));
        return this;
    }
}
