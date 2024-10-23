package main;

public class GreetingApp {
    public static void main(String[] args) {
        Human[] humans = new Human[3];
        humans[0] = new Chinese("韩令帅");
        humans[1] = new Spaniard("fskhf");
        humans[2] = new Italian("jhjhjh");

        for (Human human : humans) {
            human.sayHello();
        }
    }
}

interface Human {
    void sayHello();
}

class Chinese implements Human {
    String name;

    Chinese(String name) {
        this.name = name;
    }

    @Override
    public void sayHello() {
        System.out.println("你好, " + name);
    }
}

class Spaniard implements Human {
    String name;

    Spaniard(String name) {
        this.name = name;
    }

    @Override
    public void sayHello() {
        System.out.println("Hola, " + name);
    }
}

class Italian implements Human {
    String name;

    Italian(String name) {
        this.name = name;
    }

    @Override
    public void sayHello() {
        System.out.println("Ciao, " + name);
    }
}
