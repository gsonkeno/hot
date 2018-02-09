package com.gsonkeno.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** es滑动查询结果容器
 * Created by gaosong on 2017-04-09.
 */
public class ScrollRespContainer {
    private Client client;
    private SearchResponse scrollResp;
    private int pageSize;

    public ScrollRespContainer(Client client, SearchResponse scrollResp, int pageSize){
        this.client = client;
        this.scrollResp = scrollResp;
        this.pageSize = pageSize;
    }

    public List<Map<String,Object>> next(){
        List<Map<String,Object>> results = new ArrayList<Map<String, Object>>();
        if (scrollResp.getHits().getHits().length != 0){
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                Map<String, Object> source = hit.getSourceAsMap();
                results.add(source);
            }
        }
        scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        return results;
    }
}
