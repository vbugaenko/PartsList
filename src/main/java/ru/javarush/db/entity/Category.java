package ru.javarush.db.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @author Victor Bugaenko
 * @since 18.09.2018
 */

@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @Column(name = "id")
    private int id;
    private String title;
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Part> parts;

    public int getId()           { return id;    }
    public String getTitle()     { return title; }
    public List<Part> getParts() { return parts; }

    public void setId(int id)               { this.id = id;       }
    public void setTitle(String title)      { this.title = title; }
    public void setParts(List<Part> parts)  { this.parts = parts; }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parts=" + parts +
                '}';
    }
}
