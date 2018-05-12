package nullset.battle.actions;

import nullset.battle.fighters.Fighter;
import nullset.rpg.AttribEnums.Element;
import nullset.scenes.UIScene;

import static nullset.rpg.AttribEnums.Stat.STR;

public class AttackAction implements Action {

    private Fighter attacker, target;
    private double skillPower, baseAtk;
    private Element element;
    private boolean isPhysical;

    public AttackAction(Fighter attacker, Fighter target, double skillPower, Element element, boolean isPhysical) {
        this.attacker = attacker;
        this.target = target;
        this.skillPower = skillPower;
        this.baseAtk = attacker.getStats().base.getOrDefault(STR,0);
        this.element = element;
        this.isPhysical = isPhysical;
    }

    @Override
    public void start() {
        UIScene.getBattleUI().log(attacker.getName()+" attacks "+target.getName()+"!");
        target.damage(skillPower, baseAtk, element, isPhysical);
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isDone() {
        return true;
    }
}
