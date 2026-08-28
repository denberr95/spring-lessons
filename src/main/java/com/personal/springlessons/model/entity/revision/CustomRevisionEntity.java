package com.personal.springlessons.model.entity.revision;

import java.util.Objects;

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
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = CustomRevisionEntity.TABLE_NAME, schema = Constants.DB_SCHEMA_HISTORY)
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity {

  protected static final String TABLE_NAME = "revinfo";
  protected static final String SEQUENCE_NAME = "revinfo_seq";
  protected static final String SEQUENCE_GENERATOR_NAME = "revision_seq_gen";

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
  private @Nullable String ipAddress;

  @Column(name = "client_id", length = Constants.I_VAL_255)
  private @Nullable String clientId;

  @Column(name = "username", length = Constants.I_VAL_255)
  private @Nullable String username;

  @Column(name = "request_uri")
  private @Nullable String requestUri;

  @Column(name = "http_method", length = Constants.I_VAL_20)
  private @Nullable String httpMethod;

  public CustomRevisionEntity() {
    // Required by JPA and Hibernate Envers
  }

  public int getRev() {
    return this.rev;
  }

  public void setRev(int rev) {
    this.rev = rev;
  }

  public long getRevtstmp() {
    return this.revtstmp;
  }

  public void setRevtstmp(long revtstmp) {
    this.revtstmp = revtstmp;
  }

  public @Nullable String getIpAddress() {
    return this.ipAddress;
  }

  public void setIpAddress(@Nullable String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public @Nullable String getClientId() {
    return this.clientId;
  }

  public void setClientId(@Nullable String clientId) {
    this.clientId = clientId;
  }

  public @Nullable String getUsername() {
    return this.username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public @Nullable String getRequestUri() {
    return this.requestUri;
  }

  public void setRequestUri(@Nullable String requestUri) {
    this.requestUri = requestUri;
  }

  public @Nullable String getHttpMethod() {
    return this.httpMethod;
  }

  public void setHttpMethod(@Nullable String httpMethod) {
    this.httpMethod = httpMethod;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof CustomRevisionEntity that))
      return false;
    return this.rev == that.rev;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rev);
  }

  @Override
  public String toString() {
    return "CustomRevisionEntity{" + "rev=" + this.rev + ", revtstmp=" + this.revtstmp
        + ", ipAddress='" + this.ipAddress + '\'' + ", clientId='" + this.clientId + '\''
        + ", username='" + this.username + '\'' + ", requestUri='" + this.requestUri + '\''
        + ", httpMethod='" + this.httpMethod + '\'' + '}';
  }
}
