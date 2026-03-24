package mx.edu.cetys.Software_Quality_Lab.pets;

import jakarta.persistence.*;

@Entity
@Table(name="pets")
public class Pet{
    @Id
    @GeneratedValue()
    private Long id;

    private String name;
    private String color;
    private String race;
    private Integer age;

    public Pet(){
    }

    public Pet(Long id, String name, String color, String race, Integer age) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.race = race;
        this.age = age;
    }

    public Pet(String name, String color, String race, Integer age) {
        this.name = name;
        this.color = color;
        this.race = race;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getRace() {
        return race;
    }

    public Integer getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}