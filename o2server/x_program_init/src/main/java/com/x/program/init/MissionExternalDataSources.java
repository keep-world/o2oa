package com.x.program.init;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.apache.commons.lang3.BooleanUtils;

import com.google.gson.Gson;
import com.x.base.core.project.config.Config;
import com.x.base.core.project.config.ExternalDataSources;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.gson.XGsonBuilder;
import com.x.base.core.project.tools.BaseTools;
import com.x.base.core.project.tools.StringTools;
import com.x.program.init.Missions.Messages;
import com.x.program.init.Missions.Mission;

public class MissionExternalDataSources implements Mission {

	public static final String TYPE_MYSQL = "mysql";
	public static final String TYPE_POSTGRESQL = "postgresql";
	public static final String TYPE_ORACLE = "oracle";
	public static final String TYPE_SQLSERVER = "sqlserver";
	public static final String TYPE_DB2 = "db2";
	public static final String TYPE_H2 = "h2";
	public static final String TYPE_DM = "dm";
	public static final String TYPE_KINGBASE = "kingbase";
	public static final String TYPE_KINGBASE8 = "kingbase8";
	public static final String TYPE_GBASE = "gbase";
	public static final String TYPE_GBASEMYSQL = "gbasemysql";
	public static final String TYPE_OSCAR = "oscar";
	public static final String TYPE_INFORMIX = "informix";

	private ExternalDataSources externalDataSources;

	public ExternalDataSources getExternalDataSources() {
		return externalDataSources;
	}

	public void setExternalDataSources(ExternalDataSources externalDataSources) {
		this.externalDataSources = externalDataSources;
	}

	@Override
	public void execute(Missions.Messages messages) {
		messages.head(MissionExternalDataSources.class.getSimpleName());
		Gson gson = XGsonBuilder.instance();
		try {
			messages.msg("executing");
			Path path = Config.path_local_temp(true).resolve("externalDataSources.json");
			Files.writeString(path, gson.toJson(getExternalDataSources()), StandardCharsets.UTF_8,
					StandardOpenOption.TRUNCATE_EXISTING);
			Config.resource_commandQueue().add("ctl -initResourceFactory");
			messages.msg("success");
		} catch (Exception e) {
			messages.msg("failure");
			throw new ExceptionMissionExecute(e);
		}
	}

	public static CheckResult check() throws Exception {
		CheckResult checkResult = new CheckResult();
		ExternalDataSources obj = BaseTools.readConfigObject(Config.PATH_CONFIG_EXTERNALDATASOURCES,
				ExternalDataSources.class);
		if ((null != obj) && BooleanUtils.isTrue(obj.enable())) {
			checkResult.setExternalDataSources(obj);
			checkResult.setConfigured(true);
		} else {
			checkResult.setConfigured(false);
		}
		return checkResult;
	}

	public static class CheckResult extends GsonPropertyObject {

		private static final long serialVersionUID = -4544008653960661989L;

		private Boolean configured;

		private ExternalDataSources externalDataSources;

		public Boolean getConfigured() {
			return configured;
		}

		public void setConfigured(Boolean configured) {
			this.configured = configured;
		}

		public ExternalDataSources getExternalDataSources() {
			return externalDataSources;
		}

		public void setExternalDataSources(ExternalDataSources externalDataSources) {
			this.externalDataSources = externalDataSources;
		}

	}

}