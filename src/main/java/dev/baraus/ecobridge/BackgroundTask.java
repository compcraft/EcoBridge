package dev.baraus.ecobridge;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BackgroundTask {
	
	private Ecobridge m;
	private long lastSave = System.currentTimeMillis();
	
	public BackgroundTask(Ecobridge m) {
		this.m = m;
		runTask();
	}
	
	private void runTask() {
		if (m.getConfigHandler().getBoolean("General.saveDataTask.enabled") == true) {
			Ecobridge.log.info("Data save task is enabled.");
		} else {
			Ecobridge.log.info("Data save task is disabled.");
		}
		Bukkit.getScheduler().runTaskTimerAsynchronously(m, new Runnable() {

			@Override
			public void run() {
				m.getEcoDataHandler().updateBalanceMap();
				runSaveData();
			}
			
		}, 20L, 20L);
	}
	
	private void runSaveData() {
		if (m.getConfigHandler().getBoolean("General.saveDataTask.enabled") == true) {
			if (Bukkit.getOnlinePlayers().isEmpty() == false) {
				if (System.currentTimeMillis() - lastSave >= m.getConfigHandler().getInteger("General.saveDataTask.interval") * 60 * 1000) {
					List<Player> onlinePlayers = new ArrayList<Player>(Bukkit.getOnlinePlayers());
					lastSave = System.currentTimeMillis();
					if (m.getConfigHandler().getBoolean("General.saveDataTask.hideLogMessages") == false) {
						Ecobridge.log.info("Saving online players data...");
					}
					for (Player p : onlinePlayers) {
						if (p.isOnline() == true) {
							m.getEcoDataHandler().onDataSaveFunction(p, false, "false", false);
						}
					}
					if (m.getConfigHandler().getBoolean("General.saveDataTask.hideLogMessages") == false) {
						Ecobridge.log.info("Data save complete for " + onlinePlayers.size() + " players.");
					}
					onlinePlayers.clear();
				}
			}
		}
	}

}
