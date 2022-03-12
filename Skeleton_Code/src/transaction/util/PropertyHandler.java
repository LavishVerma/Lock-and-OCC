package transaction.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyHandler{
	
	
	public static Properties readProperties() {
		FileReader reader = null;
		Properties prop = null;
		try {
			reader = new FileReader("src/transaction/config/TransactionServerProperties.properties");
			 prop=new Properties();  
		    prop.load(reader); 
		} catch (IOException e) {
			System.out.println("Error while reading properties file  " + e.getMessage());
		}
		  
		return prop;
		
	}

}
