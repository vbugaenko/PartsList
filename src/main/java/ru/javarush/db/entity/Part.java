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
  private int id;
  private String title;
  private int amount;
  private Category category;
  private long price;

  public int getId()                         { return id;       }
  public String getTitle()                   { return title;    }
  public int getAmount()                     { return amount;   }
  public Category getCategory()              { return category; }
  public long getPrice()                     { return price;    }

  public void setId(int id)                  { this.id = id;             }
  public void setTitle(String title)         { this.title = title;       }
  public void setAmount(int amount)          { this.amount = amount;     }
  public void setCategory(Category category) { this.category = category; }
  public void setPrice(long price)           { this.price = price;       }

  @Override
  public String toString() {
    return "Part{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", amount=" + amount +
            ", category=" + category +
            ", price=" + price +
            '}';
  }
}
