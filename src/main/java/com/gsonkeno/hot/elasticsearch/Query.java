package com.gsonkeno.hot.elasticsearch;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by gaosong on 2017-04-30.
 */
public class Query {
    /**起始查询页,以1开始**/
    private int fromPage;

    /**每页查询数量**/
    private int pageSize;

    private BoolQueryBuilder boolQuery;

    private AggregationBuilder aggregationBuilder ;

    private LinkedList<FieldSortBuilder> fieldSortBuilders;

    private List<String> highlightFieldList;


    public Query(int fromPage, int pageSize){
        this.fromPage = fromPage;
        this.pageSize = pageSize;
        boolQuery = QueryBuilders.boolQuery();
        fieldSortBuilders = new LinkedList<FieldSortBuilder>();
        highlightFieldList = new ArrayList<String>();
    }

    public Query(){
        boolQuery = QueryBuilders.boolQuery();
        fieldSortBuilders = new LinkedList<FieldSortBuilder>();
    }

    /**
     * query上下文中查询(包含)
     * @param filed  查询字段
     * @param values 查询的字段值，支持多个，只要文档满足一个词条，则通过
     */
    public void addTermsQuery(String filed, Object...values){
        boolQuery.must(termsQuery(filed,values));
    }
    /**
     * terms查询子句
     * @param field
     * @param values
     * @param clauseType
     */
    public void addTermsQuery(ClauseType clauseType,String field, Object...values){
        buildClauseQuery(clauseType,termsQuery(field,values));
    }

    /**
     * 范围查询,只有起点
     * @param field 查询字段
     * @param from  查询字段起始值
     * @param includeLower 是否包含起始值
     */
    public void addRangeFromQuery(String field,Object from,boolean includeLower){
        boolQuery.must(rangeQuery(field).from(from).includeLower(includeLower));
    }

    /**
     * 范围查询,只有终止值
     * @param field 查询字段
     * @param to  查询字段终止值
     * @param includeUpper 是否包含终止值
     */
    public void addRangeToQuery(String field,Object to,boolean includeUpper){
        boolQuery.must(rangeQuery(field).to(to).includeLower(includeUpper));
    }



    /**
     * 范围查询
     * @param field 查询字段
     * @param from  查询字段起始值，包含自身
     * @param to    查询字段结束值，包含自身
     */
    public void addRangeQuery(String field,Object from, Object to){
        boolQuery.must(rangeQuery(field).gte(from).lte(to));
    }

    /**
     * 范围查询
     * @param field 查询字段
     * @param from  查询字段起始值
     * @param includeLower 是否包含查询字段起始值
     * @param to    查询字段结束值
     * @param includeUpper 是否包含查询字段结束值
     */
    public void addRangeQuery(String field,Object from, boolean includeLower,Object to,boolean includeUpper){
        boolQuery.must(rangeQuery(field).from(from,includeLower).to(to,includeUpper));
    }

    /**
     * 范围子句
     * @param clauseType 子句类型
     * @param field 范围查询字段
     * @param from  范围查询字段起始值(包含自身)
     * @param to    范围查询字段结束值(包含自身)
     */
    public void addRangeQuery(ClauseType clauseType,String field,Object from, Object to){
        buildClauseQuery(clauseType,rangeQuery(field).gte(from).lte(to));
    }

    /**
     * 范围子句
     * @param clauseType 子句类型
     * @param field 范围查询字段
     * @param from  范围查询字段起始值
     * @param includeLower 是否包含字段起始值
     * @param to    范围查询字段结束值
     * @param includeUpper 是否包含字段结束值
     */
    public void addRangeQuery(ClauseType clauseType, String field,Object from, boolean includeLower,Object to,boolean includeUpper){
        buildClauseQuery(clauseType,rangeQuery(field).from(from,includeLower).to(to,includeUpper));
    }



    /**通配符查询
     * @see
     * @param field 查询字段
     * @param value 查询字段值
     */
    public void addWildcardQuery(String field, String value){
        boolQuery.must(wildcardQuery(field,value));
    }

    /**
     * 通配符查询
     * @param clauseType 子句类型
     * @param field 查询字段
     * @param value 查询字段值
     */
    public void addWildcardQuery(ClauseType clauseType, String field,String value){
        buildClauseQuery(clauseType,wildcardQuery(field,value));
    }

