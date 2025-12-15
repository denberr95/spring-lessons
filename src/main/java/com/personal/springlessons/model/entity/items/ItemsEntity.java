package com.personal.springlessons.model.entity.items;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import com.personal.springlessons.util.Constants;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

@Entity
@DynamicInsert
@DynamicUpdate
@EntityListeners(ItemsEntityListener.class)
@Table(name = ItemsEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP,
    uniqueConstraints = @UniqueConstraint(columnNames = "barcode"))
public class ItemsEntity {

  public static final String TABLE_NAME = "items";

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @CurrentTimestamp(source = SourceType.DB, event = EventType.INSERT)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "price", precision = Constants.I_VAL_6, scale = Constants.I_VAL_2,
      nullable = false)
  private BigDecimal price;

  @Column(name = "name", nullable = false, length = Constants.I_VAL_100)
  private String name;

  @Column(name = "barcode", nullable = false, updatable = false, length = Constants.I_VAL_50)
  private String barcode;

  @ManyToOne
  @JoinColumn(name = "fk_order_items_id", referencedColumnName = "id", nullable = false)
  private OrderItemsEntity orderItemsEntity;

  public ItemsEntity(UUID id, OffsetDateTime createdAt, BigDecimal price, String name,
      String barcode, OrderItemsEntity orderItemsEntity) {
    this.id = id;
    this.createdAt = createdAt;
    this.price = price;
    this.name = name;
    this.barcode = barcode;
    this.orderItemsEntity = orderItemsEntity;
  }

  public ItemsEntity() {}

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || this.getClass() != obj.getClass())
      return false;
    ItemsEntity other = (ItemsEntity) obj;
    return Objects.equals(this.id, other.id);
  }

  @Override
  public String toString() {
    return "ItemsEntity [id=" + this.id + ", createdAt=" + this.createdAt + ", price=" + this.price
        + ", name=" + this.name + ", barcode=" + this.barcode + "]";
  }

  public static String getTableName() {
    return TABLE_NAME;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OffsetDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public BigDecimal getPrice() {
    return this.price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBarcode() {
    return this.barcode;
  }

  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }

  public OrderItemsEntity getOrderItemsEntity() {
    return this.orderItemsEntity;
  }

  public void setOrderItemsEntity(OrderItemsEntity orderItemsEntity) {
    this.orderItemsEntity = orderItemsEntity;
  }
}
