package com.gsonkeno.hot.schedule;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.utils.Key;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by gaosong on 2017-04-04.
 */
public class QuartzScheduleManager implements ScheduleManager {
    private Scheduler scheduler;

    /**
     * @param jobClass
     * @param cronExpression
     * @return
     * @throws SchedulerException
     * @see #addCronJob(Class, String, Map)
     */
    public JobKey addCronJob(Class<? extends Job> jobClass, String cronExpression) throws SchedulerException {

        return addCronJob(jobClass, cronExpression, null);
    }

    /**
     * JobKey由任务实体的类名和默认组名 {@link org.quartz.utils.Key#DEFAULT_GROUP}组成
     * <p></p>
     * TriggerKey由任务实体的类名和默认组名 {@link org.quartz.utils.Key#DEFAULT_GROUP}组成
     * <p></p>
     * 注意:该任务只能增加一次，再增加的话任务标识会重复
     *
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobParamss     注入任务实体的任务参数
     * @return
     */
    public JobKey addCronJob(Class<? extends Job> jobClass, String cronExpression, Map<String, Object> jobParamss) throws SchedulerException {
        String className = jobClass.getSimpleName();
        JobKey jobKey = new JobKey(className);
        TriggerKey triggerKey = new TriggerKey(className);

        return addCronJob(jobKey, triggerKey, jobClass, cronExpression, jobParamss);
    }

    public JobKey addCronJob(String jobName, String groupName, Class<? extends Job> jobClass, String cronExpression) throws SchedulerException {
        return addCronJob(jobName, groupName, jobClass, cronExpression, null);
    }

