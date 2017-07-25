package test.annotation;

import com.gsonkeno.hot.annotation.EsIndex;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaosong on 2017-07-25.
 */
public class EsIndexTest {

    public static void main(String[] args) {

        boolean annotationPresent = WifiModel.class.isAnnotationPresent(EsIndex.class);
        EsIndex esIndex = WifiModel.class.getAnnotation(EsIndex.class);

        System.out.println(esIndex.indexName());
        System.out.println(esIndex.indexType());
        System.out.println(esIndex.shards());
        System.out.println(esIndex.replicas());

        System.out.println(annotationPresent);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("index.number_of_shards", numberOfShards);
//        map.put("index.number_of_replicas", numberOfReplicas);
//
//        //参数设置
//        Settings.Builder builder = Settings.builder();
//        builder.put("index.number_of_shards", numberOfShards).put("index.number_of_replicas", numberOfReplicas);
//
//        Settings settings = builder.build();
//        CreateIndexResponse createIndexResponse = client.admin().indices().
//                prepareCreate(indexName).setSettings(settings).get();
//
//        return createIndexResponse.isAcknowledged();
    }
}
