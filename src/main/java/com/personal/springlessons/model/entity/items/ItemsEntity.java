package com.personal.springlessons.model.entity.items;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
import com.personal.springlessons.model.entity.orderitems.OrderItemsEntity;
import com.personal.springlessons.util.Constants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SourceType;
import org.hibernate.generator.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@EntityListeners(value = ItemsEntityListener.class)
@Table(name = ItemsEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_SPRING_APP,
        uniqueConstraints = {@UniqueConstraint(columnNames = "barcode")})
public class ItemsEntity {

    public static final String TABLE_NAME = "items";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @CurrentTimestamp(event = EventType.UPDATE)
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "price", precision = Constants.I_VAL_6, scale = Constants.I_VAL_2,
            nullable = false)
    private BigDecimal price;

    @Column(name = "name", nullable = false, length = Constants.I_VAL_100)
    private String name;

    @Column(name = "barcode", nullable = false, updatable = false, length = Constants.I_VAL_50)
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "fk_order_items_id", referencedColumnName = "id")
    @ToString.Exclude
    private OrderItemsEntity orderItemsEntity;
}