    /**
     * @see #addStringQuery(ClauseType, String[], String, Operator)
     * @param field
     * @param queryString
     */
    public void addStringQuery( String field, String queryString){
        addStringQuery(ClauseType.MUST,new String[]{field},queryString,Operator.OR);
    }

    /**
     * @see #addStringQuery(ClauseType, String[], String, Operator)
     * @param fields
     * @param queryString
     */
    public void addStringQuery( String[] fields, String queryString){
        addStringQuery(ClauseType.MUST,fields,queryString,Operator.OR);
    }


    /**
     * @see  #addStringQuery(ClauseType, String[], String, Operator)
     * @param clauseType
     * @param fields
     * @param queryString
     */
    public void addStringQuery(ClauseType clauseType,  String[] fields, String queryString){
        addStringQuery(clauseType,fields,queryString,Operator.OR);
    }

    /**
     * @see #addStringQuery(ClauseType, String[], String, Operator)
     * @param fields
     * @param queryString
     * @param operatorType
     */
    public void addStringQuery( String[] fields, String queryString, Operator operatorType){
        addStringQuery(ClauseType.MUST,fields,queryString,operatorType);
    }

    /**
     * string_query查询
     * @param clauseType 子句类型
     * @param fields      查询字段
     * @param queryString  查询语句
     * @param operatorType 查询语句分析方式(AND或OR,默认OR)
     */
    public void addStringQuery(ClauseType clauseType, String[] fields, String queryString,Operator operatorType){
        QueryStringQueryBuilder queryStringQueryBuilder = queryStringQuery(queryString);

        for (String field : fields) {
            queryStringQueryBuilder.field(field);
        }

        buildClauseQuery(clauseType,queryStringQueryBuilder.defaultOperator(operatorType));
    }


    /**
     * 前缀查询
     * @param field 查询字段
     * @param prefix 字段前缀
     */
    public void addPrefixQuery(String field,String prefix){

        boolQuery.must(prefixQuery(field,prefix));
    }

    /**
     * 前缀子句
     * @param clauseType 子句类型
     * @param field 前缀查询字段
     * @param prefix 前缀
     */
    public void addPrefixQuery(ClauseType clauseType,String field,String prefix){
        buildClauseQuery(clauseType,prefixQuery(field,prefix));
    }

    /**
     * 模糊查询
     * @param field 模糊查询字段
     * @param value 模糊查询值
     * @param distance 单个词条的容错位个数，值只能选择0,1,2
     */
    public void addFuzzyQuery(String field, Object value, int distance ){
        boolQuery.must(fuzzyQuery(field,value).fuzziness(Fuzziness.fromEdits(distance)));
    }

    /**
     * 模糊查询子句
     * @param clauseType 子句类型
     * @param field 子句查询字段
     * @param value 子句查询值
     * @param distance 单个词条的容错位个数，值只能选择0,1,2
     */
    public void addFuzzyQuery(ClauseType clauseType,String field, Object value, int distance){
        buildClauseQuery(clauseType,fuzzyQuery(field,value).fuzziness(Fuzziness.fromEdits(distance)));
    }

    public void addOrder(String field, SortOrder order){
        FieldSortBuilder fieldSort = SortBuilders.fieldSort(field).order(order);
        fieldSortBuilders.add(fieldSort);
    }

    public void addHighLighting(String highLightField){
        this.highlightFieldList.add(highLightField);
    }

    /**设置aggregationBuilder**/
    public void addAggregation(AggregationBuilder aggregationBuilder){
        this.aggregationBuilder = aggregationBuilder;
    }

    /**
     * bool查询中链式追加子句
     * @param clauseType 子句类型
     * @param query      子句
     */
    private void buildClauseQuery(ClauseType clauseType, QueryBuilder query){
        switch (clauseType){
            case MUST:
                boolQuery.must(query);
                break;

            case SHOULD:
                boolQuery.should(query);
                break;

            case MUST_NOT:
                boolQuery.mustNot(query);
                break;

            case FILTER:
                boolQuery.filter(query);
        }
    }

    public int getFromPage() {
        return fromPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<String> getHighlightFieldList(){
        return this.highlightFieldList;
    }

    public BoolQueryBuilder getBoolQuery() {
        return boolQuery;
    }

    public LinkedList<FieldSortBuilder> getFieldSortBuilders() {
        return fieldSortBuilders;
    }

    public AggregationBuilder getAggregationBuilder(){
        return this.aggregationBuilder;
    }
}
