package test.annotation;

import com.gsonkeno.hot.annotation.ES_FIELD;
import com.gsonkeno.hot.annotation.ES_INXDEX;
import com.gsonkeno.hot.meta.DataType;
import com.gsonkeno.hot.meta.IndexWay;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gaosong on 2017-07-25.
 */
public class EsIndexTest {

    public static void main(String[] args) throws IOException {

        boolean annotationPresent = WifiModel.class.isAnnotationPresent(ES_INXDEX.class);
        ES_INXDEX esIndex = WifiModel.class.getAnnotation(ES_INXDEX.class);

        String indexName = esIndex.indexName();
        String indexType = esIndex.indexType();
        int shards = esIndex.shards();
        int replicas =  esIndex.replicas();

//        Method[] methods = WifiModel.class.getDeclaredMethods();
//        for (Method method : methods){
//            method.isAnnotationPresent()
//        }
        Field[] fields = WifiModel.class.getDeclaredFields();
        //加载mapping
        XContentBuilder xbuilder = XContentFactory.jsonBuilder();

        xbuilder.startObject().startObject(indexType).startObject("properties");

        for (Field field : fields){
            if (field.isAnnotationPresent(ES_FIELD.class)){
                ES_FIELD esField = field.getAnnotation(ES_FIELD.class);
                String fieldName = esField.fieldName();
                DataType dataType = esField.fieldType();
                IndexWay indexWay = esField.index();
                boolean store = esField.store();
                boolean inAll = esField.includeInAll();
                String analyzer = esField.analyzer();

                xbuilder.startObject(fieldName)
                        .field("type",dataType.getValue())
                        .field("index",indexWay.getValue())
                        .field("store",store)
                        .field("include_in_all",inAll);

                if (!analyzer.equals("") && dataType != DataType.KEYWORD){
                    xbuilder.field("analyzer",analyzer);
                }
                xbuilder.endObject();
            }
        }

        xbuilder.endObject().endObject().endObject();

        PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(indexType).source(xbuilder);

        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = null;

        try {
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("获取es客户端失败" + e);
        }

        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);

        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        boolean exists = inExistsResponse.isExists();

        if (exists) {
            System.out.println("索引已经存在");
            return;
//            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
//                    .execute().actionGet();
//            System.out.println("删除索引" + dResponse.isAcknowledged());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index.number_of_shards", shards);
        map.put("index.number_of_replicas", replicas);

        Settings.Builder builder = Settings.builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
        settings = builder.build();
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(indexName)
                .setSettings(settings).execute().actionGet();

        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping).actionGet();

//        System.out.println(annotationPresent);
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
