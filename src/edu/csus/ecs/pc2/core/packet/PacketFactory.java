package edu.csus.ecs.pc2.core.packet;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

import edu.csus.ecs.pc2.core.list.ClarificationList;
import edu.csus.ecs.pc2.core.list.LanguageDisplayList;
import edu.csus.ecs.pc2.core.list.ProblemDisplayList;
import edu.csus.ecs.pc2.core.model.Account;
import edu.csus.ecs.pc2.core.model.Clarification;
import edu.csus.ecs.pc2.core.model.ClientId;
import edu.csus.ecs.pc2.core.model.ClientType;
import edu.csus.ecs.pc2.core.model.ContestTime;
import edu.csus.ecs.pc2.core.model.ElementId;
import edu.csus.ecs.pc2.core.model.Judgement;
import edu.csus.ecs.pc2.core.model.JudgementRecord;
import edu.csus.ecs.pc2.core.model.Language;
import edu.csus.ecs.pc2.core.model.Problem;
import edu.csus.ecs.pc2.core.model.Run;
import edu.csus.ecs.pc2.core.model.RunFiles;
import edu.csus.ecs.pc2.core.model.RunResultFiles;
import edu.csus.ecs.pc2.core.model.Site;
import edu.csus.ecs.pc2.core.packet.PacketType.Type;
import edu.csus.ecs.pc2.core.transport.ConnectionHandlerID;

/**
 * Creates {@link Packet}.
 * 
 * Each packet can be created by using a method in this class. There is a "create" method for each {@link Type}. <br>
 * There are also some methods to extract fields/classes from packets.
 * <P>
 * Typically the contents of a packet is a {@link java.util.Properties}.
 * 
 * @author pc2@ecs.csus.edu
 */

// $HeadURL$
public final class PacketFactory {

    public static final String SVN_ID = "$Id$";

    public static final String LOGIN = "LOGIN";

    public static final String RUN = "RUN";

    public static final String PASSWORD = "PASSWORD";

    public static final String JUDGEMENT_RECORD = "JUDGEMENT_RECORD";
    
    public static final String RUN_LIST = "RUN_LIST";

    public static final String RUN_FILES = "RUN_FILES";

    public static final String LANGUAGE = "LANGUAGE";

    public static final String PROBLEM = "PROBLEM";

    public static final String CLARIFICATION_ANSWER = "CLARIFICATION_ANSWER";

    public static final String REQUESTED_RUN_ELEMENT_ID = "REQUESTED_RUN_ELEMENT_ID";

    public static final String CLIENT_ID = "CLIENT_ID";

    /**
     * Site Number
     */
    public static final String SITE_NUMBER = "SITE_NUMBER";

    public static final String JUDGEMENT = "JUDGEMENT";

    /**
     * Site class.
     */
    public static final String SITE = "SITE";

    /**
     * A single {@link ContestTime}.
     */
    public static final String CONTEST_TIME = "CONTEST_TIME";

    public static final String CLARIFICATION = "CLARIFICATION";

    public static final String REQUESTED_CLARIFICATION_ELEMENT_ID = "REQUESTED_CLARIFICATION_ELEMENT_ID";

    public static final String ACCOUNT = "ACCOUNT";

    public static final String ACCOUNT_ARRAY = "ACCOUNT_ARRAY";

    public static final String CLIENT_TYPE = "CLIENT_TYPE";

    public static final String COUNT = "COUNT";

    public static final String CREATE_ACCOUNT_ACTIVE = "CREATE_ACCOUNT_ACTIVE";

    public static final String ELAPSED_TIME = "ELAPSED_TIME";

    public static final String CONTEST_LENGTH_TIME = "CONTEST_LENGTH_TIME";

    public static final String REMAINING_TIME = "REMAINING_TIME";

    public static final String CONNECTION_HANDLE_ID = "CONNECTION_HANDLE_ID";

    public static final String PROBLEM_DATA_FILES = "PROBLEM_DATA_FILES";

    public static final String LANGUAGE_DISPLAY_LIST = "LANGUAGE_DISPLAY_LIST";

