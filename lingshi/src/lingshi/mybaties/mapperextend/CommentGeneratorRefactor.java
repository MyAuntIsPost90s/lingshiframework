package lingshi.mybaties.mapperextend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class CommentGeneratorRefactor implements CommentGenerator {
	/*
	 * 父类时间
	 */
	private Boolean suppressDate;

	/**
	 * 父类所有注释
	 */
	private Boolean suppressAllComments;

	/**
	 * 当前时间
	 */
	private String currentDateStr;

	public CommentGeneratorRefactor() {
		super();
		suppressDate = null;
		suppressAllComments = null;
	}

	/*
	 * 全局配置方法，在所有方法调用前都会调用
	 */
	@Override
	public void addConfigurationProperties(Properties properties) {
		this.suppressDate = new Boolean(properties.getProperty("suppressDate"));
		this.suppressAllComments = new Boolean(properties.getProperty("suppressAllComments"));
		if (suppressDate) {
			currentDateStr = "";
		} else {
			currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
		}
	}

	/**
	 * Java类的类注释
	 */
	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		if (!suppressAllComments) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		innerClass.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
		innerClass.addJavaDocLine(" */");
	}

	/**
	 * 为类添加注释
	 */
	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		if (!suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		innerClass.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerClass.addJavaDocLine(sb.toString().replace("\n", " "));
		sb.setLength(0);
		sb.append(" ");
		sb.append(currentDateStr);
		innerClass.addJavaDocLine(" */");
	}

	/**
	 * Mybatis的Mapper.xml文件里面的注释
	 */
	@Override
	public void addComment(XmlElement xmlElement) {

	}

	/**
	 * 为枚举添加注释
	 */
	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		if (!suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		innerEnum.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		innerEnum.addJavaDocLine(sb.toString().replace("\n", " "));
		innerEnum.addJavaDocLine(" */");
	}

	/**
	 * Java属性注释
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		if (!suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedTable.getFullyQualifiedTable());
		field.addJavaDocLine(sb.toString().replace("\n", " "));
		field.addJavaDocLine(" */");

	}

	/**
	 * 为字段添加注释
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		if (!suppressAllComments) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		field.addJavaDocLine("/**");
		sb.append(" * ");
		sb.append(introspectedColumn.getRemarks());
		field.addJavaDocLine(sb.toString().replace("\n", " "));
		field.addJavaDocLine(" */");
	}

	/**
	 * 普通方法的注释，这里主要是XXXMapper.java里面的接口方法的注释
	 */
	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {

	}

	/**
	 * 给getter方法加注释
	 */
	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
	}

	/**
	 * 给Java文件加注释，这个注释是在文件的顶部，也就是package上面。
	 */
	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {

	}

	/**
	 * 为调用此方法作为根元素的第一个子节点添加注释。
	 */
	@Override
	public void addRootComment(XmlElement arg0) {

	}

	/**
	 * 给setter方法加注释
	 */
	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
	}

}