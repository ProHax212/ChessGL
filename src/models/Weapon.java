package models;

/**
 * Created by Ryan_Comer on 11/25/2016.
 */
public class Weapon {

    public String name;

    public WeaponType type;
    public int damage;

    public Weapon(String name, WeaponType type, int damage){
        this.type = type;
        this.damage = damage;
    }

    public enum WeaponType{
        SWORD,
        AXE,
        SPEAR
    }

}
