package com.gsonkeno.hot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**es索引注解
 * Created by gaosong on 2017-07-25.
 */
@Target(value ={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EsIndex {
    /**索引名称**/
    public  String indexName() default  "index";

    /**索引类型**/
    public  String indexType() default  "type";

    /**分片，默认2**/
    public  int shards() default  2;

    /**副本，默认0**/
    public  int replicas() default  0;
}
