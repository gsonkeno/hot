package test.elasticsearch;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by gaosong on 2017-04-23.
 */
public class BasicEsTest {

    private  Client client;

    @Before
    public void initClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
        InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300);
        this.client = new PreBuiltTransportClient(settings).addTransportAddress(transportAddress);

    }
    
    /**删除索引**/
    @Test
    public void deleteIndex(){
        IndicesAdminClient adminClient = client.admin().indices();
        DeleteIndexResponse response = adminClient.prepareDelete("test_index").get();

        //DeleteIndexResponse response = adminClient.prepareDelete("test_index").execute().actionGet();
    }

    /**测试store属性**/
    @Test
    public void testStore(){
        SearchRequestBuilder req = client.prepareSearch("index1")
                .setTypes("type1").setSearchType(SearchType.DEFAULT);//构建请求体

        req.addStoredField("name");

        SearchResponse resp = req.setQuery(boolQuery()).get();

        SearchHit[] hits = resp.getHits().getHits();
        
        for (SearchHit hit : hits) {
            Map<String, SearchHitField> fields = hit.getFields();

            System.out.println(fields.size());
            for (Map.Entry<String, SearchHitField> kv: fields.entrySet()){
                System.out.println(kv.getKey() + ":" + kv.getValue().getValue());
            }

            String source = hit.getSourceAsString();
            System.out.println(source);
        }
    }

    /**布尔查询**/
    @Test
    public void boolQueryTest(){
        QueryBuilder qb = boolQuery()
                .must(termQuery("content", "test1"))
                .must(termQuery("content", "test4"))
                .mustNot(termQuery("content", "test2"))
                .should(termQuery("content", "test3"))
                .filter(termQuery("content", "test5"));//构建查询语句

        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("time").order(SortOrder.DESC); //构建排序器
        SearchRequestBuilder req = client.prepareSearch("my_index").setTypes("my_type").setSearchType(SearchType.DEFAULT);//构建请求体
        SearchResponse resp = req.setQuery(qb).setFrom(1).setSize(1).addSort(sortBuilder).get(); //执行请求，获取响应

        SearchHit[] hits = resp.getHits().getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            System.out.println(source);
        }
    }
    /**创建索引，加索引设置，加type,加mapping**/
    @Test
    public void createIndexAndSettingsAndAddMapping() throws IOException {
        IndicesAdminClient adminClient = client.admin().indices();
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("test_type")

                .startObject("_all")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()

                .startObject("properties")


                .startObject("name")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()

                .endObject()
                .endObject()
                .endObject();
        CreateIndexResponse createIndexResponse = adminClient.prepareCreate("test_index").addMapping("test_type", builder).get();
    }

    /**对于已存在的索引增加一个type并设置mapping**/
    @Test
    public void addTypeAndMappingForExistedIndex() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("test_type_1")

                .startObject("_all")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()

                .startObject("properties")


                .startObject("name")
                .field("type","text")
                .field("analyzer","ik_max_word")
                .field("search_analyzer","ik_max_word")
                .endObject()

                .endObject()
                .endObject()
                .endObject();
        PutMappingResponse resp = client.admin().indices().preparePutMapping("test_index")
                .setType("test_type_1")
                .setSource(builder)
                .get();

    }

    /**对于已存在的索引和type更新mapping(更新mapping字段类型不建议，可能会报错，常见的是增加字段)**/
    @Test
    public void updateMappingForExistedIndex() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("test_type_1")

                .startObject("properties")


                .startObject("sex")
                .field("type","integer")
                .endObject()

                .endObject()
                .endObject()
                .endObject();
        PutMappingResponse resp = client.admin().indices().preparePutMapping("test_index")
                .setType("test_type_1")
                .setSource(builder)
                .get();
    }
    /**最简单的聚合查询**/
    @Test
    public void  aggregationTest(){
        SearchResponse sr = client.prepareSearch("cars").setTypes("transactions")
                .setSearchType(SearchType.DEFAULT)

                .setQuery(QueryBuilders.matchAllQuery() )
                .addAggregation(  AggregationBuilders.terms("popular_color").field("color"))
                .get();


        Aggregations aggregations = sr.getAggregations();
        StringTerms popular_color_aggregation = sr.getAggregations().get("popular_color");

        List<Terms.Bucket> buckets = popular_color_aggregation.getBuckets();

        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            Object key = bucket.getKey();
            long docCount = bucket.getDocCount();
            System.out.println(key + "_" + docCount);
        }



        SearchHit[] hits = sr.getHits().getHits();
        for (SearchHit hit :
                hits) {
            System.out.println(hit.getSourceAsString());
        }
    }

    /**删索引->建索引->加载mapping->插入数据**/
    @Test
    public void loadSchema() throws IOException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = null;

        try {
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("获取es客户端失败" + e);
        }

        String testindex = "testindex";  //索引名
        String testtype = "testtype"; //type名

        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(testindex);

        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        boolean exists = inExistsResponse.isExists();

        if (exists) {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(testindex)
                    .execute().actionGet();
            System.out.println("删除索引" + dResponse.isAcknowledged());
        }




        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index.number_of_shards", 5);
        map.put("index.number_of_replicas", 1);

        Settings.Builder builder = Settings.builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
        settings = builder.build();
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(testindex)
                .setSettings(settings).execute().actionGet();

        //加载mapping
        XContentBuilder xbuilder = jsonBuilder();

        xbuilder.startObject().startObject(testtype).startObject("properties");

        xbuilder.startObject("field1").field("type","binary").endObject();

        xbuilder.endObject().endObject().endObject();

        PutMappingRequest mapping = Requests.putMappingRequest(testindex).type(testtype).source(xbuilder);

        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping).actionGet();

        //插入一条数据


        XContentBuilder builder2 = jsonBuilder();
        builder2.startObject();

        builder2.field("field1", new BASE64Decoder().decodeBuffer("bsaxadakx"));

        builder2.endObject();

        IndexResponse indexResponse = client.prepareIndex(testindex, testtype, "1").setSource(builder2).execute().actionGet();
        DocWriteResponse.Result result = indexResponse.getResult();

        if (result == DocWriteResponse.Result.CREATED)
            System.out.println("插入一条数据成功");
    }

    /**删除索引->新建索引->加载mapping->插入数据->测试全文检索 querystring**/
    @Test
    public void loadSchema2() throws IOException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = null;

        try {
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("获取es客户端失败" + e);
        }

        String testindex = "queryindex";  //索引名
        String testtype = "querytype"; //type名

        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(testindex);

        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        boolean exists = inExistsResponse.isExists();

        if (exists) {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(testindex)
                    .execute().actionGet();
            System.out.println("删除索引" + dResponse.isAcknowledged());
        }




        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index.number_of_shards", 5);
        map.put("index.number_of_replicas", 1);

        Settings.Builder builder = Settings.builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
        settings = builder.build();
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(testindex)
                .setSettings(settings).execute().actionGet();

        //加载mapping
        XContentBuilder xbuilder = jsonBuilder();

        xbuilder.startObject().startObject(testtype).startObject("properties");

        xbuilder.startObject("field1");

        xbuilder.field("type","text");

        xbuilder.startObject("fields");
        xbuilder.startObject("keyword");
        xbuilder.field("type","keyword");
        xbuilder.field("ignore_above",256);
        xbuilder.endObject();
        xbuilder.endObject();

        xbuilder.endObject();

        xbuilder.endObject().endObject().endObject();

        PutMappingRequest mapping = Requests.putMappingRequest(testindex).type(testtype).source(xbuilder);

        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping).actionGet();

        //插入一条数据


        XContentBuilder builder2 = jsonBuilder();
        builder2.startObject();

        builder2.field("field1", "abxo zjs");

        builder2.endObject();

        IndexResponse indexResponse = client.prepareIndex(testindex, testtype, "1").setSource(builder2).execute().actionGet();
        DocWriteResponse.Result result = indexResponse.getResult();

        if (result == DocWriteResponse.Result.CREATED)
            System.out.println("插入一条数据成功");
    }

    /**删除索引->新建索引->加载mapping->插入数据->测试全文检索 keyword**/
    @Test
    public void loadSchema3() throws IOException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = null;

        try {
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            System.out.println("获取es客户端失败" + e);
        }

        String testindex = "index3";  //索引名
        String testtype = "type3"; //type名

        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(testindex);

        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();

        boolean exists = inExistsResponse.isExists();

        if (exists) {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(testindex)
                    .execute().actionGet();
            System.out.println("删除索引" + dResponse.isAcknowledged());
        }




        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index.number_of_shards", 5);
        map.put("index.number_of_replicas", 1);

        Settings.Builder builder = Settings.builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.put(entry.getKey(), entry.getValue());
        }
        settings = builder.build();
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(testindex)
                .setSettings(settings).execute().actionGet();

        //加载mapping
        XContentBuilder xbuilder = jsonBuilder();

        xbuilder.startObject().startObject(testtype).startObject("properties");

        xbuilder.startObject("field1");

        xbuilder.field("type","keyword");
        xbuilder.endObject();


        xbuilder.endObject().endObject().endObject();

        PutMappingRequest mapping = Requests.putMappingRequest(testindex).type(testtype).source(xbuilder);

        PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping).actionGet();

        //插入一条数据

        XContentBuilder builder2 = jsonBuilder();
        builder2.startObject();

        builder2.field("field1", "805940564@qq.com  127.0.0.1");

        builder2.endObject();

        IndexResponse indexResponse = client.prepareIndex(testindex, testtype, "1").setSource(builder2).execute().actionGet();
        DocWriteResponse.Result result = indexResponse.getResult();

        if (result == DocWriteResponse.Result.CREATED)
            System.out.println("插入一条数据成功");

        builder2 = jsonBuilder();
        builder2.startObject();

        builder2.field("field1", "中国 美国");

        builder2.endObject();

        indexResponse = client.prepareIndex(testindex, testtype, "2").setSource(builder2).execute().actionGet();
        result = indexResponse.getResult();

        if (result == DocWriteResponse.Result.CREATED)
            System.out.println("插入一条数据成功");
    }

    @Test
    public void queryDoc() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));

        GetResponse response = client.prepareGet("my_index", "my_type", "1").get();
        Map<String, Object> source = response.getSource();

        System.out.println(source);
        //{lists=[{name=prog_list, description=programming list}, {name=cool_list, description=cool stuff list}],
        // message=some arrays in this document..., tags=[elasticsearch, wow]}

        Object lists = source.get("lists");
        System.out.println(lists);
        //[{name=prog_list, description=programming list}, {name=cool_list, description=cool stuff list}]

        if (lists instanceof ArrayList){
            System.out.println("lists是List对象"); //执行

            ArrayList lists1 = (ArrayList) lists;

            Object o = lists1.get(0);

            if (o instanceof Map){
                System.out.println("o是Map对象"); //执行
            }
        }

        Object tags = source.get("tags");

        if (tags instanceof ArrayList)
            System.out.println(true);

        ArrayList tags1 = (ArrayList) tags;

        Object o = tags1.get(0);

        if (o instanceof String)
            System.out.println(true);
    }

    /**更新索引**/
    @Test
    public void updateDoc() throws IOException, ExecutionException, InterruptedException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = null;
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));



        UpdateRequest updateRequest = new UpdateRequest("my_index", "my_type", "2")
                .doc(jsonBuilder()
                        .startObject()
                        .field("name", "23456")
                        .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();



    }

    //批处理线程
    @Test
    public void testBulkProcessor() throws IOException {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        Client client = new PreBuiltTransportClient(settings).
                addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));


        BulkProcessor bulkProcessor = BulkProcessor.builder(client, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                System.out.println("执行bulk前");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                System.out.println("执行bulk后");
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {

                System.out.println("执行bulk后异常");
            } /* Listener methods */ })
                .setBulkActions(10000)
                .setConcurrentRequests(0)
                .build();

        // Add your requests
        bulkProcessor.add(new IndexRequest("my_index", "my_type", "1")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Gao Song")
                        .endObject()));

        bulkProcessor.add(new IndexRequest("my_index", "my_type", "2")
                .source(jsonBuilder()
                        .startObject()
                        .field("name", "Zha ks")
                        .endObject()));
       // bulkProcessor.add(new DeleteRequest("my_index","my_type","1"));


        System.out.println("flush前");
        // Flush any remaining requests
                bulkProcessor.flush();
        System.out.println("flush后");
        // Or close the bulkProcessor if you don't need it anymore
                bulkProcessor.close();
        System.out.println("关闭后");
        // Refresh your indices
                client.admin().indices().prepareRefresh().get();

        // Now you can start searching!
        SearchResponse searchResponse = client.prepareSearch().setSize(20).get();

        SearchHits hits = searchResponse.getHits();

        SearchHit[] hits1 = hits.getHits();

        for (SearchHit hit :hits1) {

            System.out.println(hit.getSourceAsString());
        }
    }
}
