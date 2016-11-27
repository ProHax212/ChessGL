package models;

/**
 * Created by Ryan_Comer on 11/25/2016.
 */
public class Character {

    public Weapon[] weapons;
    public Weapon equipped;
    public int health;
    public String name;
    public boolean alive = true;

    public Character(Weapon[] weapons, int health, String name){
        this.weapons = weapons;
        this.equipped = weapons[0];
        this.health = health;
        this.name = name;
    }

}
