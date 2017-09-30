package com.gsonkeno.hot;

import com.gsonkeno.hot.schedule.QuartzScheduleManager;
import com.gsonkeno.hot.schedule.ScheduleManager;

/**
 * Created by gaosong on 2017-04-04.
 */
public class HOT {
    public static final ScheduleManager schedule = new QuartzScheduleManager();

}
