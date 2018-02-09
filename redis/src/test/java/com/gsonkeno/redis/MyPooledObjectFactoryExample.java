package com.gsonkeno.redis;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * Created by gaosong on 2018-02-08
 */
public class MyPooledObjectFactoryExample implements PooledObjectFactory<StringBuffer> {
    /**
     * //创建StringBuffer对象
     */
    @Override
    public PooledObject<StringBuffer> makeObject() throws Exception {
        return new DefaultPooledObject<StringBuffer>(new StringBuffer());
    }
    /**
     * //销毁StringBuffer对象
     */
    @Override
    public void destroyObject(PooledObject<StringBuffer> p) throws Exception {
        StringBuffer sb = p.getObject();
        sb = null;
    }
    /**
     * //校验StringBuffer对象
     */
    @Override
    public boolean validateObject(PooledObject<StringBuffer> p) {
        return p.getObject() != null;
    }
    /**
     * //激活StringBuffer对象
     */
    @Override
    public void activateObject(PooledObject<StringBuffer> p) throws Exception {
        if (null == p.getObject())
            p = new DefaultPooledObject<StringBuffer>(new StringBuffer());
    }
    /**
     * //钝化StringBuffer对象，这里是个空实现
     */
    @Override
    public void passivateObject(PooledObject<StringBuffer> p) throws Exception {
        // TODO Auto-generated method stub

    }
}
