import models.Character;
import models.Weapon;

import java.util.Random;

import static models.Weapon.WeaponType.AXE;
import static models.Weapon.WeaponType.SPEAR;
import static models.Weapon.WeaponType.SWORD;

/**
 * Created by Ryan_Comer on 11/25/2016.
 */
public class Combat {

    private static final double ADVANTAGE_MODIFIER = 0.2;

    Random random = new Random();

    public void attack(Character c1, Character c2){
        int damage = random.nextInt(advantage(c1.equipped, c2.equipped));
        c2.health -= damage;
        System.out.println(c1.name + " attacked " + c2.name + " for " + damage + " HP: (" + c2.health + " HP remaining)");
        if(c2.health <= 0) System.out.println(c1.name + " WON!");
    }



    // Change the damage based on the weapon type (Weapon Triangle)
    private int advantage(Weapon w1, Weapon w2){
        int damageLess = (int)(w1.damage * (1 - ADVANTAGE_MODIFIER));
        int damageMore = (int)(w1.damage * (1 + ADVANTAGE_MODIFIER));
        int damage = w1.damage;
        switch(w1.type){
            case SPEAR:
                if(w2.type == AXE) damage = damageLess;
                else if(w2.type == SWORD) damage = damageMore;
                break;
            case SWORD:
                if(w2.type == SPEAR) damage = damageLess;
                else if(w2.type == AXE) damage = damageMore;
                break;
            case AXE:
                if(w2.type == SWORD) damage = damageLess;
                else if(w2.type == SPEAR) damage = damageMore;
                break;
        }

        return damage;
    }

}
