
package net.izel.framework.tool;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ResourceManager {

    private static Properties m_properties;
    private static String sFileName;

    private ResourceManager() {
    }

   public static void load(String sFileName) throws FileNotFoundException,IOException
   {
       ResourceManager.sFileName=sFileName;
       if( m_properties ==null) {
            m_properties = new Properties();
        }
        m_properties.load(new FileReader(sFileName));
   }

   public static String getValue(String sKey)
   {
      return m_properties.getProperty(sKey);
   }

  public static Properties getProperties()
  {
     return m_properties;
  }

  public static void refresh() throws FileNotFoundException, IOException
  {
      load(sFileName);
  }

}
