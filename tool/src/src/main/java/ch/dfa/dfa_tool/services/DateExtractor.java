package ch.dfa.dfa_tool.services;


import com.gitblit.utils.JGitUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by salizumberi-laptop on 30.10.2016.
 */
public class DateExtractor {

    private final static ArrayList<Integer> months = new ArrayList<>();

    private static void classfiyDatesToMonths(ArrayList<Date> commitDates) {
        for (int i = 0; i < commitDates.size(); i++) {
            addToMonth(getMonth(commitDates.get(i)));
        }
    }

    public static void addToMonth(int month) {
        for (int i = 0; i < 12; i++) {
            if (i == month - 1) {
                months.set(i, months.get(i) + 1);
            }
        }
    }

    public static void createMonths() {
        Integer month = new Integer(0);
        for (int i = 0; i < 12; i++) {
            months.add(month);
        }
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static Date getDateFromJsonString(String created_at) throws ParseException {
        String datePattern = "yyyy-MM-dd'T'HH:mm:ss";
        String test = "2014-05-29T16:23:17Z";
        DateFormat format = new SimpleDateFormat(datePattern, Locale.ENGLISH);
        return format.parse(created_at);
        // System.out.println("TEST ---->" +date (test)); // Thu May 29 16:23:17 CEST 2014
    }

    public static long getUnixDateFromCommit(RevCommit created_at) throws ParseException {
        Date date = JGitUtils.getCommitDate(created_at);
        return (long) date.getTime()/1000;
    }

    public static long getDaysBetweenTwoCommits(RevCommit oldCommit, RevCommit newcommit) {
        Date oldCommitDate = JGitUtils.getCommitDate(oldCommit);
        Date newcommitDsate = JGitUtils.getCommitDate(newcommit);
        long diff = newcommitDsate.getTime() - oldCommitDate.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static long getDaysBetweenProjectCreationAndDockerfile(RevCommit firstDockerCommit, Repository repository) {
        RevCommit firstCommit = JGitUtils.getFirstCommit(repository, null);
        Date firstCommitDate = JGitUtils.getCommitDate(firstCommit);
        Date docker = JGitUtils.getCommitDate(firstDockerCommit);
        long diff = docker.getTime() - firstCommitDate.getTime();
        repository.close();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
