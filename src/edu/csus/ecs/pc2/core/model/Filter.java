package edu.csus.ecs.pc2.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.csus.ecs.pc2.core.model.Clarification.ClarificationStates;
import edu.csus.ecs.pc2.core.model.ClientType.Type;
import edu.csus.ecs.pc2.core.model.Run.RunStates;

/**
 * A filter for runs, clients, etc.
 * 
 * Provides a way to determine whether a run, clarification, etc. matches a list of problems, sites, etc. <br>
 * Use the add methods to add items (Problems, RunStates, etc) to match against. <br>
 * Use the match and matches methods to determine if the Run, Clarification, etc matches the filter. <br>
 * Use the setUsing methods to turn on and off filtering of Problems, RunStates, etc.
 * <P>
 * The set methods for various filtering criterias will cause that filter criteria to be used. If filter.setEndElapsedTime() is used then filter.isFilteringElapsedTime() will return true and any
 * subsequent use of filter will filter on the elapsed time.
 * 
 * @author pc2@ecs.csus.edu
 * @version $Id$
 */

// $HeadURL$
public class Filter implements Serializable {

    // TODO filter for site
    // TODO filter for permissions
    // TODO filter for 

    /**
     * 
     */
    private static final long serialVersionUID = -8373119928926075959L;

    /**
     * collection of chosen run states
     */
    private Hashtable<RunStates, Date> runStateHash = new Hashtable<RunStates, Date>();

    /**
     * filtering on run states.
     */
    private boolean filteringRunStates = false;

    private boolean filteringElapsedTime = false;

    private long startElapsedTime = -1;

    private long endElapsedTime = -1;

    /**
     * collection of chosen clarification states
     */
    private Hashtable<ClarificationStates, Date> clarificationStateHash = new Hashtable<ClarificationStates, Date>();

    /**
     * filtering on clarification state
     */
    private boolean filteringClarificationStates = false;

    /**
     * collection of problem ids
     */
    private Hashtable<ElementId, Date> problemIdHash = new Hashtable<ElementId, Date>();

    /**
     * filtering on problem (problem id)
     */
    private boolean filteringProblems = false;

    /**
     * collection of language ids
     */
    private Hashtable<ElementId, Date> languageIdHash = new Hashtable<ElementId, Date>();
    
    /**
     * Collection of site ids.
     */
    private Hashtable<Integer, Date> siteIdHash = new Hashtable<Integer, Date>();
    

    /**
     * filtering on language (language id)
     */
    private boolean filteringLanguages = false;
    
    /**
     * filtering on site (site number)
     */
    private boolean filteringSites = false;


    /**
     * collection of judgement ids
     */
    private Hashtable<ElementId, Date> judgementIdHash = new Hashtable<ElementId, Date>();

    /**
     * filtering on judgements (judgement id)
     */
    private boolean filteringJudgements = false;

    /**
     * Collection of Account/Clients
     */
    private Hashtable<ClientId, Date> clientIdHash = new Hashtable<ClientId, Date>();

    private Hashtable<Type, Date> clientTypeHash = new Hashtable<Type, Date>();

    private boolean filteringAccounts = false;

    /**
     * filtering for this site only
     */
    private boolean thisSiteOnly;

    private int filterSiteNumber = 0;

    private boolean filterEnabled = true;

    private ElementId getJudgementId(Run run) {
        JudgementRecord judgementRecord = run.getJudgementRecord();
        if (judgementRecord != null) {
            return judgementRecord.getJudgementId();
        }
        return null;
    }
    
    /**
     * Match criteria against a run.
     * 
     * @param run
     */
    public boolean matches(Run run) {
        if (filterEnabled){
            ElementId judgementElementId = getJudgementId(run);
            return matchesSites(run) && matchesAccount(run.getSubmitter()) && matchesRunState(run.getStatus()) && matchesProblem(run.getProblemId()) && matchesLanguage(run.getLanguageId())
             && matchesJudgement(judgementElementId) && matchesElapsedTimeSubmission(run);
        } else {
            return true;
        }
    }

