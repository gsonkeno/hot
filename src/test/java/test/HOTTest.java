package test;

import com.gsonkeno.hot.HOT;
import org.junit.Test;
import org.quartz.SchedulerException;
import test.schedule.FirstJob;

/**
 * Created by gaosong on 2017-04-04.
 */
public class HOTTest {
    @Test
    public void test(){
        System.out.println("this is test");
    }

    public static void main(String[] args) throws SchedulerException {

        HOT.schedule.addCronJob(FirstJob.class, "30 55 22 * * ?");
        System.out.println(HOT.schedule.allTriggerKeys());
        HOT.schedule.delJob(FirstJob.class);
        System.out.println(HOT.schedule.allTriggerKeys());
//        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        JobDetail jobDetail = newJob(FirstJob.class).withIdentity("FirstJob","DEFAULT").build();
//
//
//        Trigger trigger = newTrigger().withIdentity("FIRST_TRIGGER","DEFAULT").withSchedule(cronSchedule("0/5 * * * * ?")).build();
//
//        defaultScheduler.scheduleJob(jobDetail, trigger);
//        defaultScheduler.start();

//        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
//        JobDetail jobDetail = newJob(FirstJob.class).withIdentity("FirstJob","DEFAULT").build();
//
//
//        Trigger trigger = newTrigger().withIdentity("FIRST_TRIGGER","DEFAULT").withSchedule(cronSchedule("0/5 * * * * ?")).build();
//
//        defaultScheduler.scheduleJob(jobDetail, trigger);
//        defaultScheduler.start();


    }
}
