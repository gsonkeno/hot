package com.gsonkeno.redis;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by gaosong on 2018-02-08
 */
public class CommonPool2Test {
    public static void main(String[] args) throws Exception {
        //使用Apache commons-pool2的ObjectPool的默认实现GenericObjectPool
        ObjectPool op = new GenericObjectPool<StringBuffer>(new MyPooledObjectFactoryExample(),new GenericObjectPoolConfig());

        //从ObjectPool租借对象StringBuffer
        StringBuffer sb = (StringBuffer) op.borrowObject();
        StringBuffer sb1 = (StringBuffer) op.borrowObject();

        System.out.println("sb:" + sb.hashCode());
        sb.append("7819");
        System.out.println("修改后sb:" + sb.hashCode());
        System.out.println(sb);
        System.out.println(sb1.hashCode());
        op.returnObject(sb);
        StringBuffer sb2 = (StringBuffer) op.borrowObject();
        System.out.println("sb2的hasCode:" + sb2.hashCode()); //sb2其实是返还的sb1,它的字符串值也没有改变
        System.out.println(sb2);
    }
}