    /**
     * Match criteria against a clar.
     * 
     * @param clarification
     * @return true if the clarifications matches the filter
     */
    public boolean matches(Clarification clarification) {
        if (filterEnabled){
            return matchesSites(clarification) && matchesAccount(clarification.getSubmitter()) && matchesClarificationState(clarification.getState()) 
            && matchesProblem(clarification.getProblemId()) && matchesLanguage(clarification.getLanguageId()) && matchesElapsedTimeSubmission(clarification);

        } else {
            return true;
        }
    }

    /**
     * Match criteria against a clientId.
     * 
     * @param clientId
     * @return true if the sites match
     */
    public boolean matches(ClientId clientId) {
        if (filterEnabled){
            return matchesSites(clientId) && matchesAccount(clientId);
        } else {
            return true;
        }
    }

    public boolean matchesSites(ClientId clientId) {
        return matchesSites(clientId.getSiteNumber());
    }

    public boolean isThisSiteOnly() {
        return thisSiteOnly;
    }

    public void setThisSiteOnly(boolean thisSiteOnly) {
        this.thisSiteOnly = thisSiteOnly;
    }

    // TODO unused code ?
//    public int isSiteNumber() {
//        return filterSiteNumber;
//    }

    public void setSiteNumber(int siteNumber) {
        this.filterSiteNumber = siteNumber;
    }

    public int getSiteNumber() {
        return filterSiteNumber;
    }

    public void setUsingJudgementFilter(boolean turnOn) {
        filteringJudgements = turnOn;
    }

    /**
     * Is filtering using judgement list.
     * 
     * @return true if filter judgements.
     */
    public boolean isFilteringJudgements() {
        return filteringJudgements;
    }

    /**
     * Add a judgement to match against.
     * 
     * @param judgement
     */
    public void addJudgement(Judgement judgement) {
        addJudgement(judgement.getElementId());
    }

    /**
     * Add a judgement to match against.
     * 
     * Also turns filtering on for judgement list.
     * 
     * @param elementId
     */
    private void addJudgement(ElementId elementId) {
        filteringJudgements = true;
        judgementIdHash.put(elementId, new Date());
    }

    /**
     * Return true if judgement filter ON and matches a judgement in the filter list.
     * 
     * @param judgement
     */
    public boolean matchesJudgement(Judgement judgement) {
        return matchesJudgement(judgement.getElementId());
    }

    /**
     * Return true if judgement filter ON and matches a judgement in the filter list.
     * 
     * @param judgementId
     */
    public boolean matchesJudgement(ElementId judgementId) {

        if (filteringJudgements) {
            if (judgementId == null) {
                return false;
            } else {
                return judgementIdHash.containsKey(judgementId);
            }

        } else {
            return true;
        }
    }

    public void setUsingProblemFilter(boolean turnOn) {
        filteringProblems = turnOn;
    }

    /**
     * Is filtering using problem list.
     * 
     * @return true if filter problems.
     */
    public boolean isFilteringProblems() {
        return filteringProblems;
    }

    /**
     * Add a problem to match against.
     * 
     * @param problem
     */
    public void addProblem(Problem problem) {
        addProblem(problem.getElementId());
    }

    /**
     * Add a problem to match against.
     * 
     * Also turns filtering on for problem list.
     * 
     * @param elementId
     */
    private void addProblem(ElementId elementId) {
        problemIdHash.put(elementId, new Date());
        filteringProblems = true;
    }

    /**
     * Return true if problem filter ON and matches a problem in the filter list.
     * 
     * @param problem
     */
    public boolean matchesProblem(Problem problem) {
        return matchesProblem(problem.getElementId());
    }

    /**
     * Return true if problem filter ON and matches a problem in the filter list.
     * 
     * @param problemId
     */
    public boolean matchesProblem(ElementId problemId) {
        if (filteringProblems) {
            return problemIdHash.containsKey(problemId);
        } else {
            return true;
        }
    }

    public void setUsingLanguageFilter(boolean turnOn) {
        filteringLanguages = turnOn;
    }
    

    public void setUsingSitesFilter(boolean turnOn) {
        filteringSites = turnOn;
    }

    public boolean isFilteringSites() {
        return filteringSites;
    }
    
    /**
     * Is filtering using language list.
     * 
     * @return true if filter languages.
     */
    public boolean isFilteringLanguages() {
        return filteringLanguages;
    }

