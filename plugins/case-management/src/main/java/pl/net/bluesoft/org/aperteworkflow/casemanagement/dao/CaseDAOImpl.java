package pl.net.bluesoft.org.aperteworkflow.casemanagement.dao;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.persister.entity.AbstractEntityPersister;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.*;
import pl.net.bluesoft.org.aperteworkflow.casemanagement.model.query.FindComplaintQueryBuilder;
import pl.net.bluesoft.rnd.processtool.hibernate.SimpleHibernateBean;

import java.math.BigInteger;
import java.util.*;

import static pl.net.bluesoft.org.aperteworkflow.casemanagement.model.query.FindComplaintQueryBuilder.*;

/**
 * Created by pkuciapski on 2014-04-22.
 */
public class CaseDAOImpl extends SimpleHibernateBean<Case> implements CaseDAO {
    private CaseStateDefinitionDAO caseStateDefinitionDAO;

    public CaseDAOImpl(final Session session) {
        super(session);
    }

    public CaseDAOImpl(final Session session, final CaseStateDefinitionDAO caseStateDefinitionDAO) {
        this(session);
        this.caseStateDefinitionDAO = caseStateDefinitionDAO;
    }

    @Override
    public Case createCase(CaseDefinition definition, String name, String number) {
        return createCase(definition, name, number, null);
    }

    @Override
    public Case createCase(CaseDefinition definition, String name, String number, String initialState) {
        Case newCase = new Case();
        newCase.setName(name);
        newCase.setNumber(number);
        newCase.setCreateDate(new Date());
        newCase.setDefinition(definition);
        // get the initial state from the case definition
        CaseStateDefinition initialStateDef;
        if (initialState == null) {
            initialStateDef = definition.getInitialState();
        } else {
            initialStateDef = caseStateDefinitionDAO.getStateDefinitionByName(initialState, definition);
        }

        if (initialStateDef == null)
            throw new RuntimeException("No state definition name: " + initialState);

        // add the initial stage
        CaseStage initialStage = addStage(newCase, initialStateDef);
        newCase.setCurrentStage(initialStage);

        saveOrUpdate(newCase);
        return newCase;
    }

    private Set<CaseSimpleAttribute> addSimpleAttributes(final Case caseInstance, final Map<String, String> simpleAttributes) {
        final Set<CaseSimpleAttribute> attrs = new HashSet<CaseSimpleAttribute>();
        for (Map.Entry<String, String> entry : simpleAttributes.entrySet()) {
            final CaseSimpleAttribute a = new CaseSimpleAttribute();
            a.setKey(entry.getKey());
            a.setValue(entry.getValue());
            a.setCase(caseInstance);
            attrs.add(a);
        }
        return attrs;
    }

    private CaseStage addStage(final Case caseInstance, final CaseStateDefinition stateDefinition) {
        final CaseStage stage = new CaseStage();
        stage.setStartDate(new Date());
        stage.setCaseStateDefinition(stateDefinition);
        stage.setCase(caseInstance);
        stage.setName(stateDefinition.getName());
        caseInstance.getStages().add(stage);
        return stage;
    }

    @Override
    public Case getCaseById(final long caseId) {
        final Case caseInstance = (Case) this.session.get(Case.class, caseId);
        return caseInstance;
    }

