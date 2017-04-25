package com.bcv.resources;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
public class Resources {

	   @Produces
	   @Resource(mappedName="java:/jdbc/rheiDSPool")
	   private DataSource dataSource;
}