    public static final String PROBLEM_DISPLAY_LIST = "PROBLEM_DISPLAY_LIST";

    public static final String DEFAULT_CLARIFICATION_ANSWER = "DEFAULT_CLARIFICATION_ANSWER";

    public static final String CONTEST_SETTINGS = "CONTEST_SETTINGS";

    public static final String BALOON_SETTINGS = "BALOON_SETTINGS";

    public static final String SITE_LIST = "SITE_LIST";

    public static final String MESSAGE_STRING = "MESSAGE_STRING";

    /**
     * On login, send settings to server.
     * 
     * Usually set to false.s
     */
    public static final String SEND_SETTINGS = "SEND_SETTINGS";

    public static final String RUN_RESULTS_FILE = "RUN_RESULTS_FILE";

    /**
     * Array of {@link Problem}.
     */
    public static final String PROBLEM_LIST = "PROBLEM_LIST";

    /**
     * Array of {@link Language}.
     */
    public static final String LANGUAGE_LIST = "LANGUAGE_LIST";

    /**
     * Array of {@link Judgement}.
     */
    public static final String JUDGEMENT_LIST = "JUDGEMENT_LIST";

    /**
     * Constructor is private as this is a utility class which should not be extended or invoked.
     */
    private PacketFactory() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Create a packet of {@link PacketType.Type#LOGIN_REQUEST}.
     * 
     * @param source -
     *            who is logging in.
     * @param password -
     *            password for login authentication.
     * @param destination -
     *            server to authenticate.
     * @return a {@link PacketType.Type#LOGIN_REQUEST} packet.
     */
    public static Packet createLogin(ClientId source, String password, ClientId destination) {
        Properties prop = new Properties();
        prop.put(LOGIN, source.getClientType() + "" + source.getClientNumber());
        prop.put(PASSWORD, password);
        return createPacket(PacketType.Type.LOGIN_REQUEST, source, destination, prop);
    }

    /**
     * Create a packet.
     * 
     * 
     * @param type -
     *            {@link PacketType.Type}
     * @param source -
     *            who packet sent from.
     * @param destination -
     *            who to be sent to.
     * @param serializable -
     *            contents of packet.
     * @return a Packet.
     */
    private static Packet createPacket(Type type, ClientId source, ClientId destination, Serializable serializable) {
        Packet packet = new Packet(type, source, destination, serializable);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#MESSAGE}.
     * 
     * @param source
     * @param destination
     * @param message
     */
    public static Packet createMessage(ClientId source, ClientId destination, String message) {
        Packet packet = new Packet(Type.MESSAGE, source, destination, message);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_SUBMISSION}.
     * 
     * @param source
     * @param destination
     * @param run
     * @param runFiles
     * @return submitted run packet.
     */
    public static Packet createSubmittedRun(ClientId source, ClientId destination, Run run, RunFiles runFiles) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        prop.put(RUN_FILES, runFiles);
        Packet packet = new Packet(Type.RUN_SUBMISSION, source, destination, prop);
        return packet;
    }

    /**
     * Dump packet info to PrintWriter and System.err.
     * 
     * @param pw
     * @param packet
     */
    public static void dumpPacket(PrintWriter pw, Packet packet) {

        dumpPacket(pw, packet);

        pw.println("Packet " + packet.getType());
        pw.println("  From: " + packet.getSourceId());
        pw.println("    To: " + packet.getDestinationId());
        Object obj = packet.getContent();
        if (obj instanceof Properties) {
            Properties prop = (Properties) obj;
            Enumeration enumeration = prop.keys();

            while (enumeration.hasMoreElements()) {
                String element = (String) enumeration.nextElement();
                pw.println("   key: " + element + " is: " + prop.get(element).getClass().getName()+" "+prop.get(element));
            }
        } else {

            pw.println("  Contains: " + obj.toString() + " " +obj);
        }
        pw.println();

    }

    /**
     * Dump packet to PrintStream.
     * 
     * @param pw
     * @param packet
     */
    public static void dumpPacket(PrintStream pw, Packet packet) {
        pw.println("Packet " + packet.getType());
        pw.println("  From: " + packet.getSourceId());
        pw.println("    To: " + packet.getDestinationId());
        Object obj = packet.getContent();
        if (obj instanceof Properties) {
            Properties prop = (Properties) obj;
            Enumeration enumeration = prop.keys();

            while (enumeration.hasMoreElements()) {
                String element = (String) enumeration.nextElement();
                pw.println("   key: " + element + " is: " + prop.get(element).getClass().getName()+" "+prop.get(element));
            }
        } else {

            pw.println("  Contains: " + obj.toString() + " " +obj);
        }
        pw.println();

    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_AVAILABLE}.
     * 
     * @param source
     * @param destination
     * @param run
     * @return run available packet.
     */
    public static Packet createRunAvailable(ClientId source, ClientId destination, Run run) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        Packet packet = new Packet(Type.RUN_AVAILABLE, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_UPDATE}.
     * 
     * @param source
     * @param destination
     * @param run
     */
    public static Packet createRunUpdated(ClientId source, ClientId destination, Run run) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        Packet packet = new Packet(Type.RUN_UPDATE, source, destination, prop);
        return packet;
    }

    /**
     * Send checked out packet to judges.
     * 
     * 
     * 
     * @param source
     * @param destination
     * @param run
     * @param runFiles
     *            run files or null (if run is not for destination client).
     * @param id
     * @return checked out packet.
     */
    public static Packet createCheckedOutRun(ClientId source, ClientId destination, Run run, RunFiles runFiles, ClientId id) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        if (runFiles != null) {
            prop.put(RUN_FILES, runFiles);
        }
        prop.put(CLIENT_ID, id);

        Packet packet = new Packet(Type.RUN_CHECKOUT, source, destination, prop);
        return packet;

    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_NOTAVAILABLE}.
     * 
     * @param source
     * @param destination
     * @param run
     */
    public static Packet createRunNotAvailable(ClientId source, ClientId destination, Run run) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        Packet packet = new Packet(Type.RUN_NOTAVAILABLE, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_LIST}.
     * 
     * @param source
     * @param destination
     * @param runs
     */
    public static Packet createRunList(ClientId source, ClientId destination, Run [] runs) {
        Properties prop = new Properties();
        prop.put(RUN_LIST, runs);
        Packet packet = new Packet(Type.RUN_LIST, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#CLARIFICATION_LIST}.
     * 
     * @param source
     * @param destination
     * @param clarList
     */
    public static Packet createClarList(ClientId source, ClientId destination, ClarificationList clarList) {
        Packet packet = new Packet(Type.CLARIFICATION_LIST, source, destination, clarList);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_UNCHECKOUT}.
     * 
     * @param source
     * @param destination
     * @param beingJudgingRun
     */
    public static Packet createUnCheckoutRun(ClientId source, ClientId destination, Run beingJudgingRun) {
        Properties prop = new Properties();
        prop.put(RUN, beingJudgingRun);
        Packet packet = new Packet(Type.RUN_UNCHECKOUT, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_JUDGEMENT}.
     * 
     * @param source
     * @param destination
     * @param run
     * @param judgementRecord
     * @param runResultFiles
     */
    public static Packet createRunJudgement(ClientId source, ClientId destination, Run run, JudgementRecord judgementRecord,
            RunResultFiles runResultFiles) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        prop.put(JUDGEMENT_RECORD, judgementRecord);
        if (runResultFiles != null) {
            prop.put(RUN_RESULTS_FILE, runResultFiles);
        }
        Packet packet = new Packet(Type.RUN_JUDGEMENT, source, destination, prop);
        return packet;

    }

    /**
     * Return the value (Object) inside a packet.
     * 
     * If the packet contents is a {@link Properties} object, will retrieve the value for the input key from that {@link Properties}
     * object.
     * 
     * @param packet
     * @param key
     * @return a Object value for a property inside a packet.
     */
    public static Object getObjectValue(Packet packet, String key) {
        try {
            Properties props = (Properties) packet.getContent();
            return props.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Return the String value inside a packet.
     * 
     * @see #getObjectValue(Packet, String)
     * @param packet
     * @param key
     * @return a string value for a property inside a packet.
     */
    public static String getStringValue(Packet packet, String key) {
        return (String) getObjectValue(packet, key);
    }

    /**
     * Return the Boolean value inside a packet.
     * 
     * @see #getObjectValue(Packet, String)
     * @param packet
     * @param key
     * @return a string value for a property inside a packet.
     */
    public static Boolean getBooleanValue(Packet packet, String key) {
        return (Boolean) getObjectValue(packet, key);
    }

    /**
     * Create a packet of {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param language
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Language language) {

        Properties prop = new Properties();
        prop.put(LANGUAGE, language);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    // public static Packet createUpdateSetting(ClientId source, ClientId destination, Problem problem,
    // ProblemDataFiles problemDataFiles) {
    // Properties prop = new Properties();
    // prop.put(PROBLEM, problem);
    // if (problemDataFiles != null) {
    // prop.put(PROBLEM_DATA_FILES, problemDataFiles);
    // }
    // Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
    // return packet;
    // }

    // public static Packet createAddSetting(ClientId source, ClientId destination, BalloonSettings balloonSettings,
    // ClientId userLoginId) {
    // Properties prop = new Properties();
    // prop.put(BALOON_SETTINGS, balloonSettings);
    // Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create a packet of {@link PacketType.Type#ADD_SETTING}.
     * 
     * @param source
     * @param destination
     * @param language
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, Language language) {
        Properties prop = new Properties();
        prop.put(LANGUAGE, language);
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    // public static Packet createAddSetting(ClientId source, ClientId destination, Site site) {
    // Properties prop = new Properties();
    // prop.put(SITE, site);
    // Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create a packet of {@link PacketType.Type#ADD_SETTING}.
     * 
     * @param source
     * @param destination
     * @param judgement
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, Judgement judgement) {
        Properties prop = new Properties();
        prop.put(JUDGEMENT, judgement);
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    // public static Packet createAddSetting(ClientId source, ClientId destination, Problem problem, ProblemDataFiles
    // problemDataFiles) {
    // Properties prop = new Properties();
    // prop.put(PROBLEM, problem);
    // if (problemDataFiles != null) {
    // prop.put(PROBLEM_DATA_FILES, problemDataFiles);
    // }
    // Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * @param source
     * @param destination
     * @param siteNumber
     */
    public static Packet createStartContestClock(ClientId source, ClientId destination, int siteNumber) {
        Properties prop = new Properties();
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.START_CONTEST_CLOCK, source, destination, prop);
        return packet;

    }

    /**
     * Create a packet of {@link PacketType.Type#STOP_CONTEST_CLOCK}.
     * 
     * @param source
     * @param destination
     * @param siteNumber
     */
    public static Packet createStopContestClock(ClientId source, ClientId destination, int siteNumber) {
        Properties prop = new Properties();
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.STOP_CONTEST_CLOCK, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#UPDATE_CLOCK}.
     * 
     * @param source
     * @param destination
     * @param contestTime
     * @param siteNumber
     */
    public static Packet createUpdateContestTime(ClientId source, ClientId destination, ContestTime contestTime, int siteNumber) {
        Properties prop = new Properties();
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        prop.put(CONTEST_TIME, contestTime);
        Packet packet = new Packet(Type.UPDATE_CLOCK, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#ACCOUNT_LOGIN}.
     * 
     * @param source
     * @param destination
     * @param connectionHandlerID
     */
    public static Packet createAccountLogin(ClientId source, ClientId destination, ConnectionHandlerID connectionHandlerID) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, destination);
        prop.put(CONNECTION_HANDLE_ID, connectionHandlerID);
        Packet packet = new Packet(Type.ACCOUNT_LOGIN, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#LOGOUT}.
     * 
     * @param source
     * @param destination
     * @param userId
     */
    public static Packet createLogoff(ClientId source, ClientId destination, ClientId userId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userId);
        Packet packet = new Packet(Type.LOGOUT, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#CLARIFICATION_SUBMISSION_CONFIRM}.
     * 
     * @param source
     * @param destination
     * @param newClarification
     */
    public static Packet createClarSubmissionConfirm(ClientId source, ClientId destination, Clarification newClarification) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, newClarification);
        Packet packet = new Packet(Type.CLARIFICATION_SUBMISSION_CONFIRM, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#CLAR_SUBMISSION}.
     * 
     * @param source
     * @param destination
     * @param clarification2
     */
    public static Packet createClarSubmission(ClientId source, ClientId destination, Clarification clarification2) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification2);
        Packet packet = new Packet(Type.CLAR_SUBMISSION, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#RUN_SUBMISSION_CONFIRM}.
     * 
     * @param source
     * @param destination
     * @param run
     */
    public static Packet createRunSubmissionConfirm(ClientId source, ClientId destination, Run run) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        Packet packet = new Packet(Type.RUN_SUBMISSION_CONFIRM, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#LOGIN_SUCCESS}.
     * 
     * This packet gives the user their {@link ClientId} and all the contest settings.
     * 
     * @param source
     * @param destination
     * @param inContestTime
     * @param siteNumber
     * @param languages
     * @param problems
     * @param judgements
     */
    public static Packet createLoginSuccess(ClientId source, ClientId destination, ContestTime inContestTime, int siteNumber,
            Language[] languages, Problem[] problems, Judgement[] judgements, Site [] sites, Run [] runs) {
        Properties prop = new Properties();
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        prop.put(PacketType.CONTEST_TIME, inContestTime);
        prop.put(CLIENT_ID, destination);
        prop.put(PROBLEM_LIST, problems);
        prop.put(LANGUAGE_LIST, languages);
        prop.put(JUDGEMENT_LIST, judgements);
        prop.put(SITE_LIST, sites);
        prop.put(RUN_LIST, runs);

        Packet packet = new Packet(Type.LOGIN_SUCCESS, source, destination, prop);
        return packet;
    }

    /**
     * Create a packet of {@link PacketType.Type#SETTINGS}.
     * 
     * @param source
     * @param destination
     * @param props
     */
    public static Packet createSettings(ClientId source, ClientId destination, Properties props) {
        Packet packet = new Packet(Type.SETTINGS, source, destination, props);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#LOGIN_FAILED}.
     * 
     * @param source
     * @param destination
     * @param string
     */
    public static Packet createLoginDenied(ClientId source, ClientId destination, String string) {
        Properties props = new Properties();
        props.put(PacketFactory.MESSAGE_STRING, string);
        Packet packet = new Packet(Type.LOGIN_FAILED, source, destination, props);
        return packet;

    }

    /**
     * Create packet for {@link PacketType.Type#RUN_REQUEST}.
     * 
     * @param source
     * @param destination
     * @param id
     * @param requesingId
     */
    public static Packet createRunRequest(ClientId source, ClientId destination, ElementId id, ClientId requesingId) {
        Properties props = new Properties();
        props.put(PacketFactory.REQUESTED_RUN_ELEMENT_ID, id);
        props.put(PacketFactory.CLIENT_ID, requesingId);
        Packet packet = new Packet(Type.RUN_REQUEST, source, destination, props);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_REQUEST}.
     * 
     * @param source
     * @param destination
     * @param elementId
     * @param userId
     */
    public static Packet createClarificationRequest(ClientId source, ClientId destination, ElementId elementId, ClientId userId) {
        Properties props = new Properties();
        props.put(PacketFactory.REQUESTED_CLARIFICATION_ELEMENT_ID, elementId);
        props.put(PacketFactory.CLIENT_ID, userId);
        Packet packet = new Packet(Type.CLARIFICATION_REQUEST, source, destination, props);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_CHECKOUT}.
     * 
     * @param source
     * @param destination
     * @param clarification
     * @param whoCheckedOut
     */
    public static Packet createCheckedOutClarification(ClientId source, ClientId destination, Clarification clarification,
            ClientId whoCheckedOut) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        prop.put(CLIENT_ID, whoCheckedOut);
        Packet packet = new Packet(Type.CLARIFICATION_CHECKOUT, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_UNCHECKOUT}.
     * 
     * @param source
     * @param destination
     * @param clarification
     */
    public static Packet createUnCheckoutClarification(ClientId source, ClientId destination, Clarification clarification) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        prop.put(CLIENT_ID, source);
        Packet packet = new Packet(Type.CLARIFICATION_UNCHECKOUT, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_AVAILABLE}.
     * 
     * @param source
     * @param destination
     * @param clarification
     */
    public static Packet createClarificationAvailable(ClientId source, ClientId destination, Clarification clarification) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        Packet packet = new Packet(Type.CLARIFICATION_AVAILABLE, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_UPDATE}.
     * 
     * @param source
     * @param destination
     * @param clarification
     */
    public static Packet createClarificationUpdate(ClientId source, ClientId destination, Clarification clarification) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        Packet packet = new Packet(Type.CLARIFICATION_UPDATE, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CLARIFICATION_ANSWER}.
     * 
     * @param source
     * @param destination
     * @param clarification
     * @param answer
     */
    public static Packet createAnsweredClarification(ClientId source, ClientId destination, Clarification clarification,
            String answer) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        prop.put(CLARIFICATION_ANSWER, answer);
        Packet packet = new Packet(Type.CLARIFICATION_ANSWER, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#ADD_SETTING}.
     * 
     * @param source
     * @param destination
     * @param accounts
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, Account[] accounts) {
        Properties prop = new Properties();
        prop.put(ACCOUNT_ARRAY, accounts);
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param accounts
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Account[] accounts) {
        Properties prop = new Properties();
        prop.put(ACCOUNT_ARRAY, accounts);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    // 
    // * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
    // * @param source
    // * @param destination
    // * @param sites
    // */
    // public static Packet createUpdateSetting(ClientId source, ClientId destination, Site[] sites) {
    // Properties prop = new Properties();
    // prop.put(SITE_LIST, sites);
    // Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create packet for {@link PacketType.Type#ADD_SETTING}.
     * 
     * @param source
     * @param destination
     * @param account
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, Account account) {
        Properties prop = new Properties();
        prop.put(ACCOUNT, account);
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#ADD_SETTING }.
     * 
     * @param source
     * @param destination
     * @param type
     * @param count
     * @param isActive
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, ClientType.Type type, int count, boolean isActive) {
        Properties prop = new Properties();
        prop.put(CLIENT_TYPE, type);
        prop.put(COUNT, new Integer(count));
        prop.put(CREATE_ACCOUNT_ACTIVE, new Boolean(isActive));
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#ADD_SETTING }.
     * 
     * @param source
     * @param destination
     * @param type
     * @param count
     */
    public static Packet createAddSetting(ClientId source, ClientId destination, ClientType.Type type, int count) {
        Properties prop = new Properties();
        prop.put(CLIENT_TYPE, type);
        prop.put(COUNT, new Integer(count));
        Packet packet = new Packet(Type.ADD_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param account
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Account account) {
        Properties prop = new Properties();
        prop.put(ACCOUNT, account);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#RUN_REJUDGE_REQUEST}.
     * 
     * @param source
     * @param destination
     * @param elementId
     * @param requesterId
     */
    public static Packet createRunRejudgeRequest(ClientId source, ClientId destination, ElementId elementId, ClientId requesterId) {
        Properties props = new Properties();
        props.put(PacketFactory.REQUESTED_RUN_ELEMENT_ID, elementId);
        props.put(PacketFactory.CLIENT_ID, requesterId);
        Packet packet = new Packet(Type.RUN_REJUDGE_REQUEST, source, destination, props);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#RUN_REJUDGE_CHECKOUT}.
     * 
     * @param source
     * @param destination
     * @param run
     * @param runFiles
     * @param id
     */
    public static Packet createRejudgeCheckedOut(ClientId source, ClientId destination, Run run, RunFiles runFiles, ClientId id) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        if (runFiles != null) {
            prop.put(RUN_FILES, runFiles);
        }
        prop.put(CLIENT_ID, id);

        Packet packet = new Packet(Type.RUN_REJUDGE_CHECKOUT, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param timeValue
     * @param siteNumber
     */
    public static Packet createUpdateContestLengthTime(ClientId source, ClientId destination, long timeValue, int siteNumber) {
        Properties prop = new Properties();
        prop.put(CONTEST_LENGTH_TIME, new Long(timeValue));
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param timeValue
     * @param siteNumber
     */
    public static Packet createUpdateContestRemainingTime(ClientId source, ClientId destination, long timeValue, int siteNumber) {
        Properties prop = new Properties();
        prop.put(REMAINING_TIME, new Long(timeValue));
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param timeValue
     * @param siteNumber
     */
    public static Packet createUpdateContestElapsedTime(ClientId source, ClientId destination, long timeValue, int siteNumber) {
        Properties prop = new Properties();
        prop.put(ELAPSED_TIME, new Long(timeValue));
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#DROPPED_CONNECTION}.
     * 
     * @param source
     * @param destination
     * @param connectionHandlerID
     */
    public static Packet createDroppedConnection(ClientId source, ClientId destination, ConnectionHandlerID connectionHandlerID) {
        Properties prop = new Properties();
        prop.put(CONNECTION_HANDLE_ID, connectionHandlerID);
        Packet packet = new Packet(Type.DROPPED_CONNECTION, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#ESTABLISHED_CONNECTION}.
     * 
     * @param source
     * @param destination
     * @param connectionHandlerID
     */
    public static Packet createEstablishedConnection(ClientId source, ClientId destination, ConnectionHandlerID connectionHandlerID) {
        Properties prop = new Properties();
        prop.put(CONNECTION_HANDLE_ID, connectionHandlerID);
        Packet packet = new Packet(Type.ESTABLISHED_CONNECTION, source, destination, prop);
        return packet;
    }

    // public static Packet createUpdateSetting(ClientId source, ClientId destination, Site site, ClientId id) {
    // Properties prop = new Properties();
    // prop.put(SITE, site);
    // prop.put(CLIENT_ID, id);
    //
    // Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param run
     * @param id
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Run run, ClientId id) {
        Properties prop = new Properties();
        prop.put(RUN, run);
        prop.put(CLIENT_ID, id);

        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param run
     * @param judgementRecord
     * @param id
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Run run, JudgementRecord judgementRecord,
            ClientId id) {

        Properties prop = new Properties();
        prop.put(RUN, run);
        if (judgementRecord != null) {
            prop.put(JUDGEMENT_RECORD, judgementRecord);
        }
        prop.put(CLIENT_ID, id);

        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;

    }

    /**
     * Create packet for {@link PacketType.Type#FORCE_DISCONNECTION}.
     * 
     * @param source
     * @param destination
     * @param userLoginId
     */
    public static Packet createForceLogoff(ClientId source, ClientId destination, ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        Packet packet = new Packet(Type.FORCE_DISCONNECTION, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#FORCE_DISCONNECTION}.
     * 
     * @param source
     * @param destination
     * @param connectionHandlerID
     */
    public static Packet createForceLogoff(ClientId source, ClientId destination, ConnectionHandlerID connectionHandlerID) {
        Properties prop = new Properties();
        prop.put(CONNECTION_HANDLE_ID, connectionHandlerID);
        Packet packet = new Packet(Type.FORCE_DISCONNECTION, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#START_ALL_CLOCKS}.
     * 
     * @param source
     * @param destination
     * @param userLoginId
     */
    public static Packet createStartAllClocks(ClientId source, ClientId destination, ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        Packet packet = new Packet(Type.START_ALL_CLOCKS, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#RESET_CONTEST}.
     * 
     * @param source
     * @param destination
     * @param siteNumber
     * @param userLoginId
     */
    public static Packet createResetContest(ClientId source, ClientId destination, int siteNumber, ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        prop.put(SITE_NUMBER, new Integer(siteNumber));
        Packet packet = new Packet(Type.RESET_CONTEST, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#RESET_ALL_CONTESTS}.
     * 
     * @param source
     * @param destination
     * @param userLoginId
     */
    public static Packet createResetAllSites(ClientId source, ClientId destination, ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        Packet packet = new Packet(Type.RESET_ALL_CONTESTS, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param languageDisplayList
     * @param userLoginId
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, LanguageDisplayList languageDisplayList,
            ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        prop.put(LANGUAGE_DISPLAY_LIST, languageDisplayList);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param problemDisplayList
     * @param userLoginId
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, ProblemDisplayList problemDisplayList,
            ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        prop.put(PROBLEM_DISPLAY_LIST, problemDisplayList);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param answer
     * @param userLoginId
     */
    public static Packet createUpdateSettingDefaultClarificationAnswer(ClientId source, ClientId destination, String answer,
            ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        prop.put(DEFAULT_CLARIFICATION_ANSWER, answer);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#STOP_ALL_CLOCKS}.
     * 
     * @param source
     * @param destination
     * @param userLoginId
     */
    public static Packet createStopAllClocks(ClientId source, ClientId destination, ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        Packet packet = new Packet(Type.STOP_ALL_CLOCKS, source, destination, prop);
        return packet;
    }

    // public static Packet createUpdateSetting(ClientId source, ClientId destination, BalloonSettings balloonSettings,
    // ClientId userLoginId) {
    // Properties prop = new Properties();
    // prop.put(CLIENT_ID, userLoginId);
    // prop.put(BALOON_SETTINGS, balloonSettings);
    // Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param clarification
     * @param userLoginId
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Clarification clarification,
            ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLIENT_ID, userLoginId);
        prop.put(CLARIFICATION, clarification);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    // public static Packet createUpdateSetting(ClientId source, ClientId destination, ContestProperties contestProperties,
    // ClientId userLoginId) {
    // Properties prop = new Properties();
    // prop.put(CLIENT_ID, userLoginId);
    // prop.put(CONTEST_SETTINGS, contestProperties);
    // Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
    // return packet;
    // }

    /**
     * Create packet for {@link PacketType.Type#UPDATE_SETTING}.
     * 
     * @param source
     * @param destination
     * @param clarification
     * @param answer
     * @param userLoginId
     */
    public static Packet createUpdateSetting(ClientId source, ClientId destination, Clarification clarification, String answer,
            ClientId userLoginId) {
        Properties prop = new Properties();
        prop.put(CLARIFICATION, clarification);
        prop.put(CLIENT_ID, userLoginId);
        if (answer != null) {
            prop.put(CLARIFICATION_ANSWER, answer);
        }
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CONTEST_TIME}.
     * 
     * @param source
     * @param destination
     * @param inContestTime
     */
    public static Packet createUpdateSettings(ClientId source, ClientId destination, ContestTime inContestTime) {
        Properties prop = new Properties();
        prop.put(PacketType.CONTEST_TIME, inContestTime);
        Packet packet = new Packet(Type.UPDATE_SETTING, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CONTEST_TIME}.
     * 
     * @param source
     * @param destination
     * @param contestTime
     */
    public static Packet createContestStopped(ClientId source, ClientId destination, ContestTime contestTime) {
        Properties prop = new Properties();
        prop.put(PacketType.CONTEST_TIME, contestTime);
        Packet packet = new Packet(Type.CLOCK_STOPPED, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#CONTEST_TIME}.
     * 
     * @param source
     * @param destination
     * @param contestTime
     */
    public static Packet createContestStarted(ClientId source, ClientId destination, ContestTime contestTime) {
        Properties prop = new Properties();
        prop.put(PacketType.CONTEST_TIME, contestTime);
        Packet packet = new Packet(Type.CLOCK_STARTED, source, destination, prop);
        return packet;
    }

    /**
     * Create packet for {@link PacketType.Type#LOGIN_REQUEST}.
     * 
     * @param source
     * @param password
     * @param destination
     * @param sendSettings
     */
    public static Packet createLogin(ClientId source, String password, ClientId destination, boolean sendSettings) {
        Properties prop = new Properties();
        prop.put(LOGIN, source.getClientType() + "" + source.getClientNumber());
        prop.put(PASSWORD, password);
        prop.put(SEND_SETTINGS, new Boolean(sendSettings));
        return createPacket(PacketType.Type.LOGIN_REQUEST, source, destination, prop);
    }

}
