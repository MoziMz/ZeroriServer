package com.mozi.moziserver.model.entity;

import com.mozi.moziserver.model.mappedenum.UserAuthType;
import com.mozi.moziserver.model.mappedenum.UserRoleType;
import com.mozi.moziserver.model.mappedenum.UserRoleTypeConverter;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user_auth")
public class UserAuth extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private UserAuthType type;

    private String id;

    private String pw;

    @Convert(converter = UserRoleTypeConverter.class)
    private List<UserRoleType> roleList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private com.mozi.moziserver.model.entity.User user;
}
