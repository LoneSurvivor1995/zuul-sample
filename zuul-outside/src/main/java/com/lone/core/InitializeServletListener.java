package com.lone.core;

import com.lone.config.AmqpConnection;
import com.lone.filter.post.SendResponseFilter;
import com.lone.filter.pre.SecurityFilter;
import com.lone.filter.route.WriteMqFilter;
import com.lone.java.com.netflix.zuul.FilterFileManager;
import com.lone.java.com.netflix.zuul.FilterLoader;
import com.lone.java.com.netflix.zuul.filters.FilterRegistry;
import com.lone.java.com.netflix.zuul.groovy.GroovyCompiler;
import com.lone.java.com.netflix.zuul.groovy.GroovyFileFilter;
import com.lone.java.com.netflix.zuul.monitoring.CounterFactory;
import com.lone.java.com.netflix.zuul.monitoring.TracerFactory;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.servo.util.ThreadCpuStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class InitializeServletListener implements ServletContextListener {

	private Logger LOGGER = LoggerFactory.getLogger(InitializeServletListener.class);
	private String appName = null;
	private FilterRegistry filterRegistry = FilterRegistry.instance();

//	private LogConfigurator logConfigurator;
//	private InfoBoard internalsServer;

	public InitializeServletListener() {
//		System.setProperty(Constants.DEPLOY_ENVIRONMENT, "test");
//		System.setProperty(Constants.DEPLOYMENT_APPLICATION_ID, "mobile_zuul");
//		System.setProperty(Constants.DEPLOY_CONFIG_URL, "http://localhost:8080/configfiles/mobile_zuul/default/application");
//		String applicationID = ConfigurationManager.getConfigInstance().getString(Constants.DEPLOYMENT_APPLICATION_ID);
//		if (StringUtils.isEmpty(applicationID)) {
//			LOGGER.warn("Using default config!");
//			ConfigurationManager.getConfigInstance().setProperty(Constants.DEPLOYMENT_APPLICATION_ID, "mobile_zuul");
//		}
//
//		System.setProperty(DynamicPropertyFactory.ENABLE_JMX, "true");
//
        loadConfiguration();
//        configLog();
//        registerEureka();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        try {
//        	initInfoBoard();
			initAmqp();
            initMonitor();
            initZuul();
//            updateInstanceStatusToEureka();
        } catch (Exception e) {
        	LOGGER.error("Error while initializing zuul gateway.", e);
        	throw new RuntimeException(e);
        }
	}

	private void updateInstanceStatusToEureka() {
		DynamicBooleanProperty eurekaEnabled = DynamicPropertyFactory.getInstance().getBooleanProperty("eureka.enabled", true);
		if (!eurekaEnabled.get()) return;
//        ApplicationInfoManager.getInstance().setInstanceStatus(InstanceInfo.InstanceStatus.UP);
	}

//	private void initInfoBoard() {
//    	internalsServer = new InfoBoard(appName, ConfigurationManager.getConfigInstance().getInt("server.internals.port", 8077));
//        internalsServer.start();
//	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		FilterFileManager.shutdown();
	}

	private void initAmqp() throws IOException, TimeoutException {
		AmqpConnection.init();
		AmqpConnection.initReceive("out_test");
	}

	private void initMonitor() {
//		LOGGER.info("Registering Servo Monitor");
//		MonitorRegistry.getInstance().setPublisher(new ServoMonitor());
//
//		LOGGER.info("Starting Poller");
//		MetricPoller.startPoller();

		LOGGER.info("Registering Servo TracerImpl");
		TracerFactory.initialize(new TracerImpl());

		LOGGER.info("Registering Servo Counter");
		CounterFactory.initialize(new Counter());

		LOGGER.info("Starting CPU stats");
		final ThreadCpuStats stats = ThreadCpuStats.getInstance();
		stats.start();

	}
	
    private void initZuul() throws Exception {
        LOGGER.info("Starting Groovy Filter file manager");
//        final AbstractConfiguration config = ConfigurationManager.getConfigInstance();
//        final String preFiltersPath = config.getString(Constants.ZUUL_FILTER_PRE_PATH);
//        final String postFiltersPath = config.getString(Constants.ZUUL_FILTER_POST_PATH);
//        final String routeFiltersPath = config.getString(Constants.ZUUL_FILTER_ROUTE_PATH);
//        final String errorFiltersPath = config.getString(Constants.ZUUL_FILTER_ERROR_PATH);
//        final String customPath = config.getString(Constants.Zuul_FILTER_CUSTOM_PATH);

		// TODO path动态配置
		final String preFiltersPath = "E:\\temp\\pre";
		final String postFiltersPath = "E:\\temp\\post";
		final String routeFiltersPath = "E:\\temp\\route";
		final String errorFiltersPath = "E:\\temp\\error";

        //load local filter files
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
		FilterFileManager.init(5, preFiltersPath, postFiltersPath, routeFiltersPath, errorFiltersPath);
//        if (customPath == null) {
//            FilterFileManager.init(5, preFiltersPath, postFiltersPath, routeFiltersPath, errorFiltersPath);
//        } else {
//            FilterFileManager.init(5, preFiltersPath, postFiltersPath, routeFiltersPath, errorFiltersPath, customPath);
//        }
        //load filters in DB
//        startZuulFilterPoller();
        LOGGER.info("Groovy Filter file manager started");

		filterRegistry.put(SecurityFilter.class.getName(),new SecurityFilter());
		filterRegistry.put(SendResponseFilter.class.getName(),new SendResponseFilter());
		filterRegistry.put(WriteMqFilter.class.getName(),new WriteMqFilter());

		LOGGER.info("Java Filter file manager started");

    }

