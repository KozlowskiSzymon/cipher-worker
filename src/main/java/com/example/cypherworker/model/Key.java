package com.example.cypherworker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_key", schema = "cypher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Key {

  @Id
  @Column(name = "user_id")
  private long userId;

  @Column(name = "private_key")
  private String privateKey;
}
