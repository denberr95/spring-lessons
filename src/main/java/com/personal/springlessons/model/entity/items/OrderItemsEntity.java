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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
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
  @EqualsAndHashCode.Include
  private UUID id;

  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private Channel channel;

  @ToString.Exclude
  @OneToMany(mappedBy = "orderItemsEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemsEntity> items;
}
