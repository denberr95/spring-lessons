package com.personal.springlessons.model.entity.revision;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import com.personal.springlessons.util.Constants;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = CustomRevisionEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_HISTORY)
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity {

  protected static final String TABLE_NAME = "revinfo";
  protected static final String SEQUENCE_NAME = "revinfo_seq";
  protected static final String SEQUENCE_GENERATOR_NAME = "revision_seq_gen";

  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = CustomRevisionEntity.SEQUENCE_GENERATOR_NAME)
  @SequenceGenerator(name = CustomRevisionEntity.SEQUENCE_GENERATOR_NAME,
      sequenceName = CustomRevisionEntity.SEQUENCE_NAME, schema = Constants.DB_SCHEMA_HISTORY,
      allocationSize = Constants.I_VAL_50)
  @RevisionNumber
  @Column(name = "rev", nullable = false)
  private int rev;

  @RevisionTimestamp
  @Column(name = "revtstmp")
  private long revtstmp;

  @Column(name = "ip_address", length = Constants.I_VAL_45)
  private String ipAddress;

  @Column(name = "client_id", length = Constants.I_VAL_255)
  private String clientId;

  @Column(name = "username", length = Constants.I_VAL_255)
  private String username;

  @Column(name = "request_uri")
  private String requestUri;

  @Column(name = "http_method", length = Constants.I_VAL_20)
  private String httpMethod;
}
