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

    private Character c1, c2;    // The characters involved in combat
    private int d1, d2; // Damage for each character
    private Random r = new Random();

    // Constructor - set the characters
    public Combat(Character c1, Character c2){
        this.c1 = c1; this.c2 = c2;
        this.d1 = r.nextInt(advantage(c1, c2));
        this.d2 = r.nextInt(advantage(c2, c1));
    }

    // Character 1 attacks character 2
    public void attack(){

        // Character 1 attacks
        c2.health -= d1;

        // Debug printing
        System.out.println(c1.name + " attacked " + c2.name + " for " + d1 + " HP: (" + c2.health + " HP remaining)");
        if(c2.health <= 0){
            System.out.println(c1.name + " WON!");
            return;
        }

        // Character 2 counter attacks
        if(c2.health > 0){
            c1.health -= d2;
            System.out.println(c2.name + " attacked " + c1.name + " for " + d2 + " HP: (" + c1.health + " HP remaining)");
        }

        // Debug printing
        if(c1.health <= 0){
            System.out.println(c2.name + " WON!");
        }
    }

    // Change the damage based on the weapon type (Weapon Triangle)
    private int advantage(Character c1, Character c2){

        // Weapons for each character
        Weapon w1=c1.equipped, w2=c2.equipped;

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
