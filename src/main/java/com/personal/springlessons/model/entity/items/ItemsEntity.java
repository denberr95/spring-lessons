package com.personal.springlessons.model.entity.items;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.personal.springlessons.util.Constants;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;

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
@EntityListeners(ItemsEntityListener.class)
@Table(name = ItemsEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP)
public class ItemsEntity {

  protected static final String TABLE_NAME = "items";

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  @EqualsAndHashCode.Include
  private UUID id;

  @CurrentTimestamp(source = SourceType.DB, event = EventType.INSERT)
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "price", precision = Constants.I_VAL_6, scale = Constants.I_VAL_2,
      nullable = false)
  private BigDecimal price;

  @Column(name = "name", nullable = false, length = Constants.I_VAL_100)
  private String name;

  @Column(name = "barcode", nullable = false, updatable = false, length = Constants.I_VAL_50,
      unique = true)
  private String barcode;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "order_items_id", referencedColumnName = "id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_order_items_id"))
  private OrderItemsEntity orderItemsEntity;
}
