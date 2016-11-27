package models;

/**
 * Created by Ryan_Comer on 11/25/2016.
 */
public class Character {

    public String name;
    public int health;
    public int level;

    public Stats stats;

    public Weapon[] weapons;
    public Weapon equipped;

    public boolean alive = true;

    public Character(String name, Weapon[] weapons, int health, Stats stats){
        this.weapons = weapons;
        this.equipped = weapons[0];
        this.health = health;
        this.name = name;
        this.stats = stats;
    }

}
