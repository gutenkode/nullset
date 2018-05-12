package nullset.entities;

import nullset.battle.Enemy;
import nullset.entities.components.behaviors.*;
import nullset.entities.components.colliders.*;
import nullset.entities.components.renderers.*;
import nullset.main.LazyList;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.mesh.MeshMap;
import nullset.room.Room;
import nullset.room.TerrainData;
import nullset.rpg.*;

import java.util.ArrayList;
import java.util.List;

public class EntityFactory {

    public static Entity createPlayer(float x, float y, TerrainData data, Room room) {
        return deserializeEntity("PlayerSpawn,"+x+","+y, data, room);
    }

    public static LazyList<Entity> deserializeEntities(List<String> list, TerrainData data, Room room) {
        LazyList<Entity> entities = new LazyList<>();
        entities.add(deserializeTerrain(data,room));
        for (String s : list) {
            Entity e = deserializeEntity(s, data, room);
            if (e != null)
                entities.add(e);
        }
        return entities;
    }

    private static Entity deserializeTerrain(TerrainData data, Room room) {
        return new Entity(room,
                new EmptyBehavior(),
                new TerrainCollider(data),
                new TerrainRenderer(data, TextureMap.get(data.tileset))
        );
    }

    public static List<String> serializeEntities(List<Entity> list) {
        List<String> entities = new ArrayList<>();
        for (Entity e : list)
            entities.add(serializeEntity(e));
        return entities;
    }

