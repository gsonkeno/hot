package com.gsonkeno.elasticsearch5_3;


import org.apache.commons.lang.math.RandomUtils;
import org.dom4j.DocumentException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * Created by gaosong on 2017-04-05.
 */
public class SearchEngineTest {
    public static void main(String[] args) {
        SearchEngine engine = new SearchEngine("elasticsearch", "127.0.0.1", 9300);
        //engine.createIndex("first",8,0);
        System.out.println(engine.deleteIndex("first"));
    }

    @Test
    public void createIndex(){
        SearchEngine engine = new SearchEngine("elasticsearch", "127.0.0.1", 9300);
        boolean b = engine.createIndex("first", 8, 0);
        System.out.println(b);
    }
    @Test
    public void deleteIndex(){
        SearchEngine engine = new SearchEngine("elasticsearch", "127.0.0.1", 9300);
        boolean b = engine.deleteIndex("first");
        System.out.println(b);
    }

    //创建索引结构
    @Test
    public void loadSchema() throws IOException, DocumentException {
        SearchEngine engine = new SearchEngine("elasticsearch", "127.0.0.1", 9300);
        engine.loadSchema("first", "TYPE_NAME", "E:\\ideaProject\\summerProject\\summer\\src\\test\\resources\\bigdata_wifi.xml");
    }

    @Test
    public  void addDoc() throws IOException {
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        Map<String,Object> map = new HashMap<>();
        map.put("INFO_ID", RandomUtils.nextLong());
        map.put("MAC",425625 );
        map.put("JGRQ","17231" );
        map.put("DEVICE_ID","shadi7q" );
        map.put("DEVICE_NAME","shadi7q");
        engine.addDoc("first","TYPE_NAME",map,"INFO_ID");

    }


    //删除文档
    @Test
    public  void deleteDoc(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        boolean b = engine.deleteDoc("first", "type", "1");
        System.out.println(b);
    }

    //更新文档
    @Test
    public  void updateDoc() throws IOException {
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        Map<String,Object> updateParams = new HashMap<String, Object>();
        updateParams.put("MAC","abc");
        boolean b = engine.updateDoc("first","type","1",updateParams);
        for (int i =2;i<10;i++){
            engine.updateDoc("first","type",""+i,updateParams);
        }
        System.out.println(b);
    }

    @Test
    public void scrollQuery(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        QueryBuilder termQueryBuilder = termQuery("MAC", "abc");
        ScrollRespContainer scrollRespContainer = engine.scrollQuery("first", termQueryBuilder, FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC, 2);

        List<Map<String, Object>> next = scrollRespContainer.next();
        System.out.println(next);
        System.out.println(scrollRespContainer.next());

    }

    /**多条件查询**/
    @Test
    public void multiQuery(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        engine.multiQuery();
    }

    /**批量插入文档**/
    @Test
    public void bulkAdd() throws IOException {
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);
        List<Map<String,Object>> list = new ArrayList<>();

        for (int i = 0; i <100 ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("INFO_ID", RandomUtils.nextLong());
            map.put("MAC",425625 + i);
            map.put("JGRQ","17231" + i);
            map.put("DEVICE_ID","shadi7q" + i);
            map.put("DEVICE_NAME","shadi7q" + i);
            list.add(map);
        }

        System.out.println(list.get(0).get("INFO_ID"));
        System.out.println(list.get(99).get("INFO_ID"));

        engine.bulkAddDoc("first","TYPE_NAME",list,"INFO_ID");
    }

    /**查询条件查询**/
    @Test
    public  void testSearch(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);

        Query query = new Query(1,10);
        query.addOrder("price",SortOrder.ASC);
//        query.addRangeQuery("price",10000L,500000L);
//        query.addPrefixQuery("color","g");
        query.addFuzzyQuery("make","aoyotm",2);

        String[] indices = new String[]{"cars"};
        String[] types = new String[]{"transactions"};

        List<Map<String, Object>> list = engine.searchForDocs(indices, types, query);

        System.out.println(list.size());
        System.out.println(list);
    }

    /**查询条件查询,测试string_query**/
    @Test
    public  void testSearchStringQuery(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);

        Query query = new Query(1,10);
        query.addStringQuery(new String[]{"name"},"中国美国", Operator.AND);

        String[] indices = new String[]{"test_index"};
        String[] types = new String[]{"test_type"};

        List<Map<String, Object>> list = engine.searchForDocs(indices, types, query);

        System.out.println(list.size());
        System.out.println(list);
    }

    /**查询条件查询,测试wildcard**/
    @Test
    public  void testSearchWildcardQuery(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);

        Query query = new Query(1,10);
       // query.addWildcardQuery("name","中国美国");

        String[] indices = new String[]{"test_index"};
        String[] types = new String[]{"test_type"};

        List<Map<String, Object>> list = engine.searchForDocs(indices, types, query);

        System.out.println(list.size());
        System.out.println(list);
    }

    /**测试terms桶聚合**/
    @Test
    public void testTermsAggregation(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);

//        engine.getClient().prepareSearch()
//                .addAggregation(
//                        AggregationBuilders.terms("by_country").field("country")
//                                .subAggregation(AggregationBuilders.dateHistogram("by_year")
//                                        .field("dateOfBirth")
//                                        .dateHistogramInterval(DateHistogramInterval.YEAR)
//                                        .subAggregation(AggregationBuilders.avg("avg_children").field("children"))
//                                )
//                )
//                .execute().actionGet();
        String[] indices = new String[]{"cars"};
        String[] types = new String[]{"transactions"};

        Query query = new Query();
        query.addRangeFromQuery("price",15000,false);
        query.addAggregation(AggregationBuilders.terms("popular_color").field("color"));

        SearchResponse searchResponse = engine.searchForStatistics(indices, types, query);
        Map<Object, Long> statistics = ResponseHandler.responseToTermsStatistics(searchResponse, "popular_color");

        System.out.println(statistics);

    }

    /**测试高亮字段**/
    @Test
    public void testHighlighting(){
        SearchEngine engine = new SearchEngine("elasticsearch","127.0.0.1",9300);

        String[] indices = new String[]{"test_index"};
        String[] types = new String[]{"test_type"};

        Query query = new Query(1,100);
        query.addTermsQuery("name","中国");
        query.addHighLighting("name");
        List<Map<String, Object>> list = engine.searchForDocs(indices, types, query);

        System.out.println(list);

    }
}
