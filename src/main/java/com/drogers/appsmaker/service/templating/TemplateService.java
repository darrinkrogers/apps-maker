package com.drogers.appsmaker.service.templating;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
	
	VelocityEngine velocityEngine = new VelocityEngine();
	
	public TemplateService() {

		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init(props);
	}
	
	public String makeContent(String templateName, Map<String, Object> contextVariableMap) {

		Template template = velocityEngine.getTemplate(templateName);
		
		VelocityContext velocityContext = new VelocityContext();
		for(String key: contextVariableMap.keySet()) {
			velocityContext.put(key, contextVariableMap.get(key));
		}
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		
		return stringWriter.toString();
	}
}
