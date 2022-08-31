package com.alet.common.util.text.translation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;

public class ManualTranslator {
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    private static final Map<String, String> languageList = Maps.<String, String>newHashMap();
    
    public static String translateToLocal(String page) {
        String lang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        InputStream stream = Minecraft.getMinecraft().getClass().getClassLoader().getResourceAsStream("assets/alet/lang/manual/" + page.toLowerCase() + "." + lang + ".lang");
        
        ManualTranslator.inject(stream);
        
        return stream == null ? "" : ManualTranslator.tryTranslateKey("manual.page");
    }
    
    public static void inject(InputStream inputstream) {
        try {
            Map<String, String> map = parseLangFile(inputstream);
            languageList.putAll(map);
        } finally {
            IOUtils.closeQuietly(inputstream); // Forge: close stream after use (MC-153470)
        }
    }
    
    public static Map<String, String> parseLangFile(InputStream inputstream) {
        Map<String, String> table = Maps.newHashMap();
        try {
            inputstream = net.minecraftforge.fml.common.FMLCommonHandler.instance().loadLanguage(table, inputstream);
            if (inputstream == null)
                return table;
            
            for (String s : IOUtils.readLines(inputstream, StandardCharsets.UTF_8)) {
                if (!s.isEmpty() && s.charAt(0) != '#') {
                    String[] astring = (String[]) Iterables.toArray(EQUAL_SIGN_SPLITTER.split(s), String.class);
                    
                    if (astring != null && astring.length == 2) {
                        String s1 = astring[0];
                        String s2 = NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");
                        table.put(s1, s2);
                    }
                }
            }
            
        } catch (IOException var7) {
            ;
        } catch (Exception ex) {}
        return table;
    }
    
    public static String tryTranslateKey(String key) {
        String s = languageList.get(key);
        return s == null ? key : s;
    }
    
}
