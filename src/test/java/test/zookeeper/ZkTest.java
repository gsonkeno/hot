package test.zookeeper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaosong on 2017-10-27
 */
public class ZkTest {
    public static void main(String[] args) throws IOException {
        addNode();
        //deleteCamera();
        //addCamera();
    }

    public static void addNode() throws IOException {
        try {
            ZooKeeper zkClient = new ZooKeeper("172.16.58.61:2181", 5000, new DefaultClientWatcher());
            try {
                    JSONObject jo = new JSONObject();

                    jo.put("device_id",   "44010000011320000008");
                    jo.put("device_name", "test");
                    // deviceInfo.put("device_ip",   "172.16.67.101");
                    jo.put("device_ip",   "172.16.67.84");
                    jo.put("device_port", 8000);
                    jo.put("device_user",   "admin");
                    //deviceInfo.put("device_pass",   "Pagz12345");
                    jo.put("device_pass",   "Pagz1234");
                    jo.put("device_type",      1);
                    jo.put("status",      "正常");
                zkClient.create("/bigdata/config/gateway/capture/44010000011320000008/44010000011320000008", jo.toJSONString().getBytes("UTF-8") ,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addCamera(){
        try {
            ZooKeeper zkClient = new ZooKeeper("172.16.58.61:2181", 5000, new DefaultClientWatcher());
            try {
                JSONObject jo = new JSONObject();

                jo.put("device_id",   "44010000011320000008");
                jo.put("device_name", "test");
                // deviceInfo.put("device_ip",   "172.16.67.101");
                jo.put("device_ip",   "172.16.67.84");
                jo.put("device_port", 8000);
                jo.put("device_user",   "admin");
                //deviceInfo.put("device_pass",   "Pagz12345");
                jo.put("device_pass",   "Pagz1234");
                jo.put("device_type",      1);
                jo.put("status",      "正常");

                Stat stat = zkClient.exists("/bigdata/config/gateway/capture/testGateWay", false);
                zkClient.setData("/bigdata/config/gateway/capture/testGateWay/44010000011320000008",jo.toJSONString().getBytes("UTF-8") ,
                        stat.getVersion());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteCamera(){
        try {
            ZooKeeper zkClient = new ZooKeeper("172.16.58.61:2181", 5000, new DefaultClientWatcher());
            try {
                JSONObject jo = new JSONObject();
                jo.put("gateway_id","testGateWay");
                jo.put("dev_type","capture_camera");
                jo.put("gateway_name","测试网关");

                JSONArray devicesInfo = new JSONArray();

                jo.put("devices",devicesInfo);

                Stat stat = zkClient.exists("/bigdata/config/gateway/capture/testGateWay", false);
                zkClient.setData("/bigdata/config/gateway/capture/testGateWay",jo.toJSONString().getBytes("UTF-8") ,
                        stat.getVersion());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



class DefaultClientWatcher implements Watcher
{

    Map<String, WatcherFilter> filters = new HashMap<String, WatcherFilter>();
//	private List<WatcherFilter> copyFilters = new ArrayList<WatcherFilter>();
//	private Set<WatcherFilter> filters = new HashSet<>();


    void addFilter(String path, WatcherFilter filter)
    {
        this.filters.put(path, filter);
//		this.filters.add(filter);
    }

    boolean exists(String path){
        return this.filters.containsKey(path);
    }

    @Override
    public void process(WatchedEvent event)
    {
        for (Map.Entry<String, WatcherFilter> filter : filters.entrySet()) {
            if (null == event.getPath()) {
                continue;
            }

            if (event.getPath().equals(filter.getKey())) {
                filter.getValue().filter(filter.getKey(), event);
                System.out.println(event.getPath() + " 事件被触发");
            }
        }

    }

}

 interface WatcherFilter
{

    public void filter(String path, WatchedEvent event);

}
