package com.gsonkeno.hot.elasticsearch;

import org.dom4j.DocumentException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 全文检索接口
 * Created by gaosong on 2017-04-05.
 */
public interface LucenceIndexer {

    boolean createIndex(String indexName,int numberOfShards, int numberOfReplicas);

    boolean deleteIndex(String indexName);

    void bulkAddDoc(String index, String type, List<Map<String,Object>> list, String primaryKey) throws  IOException;

    boolean addDoc (String index, String type, Map<String,Object> content,String primaryKey) throws IOException;

    /**
     * 删除文档
     * @param index 索引名
     * @param type  类型名
     * @param id    文档标识-主键
     * @return
     */
    boolean deleteDoc(String index, String type, String id);

    /**
     * 删除文档
     * @param index 索引名
     * @param queryBuilder 过滤查询条件
     */
    public void deleteDoc(String index, QueryBuilder queryBuilder);

    boolean updateDoc(String index, String type, String id, Map<String,Object> content) throws IOException;

    /**
     * 加载schema
     * @param index
     * @param type
     * @param schemaPath
     * @throws DocumentException
     * @throws IOException
     */
    void loadSchema(String index, String type, String schemaPath) throws DocumentException, IOException;

    /**
     * 查询文档
     * @param index
     * @param type
     * @param id  文档标识-主键
     * @return
     */
    Map query(String index, String type, String id);

    /**
     * 滑动查询，每屏显示固定数量的结果
     * @param index
     * @param queryBuilder
     * @param sortName
     * @param order
     * @param pageSize
     * @return
     */
    ScrollRespContainer scrollQuery(String index, QueryBuilder queryBuilder, String sortName, SortOrder order, int pageSize);

    List query(String index, String  type);





}
