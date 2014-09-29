package pl.net.bluesoft.org.aperteworkflow.casemanagement.model.query;

import pl.net.bluesoft.rnd.processtool.model.UserData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rafał Surowiecki
 * Date: 21.05.14
 * Time: 17:26
 */
public class FindComplaintQueryBuilder {

    private static final String VIEW_ROLE_NAME = "VIEW_COMPLAINT_";
    private static final String EDIT_ROLE_NAME = "EDIT_COMPLAINT_";

    public static String SelectComplaintCase = "selectComplaintCase";
    public static String SelectComplaintCaseCount = "selectComplaintCaseCount";

    public static final String PersonName = "personName";
    public static final String Pir = "pir";
    public static final String FlightDate = "flightDate";
    public static final String FlightDateTo = "flightDateTo";
    public static final String FlightDateRange = "flightDateRange";
    public static final String FlightNo = "flightNo";
    public static final String AssignedPerson = "assignedPerson";
    public static final String CaseNumber = "caseNumber";
    public static final String CaseShortNumber = "caseShortNumber";
    public static final String ComplaintType = "complaintType";
    public static final String CreateDate = "createDate";
    public static final String CreateDateTo = "createDateTo";
    public static final String CreateDateRange = "createDateRange";
    public static final String Categories = "categories";
    public static final String Stages = "stages";
    public static final String TextSearch = "textSearch";

    public static final String OrderBy = "in_orderBy";
    public static final String AscOrder = "ascOrder";
    public static final String PageSize = "pageSize";
    public static final String CurrentPage = "currentPage";

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
        params.put(FlightNo, value);
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
