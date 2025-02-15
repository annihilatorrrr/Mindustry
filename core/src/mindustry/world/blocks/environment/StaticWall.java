package mindustry.world.blocks.environment;

import arc.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class StaticWall extends Prop{
    public @Load("@-large") TextureRegion large;
    public TextureRegion[][] split;

    public StaticWall(String name){
        super(name);
        breakable = alwaysReplace = unitMoveBreakable = false;
        solid = true;
        variants = 2;
        cacheLayer = CacheLayer.walls;
        allowRectanglePlacement = true;
        placeEffect = Fx.rotateBlock;
        instantBuild = true;
        ignoreBuildDarkness = true;
        placeableLiquid = true;
    }

    @Override
    public void drawBase(Tile tile){
        int rx = tile.x / 2 * 2;
        int ry = tile.y / 2 * 2;

        if(Core.atlas.isFound(large) && eq(rx, ry) && Mathf.randomSeed(Point2.pack(rx, ry)) < 0.5 && split.length >= 2 && split[0].length >= 2){
            Draw.rect(split[tile.x % 2][1 - tile.y % 2], tile.worldx(), tile.worldy());
        }else if(variants > 0){
            Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], tile.worldx(), tile.worldy());
        }else{
            Draw.rect(region, tile.worldx(), tile.worldy());
        }

        //draw ore on top
        if(tile.overlay().wallOre){
            tile.overlay().drawBase(tile);
        }
    }

    @Override
    public void load(){
        super.load();
        int size = large.width / 2;
        split = large.split(size, size);
        if(split != null){
            for(var arr : split){
                for(var reg : arr){
                    reg.scale = region.scale;
                }
            }
        }
    }

    @Override
    public boolean canReplace(Block other){
        return other instanceof StaticWall || super.canReplace(other);
    }

    boolean eq(int rx, int ry){
        return rx < world.width() - 1 && ry < world.height() - 1
            && world.tile(rx + 1, ry).block() == this
            && world.tile(rx, ry + 1).block() == this
            && world.tile(rx, ry).block() == this
            && world.tile(rx + 1, ry + 1).block() == this;
    }
}
