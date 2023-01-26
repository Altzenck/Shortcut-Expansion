/*
 * Copyright 2020 Andre601
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.andre601.shortcut; 
import com.andre601.shortcut.logger.LegacyLogger;
import com.andre601.shortcut.logger.LoggerUtil;
import com.andre601.shortcut.logger.NativeLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;
import javax.annotation.Nonnull;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.NMSVersion;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
 
//Modified by Altzenck

 public class Shortcut
   extends PlaceholderExpansion
 {
   private final File folder = new File(PlaceholderAPIPlugin.getInstance().getDataFolder() + "/shortcuts/");
   public Shortcut() {
     LoggerUtil logger = loadLogger();
     
     if (this.folder.mkdirs()) {
       logger.info("Created shortcuts folder.");
     }
     new HashMap<>();
   }
   
   @Nonnull
   public String getIdentifier() {
     return "shortcut";
   }
   
   @Nonnull
   public String getAuthor() {
     return "Andre_601, Whitebrim";
   }
   
   @Nonnull
   public String getVersion() {
     return "1.2.0";
   }
 
   
   public String onRequest(OfflinePlayer player, @Nonnull String params) {
     String rawText, values[] = params.split(":");
     if (values.length <= 0) {
       return null;
     }
     values[0] = PlaceholderAPI.setBracketPlaceholders(player, values[0]);
     String filename = values[0].toLowerCase();
 
     
       File file = new File(this.folder, filename + ".txt");
       if (!file.exists())
         return null; 
       
       try { BufferedReader reader = Files.newBufferedReader(file.toPath()); 
         try { StringJoiner joiner = new StringJoiner("\n");
           
           String line;
           while ((line = reader.readLine()) != null) {
             joiner.add(line);
           }
           reader.close();
           rawText = joiner.toString();
           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
       { rawText = null; }
 
       
       if (rawText == null || rawText.isEmpty()) {
         return null;
       }
     
     if (values.length > 1) {
       MessageFormat format = new MessageFormat(rawText.replace("'", "''"));
       rawText = format.format(Arrays.copyOfRange(values, 1, values.length));
     } 
     
     return PlaceholderAPI.setPlaceholders(player, rawText);
   }
   
   private LoggerUtil loadLogger() {
     if (NMSVersion.getVersion("v1_18_R1") != NMSVersion.UNKNOWN) {
       return (LoggerUtil)new NativeLogger(this);
     }
     return (LoggerUtil)new LegacyLogger();
   }
 }
