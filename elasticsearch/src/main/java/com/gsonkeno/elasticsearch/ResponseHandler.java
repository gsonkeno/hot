package com.gsonkeno.elasticsearch;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**搜索结果处理器
 * Created by gaosong on 2017-05-01.
 */
public class ResponseHandler {

    /**
     * 搜索结果文档集转成List
     * @param response
     * @return
     */
    public static List<Map<String,Object>> responseToList(SearchResponse response){
        List<Map<String,Object>> sourceList = new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {

            Map<String, Object> source = hit.getSourceAsMap();

            //_source字段中高亮字段替换
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (Map.Entry<String,HighlightField> hf : highlightFields.entrySet()){
                String key = hf.getKey();
                if (source.containsKey(key)){
                    Text[] fragments = hf.getValue().getFragments();
                    source.put(key,concatText(fragments));
                }
            }

            sourceList.add(source);
        }

        return sourceList;
    }

    /**
     * 连接多个Text，以空格分隔
     * @param fragments
     * @return
     */
    private static String concatText(Text[] fragments){
        StringBuffer sb = new StringBuffer();
        for (Text text : fragments) {
            sb.append(text.string()).append(" ");
        }

        return sb.delete(sb.length()-1,sb.length()).toString();

    }

    /**
     * terms桶聚合统计
     * @param response
     * @param aggregationName
     * @return
     */
    public static Map<Object,Long> responseToTermsStatistics(SearchResponse response,String aggregationName){
        StringTerms aggregation = response.getAggregations().get(aggregationName);
        List<StringTerms.Bucket> buckets = aggregation.getBuckets();

        Map<Object,Long> statistics = new HashMap<Object,Long>();
        for (int i = 0; i < buckets.size(); i++) {
            Terms.Bucket bucket = buckets.get(i);
            Object key = bucket.getKey();
            long docCount = bucket.getDocCount();
            statistics.put(key,docCount);
        }
        return statistics;
    }
}
