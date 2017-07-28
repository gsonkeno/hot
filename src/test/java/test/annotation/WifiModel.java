package test.annotation;

import com.gsonkeno.hot.annotation.ES_FIELD;
import com.gsonkeno.hot.annotation.ES_INXDEX;
import com.gsonkeno.hot.meta.DataType;

/**
 * Created by gaosong on 2017-07-25.
 */
@ES_INXDEX(indexName = "wifi_index", indexType = "wifi_info",replicas = 0, shards = 2)
public class WifiModel {

    @ES_FIELD(fieldType = DataType.KEYWORD,fieldName = "INFO_ID")
    private  String infoId;

    @ES_FIELD(fieldType = DataType.TEXT,fieldName = "ADDRESS",analyzer = "ik_max_word", store = true)
    private  String address;

    @ES_FIELD(fieldType = DataType.KEYWORD,fieldName = "MAC")
    private  String mac;
}
