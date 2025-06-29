package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileManager {
	private static Gson gson = new Gson();
	
	private static final File rootDir = new File("client");
    private static File modsDir = new File(rootDir, "mods");
    
    public static void init() {    	
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		
		if (!modsDir.exists()) {
			modsDir.mkdirs();
		}		
	}
    
    public static Gson getGson() {
    	return gson;
    }
    
    public static File getModsDirectory() {
    	return modsDir;
    }

    public static boolean writeJsonToFile(File file, Object obj) {
        try {
        	if (!file.exists()) {
        		file.createNewFile();
        	}
        	
        	FileOutputStream fileOutputStream = new FileOutputStream(file);
        	
        	fileOutputStream.write(gson.toJson(obj).getBytes());
        	fileOutputStream.flush();
        	fileOutputStream.close();
        	
        	return true;
        } catch (IOException e) {
            e.printStackTrace();
            
            return false;
        }
    }

    public static <T extends Object> T readFromJson(File file, Class<T> c) {
    	try {
        	FileInputStream fileInputStream = new FileInputStream(file);
        	InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        	BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        	
        	StringBuilder stringBuilder = new StringBuilder();
        	String line;
        	
        	while((line = bufferedReader.readLine()) != null) {
        		stringBuilder.append(line);
        	}
        	
        	bufferedReader.close();
        	inputStreamReader.close();
        	fileInputStream.close();
        	
        	return gson.fromJson(stringBuilder.toString(), c);
        } catch (IOException e) {
            e.printStackTrace();
            
            return null;
        }
    }
}
