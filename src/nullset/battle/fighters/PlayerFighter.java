package nullset.battle.fighters;

import mote4.util.matrix.TransformationMatrix;
import mote4.util.shader.Uniform;
import mote4.util.texture.TextureMap;
import mote4.util.vertex.FontUtils;
import mote4.util.vertex.mesh.Mesh;
import nullset.battle.Battle;
import nullset.battle.FighterStats;
import nullset.battle.actions.PlayerChoiceAction;
import nullset.main.RootLayer;
import nullset.ui.UIBorderBuilder;

import static nullset.main.Vars.SCENE_H;
import static nullset.main.Vars.UI_SCALE;
import static nullset.rpg.AttribEnums.Stat.HP;

public class PlayerFighter extends Fighter {

    private static Mesh border;
    static {
        border = UIBorderBuilder.build(0,0,100,50);
    }

    //////////////

    private Mesh text;

    public PlayerFighter() {
        super(FighterStats.getPlayerStats());
        FontUtils.useMetric("font_6px");
        text = FontUtils.createString(getName(),0,0,UI_SCALE,UI_SCALE);
    }

    @Override
    public void takeTurn(Battle battle) {
        battle.addAction(PlayerChoiceAction.getInstance());
    }

    @Override
    public void update() {
        text.destroy();
        FontUtils.useMetric("font_6px");
        text = FontUtils.createString(getName()+"\nHP: "+hp+"/"+stats.base.get(HP),0,0,UI_SCALE,UI_SCALE);
    }

    @Override
    public void render(int slot, TransformationMatrix matrix) {
        matrix.push();
        {
            matrix.translate(RootLayer.getInstance().getSceneWidth()/2-50, SCENE_H-60);
            matrix.bind();

            Uniform.vec("spriteInfo", 1, 1, 0);
            TextureMap.bind("ui_scalablemenu");
            border.render();
            TextureMap.bind("font_6px");
            text.render();
        }
        matrix.pop();
    }

    @Override
    public String getName() {
        return "Player";
    }

    @Override
    public void destroy() {
        text.destroy();
    }
}
