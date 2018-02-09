package com.gsonkeno.elasticsearch5_3.metadata;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table
{
	/**索引类型**/
	private String name;

	/**索引描述**/
	private String title;

	/**索引类型下的字段主键**/
	private Field primaryKeyField;

	/**索引名称**/
	private String index;

	/**类型下的所有字段(包括主键)**/
	private List<Field> fields = new ArrayList<Field>();
	

	@SuppressWarnings("unchecked")
	public Table(Element element)
	{
		this.name = element.getName();

		this.title = StringUtils.defaultString(element.attributeValue("title"), "");

		this.index = StringUtils.defaultString(element.attributeValue("index"), "");
		
		List<Element> columns = element.elements("column");
		
		for (Element column: columns) {
            Field field = new Field(column);
            fields.add(field);
			
			if (field.isPkGenerator()) {
				this.primaryKeyField = field;
			}			
		}
	}

	public String getName()
	{
		return this.name;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getindex()
	{
		return this.index;
	}
	
	public Field getPrimaryKey()
	{
		return this.primaryKeyField;
	}
	
	public List<Field> getFields()
	{
		return this.fields;
	}
	
	public Map<String, Field> getMetaData()
	{
		Map<String, Field> metadata = new HashMap<String, Field>();	
		
		for (Field field: fields) {
			metadata.put(field.getName(), field);
		}	
		
		return metadata;
	}
		
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}
}
