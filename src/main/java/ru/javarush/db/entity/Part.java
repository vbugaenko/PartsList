package ru.javarush.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Victor Bugaenko
 * @since 17.09.2018
 */

@Entity
@Table(name = "parts")
public class Part
{
  @Id
  @Column(name = "id")
  public int id;
  public String title;
  private int type;
  public int amount;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "Part{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", type=" + type +
            ", amount=" + amount +
            '}';
  }
}
