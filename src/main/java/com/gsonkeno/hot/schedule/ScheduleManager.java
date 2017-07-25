package com.gsonkeno.hot.schedule;

import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 针对Quartz开源调度任务框架做的调度器简单封装
 * <p>
 * 任务Job拥有标识 JobKey，其由任务name和任务组groupName组成
 * <p>
 * 触发器Trigger拥有标示TriggerKey，其由触发器name和触发器组triggerName组成
 * <p>
 * Created by gaosong on 2017-04-04.
 */
public interface ScheduleManager {

    /**
     * 增加cron定时任务
     *
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobClass
     * @param cronExpression
     * @throws SchedulerException
     * @return
     */
    JobKey addCronJob(Class<? extends Job> jobClass, String cronExpression) throws SchedulerException;

    /**
     * 增加cron定时任务
     *
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobParamss      注入任务实体的任务参数
     * @throws SchedulerException
     * @return 任务标识
     */
    JobKey addCronJob(Class<? extends Job> jobClass, String cronExpression, Map<String, Object> jobParamss) throws SchedulerException;

    /**
     * 增加cron定时任务
     *
     * @param jobName        任务名称
     * @param groupName      任务所在组
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @throws SchedulerException
     * @return 任务标识
     */
    JobKey addCronJob(String jobName, String groupName, Class<? extends Job> jobClass, String cronExpression) throws  SchedulerException;

    /**
     * 增加cron定时任务
     *
     * @param jobName        任务名称
     * @param groupName      任务所在组
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobParamss      注入任务实体的任务参数
     * @throws SchedulerException
     * @return 任务标识
     */
    JobKey addCronJob(String jobName, String groupName, Class<? extends Job> jobClass,
                      String cronExpression, Map<String, Object> jobParamss) throws  SchedulerException;

