package main;

public abstract class Human {
    String name;
    Human(String name) {
        this.name = name;
    }
    abstract void sayHello();
}

public class Chinese extends Human{
    Chinese(String name) {
        super(name);
    }
    void sayHello() {
        System.out.println("你好");
    }
}
public class Spaniard extends Human {
    Spaniard(String name) {
        super(name);
    }
    void sayHello() {
        System.out.println("Hola");
    }
}
public class Italian extends Human {
    Italian(String name) {
        super(name);
    }
    void sayHello() {
        System.out.println("Ciao");
    }
}

public class Hello {
    public static void main(String args[]) {
        Chinese A = new Chinese("韩令帅");
        Spaniard B = new Spaniard("fskhf");
        Italian C = new Italian("jhjhjh");
        A.sayHello();
        B.sayHello();
        C.sayHello();

    }
}