package com.personal.springlessons.model.entity.items;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.personal.springlessons.model.lov.Channel;
import com.personal.springlessons.util.Constants;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;

@Entity
@DynamicInsert
@DynamicUpdate
@EntityListeners(OrderItemsEntityListener.class)
@Table(name = OrderItemsEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP)
public class OrderItemsEntity {

  public static final String TABLE_NAME = "order_items";

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private Channel channel;

  @OneToMany(mappedBy = "orderItemsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemsEntity> items;

  public OrderItemsEntity() {}

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
    OrderItemsEntity other = (OrderItemsEntity) obj;
    return Objects.equals(this.id, other.id);
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

  public Channel getChannel() {
    return this.channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public List<ItemsEntity> getItems() {
    return this.items;
  }

  public void setItems(List<ItemsEntity> items) {
    this.items = items;
  }

  @Override
  public String toString() {
    return "OrderItemsEntity [id=" + this.id + ", createdAt=" + this.createdAt + ", channel="
        + this.channel + ", items=" + this.items + "]";
  }
}