    private static Entity deserializeEntity(String s, TerrainData data, Room room) {
        if (s.startsWith("#") || s.trim().isEmpty())
            return null; // ignore comment lines
        Entity e;
        String[] tokens = s.split(",");
        switch (tokens[0]) // this is the beefy one...
        {
            case "PlayerSpawn": // x,z
                float fx = Float.parseFloat(tokens[1]);
                float fz = Float.parseFloat(tokens[2]);
                e = new Entity(room,
                        new PlayerBehavior(),
                        new BoxCollider(),
                        new SpriteRenderer(TextureMap.get("entity_player"),12,5));
                e.getPos().set(fx,data.heightAt(fx,fz),fz);
                e.getSize().set(.6f,.9f,.3f);
                return e;

            case "Terminal": // x,z
                int x = Integer.parseInt(tokens[1]);
                int z = Integer.parseInt(tokens[2]);
                e = new Entity(room,
                        new TerminalBehavior(),
                        new BoxCollider(),
                        new SpriteRenderer(TextureMap.get("entity_terminal"),2,2));
                e.getPos().set(x,data.heightAt(x,z),z);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "Enemy": // x,z,enemyName
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                String enemyName = tokens[3];
                Enemy enemy = Enemy.valueOf(enemyName);
                e = new Entity(room,
                        new EnemyBehavior(enemy),
                        new BoxCollider(),
                        new SpriteRenderer(TextureMap.get(enemy.spriteName)));
                e.getPos().set(x,data.heightAt(x,z),z);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "Elevator": // x,z,height,startUp
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                int y = data.heightAt(x,z);
                int height = Integer.parseInt(tokens[3]);
                boolean startUp = Boolean.parseBoolean(tokens[4]);
                e = new Entity(room,
                        new ElevatorBehavior(y, height, startUp),
                        new BoxCollider(),
                        new PlatformRenderer(TextureMap.get("entity_elevator"),3,1));
                e.getPos().set(x,y,z);
                e.getSize().set(1,0,1);
                return e;

            case "SolidPlatform": // x,z,y
                e = new Entity(room,
                        new EmptyBehavior(),
                        new BoxCollider(),
                        new PlatformRenderer(TextureMap.get("entity_solidPlatform")));
                e.getPos().set(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[2]));
                e.getSize().set(1,0,1);
                return e;

            case "TogglePlatform": // x,z,y,index,rotation,startInverted
                int index = Integer.parseInt(tokens[4]);
                int rotation = Integer.parseInt(tokens[5]);
                boolean startInverted = Boolean.parseBoolean(tokens[6]);
                e = new Entity(room,
                        new TogglePlatformBehavior(index, rotation, startInverted),
                        new BoxCollider(),
                        new PlatformRenderer(TextureMap.get("entity_togglePlatform"),3,2));
                e.getPos().set(Integer.parseInt(tokens[1]),Integer.parseInt(tokens[3]),Integer.parseInt(tokens[2]));
                e.getSize().set(1,0,1);
                return e;

            case "ToggleButton": // x,z,index
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                index = Integer.parseInt(tokens[3]);
                e = new Entity(room,
                        new ToggleButtonBehavior(index),
                        new BoxCollider(),
                        new PlatformRenderer(TextureMap.get("entity_toggleButton"),2,4));
                e.getPos().set(x,data.heightAt(x,z)+.01f,z);
                e.getSize().set(1,.05f,1);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "StaticObject": // x,z,objName,param
                String objName = tokens[3];
                float param = 0;
                if (tokens.length >= 5) // param is optional
                    param = Float.parseFloat(tokens[4]);
                return constructStaticObject(Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]),objName,param,data,room);

            case "RoomLink": // x,z,width,height,rotation,ROOM_NAME
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                int width = Integer.parseInt(tokens[3]);
                height = Integer.parseInt(tokens[4]);
                rotation = Integer.parseInt(tokens[5]);
                String roomName = tokens[6];
                e = new Entity(room,
                        new RoomLinkBehavior(roomName, rotation),
                        new BoxCollider(),
                        new EmptyRenderer());
                e.getPos().set(x,data.heightAt(x,z),z);
                e.getSize().set(width,1,height);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "Water": // y
                e = new Entity(room,
                        new WaterBehavior(data.width,data.height, Integer.parseInt(tokens[1])),
                        new EmptyCollider(),
                        new PlatformRenderer(TextureMap.get("entity_water")));
                e.getSize().set(data.width,1,data.height);
                return e;

            case "ItemPickup": // x,z,itemName
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                String itemName = tokens[3];
                Pickup pickup;
                if (itemName.startsWith("ITEM_"))
                    pickup = Item.valueOf(itemName);
                else if (itemName.startsWith("SKILL_"))
                    pickup = Skill.valueOf(itemName);
                else if (itemName.startsWith("MOD_"))
                    pickup = Mod.valueOf(itemName);
                else
                    throw new IllegalArgumentException("Invalid pickup ID: "+itemName);
                e = new Entity(room,
                        new ItemPickupBehavior(pickup),
                        new BoxCollider(),
                        new SpriteRenderer(TextureMap.get(pickup.getSprite()),1,1));
                e.getPos().set(x,data.heightAt(x,z),z);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "ScriptTrigger": // x,z,width,height,scriptName
                x = Integer.parseInt(tokens[1]);
                z = Integer.parseInt(tokens[2]);
                width = Integer.parseInt(tokens[3]);
                height = Integer.parseInt(tokens[4]);
                String scriptName = tokens[5];
                e = new Entity(room,
                        new EmptyBehavior(), // TODO code a script behavior
                        new BoxCollider(),
                        new EmptyRenderer());
                e.getPos().set(x,data.heightAt(x,z),z);
                e.getSize().set(width,1,height);
                ((BoxCollider)e.collider).setSolid(false);
                return e;

            case "KeyDoor": // x,z,doorLevel,rotation
            case "LaserGrid": // x,z,width
                System.err.println("Unimplemented entity: "+s);
                return null;

            default:
                System.err.println("Unrecognized entity: "+s);
                return null;
        }
    }

    private static Entity constructStaticObject(float x, float z, String objName, float param, TerrainData data, Room room) {
        float w = 1, h = 1, y = 0;
        Behavior behavior;
        Collider collider;
        Renderer renderer;
        switch(objName)
        {
            case "Barrel":
                behavior = new EmptyBehavior();
                collider = new BoxCollider();
                MeshRenderer r = new MeshRenderer(MeshMap.get("barrel"), TextureMap.get("obj_barrel"));
                renderer = r;
                r.modelScale.set(.65f, .7f, .65f);
                r.modelPos.set(.3f,0,.3f);
                float offset = (x+z*.7f+param)%.6f -.3f;
                r.modelRot.set(0,offset,0);
                y = data.heightAt(x,z)+param;
                x += .2+offset*.5;
                z += .2+offset*.5;
                w = h = .65f;
                break;

            case "Crate":
                behavior = new EmptyBehavior();
                collider = new BoxCollider();
                r = new MeshRenderer(MeshMap.get("cube"), TextureMap.get("obj_crate"));
                renderer = r;
                r.modelScale.set(.4f);
                r.modelPos.set(.5f,.4f,.5f);
                offset = (x+z*.7f+param)%.6f -.3f;
                r.modelRot.set(0,offset,0);
                y = data.heightAt(x,z)+param;
                x += .1+offset*.25;
                z -= .1+offset*.25;
                break;

            case "Fluorescent":
                behavior = new EmptyBehavior();
                collider = new EmptyCollider();
                renderer = new SpriteRenderer(TextureMap.get("obj_fluorescent"));
                break;

            case "Ceiling":
                behavior = new EmptyBehavior();
                collider = new EmptyCollider();
                renderer = new SpriteRenderer(TextureMap.get("obj_ceiling"));
                break;

            case "Chain":
                behavior = new EmptyBehavior();
                collider = new BoxCollider();
                renderer = new SpriteRenderer(TextureMap.get("obj_chain"));
                float length = Math.abs(param);
                if (param > 0) {
                    w = length;
                    h = 0;
                    y = data.heightAt(x,z-1);
                } else {
                    h = length;
                    w = 0;
                    y = data.heightAt(x-1,z);
                }
                break;

            case "Pipe":
                behavior = new EmptyBehavior();
                collider = new BoxCollider();
                r = new MeshRenderer(MeshMap.get("pipe"), TextureMap.get("obj_pipe"));
                renderer = r;
                r.modelPos.set(.2f,1.3f,.2f);
                r.modelRot.set((float)Math.PI/2,param*(float)Math.PI/2,0);
                r.modelScale.set(.32f,.32f,.32f);
                x += .3;
                z += .3;
                w = h = .4f;
                y = data.heightAt(x,z);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized StaticObject: "+objName);
        }

        Entity e = new Entity(room,behavior,collider,renderer);
        e.getPos().set(x,y,z);
        e.getSize().set(w,1,h);
        return e;
    }

    private static String serializeEntity(Entity e) {
        return "not implemented";
    }
}
