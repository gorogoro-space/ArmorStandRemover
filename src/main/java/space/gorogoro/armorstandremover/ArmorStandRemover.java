package space.gorogoro.armorstandremover;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * ServerInformation
 * @license    LGPv3
 * @copyright  Copyright gorogoro.space 2021
 * @author     kubotan
 * @see        <a href="https://gorogoro.space">Gorogoro Server.</a>
 */
public class ArmorStandRemover extends JavaPlugin implements Listener {

  /**
   * JavaPlugin method onEnable.
   */
  @Override
  public void onEnable() {
    try {
      getLogger().info("The Plugin Has Been Enabled!");

      // If there is no setting file, it is created
      if(!getDataFolder().exists()){
        getDataFolder().mkdir();
      }

      File configFile = new File(getDataFolder(), "config.yml");
      if(!configFile.exists()){
        saveDefaultConfig();
      }
      int intervalSeconds = getConfig().getInt("interval-seconds");

      getServer().getPluginManager().registerEvents(this, this);

      getServer().getScheduler().runTaskTimer(this, new Runnable() {
        public void run() {
          for (World world : getServer().getWorlds()) {
            for (Chunk c : world.getLoadedChunks()) {
              for (Entity e : c.getEntities()) {

                if(!e.getType().equals(EntityType.ARMOR_STAND)) {
                  continue;
                }
                ArmorStand a = (ArmorStand)e;

                if(!a.isInvisible()) {
                  continue;
                }

                if(!a.getPassengers().isEmpty()) {
                  continue;
                }

                getServer().getLogger().info(
                  "Removed. world: " + a.getLocation().getWorld().getName()
                  + " xyz: " + a.getLocation().getBlockX() 
                  + " " + a.getLocation().getBlockY()
                  + " " + a.getLocation().getBlockZ()
                );
                a.remove();

              }
            }
          }
        }
      }, 0L, intervalSeconds * 20L);
    } catch (Exception e) {
      logStackTrace(e);
    }
  }

  /**
   * JavaPlugin method onDisable.
   */
  @Override
  public void onDisable() {
    try {
      getLogger().info("The Plugin Has Been Disabled!");
    } catch (Exception e) {
      logStackTrace(e);
    }
  }

  /**
   * Output stack trace to log file.
   * @param Exception Exception
   */
  private void logStackTrace(Exception e){
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      pw.flush();
      getLogger().warning(sw.toString());
  }
}
