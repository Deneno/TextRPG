import java.util.ArrayList;
import java.util.Random;
//https://stepik.org/lesson/90695/step/10?unit=66923
class Item{
    int price, weight, type;
    public Item(int price, int weight, int type){
        this.price = price;
        this.weight = weight;
        this.type = type;
    }
}

class Enemy{
    int hp, damage;
    int exp;
    public Enemy(int hp, int damage, int exp){
        this.hp = hp;
        this.damage = damage;
        this.exp = exp;
    }
}

abstract class Hero{

    protected String nickName;
    protected int s, a, i, exp, hp, mana; // сила ловкость интеллект опыт здоровье мана
    protected int x, y; // координаты героя на карте
    protected ArrayList<Item> items = new ArrayList<>(); // список поднятых предметов
    protected int damage; // урон

    public int getMana(){
        return mana;
    }
    public int getDamage(){
        return damage;
    }
    public int getHp(){
        return hp;
    }
    public void goToCursor(int xx, int yy){
        x = xx; y = yy;
    }

    public abstract void attack(Enemy enemy); // этот метод должнен быть определен в классах наследниках

    public abstract void defense(Enemy enemy);// этот метод должнен быть определен в классах наследниках

    public void openItem(Item item){
        if (new Random().nextInt(100) >= 50) items.add(item);
        // метод должен добавлять предмет в список с вероятностью 50 %
        // для осуществления вероятностных процессов можно использовать случайное число от 0 до 100.
    }
}

class Warrior extends Hero{
    int itemBonusDamage;

    public Warrior(){
        this.s=100;
        this.a=50;
        this.i=1;
        this.hp =500;
        this.mana = 10;
        this.damage = 150;
    }
    @Override
    public void openItem(Item item){
        if (new Random().nextInt(100) >= 50) items.add(item);
        if (item.type == 1) itemBonusDamage = 100;
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.hp -= (this.damage+itemBonusDamage);
        if (enemy.hp <= 0) {
            enemy.hp =0;
            this.exp += enemy.exp;
            this.isNewLevel();
        }
    }

    @Override
    public void defense(Enemy enemy) {
        if (enemy.hp > 0 ) {
            this.hp -= enemy.damage;
            if (this.hp <= 0) {
                this.hp = 0;
                System.out.println("Ваш герой был убит");
            }
        }
    }

    public void isNewLevel() {
        if (this.exp >= 500) {
            int n = this.exp/500;
            i+=n;
            this.exp -= n*500;
            this.s+=n*10;
            this.a+=n*10;
            this.i+=n*10;
            this.hp +=n*200;
            this.mana += n*10;
            this.damage += n*20;
        }
    }

}

class Archer extends Hero{
    public Archer(){
        this.s=20;
        this.a=150;
        this.i=30;
        this.hp =200;
        this.mana = 50;
        this.damage = 200;
    }
    @Override
    public void openItem(Item item){
        items.add(item);
    }

    @Override
    public void attack(Enemy enemy) {
        enemy.hp -= this.damage;
        if (enemy.hp <= 0) {
            enemy.hp =0;
            this.exp += enemy.exp;
            this.isNewLevel();
        }
    }

    @Override
    public void defense(Enemy enemy) {
        if (enemy.hp > 0 ) {
            if (new Random().nextInt(100) >= 70) {
                this.hp -= enemy.damage;
                if (this.hp <= 0) {
                    this.hp = 0;
                    System.out.println("Ваш герой был убит");
                }
            }
        }
    }

    public void isNewLevel() {
        if (this.exp >= 500) {
            int n = this.exp/500;
            i+=n;
            this.exp -= n*500;
            this.s += n*10;
            this.a += n*30;
            this.i += n*10;
            this.hp += n*50;
            this.mana += n*10;
            this.damage += n*50;
        }
    }
}

class Magician extends Hero{
    private ArrayList<Item> casts = new ArrayList<>();
    public Magician() {
        this.s=5;
        this.a=30;
        this.i=30;
        this.hp =100;
        this.mana = 5000;
        this.damage = 40;
    }

    public void makeCast(Enemy enemy){
        if (casts.size() > 0 && this.mana >= 100) {
            enemy.hp -= 1000;
            this.mana -= 100;
            casts.remove(casts.size()-1);
        }
    }

    public void eduCast(Item item){
        if (item.type == 2) casts.add(item);
    }
    @Override
    public void openItem(Item item){
        if (new Random().nextInt(100) >= 50) items.add(item);
        this.eduCast(item);
    }

    @Override
    public void attack(Enemy enemy) {
        if (enemy.hp <= damage || casts.size() == 0 || this.mana < 100) {
            enemy.hp -= this.damage;
        } else {
            enemy.hp -= 1000;
            this.mana -= 100;
            casts.remove(0);
        }

        if (enemy.hp <= 0) {
            enemy.hp =0;
            this.exp += enemy.exp;
            this.isNewLevel();
        }
    }

    @Override
    public void defense(Enemy enemy) {
        if (enemy.hp > 0 ) {
            if (this.mana >= enemy.damage) this.mana -= enemy.damage;
            else if (this.mana > 0 && this.mana < enemy.damage) {
                int buf = enemy.damage - this.mana;
                this.mana = 0;
                this.hp -= buf;
            } else this.hp -= enemy.damage;
            if (this.hp <= 0) {
                this.hp = 0;
                System.out.println("Ваш герой был убит");
            }
        }
    }

    public void isNewLevel() {
        if (this.exp >= 500) {
            int n = this.exp/500;
            i+=n;
            this.exp -= n*500;
            this.s+=n*10;
            this.a+=n*10;
            this.i+=n*10;
            this.hp +=n*30;
            this.mana += n*1000;
            this.damage += n*10;
        }
    }


}
public class Main{
    public static void main(String[] args){
        Hero myHero = new Warrior();
        //    Hero myHero = new Archer();
        //    Hero myHero = new Magician();

        ArrayList<Enemy> enemies = new ArrayList<>();
        enemies.add(new Enemy(30, 20, 1200)); // 1 - Пряничный человек
        enemies.add(new Enemy(2018, 1, 350)); // 2 - Туча комаров
        enemies.add(new Enemy(280, 50, 3050)); // 3 - Вор
        enemies.add(new Enemy(100500, 100500, 100500)); // 4 - Гальватрон

        //найден клад
        for (int i = 0; i < 3; i++){
            Item item = new Item(0, 0, i % 3); // i%3 - будет задавать предмету тип 0, 1, 2
            myHero.openItem(item);  // герои получает предмет в руки
        }
System.out.println("Всего собрано: "+myHero.items.size());

        // бой!
        int count = 1;
        for (Enemy enemy : enemies){
            System.out.println( count++ + "-й бой:");
            System.out.println("  Герои { hp=" + myHero.hp + " mana=" + myHero.mana + " }");
            System.out.println("  Враг { hp=" + enemy.hp + " }");
            int counter = 1;
            while (myHero.hp > 0 & enemy.hp > 0){
                System.out.println( counter++ + " раунд:");
                myHero.attack(enemy);
                myHero.defense(enemy);
                System.out.println("    Герои { hp=" + myHero.hp + " mana=" + myHero.mana + " }");
                System.out.println("    Враг { hp=" + enemy.hp + " }");
            }
            System.out.println(myHero.hp > 0 ? "Победа, герой получил опыт "+ enemy.exp : "Поражение");
System.out.printf("Герой { HP=%d, MANNA=%d, damage=%d, exp=%d }\n", myHero.hp, myHero.mana, myHero.damage, myHero.exp);
        }
    }
}