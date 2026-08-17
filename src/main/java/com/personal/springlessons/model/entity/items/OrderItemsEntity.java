package com.personal.springlessons.model.entity.items;

import java.time.Instant;
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

import org.hibernate.annotations.BatchSize;
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
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private Channel channel;

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "orderItemsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemsEntity> items;

  public OrderItemsEntity() {
    // Required by JPA
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Instant getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
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
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof OrderItemsEntity that))
      return false;
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public String toString() {
    // items excluded to prevent circular toString with ItemsEntity
    return "OrderItemsEntity{" + "id=" + this.id + ", createdAt=" + this.createdAt + ", channel="
        + this.channel + '}';
  }
}
