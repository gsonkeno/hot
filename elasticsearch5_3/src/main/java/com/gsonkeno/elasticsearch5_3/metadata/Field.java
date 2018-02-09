package com.gsonkeno.elasticsearch5_3.metadata;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.dom4j.Element;


/**
 * 字段属性描述
 */
public class Field
{

    /**字段名称**/
	private String name;

	/**字段描述**/
	private String title;

	/**字段类型**/
	private String type;

	/**字段得分重要性**/
	private int boost;
	
	private boolean isPkGenerator;

	private boolean index;
	
	private boolean store;

	/**分词器**/
	private String analyzer;

    private boolean includeInAll;
	
	public Field(Element field)
	{
		
		this.name = field.attributeValue("name");

		this.title = StringUtils.defaultString(field.attributeValue("title"),"");
		
		this.type = field.attributeValue("type");
		
		this.boost = Integer.parseInt(StringUtils.defaultString(field.attributeValue("boost"), "1"));

		this.isPkGenerator = field.attributeValue("pk-generator") == null? false:true;
		
		this.index = field.attributeValue("index","").equals("true");
		
		this.store = field.attributeValue("store","").equals("true");

		this.analyzer = field.attributeValue("analyzer","ik_max_word");

		this.includeInAll = field.attributeValue("include_in_all", "").equals("false")? false:true;

	}


	public String getName()
	{
		return this.name;
	}


	
	public String getType()
	{
		return this.type;
	}

	public boolean isPkGenerator()
	{
		return this.isPkGenerator;
	}
	
	public boolean isIndex()
	{
		return this.index;
	}


	public boolean isStore()
	{
		return this.store;
	}

	
	public int getBoost()
	{
		return this.boost;
	}

	
	public String toString()
	{
		return ReflectionToStringBuilder.toString(this);
	}

    public boolean isIncludeInAll() {
        return includeInAll;
    }

    public String getAnalyzer() {
        return analyzer;
    }
}
