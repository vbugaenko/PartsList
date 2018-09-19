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
  private boolean selected;
  private int amount;


  public int getId()                         { return id;                }
  public String getTitle()                   { return title;             }
  public boolean isSelected()                { return selected;          }
  public int getAmount()                     { return amount;            }

  public void setId(int id)                  { this.id = id;             }
  public void setTitle(String title)         { this.title = title;       }
  public void setSelected(boolean selected)  { this.selected = selected; }
  public void setAmount(int amount)          { this.amount = amount;     }

  @Override
  public String toString() {
    return "Part{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", amount=" + amount +
            '}';
  }
}
