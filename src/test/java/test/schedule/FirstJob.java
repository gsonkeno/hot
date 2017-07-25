package test.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

import static com.gsonkeno.hot.utils.DateUtils.*;

/**
 * Created by gaosong on 2017-04-04.
 */
public class FirstJob implements Job {
    private  static int i;
    public void execute(JobExecutionContext context) throws JobExecutionException {
        i++;
        System.out.println("我是第一个job，执行时间" + dateToString(new Date())+"执行第" + i +"次");
    }
}
