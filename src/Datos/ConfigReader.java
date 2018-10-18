package Datos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author jp
 */
public class ConfigReader {
    
     public LinkedList<String> readConfig(String filePath) {
        
        LinkedList<String> providers = new LinkedList<>();
        try { 
            FileReader reader = new FileReader(filePath); 
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader); 
            JSONArray lang= (JSONArray) jsonObject.get("Proveedores"); 
            for(int i=0; i<lang.size(); i++){
            }
            
            Iterator iterator = lang.iterator(); 
            while (iterator.hasNext()) {
                JSONObject innerObj = (JSONObject) iterator.next();
                providers.add((String) innerObj.get("Proveedor"));
            }
            } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
            } catch (IOException ex) {
                    ex.printStackTrace();
            } catch (NullPointerException ex) {
                    ex.printStackTrace();
            } catch (org.json.simple.parser.ParseException ex) { 
                Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        return providers;
    }
     
}
