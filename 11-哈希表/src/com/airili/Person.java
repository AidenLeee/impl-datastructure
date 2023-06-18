package com.airili;

import java.util.Objects;

/**
 * @program: data_structure
 * @description:
 * @author: Airili
 * @create: 2021-03-09 13:48
 **/
public class Person {
    private int age;
    private String name;
    private float height;

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                Float.compare(person.height, height) == 0 &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name, height);
    }
}
