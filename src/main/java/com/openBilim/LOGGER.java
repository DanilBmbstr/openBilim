package com.openBilim;

import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.openBilim.Users.Authorization.JWT_Util;

public class LOGGER {
    static Logger Log;
    static
    {
        try(FileInputStream ins = new FileInputStream("src/main/java/com/openBilim/LoggerConfig")){ 
            LogManager.getLogManager().readConfiguration(ins);
            Log = Logger.getLogger("Main");
        }catch (Exception ignore){
            ignore.printStackTrace();
        }


        
    }

    public static void info(String info)
    {
        Log.info(info);
    }
    public static void warning(String warning)
    {
        Log.warning(warning);
    }
}