    @Override
    public Collection<Case> getAllCases() {
        Criteria criteria = this.session.createCriteria(Case.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return setFetchModes(criteria)
                .list();
    }

    private Criteria setFetchModes(Criteria criteria) {
        return criteria.setFetchMode("currentStage", FetchMode.SELECT)
                .setFetchMode("definition", FetchMode.SELECT)
                .setFetchMode("simpleAttributes", FetchMode.SELECT)
                .setFetchMode("attributes", FetchMode.SELECT)
                .setFetchMode("processInstances", FetchMode.SELECT)
                .setFetchMode("stages", FetchMode.SELECT)
                ;
    }

    @Override
    public Collection<Case> getAllCasesPaged(final String sortColumnProperty, final boolean sortAscending, final int pageLength, final int pageOffset) {
        Query query = this.session.createQuery("from Case order by " + sortColumnProperty + " " + (sortAscending ? "asc" : "desc"));
        query.setMaxResults(pageLength)
                .setFirstResult(pageOffset);
        return query.list();
    }

    @Override
    public Collection<Case> findCasesByName(String name) {
        Criteria criteria = this.session.createCriteria(Case.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = setFetchModes(criteria);

        if (name != null && !name.isEmpty())
            criteria.add(Restrictions.like("name", name));

        return criteria.list();
    }

    @Override
    public Collection<Case> findCasesByNumberAndSimpleAttr(String number, String key, String value, int pageLength, int pageNumber) {
        Criteria caseQueryCriteria = prepareCriteriaForCaseNumberAndSimpleAttr(number, key, value);
        caseQueryCriteria.addOrder(Order.asc("number")).
                setMaxResults(pageLength).setFirstResult((pageNumber - 1) * pageLength);
        return caseQueryCriteria.list();
    }

    @Override
    public Long getCasesByNumberAndSimpleAttrCount(String number, String key, String value) {
        Criteria criteria = prepareCriteriaForCaseNumberAndSimpleAttr(number, key, value);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private Criteria prepareCriteriaForCaseNumberAndSimpleAttr(String number, String key, String value) {
        DetachedCriteria attributeSubQueryCriteria = DetachedCriteria.forClass(CaseSimpleAttribute.class, "attr");
        attributeSubQueryCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).
                add(Restrictions.eq("attr.value", value)).
                add(Restrictions.eq("attr.key", key)).
                setProjection(Projections.property("attr.caseInstance.id"));

        Criteria caseQueryCriteria = this.session.createCriteria(Case.class);
        setFetchModes(caseQueryCriteria).add(Subqueries.propertyIn("id", attributeSubQueryCriteria));
        if (number != null && !number.isEmpty()) {
            caseQueryCriteria.add(Restrictions.like("number", number, MatchMode.ANYWHERE).ignoreCase());
        }
        return caseQueryCriteria;
    }

    @Override
    public Case findCaseByNo(String number) {
        if (number == null || number.isEmpty()) {
            return null;
        }

        Criteria criteria = this.session.createCriteria(Case.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        criteria = setFetchModes(criteria);
        criteria.add(Restrictions.eq("number", number));

        return (Case) criteria.uniqueResult();
    }

    @Override
    public Long getAllCasesCount() {
        return (Long) this.session.createCriteria(Case.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long getAllNotClosedCasesCount() {
        String sqlQuery = "select count(*) from pt_case join pt_case_stage on pt_case.current_case_stage_id = pt_case_stage.id where pt_case_stage.name ilike 'new' or pt_case_stage.name ilike 'open'";
        Query query = getSession().createSQLQuery(sqlQuery);
        BigInteger result = (BigInteger) query.uniqueResult();
        return result.longValue();
    }

    @Override
    public void updateCase(final Case caseInstance) {
        getSession().update(caseInstance);
    }

    @Override
    public List<Case> getCasesPaged(final String sortColumnProperty, final boolean sortAscending, final int pageLength, final int pageOffset, Map<String, Object> params) {
        Query query = getSession().getNamedQuery(SelectComplaintCase);
        String orderColumnName = ((AbstractEntityPersister) getSession().getSessionFactory().getClassMetadata(Case.class)).getPropertyColumnNames(sortColumnProperty)[0];
        if (orderColumnName == null)
            orderColumnName = sortColumnProperty;
        setQueryParams(query, params);
        query.setString(OrderBy, orderColumnName);
        query.setBoolean(AscOrder, sortAscending);
        query.setInteger(PageSize, pageLength);
        query.setInteger(CurrentPage, pageOffset);
        return query.list();
    }

    private void setQueryParams(Query query, Map<String, Object> params) {
        setString(PersonName, query, params);
        setString(Pir, query, params);
        query.setDate(FlightDate, (Date) params.get(FlightDate));
        query.setDate(FlightDateTo, (Date) params.get(FlightDateTo));
        query.setBoolean(FlightDateRange, (Boolean)params.get(FlightDateRange));
        setString(FlightNo, query, params);
        setString(AssignedPerson, query, params);
        setString(CaseNumber, query, params);
        setString(CaseShortNumber, query, params);
        setString(ComplaintType, query, params);
        setString(Categories, query, params);
        setString(Stages, query, params);
        query.setDate(CreateDate, (Date) params.get(CreateDate));
        query.setDate(CreateDateTo, (Date) params.get(CreateDateTo));
        query.setBoolean(CreateDateRange, (Boolean)params.get(CreateDateRange));
        setString(TextSearch, query, params);
    }

    @Override
    public Long getCasesCount(Map<String, Object> params) {
        Query query = getSession().getNamedQuery(FindComplaintQueryBuilder.SelectComplaintCaseCount);
        setQueryParams(query, params);
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    private void setString(String key, Query query, Map<String, Object> params) {
        query.setString(key, (String) params.get(key));
    }

    private Criteria createCriteria() {
        return getSession().createCriteria(Case.class);
    }


}