//    private void startZuulFilterPoller() {
//        ZuulFilterPoller.start();
//        LOGGER.info("ZuulFilterPoller Started.");
//    }
    
	private void loadConfiguration() {
		appName = ConfigurationManager.getDeploymentContext().getApplicationId();

		// Loading properties via archaius.
		if (null != appName) {
			try {
				LOGGER.info(String.format("Loading application properties with app id: %s and environment: %s", appName,
						ConfigurationManager.getDeploymentContext().getDeploymentEnvironment()));
				ConfigurationManager.loadCascadedPropertiesFromResources(appName);
			} catch (IOException e) {
				LOGGER.error(String.format(
						"Failed to load properties for application id: %s and environment: %s. This is ok, if you do not have application level properties.",
						appName, ConfigurationManager.getDeploymentContext().getDeploymentEnvironment()), e);
			}
		} else {
			LOGGER.warn(
					"Application identifier not defined, skipping application level properties loading. You must set a property 'archaius.deployment.applicationId' to be able to load application level properties.");
		}

	}

	private void configLog() {
//		logConfigurator = new LogConfigurator(appName, ConfigurationManager.getDeploymentContext().getDeploymentEnvironment());
//		logConfigurator.config();
	}

//	private void registerEureka() {
//		DynamicBooleanProperty eurekaEnabled = DynamicPropertyFactory.getInstance().getBooleanProperty("eureka.enabled",
//				true);
//		if (!eurekaEnabled.get())
//			return;
//
//		EurekaInstanceConfig eurekaInstanceConfig = new PropertiesInstanceConfig() {
//		};
//        ConfigurationManager.getConfigInstance().setProperty("eureka.statusPageUrl","http://"+ getTurbineInstance());
//
//		DiscoveryManager.getInstance().initComponent(eurekaInstanceConfig, new DefaultEurekaClientConfig());
//
//		final DynamicStringProperty serverStatus = DynamicPropertyFactory.getInstance()
//				.getStringProperty("server." + IPUtil.getLocalIP() + ".status", "up");
//		DiscoveryManager.getInstance().getDiscoveryClient().registerHealthCheckCallback(new HealthCheckCallback() {
//			@Override
//			public boolean isHealthy() {
//				return serverStatus.get().toLowerCase().equals("up");
//			}
//		});
//
//		String version = String.valueOf(System.currentTimeMillis());
//		String group = ConfigurationManager.getConfigInstance().getString("server.group", "default");
//		String dataCenter = ConfigurationManager.getConfigInstance().getString("server.data-center", "default");
//
//		Map<String, String> metadata = new HashMap<String, String>();
//		metadata.put("version", version);
//		metadata.put("group", group);
//		metadata.put("dataCenter", dataCenter);
//
//		String turbineInstance = getTurbineInstance();
//		if (turbineInstance != null) {
//			metadata.put("turbine.instance", turbineInstance);
//		}
//
//		ApplicationInfoManager.getInstance().registerAppMetadata(metadata);
//	}

//	public String getTurbineInstance() {
//		String instance = null;
//		String ip = IPUtil.getLocalIP();
//		if (ip != null) {
//			instance = ip + ":" + ConfigurationManager.getConfigInstance().getString("server.internals.port", "8077");
//		} else {
//			LOGGER.warn("Can't build turbine instance as can't fetch the ip.");
//		}
//		return instance;
//	}
}

