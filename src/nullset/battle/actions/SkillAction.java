package nullset.battle.actions;

import nullset.battle.fighters.Fighter;
import nullset.rpg.Skill;
import nullset.scenes.UIScene;

public class SkillAction implements Action {

    private Skill skill;
    private Fighter[] targets;

    public SkillAction(Skill skill, Fighter... targets) {
        this.skill = skill;
        this.targets = targets;
    }

    @Override
    public void start() {
        UIScene.getBattleUI().log("Skill used: "+skill.skillName);
        skill.useInBattle(targets);
    }

    @Override
    public void act() {

    }

    @Override
    public boolean isDone() {
        return true;
    }
}
