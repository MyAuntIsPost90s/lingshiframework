package lingshi.mybaties.mapperextend;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class LingShiMapperPlugin extends PluginAdapter {
	/**
	 * 生成dao
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
				"BaseMapper<" + introspectedTable.getBaseRecordType() + ">");
		FullyQualifiedJavaType imp = new FullyQualifiedJavaType("lingshi.mybaties.mapperextend.BaseMapper");
		interfaze.addSuperInterface(fqjt);// 添加 extends BaseDao<User>
		interfaze.addImportedType(imp);// 添加import common.BaseDao;
		interfaze.getMethods().clear();
		return true;
	}

	/**
	 * 生成mapping 添加自定义sql
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement parentElement = document.getRootElement();

		parentElement.getElements().clear(); // 移除自带sql
		parentElement.addElement(getListXmlElement(introspectedTable)); // 添加getList
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(getSingleXmlElement(introspectedTable)); // 添加getSingle
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(insertXmlElement(introspectedTable)); // 添加insert
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(updateXmlElement(introspectedTable)); // 添加update
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(batchDeleteXmlElement(introspectedTable)); // 添加delete
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(getListWithPageXmlElement(introspectedTable)); // 添加分页
		parentElement.addElement(new TextElement(""));

		parentElement.addElement(countXmlElement(introspectedTable)); // 添加getcount
		parentElement.addElement(new TextElement(""));

		return true;
	}

	/**
	 * 生成自定义insert
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement insertXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("insert");
		element.addAttribute(new Attribute("id", "insert"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		element.addElement(new TextElement("insert into " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
		if (columns != null && columns.size() > 0) {
			// 添加要插入的字段
			XmlElement trimElement = new XmlElement("trim");
			trimElement.addAttribute(new Attribute("prefix", "("));
			trimElement.addAttribute(new Attribute("suffix", ")"));
			trimElement.addAttribute(new Attribute("suffixOverrides", ","));

			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).isIdentity()) {
					continue;
				}
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(
						new Attribute("test", columns.get(i).getActualColumnName().toLowerCase() + " != null"));
				if (i != columns.size() - 1)
					ifElement.addElement(new TextElement(columns.get(i).getActualColumnName() + ","));
				if (i == columns.size() - 1 || (i == columns.size() - 2 && columns.get(i + 1).isIdentity()))
					ifElement.addElement(new TextElement(columns.get(i).getActualColumnName() + ""));
				trimElement.addElement(ifElement);
			}
			element.addElement(trimElement);

			XmlElement trimValueElement = new XmlElement("trim");
			trimValueElement.addAttribute(new Attribute("prefix", "values("));
			trimValueElement.addAttribute(new Attribute("suffix", ")"));
			trimValueElement.addAttribute(new Attribute("suffixOverrides", ","));
			for (int i = 0; i < columns.size(); i++) {
				if (columns.get(i).isIdentity()) {
					continue;
				}
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(
						new Attribute("test", columns.get(i).getActualColumnName().toLowerCase() + " != null"));
				if (i != columns.size() - 1)
					ifElement.addElement(
							new TextElement("#{" + columns.get(i).getActualColumnName().toLowerCase() + "},"));
				if (i == columns.size() - 1 || (i == columns.size() - 2 && columns.get(i + 1).isIdentity()))
					ifElement.addElement(
							new TextElement("#{" + columns.get(i).getActualColumnName().toLowerCase() + "}"));
				trimValueElement.addElement(ifElement);
			}
			element.addElement(trimValueElement);
		}
		return element;
	}

	/**
	 * update模板
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement updateXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("update");
		element.addAttribute(new Attribute("id", "update"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		element.addElement(new TextElement("update " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		XmlElement setElement = new XmlElement("set");
		List<IntrospectedColumn> columns = introspectedTable.getBaseColumns();
		if (columns != null && columns.size() > 0) {
			// 添加要插入的字段
			for (int i = 0; i < columns.size(); i++) {
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(
						new Attribute("test", columns.get(i).getActualColumnName().toLowerCase() + " != null"));
				if (i != columns.size() - 1)
					ifElement.addElement(new TextElement(columns.get(i).getActualColumnName() + " = #{"
							+ columns.get(i).getActualColumnName().toLowerCase() + "},"));
				if (i == columns.size() - 1 || (i == columns.size() - 2 && columns.get(i + 1).isIdentity()))
					ifElement.addElement(new TextElement(columns.get(i).getActualColumnName() + " = #{"
							+ columns.get(i).getActualColumnName().toLowerCase() + "}"));
				setElement.addElement(ifElement);
			}
		}
		element.addElement(setElement);

		// 添加where
		element.addElement(new TextElement("where"));
		columns = introspectedTable.getPrimaryKeyColumns();
		if (columns != null && columns.size() > 0) {
			// 添加要插入的字段
			for (int i = 0; i < columns.size(); i++) {
				element.addElement(new TextElement(columns.get(i).getActualColumnName() + " = #{"
						+ columns.get(i).getActualColumnName().toLowerCase() + "}"));
			}
		}

		return element;
	}

	/**
	 * 生成自定义select
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement getListXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "getList"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		element.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
		if (columns != null && columns.size() > 0) {
			element.addElement(new TextElement("where 1=1"));
			for (IntrospectedColumn column : columns) {
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(new Attribute("test", column.getActualColumnName().toLowerCase() + " != null"));
				ifElement.addElement(new TextElement("and " + column.getActualColumnName() + " = #{"
						+ column.getActualColumnName().toLowerCase() + "}"));
				element.addElement(ifElement);
			}
		}

		return element;
	}

	/**
	 * 生成自定义getCount
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement countXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "count"));
		element.addAttribute(new Attribute("resultType", "java.lang.Long"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		element.addElement(
				new TextElement("select count(*) from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
		if (columns != null && columns.size() > 0) {
			element.addElement(new TextElement("where 1=1"));
			for (IntrospectedColumn column : columns) {
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(new Attribute("test", column.getActualColumnName().toLowerCase() + " != null"));
				ifElement.addElement(new TextElement("and " + column.getActualColumnName() + " = #{"
						+ column.getActualColumnName().toLowerCase() + "}"));
				element.addElement(ifElement);
			}
		}

		return element;
	}

	/**
	 * 生成自定义select
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement getListWithPageXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "getListWithPage"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
		element.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
		if (columns != null && columns.size() > 0) {
			element.addElement(new TextElement("where 1=1"));
			for (IntrospectedColumn column : columns) {
				XmlElement ifElement = new XmlElement("if");
				ifElement.addAttribute(new Attribute("test", column.getActualColumnName().toLowerCase() + " != null"));
				ifElement.addElement(new TextElement("and " + column.getActualColumnName() + " = #{"
						+ column.getActualColumnName().toLowerCase() + "}"));
				element.addElement(ifElement);
			}
		}

		return element;
	}

	/**
	 * 自定义getSingle
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement getSingleXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "getSingle"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getBaseRecordType()));
		element.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
		if (columns != null && columns.size() > 0) {
			IntrospectedColumn column = columns.get(0);
			element.addElement(new TextElement("where " + column.getActualColumnName() + " = #{id}"));
		}

		// 添加分页
		element.addElement(new TextElement("limit 0,1"));

		return element;
	}

	/**
	 * 自定义delete
	 * 
	 * @param introspectedTable
	 * @return
	 */
	private XmlElement batchDeleteXmlElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("delete");
		element.addAttribute(new Attribute("id", "batchDelete"));
		element.addElement(new TextElement("delete from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

		List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();
		if (columns != null && columns.size() > 0) {
			element.addElement(new TextElement("where " + columns.get(0).getActualColumnName() + " in("));
			XmlElement foreachElement = new XmlElement("foreach");
			foreachElement.addAttribute(new Attribute("item", "item"));
			foreachElement.addAttribute(new Attribute("collection", "list"));
			foreachElement.addAttribute(new Attribute("separator", ","));
			foreachElement.addElement(new TextElement("#{item}"));

			element.addElement(foreachElement);

			element.addElement(new TextElement(")"));
		}

		return element;
	}

	public boolean validate(List<String> arg0) {
		return true;
	}
}
