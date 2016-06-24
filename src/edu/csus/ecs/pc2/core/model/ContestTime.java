package edu.csus.ecs.pc2.core.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 * This class represents the abstract notion of "Contest Time" - that is, a "clock"
 * which is initialized when the contest first starts and "counts down" to the
 * end of the contest -- but only counting down when the contest is marked as "running".
 * In this way the ContestTime object keeps track of the amount of elapsed time which is considered
 * to be part of the contest (similar to the way the "game clock" is stopped and subsequently restarted
 * in, for example, a football game).
 * 
 * The contest can be "stopped" at any time (e.g. by pushing the "Stop"
 * button on the PC2 Admin); this causes the "contest time clock" to stop counting down until
 * the contest is subsequently "restarted" (e.g. by pushing the "Start" button on the PC2 Admin).
 * When a contest is "stopped" (also called "paused") after having been started, and then the
 * "Start" button is again pushed to restart the contest, any real time which elapsed between 
 * the "stop" and subsequent "restart' is not included in the Contest Clock time.
 * 
 * Note that in a multi-site contest (one running multiple PC2 Servers), each PC2 Server has
 * its own instance of ContestTime.  This allows different sites to pause the contest at different
 * times and for different lengths of time if necessary; each site's ContestTime object insures that
 * the site runs the contest for the correct length of time independently of the other sites.  
 * 
 * Methods used to access contest time as well as start and stop contest time. <br>
 * Start clock: {@link #startContestClock()}. <br>
 * Stop contest clock: {@link #stopContestClock()}. <br>
 * <br>
 * Get remaining time (formatted) {@link #getRemainingTimeStr()} <br>
 * <br>
 * 
 * @author pc2@ecs.csus.edu
 * @version $Id$
 */

// $HeadURL$
public class ContestTime implements IElementObject {

    public static final long serialVersionUID = 6967329985187819728L;
    
    /**
     * number of ms in a second.
     */
    public static final long MS_PER_SECONDS = 1000;

    /**
     * number of seconds in a minute.
     */
    public static final long SECONDS_PER_MINUTE = 60;

    /**
     * number of ms in a minute.
     */
    public static final long MS_PER_MINUTE = SECONDS_PER_MINUTE * MS_PER_SECONDS;

    /**
     * number of minutes in an hour.
     */
    public static final long MINUTES_PER_HOUR = 60;
    
    /**
     * number of seconds in an hour.
     */
    public static final long SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;


    /**
     * Default contest length.
     */
    public static final long DEFAULT_CONTEST_LENGTH_SECONDS = 18000; // 5 * 60 * 60 = 5 hours

    /**
     * Resume time, used in calculating elapsed time.
     */
    private GregorianCalendar resumeTime = null;

    /**
     * The actual time the contest is first started. Prior to the contest ever having
     * been started this field is always null; once the contest is started for the first
     * time this field holds the real time at which that start occurred.  This field does not
     * change if the contest is "paused" and restarted (e.g. if the "Stop Contest" button
     * is pushed and then the "Start Contest" button is subsequently pushed); 
     * the subsequent "Start Contest" does not change the value of this field.
     */
    private GregorianCalendar actualTimeContestFirstStarted = null;
 
    /**
     * serverTransmitTime is set by the server and is used to calculate clock differential between client & server clocks
     */
    private GregorianCalendar serverTransmitTime = null;

    private long localClockOffset = 0;

    private boolean haltContestAtTimeZero = false;

    /**
     * Elapsed seconds since start of contest.
     */
    private long elapsedSecs = 0;
    
    /**
     * Elapsed milli-seconds since start of contest.
     */
    private long elapsedMS = 0;

    private long contestLengthSecs = DEFAULT_CONTEST_LENGTH_SECONDS;

    /**
     * Is contest clock running (counting down) ?
     */
    private boolean contestRunning = false;

    private int siteNumber;

    private ElementId elementId;

    public ContestTime() {
        this("Contest Time");
    }

    /**
     * Create contest time with given site.
     * 
     * @param siteNumber
     */
    public ContestTime(int siteNumber) {
        this("Contest Time Site " + siteNumber);
        this.siteNumber = siteNumber;
    }

    private ContestTime(String idString) {
        elementId = new ElementId(idString);
    }

    /**
     * 
     * @return contest length in minutes.
     */
    public long getContestLengthMins() {
        return contestLengthSecs / 60;
    }

    /**
     * 
     * @return contest length formatted.
     */
    public String getContestLengthStr() {
        return formatTime(contestLengthSecs);
    }

    /**
     * 
     * @return true if contest clock running, false if clock stopped.
     */
    public boolean isContestRunning() {
        return contestRunning;
    }

    /**
     * 
     * @see #formatTime(long)
     * @return elapsed time formatted
     */
    public String getElapsedTimeStr() {
        return formatTime(getElapsedSecs());
    }

    /**
     * @see #formatTime(long)
     * @return returns remaining time formatted.
     */
    public String getRemainingTimeStr() {
        return formatTime(getRemainingSecs());
    }

    /**
     * @see #formatTime(long)
     * @return returns remaining time formatted, HH:MM
     */
    public String getRemainingMinStr() {
        String timeStr = formatTime(getRemainingSecs());
        int lastIndex = timeStr.lastIndexOf(":");
        if (lastIndex != -1) {
            return timeStr.substring(0, lastIndex);
        } else {
            return timeStr;
        }
    }

    /**
     * Format the input seconds in the form HH:MM:SS.
     * 
     * @param seconds
     * @return formatted string in form HH:MM:SS
     */
    public static String formatTime(long seconds) {

        boolean negative = seconds < 0;

        if (negative) {
            seconds = seconds * -1; // absolute value it ..
        }

        long hours = seconds / 3600;
        long mins = (seconds / 60) % 60;
        long secs = (seconds % 60);

        String hourStr = new Long(hours).toString();
        // if (hours < 10)
        // hourStr = '0' + hourStr;

        String minStr = new Long(mins).toString();
        if (mins < 10) {
            minStr = '0' + minStr;
        }

        String secStr = new Long(secs).toString();
        if (secs < 10) {
            secStr = '0' + secStr;
        }

        if (negative) {
            hourStr = "-" + hourStr;
        }

        return (hourStr + ':' + minStr + ':' + secStr);
    }

    public long getContestLengthSecs() {
        return contestLengthSecs;
    }
    
    public long getContestLengthMS() {
        return contestLengthSecs * 1000;
    }

    /**
     * @return elapsed time in minutes.
     */
    public long getElapsedMins() {
        return getElapsedSecs() / 60;
    }

    /**
     * @return elapsed time in seconds.
     */
    public long getElapsedSecs() {
        return elapsedSecs + secsSinceContestStart();
    }
    
    /**
     * @return elapsed time in milliseconds.
     */
    public long getElapsedMS() {
        return elapsedMS + msSinceContestStart();
    }

    /**
     * Get minutes since contest start.
     * 
     * @return minutes since start of contest.
     */
    public long getElapsedTime() {
        return getElapsedMins();
    }

    /**
     * 
     * @return remaining seconds from contest clock.
     */
    public long getRemainingSecs() {
        // compute remaining time.
        return contestLengthSecs - (elapsedSecs + secsSinceContestStart());
    }

    /**
     * 
     * @return halt the contest at time zero ?
     */
    public boolean isHaltContestAtTimeZero() {
        return haltContestAtTimeZero;
    }

    /**
     * 
     * @return true if remaining seconds <= 0, false if more time left.
     */
    public boolean isPastEndOfContest() {

        return getRemainingSecs() <= 0;
    }

    /**
     * 
     * @see #formatTime(long)
     * @return formatted remaining time
     */
    public String remTimeStr() {
        long secsLeft = getRemainingSecs();
        return formatTime(secsLeft);
    }

    /**
     * Returns the number of seconds since the most recent "Start Contest" operation,
     * or zero if the contest is not currently running.
     * If the contest has been started but has never subsequently been "paused" 
     * (by pressing the "Stop Contest" button), the value returned will be the
     * number of seconds since the actual start of the contest.  If the contest has
     * been "paused" and subsequently restarted, the value returned will be the number
     * of seconds since the "start" which followed the pause (i.e., since the restart).
     * If the contest has been started and subsequently paused and NOT restarted then
     * the contest is not running and, as stated above, zero is returned.
     * 
     * @return the number of seconds since the most recent occurrence of a "Start Contest" operation, or zero
     */
    private long secsSinceContestStart() {
        if (contestRunning) {
            long milliDiff = msSinceContestStart();
            long totalSeconds = milliDiff / 1000;
            return totalSeconds;
        } else {
            return 0;
        }
    }
    
    /**
     * Returns the number of milliseconds since the most recent "Start Contest" operation,
     * or zero if the contest is not currently running.
     * If the contest has been started but has never subsequently been "paused" 
     * (by pressing the "Stop Contest" button), the value returned will be the
     * number of milliseconds since the actual start of the contest.  If the contest has
     * been "paused" and subsequently restarted, the value returned will be the number
     * of milliseconds since the "start" which followed the pause (i.e., since the restart).
     * If the contest has been started and subsequently paused and NOT restarted, then
     * the contest is not running and, as stated above, zero is returned.
     * 
     * @return the number of milliseconds since the most recent occurrence of a "Start Contest" operation, or zero
     */
    private long msSinceContestStart() {
        if (contestRunning) {
            TimeZone tz = TimeZone.getTimeZone("GMT");
            GregorianCalendar cal = new GregorianCalendar(tz);

            long milliDiff = cal.getTime().getTime() - resumeTime.getTime().getTime();
            return milliDiff;
        } else {
            return 0;
        }
    }

    public void setContestLengthSecs(long newSecs) {
        contestLengthSecs = newSecs;
    }

    public void setElapsedMins(long minutes) {
        setElapsedSecs(minutes * SECONDS_PER_MINUTE);
    }

    public void setElapsedSecs(long eSecs) {
        elapsedSecs = eSecs;
        elapsedMS = eSecs * MS_PER_SECONDS;
    }

    public void setHaltContestAtTimeZero(boolean newHaltContestAtTimeZero) {
        haltContestAtTimeZero = newHaltContestAtTimeZero;
    }

    public void setRemainingSecs(long remSecs) {
        setElapsedSecs(contestLengthSecs - remSecs);
    }

    /**
     * Updates the "resume time" for the contest to be the local machine's current time.
     */
    private void forceContestStartTimeResync() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        resumeTime = new GregorianCalendar(tz);
    }

    /**
     * If this is the first time a "Start Contest" operation has been invoked on this
     * {@code ContestTime} object, this method sets the time at which the contest actually 
     * started to be the current time on the local machine.
     * Subsequently, if the contest is not currently running then this method updates
     * the "resume time" (the time at which the most recent "Start Contest" operation was
     * invoked) to be the current time on the local machines, and also marks the contest
     * as "running".
     */
    public void startContestClock() {
        
        if (actualTimeContestFirstStarted == null) {
            TimeZone tz = TimeZone.getTimeZone("GMT");
            actualTimeContestFirstStarted = new GregorianCalendar(tz);
        }
        
        if (!contestRunning) {
            forceContestStartTimeResync();
            contestRunning = true;
        }
    }

    /**
     * Stop contest clock.
     * 
     */
    public void stopContestClock() {
        if (contestRunning) {
            elapsedSecs = elapsedSecs + secsSinceContestStart();
            elapsedMS = elapsedMS + msSinceContestStart();
            contestRunning = false;
        }
    }

    /**
     * @return Returns the resumeTime.
     */
    public GregorianCalendar getResumeTime() {
        return resumeTime;
    }

    /**
     * @param startTime
     *            The resumeTime to set.
     */
    public void setResumeTime(GregorianCalendar startTime) {
        this.resumeTime = startTime;
    }

    /**
     * Convert a String of format 00:00:00 to a long
     * 
     * @param intTime
     * @return the time
     */
    public long convertStringToLong(String intTime) {

        StringTokenizer stringTokenizer = new StringTokenizer(intTime, ":");

        String s1 = stringTokenizer.nextToken();
        String s2 = stringTokenizer.nextToken();
        String s3 = stringTokenizer.nextToken();

        long hh = Long.parseLong(s1);
        long mm = Long.parseLong(s2);
        long ss = Long.parseLong(s3);

        long totalSeconds = 0;
        if (hh != -1) {
            totalSeconds = hh;
        }
        if (mm != -1) {
            totalSeconds = (totalSeconds * 60) + mm;
        }
        if (ss != -1) {
            totalSeconds = (totalSeconds * 60) + ss;
        }

        if (hh == -1 || mm == -1 || ss == -1) {
            return -1;
        }

        return totalSeconds;
    }

    public long getLocalClockOffset() {
        return localClockOffset;
    }

    public void setLocalClockOffset(long localClockOffset) {
        this.localClockOffset = localClockOffset;
    }

    public GregorianCalendar getServerTransmitTime() {
        return serverTransmitTime;
    }

    /**
     * Set serverTransmitTime to local Time.
     * 
     * Should be called by the server before Transmitting a ContestTime packet. The serverTransmitTime is used to calculate the
     * localClockOffset on client machines.
     */
    public void setServerTransmitTime() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);

        this.serverTransmitTime = gregorianCalendar;
    }

    /**
     * Calculate Local Clock Offset.
     * 
     * Should be called by the client after a ContestTime packet is received to adjust the local clock.
     * 
     */
    public void calculateLocalClockOffset() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);

        calculateLocalClockOffset(gregorianCalendar);
    }

    public void calculateLocalClockOffset(GregorianCalendar localClock) {

        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);

        long milliDiff = gregorianCalendar.getTime().getTime() - localClock.getTime().getTime();
        
        localClockOffset = milliDiff / 1000;
        
        if (resumeTime != null) {
            resumeTime.setTimeInMillis(resumeTime.getTimeInMillis() + milliDiff);
        }
    }

    public ElementId getElementId() {
        return elementId;
    }

    public int versionNumber() {
        return elementId.getVersionNumber();
    }

    public int getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(int siteNumber) {
        this.siteNumber = siteNumber;

    }

    public void resetClock() {
        resumeTime = null;
        serverTransmitTime = null;
        localClockOffset = 0;
        setElapsedSecs(0);
        contestRunning = false;
    }

    public Calendar getContestStartTime() {
        return actualTimeContestFirstStarted;
    }

    public long getRemainingMS() {
        return getContestLengthMS() - getElapsedMS();
    }

 
}
