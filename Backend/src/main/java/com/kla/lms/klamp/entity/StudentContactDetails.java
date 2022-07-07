package com.kla.lms.klamp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contactDetails", schema = "klamp", indexes = {
		@Index(name = "scd_idx_1", columnList = "id", unique = true),
		@Index(name = "scd_idx_2", columnList = "status", unique = false)})
@lombok.Data
@lombok.EqualsAndHashCode(callSuper = false)
public class StudentContactDetails extends AuditEntity{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
    private long id;
	
	@Column(name = "dateofEntry", nullable = false)
	private String dateofEntry;//DD-MM-YYYY
	
	//Permanent Address as well as communication address
	@Column(name="paddressType", nullable = false)
	private String paddressType;//Permanent/Communication Address
	
	@Column(name="paddress", nullable = false)
	private String paddress;//it is text area where detail address will store as String
	
	@Column(name="pdistrict", nullable = false)
	private String pdistrict;
	
	@Column(name="pstate", nullable = false)
	private String pstate;
	
	@Column(name="ppinCode", nullable = false)
	private String ppinCode;//it is strictly 6 digits
	
	@Column(name="addressType", nullable = false)
	private String addressType;//Permanent/Communication Address
	
	//if permanent address is same as communication address it will be autofill in screen
	
	@Column(name="isPASameAsCA", nullable = false)
	private String isPASameAsCA;//Yes or No
	

	@Column(name="caddress", nullable = false)
	private String caddress;//it is text area where detail address will store as String
	
	@Column(name="cdistrict", nullable = false)
	private String cdistrict;
	
	@Column(name="cstate", nullable = false)
	private String cstate;
	
	@Column(name="cpinCode", nullable = false)
	private String cpinCode;//it is strictly 6 digits
	
	//Communication data
    @Column(name="mobileNumber", nullable = false)
	private String mobileNumber;//Number
	
	@Column(name="alternateMobileNumber")//Number
	private String alternateMobileNumber;
	
	@Column(name="landlineNumber", nullable = false)
	private String landlineNumber;
	
	@Column(name="emailId", nullable = false)
	private String emailId;//Alphanumeric
	
	@Column(name = "status", nullable = false)
	private int status;
	
	@Transient
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private long userId;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private StudentProfile studentProfile;

	public long getUserId() {
		if (studentProfile != null) {
			return studentProfile.getUserId();
		}
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}