    /**
     * 增加cron定时任务
     *
     * @param jobKey         任务标识
     * @param triggerKey     触发器标识
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @throws SchedulerException
     * @return 任务标识
     */
    JobKey addCronJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> jobClass,
                      String cronExpression) throws SchedulerException;

    /**
     * 增加cron定时任务
     *
     * @param jobKey         任务标识
     * @param triggerKey     触发器标识
     * @param jobClass       任务实体类
     * @param cronExpression cron表达式
     * @param jobParamss      注入任务实体的任务参数
     * @throws SchedulerException
     * @return 任务标识
     */
    JobKey addCronJob(JobKey jobKey, TriggerKey triggerKey, Class<? extends Job> jobClass,
                      String cronExpression, Map<String, Object> jobParamss) throws SchedulerException;


    /**
     * 增加简单定时器任务
     *
     * @param jobClass               执行任务的实现类
     * @param startTime              执行开始时间，不设置结束时间表示该任务将一直执行
     * @param intervalInMilliseconds 执行间隔ms
     * @throws SchedulerException
     */
    JobKey addSimpleJob(Class<? extends Job> jobClass,
                            Date startTime, long intervalInMilliseconds) throws SchedulerException;

    /**
     * 增加简单定时器任务
     *
     * @param jobClass               执行任务的实现类
     * @param startTime              执行开始时间，不设置结束时间表示该任务将一直执行
     * @param intervalInMilliseconds 执行间隔ms
     * @param jobParams               执行任务所需的参数，可以在job实现类中获取到
     * @throws SchedulerException
     */
    JobKey addSimpleJob(Class<? extends Job> jobClass,
                            Date startTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException;

    /**
     * 增加简单定时器任务
     *
     * @param jobClass               执行任务的实现类
     * @param startTime              执行开始时间
     * @param endTime                执行结束时间，可以为null，为null时不会结束
     * @param intervalInMilliseconds 执行间隔ms
     * @param jobParams               执行任务所需的参数，可以在job实现类中获取到
     * @throws SchedulerException
     */
    JobKey addSimpleJob(Class<? extends Job> jobClass,
                            Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams) throws SchedulerException;

    /**
     * 增加简单定时器任务
     *
     * @param jobClass               执行任务的实现类
     * @param name                   任务名称
     * @param groupName              任务所属分组名称
     * @param startTime              执行开始时间
     * @param endTime                执行结束时间，可以为null，为null时不会结束
     * @param intervalInMilliseconds 执行间隔ms
     * @param jobParams               执行任务所需的参数，可以在job实现类中获取到
     * @throws SchedulerException
     */
    JobKey addSimpleJob(Class<? extends Job> jobClass, String name, String groupName,
                            Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams)
            throws SchedulerException;

    /**
     * 增加简单定时器任务
     *
     * @param jobClass               执行任务的实现类
     * @param jobKey                 任务唯一标识
     * @param triggerKey             触发器唯一标识
     * @param startTime              执行开始时间
     * @param endTime                执行结束时间，可以为null，为null时不会结束
     * @param intervalInMilliseconds 执行间隔ms
     * @throws SchedulerException
     */
    JobKey addSimpleJob(Class<? extends Job> jobClass, JobKey jobKey, TriggerKey triggerKey,
                            Date startTime, Date endTime, long intervalInMilliseconds, Map<String, Object> jobParams)
            throws SchedulerException;

    /**
     * 删除任务
     *
     * @param jobKey 任务标识
     * @throws SchedulerException
     */
    boolean delJob(JobKey jobKey) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param jobClass 任务实体类
     * @throws SchedulerException
     */
    boolean delJob(Class<? extends Job> jobClass) throws SchedulerException;

    /**
     * 删除任务
     *
     * @param name      任务名
     * @param groupName 任务所在组名
     * @throws SchedulerException
     */
    boolean delJob(String name, String groupName) throws SchedulerException;

    /**
     * 停止任务
     *
     * @param jobKey
     * @throws SchedulerException
     */
    void pauseJob(JobKey jobKey) throws SchedulerException;

    /**
     * 停止任务
     *
     * @param name
     * @param groupName
     * @throws SchedulerException
     */
    void pauseJob(String name, String groupName) throws SchedulerException;


    /**
     * 重新开始任务，暂停期间错过的任务会被补回来
     *
     * @param jobKey
     * @throws SchedulerException
     */
    void resumeJob(JobKey jobKey) throws SchedulerException;

    /**
     * 重新开始任务，暂停期间错过的任务会被补回来
     *
     * @param name
     * @param groupName
     * @throws SchedulerException
     */
    void resumeJob(String name, String groupName) throws SchedulerException;


    /**
     * 开始所有定时作业调度
     */
    void start() throws SchedulerException;

    /**
     * 获取所有任务主键
     *
     * @return
     */
    Set<JobKey> allJobKeys() throws SchedulerException;

    /**
     * 获取所有触发器主键
     *
     * @return
     */
    Set<TriggerKey> allTriggerKeys() throws SchedulerException;

    /**
     * 停止所有任务调度
     */
    void shutdown() throws SchedulerException;

    /**
     * 根据JobKey判断任务是否存在
     *
     * @param jobKey
     * @return
     */
    boolean checkExist(JobKey jobKey) throws SchedulerException;

    /**
     * 根据JobKey修改定时任务的参数
     *
     * @param jobKey
     * @param  triggerKey
     * @param jobParams 执行任务所需的参数，可以在job实现类中获取到
     * @return
     * @throws SchedulerException
     */
    boolean modifyjobParams(JobKey jobKey,TriggerKey triggerKey, Map<String, Object> jobParams) throws SchedulerException;

    /**
     * 修改cronTrigger定时任务的执行时间
     *
     * @param jobKey  原任务标识
     * @param triggerKey 原任务关联的触发器标识
     * @param cronExpression 新的执行时间表达式
     * @throws SchedulerException
     */
    void modifyCronTriggerExcuteTime(JobKey jobKey,TriggerKey triggerKey, String cronExpression) throws SchedulerException;

    /**
     * 调度器是否已经启动
     *
     * @return
     */
    boolean isStarted() throws  SchedulerException;


}