    /**
     * 任务标识由jobName和groupName组成
     * 触发器标识也由jobName和groupName组成
     *
     * @param jobName        任务名称
     * @param groupName      任务所在组
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobParamss     注入任务实体的任务参数
     * @return
     * @throws SchedulerException
     */
    public JobKey addCronJob(String jobName, String groupName, Class<? extends Job> jobClass, String cronExpression,
                             Map<String, Object> jobParamss) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, groupName);
        TriggerKey triggerKey = new TriggerKey(jobName, groupName);
        return addCronJob(jobKey, triggerKey, jobClass, cronExpression, jobParamss);
    }

    public JobKey addCronJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> jobClass, String cronExpression) throws SchedulerException {
        return addCronJob(jobKey, triggerKey, jobClass, cronExpression, null);
    }

    public JobKey addCronJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> jobClass, String cronExpression, Map<String, Object> jobParamss) throws SchedulerException {

        ensureStarted();
        ensureJobUnique(jobKey);
        ensureTriggerUnique(triggerKey);

        JobDetail jobDetail = newJob(jobClass).withIdentity(jobKey).build();

        if (jobParamss != null) jobDetail.getJobDataMap().putAll(jobParamss);

        Trigger trigger = newTrigger().withIdentity(triggerKey).withSchedule(cronSchedule(cronExpression)).build();

        getStdScheduler().scheduleJob(jobDetail, trigger);


        return jobKey;
    }

    public JobKey addSimpleJob(Class<? extends Job> jobClass, Date startTime, long intervalInMilliseconds) throws SchedulerException {
        return addSimpleJob(jobClass, startTime, null, intervalInMilliseconds, null);
    }

    public JobKey addSimpleJob(Class<? extends Job> jobClass, Date startTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException {
        return addSimpleJob(jobClass, startTime, null, intervalInMilliseconds, jobParams);
    }

    public JobKey addSimpleJob(Class<? extends Job> jobClass, Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException {
        return addSimpleJob(jobClass, jobClass.getSimpleName(), Key.DEFAULT_GROUP,
                startTime, endTime, intervalInMilliseconds, jobParams);
    }

    public JobKey addSimpleJob(Class<? extends Job> jobClass, String name, String groupName, Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException {
        JobKey jobKey = new JobKey(name, groupName);
        TriggerKey triggerKey = new TriggerKey(name, groupName);
        addSimpleJob(jobClass, startTime, endTime, intervalInMilliseconds, null);
        return null;
    }

    /**
     * @param jobClass               执行任务的实现类
     * @param jobKey                 任务唯一标识
     * @param triggerKey             触发器唯一标识
     * @param startTime              执行开始时间
     * @param endTime                执行结束时间，可以为null，为null时不会结束
     * @param intervalInMilliseconds 执行间隔ms
     * @param jobParams
     * @return
     * @throws SchedulerException
     */
    public JobKey addSimpleJob(Class<? extends Job> jobClass, JobKey jobKey, TriggerKey triggerKey, Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException {
        ensureStarted();
        ensureJobUnique(jobKey);
        ensureTriggerUnique(triggerKey);

        JobDetail jobDetail = newJob(jobClass).withIdentity(jobKey).build();

        if (jobParams != null) jobDetail.getJobDataMap().putAll(jobParams);

        Trigger trigger = newTrigger().withIdentity(triggerKey).startAt(startTime)
                .endAt(endTime)
                .withSchedule(simpleSchedule().withIntervalInMilliseconds(intervalInMilliseconds).repeatForever())
                .build();
        getStdScheduler().scheduleJob(jobDetail,trigger);

        return jobKey;
    }

    public boolean delJob(JobKey jobKey) throws SchedulerException {
        return getStdScheduler().deleteJob(jobKey);
    }

    /**
     * 删除任务(任务标识由类型和默认组名组成)
     *
     * @param jobClass 任务实体类
     * @return
     * @throws SchedulerException
     */
    public boolean delJob(Class<? extends Job> jobClass) throws SchedulerException {
        return delJob(jobClass.getSimpleName(), Key.DEFAULT_GROUP);
    }

    public boolean delJob(String name, String groupName) throws SchedulerException {
        return delJob(new JobKey(name, groupName));
    }

    public void pauseJob(JobKey jobKey) throws SchedulerException {
        getStdScheduler().pauseJob(jobKey);
    }

    public void pauseJob(String name, String groupName) throws SchedulerException {
        pauseJob(new JobKey(name, groupName));
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        getStdScheduler().resumeJob(jobKey);
    }

    public void resumeJob(String name, String groupName) throws SchedulerException {
        resumeJob(new JobKey(name,groupName));
    }

    public void start() throws SchedulerException {
        getStdScheduler().start();
    }

    public Set<JobKey> allJobKeys() throws SchedulerException {
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.<JobKey>anyGroup());
        return jobKeys;
    }

    public Set<TriggerKey> allTriggerKeys() throws SchedulerException {
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.<TriggerKey>anyGroup());
        return triggerKeys;
    }

    public void shutdown() throws SchedulerException {
        if (!getStdScheduler().isShutdown()) getStdScheduler().shutdown();
    }

    /**
     * 判断任务是否已经存在, {@link #scheduler}不需在启动状态中,通过{@link Scheduler#checkExists(JobKey)}便可判断
     * 前提是 {@link #scheduler}被正确地初始化
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    public boolean checkExist(JobKey jobKey) throws SchedulerException {
        return getStdScheduler().checkExists(jobKey);
    }

    /**
     * 判断触发器是否已经存在, {@link #scheduler}不需在启动状态中,通过{@link Scheduler#checkExists(JobKey)}便可判断
     * 前提是 {@link #scheduler}被正确地初始化
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    public boolean checkExist(TriggerKey triggerKey) throws SchedulerException {
        return getStdScheduler().checkExists(triggerKey);
    }

    public boolean modifyjobParams(JobKey jobKey, TriggerKey triggerKey, Map<String, Object> jobParams) throws SchedulerException {
        if (!getStdScheduler().checkExists(jobKey)) {
            return false;
        }

        JobDetail jobDetail = getStdScheduler().getJobDetail(jobKey);

        jobDetail.getJobDataMap().putAll(jobParams);

        Trigger trigger = getStdScheduler().getTrigger(triggerKey);

        delJob(jobKey);

        getStdScheduler().scheduleJob(jobDetail, trigger);

        return true;
    }

    public void modifyCronTriggerExcuteTime(JobKey jobKey, TriggerKey triggerKey, String cronExpression) throws SchedulerException {
        CronTrigger newTrigger = newTrigger().withIdentity(triggerKey).forJob(jobKey).withSchedule(cronSchedule(cronExpression)).build();

        this.getStdScheduler().rescheduleJob(triggerKey, newTrigger);
    }

    /**
     * 调度器是否已经启动，在运行中
     * <p>
     * <p>
     * {@link Scheduler#isStarted()}只要启动过,则为true;
     * {@link Scheduler#isShutdown()} 是否已经关闭，当未启动时,返回 <code>false</code>
     * </p>
     *
     * @return
     * @throws SchedulerException
     */
    public boolean isStarted() throws SchedulerException {
        //当未启动时，返回false;
        if (!getStdScheduler().isStarted()) return false;

        //当启动过，但关闭时,返回false
        if (getStdScheduler().isStarted() && getStdScheduler().isShutdown()) return false;

        return true;
    }

    /**
     * 确保 {@link #scheduler} 处在工作状态中
     */
    private void ensureStarted() throws SchedulerException {
        if (!isStarted()) getStdScheduler().start();
    }

    /**
     * 确保 <code>jobKey</code>所标识的任务是唯一的
     *
     * @param jobKey
     * @throws SchedulerException 如果重复，抛出异常
     */
    private void ensureJobUnique(JobKey jobKey) throws SchedulerException {
        boolean b = checkExist(jobKey);
        if (b)
            throw new SchedulerException("相同名称 [" + jobKey.getName() + "] 以及组名 [" + jobKey.getGroup() + "] 的任务已存在，请更换后再试");
    }


    /**
     * 确保 <code>triggerKey</code>所标识的触发器是唯一的
     *
     * @param triggerKey
     * @throws SchedulerException 如果重复，抛出异常
     */
    private void ensureTriggerUnique(TriggerKey triggerKey) throws SchedulerException {
        boolean b = checkExist(triggerKey);
        if (b)
            throw new SchedulerException("相同名称 [" + triggerKey.getName() + "] 以及组名 [" + triggerKey.getGroup() + "] 的触发器已存在，请更换后再试");
    }

    /**
     * 获取单例的scheduler
     *
     * @return
     * @throws SchedulerException
     */
    private Scheduler getStdScheduler() throws SchedulerException {
        if (scheduler == null) {
            synchronized (QuartzScheduleManager.class) {
                if (scheduler == null) {
                    try {
                        scheduler = StdSchedulerFactory.getDefaultScheduler();
                    } catch (SchedulerException e) {
                        throw new SchedulerException("初始化scheduler异常", e);
                    }
                }
            }
        }
        return scheduler;
    }
}
