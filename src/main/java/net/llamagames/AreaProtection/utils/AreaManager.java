/*
    AreaProtection:

    Copyright (C) 2019 SchdowNVIDIA
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Thanks to ZAP-Hosting.com and JetBrains!

    ZAP-Hosting.com gave me a Server for testing all plugins.
    If you're interested in a cheap VPS or strong Rootserver follow the links below:
    VPS: https://zap-hosting.com/schdowvserver
    Rootserver: https://zap-hosting.com/schdowroot
    Code (10% Discount Lifetime): schdow-10
 */

package net.llamagames.AreaProtection.utils;

import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import net.llamagames.AreaProtection.AreaProtection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AreaManager {

    public static Area createArea(String name, Vector3 pos1, Vector3 pos2, Level world) {

        Config config = AreaProtection.areaDB;

        config.set(name + ".world", world.getName());
        config.set(name + ".pos1x", pos1.x);
        config.set(name + ".pos1y", pos1.y);
        config.set(name + ".pos1z", pos1.z);
        config.set(name + ".pos2x", pos2.x);
        config.set(name + ".pos2y", pos2.y);
        config.set(name + ".pos2z", pos2.z);

        HashMap<String, AreaFlag> areaFlags = new HashMap<>();
        List<String> flags = new ArrayList<>();
        AreaProtection.flags.forEach((s -> {
            flags.add(s + ":" + "false");
            areaFlags.put(s, new AreaFlag(s, false));
        }));
        config.set(name + ".flags", flags);

        Area area = new Area(name, pos1, pos2, world.getName(), areaFlags);

        AreaProtection.areas.add(area);

        config.save();

        return area;
    }

    public static void saveAreaAsync(Area area) {
        CompletableFuture.runAsync(() -> {
            try {
                saveArea(area);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void saveArea(Area area) {
        AreaProtection.areaDB.set(area.getName() + ".world", area.getWorld());
        AreaProtection.areaDB.set(area.getName() + ".pos1x", area.getPos1().getX());
        AreaProtection.areaDB.set(area.getName() + ".pos1y", area.getPos1().getY());
        AreaProtection.areaDB.set(area.getName() + ".pos1z", area.getPos1().getZ());
        AreaProtection.areaDB.set(area.getName() + ".pos2x", area.getPos2().getX());
        AreaProtection.areaDB.set(area.getName() + ".pos2y", area.getPos2().getY());
        AreaProtection.areaDB.set(area.getName() + ".pos2z", area.getPos2().getZ());

        AreaProtection.areaDB.set(area.getName() + ".flags", area.flagsAsStringList());
        AreaProtection.areaDB.save();
    }

    public static void deleteArea(String name) {
        Area area = AreaProtection.instance.getAreaByName(name);
        if (area != null) {
            AreaProtection.areas.remove(area);
            AreaProtection.areaDB.remove(area.getName());
            AreaProtection.areaDB.save();
        }
    }

}
