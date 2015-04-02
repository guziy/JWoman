package main;

import main.db.DataBase;

import java.io.File;

public class Configuration {

	
	//Singleton holding application properties
	
	private static Configuration self = null;
	
	private String userDir;
	private DataBase db;
	
	private static String fs = System.getProperty("file.separator");
	
	private static final int defaultNumPeriodsToShow = 30;
	
	
	//Period labels:
	public static final String UPCOMING = "Upcoming";
	public static final String PAST = "Past";
	public static final String CURRENT = "Current";
	
	
	
	
	
	private Configuration(){
		this.userDir = System.getProperty("user.home");
		
		//create the application folder if required
		String appFolderPath = this.userDir + fs + ".jwooman";
		File appFolder = new File(appFolderPath);
		if (!appFolder.exists()){
			appFolder.mkdir();
		}
		
		db = new DataBase(appFolderPath + fs + "period_data.db");
	}
	
	public static Configuration getConfiguration() {
		if (self == null) self = new Configuration();	
		return self;
	}
	
	
	public DataBase getDb(){
		return db;
	}
	
	
	private String getUserDir(){
		return this.userDir;
	}
	
	public int getDefaultNumberOfPeriodsToShow(){
		return defaultNumPeriodsToShow;
	}
	
}
