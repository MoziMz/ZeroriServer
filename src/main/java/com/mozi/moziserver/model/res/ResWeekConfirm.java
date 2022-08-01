package com.mozi.moziserver.model.res;

import com.mozi.moziserver.model.entity.Confirm;
import com.mozi.moziserver.model.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ResWeekConfirm {

    private Integer userCnt;

    private Integer confirmCnt;

    private ResWeekConfirm(List<User> userList, List<Confirm> confirmList)
    {
        this.userCnt= userList.size();

        this.confirmCnt=confirmList.size();
    }

    public static ResWeekConfirm of(List<User> userList, List<Confirm> confirmList) { return new ResWeekConfirm(userList,confirmList); }


}