    /**
     * Add a language to match against.
     * 
     * @param language
     */
    public void addLanguage(Language language) {
        addLanguage(language.getElementId());
    }

    /**
     * Add a language to match against.
     * 
     * Also turns filtering on for language list.
     * 
     * @param elementId
     */
    private void addLanguage(ElementId elementId) {
        languageIdHash.put(elementId, new Date());
        filteringLanguages = true;
    }

    /**
     * Return true if language filter ON and matches a language in the filter list.
     * 
     * @param language
     */
    public boolean matchesLanguage(Language language) {
        return matchesLanguage(language.getElementId());
    }

    /**
     * Return true if language filter ON and matches a language in the filter list.
     * 
     * @param languageId
     */
    public boolean matchesLanguage(ElementId languageId) {
        if (filteringLanguages) {
            return languageIdHash.containsKey(languageId);
        } else {
            return true;
        }
    }

    /**
     * Add a site to match against.
     * 
     * @param site
     */
    public void addSite(Site site) {
        addSite(site.getSiteNumber());
    }

    /**
     * Add a site to match against.
     * 
     * Also turns filtering on for site list.
     * 
     * @param elementId
     */
    public void addSite(int siteNumber) {
        siteIdHash.put(new Integer(siteNumber), new Date());
        filteringSites = true;
    }
    
    private boolean matchesSites (ISubmission submission) {
        return matchesSites (submission.getSiteNumber());
    }


    private boolean matchesSites(int siteNumber) {
        if (filteringSites) {
            return siteIdHash.containsKey(new Integer(siteNumber));
        } else {
            return true;
        }
    }

    /**
     * Return true if site filter ON and matches a site in the filter list.
     * 
     * @param site
     */
    public boolean matchesSite(Site site) {
        return matchesSites(site.getSiteNumber());
    }

    public boolean matchesAccount(Account account) {
        if (filteringAccounts) {
            if (matchesAccount(account.getClientId())) {
                return true;
            } else if (matchesClientType(account.getClientId().getClientType())) {
                return true;
            }
            return false;
        } else {
            return true;
        }
    }

    private boolean matchesClientType(Type type) {
        if (filteringAccounts) {
            return clientTypeHash.containsKey(type);
        } else {
            return false;
        }
    }

    public boolean matchesAccount(ClientId clientId) {
        if (filteringAccounts) {
//            System.out.println(new FilterFormatter().getClientsShortList(getAccountList()));
            return clientIdHash.containsKey(clientId);
        } else {
            return true;
        }
    }
    
    /**
     * Clear judgements and turn judgement filtering off.
     *
     */
    public void clearJudgementList() {
        filteringJudgements = false;
        judgementIdHash = new Hashtable<ElementId, Date>();
    }



    /**
     * Clear accounts and turn account filtering off.
     *
     */
    public void clearAccountList() {
        filteringAccounts = false;
        clientIdHash = new Hashtable<ClientId, Date>();
    }

    /**
     * Clear problem and turn problem filtering off.
     *
     */
    public void clearProblemList() {
        filteringProblems = false;
        problemIdHash = new Hashtable<ElementId, Date>();
    }
    
    /**
     * Clear language and turn language filtering off.
     *
     */
    public void clearLanguageList() {
        filteringLanguages = false;
        languageIdHash = new Hashtable<ElementId, Date>();
    }
    
    /**
     * Clear site and turn site filtering off.
     *
     */
    public void clearSiteList() {
        filteringSites = false;
        siteIdHash = new Hashtable<Integer, Date>();
    }

    public void removeSite(Site site) {
        if (siteIdHash.containsKey(site.getElementId())) {
            siteIdHash.remove(site.getElementId());
        }
    }

    public void removeProblem(Problem problem) {
        if (problemIdHash.containsKey(problem.getElementId())) {
            problemIdHash.remove(problem.getElementId());
        }
    }
    
