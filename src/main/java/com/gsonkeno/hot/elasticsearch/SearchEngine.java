package com.gsonkeno.hot.elasticsearch;

import com.gsonkeno.hot.elasticsearch.metadata.Field;
import com.gsonkeno.hot.elasticsearch.metadata.Table;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by gaosong on 2017-04-05.
 */
public class SearchEngine implements LucenceIndexer {
    private Client client;

    public boolean createIndex(String indexName, int numberOfShards, int numberOfReplicas) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index.number_of_shards", numberOfShards);
        map.put("index.number_of_replicas", numberOfReplicas);

        //参数设置
        Settings.Builder builder = Settings.builder();
        builder.put("index.number_of_shards", numberOfShards).put("index.number_of_replicas", numberOfReplicas);

        Settings settings = builder.build();
        CreateIndexResponse createIndexResponse = client.admin().indices().
                prepareCreate(indexName).setSettings(settings).get();

        return createIndexResponse.isAcknowledged();
    }

    public boolean deleteIndex(String indexName) {
        DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                .execute().actionGet();
        return dResponse.isAcknowledged();
    }

    public void bulkAddDoc(String index, String type, List<Map<String,Object>> list, String primaryKey) throws IOException {
        Set<String> fields = getFields(index, type);

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        int size  = list.size();
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            XContentBuilder builder = jsonBuilder().startObject();

            Set<String> keys = map.keySet();
            Collection subtract = CollectionUtils.subtract(keys, fields);
            keys.removeAll(subtract);

            for (String key: keys){
                builder.field(key,map.get(key));
            }


            builder.endObject();

            bulkRequest.add(client.prepareIndex(index, type, ObjectUtils.toString(map.get(primaryKey))).setSource(builder));

        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            String message = bulkResponse.buildFailureMessage();
            System.out.println(message);
        }
    }

    public boolean addDoc(String index, String type, Map<String, Object> content, String primaryKey) throws IOException {
        String keyValue = ObjectUtils.toString(content.get(primaryKey));
        Set<String> fields = getFields(index, type);

        XContentBuilder builder = jsonBuilder();
        builder.startObject();

        Set<String> keys = content.keySet();
        Collection subtract = CollectionUtils.subtract(keys, fields);
        keys.removeAll(subtract);

        for (String key: keys) {
            builder.field(key, content.get(key));
        }

        builder.endObject();

        IndexResponse indexResponse = client.prepareIndex(index, type, keyValue).setSource(builder).execute().actionGet();
        DocWriteResponse.Result result = indexResponse.getResult();

        if (result == DocWriteResponse.Result.CREATED) return true;

        return false;
    }

    /**
     * 删除文档
     * @param index 索引名
     * @param type  类型名
     * @param id    文档标识-主键
     * @return
     */
    public boolean deleteDoc(String index, String type, String id) {
        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();

        switch (response.getResult()) {

            case NOT_FOUND:
                System.out.println("索引文档标识" + id + "未找到");
                return false;
            case DELETED:
                System.out.println("标识" + id + "索引文档删除成功");
                return true;
            default:
                return false;
        }
    }

    public void deleteDoc(String index, QueryBuilder queryBuilder){

        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                //.filter(QueryBuilders.matchQuery("gender", "male"))
                .filter(queryBuilder)
                .source(index)
                .get();
        long deleted = response.getDeleted();
        System.out.println("成功删除" + deleted + "个文档");
    }

    public boolean updateDoc(String index, String type, String id, Map<String, Object> content) throws IOException {

        XContentBuilder builder = jsonBuilder();
        builder.startObject();

        for (Map.Entry<String, Object> keyValues : content.entrySet()) {
            builder.field(keyValues.getKey(), keyValues.getValue());
        }

        builder.endObject();

        UpdateResponse updateResponse = client.prepareUpdate(index, type, id).setDoc(builder).get();

        DocWriteResponse.Result result = updateResponse.getResult();

        switch (result) {
            case NOOP:
                System.out.println("文档内容没有任何改变");
                return true;
            case UPDATED:
                System.out.println("更新文档内容成功");
                return true;
            default:
                return false;
        }

    }

    /**
     * 加载schema
     * <br>1.如果schema已经加载过，执行此方法仍然会重新加载。
     * <br>2.如果新增字段，则新增字段会成功；若想删减已经加载过的字段，是不可能的
     * <br>3.对于已经加载过的字段，想修改其类型，是失败的
     * @param index
     * @param type
     * @param schemaPath
     * @throws DocumentException
     * @throws IOException
     */
    public void loadSchema(String index, String type, String schemaPath) throws DocumentException, IOException {
        InputStream in = new FileInputStream(new File(schemaPath));

        SAXReader reader = new SAXReader();

        Document doc = reader.read(in);

        List<Element> tables = doc.getRootElement().elements();

        for (Element table : tables) {

            Table _table = new Table(table);

            XContentBuilder builder = jsonBuilder();
            builder.startObject();
            builder.startObject(type);
            builder.startObject("properties");

            List<Field> fields = _table.getFields();

            for (Field field : fields) {
                String fieldType = field.getType();

                builder.startObject(field.getName())
                        .field("type", fieldType)
                        .field("boost", field.getBoost())
                        .field("index", field.isIndex())
                        .field("store", field.isStore())
                        .field("include_in_all", field.isIncludeInAll());
                
                if (fieldType.equalsIgnoreCase("text")){
                    builder.field("analyzer", field.getAnalyzer());
                }


                builder.endObject();
            }
            builder.endObject();
            builder.endObject();
            builder.endObject();

            System.out.println(builder);

            PutMappingRequest mapping = Requests.putMappingRequest(index).type(type).source(builder);

            PutMappingResponse putMappingResponse = client.admin().indices().putMapping(mapping).actionGet();
            System.out.println("加载schema成功");
        }
    }

    public Map query(String index, String type, String id) {
        GetResponse response = client.prepareGet("twitter", "tweet", "1").get();
        Map<String, Object> source = response.getSource();
        return source;
    }

    public ScrollRespContainer scrollQuery(String index, QueryBuilder queryBuilder, String sortName, SortOrder order, int pageSize){

        SearchResponse scrollResp = client.prepareSearch(index)
                .addSort(sortName, order)
                .setScroll(new TimeValue(60000))
                .setQuery(queryBuilder)
                .setSize(pageSize).get();
        return new ScrollRespContainer(client,scrollResp,pageSize);

    }

    public void multiQuery(){
        SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("b")).setSize(1);
        SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("INFO_ID", "7")).setSize(1);

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

       // You will get all individual responses from MultiSearchResponse#getResponses()
        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            SearchHit[] hits = response.getHits().getHits();

            for(SearchHit hit : hits){
                Map<String, Object> source = hit.getSource();
                System.out.println(source);
            }

            System.out.println("-----------");
            nbHits += response.getHits().getTotalHits();
        }
    }

    /**
     * 检索满足查询条件的文档的统计(不返回文档内容信息，只是统计结果)
     * @param indices
     * @param types
     * @param query
     * @return
     */
    public SearchResponse searchForStatistics(String[] indices, String[] types, Query query){
        //设置查询索引、类型、起始游标、查询数量
        SearchRequestBuilder req = client.prepareSearch(indices).setTypes(types).setSize(0);

        //设置查询方式和查询语句
        req.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(query.getBoolQuery());

        //设置聚合语句
        req.addAggregation(query.getAggregationBuilder());

        SearchResponse sr = req.get();

        return sr;
    }


    /**
     * 检索满足查询条件的文档
     * @param indices
     * @param types
     * @param query
     * @return
     */
    public List<Map<String,Object>> searchForDocs(String[] indices, String[] types, Query query){
        int from = (query.getFromPage()-1)*query.getPageSize();

        //设置查询索引、类型、起始游标、查询数量
        SearchRequestBuilder req = client.prepareSearch(indices).setTypes(types).setFrom(from).setSize(query.getPageSize());

        //设置查询方式和查询语句
        req.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(query.getBoolQuery());

        LinkedList<FieldSortBuilder> fieldSortBuilders = query.getFieldSortBuilders();

        //设置排序，支持多字段排序
        for (int i = 0; i < fieldSortBuilders.size(); i++) {
            FieldSortBuilder fieldSortBuilder = fieldSortBuilders.get(i);
            req.addSort(fieldSortBuilder);
        }

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        List<String> highlightFieldList = query.getHighlightFieldList();
        for (int i = 0; i < highlightFieldList.size(); i++) {
            String highlightField = highlightFieldList.get(i);
            highlightBuilder.field(highlightField);
        }
        highlightBuilder.preTags("<em>").postTags("</em>");

        req.highlighter(highlightBuilder);

        SearchResponse searchResponse = req.get();
        return ResponseHandler.responseToList(searchResponse);
    }



    public List query(String index, String type) {
        return null;
    }


    public SearchEngine(String clusterName, String serverIp, int serverPort) {
        Settings settings = Settings.builder().put("cluster.name", clusterName).build();
        try {
            client = new PreBuiltTransportClient(settings).
                    addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIp), serverPort));
        } catch (UnknownHostException e) {
            System.out.println("获取es客户端失败" + e);
        }
    }



    public Client getClient(){
        return this.client;
    }

    /**
     * 获取指定索引，指定类型的mapping
     * @param index
     * @param type
     */
    private  Map<String, Object> getMapping(String index, String type) throws IOException {
        ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().get()
                .getState().getMetaData().getIndices().get(index).getMappings();
        Map<String, Object> mapping = mappings.get(type).sourceAsMap();

        return mapping;
    }

    private  Set<String> getFields(String index, String type) throws IOException {
        Map<String, Object> mapping = getMapping(index, type);
        mapping = (Map<String,Object>)mapping.get("properties");
        return mapping.keySet();
    }






}
