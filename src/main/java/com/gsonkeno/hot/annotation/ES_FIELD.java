package com.gsonkeno.hot.annotation;

import com.gsonkeno.hot.meta.DataType;
import com.gsonkeno.hot.meta.IndexWay;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** es字段约束
 * Created by gaosong on 2017-07-25.
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ES_FIELD {

    /**Es字段名称**/
    public String fieldName() default "";

    /**字段数据类型**/
    public DataType fieldType() default  DataType.KEYWORD;

    /**字段索引方式**/
    public IndexWay index() default  IndexWay.TRUE;

    /**字段是否单独存储**/
    public boolean store() default  false;

    /**字段是否包含在all域中**/
    public boolean includeInAll() default true;

    /**字段使用的分词器**/
    public String analyzer() default "";
}
