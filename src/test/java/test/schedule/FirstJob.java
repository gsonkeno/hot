package test.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by gaosong on 2017-04-04.
 */
public class FirstJob implements Job {
    private  static int i;
    public void execute(JobExecutionContext context) throws JobExecutionException {
    }
}
