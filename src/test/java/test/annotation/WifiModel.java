package test.annotation;

import com.gsonkeno.hot.annotation.EsIndex;

/**
 * Created by gaosong on 2017-07-25.
 */
@EsIndex(indexName = "wifi_index", indexType = "wifi_info",replicas = 0, shards = 2)
public class WifiModel {

    private  String infoId;

    private  String address;

    private  String mac;


}
