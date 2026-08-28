package com.personal.springlessons.model.entity.items;

import java.time.Instant;
import java.util.List;
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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.jspecify.annotations.Nullable;

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
  private @Nullable UUID id;

  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false, updatable = false)
  private @Nullable Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private @Nullable Channel channel;

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "orderItemsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private @Nullable List<ItemsEntity> items;

  public OrderItemsEntity() {
    // Required by JPA
  }

  public @Nullable UUID getId() {
    return this.id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public @Nullable Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(@Nullable Instant createdAt) {
    this.createdAt = createdAt;
  }

  public @Nullable Channel getChannel() {
    return this.channel;
  }

  public void setChannel(@Nullable Channel channel) {
    this.channel = channel;
  }

  public @Nullable List<ItemsEntity> getItems() {
    return this.items;
  }

  public void setItems(@Nullable List<ItemsEntity> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof OrderItemsEntity that))
      return false;
    return this.id != null && this.id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public String toString() {
    // items excluded to prevent circular toString with ItemsEntity
    return "OrderItemsEntity{" + "id=" + this.id + ", createdAt=" + this.createdAt + ", channel="
        + this.channel + '}';
  }
}
