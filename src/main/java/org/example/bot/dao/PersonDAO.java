package org.example.bot.dao;

import org.example.bot.Object.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDAO {

    List<Person> people;

    {
        people = new ArrayList<>();
        people.add(new Person(1, "Peter Fridje"));
        people.add(new Person(2, "Egor Shishko"));
    }

}
