package com.infybuzz.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "beverages")
public class Beverage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "beverage_id")
	private Long beverageId;

	@Column(name = "beverage_name")
	private String beverageName;

	@Column(name = "beverage_cost")
	private Double beverageCost;

	@Enumerated(EnumType.STRING)
	@Column(name = "beverage_type")
	private BeverageType beverageType;

	@Column(name = "availability")
	private Integer availability;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "modifiedAt")
	private LocalDateTime modifiedAt;

	public Beverage(String beverageName, Double beverageCost, BeverageType beverageType,
					Integer availability, LocalDateTime createdAt, LocalDateTime modifiedAt) {
		this.beverageName = beverageName;
		this.beverageCost = beverageCost;
		this.beverageType = beverageType;
		this.availability = availability;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
	}
}
