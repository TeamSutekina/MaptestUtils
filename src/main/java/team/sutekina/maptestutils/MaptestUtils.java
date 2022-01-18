package team.sutekina.maptestutils;
import java.util.Dictionary;
import java.util.Hashtable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MVWorld;
import com.onarandombox.MultiverseCore.MultiverseCore;


public class MaptestUtils extends JavaPlugin {
	public MultiverseCore mv;
	 
    @Override
    public void onEnable() {
    	 mv = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
    	 OngoingTests = new Hashtable<String, String>();
    	 //this.getCommand("starttest").setExecutor(new onCommand());
    }
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    
    public Hashtable<String, String> OngoingTests;
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {    	
    	if (command.getName().equalsIgnoreCase("endtest"))
    	{
    		if(args.length == 1)
    		{
    			mv = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
    			String refPlayerName = args[0];
    			Player refPlayer = Bukkit.getPlayer(refPlayerName);
    			String mapname = OngoingTests.get(refPlayerName);
    			
    			if(mapname == null)
    			{
    				sender.sendMessage(refPlayerName + " is not in a test.");
    				return false;
    			}
    			
    			//World world = mv.getServer().getWorld(mapname);
    			mv.deleteWorld(mapname);
    			sender.sendMessage("ended test!");
    			OngoingTests.remove(refPlayerName);
    			
    			return true;
    		}
    		else 
    		{
    			sender.sendMessage("USAGE: endtest <player>");
    			return false;
    		}
    	}
    	
    	if (command.getName().equalsIgnoreCase("starttest"))
    	{
    		if(args.length == 2)
    		{
    			mv = (MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core");
    			String refPlayerName = args[0];
    			Player refPlayer = Bukkit.getPlayer(refPlayerName);
    			String mapName = args[1];
    			if(refPlayer == null)
    			{
    				sender.sendMessage(refPlayerName + " is not online.");
    				return false;
    			}
    			if(OngoingTests.get(refPlayerName) != null)
    			{
    				sender.sendMessage(refPlayerName + " is already in a test. Use /endtest " + refPlayerName + " to end the test. They are in world " + OngoingTests.get(refPlayerName));
    				return false;
    			}
    			String tempmapname = mapName + "_maptest_" + refPlayerName;
    			mv.cloneWorld(mapName, tempmapname, "voiden");
    			
    			try {
    				World world = mv.getServer().getWorld(tempmapname);
    				sender.sendMessage("Cloned to " + tempmapname + ". sent player!");
    				Location spawnpoint = world.getSpawnLocation();
    				Location loc = new Location(world, spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
    				refPlayer.teleport(loc);
    				// TODO: clear inventory when test starts
    				OngoingTests.put(refPlayerName, tempmapname);
    			} catch(Exception e) {
    				sender.sendMessage(e.getMessage());
    				sender.sendMessage("Could not clone world. Most likely reason is that it may not exist.");
    			}
    			
    			return true;
    		}
    		else {
    			sender.sendMessage("USAGE: starttest <player> <map>");
    			return false;
    		}
    	}
    	
        return false;
    }
}
