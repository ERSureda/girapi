package com.girlocal.girapi.identity.infrastructure.adapter.out.persistence.dynamodb.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@NoArgsConstructor @AllArgsConstructor
public class SessionEntity {

    public static final String PK_PREFIX = "SESSION#";
    public static final String SK_META = "META";
    public static final String GSI_USER_ID = "GSI_USER_ID";

    private String pk;
    private String sk;
    private String userId;
    private String role;
    private Long ttl;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }

    @DynamoDbAttribute("userId")
    @DynamoDbSecondaryPartitionKey(indexNames = GSI_USER_ID)
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @DynamoDbAttribute("role")
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @DynamoDbAttribute("ttl")
    public Long getTtl() { return ttl; }
    public void setTtl(Long ttl) { this.ttl = ttl; }
}
