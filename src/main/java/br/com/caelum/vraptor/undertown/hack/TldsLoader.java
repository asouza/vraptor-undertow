package br.com.caelum.vraptor.undertown.hack;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.jasper.deploy.TagAttributeInfo;
import org.apache.jasper.deploy.TagInfo;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.apache.jasper.deploy.TagVariableInfo;
import org.jboss.annotation.javaee.Icon;
import org.jboss.metadata.javaee.spec.DescriptionGroupMetaData;
import org.jboss.metadata.parser.jsp.TldMetaDataParser;
import org.jboss.metadata.parser.util.NoopXMLResolver;
import org.jboss.metadata.web.spec.AttributeMetaData;
import org.jboss.metadata.web.spec.TagMetaData;
import org.jboss.metadata.web.spec.TldMetaData;
import org.jboss.metadata.web.spec.VariableMetaData;
import org.jboss.modules.ModuleLoadException;

import br.com.caelum.vraptor.undertown.builder.ServerBuilder;

public class TldsLoader {

	private static final String[] JSTL_TAGLIBS = { "c-1_0-rt.tld", "c-1_0.tld", "c.tld", "fmt-1_0-rt.tld",
			"fmt-1_0.tld", "fmt.tld", "fn.tld", "permittedTaglibs.tld", "scriptfree.tld", "sql-1_0-rt.tld",
			"sql-1_0.tld", "sql.tld", "x-1_0-rt.tld", "x-1_0.tld", "x.tld" };

	private static TldMetaData parseTLD(String tld, InputStream is) throws Exception {
		try {
			final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			inputFactory.setXMLResolver(NoopXMLResolver.create());
			XMLStreamReader xmlReader = inputFactory.createXMLStreamReader(is);
			return TldMetaDataParser.parse(xmlReader);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// Ignore
			}
		}
	}

	public static Map<String, TagLibraryInfo> load() {
		HashMap<String, TagLibraryInfo> tagLibraries = new HashMap<String, TagLibraryInfo>();
		try {
			for (String tld : JSTL_TAGLIBS) {
				InputStream is = ServerBuilder.class.getResourceAsStream("/META-INF/" + tld);
				if (is != null) {
					TldMetaData tldMetaData = parseTLD(tld, is);
					TagLibraryInfo tagLibraryInfo = new TagLibraryInfo();
					tagLibraryInfo.setTlibversion(tldMetaData.getTlibVersion());
					if (tldMetaData.getJspVersion() == null) {
						tagLibraryInfo.setJspversion(tldMetaData.getVersion());
					} else {
						tagLibraryInfo.setJspversion(tldMetaData.getJspVersion());
					}
					tagLibraryInfo.setShortname(tldMetaData.getShortName());

					List<TagMetaData> tagsMetaData = tldMetaData.getTags();
					for (TagMetaData tagMetaData : tagsMetaData) {
						tagLibraryInfo.addTagInfo(newTagInfoFrom(tagMetaData));
					}
					tagLibraries.put(tldMetaData.getUri(), tagLibraryInfo);
				}
			}
		} catch (ModuleLoadException e) {
			// Ignore
		} catch (Exception e) {
			// Ignore
		}
		return tagLibraries;

	}

	private static TagInfo newTagInfoFrom(TagMetaData tagMetaData) {
		TagInfo tagInfo = new TagInfo();
		tagInfo.setTagName(tagMetaData.getName());
		tagInfo.setTagClassName(tagMetaData.getTagClass());
		tagInfo.setTagExtraInfo(tagMetaData.getTeiClass());
		if (tagMetaData.getBodyContent() != null) {
			tagInfo.setBodyContent(tagMetaData.getBodyContent().toString());
		}
		tagInfo.setDynamicAttributes(tagMetaData.getDynamicAttributes());
		// Description group
		if (tagMetaData.getDescriptionGroup() != null) {
			DescriptionGroupMetaData descriptionGroup = tagMetaData.getDescriptionGroup();
			if (descriptionGroup.getIcons() != null && descriptionGroup.getIcons().value() != null
					&& (descriptionGroup.getIcons().value().length > 0)) {
				Icon icon = descriptionGroup.getIcons().value()[0];
				tagInfo.setLargeIcon(icon.largeIcon());
				tagInfo.setSmallIcon(icon.smallIcon());
			}
			tagInfo.setInfoString(descriptionGroup.getDescription());
			tagInfo.setDisplayName(descriptionGroup.getDisplayName());
		}
		// Variable
		if (tagMetaData.getVariables() != null) {
			for (VariableMetaData variableMetaData : tagMetaData.getVariables()) {
				TagVariableInfo tagVariableInfo = new TagVariableInfo();
				tagVariableInfo.setNameGiven(variableMetaData.getNameGiven());
				tagVariableInfo.setNameFromAttribute(variableMetaData.getNameFromAttribute());
				tagVariableInfo.setClassName(variableMetaData.getVariableClass());
				tagVariableInfo.setDeclare(variableMetaData.getDeclare());
				if (variableMetaData.getScope() != null) {
					tagVariableInfo.setScope(variableMetaData.getScope().toString());
				}
				tagInfo.addTagVariableInfo(tagVariableInfo);
			}
		}
		// Attribute
		if (tagMetaData.getAttributes() != null) {
			for (AttributeMetaData attributeMetaData : tagMetaData.getAttributes()) {
				TagAttributeInfo tagAttributeInfo = new TagAttributeInfo();
				tagAttributeInfo.setName(attributeMetaData.getName());
				tagAttributeInfo.setType(attributeMetaData.getType());
				tagAttributeInfo.setReqTime(attributeMetaData.getRtexprvalue());
				tagAttributeInfo.setRequired(attributeMetaData.getRequired());
				tagAttributeInfo.setFragment(attributeMetaData.getFragment());
				if (attributeMetaData.getDeferredValue() != null) {
					tagAttributeInfo.setDeferredValue("true");
					tagAttributeInfo.setExpectedTypeName(attributeMetaData.getDeferredValue().getType());
				} else {
					tagAttributeInfo.setDeferredValue("false");
				}
				if (attributeMetaData.getDeferredMethod() != null) {
					tagAttributeInfo.setDeferredMethod("true");
					tagAttributeInfo.setMethodSignature(attributeMetaData.getDeferredMethod().getMethodSignature());
				} else {
					tagAttributeInfo.setDeferredMethod("false");
				}
				tagInfo.addTagAttributeInfo(tagAttributeInfo);
			}
		}

		return tagInfo;
	}

}
