package nullset.battle.fighters;

import mote4.scenegraph.Window;
import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.mesh.Mesh;
import mote4.util.vertex.mesh.MeshMap;
import nullset.battle.Battle;
import nullset.battle.actions.AttackAction;
import nullset.battle.Enemy;
import nullset.main.RootLayer;

import static nullset.main.Vars.SCENE_H;
import static nullset.main.Vars.UI_SCALE;
import static nullset.rpg.AttribEnums.Element.PHYS;
import static nullset.rpg.AttribEnums.Stat.HP;
import static nullset.rpg.AttribEnums.Stat.STR;

public class EnemyFighter extends Fighter {

    private Enemy enemy;
    private int index;
    private Mesh text;

    public EnemyFighter(Enemy e, int i) {
        super(e.stats);
        enemy = e;
        index = i;
        FontUtils.useMetric("font_ui");
        text = FontUtils.createString(getName(),0,0,UI_SCALE,UI_SCALE);
    }

    @Override
    public void takeTurn(Battle battle) {
        Fighter target = Battle.getCurrent().getAllPlayers().get(0);
        battle.addAction(new AttackAction(this,target,enemy.stats.base.get(STR),PHYS,true));
    }

    @Override
    public void update() {
        text.destroy();
        FontUtils.useMetric("font_ui");
        text = FontUtils.createString(getName()+"\nHP: "+hp+"/"+stats.base.get(HP),0,0,UI_SCALE,UI_SCALE);
    }

    @Override
    public void render(int slot, TransformationMatrix matrix) {
        matrix.push();
        {
            matrix.translate(RootLayer.getInstance().getSceneWidth()/2+128*(slot-2), SCENE_H/2);
            matrix.scale(64, 64, 1);
            matrix.bind();

            int frame = (int) (Window.time() * 6 + slot) % 4; // TODO currently hardcoded animation frames for testing
            Uniform.vec("spriteInfo", 4, 1, frame);
            TextureMap.bind(enemy.spriteName);
            MeshMap.render("quad");

            matrix.scale(1f/64, 1f/64, 1);
            matrix.translate(-32,64);
            matrix.bind();

            Uniform.vec("spriteInfo", 1,1,0);
            TextureMap.bind("font_ui");
            text.render();
        }
        matrix.pop();
    }

    @Override
    public String getName() {
        return enemy.enemyName +" "+(char)('A'+index);
    }

    @Override
    public void destroy() {
        text.destroy();
    }
}
