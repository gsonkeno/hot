package com.gsonkeno.elasticsearch;

import com.gsonkeno.elasticsearch.metadata.Field;
import com.gsonkeno.elasticsearch.metadata.Table;
import org.apache.commons.lang.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维护着若干索引结构
 */
public class Schema {
    private String schemaPath;

    private Map<String, Table> tableList = new HashMap<String, Table>();


    public Schema(String schemaPath) {
        this.schemaPath = schemaPath;
        getAllTables();
    }

    public Map<String, Table> getTableList() {
        return tableList;
    }

    private Map<String, Table> getAllTables() {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new File(schemaPath));
        } catch (DocumentException e) {
            System.out.println(e);
        }

        Element root = doc.getRootElement();
        List<Element> elements = root.elements();

        for (int i = 0; i < elements.size(); i++) {
            String type_name = elements.get(i).getName();
            Table table = new Table(elements.get(i));
            tableList.put(type_name, table);
        }

        return tableList;
    }

    /***
     * 生成符合schema要求的Document数据
     * @param table 文档结构
     * @param row   文档数据
     * @return 符合schema要求的文档数据
     */
        static Map<String, Object> indexRowMapping(Table table, Map<String, Object> row) {

        List<Field> fields = table.getFields();

        Map<String, Object> values = new HashMap<String, Object>();

        StringBuffer keywords = new StringBuffer();
        StringBuffer _sort = new StringBuffer();
        boolean sortFlag = false;

        String keyword, defaultVal;

        for (Field field : fields) {

            if (row.containsKey(field.getName()) && field.isStore()) {

                if (row.get(field.getName()) != null ) {

                    keyword = row.get(field.getName()).toString();

                    values.put(field.getName(), row.get(field.getName()));


                    if (field.getType().equalsIgnoreCase("text") ) {
                        defaultVal = ObjectUtils.defaultIfNull(row.get(field.getName()), "").toString();


                        values.put(field.getName(), defaultVal);
                    }
                    if (field.getType().equalsIgnoreCase("Integer")) {
                        defaultVal = ObjectUtils.toString(row.get(field.getName()), "-1");


                        values.put(field.getName(), Integer.valueOf(defaultVal));
                    }
                    if (field.getType().equalsIgnoreCase("Long")) {
                        defaultVal = ObjectUtils.toString(row.get(field.getName()), "-1");

                        values.put(field.getName(), Long.valueOf(defaultVal));
                    }
                    if (field.getType().equalsIgnoreCase("Numeric")) {
                        values.put(field.getName(), ((BigDecimal) row.get(field.getName())).longValue());
                    }
                    if (field.getType().equalsIgnoreCase("Float")) {
                        values.put(field.getName(), (Float) row.get(field.getName()));
                    }

                }
            }
        }

        if (!values.containsKey("KEYWORDS") && keywords.length() > 0) {
            values.put("KEYWORDS", keywords.toString());
        }

        if (sortFlag) {
            values.put("_sort", _sort.toString().substring(1));
        }

        return values;
    }

}
