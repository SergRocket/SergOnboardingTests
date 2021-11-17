package Base;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public class BuilderComparator implements Comparable<BuilderComparator> {
    private String name;
    private int age;
    /*
     CompareToBuilder class of the Apache Commons Lang library.
      to assist in implementing the Comparable.compareTo(Object) methods
     */

    public void Car(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public int compareTo(BuilderComparator builder)
    {
        return new CompareToBuilder()
                .append(this.age, builder.getAge())
                .append(this.name, builder.getName())
                .toComparison();
    }
}