    /**
     * Get list of ElementIds for the judgement in the filter list.
     * 
     * @return list of element ids.
     */
    public ElementId[] getJudgementIdList() {
        ElementId[] elementIds = new ElementId[judgementIdHash.size()];
        Enumeration<ElementId> enumeration = judgementIdHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            ElementId element = (ElementId) enumeration.nextElement();
            elementIds[i] = element;
            i++;
        }
        return elementIds;
    }
   

    /**
     * Get list of ElementIds for the problems in the filter list.
     * 
     * @return list of element ids.
     */
    public ElementId[] getProblemIdList() {
        ElementId[] elementIds = new ElementId[problemIdHash.size()];
        Enumeration<ElementId> enumeration = problemIdHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            ElementId element = (ElementId) enumeration.nextElement();
            elementIds[i] = element;
            i++;
        }
        return elementIds;
    }
    
    /**
     * Get list of ElementIds for the languages in the filter list.
     * 
     * @return list of element ids.
     */
    public ElementId[] getLanguageIdList() {
        ElementId[] elementIds = new ElementId[languageIdHash.size()];
        Enumeration<ElementId> enumeration = languageIdHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            ElementId element = (ElementId) enumeration.nextElement();
            elementIds[i] = element;
            i++;
        }
        return elementIds;
    }

    /**
     * Get list of ElementIds for the sites in the filter list.
     * 
     * @return list of element ids.
     */
    public Integer[] getSiteIdList() {
        Integer[] elementIds = new Integer[siteIdHash.size()];
        Enumeration<Integer> enumeration = siteIdHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            Integer element = (Integer) enumeration.nextElement();
            elementIds[i] = element;
            i++;
        }
        return elementIds;
    }


    
    /**
     * Get list of ClientIds for the accounts in the filter list.
     * 
     * @return list of ClientId.
     */
    public ClientId[] getAccountList() {
        ClientId[] clientIds = new ClientId[clientIdHash.size()];
        Enumeration<ClientId> enumeration = clientIdHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            ClientId element = (ClientId) enumeration.nextElement();
            clientIds[i] = element;
            i++;
        }
        return clientIds;
    }
    
    public void setUsingRunStatesFilter(boolean turnOn) {
        filteringRunStates = turnOn;
    }

    public boolean isFilteringRunStates() {
        return filteringRunStates;
    }
    
    public void addAccount(Account account) {
        addAccount(account.getClientId());
    }

    public void addAccount(ClientId clientId) {
        filteringAccounts = true;
        clientIdHash.put(clientId, new Date());
    }

    public void addAccounts(Account[] accounts) {
        for (Account account : accounts) {
            addAccount(account);
        }
    }

    public void removeAccount(Account account) {
        if (clientIdHash.get(account.getClientId()) != null) {
            clientIdHash.remove(account.getClientId());
        }
    }

    public void removeAccounts(Account[] accounts) {
        for (Account account : accounts) {
            removeAccount(account);
        }
    }

    public void addClientType(Type type) {
        filteringAccounts = true;
        clientTypeHash.put(type, new Date());
    }

    public void addRunState(RunStates runStates) {
        runStateHash.put(runStates, new Date());
        filteringRunStates = true;
    }

    public boolean matchesRunState(RunStates runStates) {
        if (filteringRunStates) {
            return runStateHash.containsKey(runStates);
        } else {
            return true;
        }
    }

    public boolean matchesElapsedTime(Run run) {
        return matchesElapsedTimeSubmission(run);
    }

    public boolean matchesElapsedTime(Clarification clarification) {
        return matchesElapsedTimeSubmission(clarification);
    }

    protected boolean matchesElapsedTimeSubmission(ISubmission submission) {
        if (filteringElapsedTime) {

            long elapsedTime = submission.getElapsedMins();

            if (startElapsedTime != -1) {
                if (elapsedTime < startElapsedTime) {
                    return false;
                }
            }

            if (endElapsedTime != -1) {
                if (elapsedTime > endElapsedTime) {
                    return false;
                }
            }
            return true;

        } else {
            return true;
        }
    }

    public void clearRunStatesList() {
        filteringRunStates = false;
        runStateHash = new Hashtable<RunStates, Date>();
    }

    public void removeRunState(RunStates runStates) {
        if (runStateHash.containsKey(runStates)) {
            runStateHash.remove(runStates);
        }
    }

    public void removeClarificationStates(ClarificationStates clarificationStates) {
        if (clarificationStateHash.containsKey(clarificationStates)) {
            clarificationStateHash.remove(clarificationStates);
        }
    }

    public ClarificationStates[] getClarificationStatesList() {
        ClarificationStates[] clarificationStates = new ClarificationStates[clarificationStateHash.size()];
        Enumeration<ClarificationStates> enumeration = clarificationStateHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            clarificationStates[i] = (ClarificationStates) enumeration.nextElement();
            i++;
        }
        return clarificationStates;
    }

    public RunStates[] getRunStates() {
        RunStates[] runStates = new RunStates[runStateHash.size()];
        Enumeration<RunStates> enumeration = runStateHash.keys();
        int i = 0;
        while (enumeration.hasMoreElements()) {
            runStates[i] = (RunStates) enumeration.nextElement();
            i++;
        }
        return runStates;
    }

    public boolean matchesClarificationState(ClarificationStates clarificationStates) {
        if (filteringClarificationStates) {
            return clarificationStateHash.containsKey(clarificationStates);
        } else {
            return true;
        }
    }

    public void addClarificationState(ClarificationStates clarificationStates) {
        clarificationStateHash.put(clarificationStates, new Date());
        filteringClarificationStates = true;
    }

    public void clearClarificationStateList() {
        filteringClarificationStates = false;
        clarificationStateHash = new Hashtable<ClarificationStates, Date>();
    }

    public String toString() {

        if (filterEnabled) {
            
            String filterInfo = "Filter ON";
            
            if (thisSiteOnly || filteringSites) {
                filterInfo += " Site(s) ";
            }
            if (filteringProblems) {
                filterInfo += " problem(s) ";
            }
            if (filteringJudgements) {
                filterInfo += " judgement(s) ";
            }
            if (filteringLanguages) {
                filterInfo += " language(s) ";
            }
            if (filteringRunStates) {
                filterInfo += " run state(s)";
            }
            if (filteringClarificationStates) {
                filterInfo += " clar state(s)";
            }
            if (filteringAccounts) {
                filterInfo += " account(s))";
            }

            return filterInfo;
        } else {
            return "";
        }
    }

    public long getStartElapsedTime() {
        return startElapsedTime;
    }

    public void setStartElapsedTime(long startElapsedTime) {
        filteringElapsedTime = true;
        this.startElapsedTime = startElapsedTime;
    }

    public long getEndElapsedTime() {
        return endElapsedTime;
    }

    public void setEndElapsedTime(long endElapsedTime) {
        filteringElapsedTime = true;
        this.endElapsedTime = endElapsedTime;
    }

    public boolean isFilteringElapsedTime() {
        return filteringElapsedTime;
    }

    public void setFilteringElapsedTime(boolean filteringElapsedTime) {
        this.filteringElapsedTime = filteringElapsedTime;
    }

    public boolean isFilteringAccounts() {
        return filteringAccounts;
    }

    public void setFilteringAccounts(boolean filteringAccounts) {
        this.filteringAccounts = filteringAccounts;
    }

    public void setFilterOff() {
        filterEnabled = false;
    }

    public void setFilter (boolean filterOn){
        filterEnabled = filterOn;
    }
    
    public void setFilterOn () {
        filterEnabled = true;
    }

    /**
     * Is one of the filters active?.
     * 
     * @return true if filter is on, false if not filtering.
     */
    public boolean isFilterOn() {
        if (filterEnabled) {
            return filteringSites || filteringAccounts || filteringClarificationStates || filteringProblems 
                || filteringJudgements || filteringLanguages || filteringElapsedTime || filteringRunStates
                || thisSiteOnly;
        } else {
            return false;
        }
    }

    public boolean isFilteringClarificationStates() {
        return filteringClarificationStates;
    }

    public void clearElapsedTime() {
        startElapsedTime = -1;
        endElapsedTime = -1;
        filteringElapsedTime = false;
    }
    
    /**
     * Count the runs that match this filter.
     * 
     * @param runs - list of runs
     * @return number of runs that match this filter.
     */
    public int countRuns(Run [] runs){
        int count = 0;
        for (Run run : runs) {
            if (matches(run)) {
                count++;
            }
        }
        return count;
    }
}
